package com.cnblogs.android.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cnblogs.android.R;
import com.cnblogs.android.activity.AuthorBlogActivity;
import com.cnblogs.android.activity.BlogDetailActivity;
import com.cnblogs.android.activity.CommentActivity;
import com.cnblogs.android.adapter.BlogListAdapter;
import com.cnblogs.android.controls.PullToRefreshListView;
import com.cnblogs.android.controls.PullToRefreshListView.OnRefreshListener;
import com.cnblogs.android.core.BlogHelper;
import com.cnblogs.android.core.Config;
import com.cnblogs.android.db.BlogListDBTask;
import com.cnblogs.android.entity.Blog;
import com.cnblogs.android.utility.NetHelper;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends Fragment {
	static final String TAG = "BlogFragment";

	List<Blog> listBlog = new ArrayList<Blog>();

	int pageIndex = 1;

    PullToRefreshListView mListView;
	private BlogListAdapter adapter;

	ProgressBar blogBody_progressBar;
	ImageButton blog_refresh_btn;
	ProgressBar blog_progress_bar;

	private LinearLayout viewFooter;

	Resources res;
	private int lastItem;
	BlogListDBTask dbHelper;
    UpdateListViewReceiver mReceiver;

    public BlogFragment(){}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		res = getResources();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.blog_layout, container, false);

		initialControls(rootView);
		bindControls();
		
		new PageTask(0, true).execute();
		
		mReceiver = new UpdateListViewReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.cnblogs.com.update_bloglist");
		getActivity().registerReceiver(mReceiver, filter);
		
		return rootView;
	}
	
	private void initialControls(View rootView) {
		mListView = (PullToRefreshListView)rootView.findViewById(R.id.blog_list);
		blogBody_progressBar = (ProgressBar)rootView.findViewById(R.id.blogList_progressBar);
		blogBody_progressBar.setVisibility(View.VISIBLE);

		blog_refresh_btn = (ImageButton)rootView.findViewById(R.id.blog_refresh_btn);
		blog_progress_bar = (ProgressBar)rootView.findViewById(R.id.blog_progressBar);

		LayoutInflater mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewFooter = (LinearLayout) mInflater.inflate(R.layout.listview_footer, null, false);
	}
	
	private void bindControls() {
		blog_refresh_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new PageTask(1, true).execute();
			}
		});
		mListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new PageTask(-1, true).execute();
            }
        });
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastItem == adapter.getCount()
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					pageIndex = pageIndex + 1;
					new PageTask(pageIndex, false).execute();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem - 2 + visibleItemCount;
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				redirectDetailActivity(v);
			}
		});
		mListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				MenuInflater inflater = getActivity().getMenuInflater();
				inflater.inflate(R.menu.blog_list_contextmenu, menu);
				menu.setHeaderTitle(R.string.menu_bar_title);
			}
		});
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }

    public class PageTask extends AsyncTask<String, Integer, List<Blog>> {
		boolean isRefresh = false;
		int curPageIndex = 0;
		boolean isLocalData = false;
		
		public PageTask(int page, boolean isRefresh) {
			curPageIndex = page;
			this.isRefresh = isRefresh;
		}

		protected List<Blog> doInBackground(String... params) {
			boolean isNetworkAvailable = NetHelper.networkIsAvailable(getActivity().getApplicationContext());

			int _pageIndex = curPageIndex;
			if (_pageIndex <= 0) {
				_pageIndex = 1;
			}
			
			if (isNetworkAvailable) {
				List<Blog> listBlogNew = BlogHelper.getBlogList(_pageIndex);
				switch (curPageIndex) {
					case -1 :
						List<Blog> listTmp = new ArrayList<Blog>();
						if (listBlog != null && listBlog.size() > 0) {
							if (listBlogNew != null && listBlogNew.size() > 0) {
								int size = listBlogNew.size();
								for (int i = 0; i < size; i++) {
									if (!listBlog.contains(listBlogNew.get(i))) {
										listTmp.add(listBlogNew.get(i));
									}
								}
							}
						}
						return listTmp;
					case 0 :
					case 1 :
					    return listBlogNew;
					default :
						List<Blog> listT = new ArrayList<Blog>();
						if (listBlog != null && listBlog.size() > 0) {
							if (listBlogNew != null && listBlogNew.size() > 0) {
								int size = listBlogNew.size();
								for (int i = 0; i < size; i++) {
									if (!listBlog.contains(listBlogNew.get(i))) {
										listT.add(listBlogNew.get(i));
									}
								}
							}
						}
						return listT;
				}
			} else {
				isLocalData = true;

                if (curPageIndex == -1) {
					return null;
				}

				return BlogListDBTask.getBlogListByPage(_pageIndex, Config.BLOG_PAGE_SIZE);
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(List<Blog> result) {
			blog_progress_bar.setVisibility(View.GONE);
			blog_refresh_btn.setVisibility(View.VISIBLE);

			if (result == null || result.size() == 0) {
				mListView.onRefreshComplete();
				if (!NetHelper.networkIsAvailable(getActivity()) && curPageIndex > 1) {
					Toast.makeText(getActivity(), R.string.sys_network_error, Toast.LENGTH_SHORT).show();
					// listView.removeFooterView(viewFooter);
				}
				return;
			}

            int size = result.size();
			if (size >= Config.BLOG_PAGE_SIZE && mListView.getFooterViewsCount() == 0) {
				mListView.addFooterView(viewFooter);
			}

			if (!isLocalData) {
				dbHelper.synchronyData2DB(result);
			}

			if (curPageIndex == -1) {
				adapter.InsertData(result);
			} else if (curPageIndex == 0) {
				listBlog = result;// dbHelper.getTopBlogList();

				blogBody_progressBar.setVisibility(View.GONE);
				adapter = new BlogListAdapter(getActivity().getApplicationContext(), listBlog, mListView);
				mListView.setAdapter(adapter);

				((PullToRefreshListView) mListView).setDataRow(listBlog.size());
				((PullToRefreshListView) mListView).setPageSize(Config.BLOG_PAGE_SIZE);
			} else if (curPageIndex == 1) {
				try {
					listBlog = result;
					if (adapter != null && adapter.GetData() != null) {
						adapter.GetData().clear();
						adapter.AddMoreData(listBlog);
					} else if (result != null) {
						adapter = new BlogListAdapter(getActivity().getApplicationContext(),listBlog, mListView);
						mListView.setAdapter(adapter);
					}
					
					((PullToRefreshListView) mListView).setDataRow(listBlog.size());
					((PullToRefreshListView) mListView).setPageSize(Config.BLOG_PAGE_SIZE);
				} catch (Exception ex) {
                    ex.printStackTrace();
					Log.e("BlogActivity", ex.getMessage());
				}
			} else {
				adapter.AddMoreData(result);
			}

			if (isRefresh) {
				((PullToRefreshListView) mListView).onRefreshComplete();
			}
		}
		
		@Override
		protected void onPreExecute() {
			
			if (mListView.getCount() == 0) {
				blogBody_progressBar.setVisibility(View.VISIBLE);
			}
			
			blog_progress_bar.setVisibility(View.VISIBLE);
			blog_refresh_btn.setVisibility(View.GONE);

			if (!isRefresh) {
				TextView tvFooterMore = (TextView) getView().findViewById(R.id.tvFooterMore);
				tvFooterMore.setText(R.string.pull_to_refresh_refreshing_label);
				tvFooterMore.setVisibility(View.VISIBLE);
				ProgressBar list_footer_progress = (ProgressBar) getView().findViewById(R.id.list_footer_progress);
				list_footer_progress.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
		}
	}
	
	
	private void redirectCommentActivity(View v) {
		TextView tvBlogCommentCount = (TextView)v.findViewById(R.id.recommend_text_comments);
		TextView tvBlogId = (TextView)v.findViewById(R.id.recommend_text_id);
		TextView tvBlogTitle = (TextView) v.findViewById(R.id.recommend_text_title);
		TextView tvBlogUrl = (TextView)v.findViewById(R.id.recommend_text_url);
		int blogId = Integer.parseInt(tvBlogId.getText().toString());
		int commentCount = Integer.parseInt(tvBlogCommentCount.getText().toString());
		String blogTitle = tvBlogTitle.getText().toString();
		String blogUrl = tvBlogUrl.getText().toString();

		if (commentCount == 0) {
			Toast.makeText(getActivity().getApplicationContext(), R.string.sys_empty_comment,
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent();
		intent.setClass(getActivity(), CommentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("contentId", blogId);
		bundle.putInt("commentType", 0);// Comment.EnumCommentType.News.ordinal());
		bundle.putString("title", blogTitle);
		bundle.putString("url", blogUrl);
		
		intent.putExtras(bundle);

		startActivity(intent);
	}
		
	private void redirectDetailActivity(View v) {
		Intent intent = new Intent();
		try {
			intent.setClass( getActivity(), BlogDetailActivity.class);
			Bundle bundle = new Bundle();
			TextView tvBlogId = (TextView)v.findViewById(R.id.recommend_text_id);
			TextView tvBlogTitle = (TextView)v.findViewById(R.id.recommend_text_title);
			TextView tvBlogAuthor = (TextView)v.findViewById(R.id.recommend_text_author);
			TextView tvBlogDate = (TextView) v.findViewById(R.id.recommend_text_date);
			TextView tvBlogUrl = (TextView) v.findViewById(R.id.recommend_text_url);
			TextView tvBlogViewCount = (TextView) v.findViewById(R.id.recommend_text_view);
			TextView tvBlogCommentCount = (TextView) v.findViewById(R.id.recommend_text_comments);
			TextView tvBlogDomain = (TextView) v.findViewById(R.id.recommend_text_domain);

			int blogId = Integer.parseInt(tvBlogId.getText().toString());
			String blogTitle = tvBlogTitle.getText().toString();
			String blogAuthor = tvBlogAuthor.getText().toString();
			String blogDate = tvBlogDate.getText().toString();
			String blogUrl = tvBlogUrl.getText().toString();
			String blogDomain = tvBlogDomain.getText().toString();
			int viewsCount = Integer.parseInt(tvBlogViewCount.getText().toString());
			int commentCount = Integer.parseInt(tvBlogCommentCount.getText().toString());

			bundle.putInt("blogId", blogId);
			bundle.putString("blogTitle", blogTitle);
			bundle.putString("author", blogAuthor);
			bundle.putString("date", blogDate);
			bundle.putString("blogUrl", blogUrl);
			bundle.putInt("view", viewsCount);
			bundle.putInt("comment", commentCount);
			bundle.putString("blogDomain", blogDomain);

			Log.d("blogId", String.valueOf(blogId));
			intent.putExtras(bundle);

			startActivity(intent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void viewInBrowser(View v) {
		TextView tvBlogUrl = (TextView) v.findViewById(R.id.recommend_text_url);
		String blogUrl = tvBlogUrl.getText().toString();
		Uri blogUri = Uri.parse(blogUrl);
		Intent it = new Intent(Intent.ACTION_VIEW, blogUri);
		startActivity(it);
	}
		
	private void redirectAuthorActivity(View v) {
		TextView tvUserName=(TextView)v.findViewById(R.id.recommend_user_name);
		String userName=tvUserName.getText().toString();
		if (userName.equals("")) {
			Toast.makeText(getActivity().getApplicationContext(), R.string.sys_no_author,
					Toast.LENGTH_SHORT).show();
			return;
		}
		TextView tvBlogAuthor = (TextView) (v.findViewById(R.id.recommend_text_author));
		String blogAuthor = tvBlogAuthor.getText().toString();

		Intent intent = new Intent();
		intent.setClass(getActivity(), AuthorBlogActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("author", userName);
		bundle.putString("blogName", blogAuthor);
		
		intent.putExtras(bundle);
		
		startActivity(intent);
	}
		
	private void shareTo(View v) {
		TextView tvBlogTitle = (TextView) (v.findViewById(R.id.recommend_text_title));
		String blogTitle = tvBlogTitle.getText().toString();
		TextView tvBlogAuthor = (TextView) (v.findViewById(R.id.recommend_text_author));
		String blogAuthor = tvBlogAuthor.getText().toString();
		TextView tvBlogUrl = (TextView) (v.findViewById(R.id.recommend_text_url));
		String blogUrl = tvBlogUrl.getText().toString();
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "请选择分享到…");
		
        String shareContent = "《" + blogTitle + "》,作者：" + blogAuthor + "，原文链接："
                        + blogUrl + " 分享自：" + res.getString(R.string.app_name)
                        + "Android客户端(" + res.getString(R.string.app_homepage) + ")";
		intent.putExtra(Intent.EXTRA_TEXT, shareContent);
		startActivity(Intent.createChooser(intent, blogTitle));
		
	}
		
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int itemIndex = item.getItemId();
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		View v = menuInfo.targetView;
		switch (itemIndex) {
		case R.id.menu_blog_view :
			redirectDetailActivity(v);
			break;
		case R.id.menu_blog_comment :
			redirectCommentActivity(v);
			break;
		case R.id.menu_blog_author :
			redirectAuthorActivity(v);
			break;
		case R.id.menu_blog_browser :
			viewInBrowser(v);
			break;
		case R.id.menu_blog_share :
			shareTo(v);
			break;
		}

		return super.onContextItemSelected(item);
	}
		
		
	public class UpdateListViewReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context content, Intent intent) {
			
			Bundle bundle = intent.getExtras();
			int[] blogIdArr = bundle.getIntArray("blogIdArray");
			for(int i = 0, len = mListView.getChildCount(); i < len; i++){
				View view = mListView.getChildAt(i);
				TextView tvId = (TextView)view.findViewById(R.id.recommend_text_id);
				if(tvId != null){
					/*int blogId=Integer.parseInt(tvId.getText().toString());
						
						ImageView icoDown=(ImageView)view.findViewById(R.id.icon_downloaded);
						TextView tvTitle=(TextView)view.findViewById(R.id.recommend_text_title);
						
						for(int j=0,size=blogIdArr.length;j<size;j++){
							if(blogId==blogIdArr[j]){
								icoDown.setVisibility(View.VISIBLE);
								tvTitle.setTextColor(R.color.gray);
							}
						}*/
				}
			}
			for(int i = 0, len = blogIdArr.length; i < len; i++){
				for(int j = 0, size = listBlog.size(); j < size; j++){
					if(blogIdArr[i] == listBlog.get(j).GetBlogId()){
						listBlog.get(i).setIsFullText(true);
						listBlog.get(i).setIsReaded(true);
					}
				}
			}
		}		
	}
	
}
