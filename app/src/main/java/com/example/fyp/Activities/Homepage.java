package com.example.fyp.Activities;

import android.os.Build;

import com.example.fyp.Adapters.Adapterviewpage;
import com.example.fyp.Fragments.Fragment1;
import com.example.fyp.Fragments.Fragment2;
import com.example.fyp.Fragments.BlankFragment;
import com.example.fyp.R;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

public class Homepage extends AppCompatActivity {
    Window window;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        if(Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.bar));
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout= findViewById(R.id.tabs);
        viewPager= findViewById(R.id.viewpager_id);
        Adapterviewpage adapter=new Adapterviewpage(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(),"Companies");
        adapter.addFragment(new Fragment2(),"Top Companies");
        adapter.addFragment(new BlankFragment(),"Notification");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);



    }

        public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slideout_right,R.anim.slide_left);
    }
}
