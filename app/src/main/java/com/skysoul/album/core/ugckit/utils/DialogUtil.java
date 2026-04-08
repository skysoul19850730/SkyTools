package com.skysoul.album.core.ugckit.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skysoul.album.util._AlbumUtils;


/**
 * 对话框工具类
 */
public class DialogUtil {

    public static void showDialog(Context context, String title, String content, final View.OnClickListener listener) {
        final Dialog dialog = new Dialog(context, _AlbumUtils.getResId(context, "ALBUM_UGCKitConfirmDialogStyle", "style"));
        final View v = LayoutInflater.from(context).inflate(_AlbumUtils.getResLayoutId(context, "album_ugckit_dialog_ugc_tip"), null);
        dialog.setContentView(v);
        TextView tvTitle = (TextView) dialog.findViewById(_AlbumUtils.getResViewId(context, "album_tv_title"));
        TextView tvContent = (TextView) dialog.findViewById(_AlbumUtils.getResViewId(context, "album_tv_msg"));
        Button btnOk = (Button) dialog.findViewById(_AlbumUtils.getResViewId(context, "album_btn_ok"));
        tvTitle.setText(title);
        tvContent.setText(content);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
