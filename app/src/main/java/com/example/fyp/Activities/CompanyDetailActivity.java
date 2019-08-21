package com.example.fyp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.fyp.Dialogs.FeedbackDialog;
import com.example.fyp.Models.COMMENT;
import com.example.fyp.Models.COMPANYINFO;
import com.example.fyp.Models.RATING;
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
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyDetailActivity extends AppCompatActivity implements RatingDialogListener {

    private static Context instance;
    COMPANYINFO companyInfo;
    TextView comapnayName, comapanyEmail, feebackBtn, rateCompany;
    ImageView profileImage;
    Toolbar toolbar;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    int check=0;
    private List<COMPANYINFO> companyInfolist;
    private List<RATING> ratingList = new ArrayList<>();

    public static Context geInstance() {
        return instance;
    }


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
        setContentView(R.layout.activity_company_detail);
        instance = this;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        comapnayName = findViewById(R.id.comapnyname);
        comapanyEmail = findViewById(R.id.email);
        feebackBtn = findViewById(R.id.feebackBtn);
        rateCompany = findViewById(R.id.ratecompany);
        profileImage = findViewById(R.id.image);
        toolbar = findViewById(R.id.toolbar);
        setUpToolbar();
        feebackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                showDialog();
                FragmentManager fm = getSupportFragmentManager();
                FeedbackDialog editNameDialogFragment = new FeedbackDialog();
                editNameDialogFragment.show(fm, "Feedback Dialog");

            }
        });


        rateCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Feedback");
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(companyInfo.getCompanyId())) {

                            for (DataSnapshot feedback : dataSnapshot.child(companyInfo.getCompanyId()).getChildren()) {
                                COMMENT comment = feedback.getValue(COMMENT.class);
                                if (comment.getUserId().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    Toast.makeText(CompanyDetailActivity.this, "You have already submitted feedback", Toast.LENGTH_SHORT).show();
                                    check=check+1;
                                }
                            }


                        }
                        else{
                            check=0;
                        }

                        if(check==0){
                            showDialog();
                        }
                        check=0;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        String info = getIntent().getExtras().getString("comapnyinfo");
        companyInfo = new Gson().fromJson(info, COMPANYINFO.class);
        comapnayName.setText(companyInfo.getCompName());
//        comapanyEmail.setText(companyInfo.getEmail());
        getSupportActionBar().setTitle(companyInfo.getCompName());
        if(!companyInfo.getProfileimage().equalsIgnoreCase("")){
            Picasso.get().load(companyInfo.getProfileimage()).placeholder(getDrawable(R.drawable.smerdapng)).into(profileImage);
        }
        getAllCompanyRating();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.companyproducts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.companyProduct:
             Intent intent=new Intent(CompanyDetailActivity.this,CompanyProductActivity.class);
             intent.putExtra("id",companyInfo.getCompanyId());
             intent.putExtra("name",companyInfo.getCompName());
             startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showPieChart(List<RATING> ratingListt) {
        AnyChartView anyChartView = findViewById(R.id.picchart);
        Pie pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
//                Toast.makeText(CompanyDetailActivity.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < ratingListt.size(); i++) {
            data.add(new ValueDataEntry(ratingListt.get(i).getName(), ratingListt.get(i).getRating()));
        }
        pie.data(data);
        pie.title("Comapny Liked By customer");
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("RATING")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);
    }


    private void showDialog() {
       new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(2)
                .setTitle("Rate this Compnay")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setDefaultComment("Good Company")
                .setStarColor(R.color.colorPrimary)
                .setNoteDescriptionTextColor(R.color.design_default_color_primary)
                .setTitleTextColor(R.color.design_default_color_primary)
                .setDescriptionTextColor(R.color.colorAccent)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.design_default_color_primary_dark)
                .setCommentTextColor(R.color.black)
                .setCommentBackgroundColor(R.color.white)
//                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(CompanyDetailActivity.this)
//                .setTargetFragment(this, 100) // only if listener is implemented by fragment
                .show();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {
        COMMENT comment = new COMMENT();
        comment.setRating(i);
        comment.setMessage(s);
        comment.setComapnayName(companyInfo.getCompName());
        comment.setComapanyId(companyInfo.getCompanyId());
        comment.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        comment.setDate(System.currentTimeMillis());
        myRef.child("Feedback").child(companyInfo.getCompanyId()).child(System.currentTimeMillis() + "").setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CompanyDetailActivity.this, "RATING upload successfully", Toast.LENGTH_SHORT).show();
                    getAllCompanyRating();
                }
            }
        });




sendNotification(s);


    }



    double rating = 0;
    int count = 0;
    COMMENT comment;

    private void getAllCompanyRating() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ratingList.clear();
                for (DataSnapshot comapny : dataSnapshot.child("Feedback").getChildren()) {
                    for (DataSnapshot eachCompany : comapny.getChildren()) {
                        comment = eachCompany.getValue(COMMENT.class);
                        rating = rating + comment.getRating();
                        count++;

                    }
                    double avgrating = 0;
                    if (count != 0)
                        avgrating = rating / count;
                    RATING rating1 = new RATING();
                    rating1.setName(comment.getComapnayName());
                    rating1.setRating(avgrating);
                    ratingList.add(rating1);
                    rating = 0;
                    count=0;
                }
                showPieChart(ratingList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                        Toast.makeText(CompanyDetailActivity.this, "Request error", Toast.LENGTH_LONG).show();
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

    private void sendNotification(String s){
        TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = companyInfo.getCompName();
        NOTIFICATION_MESSAGE =s;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notifcationBody.put("id",companyInfo.getCompanyId());

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }
}


