package com.skysoul.album.core.ugckit.cut;


import com.skysoul.album.core.ugckit.basic.UGCKitResult;

/**
 * 腾讯云短视频UGCKit({@code IVideoCutKit}):视频裁剪。
 * 本组建包含视频裁剪的SDK功能和UI展示，通过拖动裁剪范围，裁剪一个长视频为一个短视频。<br>
 * 您可以通过UGCKit很简单搭建使用视频裁剪功能，也可以定制视频裁剪UI
 * <p>
 * 如下演示了UGCKit视频裁剪模块简单用法：<br>
 * 1、在xml中设置
 * <pre>
 * {@code
 * <com.tencent.qcloud.xiaoshipin.uikit.module.cut.VideoCut
 *         android:id="@+id/video_cutter_layout"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent" />
 * }</pre>
 * <p>
 * 2、在Activity中设置
 * <pre>
 *     &#064;Override
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.activity_video_cut);
 *
 *         mVideoCut = (VideoCut) findViewById(R.id.video_cutter_layout);
 *         mVideoCut.setVideoPath(mInVideoPath, mVideoResolution, mCustomBitrate);
 *         mVideoCut.setOnCutListener(new IVideoCutKit.OnCutListener() {
 *             &#064;Override
 *             public void onCutterCompleted() {
 *             }
 *             &#064;Override
 *             public void onCutterCanceled() {
 *             }
 *         });
 *     }
 *
 *     &#064;Override
 *     protected void onResume() {
 *         super.onResume();
 *         mVideoCut.startPlay();
 *     }
 *
 *     &#064;Override
 *     protected void onPause() {
 *         super.onPause();
 *         mVideoCut.stopPlay();
 *     }
 *
 *     &#064;Override
 *     protected void onDestroy() {
 *         super.onDestroy();
 *         mVideoCut.release();
 *     }
 * </pre>
 * <p>
 * 视频裁剪UI定制化<br>
 * 1、定制化"裁剪View" {@link com.skysoul.album.core.ugckit.cut.IVideoCutLayout} <br>
 * <p>
 * 如果您不使用UGCKit视频裁剪组件，自行搭建UI，调用SDK功能。<br>
 * 请参照文档<a href="https://cloud.tencent.com/document/product/584/9502">视频编辑(Android)</a> 中"压缩裁剪"
 */
public interface IVideoCutKit {

    /**
     * 设置视频裁剪的源路径
     *
     * @param videoPath 视频裁剪的源路径
     */
    void setVideoPath(String videoPath);

    /**
     * 开始播放视频
     */
    void startPlay();

    /**
     * 停止播放视频
     */
    void stopPlay();

    /**
     * 释放资源
     */
    void release();

    /**
     * 设置视频裁剪的监听器
     */
    void setOnCutListener(OnCutListener listener);

    interface OnCutListener {
        /**
         * 视频裁剪操作完成
         */
        void onCutterCompleted(UGCKitResult ugcKitResult);

        /**
         * 视频裁剪操作取消
         */
        void onCutterCanceled();
    }

    /**
     * @return 裁剪完的视频路径
     */
    String getVideoOutputPath();
}
