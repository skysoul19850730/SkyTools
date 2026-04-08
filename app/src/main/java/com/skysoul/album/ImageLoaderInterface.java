package com.skysoul.album;

import android.widget.ImageView;

/**
 * 对外暴露的图片加载接口
 * 让接入方实现图片加载
 */
public interface ImageLoaderInterface {
    void loadImage(ImageView imageView, String imgPath);
}
