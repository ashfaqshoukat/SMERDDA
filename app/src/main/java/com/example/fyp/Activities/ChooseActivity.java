package com.example.fyp.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.fyp.Extras.PreferanceFile;
import com.example.fyp.R;

public class ChooseActivity extends AppCompatActivity {
    Window window;
ImageView cus,Smes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        cus= findViewById(R.id.customer);
        Smes= findViewById(R.id.smes);
        if(Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.bar));
        }
        cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferanceFile.getInstance(getApplicationContext()).setIsCompany(false);
                Intent intent=new Intent(ChooseActivity.this, Cus_main_signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
        Smes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferanceFile.getInstance(getApplicationContext()).setIsCompany(true);
                Intent intent=new Intent(ChooseActivity.this, SignupHome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
    }

}
