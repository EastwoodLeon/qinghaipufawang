package com.pxkeji.qinghaipufawang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pxkeji.qinghaipufawang.util.LogUtil;
import com.pxkeji.qinghaipufawang.util.MyDbHelper;

import java.util.Timer;
import java.util.TimerTask;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayoutCompat mLlUserInfoContainer;
    private TextView mTvName;
    private TextView mTvIntro;

    private Button mBtnLogout;

    private MyDbHelper mDbHelper;

    private IntentFilter mIntentFilter;
    private InfoReceiver mInfoReceiver;

    private AlertDialog DIALOG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initActionBar();

        mLlUserInfoContainer = (LinearLayoutCompat) findViewById(R.id.ll_user_info_container);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvIntro = (TextView) findViewById(R.id.tv_intro);

        mBtnLogout = (Button) findViewById(R.id.btn_logout);

        mDbHelper = new MyDbHelper(this, "qinghaipufawang.db", null, Config.DB_VERSION);

        refresh();

        mLlUserInfoContainer.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.pxkeji.qinghaipufawang.SETTING_USER_INFO_CHANGED");
        mInfoReceiver = new InfoReceiver();
        registerReceiver(mInfoReceiver, mIntentFilter);

        this.DIALOG = new AlertDialog.Builder(this).create();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_info_container:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.btn_logout:
                DIALOG.show();

                final Window window = DIALOG.getWindow();
                if (window != null) {
                    window.setContentView(R.layout.dialog_logout_panel);
                    window.setGravity(Gravity.BOTTOM);
                    window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    // 设置属性
                    final WindowManager.LayoutParams params = window.getAttributes();
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    params.dimAmount = 0.5f;
                    window.setAttributes(params);

                    window.findViewById(R.id.photodialog_btn_cancel).setOnClickListener(this);
                    window.findViewById(R.id.photodialog_btn_ok).setOnClickListener(this);

                }

                /* 普通对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder

                        .setMessage("确定要退出登录吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit();
                                editor.remove("user_id");
                                editor.apply();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                        */
                break;
            case R.id.photodialog_btn_ok:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit();
                editor.remove("user_id");
                editor.apply();
                sendBroadcast(new Intent("com.pxkeji.qinghaipufawang.LOG_IN"));

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                        finish();

                    }
                }, 500);
                Toast.makeText(SettingActivity.this, "登出成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.photodialog_btn_cancel:
                DIALOG.cancel();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mInfoReceiver);
    }

    private void getUserInfo(final int userId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 先从数据库里查
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                Cursor cursor = db.query("users", null, "id = ?",
                        new String[] {String.valueOf(userId)},
                        null, null, null);

                if (cursor.moveToFirst()) {
                    LogUtil.w("MyFragment", "从数据库里查到了数据");
                    final String name = cursor.getString(cursor.getColumnIndex("name"));

                    final String intro = cursor.getString(cursor.getColumnIndex("intro"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvName.setText(name);

                            mTvIntro.setText(intro);


                        }
                    });
                } else {
                    //queryFromServer(userId);
                }
            }
        }).start();

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

    private void refresh() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = prefs.getInt("user_id", 0);

        if (userId != 0) {
            getUserInfo(userId);
        }

    }

    class InfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    }
}
