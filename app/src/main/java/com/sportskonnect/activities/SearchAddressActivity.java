package com.sportskonnect.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;




import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sportskonnect.BuildConfig;
import com.sportskonnect.LocationFetcherActivity;
import com.sportskonnect.R;
import com.sportskonnect.adapters.SearchResultsRecyclerAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.FetchLocationRespo;
import com.sportskonnect.api.Callbacks.PlaceAPIRESPO;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.helpers.AppConstants;
import com.sportskonnect.modal.CustomAutoComplete;
import com.sportskonnect.modal.PlaceBean;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.sportskonnect.utility.Config;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class SearchAddressActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, com.google.android.gms.location.LocationListener {

    private static final long MIN_TIME = 0;
    private static final long MIN_DISTANCE = 0;
    private GoogleMap mMap;
    private Circle circle;
    private ConstraintLayout map_cl;
    private TextView tvLocName;
    EditText etxt_search_address_work;
    private RecyclerView rvSearchResults;
    private String txtinput = "";
    SwipeRefreshLayout swipreferesh;
    private GoogleApiClient mGoogleApiClient;
    private SearchResultsRecyclerAdapter adapterSearch;
    private int locationSelect;
    private SharedPreference sharedPreference;
    private ImageView iv_marker;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    private double currentLatitude;
    private double currentLongitude;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private double newLatitude;
    private double newLongitude;
    private boolean hasLocationPermissions = false;
    private boolean iscameramoved = false;

    public TextView done_btn;
    private String cityName = "";
    private String strAddress="";

    private ArrayList<String> resultData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);


        etxt_search_address_work = findViewById(R.id.etxt_search_address_work);
        swipreferesh = findViewById(R.id.swipreferesh);

        swipreferesh.setEnabled(false);
        rvSearchResults = (RecyclerView) findViewById(R.id.rv_search_home_work_results);
        map_cl = (ConstraintLayout) findViewById(R.id.map_cl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvSearchResults.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearchResults.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        setupAutoCompleteFragment();

       /* etxt_search_address_work.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                txtinput = etxt_search_address_work.getText().toString();


                swipreferesh.setRefreshing(true);

//                populateSearchResults();

                if (txtinput.equals("")) {
                    swipreferesh.setRefreshing(false);

                    rvSearchResults.setVisibility(View.GONE);
                    map_cl.setVisibility(View.VISIBLE);

                } else {


                    PlaceListTask placesListTask = new PlaceListTask(txtinput);
                    placesListTask.setStrAddress(txtinput);
                    placesListTask.execute();


//                    getplacename(txtinput,"");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                rvSearchResults.setVisibility(View.GONE);
//                map_cl.setVisibility(View.VISIBLE);

//                if (s.equals("")){
//                    rvSearchResults.setVisibility(View.GONE);
//                    map_cl.setVisibility(View.VISIBLE);
//
//                }
            }
        });
*/



//        swipreferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipreferesh.setRefreshing(false);
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);


        tvLocName = findViewById(R.id.tvLocName);
        iv_marker = findViewById(R.id.iv_marker);
        done_btn = findViewById(R.id.done_btn);

        sharedPreference = new SharedPreference(this);





        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (savedInstanceState == null) {

                            initGoogleAPIClient();


                            // Create the LocationRequest object
                            mLocationRequest = LocationRequest.create()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

                                    .setInterval(5000)        // 10 seconds, in milliseconds
                                    .setFastestInterval(16); // 1 second, in milliseconds


                            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, SearchAddressActivity.this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER

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


//        mGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
//                .build();




        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationSelect = AppConstants.LOCATION_SELECTED_ONITEMCLICK;

                PlaceBean placeBean = new PlaceBean();

                placeBean.setAddress(tvLocName.getText().toString().trim());
                placeBean.setLatitude(String.valueOf(currentLatitude));
                placeBean.setLongitude(String.valueOf(currentLongitude));
                placeBean.setCityname(cityName);
                placeBean.setName((String) "SearchLocation");

                Intent intent = new Intent();
                intent.putExtra("location", placeBean);
//                    intent.putExtra("locationSelect", locationSelect);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public void finishSearchactivity(View view) {

        finish();
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


    @Override
    public void onConnected(@Nullable Bundle bundle) {
/*
        PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, "infopark", null, null);

        result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
            @Override
            public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                Log.i("TAG", "onConnected: Result" + autocompletePredictions);


            }
        });
*/


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

                Log.i("TAG", currentLatitude + " WORKS " + currentLongitude);

                //createGeofences(currentLatitude, currentLongitude);
                //registerGeofences(mGeofenceList);
            }


            if (mGoogleApiClient.isConnected()) {

                getLocationName(currentLatitude, currentLongitude);

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
                cityName = resp.getcity();

//                Log.d("CITYNAME",resp.getcity());

            }

            @Override
            public void onFailure(Call<FetchLocationRespo> call, Throwable t) {


            }
        });
    }


    private String returnloactionname(double latitude, double longitude) {
        final String[] locationname = {""};
        Apis apis = RestAdapter.creategoogleAPI();
        Call<FetchLocationRespo> callbackCall = apis.getLocationName(latitude + "," + longitude, true, "AIzaSyCY8z3-HeF4uogoq6IDXI1gm1YcnnXR8dc");
        callbackCall.enqueue(new Callback<FetchLocationRespo>() {
            @Override
            public void onResponse(Call<FetchLocationRespo> call, Response<FetchLocationRespo> response) {
                FetchLocationRespo resp = response.body();


                locationname[0] = resp.getformatedaddress();

            }

            @Override
            public void onFailure(Call<FetchLocationRespo> call, Throwable t) {


            }
        });

        return locationname[0];
    }

    private void getplacename(String input,String key) {

        Apis apis = RestAdapter.creategoogleAPI();
        Call<PlaceAPIRESPO> callbackCall = apis.getplacedata(input, "AIzaSyCY8z3-HeF4uogoq6IDXI1gm1YcnnXR8dc");
        callbackCall.enqueue(new Callback<PlaceAPIRESPO>() {
            @Override
            public void onResponse(Call<PlaceAPIRESPO> call, Response<PlaceAPIRESPO> response) {
                PlaceAPIRESPO resp = response.body();



              /*  if (resp!=null){

                    for (int i=0;i<resp.getPredictions().size();i++){

                        CustomAutoComplete customAutoComplete = new CustomAutoComplete(resp.getPredictions().get(i).get)

                    }


                }

                for (int i=0;i<resp.getPredictions().size();i++){

                 strAddress =  resp.getPredictions().get(i).getDescription();

                 resultData.add(strAddress);
                }


                if (resp != null) {
                    rvSearchResults.setVisibility(View.VISIBLE);
                    map_cl.setVisibility(View.GONE);

                    populateSearchResults(resp.getPredictions());

                } else if (result.size() <= 0) {


                    swipreferesh.setRefreshing(false);
                    rvSearchResults.setVisibility(View.GONE);
                    map_cl.setVisibility(View.VISIBLE);

                } else {
                    swipreferesh.setRefreshing(false);

                }*/


            }

            @Override
            public void onFailure(Call<PlaceAPIRESPO> call, Throwable t) {


            }
        });
    }




    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e("TAG", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();

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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        mMap.animateCamera(cameraUpdate);

        locationManager.removeUpdates(this);


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.getUiSettings().setMapToolbarEnabled(false);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                initGoogleAPIClient();

//                mGoogleMap.setMyLocationEnabled(true);

                initmap();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            initGoogleAPIClient();
            initmap();
//            mGoogleMap.setMyLocationEnabled(true);
        }
        /*mGoogleApiClient.connect();*/
    }

    public void initmap() {

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.setMaxZoomPreference(18f);
                iscameramoved = true;

            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition postion = mMap.getCameraPosition();
                LatLng center = postion.target;


                if (iscameramoved) {
                    getLocationName(center.latitude, center.longitude);

                    newLatitude = center.latitude;
                    newLongitude = center.longitude;

                    Config.getInstance().setNewLatitude(String.valueOf(center.latitude));
                    Config.getInstance().setNewLongitude(String.valueOf(center.longitude));


                }
                iscameramoved = false;

            }
        });

    }

  /*  @SuppressLint("StaticFieldLeak")
    private class PlaceListTask extends AsyncTask<String, Integer, ArrayList<AutocompletePrediction>> {

        private String strAddress;

        public PlaceListTask(String strAddress) {
            super();
            this.strAddress = strAddress;
        }

        public String getStrAddress() {
            return strAddress;
        }

        public void setStrAddress(String strAddress) {
            if (strAddress.equals("")) {

                rvSearchResults.setVisibility(View.GONE);
                map_cl.setVisibility(View.VISIBLE);
            }
            this.strAddress = strAddress;
        }

        @Override
        protected ArrayList<AutocompletePrediction> doInBackground(String... params) {

            if (mGoogleApiClient.isConnected()) {
                Log.i("TAG", "Starting autocomplete query for: " + strAddress);

                // Submit the query to the autocomplete API and retrieve a PendingResult that will
                // contain the results when the query completes.


                PendingResult<AutocompletePredictionBuffer> results =
                        Places.GeoDataApi
                                .getAutocompletePredictions(mGoogleApiClient, strAddress, null,
                                        null);

                // This method should have been called off the main UI thread. Block and wait for at most 60s
                // for a result from the API.
                AutocompletePredictionBuffer autocompletePredictions = results
                        .await(60, TimeUnit.SECONDS);

                // Confirm that the query completed successfully, otherwise return null
                final com.google.android.gms.common.api.Status status = autocompletePredictions.getStatus();
                if (!status.isSuccess()) {
//                Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
//                        Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "Error getting autocomplete prediction API call: " + status.toString());
                    autocompletePredictions.release();
                    return null;
                }

                Log.i("TAG", "Query completed. Received " + autocompletePredictions.getCount()
                        + " predictions.");

                // Freeze the results immutable representation that can be stored safely.
                return DataBufferUtils.freezeAndClose(autocompletePredictions);
            }
            Log.e("TAG", "Google API client is not connected for autocomplete query.");
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<AutocompletePrediction> result) {
            super.onPostExecute(result);

            Log.i("TAG", "onPostExecute: Result" + result);


            if (result != null) {
                rvSearchResults.setVisibility(View.VISIBLE);
                map_cl.setVisibility(View.GONE);

                populateSearchResults(result);

            } else if (result.size() <= 0) {


                swipreferesh.setRefreshing(false);
                rvSearchResults.setVisibility(View.GONE);
                map_cl.setVisibility(View.VISIBLE);

            } else {
                swipreferesh.setRefreshing(false);

            }
        }
    }

*/
    /*private void populateSearchResults(ArrayList<AutocompletePrediction> result) {

        if (adapterSearch == null) {
            adapterSearch = new SearchResultsRecyclerAdapter(this, mGoogleApiClient, result);

            adapterSearch.setSearchResultsRecyclerAdapterListener(new SearchResultsRecyclerAdapter.SearchResultsRecyclerAdapterListener() {

                PlaceBean placeBean;

                @Override
                public void onItemSelected(Place place) {

                    locationSelect = AppConstants.LOCATION_SELECTED_ONITEMCLICK;

                    placeBean = new PlaceBean();

                    placeBean.setAddress(String.valueOf(place.getAddress()));
                    placeBean.setLatitude(String.valueOf(place.getLatLng().latitude));
                    placeBean.setLongitude(String.valueOf(place.getLatLng().longitude));
                    placeBean.setName((String) place.getName());
                    placeBean.setCityname(cityName);

                    Intent intent = new Intent();
                    intent.putExtra("location", placeBean);
//                    intent.putExtra("locationSelect", locationSelect);
                    setResult(RESULT_OK, intent);
                    finish();

                }

                @Override
                public void onSnackBarShow(String message) {

                }
            });
            rvSearchResults.setAdapter(adapterSearch);
        } else {

            adapterSearch.setmResultList(result);
            adapterSearch.notifyDataSetChanged();
        }

//        setProgressScreenVisibility(false, false);
        swipreferesh.setRefreshing(false);

    }
*/





    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active



        if (mGoogleApiClient != null) {

            if (mGoogleApiClient.isConnected()){
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

            }

        }
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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
                                ActivityCompat.requestPermissions(SearchAddressActivity.this,
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
                            initGoogleAPIClient();
                        }
                        mMap.setMyLocationEnabled(true);
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



    /**
     * Sets up the autocomplete fragment to show place details when a place is selected.
     */
    private void setupAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                showPlace(getString(R.string.place_autocomplete_search_hint), place);

            }

            @Override
            public void onError(Status status) {
                showResponse("An error occurred: " + status);
            }
        });
    }


    /**
     * Shows some text and clears any previously set image.
     */
    private void showResponse(String response) {
        /*textView.setText(response);
        imageView.setImageResource(0);*/




    }

    /**
     * Shows the name of a place, and its photo.
     */
    private void showPlace(String source, Place place) {
        locationSelect = AppConstants.LOCATION_SELECTED_ONITEMCLICK;

        PlaceBean placeBean = new PlaceBean();

        placeBean.setAddress(String.valueOf(place.getAddress()));
        placeBean.setLatitude(String.valueOf(place.getLatLng().latitude));
        placeBean.setLongitude(String.valueOf(place.getLatLng().longitude));
        placeBean.setName((String) place.getName());
        placeBean.setCityname(cityName);

        Intent intent = new Intent();
        intent.putExtra("location", placeBean);
//                    intent.putExtra("locationSelect", locationSelect);
        setResult(RESULT_OK, intent);
        finish();

    }
}
