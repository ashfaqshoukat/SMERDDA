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
import android.widget.Toast;

import com.example.fyp.Activities.CompanyProfile;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShippedMsg extends AppCompatActivity {
    TextView txt;
    DatabaseReference databaseReference;
    Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipped_msg);

        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height=metrics.heightPixels;
        int width=metrics.widthPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.4));
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);
        button1=(Button)findViewById(R.id.ok);
        txt=(TextView)findViewById(R.id.txtMsg);
        databaseReference= FirebaseDatabase.getInstance().getReference("ConfirmOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        CheckOrder();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ShippedMsg.this,CompanyDetailActivity.class));
            }
        });
    }
    private void CheckOrder() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState=dataSnapshot.child("state").getValue().toString();
                    String userName=dataSnapshot.child("cname").getValue().toString();
                    String last=dataSnapshot.child("lastName").getValue().toString();
                    if (shippingState.equals("shipped")){
                        txt.setText("Dear "+ userName +last +"\n order is shipped successfully");
                    }
                    else {
                        Toast.makeText(ShippedMsg.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
