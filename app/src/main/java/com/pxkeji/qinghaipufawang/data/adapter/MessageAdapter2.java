package com.pxkeji.qinghaipufawang.data.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pxkeji.qinghaipufawang.R;
import com.pxkeji.qinghaipufawang.data.Law;
import com.pxkeji.qinghaipufawang.data.Message;

/**
 * Created by Administrator on 2018/1/15.
 */

public class MessageAdapter2 extends BaseQuickAdapter<Message, BaseViewHolder> {

    public MessageAdapter2() {
        super(R.layout.item_message, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        /*
        helper.setText(R.id.tv_amount, item.getAmount());
        helper.setText(R.id.tv_source, item.getSource());
        helper.setText(R.id.tv_date, item.getDate());
        */
    }
}
