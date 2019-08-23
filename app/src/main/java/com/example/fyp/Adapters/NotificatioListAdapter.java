package com.example.fyp.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.Activities.ShopActivity;
import com.example.fyp.Models.GALLERY;
import com.example.fyp.Models.NOTIFICATIONS;
import com.example.fyp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotificatioListAdapter extends RecyclerView.Adapter<NotificatioListAdapter.ImageViewHolder> {
    private Context mContext;
    private List<NOTIFICATIONS> mGallery;
    public NotificatioListAdapter(Context context, List<NOTIFICATIONS> galleryList){
        mContext=context;
        mGallery=  galleryList;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        NOTIFICATIONS galleryCur=mGallery.get(position);
        holder.txtTitle.setText(galleryCur.getTitle());
        holder.txtDate.setText(galleryCur.getTime());
        Picasso.get().load(galleryCur.getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mGallery.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView txtTitle;
        public TextView txtDate;
        public ImageView image;
       public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
           root = itemView.findViewById(R.id.list_root);
           txtTitle = itemView.findViewById(R.id.list_title);
           txtDate = itemView.findViewById(R.id.txtDate);
           image = itemView.findViewById(R.id.image);
        }
    }
}
