//package com.skysoul.album.core.video;
//
//import com.tencent.rtmp.TXLiveConstants;
//import com.tencent.ugc.TXRecordCommon;
//
//public class UGCRecordConfig {
//    public static final int RECORD_MODE_TAKE_PHOTO = 1;
//    public static final int RECORD_MODE_CLICK = 2;
//    public static final int RECORD_MODE_LONG_CLICK = 3;
//
//
//    public int quality = TXRecordCommon.VIDEO_QUALITY_HIGH;
//    public int videoBitrate = 9600;
//    public int resolution = TXRecordCommon.VIDEO_RESOLUTION_720_1280;
//    public int GOP = 3;
//    public int FPS = 20;
//    public boolean isMute = false;
//    public boolean isNeedEdit = true;
//    /**
//     * 录制最短时间
//     */
//    public int minDuration = 0;
//    /**
//     * 录制最长时间
//     */
//    public int maxDuration = 120 * 1000;
//    /**
//     * 录制方向
//     */
//    public int homeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
//    /**
//     * 渲染方向
//     */
//    public int renderRotation = TXLiveConstants.RENDER_ROTATION_LANDSCAPE;
//    /**
//     * 录制速度
//     */
//    public int recordSpeed = TXRecordCommon.RECORD_SPEED_NORMAL;
//    /**
//     * 是否前置摄像头
//     */
//    public boolean frontCamera = false;
//    /**
//     * 开启手动聚焦：自动聚焦设置为false
//     */
//    public boolean touchFocus = false;
//    /**
//     * 当前屏比
//     */
//    public int aspectRatio = TXRecordCommon.VIDEO_ASPECT_RATIO_9_16;
//    public int recordMode = RECORD_MODE_LONG_CLICK;
//}
