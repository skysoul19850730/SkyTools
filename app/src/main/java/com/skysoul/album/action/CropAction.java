package com.skysoul.album.action;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.skysoul.album.CropRatio;
import com.skysoul.album.OnCropCallback;
import com.skysoul.album.action.AlbumAction;
import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.core.CoreAlbumManager;
import com.skysoul.album.ui.ImageCropActivity;
import com.skysoul.album.util.AlbumConstant;

/**
 * 图片裁剪行为
 */
public class CropAction extends AlbumAction {

    private OnCropCallback onCropCallback;
    private String cropPath = "";

    /**
     * 设置要裁剪图片的路径
     */
    public CropAction setCropPath(String cropFilePath) {
        if (cropFilePath == null) cropFilePath = "";
        cropPath = cropFilePath;
        return this;
    }

    /**
     * 设置是否使用压缩，默认使用
     */
    @Override
    public CropAction setUseCompress(boolean isUseCompress) {
        return (CropAction) super.setUseCompress(isUseCompress);
    }

    /**
     * 设置图片压缩大小限制，压缩后的图片不能超过此限制
     */
    @Override
    public CropAction setImageLimitSize(long imageLimitSize) {
        return (CropAction) super.setImageLimitSize(imageLimitSize);
    }

    /**
     * 设置裁剪回调
     */
    public CropAction setOnCropCallback(OnCropCallback onCropCallback) {
        this.onCropCallback = onCropCallback;
        return this;
    }

    @Override
    public CropAction useScreenOrientationLandscape() {
        return (CropAction) super.useScreenOrientationLandscape();
    }

    @Override
    public CropAction setCropRatio(CropRatio[] cropRatios) {
        return (CropAction) super.setCropRatio(cropRatios);
    }

    @Override
    public CropAction setCropRatio(CropRatio[] cropRatios, CropRatio defaultCropRatio){
        return (CropAction) super.setCropRatio(cropRatios, defaultCropRatio);
    }

    @Override
    public CropAction setShowOriginalCropRatio(boolean showOriginalCropRatio) {
        return (CropAction) super.setShowOriginalCropRatio(showOriginalCropRatio);
    }

    @Override
    protected Intent initIntent(Context context) {
        Intent intent = new Intent(context, ImageCropActivity.class);
        intent.putExtra(AlbumConstant.key_from_other, true);
        MediaInfo mediaInfo = new MediaInfo();
        if (!TextUtils.isEmpty(cropPath)) {
            mediaInfo.path = cropPath;
        }
        intent.putExtra(AlbumConstant.key_media_info, mediaInfo);
        if (onCropCallback != null) {
            CoreAlbumManager.getInstance().put(index, onCropCallback);
        }
        return intent;
    }
}
