package com.pxkeji.qinghaipufawang;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pxkeji.qinghaipufawang.ui.loader.LatteLoader;
import com.pxkeji.qinghaipufawang.util.MyDbHelper;

public class ChangeUserNameActivity extends AppCompatActivity {

    public static final String NAME = "name";

    private MyDbHelper mDbHelper;

    private TextInputEditText mEtName;

    private int mUserId;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_name);

        initActionBar();

        mEtName = (TextInputEditText) findViewById(R.id.ti_account);

        String name = getIntent().getStringExtra(NAME);
        if (name != null) {
            mEtName.setText(name);
            mEtName.setSelection(name.length());
        }


        mDbHelper = new MyDbHelper(this, "qinghaipufawang.db", null, Config.DB_VERSION);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mUserId = prefs.getInt("user_id", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_change_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                setData();
                if (isSubmitOk()) {
                    LatteLoader.showLoading(this);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("name", mUserName);
                    db.update("users", values, "id = ?", new String[]{String.valueOf(mUserId)});

                    Intent intent = new Intent();
                    intent.putExtra("name", mUserName);
                    setResult(RESULT_OK, intent);
                    sendBroadcast(new Intent("com.pxkeji.qinghaipufawang.LOG_IN"));
                    sendBroadcast(new Intent("com.pxkeji.qinghaipufawang.SETTING_USER_INFO_CHANGED"));
                    LatteLoader.stopLoading();
                    finish();
                }

                break;
            default:
                break;
        }
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

    private boolean isSubmitOk() {
        if (TextUtils.isEmpty(mUserName)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setData() {
        mUserName = mEtName.getText().toString().trim();
    }
}
