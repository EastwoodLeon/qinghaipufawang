package com.pxkeji.qinghaipufawang.data.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pxkeji.qinghaipufawang.R;
import com.pxkeji.qinghaipufawang.data.News;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */

public class NewsAdapter2 extends BaseQuickAdapter<News, BaseViewHolder> {


    public NewsAdapter2() {
        super(R.layout.item_news, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, News item) {
        helper.setText(R.id.news_title, item.getTitle());
        helper.setText(R.id.news_source, item.getSource());
        helper.setText(R.id.news_hits, item.getHits() + "阅读");

        ImageView imageView = helper.getView(R.id.news_image);
        String picUrl = item.getPicUrl();
        if ("".equals(picUrl)) {
            Glide.with(mContext).load(R.drawable.no_pic).into(imageView);
        } else {
            Glide.with(mContext).load(picUrl).into(imageView);
        }
    }
}
