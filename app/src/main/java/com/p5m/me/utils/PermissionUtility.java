package com.p5m.me.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionUtility {
    public static final int REQUEST_LOCATION = 2;
    public static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    public static boolean verifyLocationPermissions(AppCompatActivity appCompatActivity) {
        if (ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(appCompatActivity, PERMISSIONS_LOCATION, REQUEST_LOCATION);
            return false;
        }
        return true;
    }

}
