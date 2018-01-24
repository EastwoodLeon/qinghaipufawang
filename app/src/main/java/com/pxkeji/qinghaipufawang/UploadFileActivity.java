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

public class UploadFileActivity extends AppCompatActivity {

    private TextInputEditText mEtName;
    private TextInputEditText mEtPhone;
    private TextInputEditText mEtDepartment;
    private TextInputEditText mEtTitle;
    private Button mBtnSubmit;

    private String mName;
    private String mPhone;
    private String mDepartment;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        initActionBar();

        mEtName = (TextInputEditText) findViewById(R.id.ti_name);
        mEtPhone = (TextInputEditText) findViewById(R.id.ti_phone);
        mEtDepartment = (TextInputEditText) findViewById(R.id.ti_department);
        mEtTitle = (TextInputEditText) findViewById(R.id.ti_title);
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

    private boolean isSubmitOk() {
        if (TextUtils.isEmpty(mName)) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mPhone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mDepartment)) {
            Toast.makeText(this, "请输入部门", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mTitle)) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setData() {
        mName = mEtName.getText().toString().trim();
        mPhone = mEtPhone.getText().toString().trim();
        mDepartment = mEtDepartment.getText().toString().trim();
        mTitle = mEtTitle.getText().toString().trim();
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
