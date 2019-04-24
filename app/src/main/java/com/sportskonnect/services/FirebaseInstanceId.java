package com.sportskonnect.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceId extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String token = com.google.firebase.iid.FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("VGFood", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FcmTokenC", token);
        editor.apply();

    }

}
