package com.pxkeji.qinghaipufawang.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pxkeji.qinghaipufawang.BrowserActivity;
import com.pxkeji.qinghaipufawang.LawServiceListActivity;
import com.pxkeji.qinghaipufawang.LawsListActivity;
import com.pxkeji.qinghaipufawang.R;
import com.pxkeji.qinghaipufawang.Service1Activity;
import com.pxkeji.qinghaipufawang.WechatActivity;
import com.pxkeji.qinghaipufawang.data.ConvenienceItem;
import com.pxkeji.qinghaipufawang.data.ServiceItem;

import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */

public class ConvenienceAdapter extends RecyclerView.Adapter<ConvenienceAdapter.ViewHolder> {

    private Context mContext;
    private List<ConvenienceItem> mList;



    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView convenienceImage;
        TextView convenienceTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            convenienceImage = (ImageView) itemView.findViewById(R.id.service_image);
            convenienceTitle = (TextView) itemView.findViewById(R.id.service_name);
        }
    }

    public ConvenienceAdapter(List<ConvenienceItem> convenienceList) {
        mList = convenienceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_convenience, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ConvenienceItem item = mList.get(position);
        String title = item.getTitle();
        holder.convenienceTitle.setText(title);


        Glide.with(mContext).load(item.getImg()).into(holder.convenienceImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvenienceItem item = mList.get(holder.getAdapterPosition());
                Intent intent;
                String title = item.getTitle();
                if ("微端口".equals(title)) {
                    intent = new Intent(mContext, WechatActivity.class);
                } else {
                    intent = new Intent(mContext, BrowserActivity.class);
                    intent.putExtra(BrowserActivity.TITLE, item.getTitle());
                    intent.putExtra(BrowserActivity.URL, item.getUrl());
                }

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
