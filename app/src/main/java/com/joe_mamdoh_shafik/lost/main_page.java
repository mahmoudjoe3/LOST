package com.joe_mamdoh_shafik.lost;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class main_page extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    BottomNavigationView navView;
    Spinner spinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new list_item()).commit();
         spinner=findViewById(R.id.spinner_search);
        ArrayAdapter<CharSequence> catogery_adapter;
        catogery_adapter= ArrayAdapter.createFromResource(this,R.array.catogeryarray2,android.R.layout.simple_spinner_item);
        catogery_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        try{  spinner.setAdapter(catogery_adapter);
            spinner.setOnItemSelectedListener(this);
        }catch(Exception e){
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selctedfragment=null;
            switch (item.getItemId()) {
                case R.id.additem:
                    selctedfragment = new AddNewItem_fragment();
                    break;
                case R.id.home:
                    selctedfragment = new list_item();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selctedfragment).commit();
            return true;
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        /*Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new list_item()).commit();*/

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
