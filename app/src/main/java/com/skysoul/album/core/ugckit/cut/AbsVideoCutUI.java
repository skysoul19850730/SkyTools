//package com.skysoul.album.core.ugckit.cut;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.RelativeLayout;
//
//import com.skysoul.album.core.ugckit.basic.TitleBarLayout;
//import com.skysoul.album.core.ugckit.cut.VideoCutLayout;
//import com.skysoul.album.core.ugckit.cut.VideoPlayLayout;
//import com.skysoul.album.util._AlbumUtils;
//
//
//public abstract class AbsVideoCutUI extends RelativeLayout implements IVideoCutKit {
//
//    private TitleBarLayout mTitleBar;
//    private VideoPlayLayout mVideoPlayLayout;
//    private VideoCutLayout mVideoCutLayout;
//    private View mLoadingView;
//
//    public AbsVideoCutUI(Context context) {
//        super(context);
//        initViews();
//    }
//
//    public AbsVideoCutUI(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initViews();
//    }
//
//    public AbsVideoCutUI(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initViews();
//    }
//
//    private void initViews() {
//        inflate(getContext(), _AlbumUtils.getResLayoutId(getContext(), "album_ugckit_video_cut_layout"), this);
//
//        mTitleBar = findViewById(_AlbumUtils.getResViewId(getContext(), "album_titleBar_layout"));
//        mVideoPlayLayout = findViewById(_AlbumUtils.getResViewId(getContext(), "album_video_play_layout"));
//        mVideoCutLayout = findViewById(_AlbumUtils.getResViewId(getContext(), "album_video_cut_layout"));
//        mLoadingView = findViewById(_AlbumUtils.getResViewId(getContext(), "album_loading_layout"));
//        _AlbumUtils.topbarApi35Margin(getContext(), mTitleBar);
//    }
//
//    public TitleBarLayout getTitleBar() {
//        return mTitleBar;
//    }
//
//    public VideoPlayLayout getVideoPlayLayout() {
//        return mVideoPlayLayout;
//    }
//
//    /**
//     * 获取裁剪工具栏
//     */
//    public VideoCutLayout getVideoCutLayout() {
//        return mVideoCutLayout;
//    }
//
//    public void showLoading() {
//        mLoadingView.setVisibility(VISIBLE);
//    }
//
//    public void hideLoading() {
//        mLoadingView.setVisibility(GONE);
//    }
//}
