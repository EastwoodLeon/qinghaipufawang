package com.pxkeji.qinghaipufawang.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pxkeji.qinghaipufawang.R;

/**
 * Created by Administrator on 2018/1/23.
 */

public class ViewPagerIndicator extends LinearLayout {

    private Paint mPaint;
    private Path mPath;
    private ViewPager mViewPager;
    public PageOnChangeListener mListener;

    private int mTriangleWidth;
    private int mTriangleHeight;

    private static final float RATIO_TRIANGLE_WIDTH = 1/2F;

    private int mInitTranslationX;
    private int mTranslationX;

    private static final int COLOR_TEXT_NORMAL = 0x77000000;
    private static final int COLOR_TEXT_HIGHLIGHT = 0xFF046EB5;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mPaint.setStyle(Paint.Style.FILL);
        //mPaint.setStrokeWidth(5);
        //mPaint.setPathEffect(new CornerPathEffect(3));
    }

    public interface PageOnChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
        void onPageScrollStateChanged(int state);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 20);
        canvas.drawPath(mPath, mPaint);
        //canvas.drawLine(0, 10, 50, 10, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w / 2 * RATIO_TRIANGLE_WIDTH);
        mInitTranslationX = w / 2 / 2 - mTriangleWidth / 2;

        initTriangle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setItemClickEvent();
    }

    private void initTriangle() {
        mTriangleHeight = mTriangleWidth / 2;
        Log.d("abc", "mTriangleHeight: " + mTriangleHeight);
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        //mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.lineTo(mTriangleWidth, -25);
        mPath.lineTo(0, -25);
        mPath.close();
    }

    /**
     * 指示器跟随手指进行滚动
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        int tabWidth = getWidth() / 2;
        mTranslationX = (int) (tabWidth * offset + position * tabWidth);
        invalidate();
    }

    /**
     * 重置tab文本颜色
     */
    private void resetTextViewColor() {
        for (int i=0; i<getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 高亮某个tab的文本
     * @param pos
     */
    private void highlightTextView(int pos) {
        resetTextViewColor();
        View view = getChildAt(pos);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }

    /**
     * 设置关联的ViewPager
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager, int pos) {
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if (mListener != null) {
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mListener != null) {
                    mListener.onPageSelected(position);
                }
                highlightTextView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListener != null) {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });

        mViewPager.setCurrentItem(pos);
        highlightTextView(pos);
    }

    public void setOnPageChangeListener(PageOnChangeListener listener) {
        mListener = listener;
    }

    /**
     * 设置tab的点击事件
     */
    private void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i=0; i<cCount; i++) {
            View view = getChildAt(i);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(finalI);
                }
            });
        }
    }
}
