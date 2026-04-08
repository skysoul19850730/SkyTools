//package com.skysoul.album.core.ugckit.slider;
//
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.util.AttributeSet;
//import android.util.DisplayMetrics;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//
//import com.tencent.ugc.TXVideoEditConstants;
//import com.skysoul.album.core.ugckit.effect.Edit;
//import com.skysoul.album.core.ugckit.slider.RangeSlider;
//import com.skysoul.album.core.ugckit.slider.TCVideoEditerAdapter;
//import com.skysoul.album.util._AlbumUtils;
//import com.skysoul.album.view.HorizontalListView;
//
///**
// * 裁剪View
// */
//public class VideoCutView extends RelativeLayout implements com.skysoul.album.core.ugckit.slider.RangeSlider.OnRangeChangeListener {
//
//    private String TAG = "VideoCutView";
//
//    private Context mContext;
//
//    private HorizontalListView mHorizontalListView;
//    private com.skysoul.album.core.ugckit.slider.RangeSlider mRangeSlider;
//    private float mCurrentScroll;
//    /**
//     * 单个缩略图的宽度
//     */
//    private int mSingleWidth;
//    /**
//     * 所有缩略图的宽度
//     */
//    private int mAllWidth;
//    /**
//     * 整个视频的时长
//     */
//    private long mVideoDuration;
//    /**
//     * 控件最大时长16s
//     */
//    private long mViewMaxDuration;
//    /**
//     * 如果视频时长超过了控件的最大时长，底部在滑动时最左边的起始位置时间
//     */
//    private long mStartTime = 0;
//    /**
//     * 裁剪的起始时间，最左边是0
//     */
//    private int mViewLeftTime;
//    /**
//     * 裁剪的结束时间，最右边最大是16000ms
//     */
//    private int mViewRightTime;
//    /**
//     * 最终视频的起始时间
//     */
//    private long mVideoStartPos;
//    /**
//     * 最终视频的结束时间
//     */
//    private long mVideoEndPos;
//
//    private TCVideoEditerAdapter mAdapter;
//
//    private Edit.OnCutChangeListener mRangeChangeListener;
//
//    public VideoCutView(Context context) {
//        super(context);
//
//        init(context);
//    }
//
//    public VideoCutView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        init(context);
//    }
//
//    public VideoCutView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//
//        init(context);
//    }
//
//    private void init(Context context) {
//        mContext = context;
//        inflate(getContext(), _AlbumUtils.getResLayoutId(getContext(), "album_ugckit_item_edit_view"), this);
//
//        mRangeSlider = (com.skysoul.album.core.ugckit.slider.RangeSlider) findViewById(_AlbumUtils.getResViewId(getContext(), "album_range_slider"));
//        mRangeSlider.setRangeChangeListener(this);
//        mRangeSlider.setLeftIconResource(_AlbumUtils.getResDrawableId(getContext(), "album_ugckit_ic_progress_left"));
//        mRangeSlider.setRightIconResource(_AlbumUtils.getResDrawableId(getContext(), "album_ugckit_ic_progress_right"));
//        mRangeSlider.setTickCount(100);
//        mRangeSlider.setRangeIndex(0, 100);
//        mRangeSlider.setLineColor(Color.parseColor("#FF584C"));
//        mRangeSlider.setLineSize(_AlbumUtils.dp2px(3));
//        mRangeSlider.setThumbWidth(_AlbumUtils.dp2px(10));
//
//        mHorizontalListView = findViewById(_AlbumUtils.getResViewId(getContext(), "album_recycler_view"));
//        mHorizontalListView.setOnScrollStateChangedListener(mOnScrollListener);
//
//        mAdapter = new TCVideoEditerAdapter(mContext);
//        mHorizontalListView.setAdapter(mAdapter);
//
//        mSingleWidth = _AlbumUtils.dp2px(60);
//    }
//
//    /**
//     * 设置缩略图个数
//     *
//     * @param count
//     */
//    public void setCount(int count) {
//        ViewGroup.LayoutParams layoutParams = getLayoutParams();
//        int width = count * mSingleWidth;
//        mAllWidth = width;
//        Resources resources = getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
//        int screenWidth = dm.widthPixels;
//        if (width > screenWidth) {
//            width = screenWidth;
//        }
//        layoutParams.width = width + 2 * _AlbumUtils.dp2px(10);
//        setLayoutParams(layoutParams);
//    }
//
//    /**
//     * 设置裁剪Listener
//     *
//     * @param listener
//     */
//    public void setCutChangeListener(Edit.OnCutChangeListener listener) {
//        mRangeChangeListener = listener;
//    }
//
//    public void setMediaFileInfo(TXVideoEditConstants.TXVideoInfo videoInfo) {
//        if (videoInfo == null) {
//            return;
//        }
//        mVideoDuration = videoInfo.duration;
//        mViewMaxDuration = mVideoDuration;
//
//        mViewLeftTime = 0;
//        mViewRightTime = (int) mViewMaxDuration;
//
//        mVideoStartPos = 0;
//        mVideoEndPos = mViewMaxDuration;
//    }
//
//    public void addBitmap(int index, Bitmap bitmap) {
//        mAdapter.add(index, bitmap);
//    }
//
//    public void clearAllBitmap() {
//        mAdapter.clearAllBitmap();
//    }
//
//    @Override
//    public void onKeyDown(int type) {
//        if (mRangeChangeListener != null) {
//            mRangeChangeListener.onCutChangeKeyDown();
//        }
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if (mAdapter != null) {
//            mAdapter.clearAllBitmap();
//        }
//    }
//
//    @Override
//    public void onKeyUp(int type, int leftPinIndex, int rightPinIndex) {
//        mViewLeftTime = (int) (mViewMaxDuration * leftPinIndex / 100); //ms
//        mViewRightTime = (int) (mViewMaxDuration * rightPinIndex / 100);
//
//        onTimeChanged();
//    }
//
//    private void onTimeChanged() {
//        mVideoStartPos = mStartTime + mViewLeftTime;
//        mVideoEndPos = mStartTime + mViewRightTime;
//
//        if (mRangeChangeListener != null) {
//            mRangeChangeListener.onCutChangeKeyUp((int) mVideoStartPos, (int) mVideoEndPos, 0);
//        }
//    }
//
//    private final HorizontalListView.OnScrollStateChangedListener mOnScrollListener = new HorizontalListView.OnScrollStateChangedListener() {
//        @Override
//        public void onScrollStateChanged(ScrollState scrollState) {
//            switch (scrollState) {
//                case SCROLL_STATE_IDLE:
//                    onTimeChanged();
//                    break;
//                case SCROLL_STATE_TOUCH_SCROLL:
//                    if (mRangeChangeListener != null) {
//                        mRangeChangeListener.onCutChangeKeyDown();
//                    }
//                    break;
//                case SCROLL_STATE_FLING:
//
//                    break;
//            }
//        }
//
//        @Override
//        public void onScrolled(int dx, int dy) {
//            if (dx == 0) return;
//            mCurrentScroll = mCurrentScroll + (-dx);
//            float rate = mCurrentScroll / mAllWidth;
//            if (mCurrentScroll + mHorizontalListView.getWidth() >= mAllWidth) {
//                mStartTime = mVideoDuration - mViewMaxDuration;
//            } else {
//                mStartTime = (int) (rate * mVideoDuration);
//            }
//        }
//    };
//
//    public RangeSlider getRangeSlider() {
//        return mRangeSlider;
//    }
//
//}
