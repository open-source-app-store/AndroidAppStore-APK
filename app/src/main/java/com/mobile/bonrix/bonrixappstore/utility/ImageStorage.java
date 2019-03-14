package com.mobile.bonrix.bonrixappstore.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.mobile.bonrix.bonrixappstore.R;

import java.io.File;

public class ImageStorage {

    static String TAG = "ImageStorage";

    public static void createDir(Context context) {
        String foldername = context.getResources().getString(R.string.app_name);
        File sdcard = Environment.getExternalStorageDirectory();
        File folder = new File(sdcard.getAbsoluteFile(), foldername);//the dot makes this directory hidden to the user
        folder.mkdir();

        folder = new File(sdcard.getAbsoluteFile(), foldername);//the dot makes this directory hidden to the user
        folder.mkdir();

        folder = new File(sdcard.getAbsoluteFile(), foldername);//the dot makes this directory hidden to the user
        folder.mkdir();

    }


    public static File getImage(String imagename, Context context) {
        String foldername = context.getResources().getString(R.string.app_name);

        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
                return null;

            mediaImage = new File(myDir.getPath() + "/" + foldername + "/" + imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }

    public static boolean checkifImageExists(String imagename,Context context) {
        Bitmap b = null;
        File file = ImageStorage.getImage("/" + imagename + "",context);
        String path = file.getAbsolutePath();

        if (path != null)

            try {
                b = BitmapFactory.decodeFile(path);
            } catch (Exception e) {

            }

        if (b == null || b.equals("")) {
            return false;
        }
        return true;
    }


    public static File getDownloadedImage(String imagename, Context context) {
        String foldername = context.getResources().getString(R.string.app_name);

        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
                return null;

            mediaImage = new File(myDir.getPath() + "/" + foldername + "/" + imagename);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }


    public static boolean checkifImageExistsInDownload(String apkname, Context context) {
        File file = ImageStorage.getDownloadedImage("/" + apkname + "", context);
        Log.e(TAG, "checkifImageExistsInDownload   file   " + file);


        return file.exists();
    }

}