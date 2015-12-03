package com.tianzh.pay.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.tianzh.pay.service.callback.OnDialogClickListener;

/**
 * Created by pig on 2015-09-09.
 */
public class AlertUtils {
    static ProgressDialog progressDialog;
    private static AlertDialog alertDialog;

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showProgressDialog(Context context, String msg) {
        if (progressDialog == null) progressDialog = new ProgressDialog(context);

        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showAlertDialog(Context context, String msg, final OnDialogClickListener listener) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                listener.onPositiveClick();
            }
        });
        builder.setNegativeButton("取 消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                listener.onNegativeClick();
            }
        });

        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            builder.setTitle(packageInfo.applicationInfo.loadLabel(packageManager));
            builder.setIcon(packageInfo.applicationInfo.loadIcon(packageManager));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            builder.setTitle("注 意");
            builder.setIcon(android.R.drawable.ic_menu_info_details);
        }

        alertDialog = builder.create();
        alertDialog.show();
    }


    public static void dismissAlertDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}
