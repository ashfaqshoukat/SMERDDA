package com.example.fyp.Activities;
import android.content.Intent;
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
import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Signin= findViewById(R.id.signin_btn);
        first= findViewById(R.id.first_signin);
        second= findViewById(R.id.second_signin);
        edit_company= findViewById(R.id.companyName_signin);
        edit_email= findViewById(R.id.email_signin);
        edit_pass= findViewById(R.id.password_signin);
        mAth=FirebaseAuth.getInstance();
        firebaseUser=mAth.getCurrentUser();

        InitRealmConfig();
        if (firebaseUser!=null){
            finish();
            startActivity(new Intent(signin.this, CompanyProfile.class));
        }
        if(Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.bar));
        }
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                company=edit_company.getText().toString();
                email=edit_email.getText().toString();
                password=edit_pass.getText().toString();
                if (TextUtils.isEmpty(company)){
                   edit_company.setError("Email Required");
                    edit_company.requestFocus();
                }
                if (TextUtils.isEmpty(email)){
                    edit_email.setError("Email Required");
                    edit_email.requestFocus();
                }
                if (TextUtils.isEmpty(password)){
                    edit_pass.setError("Password required");
                    edit_pass.requestFocus();
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edit_email.setError("Enter a valid email");
                    edit_pass.requestFocus();

                }

                String sCompnyName =GetCompnayName();

//                if (! company.equals(sCompnyName))
//                {
//                    Toast.makeText(signin.this,"Compnay name not matched !",Toast.LENGTH_SHORT).show();
//                    return;
//                }

                mAth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                             checkEmailVerification();
                        }
                        else {
                            Toast.makeText(signin.this,"Login failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
    private void checkEmailVerification(){

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag=firebaseUser.isEmailVerified();
        if (emailFlag){
            startActivity(new Intent(signin.this,CompanyProfile.class));

        }
        else {
            Toast.makeText(signin.this,"Verify your Email",Toast.LENGTH_SHORT).show();
            mAth.signOut();
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

    private String GetCompnayName()
    {
        Realm realmDB=Realm.getDefaultInstance();
        String sName = "";

        RealmResults<CompnayNameRealm> userSchedules=realmDB.where(CompnayNameRealm.class).findAll();

        if(userSchedules.size() == 0)
        {
            Toast.makeText(this,"No Record Found", Toast.LENGTH_SHORT).show();
            return "0";

        }

        for (CompnayNameRealm userSchedule:userSchedules)
        {

            sName = userSchedule.getsCompnayName();
        }

        return sName;
    }



}
