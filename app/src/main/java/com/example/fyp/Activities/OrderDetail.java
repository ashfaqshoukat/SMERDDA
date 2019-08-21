package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fyp.Extras.PreferanceFile;
import com.example.fyp.Models.ORDERINFO;
import com.example.fyp.R;
import com.example.fyp.Services.MySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderDetail extends AppCompatActivity {

    private TextView customerName,email,phonenbr,address,date,qty,totalprice,price,productname,orderBtn,orderStatus;
    private ImageView image;
    ORDERINFO orderinfo;

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
        setContentView(R.layout.activity_order_detail);
        orderinfo=new Gson().fromJson(getIntent().getStringExtra("orderinfo"),ORDERINFO.class);
        init();
        allListener();

    }

    private void init() {
        customerName = findViewById(R.id.customerName);
        email = findViewById(R.id.email);
        phonenbr = findViewById(R.id.phoneNbr);
        address = findViewById(R.id.addr);
        image = findViewById(R.id.image);
        date = findViewById(R.id.time);
        qty = findViewById(R.id.qty);
        totalprice = findViewById(R.id.totalPrice);
        price = findViewById(R.id.productPrice);
        productname = findViewById(R.id.productname);
        orderBtn = findViewById(R.id.orderBtn);
        orderStatus = findViewById(R.id.ordeSatus);
        fillData();
    }


    private void allListener() {
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferanceFile.getInstance(getApplicationContext()).isIsCompany()){
                    if(orderinfo.getStatus()==0){
                        showAlert("Order Shipped","Have you shipped this Product ?",orderinfo.getStatus());
                    }
                    else if(orderinfo.getStatus()==1){
                        showAlert("Order Complete","Is order complete successfully ?",orderinfo.getStatus());
                    }
                    else if(orderinfo.getStatus()==-1){
                        showAlert("Order Remove","Are you want to remove order?",orderinfo.getStatus());
                    }
                }
                else{
                    if(orderinfo.getStatus()==0){
                        showAlert("Order Cancel","Are you want to cancel the order?",orderinfo.getStatus());
                    }
                    if(orderinfo.getStatus()==2){
                        showAlert("Order Receive","Are you receive the order?",orderinfo.getStatus());
                    }
                }

            }
        });
    }

    private void fillData() {
        customerName.setText(orderinfo.getUserFName());
        email.setText(orderinfo.getUserEmail());
        phonenbr.setText(orderinfo.getUserPhoneNbr());
        address.setText(orderinfo.getUserAddress());
        date.setText(orderinfo.getOrderTime()+"");
        qty.setText(orderinfo.getProductQty()+" qty");
        totalprice.setText("Rs. "+orderinfo.getProductPrice()*orderinfo.getProductQty());
        price.setText("Rs. "+orderinfo.getProductPrice());
        productname.setText(orderinfo.getProductName());
        Picasso.get().load(orderinfo.getProductImage()).placeholder(getDrawable(R.drawable.smerdapng)).into(image);
        if(PreferanceFile.getInstance(getApplicationContext()).isIsCompany()){
            if(orderinfo.getStatus()==0){
                orderStatus.setText("Pending");
            }
            else if(orderinfo.getStatus()==1){
                orderStatus.setText("On the Way");
                orderStatus.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            else if (orderinfo.getStatus()==2){
                orderStatus.setText("Order Complete");
                orderStatus.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                orderBtn.setVisibility(View.GONE);
            }
            else if(orderinfo.getStatus()==-1){
                orderStatus.setText("Order Cancel");
                orderBtn.setText("Remove Order");
            }
        }
        else{
            if(orderinfo.getStatus()==0){
                orderStatus.setText("Pending");
                orderBtn.setText("Cancel Order");
            }
            else if(orderinfo.getStatus()==1){
                orderStatus.setText("On the Way");
                orderBtn.setVisibility(View.GONE);
                orderStatus.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            else if (orderinfo.getStatus()==2){
                orderStatus.setText("Order Complete");
                orderStatus.setBackgroundColor(getResources().getColor(R.color.green));
                orderBtn.setText("Receive It?");
            }
        }

    }

    private void showAlert(String title,String message,final int status){
        new AlertDialog.Builder(OrderDetail.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(PreferanceFile.getInstance(getApplicationContext()).isIsCompany()){
                            if(status==0){
                                shipTheOrder();
                                sendNotification(orderinfo.getUserId(),"Order","Your order is on the way");
                            }

                            else if(status==1)
                                companyFinishOrder();
                            else if(status==-1)
                                deleteOrder();
                        }
                        else{
                            if(status==0) {
                                cancelOrder();
                                sendNotification(orderinfo.getCompanyId(),"Order Cancel",orderinfo.getUserFName()+" have cancel the order");
                            }
                            else if(status==2)
                                deleteOrder();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void shipTheOrder(){
        FirebaseDatabase.getInstance().getReference().child("Order").child(orderinfo.getCompanyId()).child(orderinfo.getOrderTime()+"")
                .child("status").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(OrderDetail.this, "Order is on the way", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void cancelOrder(){
        FirebaseDatabase.getInstance().getReference().child("Order").child(orderinfo.getCompanyId()).child(orderinfo.getOrderTime()+"")
                .child("status").setValue(-1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(OrderDetail.this, "Your order is cancel now", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    private void companyFinishOrder(){
        FirebaseDatabase.getInstance().getReference().child("Order").child(orderinfo.getCompanyId()).child(orderinfo.getOrderTime()+"")
                .child("status").setValue(2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(OrderDetail.this, "Order Complete Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void customerReciveOrder(){
        FirebaseDatabase.getInstance().getReference().child("Order").child(orderinfo.getCompanyId()).child(orderinfo.getOrderTime()+"")
                .child("status").setValue(3).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(OrderDetail.this, "Order Receive Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    private void deleteOrder(){
        FirebaseDatabase.getInstance().getReference().child("Order").child(orderinfo.getCompanyId()).child(orderinfo.getOrderTime()+"").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){}
                {
                    Toast.makeText(OrderDetail.this, "Order Delete Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void sendNotification(String order, String title, String msg){
        TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = title;
        NOTIFICATION_MESSAGE =msg;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notifcationBody.put("id",order);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
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
                        Toast.makeText(OrderDetail.this, "Request error", Toast.LENGTH_LONG).show();
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
}
