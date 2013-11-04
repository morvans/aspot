package net.zone84.aspot.desktop.media;

import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;

public class ThumbnailFilteringListener extends MediaListenerAdapter {

	public static final double SECONDS_BETWEEN_FRAMES = 5;

	public static final long MICRO_SECONDS_BETWEEN_FRAMES = (long) (Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);

	private MovieThumbnailFactory filter;

	private static long mLastPtsWrite = Global.NO_PTS;

	private int mVideoStreamIndex = -1;

	public ThumbnailFilteringListener(MovieThumbnailFactory filter) {
		this.filter = filter;
	}

	public void onVideoPicture(IVideoPictureEvent event) {
		try {
			// if the stream index does not match the selected stream index,
			// then have a closer look

			if (event.getStreamIndex() != mVideoStreamIndex) {
				// if the selected video stream id is not yet set, go ahead an
				// select this lucky video stream

				if (-1 == mVideoStreamIndex)
					mVideoStreamIndex = event.getStreamIndex();

				// otherwise return, no need to show frames from this video
				// stream

				else
					return;
			}

			// if uninitialized, backdate mLastPtsWrite so we get the very
			// first frame

			if (mLastPtsWrite == Global.NO_PTS)
				mLastPtsWrite = event.getTimeStamp()
						- MICRO_SECONDS_BETWEEN_FRAMES;

			// if it's time to write the next frame

			if (event.getTimeStamp() - mLastPtsWrite >= MICRO_SECONDS_BETWEEN_FRAMES) {

				// write out image
				filter.writeThumnail(event.getImage());

				
				mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
