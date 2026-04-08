package com.skysoul.album.view.ucrop.callback;

import android.graphics.Bitmap;

import com.skysoul.album.view.ucrop.model.ExifInfo;

public interface BitmapLoadCallback {

    void onBitmapLoaded(Bitmap bitmap, ExifInfo exifInfo, String imageInputPath, String imageOutputPath);

    void onFailure(Exception bitmapWorkerException);

}