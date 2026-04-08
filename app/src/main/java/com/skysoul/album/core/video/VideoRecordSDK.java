//package com.skysoul.album.core.video;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//
//import com.tencent.rtmp.ui.TXCloudVideoView;
//import com.tencent.ugc.TXRecordCommon;
//import com.tencent.ugc.TXUGCRecord;
//
//public class VideoRecordSDK implements IVideoRecord, TXRecordCommon.ITXVideoRecordListener{
//
//    public static final int STATE_START = 1;
//    public static final int STATE_STOP = 2;
//    public static final int STATE_RESUME = 3;
//    public static final int STATE_PAUSE = 4;
//    public static final int START_RECORD_SUCC = 0;
//    public static final int START_RECORD_FAIL = -1;
//
//
//
//    public TXUGCRecord recordSDK;
//    private boolean isPreview = false;
//    private int currentState = STATE_STOP;
//    private boolean isFrontCamera = false;
//    private AudioFocusManager audioFocusManager;
//    private Context mContext;
//    private UGCRecordConfig ugcRecordConfig;
//    private OnVideoRecordListener videoRecordListener = null;
//
//    public VideoRecordSDK(Context context) {
//        mContext = context;
//        audioFocusManager = new AudioFocusManager();
//        recordSDK = TXUGCRecord.getInstance(context);
//        currentState = STATE_STOP;
//    }
//
//
//    @Override
//    public void initConfig(UGCRecordConfig ugcRecordConfig) {
//        this.ugcRecordConfig = ugcRecordConfig;
//    }
//
//
//
//    @Override
//    public void onRecordEvent(int i, Bundle bundle) {
//        if (videoRecordListener != null) {
//            videoRecordListener.onRecordEvent(i, bundle);
//        }
//    }
//
//    @Override
//    public void onRecordProgress(long l) {
//        if (videoRecordListener != null) {
//            videoRecordListener.onRecordProgress(l);
//        }
//    }
//
//    @Override
//    public void onRecordComplete(TXRecordCommon.TXRecordResult txRecordResult) {
//        currentState = STATE_STOP;
//        if (videoRecordListener != null) {
//            videoRecordListener.onRecordComplete(txRecordResult);
//        }
//    }
//
//    @Override
//    public void setVideoRecordListener(OnVideoRecordListener videoRecordListener) {
//        this.videoRecordListener = videoRecordListener;
//    }
//
//    @Override
//    public void takePhoto(final OnSnapshotListener snapshotListener) {
//        recordSDK.snapshot(new TXRecordCommon.ITXSnapshotListener() {
//            @Override
//            public void onSnapshot(Bitmap bitmap) {
//                if (snapshotListener != null) {
//                    snapshotListener.onSnapshot(bitmap);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void startCameraPreview(TXCloudVideoView videoView) {
//        if (isPreview) {
//            return;
//        }
//        isPreview = true;
//        if (ugcRecordConfig.quality >= 0) {
//            //推荐配置
//            TXRecordCommon.TXUGCSimpleConfig simpleConfig = new TXRecordCommon.TXUGCSimpleConfig();
//            simpleConfig.videoQuality = ugcRecordConfig.quality;
//            simpleConfig.minDuration = ugcRecordConfig.minDuration;
//            simpleConfig.maxDuration = ugcRecordConfig.maxDuration;
//            simpleConfig.isFront = ugcRecordConfig.frontCamera;
//            simpleConfig.touchFocus = ugcRecordConfig.touchFocus;
//            simpleConfig.needEdit = ugcRecordConfig.isNeedEdit;
//            recordSDK.setVideoRenderMode(ugcRecordConfig.recordMode);
//            recordSDK.setMute(ugcRecordConfig.isMute);
//            recordSDK.startCameraSimplePreview(simpleConfig, videoView);
//        } else {
//            //自定义配置
//            TXRecordCommon.TXUGCCustomConfig customConfig = new TXRecordCommon.TXUGCCustomConfig();
//            customConfig.videoResolution = ugcRecordConfig.resolution;
//            customConfig.minDuration = ugcRecordConfig.minDuration;
//            customConfig.maxDuration = ugcRecordConfig.maxDuration;
//            customConfig.videoBitrate = ugcRecordConfig.videoBitrate;
//            customConfig.videoGop = ugcRecordConfig.GOP;
//            customConfig.videoFps = ugcRecordConfig.FPS;
//            customConfig.isFront = ugcRecordConfig.frontCamera;
//            customConfig.touchFocus = ugcRecordConfig.touchFocus;
//            customConfig.needEdit = ugcRecordConfig.isNeedEdit;
//            recordSDK.startCameraCustomPreview(customConfig, videoView);
//        }
//        recordSDK.setRenderRotation(ugcRecordConfig.renderRotation);
//        recordSDK.setRecordSpeed(ugcRecordConfig.recordSpeed);
//        recordSDK.setHomeOrientation(ugcRecordConfig.homeOrientation);
//        recordSDK.setAspectRatio(ugcRecordConfig.aspectRatio);
//        recordSDK.setVideoRecordListener(this);
//    }
//
//    @Override
//    public void stopCameraPreview() {
//        recordSDK.stopCameraPreview();
//        isPreview = false;
//    }
//
//    @Override
//    public int startRecord(String videoPath, String coverPath) {
//        int resultCode;
//        if (currentState == STATE_STOP) {
//            resultCode = recordSDK.startRecord(videoPath, coverPath);
//        } else if (currentState == STATE_PAUSE) {
//            resumeRecord();
//            resultCode = START_RECORD_SUCC;
//        } else {
//            resultCode = START_RECORD_SUCC;
//        }
//        currentState = STATE_START;
//        return resultCode;
//    }
//
//    /**
//     * 暂时不需要调用，这里只有暂停录制重新开始才需要调用
//     */
//    @Override
//    public void resumeRecord() {
//        if (currentState == STATE_PAUSE) {
//            recordSDK.resumeRecord();
//            audioFocusManager.requestAudioFocus(mContext);
//        }
//        currentState = STATE_RESUME;
//    }
//
//    @Override
//    public void pauseRecord() {
//        if (currentState == STATE_START || currentState == STATE_RESUME) {
//            recordSDK.pauseRecord();
//            currentState = STATE_PAUSE;
//        }
//        isPreview = false;
//        audioFocusManager.abandonAudioFocus();
//    }
//
//    @Override
//    public void stopRecord() {
//        int size = recordSDK.getPartsManager().getPartsPathList().size();
//        if (currentState == STATE_STOP && size == 0) {
//            //录制未开始，且录制片段个数为0，则不需要停止录制
//            return;
//        }
//        recordSDK.stopBGM();
//        recordSDK.stopRecord();
//        audioFocusManager.abandonAudioFocus();
//        currentState = STATE_STOP;
//    }
//
//    @Override
//    public void deleteAllDraft() {
//        recordSDK.getPartsManager().deleteAllParts();
//    }
//
//    @Override
//    public void releaseRecord() {
//        recordSDK.stopBGM();
//        recordSDK.stopCameraPreview();
//        recordSDK.setVideoRecordListener(null);
//        recordSDK.getPartsManager().deleteAllParts();
//        recordSDK.release();
//        isPreview = false;
//        isFrontCamera = false;
//
//        audioFocusManager.abandonAudioFocus();
//    }
//
//    @Override
//    public void switchCamera() {
//        isFrontCamera = !isFrontCamera;
//        recordSDK.switchCamera(isFrontCamera);
//    }
//
//
//    public interface OnVideoRecordListener {
//        void onRecordEvent(int p0, Bundle p1);
//        void onRecordProgress(Long milliSecond);
//        void onRecordComplete(TXRecordCommon.TXRecordResult p0);
//    }
//
//    public interface OnSnapshotListener {
//        void onSnapshot(Bitmap bitmap);
//    }
//}
