package com.skysoul.album.core.ugckit.utils;

import android.text.TextUtils;
import android.util.Log;

import com.skysoul.album.core.ugckit.UGCKit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 视频路径生成器
 */
public class VideoPathUtil {
    private static final String TAG = "VideoPathUtil";
    public static final String DEFAULT_MEDIA_PACK_FOLDER = "txrtmp";      // UGC编辑器输出目录
    public static final String OUTPUT_DIR_NAME = "video";

    /**
     * 生成编辑后输出视频路径
     *
     * @return
     */
    public static String generateVideoPath() {
        File sdcardDir = UGCKit.getAppContext().getExternalFilesDir(null);
        if (sdcardDir == null) {
            Log.e(TAG, "generateVideoPath sdcardDir is null");
            return "";
        }
        String outputPath = sdcardDir + File.separator + DEFAULT_MEDIA_PACK_FOLDER;
        File outputFolder = new File(outputPath);

        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
        String current = String.valueOf(System.currentTimeMillis() / 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String time = sdf.format(new Date(Long.valueOf(current + "000")));
        String saveFileName = String.format("TXVideo_%s.mp4", time);
        return outputFolder + "/" + saveFileName;
    }

    public static String getCustomVideoOutputPath() {
        return getCustomVideoOutputPath(null);
    }

    public static String getCustomVideoOutputPath(String fileNamePrefix) {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        String time = sdf.format(new Date(currentTime));

        File sdcardDir = UGCKit.getAppContext().getExternalFilesDir(null);
        if (sdcardDir == null) {
            Log.e(TAG, "sdcardDir is null");
            return null;
        }

        String outputDir = sdcardDir + File.separator + OUTPUT_DIR_NAME;
        File outputFolder = new File(outputDir);
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }
        String tempOutputPath;
        if (TextUtils.isEmpty(fileNamePrefix)) {
            tempOutputPath = outputDir + File.separator + "wanmei_" + time + ".mp4";
        } else {
            tempOutputPath = outputDir + File.separator + "wanmei_" + fileNamePrefix + time + ".mp4";
        }
        return tempOutputPath;
    }
}
