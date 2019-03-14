package com.mobile.bonrix.bonrixappstore.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.numberprogressbar.NumberProgressBar;
import com.mobile.bonrix.bonrixappstore.R;
import com.mobile.bonrix.bonrixappstore.fragment.HomeFragment;
import com.mobile.bonrix.bonrixappstore.model.AppModel;
import com.mobile.bonrix.bonrixappstore.utility.AppUtils;
import com.mobile.bonrix.bonrixappstore.utility.ImageStorage;
import com.mobile.bonrix.bonrixappstore.utility.ManagerDW;
import com.mobile.bonrix.bonrixappstore.utility.RetrofitClient;
import com.mobile.bonrix.bonrixappstore.utility.SingleMediaScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Hp on 10-05-2017.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.CreditHolder> {
    List<AppModel> transactionList;
    Context context;
    String TAG = "TransactionAdapter";
    String complaint_url, complainStatus_url;
    String Message;
    ProgressDialog progressDialog;

    public AppAdapter(List<AppModel> transactionList, Context context) {
        this.transactionList = transactionList;
        this.context = context;
    }

    @Override
    public CreditHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_home, viewGroup, false);
        CreditHolder viewHolder = new CreditHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CreditHolder holder, final int position) {
        final AppModel model = transactionList.get(position);
        final String id = "1";
        final String apkname = model.getApkName().replace(" ", "_");
        String DateTime = model.getPublishdate();
        final String APKUrl = RetrofitClient.Image_url + model.getApkPath();
        String Name = model.getName();
        final String PackageName = model.getPackage();
        final String Version = model.getVersion();
        holder.btn_install.setText("INSTALL");

        Log.e(TAG, "APKUrl   " + APKUrl);

        if (!Version.equalsIgnoreCase("null")) {
            holder.txt_version.setText("Version : " + Version);
        }

        if (!PackageName.equalsIgnoreCase("null")) {
            if (appInstalledOrNot(PackageName)) {
                holder.btn_uninstall.setVisibility(View.VISIBLE);
                holder.btn_install.setVisibility(View.GONE);
                holder.btn_open.setVisibility(View.VISIBLE);
            } else {
                holder.btn_uninstall.setVisibility(View.INVISIBLE);
                holder.btn_install.setVisibility(View.VISIBLE);
                holder.btn_open.setVisibility(View.GONE);

            }
        }
        holder.txt_date.setText("Release on : " + model.getPublishdate());

        if (Name.equalsIgnoreCase("null")) {
            holder.txt_title.setText(apkname.substring(0, apkname.length() - 4));
        } else {
            holder.txt_title.setText(Name);

        }
        setDownloadImage(holder.imgDownloadVideoDetails, holder.imgdownloadview, apkname.replace(" ", "_"));
//        Log.e(TAG, "fileExist(name)   " + ImageStorage.checkifImageExists(name));
//
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAppLink(APKUrl);
            }
        });
        holder.imgShareWhatsandroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWhatsappAppLink(APKUrl);
            }
        });
        holder.imgDownloadVideoDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadapk(APKUrl, holder, apkname, id);
            }
        });
        holder.imgShareSMSLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTextMessageDialog(apkname, APKUrl);


            }
        });
        holder.imgScopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCopy(apkname, APKUrl);


            }
        });
        holder.btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                PackageManager manager = context.getPackageManager();
                i = manager.getLaunchIntentForPackage(PackageName);
                if (i == null)
                    try {
                        throw new PackageManager.NameNotFoundException();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                context.startActivity(i);


            }
        });
        holder.btn_uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + PackageName));
                context.startActivity(intent);
                HomeFragment.getAppList();
            }
        });
//
        holder.btn_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btn_install.setText("Installing...");
                if (!ImageStorage.checkifImageExistsInDownload(apkname, context)) {

                    videoUpdatemanager(holder.layoutProgress, holder.imgDownloadVideoDetails, holder.imgdownloadview, holder.progressBarDownloadNumber, holder, id, apkname);
                    downloadWithInstallapk(APKUrl, holder, apkname, id);
                    notifyItemChanged(position);

                } else {
                    installAPK(apkname, APKUrl);
                    notifyItemChanged(position);

                }
            }
        });

        holder.imgShareWhatsandroidapk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ImageStorage.checkifImageExistsInDownload(apkname, context)) {
                    downloadWithInstallapkShare(APKUrl, holder, apkname, id);
                } else {
                    shareApplication(apkname);
                }
            }
        });
//
//        videoUpdatemanager(holder.layoutProgress, holder.imgDownloadVideoDetails, holder.imgdownloadview, holder.progressBarDownloadNumber, holder, id, apkname);

    }

    private void shareApplication(String apkname) {
        ApplicationInfo app = context.getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);
        String folder = context.getResources().getString(R.string.app_name);
        try {
            //Make new directory in new location
            File tempFile = new File(context.getExternalCacheDir() + "/"+folder);
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + apkname);
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
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            context.startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    private void installAPK(String apkname, String url) {
        String folder = context.getResources().getString(R.string.app_name);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/"+folder+"/" + apkname)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    private void doCopy(String name, String url) {
        TextView tempc = new TextView(context);
        tempc.setText(tempc.getText().toString());

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(url);
        } else {

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", url);
            clipboard.setPrimaryClip(clip);
        }
        Toast toast = Toast.makeText(context, "Copied App Link Succesfully", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void openTextMessageDialog(String name, final String url) {

        final Dialog dialog2 = new Dialog(context);
        dialog2.getWindow();
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog_msg);
        dialog2.getWindow().setLayout(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        dialog2.setCancelable(true);
        Button btnupdate = (Button) dialog2.findViewById(R.id.BTN_OK1);
        final EditText mobile = (EditText) dialog2.findViewById(R.id.edt);
        Button btnclose = (Button) dialog2.findViewById(R.id.BTN_CANCEL1);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.getText().toString().length() != 10) {
                    Toast.makeText(context, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                String message = "Hey, I just found this awesome app From Bonrix App Store. \n " + url;
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(String.valueOf(mobile.getText()), null, message, null, null);
                    Toast.makeText(context, "Message Sent",
                            Toast.LENGTH_LONG).show();
                    dialog2.dismiss();
                } catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                    dialog2.dismiss();
                }
            }
        });
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
        dialog2.show();

    }


    void setDownloadImage(ImageView imgdownload, ImageView imgdownloadview, String name) {

        try {

//            if (!(getfilename(url)) {
            //String fileName = currentData.getQuotes_image_name().substring(0, currentData.getQuotes_image_name().lastIndexOf('.'))+".jpg";
            if (!ImageStorage.checkifImageExistsInDownload(name,context)) {
                imgdownloadview.setVisibility(imgdownloadview.GONE);
                imgdownload.setVisibility(imgdownload.VISIBLE);
            } else {
                imgdownloadview.setVisibility(imgdownloadview.VISIBLE);
                imgdownload.setVisibility(imgdownload.GONE);
            }

//            }
        } catch (Exception e) {
        }
    }


    private void downloadapk(String downloadImageFileName, CreditHolder holder, String apkname, String appid) {

        if (!ImageStorage.checkifImageExistsInDownload(apkname,context)) {
            if (AppUtils.isNetworkConnected(context)) {

                Log.e(TAG, "if downloadapk ");
//                holder.layoutProgress.setVisibility(holder.layoutProgress.VISIBLE);
                holder.imgDownloadVideoDetails.setVisibility(View.GONE);
                holder.imgdownloadview.setVisibility(View.VISIBLE);
                ManagerDW dm = ManagerDW.getInstance(); //DownloadFile(VideoViewActivity.this,modelVideoList.getId(),modelVideoList.getVideourl(),downloadImageFileName,modelVideoList);
                dm.callDownload(context, appid, downloadImageFileName.replace(" ", "%20"), apkname, apkname);

            } else {
                Toast.makeText(context, "No Network Present", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(context, "Already...", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadWithInstallapk(String downloadImageFileName, CreditHolder holder, String apkname, String appid) {

        if (!ImageStorage.checkifImageExistsInDownload(apkname,context)) {
            if (AppUtils.isNetworkConnected(context)) {
//                holder.layoutProgress.setVisibility(holder.layoutProgress.VISIBLE);
                holder.imgDownloadVideoDetails.setVisibility(View.GONE);
                holder.imgdownloadview.setVisibility(View.VISIBLE);
                ManagerDW dm = ManagerDW.getInstance(); //DownloadFile(VideoViewActivity.this,modelVideoList.getId(),modelVideoList.getVideourl(),downloadImageFileName,modelVideoList);
                dm.callDownload2(context, appid, downloadImageFileName.replace(" ", "%20"), apkname, apkname);

            } else {
                Toast.makeText(context, "No Network Present", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(context, "Already...", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadWithInstallapkShare(String downloadImageFileName, CreditHolder holder, String apkname, String appid) {

        if (!ImageStorage.checkifImageExistsInDownload(apkname,context)) {
            if (AppUtils.isNetworkConnected(context)) {
//                holder.layoutProgress.setVisibility(holder.layoutProgress.VISIBLE);
                holder.imgDownloadVideoDetails.setVisibility(View.GONE);
                holder.imgdownloadview.setVisibility(View.VISIBLE);
                ManagerDW dm = ManagerDW.getInstance(); //DownloadFile(VideoViewActivity.this,modelVideoList.getId(),modelVideoList.getVideourl(),downloadImageFileName,modelVideoList);
                dm.callDownloadShare(context, appid, downloadImageFileName.replace(" ", "%20"), apkname, apkname);

            } else {
                Toast.makeText(context, "No Network Present", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(context, "Already...", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareWhatsappAppLink(String url) {
        String share = "Hey, I just found this awesome app From Bonrix App Store. \n " + url;

        Intent shareHWhatsapp = new Intent();
        shareHWhatsapp.setAction(Intent.ACTION_SEND);
        shareHWhatsapp.putExtra(Intent.EXTRA_TEXT, share);
        shareHWhatsapp.setType("text/plain");
        shareHWhatsapp.setPackage("com.whatsapp");
        shareHWhatsapp.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(shareHWhatsapp);
    }

    private void shareAppLink(String url) {
        String share = "Hey, I just found this awesome app From Bonrix App Store. \n " + url;
        Intent sendIntentHindi = new Intent();
        sendIntentHindi.setAction(Intent.ACTION_SEND);
        sendIntentHindi.putExtra(Intent.EXTRA_TEXT, share);
        sendIntentHindi.setType("text/plain");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context.startActivity(Intent.createChooser(sendIntentHindi, "Share via"));
    }

    void videoUpdatemanager(final LinearLayout layoutProgress, final ImageView imgDownloadVideoDetails, final ImageView imgdownloadview, final NumberProgressBar progressBarDownloadNumber, CreditHolder holder, final String appid, final String name) {
        ManagerDW md = ManagerDW.getInstance();

        md.setDownloadStatusIndicationInterface(new ManagerDW.DownloadStatusIndication() {
            @Override
            public void onDownloadUpdate(String id, String downloadid, int progress) {

                if (id.equals(appid)) {
                    layoutProgress.setVisibility(layoutProgress.VISIBLE);
//                    RRsharedownload.setVisibility(RRsharedownload.GONE);

//                    imgShareWhatsandroid.setEnabled(false);
                    imgDownloadVideoDetails.setEnabled(false);
                    imgdownloadview.setEnabled(false);


                    progressBarDownloadNumber.setMax(100);
                    progressBarDownloadNumber.setProgress(progress);
                    progressBarDownloadNumber.getProgress();
                }
            }

            @Override
            public void onDownloadComplete(String id, String downloadid) {

                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/BonrixAppStore/" + name);
                new SingleMediaScanner(context, file);

                if (id.equals(appid)) {
                    setDownloadImage(imgDownloadVideoDetails, imgdownloadview, name);
                    layoutProgress.setVisibility(layoutProgress.GONE);
//                    RRsharedownload.setVisibility(RRsharedownload.VISIBLE);

//                    imgShareWhatsandroid.setEnabled(true);
//                    imgDownloadVideoDetails.setEnabled(true);
//                    imgdownloadview.setEnabled(true);
//

                    String filename = name;


//                    if (file.exists()) {
//                        if (imageAction.equals("share")) {
//                            sharingImage.generalShare(ImageStorage.getDownloadedImage(filename));
//                        } else if (imageAction.equals("whatsapp")) {
//                            sharingImage.whatsappShare(ImageStorage.getDownloadedImage(filename));
//                        } else if (imageAction.equals("hike")) {
//                            sharingImage.hikeShare(ImageStorage.getDownloadedImage(filename));
//                        } else if (imageAction.equals("messagner")) {
//                            sharingImage.fbmessagnerShare(ImageStorage.getDownloadedImage(filename));
//                        } else if (imageAction.equals("instagram")) {
//                            sharingImage.instagramShare(ImageStorage.getDownloadedImage(filename));
//                        } else if (imageAction.equals("facebook")) {
//                            sharingImage.facebookShare(ImageStorage.getDownloadedImage(filename));
//                        }
//
//                    }

                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return (null != transactionList ? transactionList.size() : 0);
    }

    public class CreditHolder extends RecyclerView.ViewHolder {
        private TextView txt_title, txt_date, txt_version;
        private Button btn_uninstall, btn_install, btn_open;
        private LinearLayout layoutProgress;
        com.daimajia.numberprogressbar.NumberProgressBar progressBarDownloadNumber;


        private ImageView imgShare, imgShareWhatsandroid, imgShareWhatsandroidapk, imgDownloadVideoDetails, imgdownloadview, imgShareSMSLink, imgScopyLink;


        public CreditHolder(View itemView) {
            super(itemView);
            this.txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            this.txt_version = (TextView) itemView.findViewById(R.id.txt_version);


            this.btn_uninstall = (Button) itemView.findViewById(R.id.btn_uninstall);
            this.btn_install = (Button) itemView.findViewById(R.id.btn_install);
            this.btn_open = (Button) itemView.findViewById(R.id.btn_open);


            this.imgShare = (ImageView) itemView.findViewById(R.id.imgShare);
            this.imgShareWhatsandroid = (ImageView) itemView.findViewById(R.id.imgShareWhatsandroid);
            this.imgShareWhatsandroidapk = (ImageView) itemView.findViewById(R.id.imgShareWhatsandroidapk);
            this.imgDownloadVideoDetails = (ImageView) itemView.findViewById(R.id.imgDownloadVideoDetails);
            this.imgdownloadview = (ImageView) itemView.findViewById(R.id.imgdownloadview);
            this.imgShareSMSLink = (ImageView) itemView.findViewById(R.id.imgShareSMSLink);
            this.imgScopyLink = (ImageView) itemView.findViewById(R.id.imgScopyLink);

            progressBarDownloadNumber = (com.daimajia.numberprogressbar.NumberProgressBar) itemView.findViewById(R.id.progressBarDownloadNumber);

            this.layoutProgress = (LinearLayout) itemView.findViewById(R.id.layoutProgress);


        }
    }
}
