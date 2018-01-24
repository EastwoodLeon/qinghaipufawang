package com.pxkeji.qinghaipufawang;

import android.content.Intent;
import android.drm.DrmStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pxkeji.qinghaipufawang.data.News;
import com.pxkeji.qinghaipufawang.util.HttpUtil;
import com.pxkeji.qinghaipufawang.util.LogUtil;
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

public class NewsDetailActivity extends AppCompatActivity {

    public static final String NEWS_ID = "news_id";
    public static final String NEWS_IMAGE_URL = "news_image_url";

    private TextView mTvNewsTitle;
    private TextView mTvNewsSource;
    private TextView mTvNewsDate;
    private WebView mWvNewsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        /*
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        */

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
        int newsId = intent.getIntExtra(NEWS_ID, 0);
        String newsImageUrl = intent.getStringExtra(NEWS_IMAGE_URL);


        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView newsImageView = (ImageView) findViewById(R.id.news_image_view);


        if ("".equals(newsImageUrl)) {
            Glide.with(this).load(R.drawable.no_pic).into(newsImageView);
        } else {
            Glide.with(this).load(newsImageUrl).into(newsImageView);
        }


        mTvNewsTitle = (TextView) findViewById(R.id.tv_news_title);
        mTvNewsSource = (TextView) findViewById(R.id.tv_news_source);
        mTvNewsDate = (TextView) findViewById(R.id.tv_news_date);
        mWvNewsContent = (WebView) findViewById(R.id.wv_news_content);

        getNewsDetail(newsId);
    }

    private void getNewsDetail(final int newsId) {
        String url = StringUtil.addURLParam(Config.API_URL + "news_detail", "Id", String.valueOf(newsId));

        HttpUtil.sendOkHttpRequest( url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                LogUtil.e("NewsDetail", responseText);


                try {
                    final JSONObject responseObject = new JSONObject(responseText);
                    final String newsTitle = responseObject.getString("Title");
                    final String newsSource = responseObject.getString("source");
                    final String newsDate = responseObject.getString("date");
                    final String newsContent = responseObject.getString("Content");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvNewsTitle.setText(newsTitle);
                            mTvNewsSource.setText(newsSource);
                            mTvNewsDate.setText(newsDate);
                            mWvNewsContent.loadDataWithBaseURL(null, newsContent,
                                    "text/html", "utf-8", null);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }


}
