package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class CompanyProfile extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    StorageReference storageReference;
    DatabaseReference databaseReference, databaseReference1;
    ImageView imageView_add;
    FirebaseAuth firebaseAuth;
    RecyclerView mRecyclerView;
    GalleryAdapter mAdapter;
    ArrayList<Gallery> mGallery;
    TextView txtabout, txtCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        txtabout = findViewById(R.id.about);
        txtCompany = findViewById(R.id.companyName);
        imageView_add = findViewById(R.id.addPic);
        InitRealmConfig();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("CompanyInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        showProducts();
        MatchCompnayName();

        imageView_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompanyProfile.this, UploadPic.class));
            }
        });

    }

    private void InitRealmConfig() {

        // initialize Realm
        Realm.init(getApplicationContext());

        // create your Realm configuration
        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);
    }


    private String GetCompnayName() {
        Realm realmDB = Realm.getDefaultInstance();
        String sName = "";

        RealmResults<CompnayNameRealm> userSchedules = realmDB.where(CompnayNameRealm.class).findAll();

        if (userSchedules.size() == 0) {
            Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show();
            return "0";
        }
        for (CompnayNameRealm userSchedule : userSchedules) {
            sName = userSchedule.getsCompnayName();
        }

        return sName;
    }

    private void MatchCompnayName() {

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2;
        ref2 = ref1.child("Companies");

        final String sCompnayChild = GetCompnayName();

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String sKey = dsp.getKey();
                    String sValue = String.valueOf(dsp.child("companyName").getValue());

                    if (sValue.equals(sCompnayChild)) {

                        String sAbout = String.valueOf(dsp.child("About").getValue());
                        txtCompany.setText(sCompnayChild);
                        txtabout.setText(sAbout);

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(CompanyProfile.this, Customer_signin.class));
    }

    //add menu
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.profile_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_home1:
                startActivity(new Intent(CompanyProfile.this, Homepage.class));
                return true;
            case R.id.item_logout:
                Logout();
                return true;
            default:
                return false;
        }
    }

    private void showProducts() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CompanyProfile.this, LinearLayoutManager.HORIZONTAL, false));
        mGallery = new ArrayList<>();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Gallery");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Gallery gallery = postSnapshot.getValue(Gallery.class);
                    mGallery.add(gallery);
                }
                mAdapter = new GalleryAdapter(CompanyProfile.this, mGallery);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CompanyProfile.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

}
