package com.skysoul.album.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skysoul.album.ImageLoaderInterface;
import com.skysoul.album.OnSelectedCallback;
import com.skysoul.album.bean.MediaFolder;
import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.bean.MediaType;
import com.skysoul.album.core.CoreAlbumManager;
import com.skysoul.album.core.MediaLoadManager;
import com.skysoul.album.util.AlbumConstant;
import com.skysoul.album.util._AlbumUtils;
import com.skysoul.album.util._UriUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * 图片选择器
 */
public class ImageSelectorActivity extends AlbumBaseActivity {

    private final ArrayList<MediaInfo> mMediaImageList = new ArrayList<>();
    private final ArrayList<MediaFolder> mMediaFolderList = new ArrayList<>();
    private final ArrayList<MediaInfo> mSelectedImageList = new ArrayList<>();

    private View mTitleNameLayout;
    private TextView mTvFolderName;
    private ImageView mIvArrow;
    private TextView mTvNext;
    private View mFolderLayout;
    private View mFolderContentLayout;
    private int mFolderContentLayoutHeight = 0;
    private AdapterView mFolderListView;
    private FolderAdapter mFolderAdapter;

    private int maxSelectedCount;
    private boolean hasTakeCamera;
    //媒体类型
    private MediaType mediaType = MediaType.IMAGE;
    private int grid_NumColumns = 3;
    private GridView mGridView;
    private final ImageAdapter mImageAdapter = new ImageAdapter();
    private OnSelectedCallback mOnSelectedCallback;

    @Override
    protected int getLayoutId() {
        return _AlbumUtils.getResLayoutId(this, "album_activity_image_selector_layout");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _AlbumUtils.topbarApi35(this,"_album_title_layout");

        initIntent();

        if (mUseLandscape) {
            grid_NumColumns = 6;
        } else {
            grid_NumColumns = 3;
        }

        mTitleNameLayout = findViewById(_AlbumUtils.getResViewId(this, "_album_name_layout"));
        mTvFolderName = findViewById(_AlbumUtils.getResViewId(this, "_album_tv_title"));
        mIvArrow = findViewById(_AlbumUtils.getResViewId(this, "_album_iv_arrow"));
        mTvNext = findViewById(_AlbumUtils.getResViewId(this, "_album_tv_next"));
        mFolderLayout = findViewById(_AlbumUtils.getResViewId(this, "_album_folder_layout"));
        mFolderLayout.setVisibility(View.INVISIBLE);
        mFolderContentLayout = findViewById(_AlbumUtils.getResViewId(this, "_album_folder_content"));
        AdapterView folderListView = findViewById(_AlbumUtils.getResViewId(this, "_album_folder_list_view"));
        AdapterView folderLandscapeListView = findViewById(_AlbumUtils.getResViewId(this, "_album_landscape_folder_list_view"));
        mGridView = findViewById(_AlbumUtils.getResViewId(this, "image_grid_view"));
        mGridView.setNumColumns(grid_NumColumns);
        // 修改默认点击效果
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            folderListView.setVisibility(View.VISIBLE);
            folderLandscapeListView.setVisibility(View.GONE);
            mFolderListView = folderListView;

            mFolderContentLayoutHeight = _AlbumUtils.dp2px(420);
            mFolderContentLayout.getLayoutParams().height = mFolderContentLayoutHeight;
            mFolderListView.getLayoutParams().height = mFolderContentLayoutHeight;
            // 文件夹
            mFolderAdapter = new FolderAdapter(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ((ListView)mFolderListView).setSelector(new ColorDrawable(Color.TRANSPARENT));
        } else {
            folderListView.setVisibility(View.GONE);
            folderLandscapeListView.setVisibility(View.VISIBLE);
            mFolderListView = folderLandscapeListView;

            mFolderContentLayoutHeight = _AlbumUtils.dp2px(180);
            mFolderContentLayout.getLayoutParams().height = mFolderContentLayoutHeight;
            folderLandscapeListView.getLayoutParams().height = mFolderContentLayoutHeight;
            // 文件夹
            mFolderAdapter = new FolderAdapter(-1);
        }

        mFolderListView.setAdapter(mFolderAdapter);
        mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateFolder(mMediaFolderList.get(position));
                hideFolderLayout();
            }
        });

        updateTvNext();
        initClick();
        loadData();
    }

    private void initIntent() {
        Intent intent = getIntent();
        int index = intent.getIntExtra(AlbumConstant.key_index, 0);
        if (index > 0) {
            mOnSelectedCallback = (OnSelectedCallback) CoreAlbumManager.getInstance().getAlbumCallback(index);
        }
        // 媒体类型
        mediaType = mMediaOptions.mediaType;
        // 最大选择数
        maxSelectedCount = mMediaOptions.maxImageCount;
        hasTakeCamera = mMediaOptions.isShowCamera;
    }

    private void initClick() {
        mFolderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDoubleClick())return;
                hideFolderLayout();
            }
        });
        mTitleNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFolderLayout.getVisibility() == View.VISIBLE) {
                    // 关闭文件夹列表
                    hideFolderLayout();
                } else {
                    // 打开文件夹列表
                    showFolderLayout();
                }
            }
        });
        GridView gridView = findViewById(_AlbumUtils.getResViewId(this, "image_grid_view"));
        // 设置GridView--Item的点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageSelectorActivity.this.onItemClick(mImageAdapter.getRealPosition(position));
            }
        });
        // 下一步
        mTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDoubleClick()) return;
                MediaInfo mediaInfo = mSelectedImageList.get(0);
                if (mediaInfo.mediaType == MediaType.IMAGE.mediaType) {

                    if (mMediaOptions.maxImageCount == 1 && mMediaOptions.isOpenCropDirectly) {
                        if (mediaInfo.isGif) {
                            // 如果是Gif，直接回调
                            invokeCallback(mOnSelectedCallback, mSelectedImageList);
                        } else {
                            // 直接打开裁剪界面
                            com.skysoul.album.ui.ImageCropActivity.launchActivityForResult(
                                    ImageSelectorActivity.this,
                                    mediaInfo,
                                    mMediaOptions,
                                    mUseLandscape);
                        }
                    } else {
                        // 选择的是图片，则跳转到浏览界面
                        com.skysoul.album.ui.ImageBrowserActivity.launchActivityForResult(
                                ImageSelectorActivity.this,
                                mSelectedImageList,
                                mMediaOptions,
                                mUseLandscape);
                    }
                } else {
                    // 选择的是视频
//                    if (CoreAlbumManager.getInstance().isLicenceValid()) {
//                        // 如果配置了UGC，则跳转到视频剪辑界面
//                        com.skysoul.album.ui.VideoEditActivity.launchActivityForResult(ImageSelectorActivity.this, mediaInfo, mMediaOptions, mUseLandscape);
//                    } else {
//                        // 否则跳转到封面选择界面
//                        com.skysoul.album.ui.VideoCoverActivity.launchActivityForResult(ImageSelectorActivity.this, mediaInfo, mMediaOptions, mUseLandscape);
//                    }
                }
            }
        });

        // 取消点击
        findViewById(_AlbumUtils.getResViewId(this, "_album_tv_cancel")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadData() {
        // 加载媒体数据
        MediaLoadManager.loadAllMedia(this, mediaType, mMediaOptions.isShowGif,
                mMediaOptions.videoLimitDuration * 1000, mMediaOptions.videoLimitSize, null, new MediaLoadManager.LoadCallback() {
            @Override
            public void onLoadFinish(final ArrayList<MediaFolder> mediaFolders) {
                if (ImageSelectorActivity.this.isFinishing()) return;
                if (mediaFolders != null && mediaFolders.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMediaFolderList.addAll(mediaFolders);
                            updateFolder(mMediaFolderList.get(0));
                        }
                    });
                }
            }
        });
    }

    // 更新所选文件夹
    private void updateFolder(MediaFolder mediaFolder) {
        mTvFolderName.setText(mediaFolder.name);
        mMediaImageList.clear();
        mMediaImageList.addAll(mediaFolder.images);

        if (mGridView.getAdapter() == null) {
            mGridView.setAdapter(mImageAdapter);
        } else {
            mImageAdapter.notifyDataSetChanged();
        }
        mFolderAdapter.notifyDataSetChanged();
    }

    // 更新下一步文案
    private void updateTvNext() {
        if (mSelectedImageList.size() > 0) {
            mTvNext.setEnabled(true);
            mTvNext.setText("下一步("+ mSelectedImageList.size()+")");
        } else {
            mTvNext.setEnabled(false);
            mTvNext.setText("下一步");
        }
    }


//    private void checkPermissions(String[] permissions,int start, Runnable runnable){
//        if(permissions.length>start){
//            checkPermission(permissions[start], new Runnable() {
//                @Override
//                public void run() {
//                    int s = start+1;
//                    if(permissions.length>s){
//                        checkPermissions(permissions,s,runnable);
//                    }else{
//                        runnable.run();
//                    }
//                }
//            });
//        }
//    }
//    private void checkPermission(String permissions, Runnable runnable){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            boolean has = checkSelfPermission(permissions) == PackageManager.PERMISSION_GRANTED;
//
//            if(has){
//                runnable.run();
//            }else{
//                OnePermissionUtil.requestPermission(this, permissions, new OnePermissionListener() {
//                    @Override
//                    public void onPermissionResult(String s, boolean b) {
//                        if(b){
//                            runnable.run();
//                        }else{
//
//                        }
//                    }
//                });
//            }
//
//
//        }else{
//            runnable.run();
//        }
//
//
//    }

    // GridView_Item点击
    private void onItemClick(int position) {
        if (isDoubleClick())return;
        if (position < 0) {
            // 打开相机

//            String[] permissions = new String[]{Manifest.permission.CAMERA};
//            if (mediaType == MediaType.IMAGE || mMediaOptions.isUseSystemCamera) {
//
//            }else{
//                permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
//            }
//            if(!ImageSelectorActivity.this.isFinishing()){
//                launchCamera();
//            }
//            checkPermissions(permissions, 0, new Runnable() {
//                @Override
//                public void run() {
//                    if(!ImageSelectorActivity.this.isFinishing()){
//                        launchCamera();
//                    }
//                }
//            });
        } else {
            MediaInfo mediaInfo = mMediaImageList.get(position);
            if (mediaInfo.isImage()) {
                com.skysoul.album.ui.ImagePreViewActivity.launchActivityForResult(this, mMediaImageList, mSelectedImageList, mediaInfo, maxSelectedCount, mUseLandscape);
            } else {
                selectMedia(mediaInfo);
            }
        }
    }

    private Uri systemCameraFileUri;
//    private void launchCamera() {
//        systemCameraFileUri = null;
//        if (mMediaOptions.isUseSystemCamera) {
//            // 图片 且 使用系统相机
//            systemCameraFileUri = _AlbumUtils.createImageUri(ImageSelectorActivity.this);
//            Intent systemCameraIntent = _AlbumUtils.getOpenCamera(systemCameraFileUri);
//            startActivityForResult(systemCameraIntent, AlbumConstant.SYSTEM_CAMERA_REQUEST_CODE);
//        } else {
//            com.skysoul.album.ui.MediaTakeActivity.launch(ImageSelectorActivity.this, mediaType, mMediaOptions.videoRecodeLimitDuration, mUseLandscape);
//        }
//    }

    private ValueAnimator mFolderValueAnimator;
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private final long duration = 300L;
    private void  showFolderLayout() {
        if (mFolderValueAnimator != null && mFolderValueAnimator.isRunning()) {
            return;
        }
        mIvArrow.animate().rotation(180f).setDuration(duration).start();
        mFolderLayout.setVisibility(View.VISIBLE);
        mFolderValueAnimator = ValueAnimator.ofInt(0, mFolderContentLayoutHeight);
        mFolderValueAnimator.setDuration(duration);
        mFolderValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFolderContentLayout.getLayoutParams().height = (int) animation.getAnimatedValue();
                mFolderContentLayout.requestLayout();
                mFolderLayout.setBackgroundColor(
                        (Integer) argbEvaluator.evaluate(animation.getAnimatedFraction(),
                                Color.TRANSPARENT,
                                Color.parseColor("#66000000"))
                );
            }
        });
        mFolderValueAnimator.start();
    }

    private void hideFolderLayout() {
        if (mFolderValueAnimator != null && mFolderValueAnimator.isRunning()) {
            return;
        }
        mIvArrow.animate().rotation(0f).setDuration(duration).start();
        mFolderValueAnimator = ValueAnimator.ofInt(mFolderContentLayout.getHeight(), 0);
        mFolderValueAnimator.setDuration(duration);
        mFolderValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFolderContentLayout.getLayoutParams().height = (int) animation.getAnimatedValue();
                mFolderContentLayout.requestLayout();
                mFolderLayout.setBackgroundColor(
                        (Integer) argbEvaluator.evaluate(animation.getAnimatedFraction(),
                                Color.parseColor("#66000000"),
                                Color.TRANSPARENT)
                );
            }
        });
        mFolderValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFolderLayout.setVisibility(View.INVISIBLE);
            }
        });
        mFolderValueAnimator.start();
    }

    // 图片GridView适配器
    private class ImageAdapter extends BaseAdapter {
        ImageLoaderInterface imageLoader = CoreAlbumManager.getInstance().getImageLoader();


        @Override
        public int getCount() {
            return hasTakeCamera ? mMediaImageList.size() + 1 : mMediaImageList.size();
        }

        public int getRealPosition(int position) {
            if (hasTakeCamera) {
                return position - 1;
            } else {
                return position;
            }
        }

        @Override
        public Object getItem(int position) {
            position = getRealPosition(position);
            if (position < 0) {
                return -1;
            } else {
                return mMediaImageList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageHolder imageHolder;
            if (convertView == null) {
                imageHolder = new ImageHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(_AlbumUtils.getResLayoutId(ImageSelectorActivity.this, "album_image_selector_item"), parent, false);
                imageHolder.shade = convertView.findViewById(_AlbumUtils.getResViewId(ImageSelectorActivity.this, "_album_shade"));
                imageHolder.takePhoto = convertView.findViewById(_AlbumUtils.getResViewId(ImageSelectorActivity.this, "_album_takePhoto"));
                imageHolder.tvVideoDuration = convertView.findViewById(_AlbumUtils.getResViewId(ImageSelectorActivity.this, "_album_tv_video_duration"));
                imageHolder.selectLayout = convertView.findViewById(_AlbumUtils.getResViewId(ImageSelectorActivity.this, "_album_select_layout"));
                imageHolder.tvIndex = convertView.findViewById(_AlbumUtils.getResViewId(ImageSelectorActivity.this, "_album_tv_index"));
                imageHolder.img = convertView.findViewById(_AlbumUtils.getResViewId(ImageSelectorActivity.this, "iv_img"));
                imageHolder.img.getLayoutParams().height = (parent.getWidth() - (convertView.getPaddingStart() + convertView.getPaddingEnd()) * grid_NumColumns) / grid_NumColumns;
                convertView.setTag(imageHolder);
            } else {
                imageHolder = (ImageHolder) convertView.getTag();
            }

            position = getRealPosition(position);
            if (position < 0) {
                // 相机
                imageHolder.takePhoto.setVisibility(View.VISIBLE);
                imageHolder.selectLayout.setVisibility(View.GONE);
                imageHolder.shade.setVisibility(View.GONE);
                imageHolder.tvVideoDuration.setVisibility(View.GONE);
                if (mMediaOptions.isUseSystemCamera) {
                    if (mediaType == MediaType.VIDEO) {
                        imageHolder.shade.setVisibility(View.VISIBLE);
                    } else {
                        imageHolder.shade.setVisibility(View.GONE);
                    }
                }
            } else {
                imageHolder.selectLayout.setVisibility(View.VISIBLE);
                imageHolder.takePhoto.setVisibility(View.GONE);

                final MediaInfo mediaInfo = mMediaImageList.get(position);

                if (mediaInfo.isVideo()) {
                    imageLoader.loadImage(imageHolder.img, mediaInfo.path);
                    imageHolder.tvIndex.setBackgroundResource(_AlbumUtils.getResDrawableId(ImageSelectorActivity.this, "album_shape_video_selected_bg"));
                } else {
                    imageLoader.loadImage(imageHolder.img, mediaInfo.getShowPath());
                    imageHolder.tvIndex.setBackgroundResource(_AlbumUtils.getResDrawableId(ImageSelectorActivity.this, "album_shape_image_selected_bg"));
                }

                imageHolder.selectLayout.setSelected(mediaInfo.isChecked);
                if (mediaInfo.isChecked) {
                    imageHolder.tvIndex.setText(_AlbumUtils.getLocationForList(mediaInfo, mSelectedImageList));
                } else {
                    imageHolder.tvIndex.setText("");
                }
                imageHolder.selectLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectMedia(mediaInfo);
                    }
                });

                imageHolder.tvVideoDuration.setVisibility(mediaInfo.isVideo()?View.VISIBLE:View.GONE);
                if (mediaInfo.isVideo()) {
                    imageHolder.tvVideoDuration.setVisibility(View.VISIBLE);
                    imageHolder.tvVideoDuration.setText(_AlbumUtils.getVideoDurationStr(mediaInfo.duration));
                } else {
                    imageHolder.tvVideoDuration.setVisibility(View.GONE);
                }

                if (mediaType == MediaType.VIDEO) {
                    imageHolder.shade.setVisibility(mediaInfo.isImage()?View.VISIBLE:View.GONE);
                } else if (mediaType == MediaType.IMAGE) {
                    imageHolder.shade.setVisibility(mediaInfo.isVideo()?View.VISIBLE:View.GONE);
                } else {
                    imageHolder.shade.setVisibility(View.GONE);
                }
            }
            return convertView;
        }
    }

    private static class ImageHolder {
        public ImageView img;
        public View selectLayout;
        public TextView tvIndex;
        public View takePhoto;
        public TextView tvVideoDuration;
        public View shade;
    }

    private void selectMedia(final MediaInfo mediaInfo) {

        if (mediaInfo.isVideo() && mediaInfo.duration < 2000) {
            showToast("不能选择时长低于2秒的视频");
            return;
        }

        boolean curIsChecked = mediaInfo.isChecked;
        if (curIsChecked) {
            mSelectedImageList.remove(mediaInfo);
            mediaInfo.isChecked = false;
        } else {
            if (mediaType == MediaType.VIDEO) {
                if (mSelectedImageList.size() > 0) {
                    mSelectedImageList.get(0).isChecked = false;
                    mSelectedImageList.clear();
                }
            } else {
                if (mSelectedImageList.size() >= maxSelectedCount) {
                    showToast("最多只能选择"+ maxSelectedCount +"张照片");
                    return;
                }
            }

            mediaInfo.isChecked = true;
            mSelectedImageList.add(mediaInfo);
        }

        refreshUI();
    }

    private void refreshUI() {
        if (mSelectedImageList.isEmpty()) {
            mediaType = mMediaOptions.mediaType;
        } else {
            if (mSelectedImageList.get(0).isVideo()) {
                mediaType = MediaType.VIDEO;
            } else if (mSelectedImageList.get(0).isImage()) {
                mediaType = MediaType.IMAGE;
            }
        }

        mImageAdapter.notifyDataSetChanged();
        updateTvNext();
    }

    // 文件夹ListView适配器
    private class FolderAdapter extends BaseAdapter {

        private final int mType;
        public FolderAdapter(int type) {
            mType = type;
        }

        ImageLoaderInterface imageLoader = CoreAlbumManager.getInstance().getImageLoader();
        @Override
        public int getCount() {
            return mMediaFolderList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMediaFolderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final FolderHolder folderHolder;
            if (convertView == null) {
                folderHolder = new FolderHolder();
                if (mType == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(_AlbumUtils.getResLayoutId(ImageSelectorActivity.this, "album_folder_item"), parent, false);
                } else {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(_AlbumUtils.getResLayoutId(ImageSelectorActivity.this, "album_landscape_folder_item"), parent, false);
                }
                folderHolder.folderImage = convertView.findViewById(_AlbumUtils.getResViewId(ImageSelectorActivity.this, "_album_iv_folder_img"));
                folderHolder.folderName = convertView.findViewById(_AlbumUtils.getResViewId(ImageSelectorActivity.this, "_album_tv_folder_name"));
                folderHolder.folderImageCount = convertView.findViewById(_AlbumUtils.getResViewId(ImageSelectorActivity.this, "_album_tv_folder_image_count"));
                folderHolder.folderTvChecked = convertView.findViewById(_AlbumUtils.getResViewId(ImageSelectorActivity.this, "_album_iv_folder_checked"));
                convertView.setTag(folderHolder);
            } else {
                folderHolder = (FolderHolder) convertView.getTag();
            }
            final MediaFolder mediaFolder = mMediaFolderList.get(position);

            if (imageLoader != null) {
                imageLoader.loadImage(folderHolder.folderImage, mediaFolder.getFirstImagePath());
            }

            if (mType == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                folderHolder.folderName.setText(mediaFolder.name+"("+mediaFolder.getImageNum()+")");
            } else {
                folderHolder.folderName.setText(mediaFolder.name);
                if (folderHolder.folderImageCount != null) {
                    folderHolder.folderImageCount.setText(String.valueOf(mediaFolder.getImageNum()));
                }
            }

            return convertView;
        }
    }

    private static class FolderHolder{
        public ImageView folderImage;
        public TextView folderName;
        public TextView folderImageCount;
        public ImageView folderTvChecked;
    }

    private void showToast(String msg) {
        Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 等待其他界面结果
     * 1. 点击下一步：
     *      视频：打开视频编辑界面 VideoEditActivity
     *           如果没有UGC-Licence 则打开封面选择界面 VideoCoverActivity
     *      图片：图片浏览界面 ImageBrowserActivity
     *           如果isOpenCropDirectly=true，则打开 ImageCropActivity
     * 2. 点击相机：
     *      打开 MediaTakeActivity，返回一下结果：
     *          拍照：打开 ImageBrowserActivity
     *          视频：打开 VideoEditActivity
     * 3. 点击图片：
     *      打开 ImagePreViewActivity 图片预览界面
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AlbumConstant.PREVIEW_REQUEST_CODE) {
                /**
                 * {@link com.skysoul.album.ui.ImagePreViewActivity} 图片预览结果
                 */
                if (data != null) {
                    // 选中的媒体
                    MediaInfo mediaInfo = data.getParcelableExtra(AlbumConstant.key_media_info);
                    if (mediaInfo != null) {
                        int selectedPosition = mMediaImageList.indexOf(mediaInfo);
                        if (selectedPosition > -1) {
                            MediaInfo selectedMediaInfo = mMediaImageList.get(selectedPosition);
                            selectedMediaInfo.isChecked = true;
                            mSelectedImageList.add(selectedMediaInfo);
                        }
                    }
                }
                // 移除取消选中的
                for (int i = mSelectedImageList.size() - 1; i>=0; i--) {
                    if (!mSelectedImageList.get(i).isChecked) {
                        mSelectedImageList.remove(i);
                    }
                }

                refreshUI();
            } else if (requestCode == AlbumConstant.TAKE_REQUEST_CODE) {
                if (data != null) {
                    MediaInfo mediaInfo = data.getParcelableExtra(AlbumConstant.key_media_info);
                    handleCameraPicture(mediaInfo);
                }
            } else if (requestCode == AlbumConstant.SYSTEM_CAMERA_REQUEST_CODE) {
                // 系统相机
                String imgPath = "";
                if (data != null && !TextUtils.isEmpty(data.getDataString())) {
                    imgPath = _UriUtils.getPath(this, Uri.parse(data.getDataString()));
                    systemCameraFileUri = Uri.parse(data.getDataString());
                }
                if(TextUtils.isEmpty(imgPath)) {
                    imgPath = _UriUtils.getPath(this, systemCameraFileUri);
                }

                if(TextUtils.isEmpty(imgPath)) {
                    showToast("拍照失败");
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imgPath, options);
                    MediaInfo mediaInfo = new MediaInfo();
                    mediaInfo.path = imgPath;
                    mediaInfo.width = options.outWidth;
                    mediaInfo.height = options.outHeight;
                    handleCameraPicture(mediaInfo);
                }
            } else if (requestCode == AlbumConstant.BROWSER_REQUEST_CODE) {
                /**
                 * {@link com.skysoul.album.ui.ImageBrowserActivity} 图片浏览界面点击完成
                 */
                boolean isDone = false;
                if (data != null) {
                    isDone = data.getBooleanExtra(AlbumConstant.key_album_done, false);
                }
                if (isDone) {
                    // 点击完成
                    MediaInfo mediaInfo = data.getParcelableExtra(AlbumConstant.key_media_info);
                    if (mediaInfo != null) {
                        // 不为空说明该图片信息来源于拍照,视频，直接把该信息回调结果
                        ArrayList<MediaInfo> mediaList = new ArrayList<>();
                        mediaList.add(mediaInfo);
                        compressAndCallback(mediaList, mOnSelectedCallback);
                    } else {
                        // 把mSelectedImageList中的信息回调结果
                        compressAndCallback(mSelectedImageList, mOnSelectedCallback);
                    }
                } else {
                    // 取消，直接刷新数据
                    mImageAdapter.notifyDataSetChanged();
                }
            } else if (requestCode == AlbumConstant.VIDEO_EDIT_REQUEST_CODE
                || requestCode == AlbumConstant.VIDEO_COVER_REQUEST_CODE) {
                if (data != null) {
                    MediaInfo mediaInfo = data.getParcelableExtra(AlbumConstant.key_media_info);
                    if (mediaInfo != null) {
                        // 视频直接回调
                        ArrayList<MediaInfo> list = new ArrayList<>();
                        list.add(mediaInfo);
                        invokeCallback(mOnSelectedCallback, list);
                    }
                }
            } else if (requestCode == AlbumConstant.CROP_REQUEST_CODE) {
                /**
                 * {@link com.skysoul.album.ui.ImageCropActivity} 图片裁剪
                 */
                if (data != null) {
                    // 裁剪结果
                    MediaInfo cropMediaInfo = data.getParcelableExtra(AlbumConstant.key_media_info);
                    // 直接回调
                    if (cropMediaInfo != null) {
                        ArrayList<MediaInfo> mediaList = new ArrayList<>();
                        mediaList.add(cropMediaInfo);
                        compressAndCallback(mediaList, mOnSelectedCallback);
                    }
                }
            }
        }
    }

    // 处理拍照的图片
    private void handleCameraPicture(MediaInfo mediaInfo) {
        if (mediaInfo != null) {
            if (mediaInfo.isImage()) {
                if (mMediaOptions.maxImageCount == 1 && mMediaOptions.isOpenCropDirectly) {
                    // 直接打开裁剪
                    ImageCropActivity.launchActivityForResult(
                            ImageSelectorActivity.this,
                            mediaInfo,
                            mMediaOptions,
                            mUseLandscape);
                } else {
                    // 跳转到图片浏览
                    ImageBrowserActivity.launchActivityForResult(this, mediaInfo, mMediaOptions, mUseLandscape);
                }
            } else if (mediaInfo.isVideo()) {
//                VideoEditActivity.launchActivityForResult(this, mediaInfo, mMediaOptions, mUseLandscape);
            }
        }
    }

    @Override
    public void onBackPressed() {
        invokeCallbackFail(CoreAlbumManager.code_cancel);
        super.onBackPressed();
    }

    private void invokeCallbackFail(int code) {
        if (mOnSelectedCallback != null) {
            mOnSelectedCallback.onFail(code, CoreAlbumManager.getFailMsg(code));
        }
    }
}
