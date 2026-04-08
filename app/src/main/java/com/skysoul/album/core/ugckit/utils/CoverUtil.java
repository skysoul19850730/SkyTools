package com.skysoul.album.core.ugckit.utils;


import static com.skysoul.album.core.ugckit.utils.VideoPathUtil.DEFAULT_MEDIA_PACK_FOLDER;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;

//import com.tencent.ugc.TXVideoInfoReader;
import com.skysoul.album.core.CoreAlbumManager;
import com.skysoul.album.core.ugckit.UGCKit;
import com.skysoul.album.view.ucrop.util.InnerAsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 封面工具类
 */
public class CoverUtil {
    private String TAG = "CoverUtil";

    private static CoverUtil sInstance = new CoverUtil();
    private String mVideoPath;

    private CoverUtil() {
    }

    public static CoverUtil getInstance() {
        return sInstance;
    }


    public void setInputPath(String path) {
        mVideoPath = path;
    }

    @SuppressLint("StaticFieldLeak")
    public void createThumbFile(final ICoverListener listener) {
        createThumbFile(0, listener);
    }

    /**
     * 创建缩略图，必须先调用{@link com.skysoul.album.core.ugckit.utils.CoverUtil#setInputPath(String)} 设置视频路径
     */
    @SuppressLint("StaticFieldLeak")
    public void createThumbFile(final long time, final ICoverListener listener) {
//        InnerAsyncTask<Void, String, String> task = new InnerAsyncTask<Void, String, String>() {
//            @Override
//            protected String doInBackground(Void... voids) {
//                File outputVideo = new File(mVideoPath);
//                if (!outputVideo.exists()) {
//                    return null;
//                }
//                Bitmap bitmap = TXVideoInfoReader.getInstance(UGCKit.getAppContext()).getSampleImage(time, mVideoPath);
//                if (bitmap == null) {
//                    Log.e(TAG, "TXVideoInfoReader getSampleImage bitmap is null");
//                    return null;
//                }
//                File sdcardDir = UGCKit.getAppContext().getExternalFilesDir(null);
//                if (sdcardDir == null) {
//                    Log.e(TAG, "sdcardDir is null");
//                    return null;
//                }
//                String folder = sdcardDir + File.separator + DEFAULT_MEDIA_PACK_FOLDER;
//                File appDir = new File(folder);
//                if (!appDir.exists()) {
//                    appDir.mkdirs();
//                }
//                String fileName = "cover_" + System.currentTimeMillis() + ".jpg";
//                File file = new File(appDir, fileName);
//                if (file.exists()) {
//                    file.delete();
//                    try {
//                        file.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                try {
//                    FileOutputStream fos = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.flush();
//                    fos.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return file.getAbsolutePath();
//            }
//
//            @Override
//            protected void onPostExecute(String coverImagePath) {
//                if (listener != null) {
//                    listener.onCoverPath(coverImagePath);
//                }
//            }
//
//        };
//        task.executeOnExecutor(CoreAlbumManager.getInstance().getExecutor());
    }

    public interface ICoverListener {
        void onCoverPath(String coverPath);
    }
}
