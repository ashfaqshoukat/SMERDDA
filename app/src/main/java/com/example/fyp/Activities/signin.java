package com.example.fyp.Activities;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fyp.Activities.CompanyProfile;
import com.example.fyp.Activities.signup;
import com.example.fyp.Database.HelperProfile;
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

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
public class signin extends AppCompatActivity {
    Window window;
    Button Signin;
    ImageView first,second;
    FirebaseAuth mAth;
    EditText edit_company,edit_email,edit_pass;
    FirebaseUser firebaseUser;
    String company,email,password;
    String Name =  "";
    ProgressDialog progressDialog;
    List<String> lstName = new ArrayList<String>();
    List<String> lstEmail = new ArrayList<String>();

    HelperProfile objName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Signin=(Button)findViewById(R.id.signin_btn);
        first=(ImageView)findViewById(R.id.first_signin);
        second=(ImageView)findViewById(R.id.second_signin);
        edit_company=(EditText)findViewById(R.id.companyName_signin);
        edit_email=(EditText)findViewById(R.id.email_signin);
        edit_pass=(EditText)findViewById(R.id.password_signin);
        mAth=FirebaseAuth.getInstance();
        firebaseUser=mAth.getCurrentUser();
        objName = new HelperProfile(signin.this);

        if (firebaseUser!=null){
            finish();
            startActivity(new Intent(signin.this,CompanyProfile.class));
        }
        if(Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.bar));
        }

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean sigin=LoginCompany();
                if (sigin==true){
                    progressDialog=new ProgressDialog(signin.this);
                    progressDialog.setMessage("Account is Verifying Wait..");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                }
            }
        });
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signin.this, signup.class);
                startActivity(intent);
                //Add animation
                overridePendingTransition(R.anim.go_up,R.anim.go_down);
                finish();
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signin.this,signup.class);
                startActivity(intent);
                //Add animation
                overridePendingTransition(R.anim.go_up,R.anim.go_down);
                finish();
            }
        });
    }
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slideout_right,R.anim.slide_left);
    }
    private Boolean LoginCompany() {
        company = edit_company.getText().toString();
        email = edit_email.getText().toString();
        password = edit_pass.getText().toString();
        if (TextUtils.isEmpty(company)) {
            edit_company.setError("Email Required");
            edit_company.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            edit_email.setError("Email Required");
            edit_email.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            edit_pass.setError("Password required");
            edit_pass.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email.setError("Enter a valid email");
            edit_pass.requestFocus();
            return false;

        }

        mAth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //progressDialog.dismiss();
                    MatchMulitCompnayName();
                    updateFirebaseToken();


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(signin.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;
    }
    private Boolean checkEmailVerification()
    {


        FirebaseUser firebaseUser=mAth.getInstance().getCurrentUser();
        Boolean emailFlag=firebaseUser.isEmailVerified();
        if (emailFlag){
            //startActivity(new Intent(signin.this,CompanyProfile.class));
            //progressDialog.dismiss();
            return true;

        }
        else {
            Toast.makeText(signin.this,"Verify your Email",Toast.LENGTH_SHORT).show();
            mAth.signOut();
        }
        return false;
    }
    private void MatchMulitCompnayName()
    {
        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2;
        ref2 = ref1.child("CompanyInfo");


        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    String sKey =dsp.getKey();
                    String sCompnayName =  String.valueOf(dsp.child("compName").getValue());
                    String sEmail =  String.valueOf(dsp.child("email").getValue());


                    lstName.add(sCompnayName);
                    lstEmail.add(sEmail);

                }



                String  sCompnayName;
                String  sEmail;
                int nCounter = 0;

                Boolean bResult= checkEmailVerification();

                while (nCounter < lstName.size())
                {
                    sCompnayName = lstName.get(nCounter);
                    sEmail = lstEmail.get(nCounter);

                    if (sCompnayName.equals(edit_company.getText().toString()) && bResult == true  && sEmail.equals(edit_email.getText().toString()))
                    {

                        objName.DeleteName();
                        objName.InsertCompnayName(edit_company.getText().toString());

                        startActivity(new Intent(signin.this, CompanyProfile.class));


                        break;

                    }

                    nCounter ++;
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateFirebaseToken(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token=preferences.getString("token","");
        if(!token.equals(""))
        FirebaseDatabase.getInstance().getReference().child("CompanyInfo").child(FirebaseAuth.getInstance().getUid()).
                child("FCMToken").setValue(token);
    }

}
