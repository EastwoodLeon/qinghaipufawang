package com.pxkeji.qinghaipufawang;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pxkeji.qinghaipufawang.ui.view.NoSlidingViewPaper;
import com.pxkeji.qinghaipufawang.ui.view.ViewPagerIndicator;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private ViewPagerIndicator mIndicator;
    private NoSlidingViewPaper mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initActionBar();

        mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
        mViewPager = (NoSlidingViewPaper) findViewById(R.id.vp_main_container);

        final ArrayList<Fragment> fgLists = new ArrayList<>(2);
        fgLists.add(new MessageUnusedFragment());
        fgLists.add(new MessageUsedFragment());

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }

            @Override
            public int getCount() {
                return fgLists.size();
            }
        };

        mViewPager.setAdapter(mAdapter);

        mViewPager.setOffscreenPageLimit(1);

        mIndicator.setViewPager(mViewPager, 0);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
