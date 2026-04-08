package com.skysoul.album.core;

import android.content.Context;
import android.text.TextUtils;

import com.skysoul.album.ImageLoaderInterface;
import com.skysoul.album.core.AlbumCallbackInterface;
import com.skysoul.album.core.ugckit.UGCKit;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CoreAlbumManager {

    public static final int code_cancel = 1;
    public static final int code_media_error = 2;
    public static final int code_licence_invalid = 3;
    private static final HashMap<Integer, String> failedMessage = new HashMap<>();
    static {
        failedMessage.put(code_cancel, "用户取消");
        failedMessage.put(code_media_error, "媒体信息不对");
        failedMessage.put(code_licence_invalid, "licence无效");
    }
    public static String getFailMsg(int code) {
        return failedMessage.get(code);
    }


    private static class CoreAlbumManagerHolder {
        private static final CoreAlbumManager INSTANCE = new CoreAlbumManager();
    }
    public static CoreAlbumManager getInstance() {
        return CoreAlbumManagerHolder.INSTANCE;
    }

    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final HashMap<Integer, AlbumCallbackInterface> mAlbumInterfaceHashMap = new HashMap<>(4);
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2,
            120L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    // 图片加载样式
    private ImageLoaderInterface mImageLoader;
    // UGC是否配置
    private boolean isLicenceValid;

    private CoreAlbumManager(){
        executor.allowCoreThreadTimeOut(true);
    }

    // 初始化UGC
    public void setLicence(Context context, String ugcLicenceUrl, String ugcKey) {
//        TXUGCBase.getInstance().setLicence(context, ugcLicenceUrl, ugcKey);
        UGCKit.init(context.getApplicationContext());
        isLicenceValid = !TextUtils.isEmpty(ugcLicenceUrl) && !TextUtils.isEmpty(ugcKey);
    }

    // 简单判断是否使用UGC
    public boolean isLicenceValid() {
        return isLicenceValid;
    }

    public int getIndex() {
        return atomicInteger.incrementAndGet();
    }

    public void put(int index, AlbumCallbackInterface albumInterface) {
        mAlbumInterfaceHashMap.put(index, albumInterface);
    }

    public AlbumCallbackInterface getAlbumCallback(int index) {
        return mAlbumInterfaceHashMap.remove(index);
    }


    public void setImageLoader(ImageLoaderInterface imageLoader) {
        mImageLoader = imageLoader;
    }
    public ImageLoaderInterface getImageLoader() {
        return mImageLoader;
    }


    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    public Executor getExecutor() {
        return executor;
    }
}
