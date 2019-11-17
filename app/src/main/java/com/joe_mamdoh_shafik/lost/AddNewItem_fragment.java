package com.joe_mamdoh_shafik.lost;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.zip.Inflater;


public class AddNewItem_fragment extends Fragment implements AdapterView.OnItemSelectedListener, LocationListener {
    View myView;
    //my variables
    private String FilePathUri;
    public StorageReference Folder;
    private ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference table_items;
    Items item=new Items();
    //**//

    private static final int CAM_REQUIST = 1313;
    private static  final int LOCATION_PERMISSION_CODE =1;

    TextView loc,cat;
    Button take_picture_BTN, getlocation_BTN, add_BTN;
    EditText user_txt, phone_txt, itemName_txt, itemDesc_txt;
    ImageView item_pic_IMG;
    // item data
    Bitmap item_pic_Bitmp=null;
    String username, phone, item_catogery, item_location_lat_long="", item_name, item_description;
    //item data
    Spinner spinner;
    ArrayAdapter<CharSequence> catogery_adapter;

    private LocationManager locationManager;
    Location location;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.add_item_fragment,container,false);

        //my declarations
        Folder = FirebaseStorage.getInstance().getReference().child("ImageFolder");

        database = FirebaseDatabase.getInstance();
        table_items = database.getReference("Items");


        //**//



        init();

        catogery_adapter=ArrayAdapter.createFromResource(getActivity(),R.array.catogeryarray,android.R.layout.simple_spinner_item);
        catogery_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(catogery_adapter);
        spinner.setOnItemSelectedListener(this);
        return myView;
    }

    private void init()
    {
        loc=myView.findViewById(R.id.loc);
        cat=myView.findViewById(R.id.cat);
        getlocation_BTN=myView.findViewById(R.id.getlocation);
        getlocation_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                       // Toast.makeText(getActivity(), "You have already granted this permission!", Toast.LENGTH_SHORT).show();
                        location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                        onLocationChanged(location);
                        Toast.makeText(getActivity(), item_location_lat_long, Toast.LENGTH_SHORT).show();
                    }else
                        requestLocationPermission();
                }

                if(item_location_lat_long!="")
                {
                    getlocation_BTN.setBackgroundResource(R.drawable.signup_button_shape);
                    getlocation_BTN.setText("LOCATION LOCATED");
                }
            }
        });
        add_BTN=myView.findViewById(R.id.Add);
        add_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignment();
            }
        });
        take_picture_BTN=myView.findViewById(R.id.take_picture);
        take_picture_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cam_show=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cam_show,CAM_REQUIST);
            }
        });
        spinner=myView.findViewById(R.id.catogeryspiner);
        item_pic_IMG=myView.findViewById(R.id.item_pic);
        item_pic_IMG.setImageResource(R.drawable.camera);
        user_txt=myView.findViewById(R.id.username);
        phone_txt=myView.findViewById(R.id.phoneNumber);
        phone_txt.setText(getActivity().getIntent().getStringExtra("phone"));
        itemName_txt=myView.findViewById(R.id.itemname);
        itemDesc_txt=myView.findViewById(R.id.description);
    }

    //for camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAM_REQUIST) {
            item_pic_Bitmp = (Bitmap) data.getExtras().get("data");
            item_pic_IMG.setImageBitmap(item_pic_Bitmp);
            item_pic_IMG.setMinimumWidth(400);
            item_pic_IMG.setMinimumHeight(600);
        }
    }



    private void clearContainer() {
        item_location_lat_long="";
        item_catogery="";
        item_pic_IMG.setImageResource(R.drawable.camera);
        itemName_txt.setText("");
        itemDesc_txt.setText("");
        if(item_location_lat_long=="")
        {
            getlocation_BTN.setBackgroundColor(getResources().getColor(R.color.gray));
            getlocation_BTN.setText("GET LOCATION");
        }
    }

    public void assignment()
    {

        username=user_txt.getText().toString();
        phone=phone_txt.getText().toString();
        item_name=itemName_txt.getText().toString();
        item_description=itemDesc_txt.getText().toString();

        if (username != "" && phone != "" && item_catogery != "" && item_location_lat_long != "" && item_pic_Bitmp != null && item_name != "") {
            //////////////////////////////////////////////////////////////////////////////////////////////fire base code
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            item_pic_Bitmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte [] data = baos.toByteArray();
            final StorageReference ImageName = Folder.child(data.toString());
            ImageName.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {

                            //SaveAllItemDetails
                            progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Please Waiting...");
                            progressDialog.show();



                            final String key = table_items.push().getKey();
                            table_items.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    progressDialog.dismiss();
                                    item = new Items(cat.getText().toString(),username,phone,item_name,loc.getText().toString(),String.valueOf(uri),item_description);
                                    table_items.child(key).setValue(item);
                                    Toast.makeText(getActivity(), item_name+" Added successfully", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getActivity(),databaseError.getMessage() , Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////fire base code

            clearContainer();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("UN COMPLETE RESOURCE")
                    .setMessage("Messing field, please fill the missing field!")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        item_catogery=adapterView.getItemAtPosition(position).toString();
        cat.setText(item_catogery);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //////////////////////////////////////////////////////////////////////////////////location
    @Override
    public void onLocationChanged(Location location) {
        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        item_location_lat_long=(latitude+":"+longitude);
        loc.setText(item_location_lat_long);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }



    /////////////////////////////////////////////////////////////////////////////////////location


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////location

    //////////////////////////////////////////////////////////////////////////////////////////location
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)||ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to get the item location")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////location
}



