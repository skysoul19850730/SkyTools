//package com.skysoul.album.action;
//
//import android.content.Context;
//import android.content.Intent;
//import android.text.TextUtils;
//
//import com.skysoul.album.CropRatio;
//import com.skysoul.album.OnCoverCallback;
//import com.skysoul.album.action.AlbumAction;
//import com.skysoul.album.bean.MediaInfo;
//import com.skysoul.album.core.CoreAlbumManager;
//import com.skysoul.album.ui.VideoCoverActivity;
//import com.skysoul.album.util.AlbumConstant;
//
///**
// * 视频封面选择
// */
//public class CoverAction extends AlbumAction {
//
//    private OnCoverCallback onCoverCallback;
//    private MediaInfo mediaInfo;
//
//    public CoverAction setMediaInfo(MediaInfo mediaInfo) {
//        this.mediaInfo = mediaInfo;
//        return this;
//    }
//
//    public CoverAction setOnCoverCallback(OnCoverCallback onCoverCallback) {
//        this.onCoverCallback = onCoverCallback;
//        return this;
//    }
//
//    @Override
//    protected Intent initIntent(Context context) {
//        Intent intent = new Intent(context, VideoCoverActivity.class);
//        intent.putExtra(AlbumConstant.key_from_other, true);
//        intent.putExtra(AlbumConstant.key_media_info, mediaInfo);
//        if (onCoverCallback != null) {
//            CoreAlbumManager.getInstance().put(index, onCoverCallback);
//        }
//        return intent;
//    }
//
//    @Override
//    public void start(Context context) {
//        if (mediaInfo == null || !mediaInfo.isVideo() || TextUtils.isEmpty(mediaInfo.path)) {
//            if (onCoverCallback != null) {
//                onCoverCallback.onFail(CoreAlbumManager.code_media_error, CoreAlbumManager.getFailMsg(CoreAlbumManager.code_media_error));
//            }
//        } else {
//            super.start(context);
//        }
//    }
//
//    /**
//     * 设置是否使用裁剪，默认使用
//     */
//    public CoverAction setUseCrop(boolean isUseCrop) {
//        mMediaOptions.isUseCrop = isUseCrop;
//        return this;
//    }
//
//    /**
//     * 设置是否使用压缩，默认使用
//     */
//    @Override
//    public CoverAction setUseCompress(boolean isUseCompress) {
//        return (CoverAction) super.setUseCompress(isUseCompress);
//    }
//
//    /**
//     * 设置图片压缩大小限制，压缩后的图片不能超过此限制
//     */
//    @Override
//    public CoverAction setImageLimitSize(long imageLimitSize) {
//        return (CoverAction) super.setImageLimitSize(imageLimitSize);
//    }
//
//    @Override
//    public CoverAction useScreenOrientationLandscape() {
//        return (CoverAction) super.useScreenOrientationLandscape();
//    }
//
//    @Override
//    public CoverAction setCropRatio(CropRatio[] cropRatios) {
//        return (CoverAction) super.setCropRatio(cropRatios);
//    }
//
//    @Override
//    public CoverAction setCropRatio(CropRatio[] cropRatios, CropRatio defaultCropRatio){
//        return (CoverAction) super.setCropRatio(cropRatios, defaultCropRatio);
//    }
//
//    @Override
//    public CoverAction setShowOriginalCropRatio(boolean showOriginalCropRatio) {
//        return (CoverAction) super.setShowOriginalCropRatio(showOriginalCropRatio);
//    }
//
//    /**
//     * 设置自定义裁剪比例
//     */
//    @Override
//    public CoverAction setCropRatio(float cropRatio) {
//        return (CoverAction) super.setCropRatio(cropRatio);
//    }
//}
