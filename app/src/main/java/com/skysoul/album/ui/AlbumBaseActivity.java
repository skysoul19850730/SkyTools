package com.skysoul.album.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;

import com.skysoul.album.OnCropCallback;
import com.skysoul.album.OnSelectedCallback;
import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.core.AlbumCallbackInterface;
import com.skysoul.album.core.MediaLoadManager;
import com.skysoul.album.core.MediaOptions;
import com.skysoul.album.util.AlbumConstant;

import java.util.ArrayList;

abstract class AlbumBaseActivity extends Activity {

    private long lastClickTime; // 整个界面连续点击判断
    protected boolean mUseLandscape;
    protected MediaOptions mMediaOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸式状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        mMediaOptions = (MediaOptions) getIntent().getSerializableExtra(AlbumConstant.key_media_option);
        // 横屏还是竖屏
        mUseLandscape = getIntent().getBooleanExtra(AlbumConstant.key_use_screen_orientation_landscape, false);
        if (mUseLandscape) {
//            // 横屏隐藏状态栏
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(getLayoutId());
    }

    protected abstract int getLayoutId();

    @Override
    protected void onResume() {
        super.onResume();
        if (mUseLandscape) {
            // 隐藏状态栏 和 底部导航栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(Color.TRANSPARENT);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    // 该窗口始终允许延伸到屏幕短边上的DisplayCutout区域。
                    lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                }
                getWindow().setAttributes(lp);
                int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
                uiOptions = uiOptions | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        } else {
            // 隐藏底部导航栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(Color.TRANSPARENT);
                int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
                uiOptions = uiOptions | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
                getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        }
    }

    protected boolean isDoubleClick() {
        long curClickTime = SystemClock.elapsedRealtime();
        if (curClickTime - lastClickTime < 400) return true;
        lastClickTime = curClickTime;
        return false;
    }

    public void setStatusBarMode(boolean isLightMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            int vis = decorView.getSystemUiVisibility();
            if (isLightMode) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
    }

    protected void compressAndCallback(ArrayList<MediaInfo> list, final AlbumCallbackInterface albumCallback) {
        if (albumCallback != null) {
            if (mMediaOptions.isUseCompress) {
                MediaLoadManager.compress(this, list, mMediaOptions.imageLimitSize, new MediaLoadManager.CompressCallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinished(final ArrayList<MediaInfo> list) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (AlbumBaseActivity.this.isFinishing()) return;
                                invokeCallback(albumCallback, list);
                            }
                        });
                    }
                });
            } else {
                invokeCallback(albumCallback, list);
            }
        }
    }

    protected void invokeCallback(AlbumCallbackInterface albumCallback, ArrayList<MediaInfo> list) {
        if (albumCallback instanceof OnSelectedCallback) {
            ((OnSelectedCallback) albumCallback).onSuccess(list);
        } else if (albumCallback instanceof OnCropCallback) {
            ((OnCropCallback) albumCallback).onSuccess(list.get(0));
        }
        finish();
    }
}
