//package com.skysoul.album.core.ugckit.moudle;
//
//import android.util.Log;
//import android.widget.Toast;
//
//
//import com.tencent.ugc.TXVideoEditConstants;
//import com.tencent.ugc.TXVideoEditer;
//import com.tencent.ugc.TXVideoEditer.TXVideoGenerateListener;
//import com.skysoul.album.core.ugckit.UGCKit;
//import com.skysoul.album.core.ugckit.VideoEditerSDK;
//import com.skysoul.album.core.ugckit.basic.BaseGenerateKit;
//import com.skysoul.album.core.ugckit.effect.PlayState;
//import com.skysoul.album.core.ugckit.utils.AlbumSaver;
//import com.skysoul.album.core.ugckit.utils.CoverUtil;
//import com.skysoul.album.core.ugckit.utils.VideoPathUtil;
//import com.skysoul.album.util._AlbumUtils;
//
///**
// * 视频生成管理
// */
//public class VideoGenerateKit extends BaseGenerateKit implements TXVideoGenerateListener {
//    private static final String TAG = "VideoGenerateKit";
//    private static final int DURATION_TAILWATERMARK = 3;
//
//    private static VideoGenerateKit instance = new VideoGenerateKit();
//
//    private int     mCurrentState;
//    private int     mVideoResolution = TXVideoEditConstants.VIDEO_COMPRESSED_720P;
//    private boolean mSaveToDCIM;
//    private boolean mCoverGenerate;
//    private String mVideoOutputPath;
//    private String mCoverPath;
//    private long mDuration;     // 生成视频的时长
//
//    private WaterMarkConfig mWaterMark;
//    private TailWaterMarkConfig mTailWaterMarkConfig;
//
//    private VideoGenerateKit() {
//        // 默认生成封面
//        mCoverGenerate = true;
//        // 默认保存到相册
//        mSaveToDCIM = true;
//    }
//
//    public static VideoGenerateKit getInstance() {
//        return instance;
//    }
//
//    /**
//     * 设置视频分辨率
//     *
//     * @param resolution
//     */
//    public void setVideoResolution(int resolution) {
//        mVideoResolution = resolution;
//    }
//
//    /**
//     * 自定义视频码率
//     *
//     * @param videoBitrate
//     */
//    public void setCustomVideoBitrate(int videoBitrate) {
//        TXVideoEditer editer = VideoEditerSDK.getInstance().getEditer();
//        if (editer != null) {
//            editer.setVideoBitrate(videoBitrate);
//        }
//    }
//
//    /**
//     * 开始合成视频
//     */
//    public void startGenerate() {
//        mCurrentState = PlayState.STATE_GENERATE;
//        mVideoOutputPath = VideoPathUtil.generateVideoPath();
//        Log.d(TAG, "startGenerate mVideoOutputPath:" + mVideoOutputPath);
//
//        long startTime = VideoEditerSDK.getInstance().getCutterStartTime();
//        long endTime = VideoEditerSDK.getInstance().getCutterEndTime();
//
//        TXVideoEditer editer = VideoEditerSDK.getInstance().getEditer();
//        if (editer != null) {
//            editer.setCutFromTime(startTime, endTime);
//            editer.setVideoGenerateListener(this);
//            if (mWaterMark != null) {
//                editer.setWaterMark(mWaterMark.watermark, mWaterMark.rect);
//            }
//            if (mTailWaterMarkConfig != null) {
//                editer.setTailWaterMark(mTailWaterMarkConfig.tailwatermark, mTailWaterMarkConfig.rect, mTailWaterMarkConfig.duration);
//            }
//
//            switch (mVideoResolution) {
//                case TXVideoEditConstants.VIDEO_COMPRESSED_360P:
//                    editer.generateVideo(TXVideoEditConstants.VIDEO_COMPRESSED_360P, mVideoOutputPath);
//                    break;
//                case TXVideoEditConstants.VIDEO_COMPRESSED_480P:
//                    editer.generateVideo(TXVideoEditConstants.VIDEO_COMPRESSED_480P, mVideoOutputPath);
//                    break;
//                case TXVideoEditConstants.VIDEO_COMPRESSED_540P:
//                    editer.generateVideo(TXVideoEditConstants.VIDEO_COMPRESSED_540P, mVideoOutputPath);
//                    break;
//                case TXVideoEditConstants.VIDEO_COMPRESSED_720P:
//                    editer.generateVideo(TXVideoEditConstants.VIDEO_COMPRESSED_720P, mVideoOutputPath);
//                    break;
//                default:
//                    editer.generateVideo(TXVideoEditConstants.VIDEO_COMPRESSED_720P, mVideoOutputPath);
//                    break;
//
//            }
//        }
//    }
//
//    /**
//     * 停止合成视频[包括一些异常操作导致的合成取消]
//     */
//    public void stopGenerate() {
//        //FIXBUG:如果上一次如生成缩略图没有停止，先停止，在进行下一次生成
//        TXVideoEditer editer = VideoEditerSDK.getInstance().getEditer();
//        if (editer != null) {
//            editer.cancel();
//            editer.setVideoGenerateListener(null);
//        }
//        if (mCurrentState == PlayState.STATE_GENERATE) {
//            Toast.makeText(UGCKit.getAppContext(), _AlbumUtils.getResStringId(UGCKit.getAppContext(), "ugckit_video_editer_activity_cancel_video_generation"), Toast.LENGTH_SHORT).show();
//            mCurrentState = PlayState.STATE_NONE;
//
//            if (mOnUpdateUIListener != null) {
//                mOnUpdateUIListener.onUICancel();
//            }
//        }
//    }
//
//    @Override
//    public void onGenerateProgress(float progress) {
//        if (mOnUpdateUIListener != null) {
//            mOnUpdateUIListener.onUIProgress(progress);
//        }
//    }
//
//    @Override
//    public void onGenerateComplete(final TXVideoEditConstants.TXGenerateResult result) {
//        mCurrentState = PlayState.STATE_NONE;
//        if (result.retCode == TXVideoEditConstants.GENERATE_RESULT_OK) {
//            if (mCoverGenerate) {
//                Log.d(TAG, "onGenerateComplete outputPath:" + mVideoOutputPath);
//                // 获取哪个视频的封面
//                CoverUtil.getInstance().setInputPath(mVideoOutputPath);
//                // 创建新的封面
//                CoverUtil.getInstance().createThumbFile(new CoverUtil.ICoverListener() {
//                    @Override
//                    public void onCoverPath(String coverPath) {
//                        mCoverPath = coverPath;
//                        Log.d(TAG, "onGenerateComplete coverPath:" + coverPath);
//                        saveAndUpdate(result);
//                    }
//                });
//            } else {
//                saveAndUpdate(result);
//            }
//        }
//    }
//
//    private void saveAndUpdate(TXVideoEditConstants.TXGenerateResult result) {
//        mDuration = VideoEditerSDK.getInstance().getVideoDuration();
//        if (mSaveToDCIM) {
//            AlbumSaver.getInstance(UGCKit.getAppContext()).setOutputProfile(mVideoOutputPath, mDuration, mCoverPath);
//            AlbumSaver.getInstance(UGCKit.getAppContext()).saveVideoToDCIM();
//        }
//        // UI更新
//        if (mOnUpdateUIListener != null) {
//            mOnUpdateUIListener.onUIComplete(result.retCode, result.descMsg);
//        }
//    }
//
//    /**
//     * 获取生成视频输出路径
//     *
//     * @return
//     */
//    public String getVideoOutputPath() {
//        return mVideoOutputPath;
//    }
//
//    /**
//     * 获取视频封面路径
//     */
//    public String getCoverPath() {
//        return mCoverPath;
//    }
//
//    /**
//     * 获取生成的视频时长
//     */
//    public long getDuration() {
//        return mDuration;
//    }
//
//    public void saveVideoToDCIM(boolean flag) {
//        mSaveToDCIM = flag;
//    }
//
//    public void setCoverGenerate(boolean coverGenerate) {
//        mCoverGenerate = coverGenerate;
//    }
//
//    public void setWaterMark(WaterMarkConfig waterMark) {
//        mWaterMark = waterMark;
//    }
//
//    public void setTailWaterMark(TailWaterMarkConfig tailWaterMarkConfig) {
//        mTailWaterMarkConfig = tailWaterMarkConfig;
//    }
//}
