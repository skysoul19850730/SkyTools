package com.skysoul.album.bean;

import com.skysoul.album.bean.MediaInfo;
import java.util.ArrayList;

/**
 * 文件夹信息
 */
public class MediaFolder {
    public String name = "";         // 文件夹名称
    public String path = "";         // 文件夹路径
    public ArrayList<MediaInfo> images = new ArrayList<>();

    /**
     * 获取文件夹中的图片总数量
     */
    public int getImageNum() {
        return images.size();
    }

    /**
     * 获取第一张图片的路径
     */
    public String getFirstImagePath() {
       if (images.size() > 0) {
           return images.get(0).path;
       }
       return "";
    }
}
