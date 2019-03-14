package com.mobile.bonrix.bonrixappstore.utility;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mobile.bonrix.bonrixappstore.R;
import com.mobile.bonrix.bonrixappstore.activity.MainActivity;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.core.app.NotificationCompat;

import static android.app.Notification.GROUP_ALERT_SUMMARY;


/**
 * Created by Jatin on 31/10/2017.
 */

public class ManagerDW {
    String TAG = "ManagerDW";

    Context contextMain;
    private static volatile ManagerDW Instance = null;
    public static DownloadStatusIndication downloadStatusIndicationInterface;
    public static RetryPolicy retryPolicy;
    public static ThinDownloadManager downloadManager;
    public int downloadId1;
    String CHANNEL_ID = "Event";
    CharSequence name = "Event Notifications";


    public static ManagerDW getInstance() {
        ManagerDW localInstance = Instance;
        if (localInstance == null) {
            synchronized (ManagerDW.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new ManagerDW();
                    downloadManager = new ThinDownloadManager(10);
                    retryPolicy = new DefaultRetryPolicy();
                }
            }
        }
        return localInstance;
    }

    public interface DownloadStatusIndication {
        public void onDownloadUpdate(String id, String downloadid, int progress);

        public void onDownloadComplete(String id, String downloadid);
    }

    public static void setDownloadStatusIndicationInterface(DownloadStatusIndication playPushInterface) {
        ManagerDW.downloadStatusIndicationInterface = playPushInterface;
    }

    public void callDownload(final Context context, final String myID, String URL, final String downloadFileName, final String apkname) {
        ImageStorage.createDir(context);
        contextMain = context;
        DownloadFile d = new DownloadFile();
        DownloadRequest dr = d.DownloadFile(context, myID, URL, downloadFileName, apkname);
        int a = downloadManager.add(dr);
        Preferance.saveDownloadFileValue(context, myID, a);

    }

    public void callDownload2(final Context context, final String myID, String URL, final String downloadFileName, final String apkname) {
        ImageStorage.createDir(context);

        DownloadFile2 d = new DownloadFile2();
        DownloadRequest dr = d.DownloadFile(context, myID, URL, downloadFileName, apkname);
        int a = downloadManager.add(dr);
        Preferance.saveDownloadFileValue(context, myID, a);

    }

    public void callDownloadShare(final Context context, final String myID, String URL, final String downloadFileName, final String apkname) {
        ImageStorage.createDir(context);

        DownloadFile2Share d = new DownloadFile2Share();
        DownloadRequest dr = d.DownloadFile(context, myID, URL, downloadFileName, apkname);
        int a = downloadManager.add(dr);
        Preferance.saveDownloadFileValue(context, myID, a);
    }

    public class DownloadFile {
        public DownloadFile() {
        }

        @SuppressLint("WrongConstant")
        public DownloadRequest DownloadFile(final Context context, final String myID, String URL, final String downloadFileName, final String apkname) {

            NotificationManager mNotifyManager = null;
            NotificationCompat.Builder mBuilder = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
                mBuilder.setContentTitle(downloadFileName)
                        .setSound(null)
                        .setContentText("Download in progress")
                        .setChannelId(CHANNEL_ID)
                        .setSmallIcon(R.drawable.image_roteate)
                        .setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
                        .setGroup("My group")
                        .setGroupSummary(false)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                ;
            } else {
                mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setContentTitle(downloadFileName)
                        .setContentText("Download in progress")
                        .setSmallIcon(R.drawable.image_roteate);
            }


            String foldername = context.getResources().getString(R.string.app_name);

            File filesDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + foldername + "/");
            Uri downloadUri = Uri.parse(URL);
            Uri destinationUri = Uri.parse(filesDir + "/" + downloadFileName);
            final NotificationManager finalMNotifyManager = mNotifyManager;
            final NotificationCompat.Builder finalMBuilder = mBuilder;
            final DownloadRequest downloadRequest1 = new DownloadRequest(downloadUri)
                    .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.LOW)
                    .setRetryPolicy(retryPolicy)
                    .setDownloadContext("Download1" + myID)
                    .setDownloadListener(new DownloadStatusListener() {
                        @Override
                        public void onDownloadComplete(int id) {

                            if (ManagerDW.downloadStatusIndicationInterface != null) {
                                ManagerDW.downloadStatusIndicationInterface.onDownloadComplete(myID, myID);
                            }

                            long when = System.currentTimeMillis();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setAction("myString" + when);
                            intent.setData((Uri.parse("mystring" + when)));

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            finalMNotifyManager.cancel(Integer.parseInt(myID));


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;

                                PendingIntent Pintent = PendingIntent.getActivity(context, (int) when, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

                                Notification notification = new Notification.Builder(context)
                                        .setContentTitle("" + downloadFileName)
                                        .setContentText("Download complete")
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.ic_notication)
                                        .setContentIntent(Pintent)
                                        .setAutoCancel(true)
                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
                                        .setChannelId(CHANNEL_ID)
                                        .build();


                                NotificationManager mNotifyManager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotifyManager2.createNotificationChannel(mChannel);
                                mNotifyManager2.notify(Integer.parseInt(myID), notification);

                            } else {
                                PendingIntent Pintent = PendingIntent.getActivity(context, (int) when, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                NotificationCompat.Builder mBuilder2;
                                mBuilder2 = new NotificationCompat.Builder(context);
                                mBuilder2.setContentTitle("" + downloadFileName)
                                        .setContentText("Download complete")
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.ic_notication).setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                        R.drawable.app_icon)).setContentIntent(Pintent).setProgress(0, 0, false).setAutoCancel(true);

                                NotificationManager mNotifyManager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                mNotifyManager2.notify(Integer.parseInt(myID), mBuilder2.build());

                            }


                        }

                        @Override
                        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                            finalMNotifyManager.cancel(Integer.parseInt(myID));
                            Toast.makeText(context, "Download Faild, please try again...!!!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {

                            if (ManagerDW.downloadStatusIndicationInterface != null) {
                                ManagerDW.downloadStatusIndicationInterface.onDownloadUpdate(myID, myID, progress);

                            }
                            finalMBuilder.setProgress(100, progress, false);
                            finalMNotifyManager.notify(Integer.parseInt(myID), finalMBuilder.build());

                        }
                    });
            ;
            return downloadRequest1;
        }
    }

    public class DownloadFile2 {
        public DownloadFile2() {
        }

        @SuppressLint("WrongConstant")
        public DownloadRequest DownloadFile(final Context context, final String myID, String URL, final String downloadFileName, final String apkname) {

            final NotificationManager mNotifyManager;
            final NotificationCompat.Builder mBuilder;


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
                mBuilder.setContentTitle(downloadFileName)
                        .setSound(null)
                        .setContentText("Download in progress")
                        .setChannelId(CHANNEL_ID)
                        .setSmallIcon(R.drawable.image_roteate)
                        .setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
                        .setGroup("My group")
                        .setGroupSummary(false)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                ;
            } else {
                mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setContentTitle(downloadFileName)
                        .setContentText("Download in progress")
                        .setSmallIcon(R.drawable.image_roteate);
            }
            String foldername = context.getResources().getString(R.string.app_name);

            File filesDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + foldername + "/");
            Uri downloadUri = Uri.parse(URL);
            final NotificationManager finalMNotifyManager = mNotifyManager;
            final NotificationCompat.Builder finalMBuilder = mBuilder;
            Uri destinationUri = Uri.parse(filesDir + "/" + downloadFileName);
            final DownloadRequest downloadRequest1 = new DownloadRequest(downloadUri)
                    .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.LOW)
                    .setRetryPolicy(retryPolicy)
                    .setDownloadContext("Download1" + myID)
                    .setDownloadListener(new DownloadStatusListener() {
                        @Override
                        public void onDownloadComplete(int id) {


                            if (ManagerDW.downloadStatusIndicationInterface != null) {
                                ManagerDW.downloadStatusIndicationInterface.onDownloadComplete(myID, myID);
                            }
                            long when = System.currentTimeMillis();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setAction("myString" + when);
                            intent.setData((Uri.parse("mystring" + when)));

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            finalMNotifyManager.cancel(Integer.parseInt(myID));


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;

                                PendingIntent Pintent = PendingIntent.getActivity(context, (int) when, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

                                Notification notification = new Notification.Builder(context)
                                        .setContentTitle("" + downloadFileName)
                                        .setContentText("Download complete")
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.ic_notication)
                                        .setContentIntent(Pintent)
                                        .setAutoCancel(true)
                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
                                        .setChannelId(CHANNEL_ID)
                                        .build();


                                NotificationManager mNotifyManager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotifyManager2.createNotificationChannel(mChannel);
                                mNotifyManager2.notify(Integer.parseInt(myID), notification);

                            } else {
                                PendingIntent Pintent = PendingIntent.getActivity(context, (int) when, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                NotificationCompat.Builder mBuilder2;
                                mBuilder2 = new NotificationCompat.Builder(context);
                                mBuilder2.setContentTitle("" + downloadFileName)
                                        .setContentText("Download complete")
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.ic_notication).setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                        R.drawable.app_icon)).setContentIntent(Pintent).setProgress(0, 0, false).setAutoCancel(true);

                                NotificationManager mNotifyManager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                mNotifyManager2.notify(Integer.parseInt(myID), mBuilder2.build());

                            }
                            Intent intent1 = new Intent(Intent.ACTION_VIEW);
                            intent1.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/BonrixAppStore/" + apkname)), "application/vnd.android.package-archive");
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);



                        }

                        @Override
                        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                            mNotifyManager.cancel(Integer.parseInt(myID));
                            Toast.makeText(context, "Download Faild, please try again...!!!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {


                            if (ManagerDW.downloadStatusIndicationInterface != null) {
                                ManagerDW.downloadStatusIndicationInterface.onDownloadUpdate(myID, myID, progress);

                            }
                            mBuilder.setProgress(100, progress, false);
                            mNotifyManager.notify(Integer.parseInt(myID), mBuilder.build());

                        }
                    });
            ;
            return downloadRequest1;
        }
    }

    public class DownloadFile2Share {
        public DownloadFile2Share() {
        }

        @SuppressLint("WrongConstant")
        public DownloadRequest DownloadFile(final Context context, final String myID, String URL, final String downloadFileName, final String apkname) {

            final NotificationManager mNotifyManager;
            final NotificationCompat.Builder mBuilder;


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
                mBuilder.setContentTitle(downloadFileName)
                        .setSound(null)
                        .setContentText("Download in progress")
                        .setChannelId(CHANNEL_ID)
                        .setSmallIcon(R.drawable.image_roteate)
                        .setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
                        .setGroup("My group")
                        .setGroupSummary(false)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                ;
            } else {
                mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setContentTitle(downloadFileName)
                        .setContentText("Download in progress")
                        .setSmallIcon(R.drawable.image_roteate);
            }

            String foldername = context.getResources().getString(R.string.app_name);

            File filesDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + foldername + "/");
            Uri downloadUri = Uri.parse(URL);
            final NotificationManager finalMNotifyManager = mNotifyManager;
            final NotificationCompat.Builder finalMBuilder = mBuilder;
            Uri destinationUri = Uri.parse(filesDir + "/" + downloadFileName);
            final DownloadRequest downloadRequest1 = new DownloadRequest(downloadUri)
                    .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.LOW)
                    .setRetryPolicy(retryPolicy)
                    .setDownloadContext("Download1" + myID)
                    .setDownloadListener(new DownloadStatusListener() {
                        @Override
                        public void onDownloadComplete(int id) {


                            if (ManagerDW.downloadStatusIndicationInterface != null) {
                                ManagerDW.downloadStatusIndicationInterface.onDownloadComplete(myID, myID);
                            }


                            long when = System.currentTimeMillis();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setAction("myString" + when);
                            intent.setData((Uri.parse("mystring" + when)));

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            finalMNotifyManager.cancel(Integer.parseInt(myID));


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;

                                PendingIntent Pintent = PendingIntent.getActivity(context, (int) when, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

                                Notification notification = new Notification.Builder(context)
                                        .setContentTitle("" + downloadFileName)
                                        .setContentText("Download complete")
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.ic_notication)
                                        .setContentIntent(Pintent)
                                        .setAutoCancel(true)
                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
                                        .setChannelId(CHANNEL_ID)
                                        .build();


                                NotificationManager mNotifyManager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotifyManager2.createNotificationChannel(mChannel);
                                mNotifyManager2.notify(Integer.parseInt(myID), notification);

                            } else {
                                PendingIntent Pintent = PendingIntent.getActivity(context, (int) when, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                NotificationCompat.Builder mBuilder2;
                                mBuilder2 = new NotificationCompat.Builder(context);
                                mBuilder2.setContentTitle("" + downloadFileName)
                                        .setContentText("Download complete")
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.ic_notication).setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                        R.drawable.app_icon)).setContentIntent(Pintent).setProgress(0, 0, false).setAutoCancel(true);

                                NotificationManager mNotifyManager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                mNotifyManager2.notify(Integer.parseInt(myID), mBuilder2.build());

                            }


                            ApplicationInfo app = context.getApplicationContext().getApplicationInfo();
                            String filePath = app.sourceDir;

                            Intent intentshare = new Intent(Intent.ACTION_SEND);

                            // MIME of .apk is "application/vnd.android.package-archive".
                            // but Bluetooth does not accept this. Let's use "*/*" instead.
                            intentshare.setType("*/*");

                            // Append file and send Intent
                            File originalApk = new File(filePath);

                            try {
                                //Make new directory in new location
                                File tempFile = new File(context.getExternalCacheDir() + "/BonrixAppStore");
                                //If directory doesn't exists create new
                                if (!tempFile.isDirectory())
                                    if (!tempFile.mkdirs())
                                        return;
                                //Get application's name and convert to lowercase
                                tempFile = new File(tempFile.getPath() + "/" + downloadFileName);
                                //If file doesn't exists create new
                                if (!tempFile.exists()) {
                                    if (!tempFile.createNewFile()) {
                                        return;
                                    }
                                }
                                //Copy file to new location
                                InputStream in = new FileInputStream(originalApk);
                                OutputStream out = new FileOutputStream(tempFile);

                                byte[] buf = new byte[1024];
                                int len;
                                while ((len = in.read(buf)) > 0) {
                                    out.write(buf, 0, len);
                                }
                                in.close();
                                out.close();
                                System.out.println("File copied.");
                                //Open share dialog
                                intentshare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
                                context.startActivity(Intent.createChooser(intentshare, "Share app via"));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                            mNotifyManager.cancel(Integer.parseInt(myID));
                            Toast.makeText(context, "Download Faild, please try again...!!!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {


                            if (ManagerDW.downloadStatusIndicationInterface != null) {
                                ManagerDW.downloadStatusIndicationInterface.onDownloadUpdate(myID, myID, progress);

                            }
                            mBuilder.setProgress(100, progress, false);
                            mNotifyManager.notify(Integer.parseInt(myID), mBuilder.build());

                        }
                    });
            ;
            return downloadRequest1;
        }
    }


}
