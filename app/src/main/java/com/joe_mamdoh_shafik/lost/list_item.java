package com.joe_mamdoh_shafik.lost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class list_item extends Fragment implements AdapterView.OnItemSelectedListener {
    View myveiw;
    private RecyclerView mrecycleview;
    private item_adapter ma;
    Menu menu;
    MenuItem menuItem;
    private DatabaseReference databaseReference;
    public List<Items> ItemList;
public Spinner  spinner ;
    public String CAT_NAME;
    String selceted_item="item1";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       myveiw = inflater.inflate(R.layout.list_item,container,false);

        spinner=myveiw.findViewById(R.id.spinner_search);
        mrecycleview=myveiw.findViewById(R.id.list_of_loses_things);
        mrecycleview.setHasFixedSize(true);
        mrecycleview.setLayoutManager(new LinearLayoutManager(myveiw.getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference("Items");
        ItemList = new ArrayList<>();
        ArrayAdapter<CharSequence> catogery_adapter;
       catogery_adapter= ArrayAdapter.createFromResource(getActivity(),R.array.catogeryarray2,R.layout.spinner);
        catogery_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

          try{  spinner.setAdapter(catogery_adapter);
              spinner.setOnItemSelectedListener(this);
          }catch(Exception e){ Toast.makeText(getActivity(),e.getMessage() , Toast.LENGTH_SHORT).show();
          }




          spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                  if(parent.getItemAtPosition(position).equals("All"))
                  {
                      databaseReference.addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                              ItemList.clear();
                              for(DataSnapshot ItemSnapShot : dataSnapshot.getChildren())
                              {
                                  Items item = ItemSnapShot.getValue(Items.class);
                                      ItemList.add(item);
                              }
                              //Right Your Code Here Using *ItemList*
                              ma=new item_adapter(myveiw.getContext(),ItemList);
                              mrecycleview.setAdapter(ma);
                              //*//*//*//*//*//*//*//*//*//*//*//*//
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }

                      });
                  }
                  else {
                      databaseReference.addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                              ItemList.clear();
                              for (DataSnapshot ItemSnapShot : dataSnapshot.getChildren()) {
                                  Items item = ItemSnapShot.getValue(Items.class);
                                  if (item.getCategoryName().equals(spinner.getSelectedItem().toString())) {
                                      ItemList.add(item);
                                  }

                              }
                              //Right Your Code Here Using *ItemList*
                              ma = new item_adapter(myveiw.getContext(), ItemList);
                              mrecycleview.setAdapter(ma);
                              //*//*//*//*//*//*//*//*//*//*//*//*//
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }

                      });
                  }
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
          });

        return myveiw;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

       //Toast.makeText(getActivity(),spinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
