package com.pxkeji.qinghaipufawang;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.pxkeji.qinghaipufawang.util.HttpUtil;
import com.pxkeji.qinghaipufawang.util.LogUtil;
import com.pxkeji.qinghaipufawang.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LawDetailActivity extends AppCompatActivity {

    public static final String LAW_ID = "law_id";
    public static final String LAW_NAME = "law_name";

    private int mLawId;
    private String mLawName;

    private TextView mTvLawTitle;
    private TextView mTvLawDate;
    private WebView mWvLawContent;

    private String mLng;
    private String mLat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_detail);

        initActionBar();

        Intent intent = getIntent();
        mLawId = intent.getIntExtra(LAW_ID, 0);
        mLawName = intent.getStringExtra(LAW_NAME);

        TextView tvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(mLawName);

        mTvLawTitle = (TextView) findViewById(R.id.tv_law_title);
        mTvLawDate = (TextView) findViewById(R.id.tv_law_date);
        mWvLawContent = (WebView) findViewById(R.id.wv_law_content);

        getLawDetail(mLawId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        if ("寻找律所".equals(mLawName)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map:
                Intent intent = new Intent(this, MapActivity.class);

                int index = mLat.indexOf("&");
                if (index > -1) {
                    mLat = mLat.substring(0, index);
                }
                intent.putExtra(MapActivity.LNG, Double.parseDouble(mLng));
                intent.putExtra(MapActivity.LAT, Double.parseDouble(mLat));
                intent.putExtra(MapActivity.TITLE, mTvLawTitle.getText().toString());
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    private void getLawDetail(int lawId) {
        String url = StringUtil.addURLParam(Config.API_URL + "law_detail", "Id", String.valueOf(lawId));

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                LogUtil.w("position", responseText);
                try {
                    JSONObject responseObject = new JSONObject(responseText);
                    final String inputDate = responseObject.getString("InputDate");
                    final String title = responseObject.getString("Title");
                    final String content = responseObject.getString("Content");

                    mLng = responseObject.getString("jingdu");
                    mLat = responseObject.getString("weidu");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvLawTitle.setText(title);
                            mTvLawDate.setText(inputDate);
                            mWvLawContent.loadDataWithBaseURL(null, content, "text/html",
                                    "utf-8", null);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initActionBar() {
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
    }
}
