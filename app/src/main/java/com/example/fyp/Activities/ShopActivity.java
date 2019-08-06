package com.example.fyp.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.fyp.Models.DETAILPRODUCT;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShopActivity extends AppCompatActivity {
ImageView imageView;
TextView txtName,txtPrice;
ElegantNumberButton numberButton;
DatabaseReference databaseReference,databaseReference1;
Button button;
TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        imageView= findViewById(R.id.getImage);
        txtName= findViewById(R.id.getName);
        txtPrice= findViewById(R.id.getPrice);
        numberButton= findViewById(R.id.txt_count);
        button= findViewById(R.id.cartBtn);
        txt= findViewById(R.id.txtMsg);
        databaseReference=FirebaseDatabase.getInstance().getReference("GALLERY").child("DETAILPRODUCT");
        databaseReference1=FirebaseDatabase.getInstance().getReference("ConfirmOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        IncomingIntent();
       button.setOnClickListener(v -> {
           addToCartList();
           ConfirmProduct();
           GetData();
           //Data();
       });
       CheckOrderState();
    }

    private void CheckOrderState() {
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState=dataSnapshot.child("state").getValue().toString();
                    String userName=dataSnapshot.child("cname").getValue().toString();
                    if (shippingState.equals("shipped")){
                        txtPrice.setText("Dear "+ userName +"\n order is shipped successfully");
                        imageView.setVisibility(View.GONE);
                        txtName.setVisibility(View.GONE);
                        numberButton.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
                        txt.setVisibility(View.VISIBLE);
                    }
                    else if (shippingState.equals("not shipped")){
                        txtPrice.setText("Shipping State=Not Shipped");
                        imageView.setVisibility(View.GONE);
                        txtName.setVisibility(View.GONE);
                        numberButton.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
                        txt.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(ShopActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /* private void Data(){
         //GALLERY gallery=new GALLERY();
         Intent intent=new Intent(ShopActivity.this,ConfirmActivity.class);
         //intent.putExtra("product_title",gallery.getProduct_title());
         //intent.putExtra("product_price",gallery.getProduct_price());
         startActivity(intent);
     }*/
   private void GetData() {
        String title=txtName.getText().toString();
        String price=txtPrice.getText().toString();
       Intent intent=new Intent(ShopActivity.this, ConfirmActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("price",price);
        startActivity(intent);
    }


    private void ConfirmProduct() {
        Intent intent=new Intent(ShopActivity.this,ConfirmActivity.class);
        DETAILPRODUCT detail1=new DETAILPRODUCT();
        intent.putExtra("quantity",detail1.getQuantity());
        startActivity(intent);
    }

    private void addToCartList(){
        String numberBtn=numberButton.getNumber();
        DETAILPRODUCT detailProduct=new DETAILPRODUCT(numberBtn);
        String id=databaseReference.push().getKey();
        databaseReference.child(id).setValue(detailProduct);
    }
    private void IncomingIntent(){
        if (getIntent().hasExtra("product_title")&&getIntent().hasExtra("product_price")){
            String Pro_Name=getIntent().getStringExtra("product_title");
            String Pro_Price=getIntent().getStringExtra("product_price");
            setProducts(Pro_Name,Pro_Price);
        }
    }
    private void setProducts(String Pro_Name,String Pro_Price){
txtName.setText(Pro_Name);
txtPrice.setText(Pro_Price);
    }
}
