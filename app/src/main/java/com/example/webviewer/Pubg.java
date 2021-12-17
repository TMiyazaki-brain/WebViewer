//Copyright by T.Miyazaki
package com.example.webviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;

public class Pubg extends AppCompatActivity {

    //変数定義
    private WebView web;
    //追加部分
    public Context context;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    // the same for Android 5.0 methods only
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    //追加部分

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // タイトルバーを消す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを消す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubg);

        web = (WebView) findViewById(R.id.pubg_web);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("https://liquipedia.net/pubg/Main_Page");

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_pubg);
        bottomNavigationView.setSelectedItemId(R.id.pubg);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.youtube:
                        startActivity(new Intent(getApplication()
                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.pubg:
                        return true;

                    case R.id.apex:
                        startActivity(new Intent(getApplication()
                                ,Apex.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //追加項目
        web.setWebChromeClient(new WebChromeClient() {
            // ロリポップ用、オールインワン
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    // 写真を保存するためのファイルを作成
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // ファイル作成時にエラーが発生しました
                        Log.e(TAG, "Unable to create Image File", ex);
                    }

                    // ファイルの作成に成功した場合のみ継続
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.image_chooser));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                return true;
            }

            // 画像ファイルの作成（Lollipopのみ）
            private File createImageFile() throws IOException {

                File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                if (!imageStorageDir.exists()) {
                    imageStorageDir.mkdirs();
                }

                // イメージファイル名をつける
                imageStorageDir = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                return imageStorageDir;
            }

            // openFileChooser（アンドロイド 3.0以上用
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;

                try {
                    File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs();
                    }

                    File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                    mCapturedImageURI = Uri.fromFile(file); // save to the private variable

                    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    // captureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(i, getString(R.string.image_chooser));
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Camera Exception:" + e, Toast.LENGTH_LONG).show();
                }

            }

            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            // openFileChooser（他のAndroidバージョン用
            /* may not work on KitKat due to lack of implementation of openFileChooser() or onShowFileChooser()
               https://code.google.com/p/android/issues/detail?id=62220
               however newer versions of KitKat fixed it on some devices */
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String
                    capture) {
                openFileChooser(uploadMsg, acceptType);
            }

        });
    }

    // カメラまたはSDカードからファイルを選択した場合、ここに戻る
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // code for all versions except of Lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }

                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e, Toast.LENGTH_LONG).show();
                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }

        } // ここまでLollipopを除くすべてのバージョンのコード

        // ここからはLollipop専用コード
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != FILECHOOSER_RESULTCODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // レスポンスの良さを確認する
            if (resultCode == Activity.RESULT_OK) {
                if (data == null || data.getData() == null) {
                    // if there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } // ここまでLollipop専用コード
    }
    //追加項目

    public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
        } else {
            super.onBackPressed();
        }
    }
}