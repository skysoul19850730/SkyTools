package com.skysoul.album.core.ugckit.basic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skysoul.album.core.ugckit.basic.ITitleBarLayout;
import com.skysoul.album.util._AlbumUtils;


public class TitleBarLayout extends LinearLayout implements ITitleBarLayout {

    private LinearLayout mLeftGroup;
    private TextView mLeftTitle;
    private TextView mCenterTitle;
    private ImageView mLeftIcon;
    private Button mRightButton;
    private RelativeLayout mTitleLayout;

    public TitleBarLayout(Context context) {
        super(context);
        init();
    }

    public TitleBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), _AlbumUtils.getResLayoutId(getContext(), "wpsdk_album_ugckit_title_bar_layout"), this);
        mTitleLayout = findViewById(_AlbumUtils.getResViewId(getContext(), "wpsdk_album_page_title_layout"));
        mLeftGroup = findViewById(_AlbumUtils.getResViewId(getContext(), "wpsdk_album_page_title_left_group"));
        mLeftTitle = findViewById(_AlbumUtils.getResViewId(getContext(), "wpsdk_album_page_title_left_text"));
        mCenterTitle = findViewById(_AlbumUtils.getResViewId(getContext(), "wpsdk_album_page_title"));
        mLeftIcon = findViewById(_AlbumUtils.getResViewId(getContext(), "wpsdk_album_page_title_left_icon"));
        mRightButton = findViewById(_AlbumUtils.getResViewId(getContext(), "wpsdk_album_btn_next"));
    }

    @Override
    public void setLeftIcon(int resId) {
        mLeftIcon.setImageResource(resId);
    }

    @Override
    public void setOnBackClickListener(OnClickListener listener) {
        mLeftGroup.setOnClickListener(listener);
    }

    @Override
    public void setOnRightClickListener(OnClickListener listener) {
        mRightButton.setOnClickListener(listener);
    }

    @Override
    public void setTitle(String title, POSITION position) {
        switch (position) {
            case LEFT:
                mLeftTitle.setText(title);
                break;
            case MIDDLE:
                mCenterTitle.setText(title);
                break;
            case RIGHT:
                mRightButton.setText(title);
                break;
        }
    }

    @Override
    public void setVisible(boolean enable, POSITION position) {
        switch (position) {
            case LEFT:
                mLeftIcon.setVisibility(enable ? View.VISIBLE : View.GONE);
                break;
            case RIGHT:
                mRightButton.setVisibility(enable ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @Override
    public LinearLayout getLeftGroup() {
        return mLeftGroup;
    }

    @Override
    public ImageView getLeftIcon() {
        return mLeftIcon;
    }

    @Override
    public Button getRightButton() {
        return mRightButton;
    }

    @Override
    public TextView getLeftTitle() {
        return mCenterTitle;
    }

    @Override
    public TextView getMiddleTitle() {
        return null;
    }


}
