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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class LawsListActivity extends AppCompatActivity {

    public static final String LAW_TYPE = "law_type";
    public static final String LAW_NAME = "law_name";

    private final static int PAGE_SIZE = 10;

    private int mLawType;
    private String mLawName;

    private LawAdapter2 mLawAdapter;

    private SwipeRefreshLayout mRefreshLayout;
    private PageController mPageController = new PageController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laws_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        mLawType = intent.getIntExtra(LAW_TYPE, 0);
        mLawName = intent.getStringExtra(LAW_NAME);
        String lawName = mLawName;
        int drawableId = R.drawable.law_48;
        if ("宪法".equals(lawName)) {
            drawableId = R.drawable.law_48;
        } else if ("民商法".equals(lawName)) {
            drawableId = R.drawable.law_49;
        } else if ("刑法".equals(lawName)) {
            drawableId = R.drawable.law_50;
        } else if ("行政法".equals(lawName)) {
            drawableId = R.drawable.law_51;
        } else if ("经济法".equals(lawName)) {
            drawableId = R.drawable.law_52;
        } else if ("社会法".equals(lawName)) {
            drawableId = R.drawable.law_53;
        } else if ("诉讼与非诉讼程序法".equals(lawName)) {
            drawableId = R.drawable.law_54;
        } else if ("地方法规".equals(lawName)) {
            drawableId = R.drawable.law_55;
        } else if ("党内法规".equals(lawName)) {
            drawableId = R.drawable.law_56;
        }

        ImageView ivLaws = (ImageView) findViewById(R.id.laws_image_view);
        Glide.with(this).load(drawableId).into(ivLaws);


        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLaws();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mLawAdapter = new LawAdapter2();
        mLawAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPageController.nextPage();
                searchForMore();
            }
        });

        recyclerView.setAdapter(mLawAdapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Law law = (Law) adapter.getItem(position);

                Intent intent = new Intent(LawsListActivity.this, LawDetailActivity.class);
                intent.putExtra(LawDetailActivity.LAW_ID, law.getId());
                intent.putExtra(LawDetailActivity.LAW_NAME, mLawName);
                startActivity(intent);
            }
        });

        mRefreshLayout.setRefreshing(true);
        refreshLaws();
    }

    private void  refreshLaws() {
        mPageController.firstPage();
        mLawAdapter.setEnableLoadMore(false);
        searchForMore();
    }

    private void searchForMore() {
        String url = StringUtil.addURLParam(Config.API_URL + "law_list", "id", String.valueOf(mLawType));
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
                LogUtil.w("LawsListActivity", responseText);

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
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
