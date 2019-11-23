package com.joe_mamdoh_shafik.lost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText editTextMobile;
    private FirebaseAuth mAuth;
    SharedPreferences preferences;
    String firstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forFirstTime();


        editTextMobile = (EditText) findViewById(R.id.phone_txt);
    }

    public void forFirstTime()
    {
        preferences=getSharedPreferences("PREFERENCE",MODE_PRIVATE);
         firstTime=preferences.getString("first_time_install","");
        if(firstTime.equals("yes")){
            Intent intent = new Intent(login.this, main_page.class);
            startActivity(intent);
        }
    }



    public void login(View view) {

        String mobile = editTextMobile.getText().toString().trim();

        if(mobile.isEmpty() || mobile.length() < 10){
            editTextMobile.setError("Enter a valid mobile");
            editTextMobile.requestFocus();

            return;
        }
        //store yes for first time he login
        if(!firstTime.equals("yes")){
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("first_time_install","yes");
            editor.apply();
        }
        Intent intent = new Intent(login.this, main_page.class);
        intent.putExtra("mobile", mobile);
        startActivity(intent);
    }

}
