package com.skysoul.album.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.core.CoreAlbumManager;
import com.skysoul.album.util.AlbumConstant;
import com.skysoul.album.util._AlbumUtils;
import com.skysoul.album.view.photoview.PhotoView;
import com.skysoul.album.view.viewpager.PagerAdapter;
import com.skysoul.album.view.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 * 图片预览
 */
public class ImagePreViewActivity extends AlbumBaseActivity {

    // 从选择器界面跳转到预览界面使用静态变量的方式传递数据
    private static ArrayList<MediaInfo> MediainfoList_STATIC;
    private static ArrayList<MediaInfo> SelectedImageList_STATIC;
    protected static void launchActivityForResult(Activity activity, ArrayList<MediaInfo> mediaInfoList,
                                               ArrayList<MediaInfo> selectedMediaLists, MediaInfo clickMediaInfo,
                                               int maxSelectedCount, boolean useLandscape) {
        Intent intent = new Intent(activity, ImagePreViewActivity.class);
        MediainfoList_STATIC = new ArrayList<>();
        for (MediaInfo mediaInfo: mediaInfoList) {
            if (mediaInfo.isImage()) {
                MediainfoList_STATIC.add(mediaInfo);
            }
        }
        SelectedImageList_STATIC = selectedMediaLists;
        intent.putExtra(AlbumConstant.key_cur_position, MediainfoList_STATIC.indexOf(clickMediaInfo));
        intent.putExtra(AlbumConstant.key_option_max_selected_count, maxSelectedCount);
        intent.putExtra(AlbumConstant.key_use_screen_orientation_landscape, useLandscape);
        activity.startActivityForResult(intent, AlbumConstant.PREVIEW_REQUEST_CODE);
    }


    private ViewPager mViewPager;
    private final ImagePreViewAdapter mImageAdapter = new ImagePreViewAdapter();
    private final ArrayList<MediaInfo> mediaInfoList = new ArrayList<>();
    private final ArrayList<MediaInfo> selectedImageList = new ArrayList<>();
    private int maxSelectedCount = -1;
    private TextView mTvIndicator;
    private TextView mTvChecked;

    @Override
    protected int getLayoutId() {
        return _AlbumUtils.getResLayoutId(this, "album_activity_image_preview_layout");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _AlbumUtils.topbarApi35(this,"ly_head");

        // 最大选择数
        maxSelectedCount = getIntent().getIntExtra(AlbumConstant.key_option_max_selected_count, -1);

        mTvIndicator = findViewById(_AlbumUtils.getResViewId(this, "_album_tv_image_indicator"));
        mTvChecked = findViewById(_AlbumUtils.getResViewId(this, "_album_tv_checked"));
        mViewPager = findViewById(_AlbumUtils.getResViewId(this, "_album_view_pager"));
        if (MediainfoList_STATIC != null) {
            mediaInfoList.addAll(MediainfoList_STATIC);
            MediainfoList_STATIC = null;
        }
        if (SelectedImageList_STATIC != null) {
            selectedImageList.addAll(SelectedImageList_STATIC);
            SelectedImageList_STATIC = null;
        }

        mViewPager.setAdapter(mImageAdapter);
        mViewPager.setPageMargin(_AlbumUtils.dp2px(10));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageSelected(int position) {
                updateCheckState(position);
            }
        });
        // 当前要展示图片的下标
        int curPosition = getIntent().getIntExtra(AlbumConstant.key_cur_position, 0);
        mViewPager.setCurrentItem(curPosition);
        updateCheckState(curPosition);


        // 选中控件点击
        findViewById(_AlbumUtils.getResViewId(this, "_album_ly_checked")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mViewPager.getCurrentItem();
                if (mediaInfoList.size() > 0 && currentItem < mediaInfoList.size()) {
                    if (mTvChecked.isSelected()) {
                        mTvChecked.setSelected(false);
                        mTvChecked.setText("");
                        mediaInfoList.get(currentItem).isChecked = false;
                        selectedImageList.remove(mediaInfoList.get(currentItem));
                    } else {

                        if (selectedImageList.size() >= maxSelectedCount) {
                            Toast.makeText(ImagePreViewActivity.this.getApplicationContext(), "最多只能选择"+maxSelectedCount+"张", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent();
                        intent.putExtra(AlbumConstant.key_media_info, mediaInfoList.get(currentItem));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        // 返回箭头点击
        findViewById(_AlbumUtils.getResViewId(this, "_album_iv_back")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void updateCheckState(final int position) {
        if (mediaInfoList.size()>position && selectedImageList.contains(mediaInfoList.get(position))) {
            mTvChecked.setSelected(true);
            mTvChecked.setText(_AlbumUtils.getLocationForList(mediaInfoList.get(position), selectedImageList));
        } else {
            mTvChecked.setSelected(false);
            mTvChecked.setText("");
        }
        mTvIndicator.setText((position +1)+"/"+mediaInfoList.size());
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
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            if(viewPool.size() > 0){
                view = viewPool.poll();
            } else {
                view = LayoutInflater.from(container.getContext()).inflate(_AlbumUtils.getResLayoutId(ImagePreViewActivity.this, "album_image_preview_item"), container, false);
            }
            PhotoView photoView = view.findViewById(_AlbumUtils.getResViewId(ImagePreViewActivity.this, "_album_photoView"));
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
