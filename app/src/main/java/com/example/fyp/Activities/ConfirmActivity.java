package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.fyp.Models.DETAILPRODUCT;
import com.example.fyp.Models.GALLERY;
import com.example.fyp.Models.ORDERINFO;
import com.example.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class ConfirmActivity extends AppCompatActivity {
    TextView txt_name,txt_price,txt_quan,txt_total;
    Button button_confirm;
    DatabaseReference databaseReference,databaseReference1;
    int overTotalPrice=0;
    ORDERINFO orderinfo;
    String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        order=getIntent().getStringExtra("orderinfo");
        Gson gson=new Gson();
        orderinfo=gson.fromJson(order,ORDERINFO.class);
        txt_name= findViewById(R.id.name);
        txt_price= findViewById(R.id.pricepro);
        txt_quan= findViewById(R.id.quantity);
        button_confirm= findViewById(R.id.confirm);
        txt_total= findViewById(R.id.totalPrice);
        txt_name.setText(orderinfo.getProductName());
        txt_price.setText("$"+orderinfo.getProductPrice()+"");
        txt_total.setText("$"+orderinfo.getProductPrice()*orderinfo.getProductQty()+"");
        txt_quan.setText(orderinfo.getProductQty()+"");
//        GetDetails();
//        GetAllDetails();
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height=metrics.heightPixels;
        int width=metrics.widthPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.5));
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //txt_total.setText(String.valueOf(overTotalPrice));
//                TotalPrice();
                Intent intent=new Intent(ConfirmActivity.this, AddressDetail.class);
               // intent.putExtra("Total_price",String.valueOf(overTotalPrice));
                intent.putExtra("orderinfo",order);
                startActivity(intent);
                finish();
            }
        });
    }
    private void TotalPrice() {
        GALLERY gallery=new GALLERY();
        DETAILPRODUCT detail=new DETAILPRODUCT();
        if (gallery.getProduct_price()!=null && detail.getQuantity()!=null){
            int total_Price=(Integer.parseInt(gallery.getProduct_price()))*(Integer.parseInt(detail.getQuantity()));
            overTotalPrice=overTotalPrice+total_Price;
        }
    }
    private void GetDetails() {
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    DETAILPRODUCT product=dataSnapshot1.getValue(DETAILPRODUCT.class);
                    String quantity=product.getQuantity();
                    txt_quan.setText(quantity);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetAllDetails(){
       Bundle bundle=getIntent().getExtras();
      String data1= bundle.getString("title");
       String data2 =bundle.getString("price");
txt_name.setText(data1);
txt_price.setText(data2);
    }




}
