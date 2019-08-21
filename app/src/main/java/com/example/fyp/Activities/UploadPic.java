package com.example.fyp.Activities;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fyp.Activities.CompanyProfile;
import com.example.fyp.Models.GALLERY;
import com.example.fyp.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.concurrent.Executor;
public class UploadPic extends AppCompatActivity {
    ImageView image;
    EditText edit_price,edit_name,edit_description;
    Button choose,upload;
    final static int CHOOSE_IMAGE=1;
    private Uri imgUri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask storageTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic);
        image=(ImageView)findViewById(R.id.image_upload);
        edit_price=(EditText)findViewById(R.id.price_upload);
        edit_name=(EditText)findViewById(R.id.product_upload);
        edit_description=(EditText)findViewById(R.id.descriptionProduct);
        choose=(Button)findViewById(R.id.choosePhoto);
        upload=(Button)findViewById(R.id.uploadPhoto);
        databaseReference= FirebaseDatabase.getInstance().getReference("Gallery");
        storageReference= FirebaseStorage.getInstance().getReference("ImageGallery");
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storageTask!=null&&storageTask.isInProgress()){
                    Toast.makeText(UploadPic.this,"Upload in progress",Toast.LENGTH_SHORT).show();
                }
                else {
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
        if (requestCode==CHOOSE_IMAGE&&resultCode==RESULT_OK&&data!=null){
            imgUri=data.getData();
            Picasso.get().load(imgUri).into(image);

        }
    }
    private String getFileExtension(Uri imgUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imgUri));
    }
    private void uploadImage() {
        if (imgUri != null) {
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading..Please wait");
            progressDialog.show();
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imgUri));
            fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloaduri=uriTask.getResult();
                    GALLERY gallery = new GALLERY(edit_name.getText().toString().trim(), edit_price.getText().toString().trim(),downloaduri.toString(),edit_description.getText().toString().trim());
                    String uploadId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    gallery.setCompany_id(uploadId);
                    long timeStamp = System.currentTimeMillis();
                    gallery.setProduct_id(timeStamp+"");
                    databaseReference.child(uploadId).child(timeStamp+"").setValue(gallery);
                    Toast.makeText(UploadPic.this, "Image Upload successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadPic.this, CompanyProfile.class));
                    finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadPic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int)progress)+"%Uploading...");
                }
            })
            ;

        }
    }
}
