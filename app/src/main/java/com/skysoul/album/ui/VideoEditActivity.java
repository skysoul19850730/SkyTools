//package com.skysoul.album.ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//
//import com.skysoul.album.OnCropCallback;
//import com.skysoul.album.bean.MediaInfo;
//import com.skysoul.album.bean.MediaType;
//import com.skysoul.album.core.CoreAlbumManager;
//import com.skysoul.album.core.MediaLoadManager;
//import com.skysoul.album.core.MediaOptions;
//import com.skysoul.album.core.ugckit.UGCKit;
//import com.skysoul.album.core.ugckit.UGCKitVideoCut;
//import com.skysoul.album.core.ugckit.basic.UGCKitResult;
//import com.skysoul.album.core.ugckit.cut.IVideoCutKit;
//import com.skysoul.album.core.ugckit.utils.CoverUtil;
//import com.skysoul.album.ui.VideoCoverActivity;
//import com.skysoul.album.util.AlbumConstant;
//import com.skysoul.album.util._AlbumUtils;
//
//import java.util.ArrayList;
//
///**
// * 视频剪辑界面
// * 1. VideoEditAction 外部直接打开视频剪辑界面
// * 2. 内部：录制完成
// *         选择的视频点击下一步
// */
//public class VideoEditActivity extends AlbumBaseActivity{
//
//    protected static void launchActivityForResult(Activity activity, MediaInfo mediaInfo, MediaOptions mediaOptions, boolean useLandscape) {
//        Intent intent = new Intent(activity, VideoEditActivity.class);
//        intent.putExtra(AlbumConstant.key_media_info, mediaInfo);
//        intent.putExtra(AlbumConstant.key_use_screen_orientation_landscape, useLandscape);
//        intent.putExtra(AlbumConstant.key_media_option, mediaOptions);
//        activity.startActivityForResult(intent, AlbumConstant.VIDEO_EDIT_REQUEST_CODE);
//    }
//
//    private MediaInfo mMediaInfo;
//    private UGCKitVideoCut mUGCKitVideoCut;
//    private boolean isFromOther;
//    private OnCropCallback mOnCropCallback;
//
//    @Override
//    protected int getLayoutId() {
//        return _AlbumUtils.getResLayoutId(this, "album_activity_video_edit");
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        UGCKit.setVideoLimitCropDuration(mMediaOptions.videoLimitCropDuration);
//        isFromOther = getIntent().getBooleanExtra(AlbumConstant.key_from_other, false);
//        mMediaInfo = getIntent().getParcelableExtra(AlbumConstant.key_media_info);
//        if (isFromOther) {
//            // 外部打开
//            int index = getIntent().getIntExtra(AlbumConstant.key_index, 0);
//            if (index > 0) {
//                mOnCropCallback = (OnCropCallback) CoreAlbumManager.getInstance().getAlbumCallback(index);
//            }
//        }
//
//        mUGCKitVideoCut = findViewById(_AlbumUtils.getResViewId(this, "album_mUGCKitVideoCut"));
//        mUGCKitVideoCut.setVideoPath(mMediaInfo.path);
//        mUGCKitVideoCut.getTitleBar().setOnBackClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               onBackPressed();
//            }
//        });
//
//        if (mUseLandscape) {
//            mUGCKitVideoCut.getVideoCutLayout().setPadding(_AlbumUtils.dp2px(30), 0, 0, 0);
//        }
//
//        mUGCKitVideoCut.setOnCutListener(new IVideoCutKit.OnCutListener() {
//            @Override
//            public void onCutterCompleted(UGCKitResult ugcKitResult) {
//                if (ugcKitResult != null) {
//                    // 设置剪辑后的路径和封面图
//                    mMediaInfo.path = ugcKitResult.outputPath;
//                    mMediaInfo.coverPath = ugcKitResult.coverPath;
//                    mMediaInfo.duration = ugcKitResult.duration;
//                    mMediaInfo.compressPath = "";
//                    startVideoCover();
//                } else {
//                    String defaultCoverPath = mUGCKitVideoCut.getDefaultCoverPath();
//                    if (mMediaInfo.duration <= 0) {
//                        mMediaInfo.duration = mUGCKitVideoCut.getVideoDuration();
//                    }
//                    if (TextUtils.isEmpty(defaultCoverPath)) {
//                        // 异常情况，如果缩略图为空，则尝试从视频源获取封面
//                        CoverUtil.getInstance().setInputPath(mMediaInfo.path);
//                        CoverUtil.getInstance().createThumbFile(new CoverUtil.ICoverListener() {
//                            @Override
//                            public void onCoverPath(String coverPath) {
//                                mMediaInfo.coverPath = coverPath;
//                                startVideoCover();
//                            }
//                        });
//                    } else {
//                        mMediaInfo.coverPath = defaultCoverPath;
//                        startVideoCover();
//                    }
//                }
//            }
//
//            @Override
//            public void onCutterCanceled() {
//
//            }
//        });
//    }
//
//    // 视频封面选择界面
//    private void startVideoCover() {
//        if (VideoEditActivity.this.isFinishing()) return;
//        VideoCoverActivity.launchActivityForResult(this, mMediaInfo, mMediaOptions, mUseLandscape);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == AlbumConstant.VIDEO_COVER_REQUEST_CODE) {
//                /**
//                 * {@link com.skysoul.album.ui.VideoCoverActivity} 封面选择界面
//                 */
//                if (data != null) {
//                    MediaInfo mediaInfo = data.getParcelableExtra(AlbumConstant.key_media_info);
//                    if (mediaInfo != null) {
//                        mMediaInfo.coverPath = mediaInfo.coverPath;
//                        mMediaInfo.cropPath = mediaInfo.cropPath;
//                        mMediaInfo.compressPath = mediaInfo.compressPath;
//                    }
//                    onSuccess();
//                } else {
//                    mUGCKitVideoCut.stopPlay();
//                }
//            }
//        }
//    }
//
//    private void onSuccess() {
//        // 压缩封面
//        if (mMediaOptions.isUseCompress && !TextUtils.isEmpty(mMediaInfo.coverPath)) {
//            MediaInfo info = new MediaInfo();
//            info.mediaType = MediaType.IMAGE.mediaType;
//            info.path = mMediaInfo.coverPath;
//            ArrayList<MediaInfo> list = new ArrayList<>();
//            list.add(info);
//            MediaLoadManager.compress(VideoEditActivity.this, list, mMediaOptions.imageLimitSize, new MediaLoadManager.CompressCallback() {
//                @Override
//                public void onStart() {
//
//                }
//
//                @Override
//                public void onFinished(final ArrayList<MediaInfo> list) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (VideoEditActivity.this.isFinishing()) return;
//                            mMediaInfo.compressPath = list.get(0).compressPath;
//                            setResult();
//                        }
//                    });
//                }
//            });
//        } else {
//            setResult();
//        }
//    }
//
//    private void setResult() {
//        if (isFromOther) {
//            if (mOnCropCallback != null) {
//                mOnCropCallback.onSuccess(mMediaInfo);
//            }
//        } else {
//            Intent intent = new Intent();
//            intent.putExtra(AlbumConstant.key_media_info, mMediaInfo);
//            setResult(RESULT_OK, intent);
//        }
//        finish();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mUGCKitVideoCut.startPlay();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mUGCKitVideoCut.stopPlay();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mUGCKitVideoCut.release();
//        mUGCKitVideoCut.setOnCutListener(null);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (isFromOther && mOnCropCallback != null) {
//            mOnCropCallback.onFail(CoreAlbumManager.code_cancel, CoreAlbumManager.getFailMsg(CoreAlbumManager.code_cancel));
//        }
//        finish();
//    }
//
//    public void fileIsNotVideo() {
//        if (isFromOther && mOnCropCallback != null) {
//            mOnCropCallback.onFail(CoreAlbumManager.code_media_error, CoreAlbumManager.getFailMsg(CoreAlbumManager.code_media_error));
//        }
//        finish();
//    }
//}
