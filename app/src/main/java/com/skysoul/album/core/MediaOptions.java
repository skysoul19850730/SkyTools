package com.skysoul.album.core;

import com.skysoul.album.CropRatio;
import com.skysoul.album.bean.MediaType;

import java.io.Serializable;

public class MediaOptions implements Serializable {
    /**
     * 媒体类别
     * {@link com.skysoul.album.bean.MediaType#IMAGE}  // 图片
     * {@link com.skysoul.album.bean.MediaType#VIDEO}  // 视频
     * {@link com.skysoul.album.bean.MediaType#ALL}    // 视频+图片
     */
    public MediaType mediaType = MediaType.IMAGE;

    /**
     * 最大图片的选择数量，视频每次只能选择一个
     */
    public int maxImageCount = 9;

    /**
     * 是否使用裁剪, 默认使用
     */
    public boolean isUseCrop = true;

    /**
     * 视频封面选择界面不显示裁剪, 默认使用，只有选择过程中会设置该属性
     * isUseCrop为true则图片和视频封面选择界面都会显示裁剪
     * 如果视频封面不需要显示裁剪，则需要调用该方法
     */
    public boolean isVideoCoverNoCrop = false;

    /**
     * 点击下一步是否直接打开裁剪, 默认false
     */
    public boolean isOpenCropDirectly = false;

    /**
     * 设置裁剪比例
     * {@link com.skysoul.album.CropRatio#RATIO_0_0}  自由裁剪
     * {@link com.skysoul.album.CropRatio#RATIO_1_1}
     * {@link com.skysoul.album.CropRatio#RATIO_3_4}
     * {@link com.skysoul.album.CropRatio#RATIO_4_3}
     */
    public CropRatio[] cropRatios = null;

    public CropRatio defaultCropRatio = null;

    public boolean showOriginalCropRatio = true;

    /**
     * 是否使用压缩，默认使用
     */
    public boolean isUseCompress = true;

    /**
     * 图片中是否展示GIF
     */
    public boolean isShowGif = true;

    /**
     * 视频录制长度限制，默认录制时长最长为30s
     */
    public int videoRecodeLimitDuration = 30;   // 单位秒

    /**
     * 视频长度限制，默认展示时长最长为60s
     */
    public int videoLimitDuration = 60;   // 单位秒

    /**
     * 视频文件大小限制，只展示该大小以内的视频，默认不限制
     */
    public long videoLimitSize = Long.MAX_VALUE;

    /**
     * 视频剪辑最大时长，默认120秒, 单位秒
     */
    public int videoLimitCropDuration = 120;


    /**
     * 图片大小限制，压缩后的图片不能超过此大小
     */
    public long imageLimitSize = 3 * 1024 * 1024;  // 3兆

    /**
     * 选择界面是否显示相机项
     */
    public boolean isShowCamera = true;
    /**
     * 图片选择界面是否使用系统相机项，用户拍照
     */
    public boolean isUseSystemCamera = false;
}
