package com.pxkeji.qinghaipufawang.data.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pxkeji.qinghaipufawang.R;
import com.pxkeji.qinghaipufawang.data.News;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context mContext;
    private List<News> mNewsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView newsImage;
        TextView newsTitle;
        TextView newsSource;
        TextView newsHits;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            newsImage = (ImageView) itemView.findViewById(R.id.news_image);
            newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            newsSource = (TextView) itemView.findViewById(R.id.news_source);
            newsHits = (TextView) itemView.findViewById(R.id.news_hits);
        }
    }

    public NewsAdapter(List<News> newsList) {
        mNewsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mNewsList.get(position);
        holder.newsTitle.setText(news.getTitle());
        holder.newsSource.setText(news.getSource());
        holder.newsHits.setText(news.getHits() + "阅读");

        String picUrl = news.getPicUrl();
        if ("".equals(picUrl)) {
            Glide.with(mContext).load(R.drawable.no_pic).into(holder.newsImage);
        } else {
            Glide.with(mContext).load(picUrl).into(holder.newsImage);
        }


    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }


}
