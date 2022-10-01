package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.api.WashService;
import com.example.admin.context.GlobalContext;
import com.example.admin.model.Wash;
import com.example.admin.utils.Constants;
import com.example.admin.utils.HttpStatus;
import com.example.admin.utils.MyRunnable;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class DetailsPageDuringDropOff extends AppCompatActivity {

    private TextView name, mobile, roll, block, room, unittv, tokentv, error;
    private EditText units, token;
    private Button addWashButton;
    WashService washService;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page_during_drop_off);
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

        try {
            Toast.makeText(DetailsPageDuringDropOff.this, "User : " +
                            Constants.OBJECT_MAPPER.writeValueAsString(GlobalContext.currentOnlineUser),
                    Toast.LENGTH_LONG).show();

            Toast.makeText(DetailsPageDuringDropOff.this, "Washes : " +
                            Constants.OBJECT_MAPPER.writeValueAsString(GlobalContext.washes),
                    Toast.LENGTH_LONG).show();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Validate washes here...
        if (GlobalContext.washes.size() > 0) {
            Wash latestWash=GlobalContext.washes.get(0);
            if (latestWash.getStatus().equals("IN_PROGRESS")) {
                error.setText("Error: Wash is already submitted by Student");
                error.setVisibility(View.VISIBLE);
                units.setVisibility(View.INVISIBLE);
                token.setVisibility(View.INVISIBLE);
                unittv.setVisibility(View.INVISIBLE);
                tokentv.setVisibility(View.INVISIBLE);
                addWashButton.setVisibility(View.INVISIBLE);
                return;
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // Check if wash belongs to same week.

                Date date = new Date(latestWash.getSubmitTime()*1000);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                int year = cal.get(Calendar.YEAR);

                Date date1 = new Date(Instant.now().toEpochMilli());
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date1);
                int week1 = cal.get(Calendar.WEEK_OF_YEAR);
                int year1 = cal.get(Calendar.YEAR);
                if(year1==year && week1==week) {
                    error.setText("Error: Student has exhausted Quota of 1 wash this week");
                    error.setVisibility(View.VISIBLE);
                    units.setVisibility(View.INVISIBLE);
                    token.setVisibility(View.INVISIBLE);
                    unittv.setVisibility(View.INVISIBLE);
                    tokentv.setVisibility(View.INVISIBLE);
                    addWashButton.setVisibility(View.INVISIBLE);
                    return;
                }
            }
        }

        addWashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                washService.submitWash(GlobalContext.currentOnlineUser.getId(),
                        token.getText().toString(),
                        Long.parseLong(units.getText().toString()),
                        new MyRunnable() {
                            @Override
                            public void run() {
                                if (HttpStatus.SC_OK == statusCode) {
                                    try {
                                        Wash wash = Constants.OBJECT_MAPPER.readValue(jsonResponse, Wash.class);
                                        Toast.makeText(DetailsPageDuringDropOff.this, "Wash submitted successfully", Toast.LENGTH_LONG).show();

                                        startActivity(new Intent(DetailsPageDuringDropOff.this, HomeActivity.class));
                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(DetailsPageDuringDropOff.this, "Error : " + jsonResponse, Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });

    }
}