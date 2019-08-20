package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fyp.Adapters.ProductListAdapter;
import com.example.fyp.Models.GALLERY;
import com.example.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompanyProductActivity extends AppCompatActivity {

    private List<GALLERY> list=new ArrayList<>();
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_product);
        String id=getIntent().getStringExtra("id");
        String name=getIntent().getExtras().getString("name");
        recyclerView=findViewById(R.id.recyclerView);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);

        getAllProducts(id);
    }
    private void getAllProducts(String id) {
        FirebaseDatabase.getInstance().getReference().child("Gallery").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    GALLERY gallery=dataSnapshot1.getValue(GALLERY.class);
                    list.add(gallery);
                }

                setUpRecyclerview();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecyclerview() {
        ProductListAdapter productListAdapter=new ProductListAdapter(CompanyProductActivity.this,list);
        recyclerView.setAdapter(productListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CompanyProductActivity.this));
    }
}
