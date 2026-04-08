package com.skysoul.album.action;

import android.content.Context;
import android.content.Intent;

import com.skysoul.album.CropRatio;
import com.skysoul.album.OnSelectedCallback;
import com.skysoul.album.action.AlbumAction;
import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.core.CoreAlbumManager;
import com.skysoul.album.ui.ImageBrowserActivity;
import com.skysoul.album.util.AlbumConstant;

import java.util.ArrayList;

/**
 * 图片预览
 */
public class PreviewAction extends AlbumAction {

    private int curPreviewPosition;                 // 预览图片从哪个位置显示
    private ArrayList<MediaInfo> previewMediaInfoList;   // 预览图片集合
    private OnSelectedCallback onSelectedCallback;

    /**
     * 设置预览图片集合
     * @param list      // MediaInfo集合
     * @param position  // 从哪个位置开始显示
     */
    public PreviewAction setPreviewList(ArrayList<MediaInfo> list, int position) {
        this.previewMediaInfoList = list;
        this.curPreviewPosition = position;
        return this;
    }

    /**
     * 设置是否使用压缩，默认使用
     */
    @Override
    public PreviewAction setUseCompress(boolean isUseCompress) {
        return (PreviewAction) super.setUseCompress(isUseCompress);
    }

    /**
     * 设置图片压缩大小限制，压缩后的图片不能超过此限制
     */
    @Override
    public PreviewAction setImageLimitSize(long imageLimitSize) {
        return (PreviewAction) super.setImageLimitSize(imageLimitSize);
    }

    /**
     * 设置是否使用裁剪，默认使用
     */
    public PreviewAction setUseCrop(boolean isUseCrop) {
        mMediaOptions.isUseCrop = isUseCrop;
        return this;
    }

    /**
     * 设置图片选择回调
     */
    public PreviewAction setOnSelectedCallback(OnSelectedCallback onSelectedCallback) {
        this.onSelectedCallback = onSelectedCallback;
        return this;
    }

    @Override
    protected Intent initIntent(Context context) {
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putExtra(AlbumConstant.key_cur_position, curPreviewPosition);
        intent.putParcelableArrayListExtra(AlbumConstant.key_media_list, previewMediaInfoList);
        intent.putExtra(AlbumConstant.key_from_other, true);
        if (onSelectedCallback != null) {
            CoreAlbumManager.getInstance().put(index, onSelectedCallback);
        }
        return intent;
    }


    @Override
    public PreviewAction useScreenOrientationLandscape() {
        return (PreviewAction) super.useScreenOrientationLandscape();
    }

    @Override
    public PreviewAction setCropRatio(CropRatio[] cropRatios) {
        return (PreviewAction) super.setCropRatio(cropRatios);
    }

    @Override
    public PreviewAction setCropRatio(CropRatio[] cropRatios, CropRatio defaultCropRatio){
        return (PreviewAction) super.setCropRatio(cropRatios, defaultCropRatio);
    }

    @Override
    public PreviewAction setShowOriginalCropRatio(boolean showOriginalCropRatio) {
        return (PreviewAction) super.setShowOriginalCropRatio(showOriginalCropRatio);
    }

    /**
     * 设置自定义裁剪比例
     */
    @Override
    public PreviewAction setCropRatio(float cropRatio) {
        return (PreviewAction) super.setCropRatio(cropRatio);
    }
}
