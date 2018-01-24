package com.pxkeji.qinghaipufawang.data;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MySection extends SectionEntity<Service3Item> {

    private boolean isMore;

    public MySection(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public MySection(Service3Item t) {
        super(t);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }
}
