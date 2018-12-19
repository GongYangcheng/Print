package com.xuanhuai.print.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ====================
 *
 * @file ToastUtils
 * <p/>
 * ======================
 */
public class CustomToast {


    private static Toast toast;

    /**
     * 静态toast
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        // toast消失了  toast 会自动为null
        if (toast == null) {// 消失了
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }

        toast.setText(text);
        toast.show();
    }

    /**
     * 开发用静态toast
     * @param context
     * @param text
     */
    public static void showToastTest(Context context, String text) {
        // toast消失了  toast 会自动为null
        if (toast == null) {// 消失了
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }

        toast.setText(text);
        toast.show();
    }
}
