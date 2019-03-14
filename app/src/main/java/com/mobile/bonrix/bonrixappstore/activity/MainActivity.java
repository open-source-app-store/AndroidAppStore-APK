package com.mobile.bonrix.bonrixappstore.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.StrictMode;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.mobile.bonrix.bonrixappstore.R;
import com.mobile.bonrix.bonrixappstore.fragment.AboutFragment;
import com.mobile.bonrix.bonrixappstore.fragment.HomeFragment;
import com.mobile.bonrix.bonrixappstore.permissionutils.AskagainCallback;
import com.mobile.bonrix.bonrixappstore.permissionutils.FullCallback;
import com.mobile.bonrix.bonrixappstore.permissionutils.PermissionEnum;
import com.mobile.bonrix.bonrixappstore.permissionutils.PermissionManager;
import com.mobile.bonrix.bonrixappstore.permissionutils.PermissionUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FullCallback {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StringBuilder stringBuilder = new StringBuilder();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment(), R.id.container, HomeFragment.class.getName());
//        replaceFragment(new AboutFragment(), R.id.container, AboutFragment.class.getName());

        askRequiredPermissions();


    }


    private void askRequiredPermissions() {
        if (PermissionUtils.isGranted(MainActivity.this, PermissionEnum.READ_EXTERNAL_STORAGE)
                && PermissionUtils.isGranted(MainActivity.this, PermissionEnum.WRITE_EXTERNAL_STORAGE)
                && PermissionUtils.isGranted(MainActivity.this, PermissionEnum.SEND_SMS)
        ) {
//			openScreen();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ArrayList<PermissionEnum> permissions = new ArrayList<>();
                permissions.add(PermissionEnum.READ_EXTERNAL_STORAGE);
                permissions.add(PermissionEnum.WRITE_EXTERNAL_STORAGE);
                permissions.add(PermissionEnum.SEND_SMS);


                PermissionManager.with(MainActivity.this)
                        .permissions(permissions)
                        .askagain(true)
                        .askagainCallback(new AskagainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(this).ask();
            } else {
//				openScreen();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.handleResult(requestCode, permissions, grantResults);
        if (PermissionUtils.isGranted(MainActivity.this, PermissionEnum.READ_EXTERNAL_STORAGE)
                && PermissionUtils.isGranted(MainActivity.this, PermissionEnum.WRITE_EXTERNAL_STORAGE)
                && PermissionUtils.isGranted(MainActivity.this, PermissionEnum.SEND_SMS)
        ) {
        } else {
//			showLog("R-onRequestPermissionsResult");
        }
    }

    private void showDialog(final AskagainCallback.UserResponse response) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Permission needed")
                .setMessage("This app really need this permission to use this application, Please allow this permission?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        response.result(true);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        response.result(false);
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
        boolean isAsked = !permissionsAsked.isEmpty();
        boolean isDenied = !permissionsDenied.isEmpty();
        String message;
        if (isAsked && isDenied) {
            message = "You haven't allowed all permission requested by " + getString(R.string.app_name);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Request")
                    .setMessage(message)
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            askRequiredPermissions();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    void replaceFragment(Fragment mFragment, int id, String tag) {
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        mTransaction.replace(id, mFragment);
        mTransaction.addToBackStack(mFragment.toString());
        mTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            shareAppLink(getResources().getString(R.string.applink));
        } else if (id == R.id.nav_about) {
            replaceFragment(new AboutFragment(), R.id.container, AboutFragment.class.getName());
        } else if (id == R.id.nav_home) {
            replaceFragment(new HomeFragment(), R.id.container, HomeFragment.class.getName());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareAppLink(String url) {
        String share = getResources().getString(R.string.msg) + url;
        Intent sendIntentHindi = new Intent();
        sendIntentHindi.setAction(Intent.ACTION_SEND);
        sendIntentHindi.putExtra(Intent.EXTRA_TEXT, share);
        sendIntentHindi.setType("text/plain");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        startActivity(Intent.createChooser(sendIntentHindi, "Share via"));
    }
}
