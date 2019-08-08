package com.example.fyp.Fragments;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fyp.R;

import static android.content.Context.NOTIFICATION_SERVICE;

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
        // This is the Notification Channel ID. More about this in the next section
          final String NOTIFICATION_CHANNEL_ID = "channel_id";

//User visible Channel Name
          final String CHANNEL_NAME = "Notification Channel";

// Importance applicable to all the notifications in this Channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

//Notification channel should only be created for devices running Android 26
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

             notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);

            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true);

            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true);

            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);

            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
            notificationChannel.setVibrationPattern(new long[] {
                    500,
                    500,
                    500,
                    500,
                    500
            });

            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

}
