package com.skysoul.album.action;

import android.content.Context;
import android.content.Intent;

import com.skysoul.album.CropRatio;
import com.skysoul.album.OnSelectedCallback;
import com.skysoul.album.action.AlbumAction;
import com.skysoul.album.bean.MediaType;
import com.skysoul.album.core.CoreAlbumManager;
import com.skysoul.album.ui.ImageSelectorActivity;

/**
 * 选择媒体资源
 */
public class SelectedAction extends AlbumAction {

    private OnSelectedCallback onSelectedCallback;

    /**
     * 设置图片选择回调
     */
    public SelectedAction setOnSelectedCallback(OnSelectedCallback onSelectedCallback) {
        this.onSelectedCallback = onSelectedCallback;
        return this;
    }

    /**
     * 设置媒体类型，默认为图片
     * {@link com.skysoul.album.bean.MediaType#IMAGE}  // 图片
     * {@link com.skysoul.album.bean.MediaType#VIDEO}  // 视频
     * {@link com.skysoul.album.bean.MediaType#ALL}    // 视频+图片
     */
    public SelectedAction setMediaType(MediaType mediaType) {
        mMediaOptions.mediaType = mediaType;
        return this;
    }

    /**
     * 设置选择图片的最大数量，默认9
     */
    public SelectedAction setMaxSelectImageCount(int maxSelectImageCount) {
        mMediaOptions.maxImageCount = maxSelectImageCount;
        return this;
    }

    /**
     * 设置是否显示裁剪，默认显示
     */
    public SelectedAction setUseCrop(boolean isUseCrop) {
        mMediaOptions.isUseCrop = isUseCrop;
        return this;
    }

    /**
     * 视频封面选择界面不显示裁剪
     * isUseCrop为true则图片和视频封面选择界面都会显示裁剪
     * 如果视频封面不需要显示裁剪，则需要调用该方法
     */
    public SelectedAction videoCoverNoCrop() {
        mMediaOptions.isVideoCoverNoCrop = true;
        return this;
    }

    /**
     * 点击下一步是否直接打开裁剪, 默认false
     * 只有 setMaxSelectImageCount(1) 是有效
     */
    public SelectedAction setOpenCropDirectly(boolean isOpenCropDirectly) {
        mMediaOptions.isOpenCropDirectly = isOpenCropDirectly;
        return this;
    }

    /**
     * 设置MediaType.IMAGE图片类别中是否展示GIF，默认展示
     */
    public SelectedAction setShowGif(boolean isShowGif) {
        mMediaOptions.isShowGif = isShowGif;
        return this;
    }

    /**
     * 设置视频录制时长限制，最长录制的时长，单位秒
     */
    public SelectedAction setVideoRecodeLimitDuration(int videoRecodeLimitDuration) {
        mMediaOptions.videoRecodeLimitDuration = videoRecodeLimitDuration;
        return this;
    }

    /**
     * 设置视频裁剪最大时长，默认120秒, 单位秒
     */
    public SelectedAction setVideoLimitCropDuration(int videoLimitCropDuration) {
        mMediaOptions.videoLimitCropDuration = videoLimitCropDuration;
        return this;
    }

    /**
     * 设置视频文件大小限制，只展示该大小以内的视频，默认不限制，单位字节
     */
    public SelectedAction setVideoLimitSize(long videoLimitSize) {
        mMediaOptions.videoLimitSize = videoLimitSize;
        return this;
    }

    /**
     * 设置视频时长限制，超过此时长不展示，单位秒
     */
    public SelectedAction setVideoLimitDuration(int videoLimitDuration) {
        mMediaOptions.videoLimitDuration = videoLimitDuration;
        return this;
    }

    /**
     * 图片选择界面是否显示相机项，用户拍照和录像
     * 默认是显示
     */
    public SelectedAction setShowCamera(boolean isShowCamera) {
        mMediaOptions.isShowCamera = isShowCamera;
        return this;
    }

    /**
     * 图片选择界面是否使用系统相机项，用户拍照
     * 默认不使用
     */
    public SelectedAction setUseSystemCamera(boolean isShowSystemCamera) {
        mMediaOptions.isUseSystemCamera = isShowSystemCamera;
        return this;
    }

    /**
     * 设置是否使用压缩，默认使用
     */
    @Override
    public SelectedAction setUseCompress(boolean isUseCompress) {
        return (SelectedAction) super.setUseCompress(isUseCompress);
    }

    /**
     * 设置图片压缩大小限制，压缩后的图片不能超过此限制，默认3兆，单位字节
     */
    @Override
    public SelectedAction setImageLimitSize(long imageLimitSize) {
        return (SelectedAction) super.setImageLimitSize(imageLimitSize);
    }

    @Override
    protected Intent initIntent(Context context) {
        Intent intent = new Intent(context, ImageSelectorActivity.class);
        if (onSelectedCallback != null) {
            CoreAlbumManager.getInstance().put(index, onSelectedCallback);
        }
        return intent;
    }




    @Override
    public SelectedAction useScreenOrientationLandscape() {
        return (SelectedAction) super.useScreenOrientationLandscape();
    }

    @Override
    public SelectedAction setCropRatio(CropRatio[] cropRatios) {
        return (SelectedAction) super.setCropRatio(cropRatios);
    }

    @Override
    public SelectedAction setCropRatio(CropRatio[] cropRatios, CropRatio defaultCropRatio){
        return (SelectedAction) super.setCropRatio(cropRatios, defaultCropRatio);
    }

    @Override
    public SelectedAction setShowOriginalCropRatio(boolean showOriginalCropRatio) {
        return (SelectedAction) super.setShowOriginalCropRatio(showOriginalCropRatio);
    }

    /**
     * 设置自定义裁剪比例
     */
    @Override
    public SelectedAction setCropRatio(float cropRatio) {
        return (SelectedAction) super.setCropRatio(cropRatio);
    }

}
