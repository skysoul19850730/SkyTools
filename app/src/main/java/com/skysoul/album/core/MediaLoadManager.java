package com.skysoul.album.core;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.skysoul.album.bean.MediaFolder;
import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.bean.MediaType;
import com.skysoul.album.util._AlbumUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;


/**
 * 媒体数据加载
 */
public class MediaLoadManager {
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC";
    private static final String DURATION = "duration";
    private static final String NOT_GIF = "!='image/gif'";

    private static final long videoMaxS = 0;
    private static final long videoMinS = 0;

    // 媒体文件数据库字段
    private static final String[] PROJECTION = new String[]{
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,
            DURATION,
            MediaStore.MediaColumns.SIZE
    };

    // 图片
    private static final String SELECTION = (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0");

    private static final String SELECTION_NOT_GIF = (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0"
            + " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF);

    // 查询条件(音视频)
    private static String getSelectionArgsForSingleMediaCondition(long maxFileSize, String time_condition) {
        return (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0 AND " + MediaStore.MediaColumns.SIZE + "<" + maxFileSize
                + " AND " + time_condition);
    }

    // 全部模式下条件
    private static String getSelectionArgsForAllMediaCondition(long maxFileSize, String time_condition, boolean isGif) {
        return ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + (isGif? "":" AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)  + " AND " + MediaStore.MediaColumns.SIZE + ">0"
        + " OR "
                + (MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND " + time_condition)  + " AND " + MediaStore.MediaColumns.SIZE + ">0 AND " + MediaStore.MediaColumns.SIZE + "<" + maxFileSize + ")");
    }

    // 获取图片or视频
    private static final String[] SELECTION_ALL_ARGS = new String[]{
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
    };

    /**
     * 获取指定类型的文件
     */
    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        return new String[]{String.valueOf(mediaType)};
    }

    public static void loadAllMedia(final Context context, final MediaType mediaType, final boolean showGif,
                                    final int videoLimitDuration, final long videoLimitSize, final Set<String> selectList, final LoadCallback loadCallback) {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                String selection;
                String[] selectionArgs;
                if (mediaType == MediaType.IMAGE) {
                    // 获取图片
                    selection = showGif ? SELECTION : SELECTION_NOT_GIF;
                    selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
                } else if (mediaType == MediaType.VIDEO) {
                    // 获取视频
                    selection = getSelectionArgsForSingleMediaCondition(videoLimitSize, getDurationCondition(videoLimitDuration, 0));
                    selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
                }
//                else if (mediaType == MediaType.AUDIO) {
//                    // 获取音频
//                    selection = getSelectionArgsForSingleMediaCondition(getDurationCondition(0, AUDIO_DURATION));
//                    selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO);
//                }
                else {
                    // 获取图片 和 视频
                    selection = getSelectionArgsForAllMediaCondition(videoLimitSize, getDurationCondition(videoLimitDuration, 0), showGif);
                    selectionArgs = SELECTION_ALL_ARGS;
                }

                ArrayList<MediaFolder> imageFolders = new ArrayList<>();
                MediaFolder allImageFolder = new MediaFolder();
                ArrayList< MediaInfo > latelyImages = new ArrayList<>();

                Cursor cursor = context.getContentResolver().query(QUERY_URI, PROJECTION, selection,
                        selectionArgs, ORDER_BY, null);
                if (cursor != null) {
                    try {
                        int count = cursor.getCount();
                        if (count > 0) {
                            cursor.moveToFirst();
                            do {
                                String path = cursor.getString(cursor.getColumnIndexOrThrow(PROJECTION[1]));
                                String pictureType = cursor.getString(cursor.getColumnIndexOrThrow(PROJECTION[2]));
                                int w = cursor.getInt(cursor.getColumnIndexOrThrow(PROJECTION[3]));
                                int h = cursor.getInt(cursor.getColumnIndexOrThrow(PROJECTION[4]));
                                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(PROJECTION[5]));
                                long size = cursor.getLong(cursor.getColumnIndexOrThrow(PROJECTION[6]));
                                String name = getFileName(path);
                                if (new File(path).exists() && !name.contains("#") && !name.contains("@") && !name.contains("%") && !name.contains("{") && !name.contains("[") && !name.contains("$")) {
                                    MediaInfo image = new MediaInfo();
                                    image.path = path;
                                    image.duration = duration;
                                    image.mediaType = MediaType.ALL.isPictureType(pictureType).mediaType;
                                    image.isGif = "image/gif".equals(pictureType);
                                    image.width = w;
                                    image.height = h;
                                    image.fileSize = size;
                                    MediaFolder folder = getImageFolder(path, imageFolders);
                                    ArrayList<MediaInfo> images = folder.images;
                                    images.add(image);
                                    latelyImages.add(image);
                                    if (selectList != null) {
                                        image.isChecked = selectList.contains(path);
                                    }
                                }
                            } while (cursor.moveToNext());
                            if (latelyImages.size() > 0) {
                                sortFolder(imageFolders);
                                imageFolders.add(0, allImageFolder);
                                allImageFolder.name = "全部";
                                allImageFolder.images = latelyImages;
                            }
                            loadCallback.onLoadFinish(imageFolders);
                        } else {
                            loadCallback.onLoadFinish(imageFolders);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        cursor.close();
                    }
                } else {
                    loadCallback.onLoadFinish(imageFolders);
                }
            }
        };
        CoreAlbumManager.getInstance().execute(runnable);
    }


    private static String getFileName(String path) {
        int start = path.lastIndexOf("/");
        return start != -1 ? path.substring(start + 1) : "";
    }

    /**
     * 文件夹数量进行排序(降序)
     */
    private static void sortFolder(List<MediaFolder> imageFolders) {
        // 文件夹按图片数量排序
        Collections.sort(imageFolders, new Comparator<MediaFolder>() {
            @Override
            public int compare(MediaFolder o1, MediaFolder o2) {
                return Integer.compare(o2.images.size(), o1.images.size());
            }
        });
    }

    /**
     * 创建相应文件夹
     *
     * @param path
     * @param imageFolders
     * @return
     */
    private static MediaFolder getImageFolder(String path, List<MediaFolder> imageFolders) {
        File imageFile = new File(path);
        File folderFile = imageFile.getParentFile();
        for (int i = 0; i < imageFolders.size(); i++) {
            // 同一个文件夹下，返回自己，否则创建新文件夹
            if (imageFolders.get(i).name.equals(folderFile.getName())) {
                return imageFolders.get(i);
            }
        }
        MediaFolder newFolder = new MediaFolder();
        newFolder.name = folderFile.getName();
        newFolder.path = folderFile.getAbsolutePath();
        imageFolders.add(newFolder);
        return newFolder;
    }

    /**
     * 获取视频(最长或最小时间)
     *
     * @param exMaxLimit
     * @param exMinLimit
     * @return
     */
    private static String getDurationCondition(long exMaxLimit, long exMinLimit) {
        long maxS = videoMaxS == 0L ? Long.MAX_VALUE : videoMaxS;
        if (exMaxLimit != 0L) {
            maxS = Math.min(maxS, exMaxLimit);
        }
        return String.format(Locale.CHINA, "%d <%s duration and duration <= %d",
                Math.max(exMinLimit, videoMinS),
        (Math.max(exMinLimit, videoMinS) == 0L)? "" : "=",
                maxS);
    }

    public interface LoadCallback {
        void onLoadFinish(ArrayList<MediaFolder> mediaFolders);
    }

    public interface CompressCallback {
        void onStart();
        void onFinished(ArrayList<MediaInfo> list);
    }

    // 压缩处理
    public static void compress(final Context context, final ArrayList<MediaInfo> list, final long imageLimitSize, final CompressCallback compressCallback) {
        compressCallback.onStart();
        CoreAlbumManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                for (MediaInfo mediaInfo: list) {
                    if (mediaInfo.isImage() && !mediaInfo.isGif) {
                        _AlbumUtils.compress(context, mediaInfo, imageLimitSize);
                    } else if (mediaInfo.isVideo()) {
                        // 对视频封面进行压缩
                        String path = "";
                        if (!TextUtils.isEmpty(mediaInfo.cropPath)) {
                            path = mediaInfo.cropPath;
                        } else if (!TextUtils.isEmpty(mediaInfo.coverPath)) {
                            path = mediaInfo.coverPath;
                        }
                        if (!TextUtils.isEmpty(path)) {
                            _AlbumUtils.compress(context, mediaInfo, imageLimitSize);
                        }
                    }
                }
                compressCallback.onFinished(list);
            }
        });
    }
}
