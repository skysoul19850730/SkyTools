//package com.skysoul.album.ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import com.tencent.ugc.TXVideoEditConstants;
//import com.tencent.ugc.TXVideoEditer;
//import com.tencent.ugc.TXVideoInfoReader;
//import com.skysoul.album.OnCoverCallback;
//import com.skysoul.album.bean.MediaInfo;
//import com.skysoul.album.bean.MediaType;
//import com.skysoul.album.core.CoreAlbumManager;
//import com.skysoul.album.core.MediaLoadManager;
//import com.skysoul.album.core.MediaOptions;
//import com.skysoul.album.core.ugckit.UGCKit;
//import com.skysoul.album.core.ugckit.utils.CoverUtil;
//import com.skysoul.album.ui.ImageCropActivity;
//import com.skysoul.album.util.AlbumConstant;
//import com.skysoul.album.util._AlbumUtils;
//import com.skysoul.album.view.HorizontalListView;
//import java.util.ArrayList;
//
///**
// * 封面选择界面
// * 1. CoverAction 外部直接打开选择封面界面
// * 2. 内部：视频剪辑完成
// * 选择的视频，点击下一步时如果没有UGC-Licence
// */
//public class VideoCoverActivity extends AlbumBaseActivity {
//
//    protected static void launchActivityForResult(Activity activity, MediaInfo mediaInfo,
//                                                  MediaOptions mediaOptions, boolean useLandscape) {
//        Intent intent = new Intent(activity, VideoCoverActivity.class);
//        intent.putExtra(AlbumConstant.key_media_option, mediaOptions);
//        intent.putExtra(AlbumConstant.key_media_info, mediaInfo);
//        intent.putExtra(AlbumConstant.key_use_screen_orientation_landscape, useLandscape);
//        activity.startActivityForResult(intent, AlbumConstant.VIDEO_COVER_REQUEST_CODE);
//    }
//
//
//    private MediaInfo mediaInfo;
//    private final TXVideoEditer tXVideoEditer = new TXVideoEditer(UGCKit.getAppContext());
//    private TXVideoEditConstants.TXVideoInfo videoFileInfo;
//    private long videoDuration = 0;
//    private final ArrayList<CoverInfo> dataList = new ArrayList<>();
//    private final ImageAdapter mImageAdapter = new ImageAdapter();
//    private SeekBar seekBar;
//    private OnCoverCallback mOnCoverCallback;
//    private int thumbnailCount = 6;
//    private int thumbnailWidth;
//
//    private boolean isFromOther = false;        // 是否从内部打开
//
//    @Override
//    protected int getLayoutId() {
//        return _AlbumUtils.getResLayoutId(this, "album_activity_video_cover");
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        _AlbumUtils.topbarApi35(this, "album_head");
//        thumbnailWidth = _AlbumUtils.dp2px(45);
//        View view = findViewById(_AlbumUtils.getResViewId(this, "album_cover_layout"));
//        if (mUseLandscape) {
//            thumbnailCount = (getResources().getDisplayMetrics().widthPixels - _AlbumUtils.dp2px(80)) / thumbnailWidth;
//        } else {
//            thumbnailCount = (getResources().getDisplayMetrics().widthPixels - _AlbumUtils.dp2px(30)) / thumbnailWidth;
//        }
//        view.getLayoutParams().width = thumbnailCount * thumbnailWidth;
//
//        isFromOther = getIntent().getBooleanExtra(AlbumConstant.key_from_other, false);
//        mediaInfo = getIntent().getParcelableExtra(AlbumConstant.key_media_info);
//
//        if (isFromOther) {
//            int index = getIntent().getIntExtra(AlbumConstant.key_index, 0);
//            if (index > 0) {
//                mOnCoverCallback = (OnCoverCallback) CoreAlbumManager.getInstance().getAlbumCallback(index);
//            }
//        }
//
//        findViewById(_AlbumUtils.getResViewId(this, "album_tvBack")).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        seekBar = findViewById(_AlbumUtils.getResViewId(this, "album_seekbar"));
//
//        HorizontalListView horizontalListView = (HorizontalListView) findViewById(_AlbumUtils.getResViewId(this, "album_coverList"));
//        horizontalListView.setAdapter(mImageAdapter);
//        loadData();
//
//        ImageView ivCrop = findViewById(_AlbumUtils.getResViewId(this, "_album_iv_crop"));
//        if (mMediaOptions.isUseCrop && !mMediaOptions.isVideoCoverNoCrop) {
//            ivCrop.setVisibility(View.VISIBLE);
//            // 打开裁剪
//            ivCrop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (videoFileInfo == null) return;
//                    CoverUtil.getInstance().setInputPath(mediaInfo.path);
//                    CoverUtil.getInstance().createThumbFile((long) (seekBar.getProgress() * 1.0f / 100 * videoDuration), new CoverUtil.ICoverListener() {
//                        @Override
//                        public void onCoverPath(String coverPath) {
//                            if (VideoCoverActivity.this.isFinishing()) return;
//                            MediaInfo info = new MediaInfo();
//                            info.mediaType = MediaType.IMAGE.mediaType;
//                            info.path = coverPath;
//                            ImageCropActivity.launchActivityForResult(VideoCoverActivity.this, info, mMediaOptions, mUseLandscape);
//                        }
//                    });
//                }
//            });
//        } else {
//            ivCrop.setVisibility(View.GONE);
//        }
//
//
//        // 完成，保存选择的封面图
//        findViewById(_AlbumUtils.getResViewId(this, "album_tvSave")).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (videoFileInfo == null) return;
//                CoverUtil.getInstance().setInputPath(mediaInfo.path);
//                CoverUtil.getInstance().createThumbFile((long) (seekBar.getProgress() * 1.0f / 100 * videoDuration), new CoverUtil.ICoverListener() {
//                    @Override
//                    public void onCoverPath(String coverPath) {
//                        if (VideoCoverActivity.this.isFinishing()) return;
//                        mediaInfo.coverPath = coverPath;
//                        // 删除之前的裁剪
//                        mediaInfo.cropPath = "";
//                        mediaInfo.compressPath = "";
//                        done();
//                    }
//                });
//            }
//        });
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (videoFileInfo != null) {
//                    tXVideoEditer.previewAtTime((long) (progress * 1.0f / 100 * videoDuration));
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//    }
//
//    private void loadData() {
//        // 加载视频缩略图
//        if (mediaInfo != null) {
//            tXVideoEditer.setVideoPath(mediaInfo.path);
//
//            CoreAlbumManager.getInstance().execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        videoFileInfo = TXVideoInfoReader.getInstance(VideoCoverActivity.this).getVideoFileInfo(mediaInfo.path);
//                        if (videoFileInfo != null) {
//                            videoDuration = videoFileInfo.duration;
//                            tXVideoEditer.setCutFromTime(0, videoFileInfo.duration);
//                            mediaInfo.height = videoFileInfo.height;
//                            mediaInfo.width = videoFileInfo.width;
//                            mediaInfo.duration = videoFileInfo.duration;
//                            int thumbnailHeight = (int) (videoFileInfo.width * 1.0f / videoFileInfo.height * thumbnailWidth);
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    TXVideoEditConstants.TXPreviewParam param = new TXVideoEditConstants.TXPreviewParam();
//                                    param.videoView = findViewById(_AlbumUtils.getResViewId(VideoCoverActivity.this, "album_layout_player"));
//                                    ;
//                                    param.renderMode = TXVideoEditConstants.PREVIEW_RENDER_MODE_FILL_EDGE;
//                                    tXVideoEditer.initWithPreview(param);
//                                    // 不先调用 startPlayFromTime 直接调用previewAtTime会黑屏，暂没有找到其他使用方式
//                                    // 只能先startPlayFromTime 然后 pausePlay
//                                    tXVideoEditer.startPlayFromTime(0, videoFileInfo.duration);
//                                    tXVideoEditer.pausePlay();
//                                    tXVideoEditer.previewAtTime(0);
//                                }
//                            });
//
//                            tXVideoEditer.getThumbnail(thumbnailCount, thumbnailWidth, thumbnailHeight, false, new TXVideoEditer.TXThumbnailListener() {
//                                @Override
//                                public void onThumbnail(int index, long time, final Bitmap bitmap) {
//                                    dataList.add(new CoverInfo(time, bitmap));
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (!VideoCoverActivity.this.isFinishing()) {
//                                                mImageAdapter.notifyDataSetChanged();
//                                            }
//                                        }
//                                    });
//                                }
//                            });
//                        } else {
//                            loadDataError("videoFileInfo is null.");
//                        }
//                    } catch (final Exception exception) {
//                        loadDataError("exception = " + exception.getMessage());
//                    }
//                }
//            });
//        }
//    }
//
//    private void loadDataError(final String msg) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (isFromOther) {
//                    if (mOnCoverCallback != null) {
//                        mOnCoverCallback.onFail(CoreAlbumManager.code_media_error,
//                                CoreAlbumManager.getFailMsg(CoreAlbumManager.code_media_error) + ", " + msg);
//                    }
//                } else {
//                    setResult(RESULT_OK);
//                }
//                finish();
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (isFromOther) {
//            if (mOnCoverCallback != null) {
//                mOnCoverCallback.onFail(CoreAlbumManager.code_cancel, CoreAlbumManager.getFailMsg(CoreAlbumManager.code_cancel));
//            }
//        } else {
//            setResult(RESULT_OK);
//        }
//        finish();
//    }
//
//    private static class CoverInfo {
//        public final long time;
//        public final Bitmap bitmap;
//        public String coverPath;
//
//        CoverInfo(long time, Bitmap bitmap) {
//            this.time = time;
//            this.bitmap = bitmap;
//        }
//    }
//
//    private class ImageAdapter extends BaseAdapter {
//        @Override
//        public int getCount() {
//            return dataList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return dataList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//
//            if (convertView == null) {
//                convertView = LayoutInflater.from(parent.getContext())
//                        .inflate(_AlbumUtils.getResLayoutId(parent.getContext(), "album_item_cover"), parent, false);
//            }
//            ImageView ivCover = convertView.findViewById(_AlbumUtils.getResViewId(parent.getContext(), "album_img_cover"));
//            ivCover.setImageBitmap(dataList.get(position).bitmap);
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                ivCover.setClipToOutline(true);
////                ivCover.setOutlineProvider(new ViewOutlineProvider(){
////                    @Override
////                    public void getOutline(View view, Outline outline) {
////                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), _AlbumUtils.dp2px(4));
////                    }
////                });
////            }
//
//            return convertView;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == AlbumConstant.CROP_REQUEST_CODE && data != null) {
//                // 裁剪结果
//                MediaInfo cropMediaInfo = data.getParcelableExtra(AlbumConstant.key_media_info);
//                // 更新封面
//                mediaInfo.coverPath = cropMediaInfo.path;
//                // 更新裁剪路径
//                mediaInfo.cropPath = cropMediaInfo.cropPath;
//                mediaInfo.width = cropMediaInfo.width;
//                mediaInfo.height = cropMediaInfo.height;
//                mediaInfo.compressPath = cropMediaInfo.compressPath;
//                done();
//            }
//        }
//    }
//
//    private void done() {
//        // 先压缩 后 回调
//        if (mMediaOptions.isUseCompress) {
//            MediaInfo info = new MediaInfo();
//            info.mediaType = MediaType.IMAGE.mediaType;
//            info.path = mediaInfo.getShowPath();
//            ArrayList<MediaInfo> list = new ArrayList<>();
//            list.add(info);
//            MediaLoadManager.compress(VideoCoverActivity.this, list, mMediaOptions.imageLimitSize, new MediaLoadManager.CompressCallback() {
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
//                            mediaInfo.compressPath = list.get(0).compressPath;
//                            onSuccess();
//                        }
//                    });
//                }
//            });
//        } else {
//            onSuccess();
//        }
//    }
//
//    private void onSuccess() {
//        if (mOnCoverCallback != null) {
//            mOnCoverCallback.onSuccess(mediaInfo);
//        } else {
//            if (!isFromOther) {
//                Intent intent = new Intent();
//                intent.putExtra(AlbumConstant.key_media_info, mediaInfo);
//                setResult(RESULT_OK, intent);
//            }
//        }
//        finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        tXVideoEditer.release();
//    }
//}
