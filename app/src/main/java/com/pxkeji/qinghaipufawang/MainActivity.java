package com.pxkeji.qinghaipufawang;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.pxkeji.qinghaipufawang.ui.view.NoSlidingViewPaper;
import com.pxkeji.qinghaipufawang.util.BottomNavigationViewHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NoSlidingViewPaper mViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_cinema:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_discovery:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_my:
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (NoSlidingViewPaper) findViewById(R.id.vp_main_container);

        final ArrayList<Fragment> fgLists = new ArrayList<>(4);
        fgLists.add(new NewsFragment());
        fgLists.add(new ServiceFragment());
        fgLists.add(new ConvenienceFragment());
        fgLists.add(new MyFragment());

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

        mViewPager.setOffscreenPageLimit(3);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

    }


}
