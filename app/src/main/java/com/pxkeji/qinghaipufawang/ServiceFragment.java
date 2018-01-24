package com.pxkeji.qinghaipufawang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pxkeji.qinghaipufawang.ui.fragment.BaseFragment;
import com.pxkeji.qinghaipufawang.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/10.
 */

public class ServiceFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mBingPicImg;
    private LinearLayoutCompat mLlContainer1;
    private LinearLayoutCompat mLlContainer2;
    private LinearLayoutCompat mLlContainer3;

    @Override
    protected void initView() {
        mBingPicImg = (ImageView) mView.findViewById(R.id.bing_pic_img);
        mLlContainer1 = (LinearLayoutCompat) mView.findViewById(R.id.ll_container_1);
        mLlContainer2 = (LinearLayoutCompat) mView.findViewById(R.id.ll_container_2);
        mLlContainer3 = (LinearLayoutCompat) mView.findViewById(R.id.ll_container_3);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLlContainer1.setOnClickListener(this);
        mLlContainer2.setOnClickListener(this);
        mLlContainer3.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_service;
    }

    @Override
    protected void getDataFromServer() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(mBingPicImg);

            long bingPicTime = prefs.getLong("bing_pic_time", 0);
            long now = System.currentTimeMillis();
            int aDay = 24 * 60 * 60 * 1000;
            if (now - bingPicTime >= aDay) {
                loadBingPic();
            }
        } else {
            loadBingPic();
        }
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                editor.putString("bing_pic", bingPic);
                editor.putLong("bing_pic_time", System.currentTimeMillis());
                editor.apply();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(ServiceFragment.this).load(bingPic).into(mBingPicImg);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_container_1:
                Intent intent1 = new Intent(mContext, Service1Activity.class);
                intent1.putExtra(Service1Activity.SERVICE_TYPE, Service1Activity.SERVICE_CATEGORY_ONE);
                startActivity(intent1);
                break;
            case R.id.ll_container_2:
                Intent intent2 = new Intent(mContext, Service1Activity.class);
                intent2.putExtra(Service1Activity.SERVICE_TYPE, Service1Activity.SERVICE_CATEGORY_TWO);
                startActivity(intent2);
                break;
            case R.id.ll_container_3:
                Intent intent3 = new Intent(mContext, Service3Activity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }
}
