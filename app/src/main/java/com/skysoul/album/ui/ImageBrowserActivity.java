package com.skysoul.album.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skysoul.album.OnSelectedCallback;
import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.core.CoreAlbumManager;
import com.skysoul.album.core.MediaOptions;
import com.skysoul.album.core.ugckit.utils.CoverUtil;
import com.skysoul.album.ui.ImageCropActivity;
import com.skysoul.album.util.AlbumConstant;
import com.skysoul.album.util._AlbumUtils;
import com.skysoul.album.view.photoview.PhotoView;
import com.skysoul.album.view.viewpager.PagerAdapter;
import com.skysoul.album.view.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * 图片浏览
 */
public class ImageBrowserActivity extends AlbumBaseActivity {

    private static ArrayList<MediaInfo> SelectedImageList_STATIC;
    private ImageView mIvCrop;

    /**
     * {@link com.skysoul.album.ui.ImageSelectorActivity}中下一步点击打开浏览界面
     */
    protected static void launchActivityForResult(Activity activity, ArrayList<MediaInfo> selectedMediaLists,
                                                  MediaOptions mediaOptions, boolean useLandscape) {
        Intent intent = new Intent(activity, ImageBrowserActivity.class);
        SelectedImageList_STATIC = selectedMediaLists;
        intent.putExtra(AlbumConstant.key_media_option, mediaOptions);
        intent.putExtra(AlbumConstant.key_use_screen_orientation_landscape, useLandscape);
        activity.startActivityForResult(intent, AlbumConstant.BROWSER_REQUEST_CODE);
    }

    // 拍照图片打开浏览界面
    protected static void launchActivityForResult(Activity activity, MediaInfo mediaInfo,
                                                  MediaOptions mediaOptions, boolean useLandscape) {
        Intent intent = new Intent(activity, ImageBrowserActivity.class);
        intent.putExtra(AlbumConstant.key_media_option, mediaOptions);
        intent.putExtra(AlbumConstant.key_media_info, mediaInfo);
        intent.putExtra(AlbumConstant.key_use_screen_orientation_landscape, useLandscape);
        activity.startActivityForResult(intent, AlbumConstant.BROWSER_REQUEST_CODE);
    }


    private final ImagePreViewAdapter mImageAdapter = new ImagePreViewAdapter();
    private final ArrayList<MediaInfo> mediaInfoList = new ArrayList<>();
    private TextView mTvIndicator;
    private ViewPager mViewPager;
    private boolean isFromOther = false;        // 是否从外部打开
    private MediaInfo mTakeMediaInfo;           // 图片信息来源于相机拍照
    private OnSelectedCallback mOnSelectedCallback;
    private int position = 0;

    @Override
    protected int getLayoutId() {
        return _AlbumUtils.getResLayoutId(this, "wpsdk_album_activity_image_browser_layout");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _AlbumUtils.topbarApi35(this,"ly_head");
        isFromOther = getIntent().getBooleanExtra(AlbumConstant.key_from_other, false);

        /**
         * 打开浏览界面 ImageBrowserActivity 有以下几个来源
         *  1. 选择界面下一步按钮点击
         *  2. 图片浏览行为--外部打开该界面
         *  3. SDK内部行为打开该界面：
         *          拍照完成后(进行图片浏览及裁剪)
         *          视频裁剪完成后(进行封面浏览及裁剪)
         *          选择界面选择视频点击下一步UGC-Licence为空时
         */

        // 添加数据源
        if (isFromOther) {
            List<MediaInfo> list =  getIntent().getParcelableArrayListExtra(AlbumConstant.key_media_list);
            if (list != null) {
                mediaInfoList.addAll(list);
            }
            position = getIntent().getIntExtra(AlbumConstant.key_cur_position, 0);
            int index = getIntent().getIntExtra(AlbumConstant.key_index, 0);
            if (index > 0) {
                mOnSelectedCallback = (OnSelectedCallback) CoreAlbumManager.getInstance().getAlbumCallback(index);
            }
        } else {
            mTakeMediaInfo = getIntent().getParcelableExtra(AlbumConstant.key_media_info);
            // 为null说明是从选择界面下一步点击来的
            if (mTakeMediaInfo == null) {
                if (SelectedImageList_STATIC != null) {
                    mediaInfoList.addAll(SelectedImageList_STATIC);
                    SelectedImageList_STATIC = null;
                }
            } else {
                if (mTakeMediaInfo.isVideo()) {
                    // 如果是视频则裁剪封面
                    if (TextUtils.isEmpty(mTakeMediaInfo.coverPath)) {
                        // 获取视频封面
                        CoverUtil.getInstance().setInputPath(mTakeMediaInfo.path);
                        CoverUtil.getInstance().createThumbFile(new CoverUtil.ICoverListener() {
                            @Override
                            public void onCoverPath(String coverPath) {
                                if (ImageBrowserActivity.this.isFinishing()) return;
                                mTakeMediaInfo.coverPath = coverPath;
                                mediaInfoList.add(mTakeMediaInfo);
                                mImageAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        mediaInfoList.add(mTakeMediaInfo);
                    }
                } else {
                    mediaInfoList.add(mTakeMediaInfo);
                }
            }
        }

        mIvCrop = findViewById(_AlbumUtils.getResViewId(this, "_album_iv_crop"));
        if (mMediaOptions.isUseCrop) {
            mIvCrop.setVisibility(View.VISIBLE);
            // 打开裁剪
            mIvCrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageCropActivity.launchActivityForResult(ImageBrowserActivity.this, mediaInfoList.get(mViewPager.getCurrentItem()), mMediaOptions, mUseLandscape);
                }
            });
        } else {
            mIvCrop.setVisibility(View.GONE);
        }

        mTvIndicator = findViewById(_AlbumUtils.getResViewId(this, "_album_tv_image_indicator"));
        mViewPager = findViewById(_AlbumUtils.getResViewId(this, "_album_view_pager"));
        mViewPager.setPageMargin(_AlbumUtils.dp2px(10));
        mViewPager.setAdapter(mImageAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageSelected(int position) {
                mTvIndicator.setText((position+1)+"/"+mediaInfoList.size());
                updateCropState(position);
            }
        });
        if (mediaInfoList.size() > 0) {
            mTvIndicator.setText("1/" + mediaInfoList.size());
        }
        mViewPager.setCurrentItem(position);
        updateCropState(position);


        // 返回箭头点击
        findViewById(_AlbumUtils.getResViewId(this, "_album_iv_back")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 完成点击
        TextView tvFinish = findViewById(_AlbumUtils.getResViewId(this, "_album_tv_finish"));
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromOther) {
                    compressAndCallback(mediaInfoList, mOnSelectedCallback);
                } else {
                    Intent intent = new Intent();
                    if (mTakeMediaInfo != null) {
                        intent.putExtra(AlbumConstant.key_media_info, mTakeMediaInfo);
                    }
                    intent.putExtra(AlbumConstant.key_album_done, true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void updateCropState(int position) {
        // gif不能编辑
        if (mIvCrop.getVisibility() == View.VISIBLE) {
            if (position > -1 && position < mediaInfoList.size()) {
                if (mediaInfoList.get(position).isGif) {
                    mIvCrop.setAlpha(0.4f);
                    mIvCrop.setEnabled(false);
                } else {
                    mIvCrop.setAlpha(1f);
                    mIvCrop.setEnabled(true);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isFromOther) {
            invokeCallbackFail(CoreAlbumManager.code_cancel);
            super.onBackPressed();
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AlbumConstant.CROP_REQUEST_CODE && data != null) {
                // 裁剪结果
                MediaInfo cropMediaInfo = data.getParcelableExtra(AlbumConstant.key_media_info);
                if (cropMediaInfo != null) {
                    MediaInfo mediaInfo = null;
                    if (mViewPager.getCurrentItem() < mediaInfoList.size()) {
                        mediaInfo = mediaInfoList.get(mViewPager.getCurrentItem());
                    }
                    if (cropMediaInfo.equals(mediaInfo)) {
                        cropFromCropMediaInfo(mediaInfo, cropMediaInfo);
                    } else {
                        for (MediaInfo info : mediaInfoList) {
                            if (cropMediaInfo.equals(info)) {
                                cropFromCropMediaInfo(info, cropMediaInfo);
                                break;
                            }
                        }
                    }
                    mImageAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void cropFromCropMediaInfo(MediaInfo src, MediaInfo crop) {
        src.cropPath = crop.cropPath;
        src.width = crop.width;
        src.height = crop.height;
        src.fileSize = crop.fileSize;
        src.compressPath = crop.compressPath;
    }

    class ImagePreViewAdapter extends PagerAdapter {

        private final Queue<View> viewPool = new LinkedList<>(); //View池

        @Override
        public int getCount() {
            return mediaInfoList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            if(viewPool.size() > 0){
                view = viewPool.poll();
            } else {
                view = LayoutInflater.from(container.getContext()).inflate(_AlbumUtils.getResLayoutId(ImageBrowserActivity.this, "wpsdk_album_image_preview_item"), container, false);
            }
            PhotoView photoView = view.findViewById(_AlbumUtils.getResViewId(ImageBrowserActivity.this, "_album_photoView"));
            CoreAlbumManager.getInstance().getImageLoader().loadImage(photoView, mediaInfoList.get(position).getShowPath());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            //将当前View加入到池子中
            viewPool.offer((View) object);
        }
    }


    private void invokeCallbackFail(int code) {
        if (mOnSelectedCallback != null) {
            mOnSelectedCallback.onFail(code, CoreAlbumManager.getFailMsg(code));
        }
    }
}
