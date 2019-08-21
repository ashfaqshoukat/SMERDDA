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
import com.example.fyp.Adapters.ProductListAdapter;
import com.example.fyp.Extras.PreferanceFile;
import com.example.fyp.Models.DETAILPRODUCT;
import com.example.fyp.Models.ORDERINFO;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class ShopActivity extends AppCompatActivity {
    ImageView imageView;
    TextView txtName, txtPrice;
    ElegantNumberButton numberButton;
    DatabaseReference databaseReference, databaseReference1;
    Button button;
    TextView txt;
    String Pro_Name ;
    String Pro_Price;
    String image;
    private String productId;
    private String comapanyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        imageView = findViewById(R.id.getImage);
        txtName = findViewById(R.id.getName);
        txtPrice = findViewById(R.id.getPrice);
        numberButton = findViewById(R.id.txt_count);
        button = findViewById(R.id.cartBtn);
        txt = findViewById(R.id.txtMsg);
//        databaseReference = FirebaseDatabase.getInstance().getReference("GALLERY").child("DETAILPRODUCT");
//        databaseReference1 = FirebaseDatabase.getInstance().getReference("ConfirmOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        IncomingIntent();
        button.setOnClickListener(v -> {
//            addToCartList();
//            ConfirmProduct();
            if(PreferanceFile.getInstance(getApplicationContext()).isIsCompany())
            {
                if(PreferanceFile.getInstance(getApplicationContext()).getCompany().getCompanyId().equals(comapanyId)){
                    Toast.makeText(this, "You cannot place the order", Toast.LENGTH_SHORT).show();

                }
                else{
                    GetData();
                }
            }
            else {
                GetData();
            }
        });
//        CheckOrderState();
    }

    private void CheckOrderState() {
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("cname").getValue().toString();
                    if (shippingState.equals("shipped")) {
                        txtPrice.setText("Dear " + userName + "\n order is shipped successfully");
                        imageView.setVisibility(View.GONE);
                        txtName.setVisibility(View.GONE);
                        numberButton.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
//                        txt.setVisibility(View.VISIBLE);
                    } else if (shippingState.equals("not shipped")) {
                        txtPrice.setText("Shipping State=Not Shipped");
                        imageView.setVisibility(View.GONE);
                        txtName.setVisibility(View.GONE);
                        numberButton.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
                        txt.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ShopActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
        ORDERINFO orderinfo=new ORDERINFO();
        orderinfo.setCompanyId(comapanyId);
        orderinfo.setProductId(productId);
        orderinfo.setProductImage(image);
        orderinfo.setProductName(Pro_Name);
        orderinfo.setProductPrice(Integer.parseInt(Pro_Price));
        orderinfo.setProductQty(Integer.parseInt(numberButton.getNumber()));

        String title = txtName.getText().toString();
        String price = txtPrice.getText().toString();
        Intent intent = new Intent(ShopActivity.this, ConfirmActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("price", price);
        Gson gson=new Gson();
        String order=gson.toJson(orderinfo);
        intent.putExtra("orderinfo",order);
        startActivity(intent);

    }


    private void ConfirmProduct() {
        Intent intent = new Intent(ShopActivity.this, ConfirmActivity.class);
        DETAILPRODUCT detail1 = new DETAILPRODUCT();
        intent.putExtra("quantity", detail1.getQuantity());
        startActivity(intent);
    }

    private void addToCartList() {
        String numberBtn = numberButton.getNumber();
        DETAILPRODUCT detailProduct = new DETAILPRODUCT(numberBtn);
        String id = databaseReference.push().getKey();
        databaseReference.child(id).setValue(detailProduct);
    }

    private void IncomingIntent() {

             Pro_Name = getIntent().getStringExtra("product_title");
             Pro_Price = getIntent().getStringExtra("product_price");
            image = getIntent().getStringExtra("product_image");
            productId=getIntent().getStringExtra("product_id");
            comapanyId=getIntent().getStringExtra("company_id");
            setProducts(Pro_Name, Pro_Price, image);

    }

    private void setProducts(String Pro_Name, String Pro_Price, String image) {
        txtName.setText(Pro_Name);
        txtPrice.setText(Pro_Price);
        Picasso.get().load(image).placeholder(getDrawable(R.drawable.image)).into(imageView);
    }
}
