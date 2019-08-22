package com.example.fyp.Services;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;
public class FirebaseIDService extends FirebaseMessagingService   {
        private static final String TAG = "FirebaseIDService";
    private static final String SUBSCRIBE_TO = "userABC";
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }


        private void sendRegistrationToServer(String token) {
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            preferences.edit().putString("token",token).apply();
        }
}