package com.starschina.sdk.demo.common;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.elvishew.xlog.XLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.starschina.sdk.player.ThinkoEnvironment;

public class CIBNMainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Fresco.initialize(this.getApplicationContext());

		XLog.i("sdk setup");
		ThinkoEnvironment.setUp(getApplicationContext());
		
		setContentView(R.layout.activity_cibn_main);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("demo", "onDestroy");
		//sdk释放
		XLog.i("sdk tearDown");
		ThinkoEnvironment.tearDown();
	}

	public void toLivePage(View v) {
		Intent i = new Intent(this, LiveActivity.class);
		startActivity(i);
	}

	public void toVodPage(View v) {
		Intent i = new Intent(this, VodActivity.class);
		startActivity(i);
	}
}
