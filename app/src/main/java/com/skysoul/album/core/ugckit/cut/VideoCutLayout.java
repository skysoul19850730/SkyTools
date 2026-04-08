//package com.skysoul.album.core.ugckit.cut;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//
//import com.tencent.liteav.basic.log.TXCLog;
//import com.tencent.ugc.TXVideoEditConstants;
//import com.skysoul.album.core.ugckit.VideoEditerSDK;
//import com.skysoul.album.core.ugckit.cut.IVideoCutLayout;
//import com.skysoul.album.core.ugckit.effect.Edit;
//import com.skysoul.album.core.ugckit.moudle.PlayerManagerKit;
//import com.skysoul.album.core.ugckit.slider.VideoCutView;
//import com.skysoul.album.util._AlbumUtils;
//
//public class VideoCutLayout extends RelativeLayout implements IVideoCutLayout, View.OnClickListener, Edit.OnCutChangeListener {
//    private static final String TAG = "VideoCutLayout";
//
//    private ImageView mImageRotate;
//    private TextView mTextDuration;
//    private VideoCutView mVideoCutView;
//
//    private int mRotation;
//
//    private OnRotateVideoListener mOnRotateVideoListener;
//
//    public VideoCutLayout(Context context) {
//        super(context);
//        initViews();
//    }
//
//    public VideoCutLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initViews();
//    }
//
//    public VideoCutLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initViews();
//    }
//
//    private void initViews() {
//        inflate(getContext(), _AlbumUtils.getResLayoutId(getContext(), "wpsdk_album_ugckit_video_cut_kit"), this);
//
//        mTextDuration = (TextView) findViewById(_AlbumUtils.getResViewId(getContext(), "wpsdk_album_tv_choose_duration"));
//        mImageRotate = (ImageView) findViewById(_AlbumUtils.getResViewId(getContext(), "wpsdk_album_iv_rotate"));
//        mVideoCutView = (VideoCutView) findViewById(_AlbumUtils.getResViewId(getContext(), "wpsdk_album_video_edit_view"));
//
//        mImageRotate.setOnClickListener(this);
//        mVideoCutView.setCutChangeListener(this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        // 当旋转角度大于等于270度的时候，下一次就是0度；
//        mRotation = mRotation < 270 ? mRotation + 90 : 0;
//
//        if (mOnRotateVideoListener != null) {
//            mOnRotateVideoListener.onRotate(mRotation);
//        }
//    }
//
//    @Override
//    public void onCutClick() {
//
//    }
//
//    @Override
//    public void onCutChangeKeyDown() {
//        PlayerManagerKit.getInstance().stopPlay();
//    }
//
//    @Override
//    public void onCutChangeKeyUp(long startTime, long endTime, int type) {
//        long duration = (endTime - startTime) / 1000;
//
//        String str = getResources().getString(_AlbumUtils.getResStringId(getContext(), "ugckit_video_cutter_activity_load_video_success_already_picked")) + duration + "s";
//        mTextDuration.setText(str);
//
//        VideoEditerSDK.getInstance().setCutterStartTime(startTime, endTime);
//        PlayerManagerKit.getInstance().startPlay();
//
//        Log.d(TAG, "startTime:" + startTime + ",endTime:" + endTime + ",duration:" + duration);
//    }
//
//    @Override
//    public void setVideoInfo(TXVideoEditConstants.TXVideoInfo videoInfo) {
//        mRotation = 0;
//
//        int durationS = (int) (videoInfo.duration / 1000);
//        int time = durationS;
//        String str = getResources().getString(_AlbumUtils.getResStringId(getContext(), "ugckit_video_cutter_activity_load_video_success_already_picked")) + time + "s";
//        mTextDuration.setText(str);
//
//        int thumbCount = durationS / 3;
//        if (thumbCount <= 0) {
//            thumbCount = 1;
//        }
//
//        TXCLog.i(TAG, "[UGCKit][VideoCut]init cut time, start:" + 0 + ", end:" +videoInfo.duration);
//        VideoEditerSDK.getInstance().setCutterStartTime(0, videoInfo.duration);
//
//        mVideoCutView.setMediaFileInfo(videoInfo);
//        mVideoCutView.setCount(thumbCount);
//    }
//
//    @Override
//    public void addThumbnail(int index, Bitmap bitmap) {
//        mVideoCutView.addBitmap(index, bitmap);
//    }
//
//    public void clearThumbnail() {
//        mVideoCutView.clearAllBitmap();
//    }
//
//    public VideoCutView getVideoCutView() {
//        return mVideoCutView;
//    }
//
//    @Override
//    public void setOnRotateVideoListener(OnRotateVideoListener listener) {
//        mOnRotateVideoListener = listener;
//    }
//
//}
