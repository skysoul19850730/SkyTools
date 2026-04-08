package com.skysoul.album.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.skysoul.album.bean.MediaType;
import java.io.File;
import java.util.Objects;

/**
 * 媒体信息
 */
public class MediaInfo implements Parcelable {
    public String path = "";        // 图片or视频的路径
    public String cropPath = "";    // 裁剪后图片的路径
    public long fileSize = 0;       // 文件大小, 如果使用了裁剪则为裁剪后的值, 如果使用了压缩则最终为压缩后的大小
    public int width = 0;           //宽度 图片, 如果使用了裁剪则为裁剪后的值
    public int height = 0;          //高度 图片, 如果使用了裁剪则为裁剪后的值

    public int mediaType = MediaType.IMAGE.mediaType; // 媒体类型，图片 or 视频

    public String coverPath;        // 视频封面图片路径
    public long duration;           // 视频时长

    public String compressPath = ""; // 图片压缩地址

    public boolean isChecked = false;
    //是否是拍摄
    public boolean isTakeByCamera= false;

    public boolean isGif = false;

    public long getFileSize() {
        if (fileSize > 0) {
            return fileSize;
        }
        fileSize = new File(getShowPath()).length();
        return fileSize;
    }

    /**
     * 获取图片or视频的展示图片
     */
    public String getShowPath() {
        if (isVideo()) {
            return TextUtils.isEmpty(cropPath) ? coverPath : cropPath;
        } else {
            return TextUtils.isEmpty(cropPath) ? path : cropPath;
        }
    }

    public boolean isVideo() {
        return mediaType == MediaType.VIDEO.mediaType;
    }

    public boolean isImage() {
        return mediaType == MediaType.IMAGE.mediaType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaInfo mediaInfo = (MediaInfo) o;
        return path.equals(mediaInfo.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.cropPath);
        dest.writeLong(this.fileSize);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.mediaType);
        dest.writeString(this.coverPath);
        dest.writeLong(this.duration);
        dest.writeString(this.compressPath);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTakeByCamera ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isGif ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.path = source.readString();
        this.cropPath = source.readString();
        this.fileSize = source.readLong();
        this.width = source.readInt();
        this.height = source.readInt();
        this.mediaType = source.readInt();
        this.coverPath = source.readString();
        this.duration = source.readLong();
        this.compressPath = source.readString();
        this.isChecked = source.readByte() != 0;
        this.isTakeByCamera = source.readByte() != 0;
        this.isGif = source.readByte() != 0;
    }

    public MediaInfo() {
    }

    protected MediaInfo(Parcel in) {
        this.path = in.readString();
        this.cropPath = in.readString();
        this.fileSize = in.readLong();
        this.width = in.readInt();
        this.height = in.readInt();
        this.mediaType = in.readInt();
        this.coverPath = in.readString();
        this.duration = in.readLong();
        this.compressPath = in.readString();
        this.isChecked = in.readByte() != 0;
        this.isTakeByCamera = in.readByte() != 0;
        this.isGif = in.readByte() != 0;
    }

    public static final Creator<MediaInfo> CREATOR = new Creator<MediaInfo>() {
        @Override
        public MediaInfo createFromParcel(Parcel source) {
            return new MediaInfo(source);
        }

        @Override
        public MediaInfo[] newArray(int size) {
            return new MediaInfo[size];
        }
    };
}
