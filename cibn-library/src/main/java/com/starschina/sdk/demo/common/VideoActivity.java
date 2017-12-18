package com.starschina.sdk.demo.common;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.starschina.abs.media.ThinkoPlayerListener;
import com.starschina.sdk.demo.common.Epg.EpgEntity;
import com.starschina.sdk.demo.common.Epg.EpgFragment;
import com.starschina.sdk.demo.common.menu.LiveReviewMenu;
import com.starschina.sdk.player.StarsChinaAd;
import com.starschina.sdk.player.ThinkoPlayerView;
import com.starschina.types.DChannel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class VideoActivity extends FragmentActivity{

	private ThinkoPlayerView mPlayerView;
	private LoadingView mLoadingView;
	private VideoCtrlView mCtrlView;
	private LiveReviewMenu mLiveReviewMenu;
	private StarsChinaAd mAd;
	
	private DChannel mChannel;
	
	private Handler mHandler;

	private boolean mOnPrepared = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Log.d("demo", "[onCreate]");

		EventBus.getDefault().register(this);

		//Settings.System.putInt(getContentResolver(),
		//		Settings.System.ACCELEROMETER_ROTATION, 1);
		initOrientationListener();
		
		setContentView(R.layout.activity_player);
		
		mChannel = (DChannel) getIntent().getSerializableExtra("channel");
		Log.d("demo", "ch:"+mChannel.toString());

		initPlayer();
		addBannerAd();
		//只有直播才去加载节目单
		if (mChannel.type == 1) {
			addEpg();
		}
	}

	private void initPlayer() {
		mPlayerView = (ThinkoPlayerView) findViewById(R.id.player);

		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int screenWidth = metric.widthPixels;
		int screenHeight = metric.heightPixels;
		if (screenWidth > screenHeight) {
			LayoutParams lp22 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mPlayerView.setLayoutParams(lp22);
		}
//		mPlayerView = new ThinkoPlayerView(this);
//		LayoutParams lp22 = new LayoutParams(
//				dip2px(this, 480), dip2px(this, 240));
//		LayoutParams lp22 = new LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		mPlayerView.setLayoutParams(lp22);

		mPlayerView.setBackgroundResource(R.color.black);
//		((RelativeLayout)findViewById(R.id.player)).addView(mPlayerView);
		mPlayerView.setPlayerListener(mListener);
		//是否硬解,(开硬解，可解决偶尔绿屏)
		//mPlayerView.useMediaCodec(true);
		//设置播放器参数
//		Map<Integer, Map<String, Long>> options = new HashMap<>();
//		Map<String, Long> option_1 = new HashMap<>();
//		option_1.put("skip_loop_filter", (long) -16);
//		option_1.put("skip_idct", (long) -16);
//		options.put(IjkMediaPlayer.OPT_CATEGORY_CODEC, option_1);
//		Map<String, Long> option_2 = new HashMap<>();
//		option_2.put("mediacodec", (long) 0);
//		option_2.put("framedrop", (long) 1);
//		options.put(IjkMediaPlayer.OPT_CATEGORY_PLAYER, option_2);
//		mPlayerView.setPlayerOptions(options);

		//自定义loading view
		mLoadingView = new LoadingView(this);
		mPlayerView.setLoadingView(mLoadingView);
		//自定义播控界面
		mCtrlView = new VideoCtrlView(this, mPlayerView);
		mPlayerView.setMediaCtrlView(mCtrlView);
		mCtrlView.setTitle(mChannel.name);
		mCtrlView.setVisibility(View.INVISIBLE);
		mCtrlView.setChannel(mChannel);
		//直播
		if (mChannel.type == 1 && mChannel.currentEpg != null) {
			mCtrlView.setEpgTime(mChannel.currentEpg.startTime, mChannel.currentEpg.endTime);
		}
	}

	private void addBannerAd() {
		RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.ad_container);
		mAd = new StarsChinaAd(this);
		mAd.setOrientation(0);//0:竖屏， 1：横屏
		mAd.addBannerAd(adContainer, mChannel.id+"");
	}

	private void addEpg() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.epg_container, EpgFragment.newInstance(mChannel, false));
		transaction.commit();
	}

	private void showLiveReview() {
		Log.d("demo", "[showLiveReview]");
		long currentMil = mPlayerView != null ? mPlayerView.isPlaying() ? (long) mPlayerView.getCurrentPosition() *1000 : 0 : 0;
		int epgId = mChannel.currentEpg != null ? mChannel.currentEpg.id : -1;
		Log.d("demo", "[showLiveReview] currentMil:"+currentMil+", epgId:"+epgId);
		if (mLiveReviewMenu == null) {
			mLiveReviewMenu = new LiveReviewMenu(this, mChannel, currentMil, epgId, true);
			RelativeLayout container = (RelativeLayout) findViewById(R.id.review_container);
			container.addView(mLiveReviewMenu.getRoot());
		}else {
			mLiveReviewMenu.setData(mChannel, currentMil, epgId, true);
		}

		mLiveReviewMenu.show();
	}

	int epgstarttime = 0;
	int epgendtime = 0;

	@Subscribe
	public void onEventFromPlayer(SimpleEvent event) {
		switch (event.mType) {
			//回看
			case EventConst.EVENT_LIVE_SEEK_BY_EPG:
				EpgEntity epg = (EpgEntity) event.mObj;
				if (epg.getStartTime() > System.currentTimeMillis()/1000) {
					Toast.makeText(this, "只能回看已播放了的节目!", Toast.LENGTH_SHORT).show();
					return;
				}
				epgstarttime = epg.getStartTime();
				epgendtime = epg.getEndTime();
				//回看要重新加载播放中广告
				//mPlayerView.ifShowPlayingAd(true);
				mPlayerView.seekByEpg(epg.getStartTime(), true);
				mCtrlView.refreshPlayerStatus(false);
				mCtrlView.setEpgTime(epg.getStartTime(), epg.getEndTime());
				mCtrlView.showToLive();
				if (mLiveReviewMenu != null) {
					mLiveReviewMenu.hide();
					mCtrlView.setVisibility(View.VISIBLE);
				}
				break;
			//时移
			case EventConst.EVENT_LIVE_SEEK:
				//时移不用重新加载播放中广告
				//mPlayerView.ifShowPlayingAd(false);
				Log.d("demo", "seek time:"+event.mObj);
				mPlayerView.seekByEpg((Integer) event.mObj, false);
				mCtrlView.refreshPlayerStatus(false);
				mCtrlView.showToLive();
				break;
			case EventConst.EVENT_UPDATE_EPG:
				EpgEntity curEpg = (EpgEntity) event.mObj;
				mCtrlView.setEpgTime(curEpg.getStartTime(), curEpg.getEndTime());
				break;
			case EventConst.EVENT_EXIT:
				finish();
				break;
			//回到直播
			case EventConst.EVENT_TO_LIVE:
				mPlayerView.toLive();
				mCtrlView.setEpgTime(mChannel.currentEpg.startTime, mChannel.currentEpg.endTime);
				break;
			case EventConst.EVENT_PORTRAIT:
				portrait();
				break;
			case EventConst.EVENT_LANDSCAPE:
				landscape(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				break;
			case EventConst.EVENT_SHOW_LIVE_REVIEW:
				showLiveReview();
				mCtrlView.setVisibility(View.GONE);
				break;
			case EventConst.EVENT_UPDATE_NEXT_EPG:
				EpgEntity next = (EpgEntity) event.mObj;
				mChannel.nextEpg.id = next.getEpgId();
				mChannel.nextEpg.name = next.getEpgName();
				mChannel.nextEpg.startTime = next.getStartTime();
				mChannel.nextEpg.endTime = next.getEndTime();
				break;
			case EventConst.EVENT_SHOW_NEXT_EPG:
				mCtrlView.setEpgTime(mChannel.nextEpg.startTime, mChannel.nextEpg.endTime);
				break;
			case EventConst.EVENT_SWITCH_CHANNEL:
				DChannel channel = new DChannel();
				channel.id = 267138;
				channel.name = "cctv12";
				channel.type = 1;
				mPlayerView.stop();
				mPlayerView.prepareToPlay(channel);
				mCtrlView.setTitle(channel.name);
				break;
			//test
//			case 1020:
//				epgstarttime += 10;
//				mPlayerView.seekByEpg(epgstarttime);
//				mCtrlView.refreshPlayerStatus(false);
//				mCtrlView.setEpgTime(epgstarttime, epgendtime);
//				break;
		}
	}
	
	public void reload(){
		mPlayerView.stop();
		mPlayerView.prepareToPlay(mChannel);
	}
	
	public void landscape(int orientation){
		isPortrait = false;

		setRequestedOrientation(orientation);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPlayerView.setLayoutParams(lp);

		if (mChannel.type ==  1) {
			mCtrlView.ifShowReviewButton(true);
		}
		mAd.setOrientation(1);//0:竖屏， 1：横屏
		//findViewById(R.id.ad_container).setVisibility(View.GONE);
	}
	
	public void portrait(){
		isPortrait = true;

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, dip2px(this, 240));
		mPlayerView.setLayoutParams(lp);

		if (mChannel.type ==  1) {
			mCtrlView.ifShowReviewButton(false);
		}
		mAd.setOrientation(0);//0:竖屏， 1：横屏
		//findViewById(R.id.ad_container).setVisibility(View.VISIBLE);

		if (mLiveReviewMenu != null && mLiveReviewMenu.isShown()) {
			mLiveReviewMenu.hide();
			mCtrlView.setVisibility(View.VISIBLE);
		}
	}


	public void toLive(View v) {
		mPlayerView.toLive();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("demo", "[onResume]");
		mOrientationListener.enable();
		Log.d("demo", "sdk.prepareToPlay");
		mPlayerView.prepareToPlay(mChannel);
//		mPlayerView.prepareToPlay((int) mChannel.id, mChannel.name);
	}

	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("demo", "[onPause]");
		mOrientationListener.disable();
		mCtrlView.release();
		
		mPlayerView.stop();
	}

	@Override
	protected void onStop() {
		Log.d("demo", "[onStop] 0");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("demo", "[onDestroy]");
		mAd.release();
		mPlayerView.release();

		EventBus.getDefault().unregister(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(mPlayerView.onKeyDown(keyCode, event)){
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mLiveReviewMenu !=null && mLiveReviewMenu.isShown()) {
				mLiveReviewMenu.hide();
				mCtrlView.setVisibility(View.VISIBLE);
				return true;
			}

			if (!isPortrait) {
				portrait();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("demo", "[onTouchEvent] event:"+event.getAction());
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.d("demo", "[onTouchEvent]");
			if (mLiveReviewMenu !=null && mLiveReviewMenu.isShown()) {
				mLiveReviewMenu.hide();
				mCtrlView.setVisibility(View.VISIBLE);
				return false;
			}
			if (mPlayerView.isPlaying()) {
				if (mCtrlView.isShown()) {
					mCtrlView.setVisibility(View.GONE);
				}else {
					mCtrlView.setVisibility(View.VISIBLE);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	private ThinkoPlayerListener mListener = new ThinkoPlayerListener() {
		
		@Override
		public void onPrepared() {
			// TODO Auto-generated method stub
			Log.i("demo", "onPrepared");
			mCtrlView.refreshPlayerStatus(true);
			mOnPrepared = true;
		}
		
		@Override
		public void onNetworkSpeedUpdate(int arg0) {
			// TODO Auto-generated method stub
			Log.i("demo", "onNetworkSpeedUpdate");
		}
		
		@Override
		public boolean onInfo(int arg0, int arg1) {
			// TODO Auto-generated method stub
			Log.i("demo", "onInfo:arg0:"+arg0);
			if (mOnPrepared) {
				if (arg0 == 701) {
					mCtrlView.refreshPlayerStatus(false);
				}else if (arg0 == 702) {
					mCtrlView.refreshPlayerStatus(true);
				}
			}
			return false;
		}
		
		@Override
		public boolean onError(int arg0, int arg1) {
			// TODO Auto-generated method stub
			Log.e("demo", "onError[arg0:"+arg0+",arg1:"+arg1+"]");
            Toast.makeText(VideoActivity.this, "player error["+arg0+","+arg1+"]",
                    Toast.LENGTH_SHORT).show();
			return false;
		}
		
		@Override
		public void onCompletion() {
			// TODO Auto-generated method stub
			Log.i("demo", "onCompletion");
		}
		
		@Override
		public void onBuffer(int arg0) {
			// TODO Auto-generated method stub
			Log.i("demo", "onBuffer:"+arg0);
		}
	};
	
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	
	private void requestedOrientation(int orientation){
		setRequestedOrientation(orientation);
	}
	
	boolean isPortrait = true;
	OrientationEventListener mOrientationListener = null;
	
	private void initOrientationListener() {
		mOrientationListener = new OrientationEventListener(this,
				SensorManager.SENSOR_DELAY_NORMAL) {
			
			@Override
			public void onOrientationChanged(int orientation) {
				// TODO Auto-generated method stub
				if (orientation > 225 && orientation < 315) {
	                if (isPortrait) {
	                	Log.d("demo", "切换成横屏");
	                	requestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	                	landscape(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	                }
	            } else if (orientation > 45 && orientation < 135) {
					if (isPortrait) {
						Log.d("demo", "切换成横屏");
						requestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
						landscape(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					}
				} else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
	                if (!isPortrait) {
	                	Log.d("demo", "切换成竖屏");
	                	requestedOrientation(1);
	                	portrait();
	                }
	            }
			}
		};
	}
}
