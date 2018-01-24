package com.pxkeji.qinghaipufawang.data.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pxkeji.qinghaipufawang.R;
import com.pxkeji.qinghaipufawang.data.MySection;
import com.pxkeji.qinghaipufawang.data.Service3Item;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class SectionAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {

    public SectionAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final MySection item) {
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more, item.isMore());
        helper.addOnClickListener(R.id.more);
    }


    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        Service3Item video = (Service3Item) item.t;

        helper.setText(R.id.tv, video.getName());
    }
}
