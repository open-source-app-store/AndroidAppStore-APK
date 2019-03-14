package com.mobile.bonrix.bonrixappstore.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.view.Window;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Circle;
import com.mobile.bonrix.bonrixappstore.R;

public class AppUtils {
    static Circle mCircleDrawable;


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static Dialog showDialogProgressBar2(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog2);
        TextView progressBar = (TextView) dialog.findViewById(R.id.text);
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(context.getResources().getColor(R.color.colorPrimary));
        progressBar.setCompoundDrawables(null, null, mCircleDrawable, null);
        progressBar.setBackgroundColor(Color.TRANSPARENT);
        mCircleDrawable.start();

        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }


}
