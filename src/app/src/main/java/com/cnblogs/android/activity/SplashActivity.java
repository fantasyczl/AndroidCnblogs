package com.cnblogs.android.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.cnblogs.android.R;

public class SplashActivity extends BaseActivity{

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
				WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable(){
			public void run() {
				redirectMainActivity();
			}
		}, 1500);
	}

	private void redirectMainActivity(){
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}
}
