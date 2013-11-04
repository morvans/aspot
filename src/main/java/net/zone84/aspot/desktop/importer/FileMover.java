package net.zone84.aspot.desktop.importer;

import java.io.File;
import java.util.List;

import net.zone84.aspot.desktop.FileMove;
import net.zone84.aspot.desktop.FileMove.FileMoveStatus;
import net.zone84.aspot.desktop.media.PictureThumbnailFactory;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;

public class FileMover implements Runnable {

	private FileMoverModel model;

	private boolean running;

	public FileMover(FileMoverModel model) {
		this.model = model;
	}

	@Override
	public void run() {

		if (running) {
			throw new IllegalStateException("Already running");
		}
		running = true;

		List<FileMove> moves = model.getMoves();

		for (int i = 0; i < moves.size(); i++) {

			FileMove move = moves.get(i);

			if (!move.getSource().isFile()) {
				throw new IllegalStateException("Source is not a regular file");
			}

			if (!move.getDestinationPath().exists()) {
				move.getDestinationPath().mkdirs();
			} else if (!move.getDestinationPath().isDirectory()) {
				throw new IllegalStateException(
						"Destination is not a directory");
			}

			File newFile = new File(move.getDestinationPath(), move.getSource()
					.getName());

			check(move);
			
			
			if (newFile.exists()) {
				System.err.println(newFile.getAbsolutePath()
						+ " already exists, skipping");
				move.setStatus(FileMoveStatus.TARGET_EXISTS);
			} else {

				boolean success = move.getSource().renameTo(
						new File(move.getDestinationPath(), move.getSource()
								.getName()));

				System.out.println("Move " + (i + 1) + "/" + moves.size()
						+ " : " + success);
				
				move.setStatus(FileMoveStatus.MOVED);

			}

			
			model.refresh(move);
		}

	}

	private void check(FileMove move) {

		File newFile = new File(move.getDestinationPath(), move.getSource()
				.getName());

		if (newFile.exists()) {

			MimeType type = MimeTypes.getDefaultMimeTypes()
					.getMimeType(newFile);

			if (type.getName().equals("image/jpeg")) {
				try {
					move.setThumbnail(PictureThumbnailFactory.createThumbnail(
							move.getSource().getAbsolutePath(), 150, 75));					
					move.setExistingThumbnail(PictureThumbnailFactory.createThumbnail(
							newFile.getAbsolutePath(), 150, 75));
				} catch (Exception ex) {

				}

			}
		}

	}

}
