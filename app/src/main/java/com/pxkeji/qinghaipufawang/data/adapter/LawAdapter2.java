package com.pxkeji.qinghaipufawang.data.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pxkeji.qinghaipufawang.R;
import com.pxkeji.qinghaipufawang.data.Law;

import java.util.List;

/**
 * Created by Administrator on 2018/1/15.
 */

public class LawAdapter2 extends BaseQuickAdapter<Law, BaseViewHolder> {

    public LawAdapter2() {
        super(R.layout.item_laws, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, Law item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_date, item.getDate());
    }
}
