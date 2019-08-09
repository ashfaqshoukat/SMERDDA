package com.example.fyp.Fragments;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fyp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    Context context=null;
    String CHANEL_ID="SMERDA";
    String CHANEL_NAME="SMERDA NOTIFICATION";
    String CHANEL_DESC="Notification of growth analysis";
    Button button_send;
    public BlankFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blank, container, false);
        context=this.getActivity();
        button_send=(Button)view.findViewById(R.id.send_noti);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotification();
            }
        });
        return view;
    }
    private void displayNotification(){
        NotificationManager manager1= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,CHANEL_ID)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("It is working")
                .setContentText("Your first Notification")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        manager1.notify(11,builder.build());
        // NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        //notificationManagerCompat.notify(1,builder.build());
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANEL_ID,CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANEL_DESC);
            manager1.createNotificationChannel(channel);

        }

    }

}
