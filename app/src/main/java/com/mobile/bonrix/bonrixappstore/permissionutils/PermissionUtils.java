package com.mobile.bonrix.bonrixappstore.permissionutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.ContextCompat;

/**
 * Created by rahul on 22/06/16.
 */
public class PermissionUtils {

    public static boolean isGranted(Context context, PermissionEnum permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(context, permission.toString()) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isGranted(Context context, PermissionEnum... permission) {
        for (PermissionEnum permissionEnum : permission) {
            if (!isGranted(context, permissionEnum)) {
                return false;
            }
        }
        return true;
    }

    public static Intent openApplicationSettings(String packageName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            return intent;
        }
        return new Intent();
    }

    public static void openApplicationSettings(Context context, String packageName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = openApplicationSettings(packageName);
            context.startActivity(intent);
        }
    }

}
