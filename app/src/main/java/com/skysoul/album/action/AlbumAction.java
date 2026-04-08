package com.skysoul.album.action;

import android.content.Context;
import android.content.Intent;
import com.skysoul.album.CropRatio;
import com.skysoul.album.core.CoreAlbumManager;
import com.skysoul.album.core.MediaOptions;
import com.skysoul.album.util.AlbumConstant;


public abstract class AlbumAction {

    protected final int index;        // 次序
    protected final MediaOptions mMediaOptions = new MediaOptions();
    private boolean useScreenOrientationLandscape = false; // 使用横屏，默认是false，使用竖屏

    public AlbumAction() {
        index = CoreAlbumManager.getInstance().getIndex();
    }

    /**
     * 设置是否使用压缩，默认不使用
     */
    public AlbumAction setUseCompress(boolean isUseCompress) {
        mMediaOptions.isUseCompress = isUseCompress;
        return this;
    }

    /**
     * 设置图片压缩大小限制，压缩后的图片不能超过此限制
     */
    public AlbumAction setImageLimitSize(long imageLimitSize) {
        mMediaOptions.imageLimitSize = imageLimitSize;
        return this;
    }

    /**
     * 设置裁剪比例
     */
    public AlbumAction setCropRatio(CropRatio[] cropRatios) {
        mMediaOptions.cropRatios = cropRatios;
        return this;
    }

    public AlbumAction setCropRatio(CropRatio[] cropRatios, CropRatio defaultCropRatio) {
        mMediaOptions.cropRatios = cropRatios;
        mMediaOptions.defaultCropRatio = defaultCropRatio;
        return this;
    }

    public AlbumAction setShowOriginalCropRatio(boolean showOriginalCropRatio) {
        mMediaOptions.showOriginalCropRatio = showOriginalCropRatio;
        return this;
    }

    /**
     * 设置自定义裁剪比例
     */
    public AlbumAction setCropRatio(float cropRatio) {
        if (cropRatio >= 0.2f && cropRatio <= 5f) {
            CropRatio.RATIO_CUSTOM.setRatio(cropRatio);
            mMediaOptions.cropRatios = new CropRatio[]{CropRatio.RATIO_CUSTOM};
        }
        return this;
    }

    /**
     * 使用横屏模式
     */
    public AlbumAction useScreenOrientationLandscape() {
        this.useScreenOrientationLandscape = true;
        return this;
    }

    /**
     * 开始操作
     */
    public void start(Context context) {
        Intent intent = initIntent(context);
        intent.putExtra(AlbumConstant.key_media_option, mMediaOptions);
        intent.putExtra(AlbumConstant.key_index, index);
        intent.putExtra(AlbumConstant.key_use_screen_orientation_landscape, useScreenOrientationLandscape);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected abstract Intent initIntent(Context context);
}
