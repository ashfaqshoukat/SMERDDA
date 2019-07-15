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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.myHolder> {
    Context context;
    ArrayList<CompanyInfo> list;

    public AdapterUsers(Context context, List<CompanyInfo> list) {
        this.context = context;
        this.list = (ArrayList<CompanyInfo>) list;
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
        String companyName = list.get(i).getCompName();
        myHolder.textView.setText(companyName);
        myHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,CompanyDetailActivity.class);
//                Type type = new TypeToken<List<CompanyInfo>>() {}.getType();
                Gson gson=new Gson();
                String data=gson.toJson(list.get(i));
//                String listDta=gson.toJson(list,type);
                intent.putExtra("comapnyinfo",data);
//                intent.putExtra("comapnyinfolist",listDta);
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

        public myHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgIv);
            textView = itemView.findViewById(R.id.nameTv);
        }
    }
}
