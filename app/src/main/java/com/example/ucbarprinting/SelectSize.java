package com.example.ucbarprinting;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectSize extends AppCompatActivity {

    int backcount=0;
    Button dt50x90;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_size);

        dt50x90=findViewById(R.id.dt50x90);

        dt50x90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), DT50X90.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backcount++;
        if(backcount==2){
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectSize.this);
            builder.setTitle(R.string.app_name);

            builder.setMessage("Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            Toast.makeText(getApplicationContext(),"Press the Back button again to exit the application",Toast.LENGTH_LONG).show();
        }
    }
}