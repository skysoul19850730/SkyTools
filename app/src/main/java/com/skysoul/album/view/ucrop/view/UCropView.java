package com.skysoul.album.view.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.skysoul.album.util._AlbumUtils;
import com.skysoul.album.view.ucrop.callback.CropBoundsChangeListener;
import com.skysoul.album.view.ucrop.callback.OverlayViewChangeListener;
import com.skysoul.album.view.ucrop.view.GestureCropImageView;
import com.skysoul.album.view.ucrop.view.OverlayView;


public class UCropView extends FrameLayout {

    private GestureCropImageView mGestureCropImageView;
    private final OverlayView mViewOverlay;

    public UCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(_AlbumUtils.getResLayoutId(context, "album_ucrop_view"), this, true);
        mGestureCropImageView = findViewById(_AlbumUtils.getResViewId(context, "image_view_crop"));
        mViewOverlay = findViewById(_AlbumUtils.getResViewId(context, "view_overlay"));
        mViewOverlay.processStyledAttributes();
        mGestureCropImageView.processStyledAttributes();


        setListenersToViews();
    }

    private void setListenersToViews() {
        mGestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() {
            @Override
            public void onCropAspectRatioChanged(float cropRatio) {
                mViewOverlay.setTargetAspectRatio(cropRatio);
            }
        });
        mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener() {
            @Override
            public void onCropRectUpdated(RectF cropRect) {
                mGestureCropImageView.setCropRect(cropRect);
            }
        });
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    
    public GestureCropImageView getCropImageView() {
        return mGestureCropImageView;
    }

    
    public OverlayView getOverlayView() {
        return mViewOverlay;
    }

    /**
     * Method for reset state for UCropImageView such as rotation, scale, translation.
     * Be careful: this method recreate UCropImageView instance and reattach it to layout.
     */
    public void resetCropImageView() {
        removeView(mGestureCropImageView);
        mGestureCropImageView = new GestureCropImageView(getContext());
        setListenersToViews();
        mGestureCropImageView.setCropRect(getOverlayView().getCropViewRect());
        addView(mGestureCropImageView, 0);
    }
}