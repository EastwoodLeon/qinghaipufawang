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
import com.pxkeji.qinghaipufawang.LawServiceListActivity;
import com.pxkeji.qinghaipufawang.LawsListActivity;
import com.pxkeji.qinghaipufawang.R;
import com.pxkeji.qinghaipufawang.Service1Activity;
import com.pxkeji.qinghaipufawang.data.News;
import com.pxkeji.qinghaipufawang.data.ServiceItem;

import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private Context mContext;
    private List<ServiceItem> mServiceList;



    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView serviceImage;
        TextView serviceTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            serviceImage = (ImageView) itemView.findViewById(R.id.service_image);
            serviceTitle = (TextView) itemView.findViewById(R.id.service_name);
        }
    }

    public ServiceAdapter(List<ServiceItem> serviceList) {
        mServiceList = serviceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_service, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ServiceItem serviceItem = mServiceList.get(position);
        String serviceName = serviceItem.getName();
        holder.serviceTitle.setText(serviceName);

        int drawableId = R.drawable.law_constitution;
        if ("宪法".equals(serviceName)) {
            drawableId = R.drawable.law_constitution;
        } else if ("民商法".equals(serviceName)) {
            drawableId = R.drawable.law_commercial;
        } else if ("刑法".equals(serviceName)) {
            drawableId = R.drawable.law_criminal_law;
        } else if ("行政法".equals(serviceName)) {
            drawableId = R.drawable.law_admin;
        } else if ("经济法".equals(serviceName)) {
            drawableId = R.drawable.law_economy;
        } else if ("社会法".equals(serviceName)) {
            drawableId = R.drawable.law_society;
        } else if ("诉讼与非诉讼程序法".equals(serviceName)) {
            drawableId = R.drawable.law_litigation;
        } else if ("地方法规".equals(serviceName)) {
            drawableId = R.drawable.law_local;
        } else if ("党内法规".equals(serviceName)) {
            drawableId = R.drawable.law_party;
        } else if ("寻找律所".equals(serviceName)) {
            drawableId = R.drawable.service_law_office;
        } else if ("寻找律师".equals(serviceName)) {
            drawableId = R.drawable.service_lawyer;
        } else if ("法律援助".equals(serviceName)) {
            drawableId = R.drawable.service_help;
        } else if ("司法鉴定".equals(serviceName)) {
            drawableId = R.drawable.service_forensics;
        } else if ("办理公证".equals(serviceName)) {
            drawableId = R.drawable.service_notarization;
        } else if ("人民调解".equals(serviceName)) {
            drawableId = R.drawable.service_mediation;
        } else if ("基层司法".equals(serviceName)) {
            drawableId = R.drawable.service_grassroots;
        }
        Glide.with(mContext).load(drawableId).into(holder.serviceImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceItem serviceItem = mServiceList.get(holder.getAdapterPosition());
                Intent intent;
                switch (serviceItem.getCategory()) {
                    case Service1Activity.SERVICE_CATEGORY_ONE:
                        intent = new Intent(mContext, LawsListActivity.class);
                        intent.putExtra(LawsListActivity.LAW_TYPE, serviceItem.getId());
                        intent.putExtra(LawsListActivity.LAW_NAME, serviceItem.getName());
                        break;
                    case Service1Activity.SERVICE_CATEGORY_TWO:
                        intent = new Intent(mContext, LawServiceListActivity.class);
                        intent.putExtra(LawServiceListActivity.LAW_SERVICE_TYPE, serviceItem.getId());
                        intent.putExtra(LawServiceListActivity.LAW_SERVICE_NAME, serviceItem.getName());
                        break;
                    default:
                        intent = new Intent(mContext, LawsListActivity.class);
                        intent.putExtra(LawsListActivity.LAW_TYPE, serviceItem.getId());
                        intent.putExtra(LawsListActivity.LAW_NAME, serviceItem.getName());
                        break;
                }


                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mServiceList.size();
    }
}
