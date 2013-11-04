package net.zone84.aspot.desktop.importer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zone84.aspot.desktop.App;
import net.zone84.aspot.desktop.FileMove;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

public class Scanner implements Runnable {

	private List<File> unsupported = new ArrayList<File>();

	private File destinationPath;
	private File sourcePath;

	private FileMoverModel model;

	private Set<ScannerListener> listeners = new HashSet<ScannerListener>();

	public Scanner(File destinationPath, FileMoverModel model, File sourcePath) {
		this.destinationPath = destinationPath;
		this.model = model;
		this.sourcePath = sourcePath;
	}

	private List<FileMove> handleDirectory(File file) {
		if (!file.isDirectory()) {
			throw new RuntimeException("Not a directory : " + file.getPath());
		}

		List<FileMove> moves = new ArrayList<FileMove>();

		for (File child : file.listFiles()) {

			if (child.isDirectory()) {
				List<FileMove> subList = handleDirectory(child);
				moves.addAll(subList);
			} else {

				BufferedImage thumbnail = null;

				MimeType type = MimeTypes.getDefaultMimeTypes().getMimeType(
						child);
				// System.out.println(child + "--> " + type + " ("
				// + type.getType().getBaseType().getType() + ")");

				Date date = null;

				if (type.getName().equals("image/jpeg")) {

					Metadata metadata;
					try {
						metadata = JpegMetadataReader.readMetadata(child);
					} catch (JpegProcessingException e) {
						throw new RuntimeException("JPEG processing failed: "
								+ child.getName(), e);
					}

					Directory directory = metadata
							.getDirectory(ExifDirectory.class);

					try {
						App.debugTag(directory, ExifDirectory.TAG_DATETIME);
						App.debugTag(directory,
								ExifDirectory.TAG_DATETIME_DIGITIZED);
						App.debugTag(directory,
								ExifDirectory.TAG_DATETIME_ORIGINAL);
					} catch (Exception ex) {

					}
					try {
						if (directory
								.containsTag(ExifDirectory.TAG_DATETIME_ORIGINAL)) {
							date = directory
									.getDate(ExifDirectory.TAG_DATETIME_ORIGINAL);
						}
					} catch (MetadataException e) {
						throw new RuntimeException("Can't get EXIF date", e);
					}

					// System.out.println("--->" + date);

					/*try {
						thumbnail = PictureThumbnailFactory.createThumbnail(
								child.getAbsolutePath(), 150, 75);
					} catch (Exception ex) {
					}*/

				} else if (type.getType().getBaseType().getType()
						.equals("video")
						|| type.getType().getBaseType().getType()
								.equals("image")) {

					date = new Date(child.lastModified());
					// System.out.println("--->" + date);

					/*try {
						thumbnail = (new MovieThumbnailFactory()).makeThumbnail(
								child.getAbsolutePath(), 150, 75);
					} catch (Exception ex) {
					}*/
					
				} else if (type.getName().equals("application/octet-stream")) {
					// ignore
				} else {

					System.err.println(child + "---> unsupported (" + type
							+ ")");
					unsupported.add(child);

				}

				if (date != null) {

					DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
					FileMove move = new FileMove(child, new File(
							destinationPath, format.format(date)), thumbnail);

					moves.add(move);

					model.addMove(move);
				}

			}
		}

		return moves;
	}

	@Override
	public void run() {
		handleDirectory(sourcePath);
		fireScanEnd();
	}

	public void addScannerListener(ScannerListener listener) {
		listeners.add(listener);
	}
	
	private void fireScanEnd() {
		for(ScannerListener listener : listeners){
			listener.scanEnded(new ScannerEvent(this));
		}
	}

}
