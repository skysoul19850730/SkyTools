package com.skysoul.album.core.ugckit.slider;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.skysoul.album.util._AlbumUtils;

import java.util.ArrayList;

public class TCVideoEditerAdapter extends BaseAdapter {
    private final Context mContext;
    private ArrayList<Bitmap> data = new ArrayList<Bitmap>();

    public TCVideoEditerAdapter(Context context) {
        mContext = context;
    }


    public void add(int position, Bitmap b) {
        data.add(b);
        notifyDataSetChanged();
    }

    public void clearAllBitmap() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            int height = _AlbumUtils.dp2px(60);
            ImageView view = new ImageView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(height, height));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder = new ViewHolder();
            convertView = view;
            viewHolder.thumb = view;
            convertView.setTag(viewHolder);
        }
        viewHolder.thumb.setImageBitmap(data.get(position));
        return convertView;
    }

    public class ViewHolder {
        public ImageView thumb;
    }

}
