package com.example.fyp.Activities;
import android.app.ProgressDialog;
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

import com.example.fyp.Models.CUSTOMERINFO;
import com.example.fyp.R;
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
    FirebaseAuth firebaseAuth;
    String userName, email, phoneNo, address, password;
    ProgressDialog proDialog;
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
        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerInfo");
        firebaseAuth = FirebaseAuth.getInstance();
        arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Customer_signup.this, Customer_signin.class));
                overridePendingTransition(R.anim.go_up, R.anim.go_down);
                finish();
            }
        });
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean result=Registration();
            if (result==true){
                proDialog=new ProgressDialog(Customer_signup.this);
                proDialog.setMessage("Account is creating..Wait");
                proDialog.setCancelable(false);
                proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                proDialog.show();
            }

        }
    };

    private Boolean Registration() {
        userName = edit_user.getText().toString();
        email = edit_email.getText().toString();
        phoneNo = edit_phone.getText().toString();
        address = edit_adress.getText().toString();
        password = edit_password.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            edit_user.setError("User name required");
            edit_user.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            edit_email.setError("Email Required");
            edit_email.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            edit_password.setError("Password required");
            edit_password.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email.setError("Enter a valid email");
            edit_email.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(phoneNo)) {
            edit_phone.setError("Mobile number Required");
            edit_phone.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            edit_adress.setError("Address Required");
            edit_adress.requestFocus();
            return false;
        }
        if (password.length()<6) {
            edit_password.setError("Password greater than six character");
            edit_password.requestFocus();
            return false;
        }
        Query query1 = FirebaseDatabase.getInstance().getReference().child("CustomerInfo").orderByChild("email").equalTo(email);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                proDialog=new ProgressDialog(Customer_signup.this);
                if (dataSnapshot.getChildrenCount() > 0) {
                    proDialog.dismiss();
                    Toast.makeText(Customer_signup.this, "Email already exist", LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final CUSTOMERINFO customerInfo = new CUSTOMERINFO(userName, email, phoneNo, address, password,"");
                                FirebaseDatabase.getInstance().getReference("CustomerInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(customerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            sendEmailVerification();
                                            FirebaseDatabase.getInstance().getReference().child("checkinfo").child(FirebaseAuth.getInstance().getUid()).child("info").setValue("customer");


                                        } else {
                                            proDialog.dismiss();
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

return true;
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
