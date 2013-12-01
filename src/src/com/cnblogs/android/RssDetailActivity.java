package com.cnblogs.android;

import java.io.InputStream;

import com.cnblogs.android.core.Config;
import com.cnblogs.android.utility.AppUtil;
import com.cnblogs.android.utility.NetHelper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector.OnGestureListener;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ������������
 * 
 * @author walkingp
 * @date 2012-3
 */
public class RssDetailActivity extends BaseActivity
		implements
			OnGestureListener {
	private String blogTitle;// ����
	private String blogAuthor;// ����
	private String blogDate;// ����ʱ��
	private String blogUrl;// ��������
	private String blogContent;// ��������

	static final int MENU_FORMAT_HTML = Menu.FIRST;// ��ʽ���Ķ�
	static final int MENU_READ_MODE = Menu.FIRST + 1;// �л��Ķ�ģʽ

	final String mimeType = "text/html";
	final String encoding = "utf-8";

	private Button blog_button_back;// ����
	WebView webView;
	ProgressBar blogBody_progressBar;
	RelativeLayout rl_blog_detail;// ͷ������

	boolean isFullScreen = false;// �Ƿ�ȫ��

	private GestureDetector gestureScanner;// ����

	Resources res;// ��Դ
	SharedPreferences sharePreferencesSettings;// ����
	TextView tvSeekBar;// SeekBar��ʾ�ı���
	SeekBar seekBar;// SeekBar
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ֹ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.setContentView(R.layout.rss_detail);

		res = this.getResources();
		sharePreferencesSettings = getSharedPreferences(
				res.getString(R.string.preferences_key), MODE_PRIVATE);

		InitialData();
		MarkAsReaded();
	}
	/**
	 * ������ݿ�
	 */
	private void MarkAsReaded() {
	}
	/**
	 * ��ʼ��
	 */
	private void InitialData() {
		// ���ݹ�����ֵ
		blogTitle = getIntent().getStringExtra("title");
		blogAuthor = getIntent().getStringExtra("author");
		blogDate = getIntent().getStringExtra("date");
		blogUrl = getIntent().getStringExtra("link");
		blogContent = getIntent().getStringExtra("content");

		TextView txtAppTitle = (TextView) findViewById(R.id.txtAppTitle);
		txtAppTitle.setText(blogTitle);
		// ͷ��
		rl_blog_detail = (RelativeLayout) findViewById(R.id.rl_blog_detail);
		// ˫��ȫ��
		rl_blog_detail.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureScanner.onTouchEvent(event);
			}
		});
		// ����
		blog_button_back = (Button) findViewById(R.id.blog_button_back);
		blog_button_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RssDetailActivity.this.finish();
			}
		});
		try {
			webView = (WebView) findViewById(R.id.rss_body_webview_content);
			webView.getSettings().setDefaultTextEncodingName("utf-8");// ������������
			webView.addJavascriptInterface(this, "javatojs");
			webView.setSelected(true);
			webView.setScrollBarStyle(0);
			WebSettings webSetting = webView.getSettings();
			webSetting.setJavaScriptEnabled(true);
//			webSetting.setPluginsEnabled(true);
			webSetting.setNeedInitialFocus(false);
			webSetting.setSupportZoom(true);

			webSetting.setDefaultFontSize(14);
			webSetting.setCacheMode(WebSettings.LOAD_DEFAULT
					| WebSettings.LOAD_CACHE_ELSE_NETWORK);
			// ˫��ȫ��
			webView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return gestureScanner.onTouchEvent(event);
				}
			});
			int scalePercent = 120;
			// ��һ�α�������ű���
			float webviewScale = sharePreferencesSettings.getFloat(
					res.getString(R.string.preferences_webview_zoom_scale),
					(float) 1.2);
			scalePercent = (int) (webviewScale * 100);
			webView.setInitialScale(scalePercent);

			blogBody_progressBar = (ProgressBar) findViewById(R.id.blogBody_progressBar);
			blogBody_progressBar.setVisibility(View.VISIBLE);

			// ��һ��ȫ������״̬
			isFullScreen = sharePreferencesSettings.getBoolean(
					res.getString(R.string.preferences_is_fullscreen), false);
			// ��ʼ�Ƿ�ȫ��
			if (isFullScreen) {
				setFullScreen();
			}
			PageTask task = new PageTask();
			task.execute(blogUrl);
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), R.string.sys_error,
					Toast.LENGTH_SHORT).show();
		}

		// ������Ļ�����¼� ȫ��
		gestureScanner = new GestureDetector(this);
		gestureScanner.setIsLongpressEnabled(true);
		gestureScanner
				.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
					public boolean onDoubleTap(MotionEvent e) {
						if (!isFullScreen) {
							setFullScreen();
						} else {
							quitFullScreen();
						}
						isFullScreen = !isFullScreen;
						// ��������
						sharePreferencesSettings
								.edit()
								.putBoolean(
										res.getString(R.string.preferences_is_fullscreen),
										isFullScreen).commit();
						return false;
					}
					public boolean onDoubleTapEvent(MotionEvent e) {
						return false;
					}
					public boolean onSingleTapConfirmed(MotionEvent e) {
						return false;
					}
				});
	}
	// �����˵�
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.blog_body_webview_content) {
			menu.setHeaderTitle("��ѡ�����");
			menu.add(0, MENU_FORMAT_HTML, 0, "�鿴����");
			menu.add(0, MENU_READ_MODE, 1, "�л���ģʽ");
		}
	}
	/**
	 * �������ű���
	 */
	public void onDestroy() {
		float webviewScale = webView.getScale();
		sharePreferencesSettings
				.edit()
				.putFloat(
						res.getString(R.string.preferences_webview_zoom_scale),
						webviewScale).commit();
		super.onDestroy();
	}
	/**
	 * ���߳�����
	 * 
	 * @author walkingp
	 * 
	 */
	public class PageTask extends AsyncTask<String, Integer, String> {
		// �ɱ䳤�����������AsyncTask.exucute()��Ӧ
		@Override
		protected String doInBackground(String... params) {

			try {
				String _blogContent = blogContent;// BlogHelper.GetBlogById(blogId,
													// getApplicationContext());

				return _blogContent;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "";
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
		/**
		 * ��������
		 */
		@Override
		protected void onPostExecute(String _blogContent) {
			String htmlContent = "";
			try {
				InputStream in = getAssets().open("NewsDetail.html");
				byte[] temp = NetHelper.readInputStream(in);
				htmlContent = new String(temp);
			} catch (Exception e) {
				Log.e("error", e.toString());
			}

			String blogInfo = "����: " + blogAuthor + "   ����ʱ��:" + blogDate;
			// ��ʽ��html
			_blogContent = AppUtil.FormatContent(getApplicationContext(),
					_blogContent);

			htmlContent = htmlContent.replace("#title#", blogTitle)
					.replace("#time#", blogInfo)
					.replace("#content#", _blogContent);
			LoadWebViewContent(webView, htmlContent);
			blogBody_progressBar.setVisibility(View.GONE);
		}

		@Override
		protected void onPreExecute() {
			blogBody_progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
		}
	}
	/**
	 * ��������
	 * 
	 * @param webView
	 * @param content
	 */
	private void LoadWebViewContent(WebView webView, String content) {
		webView.loadDataWithBaseURL(Config.LOCAL_PATH, content, "text/html",
				Config.ENCODE_TYPE, null);
	}
	/**
	 * �˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.blog_detail_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	/**
	 * ȫ��
	 */
	private void setFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ���ص���
		rl_blog_detail.setVisibility(View.GONE);
	}
	/**
	 * �˳�ȫ��
	 */
	private void quitFullScreen() {
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setAttributes(attrs);
		getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		// ��ʾ����
		rl_blog_detail.setVisibility(View.VISIBLE);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_blog_back :// �����б�
				RssDetailActivity.this.setResult(0, getIntent());
				RssDetailActivity.this.finish();
				break;
			case R.id.menu_blog_comment :// �鿴����
				break;
			case R.id.menu_blog_share :// ����
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, blogTitle);
				String shareContent = "��" + blogTitle + "��,���ߣ�" + blogAuthor
						+ "��ԭ�����ӣ�" + blogUrl + " �����ԣ�"
						+ res.getString(R.string.app_name) + "Android�ͻ���("
						+ res.getString(R.string.app_homepage) + ")";
				intent.putExtra(Intent.EXTRA_TEXT, shareContent);
				startActivity(Intent.createChooser(intent, blogTitle));
				break;
			case R.id.menu_blog_author :// ����
				break;
			case R.id.menu_blog_browser :// �鿴��ҳ
				Uri blogUri = Uri.parse(blogUrl);
				Intent it = new Intent(Intent.ACTION_VIEW, blogUri);
				startActivity(it);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {

	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
