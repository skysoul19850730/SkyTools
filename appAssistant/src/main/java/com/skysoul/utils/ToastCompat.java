package com.skysoul.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Created by liuxiang on 2020/3/25
 */
public final class ToastCompat {
    private static final String TAG = "ToastCompat";
    private static Field sField_TN;
    private static Field sField_TN_Handler;

    public static Toast makeText(Context context, CharSequence text, int duration) {
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(context, text, duration);
        hackTN(toast);
        return toast;
    }

    public static Toast makeText(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }


    public static boolean checkIfNeedToHack() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1;
    }

    /**
     * Android 7.1 系统的Bug Toast.show时可能会导致BadTokenException,该问题在Android 8.0源码已catch该异常
     * 所以在Android 7.1的系统中采用hook方法修复
     *
     * @param toast
     */
    public static void hackTN(Toast toast) {
        if (checkIfNeedToHack()) {
            try {
                Log.w(TAG, "hackTN: Toast");
                if (sField_TN == null) {
                    sField_TN = FieldUtils.getDeclaredField(Toast.class, "mTN");
                }
                Object tn = sField_TN.get(toast);
                if (sField_TN_Handler == null) {
                    sField_TN_Handler = FieldUtils.getDeclaredField(sField_TN.getType(), "mHandler");
                }
                Handler preHandler = (Handler) sField_TN_Handler.get(tn);
                FieldUtils.setFieldValue(tn, sField_TN_Handler, new SafeHandler(preHandler));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class SafeHandler extends Handler {

        private final Handler impl;

        public SafeHandler(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);
        }
    }
}
