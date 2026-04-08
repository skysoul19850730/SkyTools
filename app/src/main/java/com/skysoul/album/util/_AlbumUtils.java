package com.skysoul.album.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import android.view.View;
import android.view.ViewGroup;
import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.view.ucrop.media.ExifInterface;
import com.skysoul.album.view.ucrop.model.ExifInfo;
import com.skysoul.album.view.ucrop.util.BitmapLoadUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class _AlbumUtils {

    public static void topbarApi35(Activity activity, String topbarVId) {
        View topbar = activity.findViewById(_AlbumUtils.getResViewId(activity, topbarVId));
        topbarApi35(activity, topbar);
    }
    public static void topbarApi35(Context activity, View topbar) {
        if(isApi35(activity)){
            topbar.setPadding(topbar.getPaddingLeft(),topbar.getPaddingTop()+getStatusBarHeight(activity)
            ,topbar.getPaddingRight(),topbar.getPaddingBottom());
        }
    }
    public static void topbarApi35Margin(Context activity, View topbar) {
        if(isApi35(activity)){
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) topbar.getLayoutParams();
            params.topMargin += getStatusBarHeight(activity);
            topbar.setLayoutParams(params);
        }
    }

    private static boolean isApi35(Context context){
        return Build.VERSION.SDK_INT>=Build.VERSION_CODES.VANILLA_ICE_CREAM
                && getTargetSdkVersion(context)>=Build.VERSION_CODES.VANILLA_ICE_CREAM;
    }
    private static int getTargetSdkVersion(Context context) {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .applicationInfo
                    .targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        } catch (Exception e1) {
            e1.printStackTrace();

        }
        return statusBarHeight;
    }

    public static int dp2px(float dpValue) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, Resources.getSystem().getDisplayMetrics());
        return (int) px;
    }

    // 拍照的图片保存的路径 Android/Data/包名/files/_album
    public static String getImagePath(Context context) {
        File filesDir = context.getExternalFilesDir("_album");
        if(filesDir==null){
            filesDir = context.getFilesDir();
        }
        if (!filesDir.exists()) {
            filesDir.mkdirs();
        }
        return filesDir.getAbsolutePath() + "/";
    }

    public static String getVideoPath(Context context) {
        File filesDir = context.getExternalFilesDir("_album/video");
        if (!filesDir.exists()) {
            filesDir.mkdirs();
        }
        if (!filesDir.exists()) {
            filesDir.mkdirs();
        }
        return filesDir.getAbsolutePath() + "/";
    }

    public static boolean saveBitmap(Bitmap bmp, String desFilePath, int quality) {
        try {
            if (bmp.isRecycled() || TextUtils.isEmpty(desFilePath)) {
                return false;
            }
            File file = new File(desFilePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, outStream);
            outStream.flush();
            outStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取图片在已经选择列表的位置
     */
    public static String getLocationForList(MediaInfo item, List<MediaInfo> list) {
        if (item.isVideo()) {
            return "";
        }
        int index = list.indexOf(item);
        if (index < 0) {
            return "";
        } else {
            return String.valueOf(index + 1);
        }
    }

    public static String getVideoDurationStr(long duration) {
        duration = duration / 1000;
        if (duration < 10) {
            return "00:0"+duration;
        } else if (duration < 60) {
            return "00:"+duration;
        }
        long minute = duration / 60;
        long second = duration % 60;
        StringBuilder sb = new StringBuilder();
        if (minute < 10) {
            sb.append("0").append(minute);
        } else {
            sb.append(minute);
        }
        sb.append(":");
        if (second < 10) {
            sb.append("0").append(second);
        } else {
            sb.append(second);
        }
        return sb.toString();
    }

    /**
     * 删除临时文件
     * 删除裁剪图，封面，压缩的图片文件
     */
    public static void deleteTempFile(Context context, List<MediaInfo> list) {
        for (MediaInfo mediaInfo: list) {
            if (!TextUtils.isEmpty(mediaInfo.cropPath)) {
                delFile(mediaInfo.coverPath);
            }
            if (!TextUtils.isEmpty(mediaInfo.compressPath)) {
                delFile(mediaInfo.compressPath);
            }
            if (!TextUtils.isEmpty(mediaInfo.cropPath)) {
                delFile(mediaInfo.cropPath);
            }
        }
    }

    public static void delFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static String getAppNameFromManifest(Activity activity) {
        ActivityInfo activityInfo;
        try {
            activityInfo = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 0);
            return activityInfo.loadLabel(activity.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 压缩，先尺寸压缩，再质量压缩
     */
    public static void compress(Context context, MediaInfo mediaInfo, long imageLimitSize) {
        if (!TextUtils.isEmpty(mediaInfo.compressPath)) {
            return;
        }
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        FileOutputStream fos = null;
        String filePath = mediaInfo.getShowPath();
        int width = mediaInfo.width;
        int height = mediaInfo.height;
        long fileSize = mediaInfo.getFileSize();

        try {
            bis = new BufferedInputStream(new FileInputStream(filePath), 64 * 1024);
            baos = new ByteArrayOutputStream();

            BitmapFactory.Options options = new BitmapFactory.Options();
            if (width <= 0 || height <= 0) {
                options.inJustDecodeBounds = true;
                options.inSampleSize = 1;
                BitmapFactory.decodeStream(bis, null, options);
                width = options.outWidth;
                height = options.outHeight;
                // 重新创建输入流
                close(bis);
                bis = new BufferedInputStream(new FileInputStream(filePath), 64 * 1024);
            }
            Bitmap tagBitmap = null;
            options.inJustDecodeBounds = false;
            int inSampleSize = computeSize(width, height);

            // 需要进行尺寸压缩 or 质量压缩
            if (inSampleSize > 1 || fileSize > imageLimitSize) {
                options.inSampleSize = inSampleSize;
                tagBitmap = BitmapFactory.decodeStream(bis, null, options);
            }

            if (tagBitmap != null) {

                // 判断图片是否需要旋转
                int exifOrientation = getImageOrientation(filePath);
                int exifDegrees = BitmapLoadUtils.exifToDegrees(exifOrientation);
                if (exifDegrees != 0) {
                    tagBitmap = rotateBitmap(tagBitmap, exifDegrees);
                }

                int quality = 100;
                // 第一次先以100进行压缩，获取图片大小
                tagBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                int baosLength = baos.toByteArray().length;
                // 循环进行质量压缩，每次递减10
                while (baosLength > imageLimitSize) {
                    baos.reset();
                    quality -= 10;
                    tagBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    baosLength = baos.toByteArray().length;
                    if (quality == 0) {
                        break;
                    }
                }

                int newWidth = tagBitmap.getWidth();
                int newHeight = tagBitmap.getHeight();
                tagBitmap.recycle();
                String compressPath = getImagePath(context) + "cps_" + System.currentTimeMillis() + ".jpg";
                fos = new FileOutputStream(compressPath);
                fos.write(baos.toByteArray());
                fos.flush();

                mediaInfo.compressPath = compressPath;
                mediaInfo.width = newWidth;
                mediaInfo.height = newHeight;
                mediaInfo.fileSize = baosLength;
            }else{
                mediaInfo.compressPath = mediaInfo.cropPath;
            }
        } catch (Throwable e) {
            // OOM or 其他异常
            e.printStackTrace();
        } finally {
            close(bis);
            close(baos);
            close(fos);
        }
    }

    private static int getImageOrientation(String sourcePath) {
        try {
            ExifInterface exifInfo = new ExifInterface(sourcePath);
            return exifInfo.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
            );
        } catch (Exception ignored) {
            return ExifInterface.ORIENTATION_UNDEFINED;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        try {
            Matrix matrix = new Matrix();
            matrix.preRotate(degrees);
            Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            bitmap = converted;
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return bitmap;
    }


    // 计算缩放大小，参考第三方luban压缩库 https://github.com/Curzibn/Luban
    private static int computeSize(int srcWidth, int srcHeight) {
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);

        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getResViewId(Context context, String name) {
        return getResId(context, name, "id");
    }

    public static int getResLayoutId(Context context, String name) {
        return getResId(context, name, "layout");
    }

    public static int getResDrawableId(Context context, String name) {
        return getResId(context, name, "drawable");
    }

    public static int getResStringId(Context context, String name) {
        return getResId(context, name, "string");
    }

    public static int getResId(Context context, String name, String type) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }

    public static Intent getOpenCamera(Uri uri){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        return takePictureIntent;
    }

    public static Uri createImageUri(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String status = Environment.getExternalStorageState();
            ContentValues contentValues = new ContentValues();
            String imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg";
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                return context.getContentResolver()
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            } else {
                return context.getContentResolver()
                        .insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues);
            }
        } else {
            File photoFile = new File(getImagePath(context) + "JPEG_" + System.currentTimeMillis() + ".jpg");
            return Uri.fromFile(photoFile);
        }
    }
}
