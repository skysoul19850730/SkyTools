package com.skysoul.album.core.ugckit.basic;

import com.skysoul.album.core.ugckit.basic.OnUpdateUIListener;

/**
 * 视频生成器
 */
public class BaseGenerateKit {

    protected OnUpdateUIListener mOnUpdateUIListener;

    public void setOnUpdateUIListener(OnUpdateUIListener listener) {
        mOnUpdateUIListener = listener;
    }
}
