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

import com.example.fyp.Activities.CompanyDetailActivity;
import com.example.fyp.Models.COMPANYINFO;
import com.example.fyp.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.myHolder> {
    Context context;
    ArrayList<COMPANYINFO> list;


    public CompanyAdapter(Context context, List<COMPANYINFO> list) {
        this.context = context;
        this.list = (ArrayList<COMPANYINFO>) list;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.company_profile_tab, viewGroup, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        COMPANYINFO company = list.get(i);
        myHolder.textView.setText(company.getCompName());
        if(!company.getProfileimage().equalsIgnoreCase("")){
            Picasso.get().load(company.getProfileimage()).into(myHolder.imageView);
        }

        myHolder.itemLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CompanyDetailActivity.class);
                Gson gson = new Gson();
                String data = gson.toJson(list.get(i));
                intent.putExtra("comapnyinfo", data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout itemLyt;

        public myHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgIv);
            textView = itemView.findViewById(R.id.nameTv);
            itemLyt = itemView.findViewById(R.id.itemLyt);
        }
    }
}
