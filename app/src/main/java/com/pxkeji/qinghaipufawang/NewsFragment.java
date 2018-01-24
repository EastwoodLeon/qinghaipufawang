package com.pxkeji.qinghaipufawang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.pxkeji.qinghaipufawang.data.News;
import com.pxkeji.qinghaipufawang.data.adapter.NewsAdapter;
import com.pxkeji.qinghaipufawang.data.adapter.NewsAdapter2;
import com.pxkeji.qinghaipufawang.ui.fragment.BaseFragment;
import com.pxkeji.qinghaipufawang.util.HttpUtil;
import com.pxkeji.qinghaipufawang.util.LogUtil;
import com.pxkeji.qinghaipufawang.util.PageController;
import com.pxkeji.qinghaipufawang.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/10.
 */

public class NewsFragment extends BaseFragment {

    private final static int PAGE_SIZE = 10;

    private final static int NEWS_EXPRESS = 0;
    private final static int NEWS_QINGHAI = 1;
    private final static int NEWS_CASE = 2;
    private final static int NEWS_CULTURE = 3;

    private int mSelectedNewsType = NEWS_EXPRESS;

    private NewsAdapter2 mNewsAdapter;
    private TextView mTvToolbarTitle;
    private SwipeRefreshLayout mRefreshLayout;

    private PageController mPageController = new PageController();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initView() {
        mTvToolbarTitle = (TextView) mView.findViewById(R.id.tv_toolbar_title);

        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });

        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mNewsAdapter = new NewsAdapter2();
        mNewsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPageController.nextPage();
                searchForMore();
            }
        });
        recyclerView.setAdapter(mNewsAdapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                News news = (News) adapter.getItem(position);
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.NEWS_ID, news.getId());
                intent.putExtra(NewsDetailActivity.NEWS_IMAGE_URL, news.getPicUrl());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        Toolbar toolbar = (Toolbar) mView.findViewById(R.id.toobar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void getDataFromServer() {
        setToolbarTitle();
        mRefreshLayout.setRefreshing(true);
        refreshNews();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.toolbar_news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.express:
                mSelectedNewsType = NEWS_EXPRESS;
                break;
            case R.id.qinghai:
                mSelectedNewsType = NEWS_QINGHAI;
                break;
            case R.id.cases:
                mSelectedNewsType = NEWS_CASE;
                break;
            case R.id.culture:
                mSelectedNewsType = NEWS_CULTURE;
                break;
            default:
                break;
        }
        setToolbarTitle();
        mRefreshLayout.setRefreshing(true);
        refreshNews();
        return true;
    }



    private void refreshNews() {
        mPageController.firstPage();
        mNewsAdapter.setEnableLoadMore(false);
        searchForMore();
    }

    private void searchForMore() {
        String url = StringUtil.addURLParam(Config.API_URL + "news_list", "type", String.valueOf(mSelectedNewsType));
        url = StringUtil.addURLParam(url, "pagesize", String.valueOf(PAGE_SIZE));
        url = StringUtil.addURLParam(url, "page", String.valueOf(mPageController.getCurrentPage()));


        HttpUtil.sendOkHttpRequest( url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                LogUtil.e("NewsFragment", responseText);


                try {
                    JSONObject responseObject = new JSONObject(responseText);
                    mPageController.setTotalPages(responseObject.getInt("totalPage"));
                    JSONArray newsJSONArray = responseObject.getJSONArray("list");
                    int newsCount = newsJSONArray.length();

                    if (newsCount > 0) {
                        final List<News> newsList = new ArrayList<>();
                        for (int i = 0; i < newsCount; i++) {
                            JSONObject newsObject = newsJSONArray.getJSONObject(i);
                            News news = new News.Builder()
                                    .id(newsObject.getInt("Id"))
                                    .hits(newsObject.getInt("Hits"))
                                    .title(newsObject.getString("Title"))
                                    .source(newsObject.getString("source"))
                                    .picUrl("".equals(newsObject.getString("PicUrl")) ?
                                            "" :
                                            Config.IMG_URL + newsObject.getString("PicUrl"))
                                    .build();
                            newsList.add(news);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshLayout.setRefreshing(false);
                                /*

                                mNewsAdapter.notifyDataSetChanged();
                                */
                                if (mPageController.getCurrentPage() == 1) {
                                    mNewsAdapter.setNewData(newsList);
                                } else if (mPageController.getCurrentPage() > 1) {
                                    mNewsAdapter.addData(newsList);
                                }

                                // 当前页加载完成
                                mNewsAdapter.loadMoreComplete();

                                if (mPageController.hasNextPage()) {
                                    mNewsAdapter.setEnableLoadMore(true);
                                } else {
                                    mNewsAdapter.loadMoreEnd(false);
                                }




                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void setToolbarTitle() {
        String title;
        switch (mSelectedNewsType) {
            case NEWS_EXPRESS:
                title = "普法速递";
                break;
            case NEWS_QINGHAI:
                title = "法治青海";
                break;
            case NEWS_CASE:
                title = "以案释法";
                break;
            case NEWS_CULTURE:
                title = "法治文化";
                break;
            default:
                title = "普法速递";
                break;
        }
        mTvToolbarTitle.setText(title);
    }
}
