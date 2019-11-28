package com.joe_mamdoh_shafik.lost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class login extends AppCompatActivity {

    private EditText editTextMobile;
    SharedPreferences preferences;
    String firstTime;
    private Spinner CountrySpinner;
    private DatabaseReference Phone_Table;
    ArrayList<String> CountryCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Phone_Table = FirebaseDatabase.getInstance().getReference("PhoneNumbers");

        CountrySpinner = (Spinner) findViewById(R.id.country_list);
        ArrayAdapter<CharSequence> CountryName =
                ArrayAdapter.createFromResource(this,R.array.Country_Name,android.R.layout.simple_spinner_item);
        CountryName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CountrySpinner.setAdapter(CountryName);
        CountryCode = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.country_code)));
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


        final String mobile = "+" + CountryCode.get(CountrySpinner.getSelectedItemPosition()) + editTextMobile.getText().toString().trim();
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
        Phone_Table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean Found = false;
                for(DataSnapshot PhoneSnapShot : dataSnapshot.getChildren())
                {
                    if(mobile.equals(PhoneSnapShot.getValue()))
                    {
                        Found = true;
                        Intent intent = new Intent(login.this, main_page.class);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                    }
                }
                if(Found == false)
                {
                    Intent intent = new Intent(login.this, Verification.class);
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
