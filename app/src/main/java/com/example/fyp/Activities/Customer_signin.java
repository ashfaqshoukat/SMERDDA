package com.example.fyp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Progress;

public class Customer_signin extends AppCompatActivity {
EditText et_cus_email,et_cus_pass;
Button cus_signin;
TextView ed_forgot_pass;
FirebaseAuth mAth;
FirebaseUser firebaseUser;
String email,password;
ImageView arrow1,arrow2;
ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_signin);
        et_cus_email= findViewById(R.id.cus_edit_email);
        et_cus_pass= findViewById(R.id.cus_edit_pass);
        cus_signin= findViewById(R.id.cus_signin_btn);
        ed_forgot_pass= findViewById(R.id.forgot_pass);
        cus_signin.setOnClickListener(listener);
        arrow1=(ImageView)findViewById(R.id.arrow_signin);
        arrow2=(ImageView)findViewById(R.id.arrowsigninsecond);
        mAth=FirebaseAuth.getInstance();
        firebaseUser=mAth.getCurrentUser();
        if (firebaseUser!=null){
            finish();
            startActivity(new Intent(Customer_signin.this,Customer_profile.class));
        }
        ed_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Customer_signin.this, Customer_forgot.class));
            }
        });
        arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Customer_signin.this,Customer_signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.go_up,R.anim.go_down);
                finish();
            }
        });
        arrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Customer_signin.this,Customer_signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.go_up,R.anim.go_down);
                finish();
            }
        });

    }
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean loginCustomer=LoginButton();
            if (loginCustomer==true){
                pd=new ProgressDialog(Customer_signin.this);
                pd.setMessage("Account Verified...");
                pd.setCancelable(false);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();
            }

        }
    };
    private Boolean LoginButton(){
        email=et_cus_email.getText().toString();
        password=et_cus_pass.getText().toString();
        if (TextUtils.isEmpty(email)){
            et_cus_email.setError("Email Required");
            et_cus_email.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)){
            et_cus_pass.setError("Password required");
            et_cus_pass.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_cus_email.setError("Enter a valid email");
            et_cus_email.requestFocus();
            return false;

        }
        mAth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    updateFirebaseToken();
                    checkEmailVerification();
                }
                else {
                    pd.dismiss();
                    Toast.makeText(Customer_signin.this,"Please first registered yourself",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Customer_signin.this,Customer_signup.class);
                    startActivity(intent);
                }
            }
        });
return true;
    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag=firebaseUser.isEmailVerified();
        if (emailFlag){
            pd.dismiss();
            startActivity(new Intent(Customer_signin.this,Customer_profile.class));

        }
        else {
            pd.dismiss();
            Toast.makeText(Customer_signin.this,"Verify your Email",Toast.LENGTH_SHORT).show();
            mAth.signOut();
        }
    }

    private void updateFirebaseToken(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token=preferences.getString("token","");
        if(!token.equals(""))
            FirebaseDatabase.getInstance().getReference().child("CustomerInfo").child(FirebaseAuth.getInstance().getUid()).
                    child("FCMToken").setValue(token);
    }
}
