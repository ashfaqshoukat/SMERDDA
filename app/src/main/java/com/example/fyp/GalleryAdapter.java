package com.example.fyp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageViewHolder> {
    private Context mContext;
    private ArrayList<Gallery> mGallery;
    public GalleryAdapter(Context context,List<Gallery> galleryList){
        mContext=context;
        mGallery= (ArrayList<Gallery>) galleryList;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.gallery_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Gallery galleryCur=mGallery.get(position);
        holder.prod_name.setText(galleryCur.getProduct_title());
        holder.prod_price.setText(galleryCur.getProduct_price());
        Picasso.get().load(galleryCur.getProduct_image()).into(holder.prod_img);
       // Picasso.with(mContext).load(galleryCur.getProduct_image()).into(holder.prod_img);
               // .fit().centerCrop().into(holder.prod_img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,ShopActivity.class);
                intent.putExtra("product_title",galleryCur.getProduct_title());
                intent.putExtra("product_price",galleryCur.getProduct_price());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGallery.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
         public TextView prod_name,prod_price;
        public ImageView prod_img;
       public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            prod_name= itemView.findViewById(R.id.product_name);
            prod_price= itemView.findViewById(R.id.price);
            prod_img= itemView.findViewById(R.id.image_product);
        }
    }
}
