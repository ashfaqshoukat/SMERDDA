package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UploadPic extends AppCompatActivity {
ImageView image;
EditText edit_price,edit_name;
Button choose,upload;
final static int CHOOSE_IMAGE=1;
private Uri imgUri;
DatabaseReference databaseReference;
StorageReference storageReference;
StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic);
        image= findViewById(R.id.image_upload);
        edit_price= findViewById(R.id.price_upload);
        edit_name= findViewById(R.id.product_upload);
        choose= findViewById(R.id.choosePhoto);
        upload= findViewById(R.id.uploadPhoto);
        databaseReference= FirebaseDatabase.getInstance().getReference("Gallery");
        storageReference= FirebaseStorage.getInstance().getReference("ImageGallery");
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask!=null&&mUploadTask.isInProgress()){
                    Toast.makeText(UploadPic.this,"Upload in progress",Toast.LENGTH_LONG).show();
                }
                else{
                    uploadImage();
                }
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChoose();
            }
        });

    }
    private void showFileChoose(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CHOOSE_IMAGE&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            imgUri=data.getData();
            Picasso.get().load(imgUri).fit().centerCrop().into(image);
            //Picasso.with(this).load(imgUri).into(image);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        if (imgUri!=null){
            StorageReference fileReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imgUri));
            mUploadTask=fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  Toast.makeText(UploadPic.this,"Upload successfully",Toast.LENGTH_SHORT).show();
                  Gallery gallery=new Gallery(edit_name.getText().toString().trim(),edit_price.getText().toString().trim(),taskSnapshot.getUploadSessionUri().toString());
                  String uploadId=databaseReference.push().getKey();
                  databaseReference.child(uploadId).setValue(gallery);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadPic.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(UploadPic.this,"Image Upload successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UploadPic.this,CompanyProfile.class));
finish();
                    }
                }
            });
        }
    }
}
