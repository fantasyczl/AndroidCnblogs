package com.cnblogs.android;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost.TabSpec;

import com.cnblogs.android.fragment.BlogFragment;
import com.cnblogs.android.fragment.MoreFragment;
import com.cnblogs.android.fragment.NewsFragment;
import com.cnblogs.android.fragment.RssFragment;
import com.cnblogs.android.fragment.SearchFragment;

public class MainActivity extends FragmentActivity  implements OnCheckedChangeListener {
	private FragmentTabHost mTabHost;
	private RadioButton rbBlog;
	private RadioButton rbNews;
	private RadioButton rbRss;
	private RadioButton rbSearch;
	private RadioButton rbMore;

    public String whichTab = "";

	Resources res;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		res = this.getResources();

		InitialRadios();
		InitialTab();
		initialSelectedTab();
	}

	private void InitialRadios() {
		rbBlog = (RadioButton) findViewById(R.id.TabBlog);
		rbBlog.setOnCheckedChangeListener(this);
		rbNews = (RadioButton) findViewById(R.id.TabNews);
		rbNews.setOnCheckedChangeListener(this);
		rbRss = (RadioButton) findViewById(R.id.TabRss);
		rbRss.setOnCheckedChangeListener(this);
		rbSearch = (RadioButton) findViewById(R.id.TabSearch);
		rbSearch.setOnCheckedChangeListener(this);
		rbMore = (RadioButton) findViewById(R.id.TabMore);
		rbMore.setOnCheckedChangeListener(this);
	}

	private void InitialTab() {
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

		TabSpec blogSpec = mTabHost.newTabSpec("blog");
		blogSpec.setIndicator(getString(R.string.main_home), res.getDrawable(R.drawable.icon));
		mTabHost.addTab(blogSpec, BlogFragment.class, null);
		
		TabSpec newsSpec = mTabHost.newTabSpec("news");
		newsSpec.setIndicator(getString(R.string.main_news), res.getDrawable(R.drawable.icon));
		mTabHost.addTab(newsSpec, NewsFragment.class, null);
		
		TabSpec rssSpec = mTabHost.newTabSpec("rss");
		rssSpec.setIndicator(getString(R.string.main_rss), res.getDrawable(R.drawable.icon));
		mTabHost.addTab(rssSpec, RssFragment.class, null);
		
		TabSpec searchSpec = mTabHost.newTabSpec("search");
		searchSpec.setIndicator(getString(R.string.main_search), res.getDrawable(R.drawable.icon));
		mTabHost.addTab(searchSpec, SearchFragment.class, null);
		
		TabSpec moreSpec = mTabHost.newTabSpec("more");
		moreSpec.setIndicator(getString(R.string.main_more), res.getDrawable(R.drawable.icon));
		mTabHost.addTab(moreSpec, MoreFragment.class, null);
	}

	private void initialSelectedTab() {
		SharedPreferences settings = getSharedPreferences(res.getString(R.string.preferences_key), MODE_PRIVATE);
		whichTab = settings.getString(res.getString(R.string.preferences_current_tab), "blog");
		if (whichTab.equals("blog"))
			rbBlog.setChecked(true);
		else if (whichTab.equals("news"))
			rbNews.setChecked(true);
		else if (whichTab.equals("rss"))
			rbRss.setChecked(true);
		else if (whichTab.equals("search"))
			rbSearch.setChecked(true);
		else if (whichTab.equals("more"))
			rbMore.setChecked(true);
	}


	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (!isChecked) {
			return;
		}
		switch (buttonView.getId()) {
			case R.id.TabBlog :
				whichTab = "blog";
				mTabHost.setCurrentTabByTag("blog");
				break;
			case R.id.TabNews :
				whichTab = "news";
				mTabHost.setCurrentTabByTag("news");
				break;
			case R.id.TabRss :
				whichTab = "rss";
				mTabHost.setCurrentTabByTag("rss");
				break;
			case R.id.TabSearch :
				whichTab = "search";
				mTabHost.setCurrentTabByTag("search");
				break;
			case R.id.TabMore :
				whichTab = "more";
				mTabHost.setCurrentTabByTag("more");
				break;
		}
	}

	protected void onDestroy() {
		SharedPreferences settings = getSharedPreferences( getString(R.string.preferences_key), MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString( getString(R.string.preferences_current_tab), whichTab);
		editor.commit();
		
		super.onDestroy();
	}
}