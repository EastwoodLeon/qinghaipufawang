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
import com.pxkeji.qinghaipufawang.util.StringUtil;

public class ChangeIntroActivity extends AppCompatActivity {

    public static final String INTRO = "intro";
    private MyDbHelper mDbHelper;
    private TextInputEditText mEtIntro;

    private int mUserId;
    private String mIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_intro);
        initActionBar();

        mEtIntro = (TextInputEditText) findViewById(R.id.ti_intro);
        String intro = getIntent().getStringExtra(INTRO);
        if (intro != null) {
            mEtIntro.setText(intro);
            mEtIntro.setSelection(intro.length());
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
                    values.put("intro", mIntro);
                    db.update("users", values, "id = ?", new String[]{String.valueOf(mUserId)});

                    Intent intent = new Intent();
                    intent.putExtra("intro", mIntro);
                    setResult(RESULT_OK, intent);
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
        if (StringUtil.length(mIntro) > 100) {
            Toast.makeText(this, "最多支持50个中文或100个英文，请修改后再试", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setData() {
        mIntro = mEtIntro.getText().toString().trim();
    }
}
