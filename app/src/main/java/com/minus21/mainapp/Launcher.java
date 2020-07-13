package com.minus21.mainapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Launcher extends Activity {
    private static final int REQUEST_EXIT = 1;
    private static final int PERMISSIONS_REQUEST_CODE = 1240;

    String[] appPermissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkAndRequestPermissions())
            startMainActivity();

        /* Start MainActivity */
    }

    private boolean checkAndRequestPermissions(){
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions){
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED)
            {
                listPermissionsNeeded.add(perm);
            }
        }

        if (!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSIONS_REQUEST_CODE);
            Log.d("state","permission request");
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE){
            int deniedCount = 0;
            if (grantResults.length > 0){
                for (int i=0; i<grantResults.length; i++){
                    if (permissions[i].equals(appPermissions[i])){
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            deniedCount++;
                        }
                    }
                }
            }  else showToast_PermissionDeny();
            if (deniedCount == 0) startMainActivity();
            else showToast_PermissionDeny();
        }
    }

    private void showToast_PermissionDeny(){
        Toast.makeText(this, "권한 요청에 동의해주세요.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void startMainActivity(){
        Intent intent = new Intent (this, MainActivity.class);
        startActivityForResult(intent,REQUEST_EXIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXIT){
            if (resultCode == RESULT_OK){
                finish();
            }
        }
    }
}
