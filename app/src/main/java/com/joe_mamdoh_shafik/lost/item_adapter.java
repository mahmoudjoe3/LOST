package com.joe_mamdoh_shafik.lost;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class item_adapter extends RecyclerView.Adapter<item_adapter.item_holder> {
    private Context context;
    private List<Items> ItemList;
    public item_adapter(Context context2,List<Items> ItemList2)
    {
        context=context2;
        ItemList=ItemList2;
    }
    @NonNull
    @Override
    public item_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.cardveiw,viewGroup,false);
        return new item_holder(v);
    }

    @Override
    public void onBindViewHolder( item_holder v, int i) {
Items item_current=ItemList.get(i);
v.lost_name.setText(item_current.getLostName());
v.name.setText(item_current.getName());
v.description.setText(item_current.getDescription());
v.phone.setText(item_current.getPhone());
v.url=item_current.getImageUrl();
v.cat.setText(item_current.getCategoryName());
        v.image.setImageBitmap(null);
        loadimage image1=new loadimage(v.image);
        image1.execute(v.url);

    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public class item_holder extends RecyclerView.ViewHolder{

public TextView lost_name;
public TextView name;
public TextView phone;
public TextView description;
public TextView cat;
public ImageView image;
public String url;
        public item_holder(@NonNull View itemView) {
            super(itemView);
            lost_name=itemView.findViewById(R.id.lost_name);
            name=itemView.findViewById(R.id.name);
            phone=itemView.findViewById(R.id.phone);
            description=itemView.findViewById(R.id.description);
            image=itemView.findViewById(R.id.image);
            cat=itemView.findViewById(R.id.catog);



        }
    }

    private class loadimage extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;
public loadimage (ImageView i)
{
    this.imageView=i;
}
        @Override
        protected Bitmap doInBackground(String... strings) {
    String url=strings[0];
    Bitmap bitmap=null;
            try {
                InputStream inputStream = new java.net.URL(url).openStream() ;
                bitmap= BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
    imageView.setImageBitmap(bitmap);

        }
    }
}
