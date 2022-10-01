package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.api.WashService;
import com.example.admin.context.GlobalContext;
import com.example.admin.utils.HttpStatus;
import com.example.admin.utils.MyRunnable;

public class DetailsPageDuringPickUp extends AppCompatActivity {

    private TextView name, mobile, roll, block, room, unittv, tokentv, error, units, token;
    private Button addWashButton;
    WashService washService;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page_during_pick_up);
        name=findViewById(R.id.nameField);
        mobile=findViewById(R.id.phoneField);
        roll=findViewById(R.id.regnoField);
        block=findViewById(R.id.blockField);
        room=findViewById(R.id.roomField);
        units=findViewById(R.id.wash_quantity1);
        token=findViewById(R.id.wash_token1);
        unittv=findViewById(R.id.wash_quantity);
        tokentv=findViewById(R.id.wash_token);
        addWashButton=findViewById(R.id.add_wash_btn);
        error=findViewById(R.id.error_text);
        progressDialog = new ProgressDialog(this);
        washService = new WashService();

        name.setText(GlobalContext.currentOnlineUser.getName());
        mobile.setText(GlobalContext.currentOnlineUser.getMobile());
        roll.setText("Reg No : " + GlobalContext.currentOnlineUser.getRoll());
        block.setText("Block: " + GlobalContext.currentOnlineUser.getBlock());
        room.setText("Room : " + GlobalContext.currentOnlineUser.getRoom());

        if (GlobalContext.washes.size() == 0 ||
                !GlobalContext.washes.get(0).getStatus().equals("IN_PROGRESS")) {
            error.setText("Error: No Deposited Wash Found for Student");
            error.setVisibility(View.VISIBLE);
            units.setVisibility(View.INVISIBLE);
            token.setVisibility(View.INVISIBLE);
            unittv.setVisibility(View.INVISIBLE);
            tokentv.setVisibility(View.INVISIBLE);
            addWashButton.setVisibility(View.INVISIBLE);
            return;
        }

        addWashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                washService.pickupWash(GlobalContext.currentOnlineUser.getId(),
                        new MyRunnable() {
                            @Override
                            public void run() {
                                if (HttpStatus.SC_OK == statusCode) {
                                    Toast.makeText(DetailsPageDuringPickUp.this, "Wash picked up by student successfully", Toast.LENGTH_LONG).show();

                                    startActivity(new Intent(DetailsPageDuringPickUp.this, HomeActivity.class));
                                } else {
                                    Toast.makeText(DetailsPageDuringPickUp.this, "Error : " + jsonResponse, Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });

    }
}