package com.example.fyp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fyp.Database.CompnayNameRealm;
import com.example.fyp.Extras.PreferanceFile;
import com.example.fyp.Models.COMPANYINFO;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Signup = findViewById(R.id.signup_btn);
        signup_first = findViewById(R.id.first_signup);
        signup_second = findViewById(R.id.second_signup);
        et_CompnayName = findViewById(R.id.company);
        et_Email = findViewById(R.id.email);
        et_Password = findViewById(R.id.password);
        et_repass = findViewById(R.id.re_pass);
        et_phonenbr = findViewById(R.id.phoneNo);
         InitRealmConfig();

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

    private void InitRealmConfig(){

        // initialize Realm
        Realm.init(getApplicationContext());

        // create your Realm configuration
        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);
    }

    public  void SaveCompnayRealm(String name)
    {

        DelleteName();

        Realm realmDB=Realm.getDefaultInstance();

        try{
            realmDB.beginTransaction();
            CompnayNameRealm objRealm = realmDB.createObject(CompnayNameRealm.class);
            objRealm.setsCompnayName(name);

            realmDB.commitTransaction();
        }
        catch (Exception ex){

            Toast.makeText(this, "Error in realm", Toast.LENGTH_SHORT).show();
        }

    }


    public void DelleteName()
    {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(CompnayNameRealm.class);
            }
        });

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
        if (TextUtils.isEmpty(et_phonenbr.getText().toString())) {
            et_Password.setError("Phone no required");
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

       SaveCompnayRealm(et_CompnayName.getText().toString());

        return true;
    }

    private void CompnayNames()
    {

        String sCompnayName = dataList.get(0);

        if (sCompnayName.equals(et_CompnayName.getText().toString()))
        {
//            SaveData();
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
//                    SaveData();
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
                               // String uid=firebaseAuth.getCurrentUser().getUid();
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

    private void addcheckinfo() {


        FirebaseDatabase.getInstance().getReference().child("checkinfo").child(FirebaseAuth.getInstance().getUid()).child("info").setValue("company");

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


                    if(sValue.equalsIgnoreCase(et_CompnayName.getText().toString())){
                        SaveData(String.valueOf(dsp.child("About").getValue()));
                        return;

                    }






//                    dataList.add(sValue); //add result into array list

                }
                Toast.makeText(signup.this, "Compnay Record not Found", Toast.LENGTH_SHORT).show();

//                CompnayNames();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void CompanyInfo() {

        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("CompanyInfo");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot zoneSnapshot : snapshot.getChildren()) {

                    //   sCompnyInfoName = zoneSnapshot.child("compName").getValue().toString();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


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
