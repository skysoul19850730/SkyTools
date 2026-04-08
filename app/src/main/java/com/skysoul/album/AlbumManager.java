package com.skysoul.album;

import android.content.Context;

//import com.skysoul.album.action.CameraAction;
import com.skysoul.album.core.CoreAlbumManager;
import com.skysoul.album.action.SelectedAction;
import com.skysoul.album.action.PreviewAction;
import com.skysoul.album.action.CropAction;

public class AlbumManager {

    /**
     * 初始化
     * @param ugcLicenceUrl  腾讯短视频SDK ugc Licence地址
     * @param ugcKey         腾讯短视频SDK ugc key
     * @param imageLoader    图片加载接口
     */
    public static void init(Context context, String ugcLicenceUrl, String ugcKey, ImageLoaderInterface imageLoader){
        CoreAlbumManager.getInstance().setImageLoader(imageLoader);
        CoreAlbumManager.getInstance().setLicence(context, ugcLicenceUrl, ugcKey);
    }

    /**
     * 选择图片
     */
    public static SelectedAction doSelected() {
        return new SelectedAction();
    }

    /**
     * 浏览图片
     */
    public static PreviewAction doPreview() {
        return new PreviewAction();
    }

    /**
     * 图片裁剪
     */
    public static CropAction doCrop() {
        return new CropAction();
    }

    /**
     * 视频封面选择
     */
//    public static CoverAction doCover() {
//        return new CoverAction();
//    }

    /**
     * 视频剪辑
     */
//    public static VideoEditAction doVideoEdit() {
//        return new VideoEditAction();
//    }

    /**
     * 打开相机
     */
//    public static CameraAction doCamera() {
//        return new CameraAction();
//    }
}
