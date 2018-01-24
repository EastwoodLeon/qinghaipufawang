package com.pxkeji.qinghaipufawang;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pxkeji.qinghaipufawang.ui.fragment.BaseFragment;
import com.pxkeji.qinghaipufawang.util.EncryptUtil;
import com.pxkeji.qinghaipufawang.util.HttpUtil;
import com.pxkeji.qinghaipufawang.util.LogUtil;
import com.pxkeji.qinghaipufawang.util.MyDbHelper;
import com.pxkeji.qinghaipufawang.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/10.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {

    private IntentFilter mIntentFilter;
    private LoginReceiver mLoginReceiver;

    private ImageView mIvSetting;
    private ImageView mIvMessage;
    private RelativeLayout mRlHeaderContainer;
    private RelativeLayout mRlFile;
    private RelativeLayout mRlPassword;
    private TextView mTvUserName;

    private MyDbHelper mDbHelper;

    @Override
    protected void initView() {
        mIvSetting = (ImageView) mView.findViewById(R.id.iv_setting);
        mIvMessage = (ImageView) mView.findViewById(R.id.iv_message);
        mRlHeaderContainer = (RelativeLayout) mView.findViewById(R.id.rl_header_container);
        mRlFile = (RelativeLayout) mView.findViewById(R.id.rl_file);
        mRlPassword = (RelativeLayout) mView.findViewById(R.id.rl_password);
        mTvUserName = (TextView) mView.findViewById(R.id.appCompatTextView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void getDataFromServer() {
        refresh();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIvSetting.setOnClickListener(this);
        mIvMessage.setOnClickListener(this);
        mRlHeaderContainer.setOnClickListener(this);
        mRlFile.setOnClickListener(this);
        mRlPassword.setOnClickListener(this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.pxkeji.qinghaipufawang.LOG_IN");
        mLoginReceiver = new LoginReceiver();
        mContext.registerReceiver(mLoginReceiver, mIntentFilter);

        mDbHelper = new MyDbHelper(mContext, "qinghaipufawang.db", null, Config.DB_VERSION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mLoginReceiver);
    }

    @Override
    public void onClick(View v) {
        if (isLogged()) {
            switch (v.getId()) {
                case R.id.iv_setting:
                    startActivity(new Intent(mContext, SettingActivity.class));
                    break;
                case R.id.iv_message:
                    LogUtil.w("", "message clicked");
                    startActivity(new Intent(mContext, MessageActivity.class));
                    break;
                case R.id.rl_header_container:
                    startActivity(new Intent(mContext, UserInfoActivity.class));
                    break;
                case R.id.rl_file:
                    startActivity(new Intent(mContext, UploadFileActivity.class));
                    break;
                case R.id.rl_password:
                    startActivity(new Intent(mContext, ChangePasswordActivity.class));
                    break;
                default:
                    break;
            }
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvUserName.setText(name);
                        }
                    });
                } else {
                    queryFromServer(userId);
                }
            }
        }).start();

    }

    private boolean isLogged() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        int userId = prefs.getInt("user_id", 0);
        if (userId == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void queryFromServer(final int userId) {
        String url = StringUtil.addURLParam(Config.USER_API_URL, "action", "query_user_msg");
        url = StringUtil.addURLParam(url, "uid", String.valueOf(userId));

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();

                try {
                    JSONObject responseObject = new JSONObject(responseText);
                    final String userName = responseObject.getString("userno");



                    //存入数据库
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("id", userId);
                    values.put("name", userName);
                    long result = db.insert("users", null, values);
                    if (result > 0) {
                        //保存成功后
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getUserInfo(userId);
                            }
                        });
                    } else {
                        LogUtil.w("MyFragment", "用户数据insert失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        });
    }

    private void refresh() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        int userId = prefs.getInt("user_id", 0);

        if (userId != 0) {
            getUserInfo(userId);
        } else {
            mTvUserName.setText("立即登录");
        }

    }

    class LoginReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    }




}
