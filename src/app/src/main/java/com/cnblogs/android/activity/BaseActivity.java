package com.cnblogs.android.activity;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * 基类，大部分Activity继承自此类
 * @author walkingp
 * @date:2011-11
 *
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	/**
	 * 横竖屏
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (!SettingActivity.getIsAutoHorizontal(this))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		else
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
	}
	protected void onPause() {
		super.onPause();
	}

}
