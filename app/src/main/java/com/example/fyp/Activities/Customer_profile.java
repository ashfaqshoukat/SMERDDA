package com.example.fyp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class Customer_profile extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    TextView t_user, t_email, t_address, t_phone;
    RoundedImageView roundedImageView;
    ImageView choosePicBtn;
    ImageView uploadPic, menu;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button button;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    final static int gallery_pic = 1;
    String username, address, phone, email;
    Uri resultUri;
    public static final int PICK_IMAGE = 1;


    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        t_user = findViewById(R.id.username);
        t_email = findViewById(R.id.Email);
        t_address = findViewById(R.id.address);
        t_phone = findViewById(R.id.phoneNo);
        menu = findViewById(R.id.img_menu);
        roundedImageView = findViewById(R.id.imageView1);
        choosePicBtn = findViewById(R.id.choosePic);
        uploadPic = findViewById(R.id.uploadPic);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        dataProfile();
        roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChoose();
            }
        });
    }

    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(Customer_profile.this, Customer_signin.class));

    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {

            uploadImages(data.getData());
        }
    }



    private void uploadImages(Uri filePath) {
        uploadPic.setVisibility(View.INVISIBLE);
        roundedImageView.setImageURI(filePath);

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
                            Toast.makeText(Customer_profile.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CustomerInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    try {
                                        ref.child("profileimage").setValue(url.toString());

                                    } catch (Exception ex) {
                                        Log.e("ERRORR", ex.getMessage());
                                    }

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Customer_profile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == gallery_pic && resultCode == RESULT_OK && data != null) {
//            Uri imguri = data.getData();
//            //roundedImageView.setImageURI(imguri);
//            CropImage.activity(imguri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1, 1).start(this);
//        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                resultUri = result.getUri();
//                roundedImageView.setImageURI(resultUri);
//                String user = firebaseAuth.getCurrentUser().getUid();
//                final StorageReference filePath = storageReference.child(user + ".jpg");
//                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
//                        databaseReference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
//                                databaseReference.child("profileimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(Customer_profile.this, "Image successfully stored", Toast.LENGTH_SHORT).show();
//                                            Intent selfIntent = new Intent(Customer_profile.this, Customer_profile.class);
//                                            startActivity(selfIntent);
//                                            CUSTOMERINFO customerInfo = new CUSTOMERINFO();
//                                            Picasso.get().load(customerInfo.getProfileimage()).into(roundedImageView);
//
//                                        } else {
//                                            Toast.makeText(Customer_profile.this, "Error occurred", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                });
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//                    }
//                });
//            }
//        }
//
//    }

    //Registration data fetch and show on profile
    private void dataProfile() {
        DatabaseReference databaseReference;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerInfo").child(currentUderId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username = (String) dataSnapshot.child("username").getValue();
                    address = (String) dataSnapshot.child("address").getValue();
                    phone = (String) dataSnapshot.child("phone").getValue();
                    email = (String) dataSnapshot.child("email").getValue();
                    String imgurl = (String) dataSnapshot.child("profileimage").getValue();
                    if (imgurl != null && (!imgurl.equalsIgnoreCase(""))) {
                        Picasso.get().load(imgurl).into(roundedImageView);
                        uploadPic.setVisibility(View.INVISIBLE);
                    }
                    t_user.setText(username);
                    t_email.setText(email);
                    t_address.setText(address);
                    t_phone.setText(phone);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Customer_profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });
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
                startActivity(new Intent(Customer_profile.this, Homepage.class));
                return true;
            case R.id.item_logout:
                Logout();
                return true;
            default:
                return false;
        }


    }

    private void showDialog() {
        pd = new ProgressDialog(Customer_profile.this);
        pd.setMessage("loading");
        pd.show();
    }

    private void hideDialog() {
        if (pd != null && (!pd.isShowing())) {
            pd.dismiss();
        }
    }
}

