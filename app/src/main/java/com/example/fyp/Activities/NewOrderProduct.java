package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class NewOrderProduct extends AppCompatActivity {
    ImageView imageView;
    TextView t1,t2,t3,t4;
    DatabaseReference db_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_product);
        t1=(TextView)findViewById(R.id.show_pname);
        t2=(TextView)findViewById(R.id.showp_price);
        t3=(TextView)findViewById(R.id.showp_quantity);
        t4=(TextView)findViewById(R.id.showp_total);
        db_ref= FirebaseDatabase.getInstance().getReference("CustomerInfo").child("Products");
        CheckProducts();
    }
    private void CheckProducts(){
        String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        db_ref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    //ProductsModel productsModel=dataSnapshot1.getValue(ProductsModel.class);
                    //String name=productsModel.getP_name();
                    //String price=productsModel.getP_price();
                    //String quantity=productsModel.getP_quantity();
                    //String total=productsModel.getTotal_quantity();
                    String name=dataSnapshot1.child("p_name").getValue(String.class);
                    String price=dataSnapshot1.child("p_price").getValue(String.class);
                    String quantity=dataSnapshot1.child("p_quantity").getValue(String.class);
                    String total=dataSnapshot1.child("total_quantity").getValue(String.class);
                    t1.setText(name);
                    t2.setText(price);
                    t3.setText(quantity);
                    t4.setText(total);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
