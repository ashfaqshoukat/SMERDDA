package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddressDetail extends AppCompatActivity {
    EditText edit_name,edit_phone,edit_address,edit_email;
String total_Amount="",state="Normal";
Button btn;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);
        edit_name= findViewById(R.id.your_name);
        edit_phone= findViewById(R.id.your_number);
        edit_address= findViewById(R.id.your_email);
        edit_email= findViewById(R.id.your_address);
        btn= findViewById(R.id.btn);
        databaseReference= FirebaseDatabase.getInstance().getReference("ConfirmOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        total_Amount=getIntent().getStringExtra("Total_price");
        Toast.makeText(AddressDetail.this,"Total Price = $" +total_Amount,Toast.LENGTH_LONG).show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidation();
                if (state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(AddressDetail.this,"You can purchased new products after shipped the previous product",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    protected void onStart(){
        super.onStart();
        CheckOrderState();
    }
    private void CheckOrderState() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState=dataSnapshot.child("state").getValue().toString();
                    if (shippingState.equals("shipped")){
                       state="Order Shipped";
                    }
                    else if (shippingState.equals("not shipped")){
                        state="Order Placed";

                    }
                    else {
                        Toast.makeText(AddressDetail.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void CheckValidation() {
        String name=edit_name.getText().toString();
        String phone=edit_phone.getText().toString();
        String address=edit_address.getText().toString();
        String email=edit_email.getText().toString();
        if (TextUtils.isEmpty(name)){
            edit_name.setError("Enter your name");
            edit_name.requestFocus();
        }
       else if (TextUtils.isEmpty(phone)){
            edit_phone.setError("Enter your Phone Number");
            edit_phone.requestFocus();
        }
       else if (TextUtils.isEmpty(address)){
            edit_address.setError("Enter your Address");
            edit_address.requestFocus();
        }
        else if (TextUtils.isEmpty(email)){
            edit_email.setError("Enter your email");
            edit_email.requestFocus();
        }
        else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        HashMap<String,Object> map=new HashMap<>();
        map.put("cname",edit_name.getText().toString());
        map.put("phoneNum",edit_phone.getText().toString());
        map.put("address",edit_address.getText().toString());
        map.put("email",edit_email.getText().toString());
        map.put("state","not shipped");
        databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("Gallery").child("DetailProduct").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(AddressDetail.this,"Your final order has been processed",Toast.LENGTH_SHORT).show();
                           Intent intent=new Intent(AddressDetail.this,CompanyProfile.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                       }
                        }
                    });
                }
            }
        });
    }


}
