package com.example.fyp.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fyp.Extras.PreferanceFile;
import com.example.fyp.Models.COMPANYINFO;
import com.example.fyp.Models.CUSTOMERINFO;
import com.example.fyp.Models.ORDERINFO;
import com.example.fyp.R;
import com.example.fyp.Services.MySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddressDetail extends AppCompatActivity {
    EditText edit_name,edit_phone,edit_address,edit_email,edit_lastname;
    String total_Amount="",state="Normal";
    Button btn;
    ORDERINFO orderinfo;
    DatabaseReference databaseReference;
    CUSTOMERINFO customerinfo;
    COMPANYINFO companyinfo;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAIvjGbME:APA91bGguFNNBwx05QNrFGLBtVb11XHdWNHjFDm7W0jzN0w1HDRHvkLHKux8KC-VMs1jViTNK5wibrxvEtSm6TMsRtddlhzbxmn1323NYbczaQkgVpeVoe5Ao73RPEALR9ypJ5u2mwts";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);
        edit_name=(EditText)findViewById(R.id.your_name);
        edit_phone=(EditText)findViewById(R.id.your_number);
        edit_address=(EditText)findViewById(R.id.your_address);
        edit_email=(EditText)findViewById(R.id.your_email);
        edit_lastname=(EditText)findViewById(R.id.last_name);
        btn=(Button)findViewById(R.id.btn);
        String o=getIntent().getStringExtra("orderinfo");
        orderinfo=new Gson().fromJson(o,ORDERINFO.class);

        total_Amount=getIntent().getStringExtra("Total_price");
        Toast.makeText(AddressDetail.this,"Total Price = $" +total_Amount,Toast.LENGTH_LONG).show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidation();
            }
        });
        fillForm();
    }


    private void CheckValidation() {
        String name=edit_name.getText().toString();
        String last_name=edit_lastname.getText().toString();
        String phone=edit_phone.getText().toString();
        String address=edit_address.getText().toString();
        String email=edit_email.getText().toString();
        if (TextUtils.isEmpty(name)){
            edit_name.setError("Enter your name");
            edit_name.requestFocus();
        }
        else if (TextUtils.isEmpty(last_name)){
            edit_lastname.setError("Please enter your last or surname");
            edit_lastname.requestFocus();
        }
        else if (TextUtils.isEmpty(phone)){
            edit_phone.setError("Enter your Phone Number");
            edit_phone.requestFocus();
        }
        else if (TextUtils.isEmpty(address)){
            edit_address.setError("Enter your Address");
            edit_address.requestFocus();
        }
        else if (TextUtils.isEmpty(email)){
            edit_email.setError("Enter your email");
            edit_email.requestFocus();
        }
        else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        orderinfo.setUserFName(edit_name.getText().toString());
        orderinfo.setUserLName(edit_lastname.getText().toString());
        orderinfo.setUserPhoneNbr(edit_phone.getText().toString());
        orderinfo.setUserAddress(edit_address.getText().toString());
        orderinfo.setUserEmail(edit_email.getText().toString());
        long time = System.currentTimeMillis();
        orderinfo.setOrderTime(time);
        orderinfo.setStatus(0);
        FirebaseDatabase.getInstance().getReference().child("Order").child(orderinfo.getCompanyId()).child(orderinfo.getOrderTime() + "").setValue(orderinfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddressDetail.this, "Order place successfully", Toast.LENGTH_SHORT).show();
                    sendNotification("New Order Received",orderinfo.getProductName());
                    finish();
                }
            }
        });
    }


    private void fillForm(){
        if(PreferanceFile.getInstance(getApplicationContext()).isIsCompany()){
            companyinfo=PreferanceFile.getInstance(getApplicationContext()).getCompany();
            edit_phone.setText(companyinfo.getPhonenbr());
            edit_email.setText(companyinfo.getEmail());
            orderinfo.setUserId(companyinfo.getCompanyId());
            orderinfo.setUserImage(companyinfo.getProfileimage());

        }
        else{
            customerinfo=PreferanceFile.getInstance(getApplicationContext()).getCustomer();
            edit_phone.setText(customerinfo.getPhone());
            edit_email.setText(customerinfo.getEmail());
            edit_name.setText(customerinfo.getUsername());
            edit_address.setText(customerinfo.getAddress());
            orderinfo.setUserId(FirebaseAuth.getInstance().getUid());
            orderinfo.setUserImage(customerinfo.getProfileimage());

        }
    }


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddressDetail.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void sendNotification(String title,String s){
        TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = title;
        NOTIFICATION_MESSAGE =s;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notifcationBody.put("id",orderinfo.getCompanyId());

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }
}
