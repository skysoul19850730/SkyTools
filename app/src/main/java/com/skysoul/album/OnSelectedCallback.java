package com.skysoul.album;

import com.skysoul.album.bean.MediaInfo;
import com.skysoul.album.core.AlbumCallbackInterface;

import java.util.List;

/**
 * 图片选择回调
 */
public interface OnSelectedCallback extends AlbumCallbackInterface {
    void onSuccess(List<MediaInfo> mediaInfoList);
    void onFail(int code, String msg);
}
