package com.pxkeji.qinghaipufawang;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pxkeji.qinghaipufawang.util.DateDialogUtil;
import com.pxkeji.qinghaipufawang.util.LogUtil;
import com.pxkeji.qinghaipufawang.util.MyDbHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private static final int CHANGE_NAME = 3;
    private static final int CHANGE_GENDER = 4;
    private static final int CHANGE_INTRO = 5;

    private MyDbHelper mDbHelper;

    private AlertDialog DIALOG;

    private Uri mImageUri;

    private RelativeLayout mRlAvatarContainer;
    private CircleImageView mIvAvatar;
    private RelativeLayout mRlName;
    private TextView mTvUserName;
    private RelativeLayout mRlGender;
    private TextView mTvGender;
    private RelativeLayout mRlIntro;
    private TextView mTvIntro;
    private RelativeLayout mRlBirthday;
    private TextView mTvBirthday;

    private int mYear;
    private int mMonth;
    private int mDay;

    private DateDialogUtil mDateDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initActionBar();

        mRlAvatarContainer = (RelativeLayout) findViewById(R.id.rl_header_container);
        mIvAvatar = (CircleImageView) findViewById(R.id.iv_avatar);
        mRlName = (RelativeLayout) findViewById(R.id.rl_name);
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mRlGender = (RelativeLayout) findViewById(R.id.rl_gender);
        mTvGender = (TextView) findViewById(R.id.tv_gender);
        mRlIntro = (RelativeLayout) findViewById(R.id.rl_intro);
        mTvIntro = (TextView) findViewById(R.id.tv_intro);
        mRlBirthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        mTvBirthday = (TextView) findViewById(R.id.tv_birthday);

        mDbHelper = new MyDbHelper(this, "qinghaipufawang.db", null, Config.DB_VERSION);

        this.DIALOG = new AlertDialog.Builder(this).create();

        mDateDialogUtil = new DateDialogUtil();
        mDateDialogUtil.setIDateListener(new DateDialogUtil.IDateListener() {
            @Override
            public void onDateChange(String date) {
                Toast.makeText(UserInfoActivity.this, date, Toast.LENGTH_SHORT).show();
            }
        });

        refresh();

        mRlAvatarContainer.setOnClickListener(this);
        mRlName.setOnClickListener(this);
        mRlGender.setOnClickListener(this);
        mRlIntro.setOnClickListener(this);
        mRlBirthday.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        LogUtil.w("onClick", v.getId() + "");
        switch (v.getId()) {
            case R.id.rl_header_container:
                DIALOG.show();

                final Window window = DIALOG.getWindow();
                if (window != null) {
                    window.setContentView(R.layout.dialog_camera_panel);
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
                    window.findViewById(R.id.photodialog_btn_take).setOnClickListener(this);
                    window.findViewById(R.id.photodialog_btn_native).setOnClickListener(this);
                }
                break;
            case R.id.rl_name:
                Intent intent = new Intent(this, ChangeUserNameActivity.class);
                intent.putExtra(ChangeUserNameActivity.NAME, mTvUserName.getText().toString());
                startActivityForResult(intent, CHANGE_NAME);
                break;
            case R.id.rl_gender:
                Intent intentGender = new Intent(this, ChangeGenderActivity.class);
                intentGender.putExtra(ChangeGenderActivity.GENDER, mTvGender.getText().toString());
                startActivityForResult(intentGender, CHANGE_GENDER);
                break;
            case R.id.rl_intro:
                Intent intentIntro = new Intent(this, ChangeIntroActivity.class);
                intentIntro.putExtra(ChangeIntroActivity.INTRO, mTvIntro.getText().toString());
                startActivityForResult(intentIntro, CHANGE_INTRO);
                break;
            case R.id.rl_birthday:
                mDateDialogUtil.showDialog(this);
                break;
            case R.id.photodialog_btn_cancel:
                DIALOG.cancel();
                break;
            case R.id.photodialog_btn_take:
                takePhoto();
                DIALOG.cancel();
                break;
            case R.id.photodialog_btn_native:
                pickPhoto();
                DIALOG.cancel();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                        // 用Glide报错
                        //Glide.with(this).load(bitmap).into(mIvTest);
                        mIvAvatar.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4 及以上
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case CHANGE_NAME:
                if (resultCode == RESULT_OK) {
                    mTvUserName.setText(data.getStringExtra("name"));
                }
                break;
            case CHANGE_GENDER:
                if (resultCode == RESULT_OK) {
                    mTvGender.setText(data.getStringExtra("gender"));
                }
                break;
            case CHANGE_INTRO:
                if (resultCode == RESULT_OK) {
                    mTvIntro.setText(data.getStringExtra("intro"));
                }
                break;
            default:
                break;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }
                break;
            default:
                break;
        }
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mIvAvatar.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
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
                    String gender = cursor.getString(cursor.getColumnIndex("gender"));
                    final String genderF;
                    if ("女".equals(gender)) {
                        genderF = "女";
                    }else {
                        genderF = "男";
                    }
                    final String intro = cursor.getString(cursor.getColumnIndex("intro"));
                    final int year = cursor.getInt(cursor.getColumnIndex("year"));
                    final int month = cursor.getInt(cursor.getColumnIndex("month"));
                    final int day = cursor.getInt(cursor.getColumnIndex("day"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvUserName.setText(name);
                            mTvGender.setText(genderF);
                            mTvIntro.setText(intro);
                            if (year > 0 && month > 0 && day > 0) {
                                mTvBirthday.setText(month + "月" + day + "日");
                            }

                        }
                    });
                } else {
                    //queryFromServer(userId);
                }
            }
        }).start();

    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }

        displayImage(imagePath);
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

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    private void pickPhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }



    private void refresh() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = prefs.getInt("user_id", 0);

        if (userId != 0) {
            getUserInfo(userId);
        }

    }

    private void takePhoto() {
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            mImageUri = FileProvider.getUriForFile(this,
                    "com.pxkeji.qinghaipufawang.fileprovider", outputImage);
        } else {
            mImageUri = Uri.fromFile(outputImage);
        }

        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
}
