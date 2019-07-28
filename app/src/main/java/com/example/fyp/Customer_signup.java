package com.example.fyp;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Customer_signup extends AppCompatActivity {
    EditText edit_user, edit_email, edit_adress, edit_phone, edit_password;
    Button Customer_Signup;
    ImageView arrow_btn;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    String userName, email, phoneNo, address, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_signup);
        edit_user = findViewById(R.id.userName);
        edit_email = findViewById(R.id.email);
        edit_phone = findViewById(R.id.phone);
        edit_adress = findViewById(R.id.address);
        edit_password = findViewById(R.id.password);
        Customer_Signup = findViewById(R.id.cus_signup);
        arrow_btn = findViewById(R.id.arrow_signup);
        Customer_Signup.setOnClickListener(onClickListener);
        progressBar = findViewById(R.id.progress_bar);
        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerInfo");
        firebaseAuth = FirebaseAuth.getInstance();
        arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Customer_signup.this, Customer_signin.class));
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Registration();
        }
    };

    private void Registration() {
        userName = edit_user.getText().toString();
        email = edit_email.getText().toString();
        phoneNo = edit_phone.getText().toString();
        address = edit_adress.getText().toString();
        password = edit_password.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            edit_user.setError("User name required");
            edit_user.requestFocus();
        }
        if (TextUtils.isEmpty(email)) {
            edit_email.setError("Email Required");
            edit_email.requestFocus();
        }
        if (TextUtils.isEmpty(password)) {
            edit_password.setError("Password required");
            edit_password.requestFocus();
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email.setError("Enter a valid email");
            edit_email.requestFocus();

        }
        if (TextUtils.isEmpty(phoneNo)) {
            edit_phone.setError("Mobile number Required");
            edit_phone.requestFocus();
        }
        if (TextUtils.isEmpty(address)) {
            edit_adress.setError("Address Required");
            edit_adress.requestFocus();
        }
        if (password.length() < 6) {
            edit_password.setError("Password greater than six character");
            edit_password.requestFocus();
        }
        Query query1 = FirebaseDatabase.getInstance().getReference().child("CustomerInfo").orderByChild("email").equalTo(email);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Toast.makeText(Customer_signup.this, "Email already exist", LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final CustomerInfo customerInfo = new CustomerInfo(userName, email, phoneNo, address, password);
                                FirebaseDatabase.getInstance().getReference("CustomerInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(customerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            sendEmailVerification();

                                        } else {
                                            Toast.makeText(Customer_signup.this, "User already Registered,please change your email", LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Customer_signup.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        Intent intent = new Intent(Customer_signup.this, Customer_signin.class);
                        startActivity(intent);


                    }
                }
            });
        }
    }

}
