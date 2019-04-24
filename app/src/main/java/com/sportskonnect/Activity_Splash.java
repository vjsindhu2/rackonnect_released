package com.sportskonnect;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.sportskonnect.helpers.AppSignatureHelper;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Activity_Splash extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    Button btnStart;
    TextView tvWel, tvWel1;
    Typeface typeface, typeface1;
    String first = "TO";
    String Second = "<font color=#ffffff>  <b> RAC</font><font color=#00FF00>KONNECT  <b> </font>";
    private SharedPreference sharedPreference;
    private boolean islogedin = false;
    private GoogleApiClient googleApiClient;
    private String beforeEnable;
    private boolean isgpson = false;
    Handler handler = new Handler();

    private AppSignatureHelper appSignature;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        sharedPreference = new SharedPreference(this);
        islogedin = sharedPreference.getSharedPreferenceBoolean(this, SharedprefKeys.ISLOGEDIN, false);

        init();

//        try{
//            WifiManager wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            WifiInfo winfo = wifimanager.getConnectionInfo();
//            String MACAddress = "BSSID:"+winfo.getBSSID() +"\n"+"IPADDRESS:"+winfo.getIpAddress()+"\n"+"NETWORK ID:"+winfo.getNetworkId();
////            "IPADDRESS:"+winfo.getIpAddress()+"NETWORK ID"+winfo.getNetworkId()+
//            Toast.makeText(getApplicationContext(),MACAddress+"",Toast.LENGTH_LONG).show();
//
//
//        }catch (Exception ex){ex.printStackTrace();}
    }

    private void init() {
        checkPermissions();
        EnableGPSAutoMatically();

        appSignature = new AppSignatureHelper(this);
        appSignature.getAppSignatures();

        tvWel = (TextView) findViewById(R.id.tvWel);
        tvWel1 = (TextView) findViewById(R.id.tvWel1);
        btnStart = (Button) findViewById(R.id.btnStart);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/" + "Akzidenz Grotesk Next Bold.otf");
        typeface1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");
        tvWel.setTypeface(typeface);

        tvWel1.setText(Html.fromHtml(first + "  " + Second));
        tvWel1.setTypeface(typeface1);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibre Black.otf");
        btnStart.setTypeface(typeface1);

//        if (!sharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_ID,"").equals("")){
//
//        sendBroadcast(new Intent("YouWillNeverKillMe"));

//            startService(new Intent(getBaseContext(), OnlineorOfflineUser.class));
//
//        }

        if(islogedin) {

            btnStart.setVisibility(View.GONE);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Activity_Splash.this, Activity_Badminton.class);
                    startActivity(intent);
                    finish();
                }
            },2000);
        }else {

            btnStart.setVisibility(View.VISIBLE);
//            Intent intent = new Intent(Activity_Splash.this, Activity_Login.class);
//            startActivity(intent);
//            finish();

        }


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isgpson) {
                    if (islogedin) {
                        Intent intent = new Intent(Activity_Splash.this, Activity_Badminton.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Intent intent = new Intent(Activity_Splash.this, Activity_Login.class);
                        startActivity(intent);
                        finish();

                    }

                }else{

                    EnableGPSAutoMatically();

                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(Activity_Splash.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
//            getMyLocation();
        }
    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(Activity_Splash.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
//            getMyLocation();
        }

    }

    private void EnableGPSAutoMatically() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
//                            toast("Success");

                            isgpson = true;
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            toast("GPS is not on");
                            isgpson  = false;
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(Activity_Splash.this, 1000);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                            toast("Setting change not allowed");
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        toast("Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        toast("Failed");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");


            }
            if (resultCode == Activity.RESULT_CANCELED) {


                //Write your code if there's no result
            }
        }
    }

    private void toast(String message) {
        try {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
//            log("Window has been closed");
        }
    }
}
