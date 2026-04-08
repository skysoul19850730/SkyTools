//package com.skysoul.album.core.ugckit.cut;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.widget.FrameLayout;
//
//
//import com.tencent.ugc.TXVideoEditConstants;
//import com.tencent.ugc.TXVideoEditer;
//import com.skysoul.album.core.ugckit.VideoEditerSDK;
//import com.skysoul.album.util._AlbumUtils;
//
///**
// * 视频预览播放Layout
// */
//public class VideoPlayLayout extends FrameLayout {
//
//    private FrameLayout mLayoutPlayer;
//
//    public VideoPlayLayout(Context context) {
//        super(context);
//        initViews();
//    }
//
//    public VideoPlayLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initViews();
//    }
//
//    public VideoPlayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initViews();
//    }
//
//    private void initViews() {
//        inflate(getContext(), _AlbumUtils.getResLayoutId(getContext(), "wpsdk_album_ugckit_video_play_layout"), this);
//
//        mLayoutPlayer = (FrameLayout) findViewById(_AlbumUtils.getResViewId(getContext(), "wpsdk_album_layout_player"));
//    }
//
//    /**
//     * 初始化预览播放器
//     */
//    public void initPlayerLayout() {
//        TXVideoEditConstants.TXPreviewParam param = new TXVideoEditConstants.TXPreviewParam();
//        param.videoView = mLayoutPlayer;
//        param.renderMode = TXVideoEditConstants.PREVIEW_RENDER_MODE_FILL_EDGE;
//        TXVideoEditer videoEditer = VideoEditerSDK.getInstance().getEditer();
//        if (videoEditer != null) {
//            videoEditer.initWithPreview(param);
//        }
//    }
//}
