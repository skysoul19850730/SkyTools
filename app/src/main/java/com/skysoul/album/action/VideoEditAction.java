//package com.skysoul.album.action;
//
//import android.content.Context;
//import android.content.Intent;
//import android.text.TextUtils;
//
//import com.skysoul.album.CropRatio;
//import com.skysoul.album.OnCropCallback;
//import com.skysoul.album.action.AlbumAction;
//import com.skysoul.album.bean.MediaInfo;
//import com.skysoul.album.bean.MediaType;
//import com.skysoul.album.core.CoreAlbumManager;
//import com.skysoul.album.ui.VideoEditActivity;
//import com.skysoul.album.util.AlbumConstant;
//
///**
// * 视频裁剪行为
// */
//public class VideoEditAction extends AlbumAction {
//
//    private OnCropCallback onCropCallback;
//    private String cropVideoPath;
//
//    /**
//     * 设置要剪辑的视频路径
//     */
//    public VideoEditAction setVideoPath(String videoFilePath) {
//        if (videoFilePath == null) videoFilePath = "";
//        cropVideoPath = videoFilePath;
//        return this;
//    }
//
//    /**
//     * 设置视频剪辑回调
//     */
//    public VideoEditAction setOnCropCallback(OnCropCallback onCropCallback) {
//        this.onCropCallback = onCropCallback;
//        return this;
//    }
//
//    @Override
//    protected Intent initIntent(Context context) {
//        Intent intent = new Intent(context, VideoEditActivity.class);
//        intent.putExtra(AlbumConstant.key_from_other, true);
//        MediaInfo mediaInfo = new MediaInfo();
//        mediaInfo.mediaType = MediaType.VIDEO.mediaType;
//        if (!TextUtils.isEmpty(cropVideoPath)) {
//            mediaInfo.path = cropVideoPath;
//        }
//        intent.putExtra(AlbumConstant.key_media_info, mediaInfo);
//        if (onCropCallback != null) {
//            CoreAlbumManager.getInstance().put(index, onCropCallback);
//        }
//        return intent;
//    }
//
//    @Override
//    public void start(Context context) {
//        if (CoreAlbumManager.getInstance().isLicenceValid()) {
//            super.start(context);
//        } else {
//            if (onCropCallback != null) {
//                onCropCallback.onFail(CoreAlbumManager.code_licence_invalid, CoreAlbumManager.getFailMsg(CoreAlbumManager.code_licence_invalid));
//            }
//        }
//    }
//
//    @Override
//    public VideoEditAction setUseCompress(boolean isUseCompress) {
//        return (VideoEditAction) super.setUseCompress(isUseCompress);
//    }
//
//    @Override
//    public VideoEditAction setImageLimitSize(long imageLimitSize) {
//        return (VideoEditAction) super.setImageLimitSize(imageLimitSize);
//    }
//
//    @Override
//    public VideoEditAction useScreenOrientationLandscape() {
//        return (VideoEditAction) super.useScreenOrientationLandscape();
//    }
//
//
//
//    /**
//     * 以下方法在视频封面选择中不支持配置，标记为废弃
//     */
//    @Deprecated
//    @Override
//    public VideoEditAction setCropRatio(CropRatio[] cropRatios) {
//        return this;
//    }
//
//    /**
//     * 以下方法在视频封面选择中不支持配置，标记为废弃
//     */
//    @Deprecated
//    @Override
//    public VideoEditAction setCropRatio(float cropRatio) {
//        return (VideoEditAction) super.setCropRatio(cropRatio);
//    }
//
//    @Override
//    public VideoEditAction setCropRatio(CropRatio[] cropRatios, CropRatio defaultCropRatio){
//        return this;
//    }
//
//    @Override
//    public VideoEditAction setShowOriginalCropRatio(boolean showOriginalCropRatio) {
//        return this;
//    }
//
//}
