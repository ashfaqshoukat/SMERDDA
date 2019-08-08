package com.example.fyp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fyp.Activities.signin;
import com.example.fyp.Database.HelperProfile;
import com.example.fyp.Models.COMPANYINFO;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class signup extends AppCompatActivity {
    Window window;
    Button Signup;
    ImageView signup_first, signup_second;
    ProgressDialog progressBar;

    EditText et_CompnayName, et_Email, et_Password, et_repass,et_phonenbr;

    DatabaseReference databaseReference;
    DatabaseReference obj_CompnayName;


    FirebaseAuth firebaseAuth;
    String company, email, password, repass;

    List<String> dataList = new ArrayList<String>();
    HelperProfile objHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Signup = (Button) findViewById(R.id.signup_btn);
        signup_first = (ImageView) findViewById(R.id.first_signup);
        signup_second = (ImageView) findViewById(R.id.second_signup);
        et_CompnayName = (EditText) findViewById(R.id.company);
        et_Email = (EditText) findViewById(R.id.email);
        et_Password = (EditText) findViewById(R.id.password);
        et_repass = (EditText) findViewById(R.id.re_pass);
        et_phonenbr = findViewById(R.id.phoneNo);
        databaseReference = FirebaseDatabase.getInstance().getReference("CompanyInfo");
        obj_CompnayName = FirebaseDatabase.getInstance().getReference("Companies");
        firebaseAuth = FirebaseAuth.getInstance();



        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.bar));
        }
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Boolean bResult = ValidationUI();
                if (bResult == true)
                {
                    MatchMulitCompnayName();

                    progressBar = new ProgressDialog(signup.this);
                    progressBar.setCancelable(false);
                    progressBar.setMessage("Pease Wait ...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.show();
                }

            }
        });


        signup_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, signin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.go_up, R.anim.go_down);
                finish();
            }
        });
        signup_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, signin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.go_up, R.anim.go_down);

                finish();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slideout_right, R.anim.slide_left);
    }

    //emailVerification
    private void sendCompanyEmailVerification() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(signup.this, "Registration successfully Completed", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(signup.this, signin.class));


                    }
                }
            });
        }
    }

    FirebaseDatabase m_Fir_Database;
    DatabaseReference m_Fir_Reference;

    private void SaveToCloudCompmay( String sCompnayName)
    {

        //String sDeviceName = getDeviceName();
        m_Fir_Database = FirebaseDatabase.getInstance();
        m_Fir_Reference= FirebaseDatabase.getInstance().getReference().child("MatchString").child(sCompnayName);

        m_Fir_Reference.setValue(sCompnayName);
    }

    private Boolean ValidationUI()
    {
        company = et_CompnayName.getText().toString();
        email = et_Email.getText().toString();
        password = et_Password.getText().toString();
        repass = et_repass.getText().toString();
        if (TextUtils.isEmpty(company)) {
            et_CompnayName.setError("Official company name required");
            et_CompnayName.requestFocus();

            return false;
        }
        if (TextUtils.isEmpty(email)) {
            et_Email.setError("Email Required");
            et_Email.requestFocus();

            return false;
        }
        if (TextUtils.isEmpty(password)) {
            et_Password.setError("Password required");
            et_Password.requestFocus();

            return false;
        }
        if (TextUtils.isEmpty(repass)) {
            et_repass.setError("Re-enter Password");
            et_repass.requestFocus();

            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_Email.setError("Enter a valid email");
            et_Email.requestFocus();

            return false;

        }
        if (password.length() < 6) {
            et_Password.setError("Password greater than six character");
            et_Password.requestFocus();
            return false;
        }
        if (!repass.equals(password)) {
            et_Password.setError("Both passwords are not mizatch");
            et_Password.requestFocus();
            return false;
        }

        SaveToCloudCompmay(et_CompnayName.getText().toString());

        return true;
    }

    private void CompnayNames()
    {

        String sCompnayName = dataList.get(0);

        if (sCompnayName.equals(et_CompnayName.getText().toString()))
        {
            SaveData();
            return;
        }
        if (!sCompnayName.equals(et_CompnayName.getText().toString()))
        {

            int nCounter = 0;
            while (nCounter < dataList.size())
            {
                sCompnayName = dataList.get(nCounter);

                if (sCompnayName.equals(et_CompnayName.getText().toString()))
                {
                    SaveData(); //work


                    break;

                }


                nCounter ++;
            }

            if (! sCompnayName.equals(et_CompnayName.getText().toString()) && nCounter <= dataList.size())
            {
                Toast.makeText(signup.this, "Compnay Record not Found", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }

        }

    }

    private void SaveData(String about) {
        Query query = FirebaseDatabase.getInstance().getReference().child("CompanyInfo").orderByChild("compName").equalTo(company);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0)
                {
                    Toast.makeText(signup.this, "Choose different company name,this company already exist", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final COMPANYINFO companyInfo = new COMPANYINFO(company, email, password);
                                companyInfo.setProfileimage("");
                                companyInfo.setPhonenbr(et_phonenbr.getText().toString());
                                companyInfo.setAbout(about);
                                // FirebaseDatabase.getInstance().getReference("COMPANYINFO").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uid).setValue(companyInfo);
                                FirebaseDatabase.getInstance().getReference("CompanyInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(companyInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            sendCompanyEmailVerification();
                                            addcheckinfo();



                                        }
                                        else {
                                            Toast.makeText(signup.this, "Error", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(signup.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void MatchMulitCompnayName()
    {

        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2;
        ref2 = ref1.child("Companies");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    String sKey =dsp.getKey();
                    String sValue =  String.valueOf(dsp.child("companyName").getValue());

                    dataList.add(sValue); //add result into array list

                }

                CompnayNames();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void addcheckinfo() {


        FirebaseDatabase.getInstance().getReference().child("checkinfo").child(FirebaseAuth.getInstance().getUid()).child("info").setValue("company");

    }
    public String getMessage(){
        final AtomicBoolean done = new AtomicBoolean(false);
        final String[] message1 = new String[0];

        //assuming you have already called firebase initialization code for admin sdk, android etc
        DatabaseReference root = FirebaseDatabase.getInstance().getReference("Companies");
        root.child("company1").child("companyName").addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                message1[0] =  dataSnapshot.getValue(String.class);
                done.set(true);
            }

            public void onCancelled(DatabaseError error) {
                // TODO Auto-generated method stub

            }
        });
        while (!done.get());

        return message1[0];
    }
}
