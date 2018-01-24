package com.pxkeji.qinghaipufawang;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.pxkeji.qinghaipufawang.data.ServiceItem;
import com.pxkeji.qinghaipufawang.data.adapter.ServiceAdapter;
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

public class Service1Activity extends AppCompatActivity {

    public static final String SERVICE_TYPE = "service_type";

    public static final int SERVICE_CATEGORY_ONE = 34;
    public static final int SERVICE_CATEGORY_TWO = 35;

    private int mServiceType;

    private List<ServiceItem> mServiceItemList = new ArrayList<>();
    private ServiceAdapter mServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        /*
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        mServiceType = intent.getIntExtra(SERVICE_TYPE, 0);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        mServiceAdapter = new ServiceAdapter(mServiceItemList);
        recyclerView.setAdapter(mServiceAdapter);

        getServices();
    }

    private void getServices() {
        String url = StringUtil.addURLParam(Config.API_URL + "law_type", "id", String.valueOf(mServiceType));

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
                    JSONArray serviceJSONArray = responseObject.getJSONArray("list");
                    int serviceCount = serviceJSONArray.length();

                    if (serviceCount > 0) {
                        for (int i = 0; i < serviceCount; i++) {
                            JSONObject serviceObject = serviceJSONArray.getJSONObject(i);
                            ServiceItem serviceItem = new ServiceItem(serviceObject.getInt("Id"), serviceObject.getString("Alias"), mServiceType);
                            mServiceItemList.add(serviceItem);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mServiceAdapter.notifyDataSetChanged();

                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
