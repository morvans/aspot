package net.zone84.aspot.desktop;

import java.awt.image.BufferedImage;
import java.io.File;

public class FileMove {

	public enum FileMoveStatus {PENDING, MOVED, TARGET_EXISTS};
	
	private File source;
	
	private BufferedImage thumbnail;
	
	private BufferedImage existingThumbnail;
	
	private File destinationPath;
	
	private FileMoveStatus status;
	

	public FileMove(File source, File destinationPath, BufferedImage thumbnail) {
		super();
		status = FileMoveStatus.PENDING;
		this.source = source;
		this.destinationPath = destinationPath;
		this.thumbnail = thumbnail;
	}
	
	public File getDestinationPath() {
		return destinationPath;
	}
	
	public File getSource() {
		return source;
	}
	
	public BufferedImage getThumbnail() {
		return thumbnail;
	}
	
	
	
	public void setThumbnail(BufferedImage thumbnail) {
		this.thumbnail = thumbnail;
	}

	public BufferedImage getExistingThumbnail() {
		return existingThumbnail;
	}
	
	public void setExistingThumbnail(BufferedImage existingThumbnail) {
		this.existingThumbnail = existingThumbnail;
	}
	
	@Override
	public String toString() {
		return source.getName() + " ("+source.getParent()+" => "+destinationPath.getAbsolutePath()+")";
	}

	public void setStatus(FileMoveStatus status) {
		this.status = status;
	}

	public FileMoveStatus getStatus() {
		return status;
	}
}
