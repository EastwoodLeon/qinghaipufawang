package com.pxkeji.qinghaipufawang;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;


public class MapActivity extends AppCompatActivity {

    public static final String LNG = "lng";
    public static final String LAT = "lat";
    public static final String TITLE = "title";

    private MapView mMapView;
    private AMap mAMap;
    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        initActionBar();

        mTvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        initMap();

        position();
    }




    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_types, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normal:
                mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
                mTvTitle.setText("地图");
                break;
            case R.id.satellite:
                mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                mTvTitle.setText("卫星地图");
                break;
            case R.id.night:
                mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
                mTvTitle.setText("夜间地图");
                break;
            default:
                break;
        }
        item.setChecked(true);
        return true;
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            CameraUpdate cu = CameraUpdateFactory.zoomTo(15);
            mAMap.moveCamera(cu);
        }
    }

    private void position() {
        Intent intent = getIntent();
        double lng = intent.getDoubleExtra(LNG, 0);
        double lat = intent.getDoubleExtra(LAT, 0);
        String title = intent.getStringExtra(TITLE);
        LatLng pos = new LatLng(lat, lng);
        CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
        mAMap.moveCamera(cu);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        markerOptions.title(title);
        markerOptions.snippet("摘录");
        Marker marker = mAMap.addMarker(markerOptions);
        marker.showInfoWindow();
    }
}
