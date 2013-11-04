package net.zone84.aspot.desktop.media;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class MovieThumbnailFactory {

	private boolean thumbnailWritten;

	private Integer width, height;

	private BufferedImage scaledImage;
	
	public BufferedImage makeThumbnail(String videoFile, int thumbWidth, int thumbHeight) {

		IMediaReader reader = ToolFactory.makeReader(videoFile);

		// stipulate that we want BufferedImages created in BGR 24bit color
		// space
		reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

		// note that DecodeAndCaptureFrames is derived from
		// MediaReader.ListenerAdapter and thus may be added as a listener
		// to the MediaReader. DecodeAndCaptureFrames implements
		// onVideoPicture().

		reader.addListener(new ThumbnailFilteringListener(this));

		// read out the contents of the media file, note that nothing else
		// happens here. action happens in the onVideoPicture() method
		// which is called when complete video pictures are extracted from
		// the media source

		thumbnailWritten = false;

		while (reader.readPacket() == null && !thumbnailWritten)
			do {
			} while (false);

		return scaledImage;
		
	}

	public void writeThumnail(BufferedImage image) throws Exception {

		thumbnailWritten = true;

		// Create new (blank) image of required (scaled) size

		BufferedImage scaledImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		// Paint scaled version of image to new image

		Graphics2D graphics2D = scaledImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, width, height, null);

		// clean up

		graphics2D.dispose();

		

	}

}
