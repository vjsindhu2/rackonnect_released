package com.sportskonnect.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.UserOnlineStatus;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static android.os.SystemClock.elapsedRealtime;

public class OnlineorOfflineUser extends Service {

//    private SharedPreference sharedPreference;
//    Context mContext = this;
//
//    public OnlineorOfflineUser() {
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//
//        sharedPreference = new SharedPreference(mContext);
//
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("ClearFromRecentService", "Service Started");
////        Toast.makeText(mContext,"Service started",Toast.LENGTH_SHORT).show();
//
//
//        if (new SharedPreference(mContext).getSharedPreferenceString(mContext, SharedprefKeys.USER_ID, "") != "") {
//
//            onlineornot("online", new SharedPreference(mContext).getSharedPreferenceString(mContext, SharedprefKeys.USER_ID, ""));
//
//        }
//
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("ClearFromRecentService", "Service Destroyed");
//        Toast.makeText(mContext,"END",Toast.LENGTH_SHORT).show();
//
//    }
//
//
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        Log.d("ClearFromRecentService", "END");
//        //Code here
//
//        Toast.makeText(mContext,"END",Toast.LENGTH_SHORT).show();
////        onlineornot("offline", new SharedPreference(mContext).getSharedPreferenceString(mContext, SharedprefKeys.USER_ID, ""));
//
////        super.onTaskRemoved(rootIntent);
//
//        stopSelf();
//    }
//
//
//
//
//
//    private void onlineornot(String status, String user_id) {
//
////        loader_dialog(this);
//
//        Apis apis = RestAdapter.createAPI();
//        Call<UserOnlineStatus> callbackCall = apis.saveonlineorNot(status, user_id);
//        callbackCall.enqueue(new Callback<UserOnlineStatus>() {
//            @Override
//            public void onResponse(Call<UserOnlineStatus> call, Response<UserOnlineStatus> response) {
//
//
//                UserOnlineStatus resp = response.body();
//                Log.d("LOGINRESPO", new Gson().toJson(resp));
//                if (resp != null && !resp.getError()) {
//
//                    if (status.equals("offline")) {
////                        stopSelf();
//                    }
//
//
////                    dialogServerNotConnect();
//                } else {
//
//
////                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserOnlineStatus> call, Throwable t) {
//                Log.e("onFailure", t.getMessage());
////                dialogServerNotConnect(Activity_Login.this,t.getMessage());
//            }
//        });
//    }
//
//
//    public static void disconnectToServer(String status, String user_id) {
//
////        loader_dialog(this);
//
//        Apis apis = RestAdapter.createAPI();
//        Call<UserOnlineStatus> callbackCall = apis.saveonlineorNot(status, user_id);
//        callbackCall.enqueue(new Callback<UserOnlineStatus>() {
//            @Override
//            public void onResponse(Call<UserOnlineStatus> call, Response<UserOnlineStatus> response) {
//
//
//                UserOnlineStatus resp = response.body();
//                Log.d("LOGINRESPO", new Gson().toJson(resp));
//                if (resp != null && !resp.getError()) {
//
//                    if (status.equals("offline")) {
////                        stopSelf();
//                    }
//
//
////                    dialogServerNotConnect();
//                } else {
//
//
////                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserOnlineStatus> call, Throwable t) {
//                Log.e("onFailure", t.getMessage());
////                dialogServerNotConnect(Activity_Login.this,t.getMessage());
//            }
//        });
//    }



    private static final String TAG = "StickyService";


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("YouWillNeverKillMe"));
    }

}
