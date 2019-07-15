package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Customer_forgot extends AppCompatActivity {
EditText editText;
Button button;
FirebaseAuth mAth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_forgot);
        editText= findViewById(R.id.email_reset);
        button= findViewById(R.id.btn_reset);
        mAth=FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           String userEmail=editText.getText().toString().trim();
           if (userEmail.equals("")){
               Toast.makeText(Customer_forgot.this,"Please enter your email address",Toast.LENGTH_LONG).show();
           }
           else{
               mAth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                  if (task.isSuccessful()){
                      Toast.makeText(Customer_forgot.this,"Password Reset email sent!",Toast.LENGTH_LONG).show();
                      finish();
                      startActivity(new Intent(Customer_forgot.this,Customer_signin.class));
                  }
                  else {
                      Toast.makeText(Customer_forgot.this,"Error sending email password.",Toast.LENGTH_LONG).show();
                  }
                   }
               });
           }
            }
        });

    }
}
