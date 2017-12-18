package com.starschina.sdk.demo.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.starschina.sdk.abs.media.ThinkoPlayerCtrlView;
import com.starschina.sdk.player.ThinkoPlayerView;
import com.starschina.types.DChannel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VideoCtrlView extends ThinkoPlayerCtrlView {

	private static final String TAG = "VideoCtrlView";
	private Context mContext;

	private Handler mVideoSeekHandler = null;
	private VideoProcessUpdater mProcessUpdater;

	@BindView(R2.id.title) TextView mTitleView;
	@BindView(R2.id.zoom) TextView mZoom;
	@BindView(R2.id.tolive) TextView mToLive;
	@BindView(R2.id.start_pos) TextView mStartPos;
	@BindView(R2.id.end_pos) TextView mEndPos;
	@BindView(R2.id.play_pause) ImageView mPauseBt;
	@BindView(R2.id.seekbar_video) SeekBar mVideoSeekbar;
	@BindView(R2.id.review) TextView mVideoReview;
	@BindView(R2.id.aspect_ratio) TextView mAspectRatio;

	private int mVideoSeekProgress;
	private long mStartTime;
	private long mEndTime;

	private ThinkoPlayerView mPlayerView;

	private DChannel mChannel;
	
	private boolean isFullScreen = false;
	
	public VideoCtrlView(Context act, ThinkoPlayerView playerView){
		super(act);
		mContext = act;
		mVideoSeekHandler = new Handler();

		mProcessUpdater = new VideoProcessUpdater();

		mPlayerView = playerView;
		initVideoCtrlView();
	}

	private void initVideoCtrlView(){
		View layout = View.inflate(mContext, R.layout.layout_videoctrl, this);
		ButterKnife.bind(this, layout);

		mVideoSeekbar.setOnSeekBarChangeListener(mViedoSeekBarListener);
	}

	public void setTitle(String title){
		mTitleView.setText(title);
	}

	public void setChannel(DChannel channel) {
		mChannel = channel;
	}

	public void setEpgTime(long startTime, long endTime) {
		mStartTime = startTime * 1000;
		mEndTime = endTime * 1000;
//		mStartPos.setText(DateUtils.getHourAndMin(0));
//		mEndPos.setText(DateUtils.getHourAndMin(mEndTime-mStartTime));
		mStartPos.setText(formatTime(0));
		mEndPos.setText(formatTime(mEndTime-mStartTime));
		int max = (int) ((mEndTime-mStartTime)/1000);
		mVideoSeekbar.setMax(max);
		mVideoSeekProgress = 0;

		startPos = 0;
	}

	public void refreshPlayerStatus(boolean isplaying){
		//mVideoSeekHandler.removeCallbacks(mVideoSeekRunnable);
		if (isplaying) {
			if (mPauseBt != null) {
				mPauseBt.setImageResource(R.drawable.player_pause);
			}

			//直播
//			if (mChannel.type == 1) {
//				mVideoSeekHandler.post(mVideoSeekRunnable);
//			}else if (mChannel.type == 0){//点播
//				mEndTime = 0;
//				mVideoSeekHandler.post(mVodRefreshRunnable);
//			}

			mProcessUpdater.start();

		} else {
			//直播
//			if (mChannel.type == 1) {
//				mVideoSeekHandler.removeCallbacks(mVideoSeekRunnable);
//			}else if (mChannel.type == 0){//点播
//				mVideoSeekHandler.removeCallbacks(mVodRefreshRunnable);
//			}

			mProcessUpdater.stop();

			if (mPauseBt != null) {
				mPauseBt.setImageResource(R.drawable.player_play);
			}
		}
	}

	public void showToLive() {
		mToLive.setVisibility(VISIBLE);
	}

	public void ifShowReviewButton(boolean show) {
		if (show) {
			mVideoReview.setVisibility(VISIBLE);
		}else {
			mVideoReview.setVisibility(GONE);
		}
	}

	public void release() {
		if (mVideoSeekHandler != null) {
			mVideoSeekHandler.removeCallbacksAndMessages(null);
		}
		mProcessUpdater.stop();
	}

	@OnClick(R2.id.btn_exit)
	public void playerExit() {
		EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_EXIT));
	}

	@OnClick(R2.id.tolive)
	public void toLive() {
		mProcessUpdater.stop();
		mToLive.setVisibility(GONE);
		EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_TO_LIVE));
		//mContext.startActivity(new Intent(mContext, VodActivity.class));
	}

	@OnClick(R2.id.zoom)
	public void zoom() {
		if(isFullScreen){
			isFullScreen = false;
			mZoom.setText("全屏");
			//mAspectRatio.setVisibility(GONE);
			EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_PORTRAIT));
		}else {
			isFullScreen = true;
			mZoom.setText("竖屏");
			//mAspectRatio.setVisibility(VISIBLE);
			EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_LANDSCAPE));
		}
	}


	int retioIndex = 0;
	@OnClick(R2.id.aspect_ratio)
	public void aspectRatio() {
		retioIndex = (retioIndex++) < 3 ? retioIndex : 0;
		Log.d(TAG, "retioIndex:"+retioIndex);
		mPlayerView.setAspectRatio(retioIndex);
	}

	@OnClick(R2.id.review)
	public void vidoeReview() {
		EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_SHOW_LIVE_REVIEW));
	}

	@OnClick(R2.id.play_pause)
	public void pauseOrStart() {
		if (mPlayerView.isPlaying()) {
			mPlayerView.pause();
			refreshPlayerStatus(false);
		}else {
			mPlayerView.start();
			refreshPlayerStatus(true);
		}
	}

	@OnClick(R2.id.switch_vod)
	public void switch_channel() {
		EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_SWITCH_CHANNEL));
	}

	private SeekBar.OnSeekBarChangeListener mViedoSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
		int seek_pos;
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
			seek_pos = progress;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			if (mChannel.type == 0){//点播
				//mVideoSeekHandler.removeCallbacks(mVodRefreshRunnable);
				mProcessUpdater.stop();
			}
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (mChannel.type == 1) {//直播
				startPos = 0;
				mVideoSeekProgress = seekBar.getProgress();
				long playTime = mStartTime + seek_pos * 1000;
				Log.d("demo", "seekBar.onStopTrackingTouch playTime:"+playTime);
				long seekTime = System.currentTimeMillis() - playTime;
				Log.d("demo", "seekBar.onStopTrackingTouch seekTime:"+seekTime);
				if (seekTime < 0){
					mVideoSeekbar.setProgress(mVideoSeekProgress);
					Toast.makeText(mContext, "只能回看已播放了的节目!", Toast.LENGTH_SHORT).show();
					return;
				}

				EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_LIVE_SEEK, (int)(playTime/1000)));
				//mVideoSeekHandler.removeCallbacks(mVideoSeekRunnable);
				mProcessUpdater.stop();

			}else if (mChannel.type == 0) {//点播
				final long newposition = seek_pos * mEndTime / 100L;
				seekTo((int) newposition);
				start();
				//mVideoSeekHandler.post(mVodRefreshRunnable);
				mProcessUpdater.start();
			}

		}
	};

	private final class VideoProcessUpdater extends Handler {

		public void start() {
			sendEmptyMessage(0);
		}

		public void stop() {
			removeMessages(0);
		}

		@Override
		public void handleMessage(Message msg) {
			if (mChannel.type == 1) {
				updateLiveProgress();
			}else {
				updateVodProgress();
			}
			sendEmptyMessageDelayed(0 , 1000);
		}
	}

	private void updateLiveProgress() {
		mVideoSeekProgress = (int) (mPlayerView.getCurrentPosition() - mStartTime/1000);
		mVideoSeekbar.setProgress(mVideoSeekProgress);
		mVideoSeekbar.setSecondaryProgress((int) (System.currentTimeMillis()/1000 - mStartTime/1000));

		startPos = startPos > 0 ? startPos+1000 : ((long) mPlayerView.getCurrentPosition()*1000 - mStartTime);
		if ((long)mPlayerView.getCurrentPosition()*1000 >= mEndTime) {
			startPos = 0;
			EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_SHOW_NEXT_EPG));
		}
		Log.d(TAG, "seek progress:"+mVideoSeekProgress);
		mStartPos.setText(formatTime(startPos));
	}

	private void updateVodProgress() {
		if (mPauseBt != null) {
			if (isPlaying()) {
				mPauseBt.setImageResource(R.drawable.player_pause);
			} else {
				mPauseBt.setImageResource(R.drawable.player_play);
			}
		}

		mStartTime = getCurrentPosition();
		mStartPos.setText(formatTime(mStartTime));
		if (mEndTime <= 0) {
			mEndTime = getDuration();
		}
		mEndPos.setText(formatTime(mEndTime));

		if (mStartTime > 0 && mEndTime > 0 && mVideoSeekbar != null) {
			mVideoSeekbar.setProgress((int) (mStartTime * 100 / mEndTime));
		}
	}

	long startPos = 0;
	private Runnable mVideoSeekRunnable = new Runnable() {
		@Override
		public void run() {
			mVideoSeekProgress = (int) (mPlayerView.getCurrentPosition() - mStartTime/1000);
			mVideoSeekbar.setProgress(mVideoSeekProgress);
			mVideoSeekbar.setSecondaryProgress((int) (System.currentTimeMillis()/1000 - mStartTime/1000));

			if((long)mPlayerView.getCurrentPosition()*1000>=mEndTime){
//				android.util.Log.d("slamb", "----------controller调用 changeEpg--------------");
//				changeEpg(fingEpgByTime((long)mPlayerView.getCurrentPosition()*1000));
//				EventBus.getDefault().post(new EventMessage<>((long)mPlayerView.getCurrentPosition()*1000,VideoContainerFragment.CURRENT_EPG_CHANGE));
//				EventBus.getDefault().post(new EventMessage(VideoContainerFragment.UPDATE_CURRENT_EPG));
			}

			startPos = startPos > 0 ? startPos+1000 : ((long) mPlayerView.getCurrentPosition()*1000 - mStartTime);
			if ((long)mPlayerView.getCurrentPosition()*1000 >= mEndTime) {
				startPos = 0;
				EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_SHOW_NEXT_EPG));
			}
			mStartPos.setText(formatTime(startPos));

			//test
//			if (startPos > 10*1000) {
//				EventBus.getDefault().post(new SimpleEvent(1020));
//			}

			mVideoSeekHandler.postDelayed(this, 1000);
		}
	};

	private Runnable mVodRefreshRunnable = new Runnable() {

		@Override
		public void run() {
			if (mPauseBt != null) {
				if (isPlaying()) {
					mPauseBt.setImageResource(R.drawable.player_pause);
				} else {
					mPauseBt.setImageResource(R.drawable.player_play);
				}
			}

			mStartTime = getCurrentPosition();
			mStartPos.setText(formatTime(mStartTime));
			if (mEndTime <= 0) {
				mEndTime = getDuration();
			}
			mEndPos.setText(formatTime(mEndTime));

			if (mStartTime > 0 && mEndTime > 0 && mVideoSeekbar != null) {
				mVideoSeekbar.setProgress((int) (mStartTime * 100 / mEndTime));
			}

			mVideoSeekHandler.postDelayed(this, 1000);
		}
	};

	private String formatTime(long duration) {
		String result = null;
		duration = duration / 1000;
		long h = duration / (60 * 60);
		long m = (duration % 3600) / 60;
		long s = duration % 3600 % 60;

		StringBuilder sb = new StringBuilder();
		String zero = "0";
		if (h > 0) {
			if (h < 10) {
				sb.append(zero).append(Long.toString(h));
			} else {
				sb.append(Long.toString(h));
			}
			sb.append(":");
		}
		if (m < 10) {
			sb.append(zero).append(Long.toString(m));
		} else {
			sb.append(Long.toString(m));
		}
		sb.append(":");
		if (s < 10) {
			sb.append(zero).append(Long.toString(s));
		} else {
			sb.append(Long.toString(s));
		}
		result = sb.toString();

		return result;
	}
}
