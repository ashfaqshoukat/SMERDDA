package com.example.fyp.Adapters;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout root;
    public TextView txtTitle;
    public TextView txtDate;
    public ImageView image;

    public ViewHolder(View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.list_root);
        txtTitle = itemView.findViewById(R.id.list_title);
        txtDate = itemView.findViewById(R.id.txtDate);
        image = itemView.findViewById(R.id.image);
    }

    public void setTxtTitle(String string) {
        txtTitle.setText(string);
    }



    public void setTxtDate(String txtDat) {
        txtDate.setText(txtDat+"");
    }

    public void setImage(String imag) {
        Picasso.get().load(imag).into(image);
    }
}