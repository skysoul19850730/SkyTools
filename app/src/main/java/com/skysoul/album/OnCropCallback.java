package com.skysoul.album;

import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.core.AlbumCallbackInterface;

/**
 * 图片裁剪回调
 */
public interface OnCropCallback extends AlbumCallbackInterface {
    void onSuccess(MediaInfo mediaInfo);
    void onFail(int code, String msg);
}
