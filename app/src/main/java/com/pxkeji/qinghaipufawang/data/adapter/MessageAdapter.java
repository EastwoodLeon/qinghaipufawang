package com.pxkeji.qinghaipufawang.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pxkeji.qinghaipufawang.R;
import com.pxkeji.qinghaipufawang.data.Message;

import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<Message> mList;

    public MessageAdapter(List<Message> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);

        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Message message = mList.get(position);
        if (message.getType() == 1) {
            Glide.with(mContext).load(R.drawable.hongbao_tu1).into(holder.msgImage);
        } else {
            Glide.with(mContext).load(R.drawable.hongbao_tu2).into(holder.msgImage);
        }
        holder.tvAmount.setText("￥" + String.valueOf(message.getAmount()));
        holder.tvSource.setText(message.getSource());
        holder.tvDate.setText(message.getDate());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView msgImage;
        TextView tvAmount;
        TextView tvSource;
        TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            msgImage = (ImageView) itemView.findViewById(R.id.image);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            tvSource = (TextView) itemView.findViewById(R.id.tv_source);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
