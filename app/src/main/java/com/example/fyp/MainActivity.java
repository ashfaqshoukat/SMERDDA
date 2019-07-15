package com.example.fyp;

import android.content.Intent;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
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
                    Intent intent = new Intent(MainActivity.this, choose.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();

        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash_animation_txt);
        textView.startAnimation(animation);
        Animation animation1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash_pic_animation);
        imageView.startAnimation(animation1);
    }
}
