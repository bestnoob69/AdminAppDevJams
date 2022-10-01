package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

//idBtnScanQR checkHistory
    private Button scanQRBtnDropOff,scanQRBtnPickUp;
    //String origin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//      checkHistory = findViewById(R.id.checkHistory);
        scanQRBtnDropOff = findViewById(R.id.idBtnScanQR);
        scanQRBtnPickUp = findViewById(R.id.idBtnScanQRPickUp);

//        checkHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                origin = "History";
//                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit();
//                editor.putString("USERNAME", origin);
//                editor.apply();
//                Intent i = new Intent(HomeActivity.this,CheckHistory.class);
//                startActivity(i);
//            }
//        });
        scanQRBtnDropOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                origin = "Drop";
//                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit();
//                editor.putString("USERNAME", origin);
//                editor.apply();
                Intent i = new Intent(HomeActivity.this, ScanQrDropOff.class);
                startActivity(i);
            }
        });
        scanQRBtnPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                origin = "Pick";
//                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit();
//                editor.putString("USERNAME", origin);
//                editor.apply();
                Intent i = new Intent(HomeActivity.this, ScanQrPickUp.class);
                startActivity(i);
            }
        });



    }
}