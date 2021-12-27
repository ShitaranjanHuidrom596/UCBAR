package com.example.ucbarprinting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username_txt,password_txt;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username_txt=findViewById(R.id.username_txt);
        password_txt=findViewById(R.id.password_txt);
        button=findViewById(R.id.login_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username_txt.getText().toString().equals("admin")&& password_txt.getText().toString().equals("print")){
                    Intent myIntent = new Intent(MainActivity.this, SelectSize.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(myIntent);
                }else{
                    Toast.makeText(getApplicationContext(),"Username and Password doesnt match",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}