package com.pxkeji.qinghaipufawang;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pxkeji.qinghaipufawang.ui.loader.LatteLoader;

import java.util.Timer;
import java.util.TimerTask;

public class ChangePasswordActivity extends AppCompatActivity {

    private String mPasswordOld;
    private String mPasswordNew;
    private String mPasswordConfirm;

    private TextInputEditText mEtPasswordOld;
    private TextInputEditText mEtPasswordNew;
    private TextInputEditText mEtPasswordConfirm;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initActionBar();

        mEtPasswordOld = (TextInputEditText) findViewById(R.id.ti_password_old);
        mEtPasswordNew = (TextInputEditText) findViewById(R.id.ti_password_new);
        mEtPasswordConfirm = (TextInputEditText) findViewById(R.id.ti_password_confirm);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();


                if (isSubmitOk()) {
                    submit();
                }
                
            }
        });
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
        if (TextUtils.isEmpty(mPasswordOld)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mPasswordNew)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mPasswordConfirm)) {
            Toast.makeText(this, "请确认密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mPasswordConfirm.equals(mPasswordNew)) {
            Toast.makeText(this, "密码输入不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setData() {
        mPasswordOld = mEtPasswordOld.getText().toString().trim();

        mPasswordNew = mEtPasswordNew.getText().toString().trim();
        mPasswordConfirm = mEtPasswordConfirm.getText().toString().trim();

    }

    private void submit() {
        LatteLoader.showLoading(this);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                LatteLoader.stopLoading();
                finish();
            }
        }, 2000);
    }
}
