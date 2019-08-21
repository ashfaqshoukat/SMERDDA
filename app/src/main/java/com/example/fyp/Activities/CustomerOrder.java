package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fyp.Adapters.OrderListAdapter;
import com.example.fyp.Extras.PreferanceFile;
import com.example.fyp.Models.ORDERINFO;
import com.example.fyp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CustomerOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Query databaseReference2;
    TextView orderdetail;
    private List<ORDERINFO> orderList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerOrder.this));
        databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        String uid = FirebaseAuth.getInstance().getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    for (DataSnapshot myOrder : children.getChildren()) {
                        ORDERINFO orderinfo = myOrder.getValue(ORDERINFO.class);
                        if (orderinfo.getUserId().equals(uid))
                            if(orderinfo.getStatus()!=3 && orderinfo.getStatus()!=-1)
                            orderList.add(orderinfo);
                    }

                }
                initailizeRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initailizeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerOrder.this));
        recyclerView.setAdapter(new OrderListAdapter(CustomerOrder.this, orderList));

    }
}

