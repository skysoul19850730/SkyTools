package com.skysoul.album.bean;

public enum MediaType {
    ALL(0), IMAGE(1), VIDEO(2);//, AUDIO(3);

    public int mediaType;

    MediaType(int type) {
        mediaType = type;
    }



    //获取图片的类型
    public MediaType isPictureType(String pictureType) {
        if ("images/png".equals(pictureType) || "images/PNG".equals(pictureType) || "images/jpeg".equals(pictureType) || "images/JPEG".equals(pictureType)
                || "mages/webp".equals(pictureType) || "images/WEBP".equals(pictureType) || "images/gif".equals(pictureType) || "images/GIF".equals(pictureType)
                || "images/bmp".equals(pictureType) || "imagex-ms-bmp".equals(pictureType) || "images/*".equals(pictureType)) {
            return IMAGE;
        } else if ("video/3gp".equals(pictureType) || "video/3gpp".equals(pictureType) || "video/3gpp2".equals(pictureType) || "video/avi".equals(pictureType)
                || "video/mp4".equals(pictureType) || "video/quicktime".equals(pictureType) || "video/x-msvideo".equals(pictureType) || "video/x-matroska".equals(pictureType)
                || "video/mpeg".equals(pictureType) || "video/webm".equals(pictureType) || "video/mp2ts".equals(pictureType) || "video/*".equals(pictureType) ) {
            return VIDEO;
        }
//        else if ("audio/mpeg".equals(pictureType) || "audio/x-ms-wma".equals(pictureType) || "audio/x-wav".equals(pictureType) || "audio/amr".equals(pictureType)
//                || "audio/wav".equals(pictureType) || "audio/aac".equals(pictureType) || "audio/mp4".equals(pictureType) || "audio/quicktime".equals(pictureType)
//                || "audio/lamr".equals(pictureType) || "audio/3gpp".equals(pictureType) ) {
//            return AUDIO;
//        }
        else {
            return IMAGE;
        }
    }
}
