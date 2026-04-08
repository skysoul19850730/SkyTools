package com.skysoul.album.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skysoul.album.CropRatio;
import com.skysoul.album.OnCropCallback;
import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.core.CoreAlbumManager;
import com.skysoul.album.core.MediaOptions;
import com.skysoul.album.core.ugckit.utils.CoverUtil;
import com.skysoul.album.util.AlbumConstant;
import com.skysoul.album.util._AlbumUtils;
import com.skysoul.album.view.ucrop.callback.BitmapCropCallback;
import com.skysoul.album.view.ucrop.view.OverlayView;
import com.skysoul.album.view.ucrop.view.TransformImageView;
import com.skysoul.album.view.ucrop.view.UCropView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片裁剪
 *  1. CropAction 外部打开
 *  2. 内部跳转：ImageBrowserActivity 打开裁剪
 *              VideoCoverActivity   打开裁剪
 */
public class ImageCropActivity extends AlbumBaseActivity {

    public static void launchActivityForResult(Activity activity, MediaInfo mediaInfo,
                                               MediaOptions mediaOptions, boolean useLandscape) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(AlbumConstant.key_media_info, mediaInfo);
        intent.putExtra(AlbumConstant.key_media_option, mediaOptions);
        intent.putExtra(AlbumConstant.key_use_screen_orientation_landscape, useLandscape);
        activity.startActivityForResult(intent, AlbumConstant.CROP_REQUEST_CODE);
    }

    private boolean isFromOther = false;        // 是否内部打开的裁剪
    private boolean isCropOriginal = false;       // 是否是原始尺寸
    private UCropView mUCropView;
    private LinearLayout mRatioListLayout;
    private MediaInfo mMediaInfo;
    private OnCropCallback mOnCropCallback;

    @Override
    protected int getLayoutId() {
        return _AlbumUtils.getResLayoutId(this, "album_activity_image_crop_layout");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFromOther = getIntent().getBooleanExtra(AlbumConstant.key_from_other, false);
        mMediaInfo = getIntent().getParcelableExtra(AlbumConstant.key_media_info);
        if (isFromOther) {
            int index = getIntent().getIntExtra(AlbumConstant.key_index, 0);
            if (index > 0) {
                mOnCropCallback = (OnCropCallback) CoreAlbumManager.getInstance().getAlbumCallback(index);
            }
        }

        if (mUseLandscape) {
            findViewById(_AlbumUtils.getResViewId(this, "bottom_layout")).setPadding(0, 0, 0, _AlbumUtils.dp2px(20));
        }

        mUCropView = findViewById(_AlbumUtils.getResViewId(this, "_album_cropView"));
        mRatioListLayout = findViewById(_AlbumUtils.getResViewId(this, "_album_ratio_list"));
        initRatioLayout(mMediaOptions.cropRatios,mMediaOptions.defaultCropRatio,mMediaOptions.showOriginalCropRatio);


        if (mMediaInfo.isVideo()) {
            // 如果是视频则裁剪封面
            if (TextUtils.isEmpty(mMediaInfo.coverPath)) {
                // 获取视频封面
                CoverUtil.getInstance().setInputPath(mMediaInfo.path);
                CoverUtil.getInstance().createThumbFile(new CoverUtil.ICoverListener() {
                    @Override
                    public void onCoverPath(String coverPath) {
                        if (ImageCropActivity.this.isFinishing()) return;
                        mMediaInfo.coverPath = coverPath;
                        setCropPath(mMediaInfo.coverPath);
                    }
                });
            } else {
                setCropPath(mMediaInfo.coverPath);
            }
        } else {
            setCropPath(mMediaInfo.path);
        }

        mUCropView.getCropImageView().setTransformImageListener(transformImageListener);
        // 禁止旋转
        mUCropView.getCropImageView().setRotateEnabled(false);


        findViewById(_AlbumUtils.getResViewId(this, "_album_iv_save")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCropOriginal) {
                    onSuccess();
                } else{
                    saveImage();
                }
            }
        });

        findViewById(_AlbumUtils.getResViewId(this, "_album_iv_close")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // 设置裁剪的图片路径和裁剪后保存的路径
    private void setCropPath(String cropPath) {
        mUCropView.getCropImageView().setImageUri(
                Uri.fromFile(new File(cropPath)),
                Uri.fromFile(new File(_AlbumUtils.getImagePath(this), "crop_"+System.currentTimeMillis()+".jpg"))
        );
    }

    private TransformImageView.TransformImageListener transformImageListener = new TransformImageView.TransformImageListener() {
        @Override
        public void onLoadComplete() {

        }

        @Override
        public void onLoadFailure(Exception e) {

        }

        @Override
        public void onRotate(float currentAngle) {

        }

        @Override
        public void onScale(float currentScale) {

        }
    };

    private void saveImage() {
        mUCropView.getCropImageView().cropAndSaveImage(Bitmap.CompressFormat.JPEG, 100, new BitmapCropCallback() {
            @Override
            public void onBitmapCropped(Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                if (resultUri != null) {
                    mMediaInfo.cropPath = resultUri.getPath();
                    mMediaInfo.width = imageWidth;
                    mMediaInfo.height = imageHeight;
                    mMediaInfo.fileSize = new File(mMediaInfo.cropPath).length();
                    mMediaInfo.compressPath = "";
                }
                onSuccess();
            }
            @Override
            public void onCropFailure(Throwable t) {
                Toast.makeText(ImageCropActivity.this, "图片剪裁失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSuccess() {
        if (mMediaInfo.width <= 0 || mMediaInfo.height <= 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mMediaInfo.getShowPath(), options);
            mMediaInfo.width = options.outWidth;
            mMediaInfo.height = options.outHeight;
        }
        if (isFromOther) {
            ArrayList<MediaInfo> list = new ArrayList<>();
            list.add(mMediaInfo);
            compressAndCallback(list, mOnCropCallback);
        } else {
            Intent intent = new Intent();
            intent.putExtra(AlbumConstant.key_media_info, mMediaInfo);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // 初始化比例布局
    public void initRatioLayout(CropRatio[] ratios,CropRatio defaultCropRatio,boolean showOriginalCropRatio) {
        int size = 0;
        if (ratios != null) {
            size = ratios.length;
        }

        if (size == 1 && ratios[0] == CropRatio.RATIO_CUSTOM) {
            // 如果使用了自定义裁剪比例，则只显示自定义裁剪比例
            setRatio(ratios[0]);
            findViewById(_AlbumUtils.getResViewId(this, "tv_crop_size")).setVisibility(View.GONE);
        } else {
            // 添加比例按钮
            CropRatio cropRatio = null;
            CropRatio defaultRatio = null;
            for (int i = -1; i < size; i++) {
                if (i >= 0) {
                    cropRatio = ratios[i];
                }
                if(i<0 && size>0 && !showOriginalCropRatio){//传了其他比例，并且设置不显示“原始”,则不添加原始
                    continue;
                }
                TextView textView = new TextView(this);
                if (cropRatio == null) {
                    textView.setText("原始");
                } else {
                    textView.setText(cropRatio.name);
                }
                textView.setTextSize(13f);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.WHITE);
                textView.setTag(cropRatio);
                textView.setPadding(_AlbumUtils.dp2px(15f), 0, _AlbumUtils.dp2px(12f), 0);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRatio((CropRatio) v.getTag());
                    }
                });
                mRatioListLayout.addView(textView, new ViewGroup.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, _AlbumUtils.dp2px(35f)));
                if(cropRatio!=null && defaultCropRatio!=null && cropRatio.getRatio()== defaultCropRatio.getRatio()){//防止传的默认不在列表里
                    defaultRatio = cropRatio;
                }
            }
            if(defaultRatio==null && size>0 && !showOriginalCropRatio){
                defaultRatio = ratios[0];
            }


            // 默认原始尺寸
            setRatio(defaultRatio);
        }
    }

    private void setRatio(CropRatio cropRatio) {
        isCropOriginal = cropRatio == null;
        if (isCropOriginal) {
            // 原始尺寸
            mUCropView.getCropImageView().setTargetAspectRatio(0);
            mUCropView.getOverlayView().setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_DISABLE);
        } else {
            if (cropRatio == CropRatio.RATIO_0_0) {
                mUCropView.getOverlayView().setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE);
            } else {
                mUCropView.getCropImageView().setTargetAspectRatio(cropRatio.getRatio());
                mUCropView.getOverlayView().setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_DISABLE);
            }
        }

        mUCropView.getOverlayView().setVisibility(isCropOriginal? View.GONE : View.VISIBLE);
        mUCropView.getOverlayView().setShowCropGrid(!isCropOriginal);
        mUCropView.getOverlayView().setShowCropFrame(!isCropOriginal);

        int childCount = mRatioListLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView child = (TextView) mRatioListLayout.getChildAt(i);
            if (child.getTag() == cropRatio) {
                child.setTextColor(Color.parseColor("#ff505e"));
            } else {
                child.setTextColor(Color.WHITE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isFromOther) {
            invokeCallbackFail(CoreAlbumManager.code_cancel);
        }
        super.onBackPressed();
    }


    private void invokeCallbackFail(int code) {
        if (mOnCropCallback != null) {
            mOnCropCallback.onFail(code, CoreAlbumManager.getFailMsg(code));
        }
    }
}
