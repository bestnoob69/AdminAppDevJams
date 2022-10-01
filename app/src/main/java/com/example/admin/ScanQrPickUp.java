package com.example.admin;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.api.UserService;
import com.example.admin.api.WashService;
import com.example.admin.context.GlobalContext;
import com.example.admin.model.User;
import com.example.admin.model.Wash;
import com.example.admin.utils.Constants;
import com.example.admin.utils.HttpStatus;
import com.example.admin.utils.MyRunnable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class ScanQrPickUp extends AppCompatActivity {
    private ScannerLiveView scannerLiveView;
    private UserService userService;
    private WashService washService;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_pick_up);
        scannerLiveView = findViewById(R.id.camView);
        userService = new UserService();
        washService = new WashService();
        progressDialog=new ProgressDialog(this);

        if(checkPermission()) Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        else requestPermission();


        scannerLiveView.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                Toast.makeText(ScanQrPickUp.this, "Scanner Started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                Toast.makeText(ScanQrPickUp.this, "Scanner Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {

            }

            @Override
            public void onCodeScanned(String data) {

                progressDialog.setTitle("Verifying Details");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                userService.getUser(data, new MyRunnable() {
                    @Override
                    public void run() {
                        if (HttpStatus.SC_OK == statusCode) {
                            try {
                                User user = Constants.OBJECT_MAPPER.readValue(jsonResponse, User.class);
                                GlobalContext.currentOnlineUser = user;

                                getWashesAndMoveAhead();
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ScanQrPickUp.this, "Error : " + jsonResponse, Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });

            }
        });
    }

    private void getWashesAndMoveAhead() {
        washService.listWashesForUser(GlobalContext.currentOnlineUser.getId(),
                new MyRunnable() {
                    @Override
                    public void run() {
                        if (HttpStatus.SC_OK == statusCode) {
                            try {
                                GlobalContext.washes = Constants.OBJECT_MAPPER.readValue(jsonResponse, new TypeReference<List<Wash>>(){});

                                Intent i = new Intent(ScanQrPickUp.this,DetailsPageDuringPickUp.class);
                                startActivity(i);

                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ScanQrPickUp.this, "Error : " + jsonResponse, Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private boolean checkPermission(){
        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        int vibratePermission = ContextCompat.checkSelfPermission(getApplicationContext(),VIBRATE);
        return cameraPermission == PackageManager.PERMISSION_GRANTED && vibratePermission == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(){
        int permissionCode = 200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA,VIBRATE},permissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if(cameraAccepted && vibrationAccepted) Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "You cannot access Camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        scannerLiveView.stopScanner();
        super.onPause();
    }

    @Override
    protected void onResume() {
        ZXDecoder decoder = new ZXDecoder();

        decoder.setScanAreaPercent(0.8);
        scannerLiveView.setDecoder(decoder);
        scannerLiveView.startScanner();

        super.onResume();
    }
}
