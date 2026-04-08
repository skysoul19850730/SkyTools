//package com.skysoul.album.ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.View;
//import static android.view.View.INVISIBLE;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
////import com.tencent.rtmp.TXLiveConstants;
////import com.tencent.rtmp.ui.TXCloudVideoView;
////import com.tencent.ugc.TXRecordCommon;
//import com.skysoul.album.OnCropCallback;
//import com.skysoul.album.bean.MediaInfo;
//import com.skysoul.album.bean.MediaType;
//import com.skysoul.album.core.CoreAlbumManager;
//import com.skysoul.album.core.video.UGCRecordConfig;
////import com.skysoul.album.core.video.VideoRecordSDK;
//import com.skysoul.album.util.AlbumConstant;
//import com.skysoul.album.util._AlbumUtils;
//import com.skysoul.album.view.RecordBtn;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//
///**
// * 拍照和拍视频
// */
//public class MediaTakeActivity extends AlbumBaseActivity{
//
//    public static void launch(Activity activity, MediaType mediaType, int videoLimitDuration, boolean useLandscape) {
//        Intent intent = new Intent(activity, MediaTakeActivity.class);
//        intent.putExtra(AlbumConstant.key_media_type, mediaType);
//        intent.putExtra(AlbumConstant.key_video_limit_duration, videoLimitDuration);
//        intent.putExtra(AlbumConstant.key_use_screen_orientation_landscape, useLandscape);
//        activity.startActivityForResult(intent, AlbumConstant.TAKE_REQUEST_CODE);
//    }
//
//    private MediaType mediaType = MediaType.IMAGE;
//    private int videoLimitDuration;
////    private TXCloudVideoView videoView;
////    private VideoRecordSDK mVideoRecordSDK;
//    private ImageView ivSwitch;
//    private ImageView ivClose;
//    private boolean isFromOther = false;        // 是否从外部打开
//    private OnCropCallback mOnCropCallback;
//
//    @Override
//    protected int getLayoutId() {
//        return _AlbumUtils.getResLayoutId(this, "album_activity_media_take");
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        _AlbumUtils.topbarApi35(this,"parent");
//
//        videoLimitDuration = getIntent().getIntExtra(AlbumConstant.key_video_limit_duration, 30);
//        Serializable media_type = getIntent().getSerializableExtra(AlbumConstant.key_media_type);
//        if (media_type instanceof MediaType) {
//            mediaType = (MediaType) media_type;
//        }
//
//        isFromOther = getIntent().getBooleanExtra(AlbumConstant.key_from_other, false);
//        if (isFromOther) {
//            int index = getIntent().getIntExtra(AlbumConstant.key_index, 0);
//            if (index > 0) {
//                mOnCropCallback = (OnCropCallback) CoreAlbumManager.getInstance().getAlbumCallback(index);
//            }
//        }
//
////        mVideoRecordSDK = new VideoRecordSDK(this);
//        ivClose = findViewById(_AlbumUtils.getResViewId(this, "_album_iv_close"));
//        ivClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        ivSwitch = findViewById(_AlbumUtils.getResViewId(this, "_album_iv_switch"));
//        ivSwitch.setVisibility(INVISIBLE);
////        ivSwitch.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mVideoRecordSDK.switchCamera();
////            }
////        });
////        videoView = findViewById(_AlbumUtils.getResViewId(this, "_album_videoView"));
//        initData();
//    }
//
//    private void initData() {
//        UGCRecordConfig ugcRecordConfig = new UGCRecordConfig();
//        int marginTop;
//        // 设置屏幕方向
//        if (mUseLandscape) {
//            marginTop = _AlbumUtils.dp2px(25);
////            ugcRecordConfig.renderRotation = TXLiveConstants.RENDER_ROTATION_LANDSCAPE;
//        } else {
//            marginTop = _AlbumUtils.dp2px(10);
////            ugcRecordConfig.renderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
//        }
//
//        ViewGroup.MarginLayoutParams ivSwitchLayoutParams = (ViewGroup.MarginLayoutParams) ivSwitch.getLayoutParams();
//        ViewGroup.MarginLayoutParams icCloseLayoutParams = (ViewGroup.MarginLayoutParams) ivClose.getLayoutParams();
//        if (ivSwitchLayoutParams != null) {
//            ivSwitchLayoutParams.topMargin = marginTop;
//        }
//        if (icCloseLayoutParams != null) {
//            icCloseLayoutParams.topMargin = marginTop;
//        }
//
////        mVideoRecordSDK.initConfig(ugcRecordConfig);
//        TextView _album_tv_hint = findViewById(_AlbumUtils.getResViewId(this, "_album_tv_hint"));
//        if (mediaType == MediaType.IMAGE) {
//            _album_tv_hint.setText("轻触拍照");
//        } else if (mediaType == MediaType.VIDEO) {
//            _album_tv_hint.setText("长按拍摄");
//            initVideo();
//        } else {
//            _album_tv_hint.setText("长按拍摄/轻触拍照");
//            initVideo();
//        }
//        RecordBtn recordBtn = findViewById(_AlbumUtils.getResViewId(this, "_album_iv_recordBtn"));
//        recordBtn.setMediaType(mediaType);
//        recordBtn.setLoadingTime(videoLimitDuration);
//        recordBtn.setOnProgressTouchListener(new RecordBtn.OnProgressTouchListener() {
//            @Override
//            public void onClick() {
//                takeCamera();
//            }
//
//            @Override
//            public void onLongClick() {
//                ivSwitch.setVisibility(View.GONE);
//                startRecord();
//            }
//
//            @Override
//            public void onLongClickUp() {
//                onAnimatorFinish();
//            }
//
//            @Override
//            public void onAnimatorFinish() {
//                ivSwitch.setVisibility(View.VISIBLE);
////                mVideoRecordSDK.stopRecord();
//            }
//        });
//    }
//
//    private void initVideo() {
////        mVideoRecordSDK.setVideoRecordListener(new VideoRecordSDK.OnVideoRecordListener() {
////            long duration = 0L;
////            @Override
////            public void onRecordEvent(int p0, Bundle p1) {
////
////            }
////
////            @Override
////            public void onRecordProgress(Long milliSecond) {
////                duration = milliSecond;
////            }
////
////            @Override
////            public void onRecordComplete(TXRecordCommon.TXRecordResult result) {
////                if (result.retCode == TXRecordCommon.RECORD_RESULT_OK || result.retCode == TXRecordCommon.RECORD_RESULT_OK_REACHED_MAXDURATION) {
////                    Toast.makeText(MediaTakeActivity.this.getApplicationContext(), "正在处理请稍后...", Toast.LENGTH_SHORT).show();
////                    MediaInfo mediasInfo = new MediaInfo();
////                    mediasInfo.isTakeByCamera = true;
////                    mediasInfo.path = result.videoPath;
////                    mediasInfo.mediaType = MediaType.VIDEO.mediaType;
////                    mediasInfo.coverPath = result.coverPath;
////                    mediasInfo.duration = duration;
////                    Intent intent = new Intent();
////                    intent.putExtra(AlbumConstant.key_media_info, mediasInfo);
////                    setResult(RESULT_OK, intent);
////                    finish();
////                } else {
////                    // 录制失败
////                    Toast.makeText(MediaTakeActivity.this, "录制失败", Toast.LENGTH_SHORT).show();
////                    setResult(RESULT_CANCELED);
////                    finish();
////                }
////            }
////        });
//    }
//
//    private void takeCamera() {
//        if (mediaType == MediaType.IMAGE || mediaType == MediaType.ALL) {
//            mVideoRecordSDK.takePhoto(new VideoRecordSDK.OnSnapshotListener() {
//                @Override
//                public void onSnapshot(Bitmap bitmap) {
//                    String imagePath = _AlbumUtils.getImagePath(MediaTakeActivity.this) + "camera_" + System.currentTimeMillis() + ".jpg";
//                    MediaInfo mediasInfo = new MediaInfo();
//                    mediasInfo.isTakeByCamera = true;
//                    mediasInfo.width = bitmap.getWidth();
//                    mediasInfo.height = bitmap.getHeight();
//                    mediasInfo.path = imagePath;
//                    mediasInfo.mediaType = MediaType.IMAGE.mediaType;
//                    _AlbumUtils.saveBitmap(bitmap, imagePath, 100);
//                    bitmap.recycle();
//
//                    if (isFromOther) {
//                        if (mMediaOptions.isUseCrop) {
//                            com.skysoul.album.ui.ImageCropActivity.launchActivityForResult(MediaTakeActivity.this, mediasInfo, mMediaOptions, mUseLandscape);
//                        } else {
//                            ArrayList<MediaInfo> list = new ArrayList<>();
//                            list.add(mediasInfo);
//                            compressAndCallback(list, mOnCropCallback);
//                        }
//                    } else {
//                        Intent intent = new Intent();
//                        intent.putExtra(AlbumConstant.key_media_info, mediasInfo);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                }
//            });
//        }
//    }
//
//    private void startRecord() {
//        String videoPath = _AlbumUtils.getVideoPath(this) + System.currentTimeMillis() + ".mp4";
//        String coverPath = _AlbumUtils.getImagePath(this) + System.currentTimeMillis() + ".jpg";
//        mVideoRecordSDK.startRecord(videoPath, coverPath);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mVideoRecordSDK.startCameraPreview(videoView);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mVideoRecordSDK.stopCameraPreview();
//        mVideoRecordSDK.pauseRecord();
//    }
//
//    protected void onDestroy() {
//        super.onDestroy();
//        mVideoRecordSDK.releaseRecord();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == AlbumConstant.CROP_REQUEST_CODE) {
//            /**
//             * {@link com.skysoul.album.ui.ImageCropActivity} 图片裁剪
//             */
//            if (isFromOther && data != null) {
//                // 裁剪结果
//                MediaInfo cropMediaInfo = data.getParcelableExtra(AlbumConstant.key_media_info);
//                // 直接回调
//                if (cropMediaInfo != null) {
//                    ArrayList<MediaInfo> mediaList = new ArrayList<>();
//                    mediaList.add(cropMediaInfo);
//                    compressAndCallback(mediaList, mOnCropCallback);
//                }
//            }
//        }
//    }
//}
