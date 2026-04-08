//package com.skysoul.album.core.ugckit;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.skysoul.album.core.CoreAlbumManager;
//import com.skysoul.album.core.ugckit.VideoEditerSDK;
//import com.skysoul.album.core.ugckit.basic.OnUpdateUIListener;
//import com.skysoul.album.core.ugckit.basic.UGCKitResult;
//import com.skysoul.album.core.ugckit.cut.AbsVideoCutUI;
//import com.skysoul.album.core.ugckit.cut.IVideoCutLayout;
//import com.skysoul.album.core.ugckit.moudle.PlayerManagerKit;
//import com.skysoul.album.core.ugckit.moudle.ProcessKit;
//import com.skysoul.album.core.ugckit.moudle.VideoGenerateKit;
//import com.skysoul.album.core.ugckit.utils.BackgroundTasks;
//import com.skysoul.album.core.ugckit.utils.DialogUtil;
//import com.skysoul.album.core.ugckit.utils.ProgressFragmentUtil;
//import com.skysoul.album.ui.VideoEditActivity;
//import com.skysoul.album.util._AlbumUtils;
//
///**
// * 腾讯云短视频UGCKit:视频裁剪控件
// * <p>
// * 功能：用于实现长时间视频裁剪其中一段生成一段短时间的视频。<p/>
// */
//public class UGCKitVideoCut extends AbsVideoCutUI implements PlayerManagerKit.OnPreviewListener {
//    private static final String TAG = "UGCKitVideoCut";
//
//    private ProgressFragmentUtil mProgressFragmentUtil;
//    private boolean mComplete = false;
//    private boolean isInit = false;
//    private String mCoverPath = ""; // 封面路径
//    private long mVideoDuration = 0; // 视频时长
//    private TXVideoEditConstants.TXVideoInfo mTXVideoInfo;
//
//    public UGCKitVideoCut(Context context) {
//        super(context);
//        initDefault();
//    }
//
//    public UGCKitVideoCut(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initDefault();
//    }
//
//    public UGCKitVideoCut(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initDefault();
//    }
//
//    private void initDefault() {
//        mProgressFragmentUtil = new ProgressFragmentUtil((Activity) getContext(),
//                getResources().getString(_AlbumUtils.getResStringId(getContext(), "ugckit_video_cutting")));
//
//        VideoEditerSDK.getInstance().releaseSDK();
//        VideoEditerSDK.getInstance().clear();
//        VideoEditerSDK.getInstance().initSDK();
//
//        // 点击"下一步"
//        getTitleBar().setOnRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                long startTime = VideoEditerSDK.getInstance().getCutterStartTime();
//                long endTime = VideoEditerSDK.getInstance().getCutterEndTime();
//                long duration = (endTime - startTime) / 1000;
//                long cTime = VideoEditerSDK.getInstance().getTXVideoInfo().duration / 1000;
//                if (duration < 1) {
//                    Toast.makeText(getContext(), "视频长度不能为0", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (duration > UGCKit.getVideoLimitCropDuration()) {
//                    Toast.makeText(getContext(), "选择的视频长度不能大于"+UGCKit.getVideoLimitCropDuration()+"秒", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if ((duration == cTime) && duration < UGCKit.getVideoLimitCropDuration()) {
//                    if (listener != null) {
//                        listener.onCutterCompleted(null);
//                    }
//                    return;
//                }
//                mProgressFragmentUtil.showLoadingProgress(new ProgressFragmentUtil.IProgressListener() {
//                    @Override
//                    public void onStop() {
//                        // 取消裁剪
//                        mProgressFragmentUtil.dismissLoadingProgress();
//                        VideoGenerateKit.getInstance().stopGenerate();
//                        PlayerManagerKit.getInstance().startPlay();
//                        // 未加载完缩略图，重新进行加载
//                        if (!mComplete) {
//                            TXCLog.i(TAG, "[UGCKit][VideoCut]last load uncomplete, reload thunmail");
//                            loadThumbnail(mTXVideoInfo);
//                        }
//                    }
//                });
//                PlayerManagerKit.getInstance().stopPlay();
//                //如果图片没有加载完，先停止加载
//                ProcessKit.getInstance().stopProcess();
//                VideoGenerateKit.getInstance().startGenerate();
//            }
//        });
//        // 监听电话
////        TelephonyUtil.getInstance().initPhoneListener();
//    }
//
//    // 获取默认的封面路径
//    public String getDefaultCoverPath() {
//        return mCoverPath;
//    }
//
//    public long getVideoDuration() {
//        return mVideoDuration;
//    }
//
//    @Override
//    public void setVideoPath(final String videoPath) {
//        TXCLog.i(TAG, "[UGCKit][VideoCut]setVideoPath:" + videoPath);
//        if (TextUtils.isEmpty(videoPath)) {
//            Toast.makeText(getContext(), _AlbumUtils.getResStringId(getContext(), "ugckit_video_cutter_activity_oncreate_an_unknown_error_occurred_the_path_cannot_be_empty"), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        VideoEditerSDK.getInstance().setVideoPath(videoPath);
//        // 重新设置路径，缩略图重新加载
//        mComplete = false;
//        // 显示圆形进度条
//        showLoading();
//        CoreAlbumManager.getInstance().execute(new Runnable() {
//            @Override
//            public void run() {
//                // 加载视频信息,耗时
//                try {
//                    mTXVideoInfo = TXVideoInfoReader.getInstance(UGCKit.getAppContext()).getVideoFileInfo(videoPath);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (mTXVideoInfo != null && mTXVideoInfo.coverImage != null) {
//                    // 保存封面
//                    String coverPath = _AlbumUtils.getImagePath(UGCKit.getAppContext()) + "cover_" + System.currentTimeMillis() + ".jpg";
//                    if (_AlbumUtils.saveBitmap(mTXVideoInfo.coverImage, coverPath, 100)) {
//                        mCoverPath = coverPath;
//                    }
//                }
//                post(new Runnable() {
//                    @Override
//                    public void run() {
//                        isInit = true;
//                        // 圆形进度条消失
//                        hideLoading();
//
//                        if (mTXVideoInfo == null) {
//                            DialogUtil.showDialog(getContext(), getResources().getString(_AlbumUtils.getResStringId(getContext(), "ugckit_video_cutter_activity_video_main_handler_edit_failed")),
//                                    getResources().getString(_AlbumUtils.getResStringId(getContext(), "ugckit_does_not_support_android_version_below_4_3")), new OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            ((VideoEditActivity)getContext()).fileIsNotVideo();
//                                        }
//                                    });
//                        } else {
//                            mVideoDuration = mTXVideoInfo.duration;
//                            VideoEditerSDK.getInstance().setTXVideoInfo(mTXVideoInfo);
//                            getVideoCutLayout().setVideoInfo(mTXVideoInfo);
//                            getVideoCutLayout().setOnRotateVideoListener(new IVideoCutLayout.OnRotateVideoListener() {
//                                @Override
//                                public void onRotate(int rotation) {
//                                    VideoEditerSDK.getInstance().getEditer().setRenderRotation(rotation);
//                                }
//                            });
//                            TXCLog.i(TAG, "[UGCKit][VideoCut]load thunmail");
//                            loadThumbnail(mTXVideoInfo);
//                            // 播放视频
//                            // 初始化播放器界面[必须在setPictureList/setVideoPath设置数据源之后]
//                            getVideoPlayLayout().initPlayerLayout();
//                            PlayerManagerKit.getInstance().startPlayCutTime();
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//    private void loadThumbnail(TXVideoEditConstants.TXVideoInfo videoInfo) {
//        // 初始化缩略图列表，裁剪缩略图时间间隔3秒钟一张
//        getVideoCutLayout().clearThumbnail();
//        final int interval = 3000;
//
//        // 计算整个视频需要加载的缩略图数量
//        int count = 0;
//        if (videoInfo == null) {
//            videoInfo = VideoEditerSDK.getInstance().getTXVideoInfo();
//        }
//        if (videoInfo != null) {
//            count = (int) (videoInfo.duration / interval);
//        }
//        if (count <= 0) {
//            count = 1;
//        }
//        final int thumbnailCount = count;
//
//        VideoEditerSDK.getInstance().initThumbnailList(thumbnailCount, interval, new TXVideoEditer.TXThumbnailListener() {
//            @Override
//            public void onThumbnail(final int index, long timeMs, final Bitmap bitmap) {
//                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getVideoCutLayout().addThumbnail(index, bitmap);
//
//                        if (index >= thumbnailCount - 1) { // Note: index从0开始增长
//                            mComplete = true;
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//    OnCutListener listener;
//
//    @Override
//    public void setOnCutListener(final OnCutListener listener) {
//        if (listener == null) {
//            ProcessKit.getInstance().setOnUpdateUIListener(null);
//            VideoGenerateKit.getInstance().setOnUpdateUIListener(null);
//            return;
//        }
//        this.listener = listener;
//        // 设置生成的监听器，用来更新控件
//        VideoGenerateKit.getInstance().setOnUpdateUIListener(new OnUpdateUIListener() {
//            @Override
//            public void onUIProgress(float progress) {
//                mProgressFragmentUtil.updateGenerateProgress((int) (progress * 100));
//            }
//
//            @Override
//            public void onUIComplete(int retCode, String descMsg) {
//                mProgressFragmentUtil.dismissLoadingProgress();
//                Log.i(TAG, "00000000000000000-------");
//                if (listener != null) {
//                    UGCKitResult ugcKitResult = new UGCKitResult();
//                    ugcKitResult.errorCode = retCode;
//                    ugcKitResult.descMsg = descMsg;
//                    ugcKitResult.outputPath = VideoGenerateKit.getInstance().getVideoOutputPath();
//                    ugcKitResult.coverPath = VideoGenerateKit.getInstance().getCoverPath();
//                    ugcKitResult.duration = VideoGenerateKit.getInstance().getDuration();
//                    listener.onCutterCompleted(ugcKitResult);
//                }
//            }
//
//            @Override
//            public void onUICancel() {
//                if (listener != null) {
//                    listener.onCutterCanceled();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void startPlay() {
//        PlayerManagerKit.getInstance().addOnPreviewLitener(this);
//        PlayerManagerKit.getInstance().startPlay();
//        // 未加载完缩略图，重新进行加载
//        if (isInit && !mComplete) {
//            TXCLog.i(TAG, "startPlay, [UGCKit][VideoCut]last load uncomplete, reload thunmail");
//            loadThumbnail(mTXVideoInfo);
//        }
//    }
//
//    @Override
//    public void stopPlay() {
//        PlayerManagerKit.getInstance().stopPlay();
//        PlayerManagerKit.getInstance().removeOnPreviewListener(this);
//        VideoGenerateKit.getInstance().stopGenerate();
//        mProgressFragmentUtil.dismissLoadingProgress();
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        VideoEditerSDK.getInstance().clear();
//    }
//
//    @Override
//    public void release() {
////        TelephonyUtil.getInstance().uninitPhoneListener();
//    }
//
//    @Override
//    public String getVideoOutputPath() {
//        return VideoGenerateKit.getInstance().getVideoOutputPath();
//    }
//
//    @Override
//    public void onPreviewProgress(int time) {
//
//    }
//
//    @Override
//    public void onPreviewFinish() {
//        // 循环播放
//        PlayerManagerKit.getInstance().startPlay();
//    }
//}
