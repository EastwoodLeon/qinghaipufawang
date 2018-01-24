package com.pxkeji.qinghaipufawang;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pxkeji.qinghaipufawang.ui.loader.LatteLoader;
import com.pxkeji.qinghaipufawang.util.MyDbHelper;

import java.util.List;

public class ChangeGenderActivity extends AppCompatActivity {

    public static final String GENDER = "gender";

    private ListView mListView;

    private ArrayAdapter<String> mAdapter;

    private String[] mList = {
            "男", "女"
    };

    private int mIndex;

    private MyDbHelper mDbHelper;

    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_gender);

        initActionBar();

        setIndex();

        mListView = (ListView) findViewById(R.id.list_view);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, mList);
        mListView.setAdapter(mAdapter);
        //mListView.setItemsCanFocus(true);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIndex = position;
            }
        });
        mListView.setItemChecked(mIndex, true);

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

                LatteLoader.showLoading(this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("gender", mList[mIndex]);
                db.update("users", values, "id = ?", new String[]{String.valueOf(mUserId)});

                Intent intent = new Intent();
                intent.putExtra("gender", mList[mIndex]);
                setResult(RESULT_OK, intent);
                LatteLoader.stopLoading();
                finish();

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

    private void setIndex() {
        String gender = getIntent().getStringExtra(GENDER);
        int count = mList.length;
        for (int i = 0; i < count; i++) {
            if (mList[i].equals(gender)) {
                mIndex = i;
                return;
            }
        }

        mIndex = 0;
    }
}
