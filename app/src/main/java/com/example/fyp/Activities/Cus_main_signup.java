package com.example.fyp.Activities;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fyp.R;
import com.example.fyp.Adapters.ViewPagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class Cus_main_signup extends AppCompatActivity{
Button btn_mail_signup,btn_mail_signin;
ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_main_signup);
        btn_mail_signup= findViewById(R.id.cus_signup);
        btn_mail_signin= findViewById(R.id.signin_cus);
        btn_mail_signup.setOnClickListener(listener);
        btn_mail_signin.setOnClickListener(listener2);
        viewPager= findViewById(R.id.viewpager1);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),2000,3000);
        viewPager.setAdapter(viewPagerAdapter);

    }
    private  View.OnClickListener listener2=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Cus_main_signup.this, Customer_signin.class));
        }
    };
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ButtonClick();
        }
    };
    private void ButtonClick(){
        startActivity(new Intent(Cus_main_signup.this, Customer_signup.class));

    }
    class MyTimerTask extends TimerTask{
        public void run(){
            Cus_main_signup.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem()==0){
                        viewPager.setCurrentItem(1);
                    }
                    else if(viewPager.getCurrentItem()==1){
                        viewPager.setCurrentItem(2);
                    }
                    else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });

        }

    }


}
