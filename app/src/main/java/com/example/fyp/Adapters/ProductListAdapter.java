package com.example.fyp.Adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.Activities.ShopActivity;
import com.example.fyp.Models.GALLERY;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ImageViewHolder> {
    private Context mContext;
    private ArrayList<GALLERY> mGallery;
    public ProductListAdapter(Context context, List<GALLERY> galleryList){
        mContext=context;
        mGallery= (ArrayList<GALLERY>) galleryList;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.productitem,parent,false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        GALLERY galleryCur=mGallery.get(position);
        holder.prod_name.setText(galleryCur.getProduct_title());
        holder.prod_price.setText("$"+galleryCur.getProduct_price());
        String image=galleryCur.getProduct_image();
        // String url="https://firebasestorage.googleapis.com/v0/b/smerda-344e3.appspot.com/o?name=ImageGallery%2F1562920350396.jpg&uploadType=resumable&upload_id=AEnB2Ure0oCg2EGnm8JfUyCLdL56EbQAnddxmysm_4f38qrfwD-bqsDd6qZnao-hxxCunqUaLegBzlzUofkeNvA6T8rYQiUTWw&upload_protocol=resumable";
        Picasso.get().load(image).into(holder.prod_img);
        int currentposition=position;
        GALLERY info=mGallery.get(currentposition);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ShopActivity.class);
                intent.putExtra("product_title",galleryCur.getProduct_title());
                intent.putExtra("product_price",galleryCur.getProduct_price());
                intent.putExtra("product_description",galleryCur.getProduct_description());
                intent.putExtra("product_image",image);
                intent.putExtra("product_id",galleryCur.getProduct_id());
                intent.putExtra("company_id",galleryCur.getCompany_id());
                mContext.startActivity(intent);
            }
        });
       /* holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeItem(info);

                return true;
            }

            private void removeItem(GALLERY info) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Gallery");
                AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setMessage("Do you want to delete this product?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int currentPosition=mGallery.indexOf(info);
                                mGallery.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference.child(uid).removeValue();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.setTitle("Confirm");
                dialog.show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mGallery.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
         public TextView prod_name,prod_price,prod_des;
        public ImageView prod_img;
       public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            prod_name= itemView.findViewById(R.id.product_name);
            prod_price= itemView.findViewById(R.id.price);
            prod_img= itemView.findViewById(R.id.image_product);
           prod_des=(TextView)itemView.findViewById(R.id.product_des);
        }
    }
}
