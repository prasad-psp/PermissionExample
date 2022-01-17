package com.psp.permissionexample;

public interface PermissionCallback {
    void onRequestPermission();
    void onPermissionGranted();
    void onPermissionPreviouslyDenied();
    void onPermissionPreviouslyDeniedWithNeverAsk();
}
