package com.skysoul.album;

/**
 * 裁剪比例
 */
public enum CropRatio {
    RATIO_0_0(0f, "自由"),
    RATIO_1_1(1f, "1:1"),
    RATIO_4_3(4 / 3f, "4:3"),
    RATIO_3_4(3 / 4f, "3:4"),
    RATIO_CUSTOM(0f, "自定义比例");

    private float ratio;
    public final String name;
    CropRatio(float ratio, String name) {
        this.ratio = ratio;
        this.name = name;
    }

    public float getRatio() {
        return ratio;
    }

    // 设置自定义裁剪比例
    public void setRatio(float ratio) {
        if (this == RATIO_CUSTOM) {
            this.ratio = ratio;
        }
    }
}
