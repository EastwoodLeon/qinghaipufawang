package com.pxkeji.qinghaipufawang.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/1/10.
 */

public class NoSlidingViewPaper extends ViewPager {

    public NoSlidingViewPaper(Context context) {
        super(context);
    }

    public NoSlidingViewPaper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * 表示把滑动事件传递给下一个view
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /*
     * 可以啥都不做
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    //去除页面切换时的滑动翻页效果
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
}
