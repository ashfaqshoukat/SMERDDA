package com.example.fyp.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Activities.Homepage;
import com.example.fyp.Activities.NewOrder;
import com.example.fyp.Activities.UploadPic;
import com.example.fyp.Activities.signin;
import com.example.fyp.Adapters.GalleryAdapter;
import com.example.fyp.Database.HelperProfile;

import com.example.fyp.Models.COMPANYINFO;
import com.example.fyp.Models.GALLERY;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
public class CompanyProfile extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    StorageReference storageReference;
    DatabaseReference databaseReference,databaseReference1;
    ImageView imageView_add,chooseImage;
    FirebaseAuth firebaseAuth;
    RecyclerView mRecyclerView;
    GalleryAdapter mAdapter;
    ArrayList<GALLERY> mGallery;
    TextView txtabout,txtCompany,txtEmail,txtemail,txtphonenbr ;
    TextView button2;
    String Name;
    public static final int PICK_IMAGE_Gallery = 1;
    RoundedImageView circularimage;

    HelperProfile objHelperProfile ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        txtabout = (TextView) findViewById(R.id.about);
        txtCompany = (TextView) findViewById(R.id.companyName);
        imageView_add = (ImageView) findViewById(R.id.addPic);
        chooseImage=(ImageView)findViewById(R.id.choosePic);
        button2 = (TextView) findViewById(R.id.newOrder);
        txtEmail=findViewById(R.id.email);
        circularimage=(RoundedImageView)findViewById(R.id.circularimage);
        firebaseAuth = FirebaseAuth.getInstance();

        objHelperProfile = new HelperProfile(CompanyProfile.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("CompanyInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //databaseReference2=FirebaseDatabase.getInstance().getReference("CustomerCompanyProfile").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        storageReference= FirebaseStorage.getInstance().getReference("ImagesCompany");
        showComapnyProfile();
        showProducts();
        MatchCompnayName();



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompanyProfile.this, NewOrder.class));
            }
        });

        imageView_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompanyProfile.this, UploadPic.class));
            }
        });
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileImage();
            }
        });
    }


    private String GetCompanyName()
    {
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot zoneSnapshot : snapshot.getChildren()) {
                    Name = zoneSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return Name;
    }
    private void MatchCompnayName()
    {
        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2;
        ref2 = ref1.child("Companies");

        String sCompnayName =  GetCounter();

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    String sKey =dsp.getKey();
                    String sValue =  String.valueOf(dsp.child("companyName").getValue());

                    if (sValue.equals(sCompnayName))
                    {

                        String sAbout =  String.valueOf(dsp.child("About").getValue());
                        txtCompany.setText(sCompnayName);
                        txtabout.setText(sAbout);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(CompanyProfile.this, signin.class));
    }
    //add menu
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.profile_menu);
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
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
    private void showFileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_Gallery);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_Gallery) {

            uploadImages(data.getData());
        }
    }
    private void uploadImages(Uri filePath) {
        chooseImage.setVisibility(View.INVISIBLE);
        circularimage.setImageURI(filePath);

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(CompanyProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CompanyInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    try {
                                        ref.child("profileimage").setValue(url.toString());

                                    } catch (Exception ex) {
                                        Log.e("ERROR", ex.getMessage());
                                    }

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CompanyProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
    private void showComapnyProfile() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                COMPANYINFO companyInfo=dataSnapshot.getValue(COMPANYINFO.class);
                txtabout.setText(companyInfo.getAbout());
                txtCompany.setText(companyInfo.getCompName());

                txtEmail.setText(companyInfo.getEmail());
                txtphonenbr.setText(companyInfo.getPhonenbr());
                if(!companyInfo.getProfileimage().equalsIgnoreCase("")){

                    Picasso.get().load(companyInfo.getProfileimage()).into(circularimage);}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showProducts() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CompanyProfile.this, LinearLayoutManager.HORIZONTAL, false));
        mGallery = new ArrayList<>();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Gallery").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGallery.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    GALLERY gallery = postSnapshot.getValue(GALLERY.class);
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


    private String GetCounter()
    {

        String sCompnayName =" ";

        Cursor objCursor = objHelperProfile.SelectName();
        if (objCursor.getCount() > 0)
        {
            while (objCursor.moveToNext())
            {
                sCompnayName= objCursor.getString(objCursor.getColumnIndex("CompnayName"));

            }
        }
        return sCompnayName;
    }
}
