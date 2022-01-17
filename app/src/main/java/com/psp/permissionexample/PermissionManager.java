package com.psp.permissionexample;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionManager {

    private final Context context;
    private final SessionManager sessionManager;

    public PermissionManager(Context context){
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    public  boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private  boolean shouldAskPermission(String permission){
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public void checkRuntimePermission(String permission, PermissionCallback listener) {
        if (shouldAskPermission(permission)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity)context,permission)) {
                listener.onPermissionPreviouslyDenied();
            } else {
                if (sessionManager.isFirstTimeAsking(permission)) {
                    sessionManager.firstTimeAsking(permission, false);
                    listener.onRequestPermission();
                } else {
                    listener.onPermissionPreviouslyDeniedWithNeverAsk();
                }
            }
        } else {
            listener.onPermissionGranted();
        }
    }
}
