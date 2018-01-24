package com.pxkeji.qinghaipufawang;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pxkeji.qinghaipufawang.data.ConvenienceItem;
import com.pxkeji.qinghaipufawang.data.adapter.ConvenienceAdapter;
import com.pxkeji.qinghaipufawang.ui.fragment.BaseFragment;
import com.pxkeji.qinghaipufawang.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/10.
 */

public class ConvenienceFragment extends BaseFragment {

    private ImageView mBingPicImg;
    private ConvenienceAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<ConvenienceItem> mList = new ArrayList<>();

    @Override
    protected void initView() {
        mBingPicImg = (ImageView) mView.findViewById(R.id.bing_pic_img);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.setLayoutManager(layoutManager);


        mAdapter = new ConvenienceAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
    }



    @Override
    public int getLayoutId() {
        return R.layout.fragment_convenience;
    }

    @Override
    protected void getDataFromServer() {
        mList.clear();
        initData();
        mAdapter.notifyDataSetChanged();

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

    private void initData() {
        mList.add(new ConvenienceItem("微端口", "", R.drawable.convenience_wechat));
        mList.add(new ConvenienceItem("查天气", "http://tianqi.moji.com/", R.drawable.convenience_weather));
        mList.add(new ConvenienceItem("查地图", "http://ditu.amap.com/", R.drawable.convenience_map));
        mList.add(new ConvenienceItem("查违章", "http://www.jiazhao.com", R.drawable.convenience_violation));
        mList.add(new ConvenienceItem("买机票", "http://m.ctrip.com/html5/flight/matrix.html", R.drawable.convenience_plane));
        mList.add(new ConvenienceItem("查火车", "http://m.ctrip.com/webapp/train/", R.drawable.convenience_train));
        mList.add(new ConvenienceItem("微团购", "http://i.meituan.com/?city=xining", R.drawable.convenience_group_buy));
        mList.add(new ConvenienceItem("住酒店", "http://m.ctrip.com/webapp/hotel/", R.drawable.convenience_hotel));
        mList.add(new ConvenienceItem("万年历", "http://wannianli.tianqi.com/", R.drawable.convenience_calendar));
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
                        Glide.with(ConvenienceFragment.this).load(bingPic).into(mBingPicImg);
                    }
                });
            }
        });
    }
}
