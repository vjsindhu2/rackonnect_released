package com.sportskonnect;

import android.Manifest;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.FetchLocationRespo;
import com.sportskonnect.api.Callbacks.FetchSavedLocationRespo;
import com.sportskonnect.api.Callbacks.SaveCustomLocationRespo;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.sportskonnect.utility.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class LocationFetcherActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, com.google.android.gms.location.LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final long MIN_TIME = 0;
    private static final long MIN_DISTANCE = 0;
    static GoogleApiClient mGoogleApiClient;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    TextView tv_kms;
    private GoogleMap mMap;
    private Circle circle;
    private SeekBar radiusSeek;
    private TextView tvLocName;
    private List<Geofence> mGeofenceList;
    private double currentLatitude;
    private double currentLongitude;
    private PendingIntent mGeofencePendingIntent;
    private boolean hasLocationPermissions;
    private boolean isLocationServiceEnableRequestShown;
    private boolean isInit = true;
    private LocationManager locationManager;
    private boolean iscameramoved = false;
    private SharedPreference sharedPreference;
    private TextView _tvcurrentlocation;
    private Circle geoFenceLimits;
    private ImageView iv_marker;
    private double newLatitude;
    private double newLongitude;
    private int currentmeeterradius = 5000;
    private int circleprogress = 50;
    private TextView done_btn;
    private String saved_current_latitude = "";
    private String saved_current_longetude = "";
    private int saved_current_radius = 50;
    private boolean iswantcurrentlocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_fetcher);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fetchlocationfromdb(SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, ""));

        radiusSeek = findViewById(R.id.radiusSeek);
        tvLocName = findViewById(R.id.tvLocName);
        iv_marker = findViewById(R.id.iv_marker);
        tv_kms = findViewById(R.id.tv_kms);
        done_btn = findViewById(R.id.done_btn);
        _tvcurrentlocation = findViewById(R.id._tvcurrentlocation);

        sharedPreference = new SharedPreference(this);


        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserLocation(SharedPreference.getSharedPreferenceString(LocationFetcherActivity.this, SharedprefKeys.USER_ID, ""), currentmeeterradius, newLatitude, newLongitude, tvLocName.getText().toString().trim());

            }
        });



        radiusSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                currentmeeterradius = progress * 5000;

                circleprogress = progress;


                Log.d("KM", currentmeeterradius + "");
//                if (progress==0){
//                    tv_kms.setText(1+" KM");
//
//                }
                tv_kms.setText(currentmeeterradius / 1000 + " KM");


                circle.setRadius(progress * 50);

//                createGeofences(currentLatitude,currentLongitude,progress*10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (savedInstanceState == null) {

                            mGeofenceList = new ArrayList<Geofence>();

                            int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(LocationFetcherActivity.this);
                            if (resp == ConnectionResult.SUCCESS) {

                                initGoogleAPIClient();


                                createGeofences(currentLatitude, currentLongitude, 100);


                            } else {
                                Log.e("TAG", "Your Device doesn't support Google Play Services.");
                            }

                            // Create the LocationRequest object
                            mLocationRequest = LocationRequest.create()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

                                    .setInterval(5000)        // 10 seconds, in milliseconds
                                    .setFastestInterval(16); // 1 second, in milliseconds


                            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, LocationFetcherActivity.this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER

                        }


                    }


                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


    }

    public CircleOptions getCircleOptions(LatLng latlng, int radius) {
        CircleOptions co = new CircleOptions();
//        int color = Color.argb(20, 255, 0, 0);
        co.center(latlng);
        co.radius(radius);
        co.fillColor(Color.parseColor("#5a979db4"));
        co.strokeColor(Color.parseColor("#5a979db4"));
        co.strokeWidth(0.0f);
        return co;
    }

    public void animateCircle() {
        ValueAnimator vAnimator = ValueAnimator.ofInt(85, 100);
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setRepeatMode(ValueAnimator.REVERSE);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setDuration(300);

        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (Integer) valueAnimator.getAnimatedValue();
                circle.setRadius(animatedValue);
            }
        });
        vAnimator.start();
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    public void finishactivity(View view) {

        finish();
    }

    private void saveUserLocation(String user_id, int radius, double lat, double lng, String address) {

        Apis apis = RestAdapter.createAPI();
        Call<SaveCustomLocationRespo> callbackCall = apis.save_customlocation(user_id, radius, lat, lng, address);
        callbackCall.enqueue(new Callback<SaveCustomLocationRespo>() {
            @Override
            public void onResponse(Call<SaveCustomLocationRespo> call, Response<SaveCustomLocationRespo> response) {
                SaveCustomLocationRespo resp = response.body();
                if (resp != null && !resp.getError()) {


                    Activity_Badminton.iscomformsavedlocation = true;

                    Intent intent = new Intent(LocationFetcherActivity.this, Activity_Badminton.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


//                    dialogServerNotConnect();
                } else {


//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<SaveCustomLocationRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public void bounceAnimation(final ImageView bounce_marker) {

        bounce_marker.clearAnimation();

        TranslateAnimation transAnim = new TranslateAnimation(0, 0, -getDisplayHeight() / 15, 0);
        transAnim.setStartOffset(100);
        transAnim.setDuration(2000);
        transAnim.setRepeatCount(1);
//        transAnim.setRepeatMode(Animation.REVERSE);
        transAnim.setInterpolator(new AccelerateInterpolator());
        transAnim.setInterpolator(new BounceInterpolator());
        transAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                final int left = bounce_marker.getLeft();
                final int top = bounce_marker.getTop();
                final int right = bounce_marker.getRight();
                final int bottom = bounce_marker.getBottom();
                bounce_marker.layout(left, top, right, bottom);


            }
        });
        bounce_marker.startAnimation(transAnim);


    }

    public void clearbounceAnimation(ImageView bounce_marker) {
        bounce_marker.clearAnimation();

    }

    private int getDisplayHeight() {
        return this.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);


//        //move map camera
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));


        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        newLatitude = currentLatitude;
        newLongitude = currentLongitude;

        if(!iswantcurrentlocation){

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
            mGoogleMap.animateCamera(cameraUpdate);

        }


        locationManager.removeUpdates(this);


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();

        }
    }

    public void onLocationButtonClick(View view) {

        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        //mVibrator.vibrate(25);

        Log.i("TAG", "onLocationButtonClick: Clicked");

//        displayLocation();

        if (mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {

            bounceAnimation(iv_marker);
            getCurrentLocation();

        } else {
            mGoogleApiClient.connect();
        }
    }

    protected void getCurrentLocation() {

        iswantcurrentlocation = false;




        initGoogleAPIClient();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (!checkForLocationPermissions())
                getLocationPermissions();
//            setting();
//            open setting


        } else {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

                if (LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient) != null) {
                    Config.getInstance().setCurrentLatitude(""
                            + LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude());
                    Config.getInstance().setCurrentLongitude(""
                            + LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLongitude());
//                    getLocationName();


//                    clearbounceAnimation(iv_marker);

                }
            }
            /*else{
                System.out.println("Last Location : " + mockLocation);
				currentLatitude = ""+mockLocation.getLatitude();
				currentLongitude = ""+mockLocation.getLongitude();
			}*/

            if ((Config.getInstance().getCurrentLatitude() == null || Config.getInstance().getCurrentLongitude() == null)
                    || (Config.getInstance().getCurrentLatitude().equals("") || Config.getInstance().getCurrentLatitude().equals(""))) {
//            Toast.makeText(BaseAppCompatActivity.this, "Retrieving Current Location...", Toast.LENGTH_SHORT).show();
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                } else {
                    locationManager.requestLocationUpdates(NETWORK_PROVIDER, 0, 0, this);
                }
            } else {
                if (isInit) {
                    getData();
                    isInit = false;
                }
                if (mGoogleApiClient != null) {
                    mGoogleApiClient.disconnect();
                }
            }

            //			mHandler.postDelayed(periodicTask, 3000);
        }
    }

    private void getData() {

//        TODO:need to add Internet Permission

//        mGoogleMap.seton


    }

//    protected boolean checkLocationSettingsStatus() {
//
//        int locationMode = 0;
//        String locationProviders;
//        boolean isLocationServiceEnabled = false;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            try {
//                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
//            } catch (Settings.SettingNotFoundException e) {
//                e.printStackTrace();
//            }
//            isLocationServiceEnabled = (locationMode != Settings.Secure.LOCATION_MODE_OFF);
//        } else {
//            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//            isLocationServiceEnabled = !TextUtils.isEmpty(locationProviders);
//        }
//
//        if (!isLocationServiceEnabled) {
//            // notify user
//            if (!isFinishing()) {
//                if (!isLocationServiceEnableRequestShown) {
//                    isLocationServiceEnableRequestShown = true;
//                    PopupMessage popupMessage = new PopupMessage(this);
//                    popupMessage.show("Please enable location service from the Settings.", 0, "Open Settings");
//                    popupMessage.setPopupActionListener(new PopupMessage.PopupActionListener() {
//                        @Override
//                        public void actionCompletedSuccessfully(boolean result) {
//                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(myIntent);
//                            isLocationServiceEnableRequestShown = false;
//                        }
//
//                        @Override
//                        public void actionFailed() {
//                            isLocationServiceEnableRequestShown = false;
//                        }
//                    });
//                }
//            }
//        }
//        return isLocationServiceEnabled;
//    }

    protected void getLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,};
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            if (location == null) {
                if (mGoogleApiClient.isConnected()) {

                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                }

            } else {
                //If everything went fine lets get latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                if(!iswantcurrentlocation){
                    circle = mGoogleMap.addCircle(getCircleOptions(new LatLng(currentLatitude, currentLongitude), circleprogress));

                }else {

                    try{
                        if (!saved_current_latitude.equals("")&& !saved_current_longetude.equals("")&& saved_current_radius!=0)
                            circle = mGoogleMap.addCircle(getCircleOptions(new LatLng(Double.parseDouble(saved_current_latitude), Double.parseDouble(saved_current_longetude)), saved_current_radius));

                    }catch (Exception ex){ex.printStackTrace();}



                }



                Log.i("TAG", currentLatitude + " WORKS " + currentLongitude);

                //createGeofences(currentLatitude, currentLongitude);
                //registerGeofences(mGeofenceList);
            }


            try {

                if (mGoogleApiClient.isConnected()) {
                    LocationServices.GeofencingApi.addGeofences(
                            mGoogleApiClient,
                            getGeofencingRequest(),
                            getGeofencePendingIntent()
                    ).setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status status) {


                            if (status.isSuccess()) {
                                Log.i("TAG", "Saving Geofence");
//                                drawGeofence();

                            } else {
                                Log.e("TAG", "Registering geofence failed: " + status.getStatusMessage() +
                                        " : " + status.getStatusCode());
                            }
                        }
                    });
                }

            } catch (SecurityException securityException) {
                // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
                Log.e("TAG", "Error");
            }


            if (!iswantcurrentlocation) {
                if (mGoogleApiClient.isConnected()) {

                    getLocationName(currentLatitude, currentLongitude);


                }

            } else {

                if (!saved_current_latitude.equals("") && !saved_current_longetude.equals(""))
                    getLocationName(Double.parseDouble(saved_current_latitude), Double.parseDouble(saved_current_longetude));
            }


        }


    }

    private void getLocationName(double latitude, double longitude) {

        Apis apis = RestAdapter.creategoogleAPI();
        Call<FetchLocationRespo> callbackCall = apis.getLocationName(latitude + "," + longitude, true, "AIzaSyCY8z3-HeF4uogoq6IDXI1gm1YcnnXR8dc");
        callbackCall.enqueue(new Callback<FetchLocationRespo>() {
            @Override
            public void onResponse(Call<FetchLocationRespo> call, Response<FetchLocationRespo> response) {
                FetchLocationRespo resp = response.body();


                tvLocName.setText(resp.getformatedaddress());

                resp.getformatedaddress();



                if (circle != null) {

                    if(!iswantcurrentlocation){
                        mGoogleMap.clear();
                        circle = mGoogleMap.addCircle(getCircleOptions(new LatLng(newLatitude, newLongitude), circleprogress));

                    }else {
                        mGoogleMap.clear();
                        circle = mGoogleMap.addCircle(getCircleOptions(new LatLng(newLatitude, newLongitude), circleprogress));

                    }


                }


            }

            @Override
            public void onFailure(Call<FetchLocationRespo> call, Throwable t) {


            }
        });
    }

    // Draw Geofence circle on GoogleMap
    private void drawGeofence() {
        Log.d("TAG", "drawGeofence()");

        if (geoFenceLimits != null)
            geoFenceLimits.remove();


        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(newLatitude, newLongitude))
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(100);
        geoFenceLimits = mGoogleMap.addCircle(circleOptions);
    }

    /**
     * Create a Geofence list by adding all fences you want to track
     */
    public void createGeofences(double latitude, double longitude, float radius) {

        String id = UUID.randomUUID().toString();
        Geofence fence = new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, radius) // Try changing your radius

                .setExpirationDuration(Geofence.NEVER_EXPIRE)

                .build();
        mGeofenceList.add(fence);

        for (int i = 0; i < mGeofenceList.size(); i++) {
            Log.d("MGEOFENCELIST", mGeofenceList.get(i).getRequestId());

        }

    }

    private GeofencingRequest getGeofencingRequest() {

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();

    }

    private PendingIntent getGeofencePendingIntent() {

        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mGoogleMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(false);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();

//                mGoogleMap.setMyLocationEnabled(true);

                initmap();
//                LatLng coordinate = new LatLng(Double.parseDouble(saved_current_latitude), Double.parseDouble(saved_current_longetude));
//
//                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));

            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            initmap();
//            mGoogleMap.setMyLocationEnabled(true);
        }


    }

    public void initmap() {

        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                if (!iswantcurrentlocation){
                    mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
                    mGoogleMap.setMaxZoomPreference(18f);
                    iscameramoved = true;

                }


//                animateCircle();


            }
        });

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition postion = mMap.getCameraPosition();
                LatLng center = postion.target;


                if (iscameramoved) {


                    if (!iswantcurrentlocation){
                        getLocationName(center.latitude, center.longitude);

                    }else {
                        if (!saved_current_latitude.equals("")&& !saved_current_longetude.equals(""))
                        getLocationName(Double.parseDouble(saved_current_latitude), Double.parseDouble(saved_current_longetude));

                    }

                    newLatitude = center.latitude;
                    newLongitude = center.longitude;

                    Config.getInstance().setNewLatitude(String.valueOf(center.latitude));
                    Config.getInstance().setNewLongitude(String.valueOf(center.longitude));

//                    createGeofences(center.latitude,center.longitude,100);


                }
                iscameramoved = false;

            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationFetcherActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    protected boolean checkForLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                /*String[] permissions = new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_LOCATION);*/
                return hasLocationPermissions = false;
            } else {
                return hasLocationPermissions = true;
            }
        } else {
            return hasLocationPermissions = true;
        }
    }


    private void fetchlocationfromdb(String user_id) {

        Apis apis = RestAdapter.createAPI();
        Call<FetchSavedLocationRespo> callbackCall = apis.fetchsavedlocation(user_id);
        callbackCall.enqueue(new Callback<FetchSavedLocationRespo>() {
            @Override
            public void onResponse(Call<FetchSavedLocationRespo> call, Response<FetchSavedLocationRespo> response) {
                FetchSavedLocationRespo resp = response.body();
                if (resp != null && !resp.getError()) {

                    Log.d("DATALOCATION", new Gson().toJson(resp));

                    tvLocName.setText(resp.getAddress());



                    saved_current_latitude = resp.getLat();

                    saved_current_longetude = resp.getLng();
                    saved_current_radius = (int) ((Double.parseDouble(resp.getRadius())/0.000621371)/100);


                    Log.d("RADIUS",saved_current_radius+"");
//                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(Double.parseDouble(saved_current_latitude), Double.parseDouble(saved_current_longetude)));
                    LatLng coordinate = new LatLng(Double.parseDouble(saved_current_latitude), Double.parseDouble(saved_current_longetude));

//                    radiusSeek.setProgress(c);

                    try{
//                        if (!saved_current_latitude.equals("")&& !saved_current_longetude.equals("")&& saved_current_radius!=0)
//                            circle = mGoogleMap.addCircle(getCircleOptions(new LatLng(currentLatitude, currentLongitude), saved_current_radius*50));

//                        circle.setRadius(saved_current_radius * 10);

                    }catch (Exception ex){ex.printStackTrace();}


                    if (saved_current_radius==50){

                        Log.d("SEEKRUN","25");
                        radiusSeek.setProgress(1);
                        radiusSeek.refreshDrawableState();
                        circle.setRadius(5000);
                        circleprogress = 50;


                    }else if(saved_current_radius==100){

                        Log.d("SEEKRUN","25 100wala");


                        radiusSeek.setProgress(2);
                        radiusSeek.refreshDrawableState();
                        circle.setRadius(10000);
                        circleprogress = 50*2;


                    }else if(saved_current_radius==150){
                        Log.d("SEEKRUN","150");

                        radiusSeek.setProgress(3);
                        radiusSeek.refreshDrawableState();
                        circle.setRadius(15000);
                        circleprogress = 50*3;


                    }else if(saved_current_radius==200){
                        Log.d("SEEKRUN","200");

                        radiusSeek.setProgress(4);
                        radiusSeek.refreshDrawableState();
                        circle.setRadius(20000);
                        circleprogress = 50*4;

                    }else {
                        Log.d("SEEKRUN","ELSE");
                        radiusSeek.setProgress(5);
                        radiusSeek.refreshDrawableState();
                        circle.setRadius(5000);
                        circleprogress = 50*5;


                    }



//                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));

                   /* CameraPosition.Builder camBuilder = CameraPosition.builder();
                    camBuilder.bearing(45);
                    camBuilder.tilt(30);
                    camBuilder.target(coordinate);
                    camBuilder.zoom(18);
                    CameraPosition cp = camBuilder.build();

                    mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));

                    mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
                    mGoogleMap.setMaxZoomPreference(18f);
                    iscameramoved = false;*/

                } else {


                }
            }

            @Override
            public void onFailure(Call<FetchSavedLocationRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }
}
