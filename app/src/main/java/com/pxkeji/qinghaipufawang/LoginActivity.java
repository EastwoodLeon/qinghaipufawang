package com.pxkeji.qinghaipufawang;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pxkeji.qinghaipufawang.ui.loader.LatteLoader;
import com.pxkeji.qinghaipufawang.util.EncryptUtil;
import com.pxkeji.qinghaipufawang.util.HttpUtil;
import com.pxkeji.qinghaipufawang.util.LogUtil;
import com.pxkeji.qinghaipufawang.util.StringUtil;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private boolean mIsSubmitOk = true;

    private TextInputEditText mTiAccount;
    private TextInputEditText mTiPassword;
    private Button mBtnSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.w("aaa", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initActionBar();

        mTiAccount = (TextInputEditText) findViewById(R.id.ti_account);
        mTiPassword = (TextInputEditText) findViewById(R.id.ti_password);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);

        mTiAccount.setSelection(mTiAccount.getText().toString().length());

        mTiAccount.addTextChangedListener(this);
        mTiPassword.addTextChangedListener(this);

        mTiPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    login();
                }
                return false;
            }
        });

        mBtnSubmit.setOnClickListener(this);



    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkSubmitState();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                login();
                break;
            default:
                break;
        }
    }

    private void checkSubmitState() {
        LogUtil.w("aaa", "checkSubmitState");
        mIsSubmitOk = isSubmitOk();
        int textColor;
        if (mIsSubmitOk) {
            textColor = ResourcesCompat.getColor(getResources(), R.color.activePrimaryButtonTextColor, null);
        } else {
            textColor = ResourcesCompat.getColor(getResources(), R.color.inactivePrimaryButtonTextColor, null);
        }

        mBtnSubmit.setTextColor(textColor);
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

    private boolean isSubmitOk() {
        if (TextUtils.isEmpty(mTiAccount.getText().toString())) {
            return false;
        }

        if (TextUtils.isEmpty(mTiPassword.getText().toString())) {
            return false;
        }

        return true;
    }

    private void login() {
        if (mIsSubmitOk) {
            LatteLoader.showLoading(this);

            String url = StringUtil.addURLParam(Config.USER_API_URL, "action", "login");

            url = StringUtil.addURLParam(url, "uid", mTiAccount.getText().toString());
            url = StringUtil.addURLParam(url, "upwd", mTiPassword.getText().toString());
            LogUtil.w("url", url);

            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LatteLoader.stopLoading();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();

                    try {
                        JSONObject responseObject = new JSONObject(responseText);

                        if ("10104".equals(responseObject.getString("code"))) {
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences(LoginActivity.this)
                                            .edit();
                            editor.putInt("user_id", responseObject.getInt("userno"));
                            editor.apply();

                            sendBroadcast(new Intent("com.pxkeji.qinghaipufawang.LOG_IN"));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LatteLoader.stopLoading();
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LatteLoader.stopLoading();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder
                                            //.setTitle(getResources().getString(R.string.app_name))
                                            .setMessage("用户名或密码不正确")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mTiPassword.setText("");
                                                    mTiPassword.requestFocus();
                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .show();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            /*
            LatteLoader.showLoading(this);

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {


                    SharedPreferences.Editor editor =
                            PreferenceManager.getDefaultSharedPreferences(LoginActivity.this)
                                    .edit();
                    editor.putInt("user_id", 20);
                    editor.apply();

                    sendBroadcast(new Intent("com.pxkeji.qinghaipufawang.LOG_IN"));
                    LatteLoader.stopLoading();
                    finish();
                }
            };
            new Timer().schedule(timerTask, 2000);
            */
        }
    }

}
