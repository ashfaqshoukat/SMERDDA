package com.example.fyp.Activities;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    Window window;
    Thread thread;
    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.txt);
        imageView = findViewById(R.id.imageview);
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.bar));
        }
        thread = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                    FirebaseUser instance = FirebaseAuth.getInstance().getCurrentUser();
                    if (instance != null) {
                        FirebaseDatabase.getInstance().getReference().child("checkinfo").child(instance.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String value = dataSnapshot.child("info").getValue(String.class);
                                if (value != null) {
                                    if (value.equalsIgnoreCase("company")) {
                                        Intent intent = new Intent(SplashActivity.this, CompanyProfile.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(SplashActivity.this, Customer_profile.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, ChooseActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(SplashActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Intent intent = new Intent(SplashActivity.this, ChooseActivity.class);
                        startActivity(intent);
                        finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();

        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_animation_txt);
        textView.startAnimation(animation);
        Animation animation1 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_pic_animation);
        imageView.startAnimation(animation1);
    }
}
