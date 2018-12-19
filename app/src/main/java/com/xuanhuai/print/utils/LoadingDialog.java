package com.xuanhuai.print.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog
{
    private static ProgressDialog dialog;

    public static void showDialog(Context context, String message)
    {
        dialog = new ProgressDialog(context);

        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    public static void closeDialog()
    {
        dialog.dismiss();
    }
}
