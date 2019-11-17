package com.joe_mamdoh_shafik.lost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText editTextMobile;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextMobile = (EditText) findViewById(R.id.phone_txt);
    }

    public void login(View view) {

        String mobile = editTextMobile.getText().toString().trim();

        if(mobile.isEmpty() || mobile.length() < 10){
            editTextMobile.setError("Enter a valid mobile");
            editTextMobile.requestFocus();
            return;
        }
        Intent intent = new Intent(login.this, main_page.class);
        intent.putExtra("mobile", mobile);
        startActivity(intent);
    }

}
