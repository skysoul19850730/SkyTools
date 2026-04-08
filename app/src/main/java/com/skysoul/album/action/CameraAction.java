//package com.skysoul.album.action;
//
//import android.content.Context;
//import android.content.Intent;
//
//import com.skysoul.album.CropRatio;
//import com.skysoul.album.OnCropCallback;
//import com.skysoul.album.action.AlbumAction;
//import com.skysoul.album.bean.MediaType;
//import com.skysoul.album.core.CoreAlbumManager;
//import com.skysoul.album.ui.MediaTakeActivity;
//import com.skysoul.album.util.AlbumConstant;
//
///**
// * 打开相机
// */
//public class CameraAction extends AlbumAction {
//
//    private OnCropCallback onCropCallback;
//
//    /**
//     * 设置是否使用裁剪，默认使用
//     */
//    public CameraAction setUseCrop(boolean isUseCrop) {
//        mMediaOptions.isUseCrop = isUseCrop;
//        return this;
//    }
//
//    /**
//     * 设置是否使用压缩，默认使用
//     */
//    @Override
//    public CameraAction setUseCompress(boolean isUseCompress) {
//        return (CameraAction) super.setUseCompress(isUseCompress);
//    }
//
//    /**
//     * 设置图片压缩大小限制，压缩后的图片不能超过此限制
//     */
//    @Override
//    public CameraAction setImageLimitSize(long imageLimitSize) {
//        return (CameraAction) super.setImageLimitSize(imageLimitSize);
//    }
//
//    /**
//     * 设置回调
//     */
//    public CameraAction setOnCropCallback(OnCropCallback onCropCallback) {
//        this.onCropCallback = onCropCallback;
//        return this;
//    }
//
//    @Override
//    public CameraAction useScreenOrientationLandscape() {
//        return (CameraAction) super.useScreenOrientationLandscape();
//    }
//
//    @Override
//    public CameraAction setCropRatio(CropRatio[] cropRatios) {
//        return (CameraAction) super.setCropRatio(cropRatios);
//    }
//
//    @Override
//    public CameraAction setCropRatio(CropRatio[] cropRatios, CropRatio defaultCropRatio){
//        return (CameraAction) super.setCropRatio(cropRatios, defaultCropRatio);
//    }
//
//    @Override
//    public CameraAction setShowOriginalCropRatio(boolean showOriginalCropRatio) {
//        return (CameraAction) super.setShowOriginalCropRatio(showOriginalCropRatio);
//    }
//
//    @Override
//    protected Intent initIntent(Context context) {
//        Intent intent = new Intent(context, MediaTakeActivity.class);
//        intent.putExtra(AlbumConstant.key_from_other, true);
//        intent.putExtra(AlbumConstant.key_media_type, MediaType.IMAGE);
//        if (onCropCallback != null) {
//            CoreAlbumManager.getInstance().put(index, onCropCallback);
//        }
//        return intent;
//    }
//}
