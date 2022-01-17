package com.psp.permissionexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.psp.permissionexample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements PermissionCallback {

    ActivityMainBinding binding;

    private final int REQUEST_READ_CONTACTS = 1;

    private PermissionManager permissionManager;

    private final String PERMISSION = Manifest.permission.READ_CONTACTS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize permission manager
        permissionManager = new PermissionManager(this);

        binding.btnRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                permissionManager.checkRuntimePermission(PERMISSION,MainActivity.this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if(requestCode == REQUEST_READ_CONTACTS) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]
                {PERMISSION}, REQUEST_READ_CONTACTS);
        Toast.makeText(this, "Request permission", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionGranted() {
        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionPreviouslyDenied() {

        new AlertDialog.Builder(this)
                .setTitle("Permission denied")
                .setMessage("Without Read Contact permission the app is unable to send messages.Are you sure you want to denied these permission?")
                .setPositiveButton("RE-TRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]
                                {PERMISSION}, REQUEST_READ_CONTACTS);
                    }
                })
                .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .create().show();
    }

    @Override
    public void onPermissionPreviouslyDeniedWithNeverAsk() {
        new AlertDialog.Builder(this)
                .setTitle("Permission not enabled")
                .setMessage("PermissionExample requires Read Contact permission be enabled to read contact for calling service." +
                        " Please enabled Read Contact permission in settings.")
                .setPositiveButton("OPEN SETTING", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "failed to open Settings\n" + e, Toast.LENGTH_LONG).show();
                            Log.d("error", e.toString());
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .create().show();
    }
}