package com.pxkeji.qinghaipufawang;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.pxkeji.qinghaipufawang.data.Law;
import com.pxkeji.qinghaipufawang.data.adapter.LawAdapter2;
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

public class LawServiceListActivity extends AppCompatActivity {

    public static final String LAW_SERVICE_TYPE = "law_service_type";
    public static final String LAW_SERVICE_NAME = "law_service_name";


    private final static int PAGE_SIZE = 20;

    private int mLawServiceType;
    private String mLawServiceName;

    private LawAdapter2 mLawAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private PageController mPageController = new PageController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_service_list);

        initActionBar();

        Intent intent = getIntent();
        mLawServiceType = intent.getIntExtra(LAW_SERVICE_TYPE, 0);
        mLawServiceName = intent.getStringExtra(LAW_SERVICE_NAME);

        TextView tvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(intent.getStringExtra(LAW_SERVICE_NAME));

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLaws();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mLawAdapter = new LawAdapter2();
        mLawAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPageController.nextPage();
                searchForMore();
            }
        });


        mRecyclerView.setAdapter(mLawAdapter);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Law law = (Law) adapter.getItem(position);

                Intent intent = new Intent(LawServiceListActivity.this, LawDetailActivity.class);
                intent.putExtra(LawDetailActivity.LAW_ID, law.getId());
                intent.putExtra(LawDetailActivity.LAW_NAME, mLawServiceName);
                startActivity(intent);
            }
        });

        mRefreshLayout.setRefreshing(true);
        refreshLaws();
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void  refreshLaws() {
        mPageController.firstPage();
        mLawAdapter.setEnableLoadMore(false);
        searchForMore();
    }

    private void searchForMore() {
        String url = StringUtil.addURLParam(Config.API_URL + "law_list", "id", String.valueOf(mLawServiceType));
        url = StringUtil.addURLParam(url, "pagesize", String.valueOf(PAGE_SIZE));
        url = StringUtil.addURLParam(url, "page", String.valueOf(mPageController.getCurrentPage()));


        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                LogUtil.w("LawServiceListActivity", responseText);

                try {
                    JSONObject responseObject = new JSONObject(responseText);
                    mPageController.setTotalPages(responseObject.getInt("totalPage"));
                    JSONArray lawsJSONArray = responseObject.getJSONArray("list");
                    final List<Law> lawList = new ArrayList<>();
                    int lawsCount = lawsJSONArray.length();

                    for (int i = 0; i < lawsCount; i++) {
                        JSONObject lawObject = lawsJSONArray.getJSONObject(i);
                        Law law = new Law(lawObject.getInt("Id"), lawObject.getString("Title"), lawObject.getString("InputDate"));
                        lawList.add(law);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshLayout.setRefreshing(false);

                            if (mPageController.getCurrentPage() == 1) {
                                mLawAdapter.setNewData(lawList);
                            } else if (mPageController.getCurrentPage() > 1) {
                                mLawAdapter.addData(lawList);
                            }

                            mLawAdapter.loadMoreComplete();

                            if (mPageController.hasNextPage()) {
                                mLawAdapter.setEnableLoadMore(true);
                            } else {
                                mLawAdapter.loadMoreEnd(false);
                            }

                            if (mLawAdapter.getItemCount() == 0) {
                                mLawAdapter.setEmptyView(R.layout.view_no_data, (ViewGroup) mRecyclerView.getParent());
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
