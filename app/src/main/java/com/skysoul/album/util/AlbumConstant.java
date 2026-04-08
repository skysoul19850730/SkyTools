package com.skysoul.album.util;

import java.util.HashMap;

public interface AlbumConstant {

    int PREVIEW_REQUEST_CODE = 10001;           // 图片预览
    int BROWSER_REQUEST_CODE = 10002;           // 图片浏览
    int CROP_REQUEST_CODE = 10003;              // 图片裁剪
    int TAKE_REQUEST_CODE = 10004;              // 拍照录像
    int VIDEO_EDIT_REQUEST_CODE = 10005;        // 视频剪辑
    int VIDEO_COVER_REQUEST_CODE = 10006;        // 视频封面
    int SYSTEM_CAMERA_REQUEST_CODE = 10007;        // 系统相机


    String key_from_other = "key_from_other";           // 是否来源于外部
    String key_index = "key_index";           // 次序
    String key_media_option = "key_media_option";           // 操作配置
    String key_use_screen_orientation_landscape = "key_use_landscape";  // 是否使用横屏

    String key_media_type = "key_media_type";           // 媒体类别
    String key_media_info = "key_media_info";
    String key_video_limit_duration = "key_video_limit_duration";   // 录制时长限制
    String key_cur_position = "key_cur_position";        // 图片所在的下标
    String key_option_max_selected_count = "key_max_selected_count"; // 最大的选择数量
    String key_media_list = "key_media_list";            // 预览图片的集合
    String key_album_done = "key_album_done";            // 执行完成，点击完成


}
