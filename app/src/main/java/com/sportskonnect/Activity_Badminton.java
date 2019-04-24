package com.sportskonnect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.sportskonnect.adapters.DoublesRvAdapter;
import com.sportskonnect.adapters.SelectedGamesAdapter;
import com.sportskonnect.adapters.SinglesRvAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.CreateMatchRespo;
import com.sportskonnect.api.Callbacks.FetchLocationRespo;
import com.sportskonnect.api.Callbacks.FetchOpponentsRespo;
import com.sportskonnect.api.Callbacks.FetchSavedLocationRespo;
import com.sportskonnect.api.Callbacks.MatchListDualResponse;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.database.DatabaseHandler;
import com.sportskonnect.helpers.AppConstants;
import com.sportskonnect.modal.DualMatchListDatum;
import com.sportskonnect.modal.OpponetsDatum;
import com.sportskonnect.modal.SelectedGameModal;
import com.sportskonnect.modal.UserOnlineStatus;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.sportskonnect.utility.RecyclerTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.codetail.widget.RevealFrameLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static com.sportskonnect.services.MainService.mSocket;

public class Activity_Badminton extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener, View.OnTouchListener, LocationListener {


    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    public static SwipeRefreshLayout swipreferesh;
    public static boolean iscomformsavedlocation = false;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    TextView tvFinalName, tvAvaPlayers, tvMin, tvAreaName, tvChat, tvName, tvAreaName2, tvChat1, tvName2, tvAreaName3, main_text_title;
    LinearLayout llSingles, llDoubles, llSingle, llDouble;
    View view1, view2;
    String text = "<font color=#ffffff>RAC</font><font color=#00FF00>KONNECT</font>";
    Toolbar mToolbar;
    ImageView location;
    DrawerLayout drawerLayout;
    EditText search_et;
    TextView tvLocName;
    TextView selected_game_tv, experties_tv;
    ImageView search_opp;
    TextView statusrack;
    private GuideView mGuideView;
    private GuideView.Builder builder;
    private SharedPreference sharedPreference;
    private DatabaseHandler db;
    private RecyclerView singles_rv, doubless_rv;
    private int selected_cat = 0;
    private int selected_level = 0;
    private NestedScrollView ll_filter;
    private List<OpponetsDatum> opponetsDatumList = new ArrayList<>();
    private List<DualMatchListDatum> dualMatchListDatumList = new ArrayList<>();
    private Dialog choosegame_dialog;
    private SelectedGamesAdapter mAdapter;
    private TextView filter_tv, lbl_tvFemale, lbl_tvMale, lbl_allplayer;
    private ImageView ivFilter;
    private boolean isOpen = false;
    private RevealFrameLayout revealFrameLayout;
    private boolean hidden = true;
    private Animator animator;
    private ImageView filter_close;
    private CrystalRangeSeekbar ageSeekbar;
    private TextView age_minval, range_lbl, age_maxval;
    private RadioGroup level_group;
    private RadioButton levelRadiobtn;
    private Button applyfilter_btn;
    private CardView male_cv, female_cv, allplayer_cv;
    private String gender = "";
    private String minAge = "";
    private String maxAge = "";
    private View lyt_include;
    private View lyt_include_two;
    private ImageView ivLight;
    private int online_flag = 0;
    private String user_id = "";
    private boolean issearchvisible = false;
    private SinglesRvAdapter singlesRVAdapter;
    private String timePref = "";
    private CardView anytym_cv, morning_cv, afternoon_cv, evening_cv;
    private TextView lbl_anytym, lbl_tvMorning, lbl_tvAfternoon, lbl_tvevening;
    private boolean isfilterClicked = false;
    private DoublesRvAdapter doublesRVAdapter;
    private boolean issingleclicked = false;
    private boolean isdoubleclicked = false;
    private String current_latitude = "";
    private String current_longetude = "";
    private String current_latitude_user = "";
    private String current_longetude_user = "";
    private String current_radius = "100";
    private String tokenID = "";
    private String gamenotipush = "";
    private String cancelnotipush = "";
    private String scorenotipush = "";
    private String gamenotiemail = "";
    private String cancelnotiemail = "";
    private String scorenotiemail = "";
    private String sound = "";
    private String chat = "";
    private LinearLayout genderfilter_section;
    private String maxage_filter = "";
    private String minage_filter = "";
    private LinearLayout tabscontainer_ll;
    private GestureDetector gestureDetector;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private Boolean mRequestingLocationUpdates;
    private PopupWindow racknowpopup;
    private boolean doubleBackToExitPressedOnce=false;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badminton);
        sharedPreference = new SharedPreference(this);

        db = new DatabaseHandler(this);
        issingleclicked = true;

//        initlocation();


        JSONObject json = new JSONObject();
        try {
            json.put("connected", true);
            json.put("senderId", Integer.parseInt(SharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_ID,"")));
        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
        mSocket.emit(AppConstants.SOCKET_IS_ONLINE, json);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);




        init();

       /* Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
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

*/
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

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i("TAG", "All location settings are satisfied.");

//                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        if (iscomformsavedlocation) {

                        } else {
                            updateLocationUI();

                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i("TAG", "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Activity_Badminton.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i("TAG", "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e("TAG", errorMessage);

//                                Toast.makeText(Activity_ChooseSports.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        if (iscomformsavedlocation) {

                        } else {
                            updateLocationUI();

                        }
                    }
                });
    }


    private void initlocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                if (iscomformsavedlocation) {

                } else {
                    updateLocationUI();

                }
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    private void updateLocationUI() {

        if (mCurrentLocation != null) {


//            getLocationName(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {

        user_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        location = findViewById(R.id.location);
        tabscontainer_ll = findViewById(R.id.tabscontainer_ll);

        tvFinalName = (TextView) findViewById(R.id.tvFinalName);
        tvAvaPlayers = (TextView) findViewById(R.id.tvAvaPlayers);
        tvMin = (TextView) findViewById(R.id.tvMin);
        tvAreaName = (TextView) findViewById(R.id.tvAreaName);
        statusrack = (TextView) findViewById(R.id.statusrack);
        swipreferesh = (SwipeRefreshLayout) findViewById(R.id.swipreferesh);
        search_et = (EditText) findViewById(R.id.search_et);
        tvAreaName2 = (TextView) findViewById(R.id.tvAreaName2);
        tvName = (TextView) findViewById(R.id.tvName);
        tvChat = (TextView) findViewById(R.id.tvChat);
        tvChat1 = (TextView) findViewById(R.id.tvChat1);
        search_opp = (ImageView) findViewById(R.id.search_opp);
        tvName2 = (TextView) findViewById(R.id.tvName2);
        tvLocName = (TextView) findViewById(R.id.tvLocName);
        tvAreaName3 = (TextView) findViewById(R.id.tvAreaName3);
        filter_tv = (TextView) findViewById(R.id.filter_tv);
        lbl_allplayer = (TextView) findViewById(R.id.lbl_allplayer);
        lbl_tvFemale = (TextView) findViewById(R.id.lbl_tvFemale);
        lbl_tvMale = (TextView) findViewById(R.id.lbl_tvMale);
        main_text_title = (TextView) findViewById(R.id.main_text_title);
        experties_tv = (TextView) findViewById(R.id.experties_tv);
        age_minval = (TextView) findViewById(R.id.age_minval);
        age_maxval = (TextView) findViewById(R.id.age_maxval);
        range_lbl = (TextView) findViewById(R.id.range_lbl);
        selected_game_tv = (TextView) findViewById(R.id.selected_game_tv);
        ll_filter = (NestedScrollView) findViewById(R.id.ll_filter);
        singles_rv = (RecyclerView) findViewById(R.id.singles_rv);
        genderfilter_section = (LinearLayout) findViewById(R.id.genderfilter_section);
        doubless_rv = (RecyclerView) findViewById(R.id.doubless_rv);
        ivFilter = (ImageView) findViewById(R.id.ivFilter);
        filter_close = (ImageView) findViewById(R.id.colse);
        ivLight = (ImageView) findViewById(R.id.ivLight);
        ageSeekbar = (CrystalRangeSeekbar) findViewById(R.id.ageSeekbar);
        revealFrameLayout = (RevealFrameLayout) findViewById(R.id.revell_ll);

        view1 = (View) findViewById(R.id.view1);
        view2 = (View) findViewById(R.id.view2);
        lyt_include = (View) findViewById(R.id.lyt_include);
        lyt_include_two = (View) findViewById(R.id.lyt_include_two);

        llDoubles = (LinearLayout) findViewById(R.id.llDoubles);
        llSingles = (LinearLayout) findViewById(R.id.llSingles);
        llSingle = (LinearLayout) findViewById(R.id.llSingle);
        llDouble = (LinearLayout) findViewById(R.id.llDouble);
        male_cv = (CardView) findViewById(R.id.male_cv);
        female_cv = (CardView) findViewById(R.id.female_cv);
        allplayer_cv = (CardView) findViewById(R.id.bothgender_cv);
        level_group = (RadioGroup) findViewById(R.id.level_group);
        applyfilter_btn = (Button) findViewById(R.id.applyfilter_btn);


        anytym_cv = findViewById(R.id.anytym_cv);
        morning_cv = findViewById(R.id.morning_cv);
        afternoon_cv = findViewById(R.id.afternoon_cv);
        evening_cv = findViewById(R.id.evening_cv);

        lbl_anytym = findViewById(R.id.lbl_anytym);
        lbl_tvMorning = findViewById(R.id.lbl_tvMorning);
        lbl_tvAfternoon = findViewById(R.id.lbl_tvAfternoon);
        lbl_tvevening = findViewById(R.id.lbl_tvevening);


        llSingle.setOnClickListener(this);
        llDouble.setOnClickListener(this);
        location.setOnClickListener(this);
        ivLight.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
        applyfilter_btn.setOnClickListener(this);
        filter_close.setOnClickListener(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");

        tvFinalName.setTypeface(typeface);
        tvAvaPlayers.setTypeface(face2);
        filter_tv.setTypeface(face2);


        selected_game_tv.setTypeface(typeface);
        experties_tv.setTypeface(typeface);
        male_cv.setOnClickListener(this);
        female_cv.setOnClickListener(this);
        allplayer_cv.setOnClickListener(this);
        main_text_title.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        setSupportActionBar(mToolbar);

        FragmentDrawer drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, mToolbar);
        drawerFragment.setDrawerListener(this);
        drawerLayout.setOnClickListener(this);

        try {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        tvFinalName.setText(sharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_NAME, ""));

        fetchlocationfromdb(sharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, ""));


        search_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issearchvisible = true;
                search_et.setVisibility(View.VISIBLE);
            }
        });

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        singles_rv.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        singles_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        singles_rv.setItemAnimator(new DefaultItemAnimator());





        /*        Doubles Match List*/


        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManagerDoubles = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        doubless_rv.setLayoutManager(mLayoutManagerDoubles);

        // adding inbuilt divider line
        doubless_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        doubless_rv.setItemAnimator(new DefaultItemAnimator());


        if (db.getActiveCartList().size() > 0) {


            if (db.getActiveCartList().get(0).getCatId() == 1) {

                selected_game_tv.setText("SQUASH");
                selected_cat = 1;
                SharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "1");


            } else if (db.getActiveCartList().get(0).getCatId() == 2) {

                selected_game_tv.setText("TABLE TENNIS");
                selected_cat = 2;
                SharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "2");

            } else if (db.getActiveCartList().get(0).getCatId() == 3) {

                selected_game_tv.setText("BADMINTON");
                selected_cat = 3;
                SharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "3");


            } else if (db.getActiveCartList().get(0).getCatId() == 4) {

                selected_game_tv.setText("TENNIS");
                selected_cat = 4;
                SharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "4");

            } else {
                selected_game_tv.setText("Select Game");
                SharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "");

            }

            if (db.getActiveCartList().get(0).getLevelId() == 1) {

                experties_tv.setText("Amateur");
                selected_level = 1;
                SharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_LEVEL, "1");

            } else if (db.getActiveCartList().get(0).getLevelId() == 2) {

                experties_tv.setText("Semi Pro");
                selected_level = 2;
                SharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_LEVEL, "2");

            } else if (db.getActiveCartList().get(0).getLevelId() == 3) {

                experties_tv.setText("Pro");
                selected_level = 3;
                SharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_LEVEL, "3");


            } else if (db.getActiveCartList().get(0).getLevelId() == 4) {

                experties_tv.setText("Expert");
                selected_level = 4;
                SharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_LEVEL, "4");

            } else {
                experties_tv.setText("---");
                SharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_LEVEL, "");

            }
        }


        selected_game_tv.setOnClickListener(this);


        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                singlesRVAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {


                if (issingleclicked) {
                    if (singlesRVAdapter.getItemCount() > 0) {

//                    singlesRVAdapter.getFilter().filter(s);

                        List<OpponetsDatum> filteredList = new ArrayList<>();
                        for (OpponetsDatum row : opponetsDatumList) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getName().toLowerCase().contains(s.toString().toLowerCase())) {


                                filteredList.add(row);
//                            Log.d("CHARSTRINGINIF",row.getName().toLowerCase()+"=="+charString.toLowerCase());

                            }
                        }


                        if (filteredList.size() > 0) {
                            singles_rv.setVisibility(View.VISIBLE);
                            lyt_include.setVisibility(View.GONE);
                            singlesRVAdapter = new SinglesRvAdapter(Activity_Badminton.this, filteredList);

                            singles_rv.setAdapter(singlesRVAdapter);

                        } else {

                            singles_rv.setVisibility(View.GONE);
                            lyt_include.setVisibility(View.VISIBLE);
                        }


//                    singles_rv.notify();

                    }

                } else if (isdoubleclicked) {

                    if (doublesRVAdapter.getItemCount() > 0) {

//                    singlesRVAdapter.getFilter().filter(s);

                        List<DualMatchListDatum> filteredList = new ArrayList<>();
                        for (DualMatchListDatum row : dualMatchListDatumList) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getMatchname().toLowerCase().contains(s.toString().toLowerCase())) {


                                filteredList.add(row);
//                            Log.d("CHARSTRINGINIF",row.getName().toLowerCase()+"=="+charString.toLowerCase());

                            }
                        }


                        if (filteredList.size() > 0) {
                            doubless_rv.setVisibility(View.VISIBLE);
                            lyt_include_two.setVisibility(View.GONE);
                            doublesRVAdapter = new DoublesRvAdapter(Activity_Badminton.this, filteredList);

                            doubless_rv.setAdapter(doublesRVAdapter);


                        } else {

                            doubless_rv.setVisibility(View.GONE);
                            lyt_include_two.setVisibility(View.VISIBLE);
                        }

                        //                    singles_rv.notify();

                    }


                }


            }
        });


// set listener
        ageSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {


                age_minval.setText("" + minValue + " YEARS");
                range_lbl.setText("" + minValue + " - " + maxValue);
                age_maxval.setText("" + maxValue + " YEARS");

            }
        });

// set final value listener
        ageSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
//                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));

                minAge = String.valueOf(minValue);
                maxAge = String.valueOf(maxValue);

            }
        });


        anytym_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePref = "";

                anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_anytym.setTextColor(getResources().getColor(R.color.color_white));

            }
        });

        morning_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePref = "1";

                morning_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMorning.setTextColor(getResources().getColor(R.color.color_white));
                lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        afternoon_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePref = "2";

                afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.color_white));
                lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));


            }
        });

        evening_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePref = "3";

                evening_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvevening.setTextColor(getResources().getColor(R.color.color_white));
                lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));


            }
        });


        swipreferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                iscomformsavedlocation = false;

                fetchOpponents(SharedPreference.getSharedPreferenceString(Activity_Badminton.this, SharedprefKeys.USER_ID, ""), selected_cat, selected_level, current_latitude, current_longetude, current_radius);

                fetchDualMatchlist(SharedPreference.getSharedPreferenceString(Activity_Badminton.this, SharedprefKeys.USER_ID, ""), current_latitude, current_longetude, current_radius, String.valueOf(selected_cat));

            }
        });


        if (tokenID == null || tokenID.equals("")) {
            tokenID = FirebaseInstanceId.getInstance().getToken();
        }


        gamenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
        cancelnotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
        scorenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
        gamenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
        cancelnotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
        scorenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
        sound = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
        chat = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


        addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);


//        if (!current_latitude_user.equals("")&& !current_longetude_user.equals("")){
//        }


        if (!SharedPreference.getSharedPreferenceBoolean(this, SharedprefKeys.HOMESHOWCASE, false)) {


            builder = new GuideView.Builder(this)
                    .setTitle("Select Game")
                    .setContentText("The sport you are currently playing, you can " +
                            "change sport by clicking on insert down arrow that we " +
                            "have.")
                    .setGravity(smartdevelop.ir.eram.showcaseviewlib.config.Gravity.auto)
                    .setDismissType(DismissType.outside)
                    .setTitleTypeFace(face2)
                    .setContentTypeFace(typeface)
                    .setTargetView(selected_game_tv)
                    .setGuideListener(new GuideListener() {
                        @Override
                        public void onDismiss(View view) {
                            switch (view.getId()) {
                                case R.id.selected_game_tv:

                                    builder.setTargetView(llSingle);
                                    builder.setTitle("Singles")
                                            .setContentText("Find and challenge players wanting to play one " +
                                                    "on one.").setDismissType(DismissType.outside).build();
                                    break;
                                case R.id.llSingle:
                                    builder.setTargetView(llDouble);
                                    builder.setTitle("Doubles")
                                            .setContentText("Find players to exhibit your team skills in a " +
                                                    "doubles match.").setDismissType(DismissType.outside).build();
                                    break;

                                case R.id.llDouble:
                                    builder.setTargetView(ivFilter);
                                    builder.setTitle("Filters")
                                            .setContentText("You can select your opponent according to your " +
                                                    "preferences.").setDismissType(DismissType.outside).build();
                                    break;
                                case R.id.ivFilter:
                                    builder.setTargetView(ivLight);
                                    builder.setTitle("Rack now")
                                            .setContentText("Click on RackNow and find someone to play " +
                                                    "with immediately.").setDismissType(DismissType.outside).build();

                                    break;
                                case R.id.ivLight:
                                    SharedPreference.setSharedPreferenceBoolean(Activity_Badminton.this, SharedprefKeys.HOMESHOWCASE, true);

                                    return;

                            }
                            mGuideView = builder.build();
                            mGuideView.show();
                        }
                    });

            mGuideView = builder.build();
            mGuideView.show();

        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {


        displayView(position);
    }

    private void displayView(int position) {

//        Toast.makeText(getApplicationContext(),"CLICKED",Toast.LENGTH_LONG).show();

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSingle:

                issingleclicked = true;
                isdoubleclicked = false;

                llSingles.setVisibility(View.VISIBLE);
                llDoubles.setVisibility(View.GONE);
                genderfilter_section.setVisibility(View.VISIBLE);
                view1.setBackgroundResource(R.color.color_white);
                view2.setBackgroundResource(R.color.colorPrimary);
                tvAvaPlayers.setText(String.valueOf(opponetsDatumList.size()) + " Players available");
                break;

            case R.id.llDouble:

                issingleclicked = false;
                isdoubleclicked = true;
                genderfilter_section.setVisibility(View.GONE);
                gender = "";

                llSingles.setVisibility(View.GONE);
                llDoubles.setVisibility(View.VISIBLE);
                view1.setBackgroundResource(R.color.colorPrimary);
                view2.setBackgroundResource(R.color.color_white);
                tvAvaPlayers.setText(String.valueOf(dualMatchListDatumList.size()) + " Matches available");
                break;

            case R.id.tvChat:
                Intent intent = new Intent(Activity_Badminton.this, Activity_Chat.class);
                startActivity(intent);
                break;

            case R.id.tvChat1:
                Intent intent1 = new Intent(Activity_Badminton.this, Activity_Chat.class);
                startActivity(intent1);
                break;

            case R.id.location:

                Intent intent2 = new Intent(Activity_Badminton.this, LocationFetcherActivity.class);
                startActivity(intent2);
                break;
            case R.id.selected_game_tv:

                choosegameDialog(Activity_Badminton.this, "");

                break;
            case R.id.ivFilter:

                int cx = ll_filter.getRight();
                int cy = ll_filter.getTop();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    makeEffect(ll_filter, cx, cy);
                }
                isfilterClicked = true;
                Log.d("LCIK", "CLID");
                break;

            case R.id.colse:
                ll_filter.setVisibility(View.INVISIBLE);
                hidden = true;
                isfilterClicked = false;
                break;
            case R.id.ivLight:

                if (online_flag == 0) {
                    online_flag = 1; // 1 => Button ON
                    ivLight.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_green_dark), PorterDuff.Mode.SRC_IN);

                    showracknowpop(ivLight, "Rack Now", "online");

//                    statusrack.setText("Rack Now");
//                    Toast.makeText(this, "Rack Now", Toast.LENGTH_SHORT).show();

                } else {
                    online_flag = 0; // 0 => Button OFF

                    ivLight.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
//                    Toast.makeText(this, "Off", Toast.LENGTH_SHORT).show();

                    showracknowpop(ivLight, "Off", "offline");

//                    statusrack.setText("Off");

                }
                break;

            case R.id.male_cv:

                gender = "Male";

                male_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                female_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                allplayer_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMale.setTextColor(getResources().getColor(R.color.color_white));
                lbl_tvFemale.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_allplayer.setTextColor(getResources().getColor(R.color.colorPrimary));

                break;

            case R.id.female_cv:

                gender = "Female";

                female_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                allplayer_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                male_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMale.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvFemale.setTextColor(getResources().getColor(R.color.color_white));
                lbl_allplayer.setTextColor(getResources().getColor(R.color.colorPrimary));

                break;

            case R.id.bothgender_cv:

                gender = "";

                allplayer_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                male_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                female_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMale.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvFemale.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_allplayer.setTextColor(getResources().getColor(R.color.color_white));

                break;

            case R.id.applyfilter_btn:

                String level_id = "";
                int selectedId = level_group.getCheckedRadioButtonId();
                levelRadiobtn = (RadioButton) findViewById(selectedId);


                if (levelRadiobtn != null) {
//                    Toast.makeText(Activity_Badminton.this,levelRadiobtn.getText()+"--"+gender+"--"+minAge+"--"+ maxAge,Toast.LENGTH_SHORT).show();

                    if (levelRadiobtn.getText().equals("Amateur")) {
                        level_id = "1";
                    } else if (levelRadiobtn.getText().equals("Semi Pro")) {
                        level_id = "2";

                    } else if (levelRadiobtn.getText().equals("Pro")) {
                        level_id = "3";

                    } else if (levelRadiobtn.getText().equals("Expert")) {
                        level_id = "4";

                    } else {
                        level_id = "";
                    }


                    if (issingleclicked) {
                        filteropponets(String.valueOf(selected_cat), level_id, gender, minAge, maxAge, timePref, user_id);

                    } else if (isdoubleclicked) {

                        filtermatchDoubles(String.valueOf(selected_cat), level_id, gender, minAge, maxAge, timePref, user_id);

                    }


                } else {

                    if (issingleclicked) {
                        filteropponets(String.valueOf(selected_cat), level_id, gender, minAge, maxAge, timePref, user_id);

                    } else if (isdoubleclicked) {
                        filtermatchDoubles(String.valueOf(selected_cat), level_id, gender, minAge, maxAge, timePref, user_id);

                    }

//                    filteropponets(String.valueOf(selected_cat), level_id, gender, minAge, maxAge, timePref, user_id);

                }
                break;

        }
    }


    public void resetallfilter(View view) {

        male_cv.setBackgroundColor(getResources().getColor(R.color.color_white));
        female_cv.setBackgroundColor(getResources().getColor(R.color.color_white));
        allplayer_cv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        lbl_allplayer.setTextColor(getResources().getColor(R.color.color_white));
        lbl_tvMale.setTextColor(getResources().getColor(R.color.colorPrimary));
        lbl_tvFemale.setTextColor(getResources().getColor(R.color.colorPrimary));

        morning_cv.setBackgroundColor(getResources().getColor(R.color.color_white));
        afternoon_cv.setBackgroundColor(getResources().getColor(R.color.color_white));
        evening_cv.setBackgroundColor(getResources().getColor(R.color.color_white));
        anytym_cv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        lbl_anytym.setTextColor(getResources().getColor(R.color.color_white));
        lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
        lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));
        lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));


        gender = "";
        minAge = "0";
        maxAge = "0";

        ageSeekbar.setMinValue(0);
        level_group.clearCheck();


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void makeEffect(final NestedScrollView layout, int cx, int cy) {

        int radius = Math.max(layout.getWidth(), layout.getHeight());

        if (hidden) {
            Animator anim = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                anim = android.view.ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, radius);
            }
            layout.setVisibility(View.VISIBLE);
            anim.start();
            invalidateOptionsMenu();
            hidden = false;

        } else {
            Animator anim = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                anim = android.view.ViewAnimationUtils.createCircularReveal(layout, cx, cy, radius, 0);
            }
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    layout.setVisibility(View.INVISIBLE);
                    hidden = true;
                    invalidateOptionsMenu();
                }
            });
            anim.start();
        }
    }


    public void choosegameDialog(final Context context, String validation_msg) {
        choosegame_dialog = new Dialog(context);
        choosegame_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.choosegame_dilog_lyt, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/

        RecyclerView selected_game_list_rv = view.findViewById(R.id.selected_game_list_rv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        selected_game_list_rv.setLayoutManager(mLayoutManager);
        selected_game_list_rv.setItemAnimator(new DefaultItemAnimator());
        selected_game_list_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        mAdapter = new SelectedGamesAdapter(this, db.getActiveCartList());
        selected_game_list_rv.setAdapter(mAdapter);


        selected_game_list_rv.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), selected_game_list_rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SelectedGameModal selectedGameModal = db.getActiveCartList().get(position);
//                Toast.makeText(getApplicationContext(), selectedGameModal.getCatId() + " is selected!", Toast.LENGTH_SHORT).show();

              /*  llSingles.setVisibility(View.VISIBLE);
                llDoubles.setVisibility(View.GONE);*/

//                sharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, String.valueOf(selectedGameModal.getCatId()));


                if (selectedGameModal.getCatId() == 1) {

                    selected_game_tv.setText("SQUASH");
                    selected_cat = 1;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "1");


                } else if (selectedGameModal.getCatId() == 2) {

                    selected_game_tv.setText("TABLE TENNIS");
                    selected_cat = 2;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "2");


                } else if (selectedGameModal.getCatId() == 3) {

                    selected_game_tv.setText("BADMINTON");
                    selected_cat = 3;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "3");


                } else if (selectedGameModal.getCatId() == 4) {

                    selected_game_tv.setText("TENNIS");
                    selected_cat = 4;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "4");

                } else {
                    selected_game_tv.setText("Select Game");
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "");


                }

                if (selectedGameModal.getLevelId() == 1) {

                    experties_tv.setText("Amateur");
                    selected_level = 1;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_LEVEL, "1");
                    FragmentDrawer.tvLevel.setText("Amateur");

                } else if (selectedGameModal.getLevelId() == 2) {

                    experties_tv.setText("Semi Pro");
                    selected_level = 2;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_LEVEL, "2");
                    FragmentDrawer.tvLevel.setText("Semi Pro");


                } else if (selectedGameModal.getLevelId() == 3) {

                    experties_tv.setText("Pro");
                    selected_level = 3;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_LEVEL, "3");
                    FragmentDrawer.tvLevel.setText("Pro");


                } else if (selectedGameModal.getLevelId() == 4) {

                    experties_tv.setText("Expert");
                    selected_level = 4;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_LEVEL, "4");
                    FragmentDrawer.tvLevel.setText("Expert");


                } else {
                    experties_tv.setText("---");
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_LEVEL, "");

                }

//                if (issingleclicked){

                fetchOpponents(SharedPreference.getSharedPreferenceString(Activity_Badminton.this, SharedprefKeys.USER_ID, ""), selected_cat, selected_level, current_latitude, current_longetude, current_radius);

//                }else if(isdoubleclicked) {

                fetchDualMatchlist(SharedPreference.getSharedPreferenceString(Activity_Badminton.this, SharedprefKeys.USER_ID, ""), current_latitude, current_longetude, current_radius, String.valueOf(selected_cat));

//                }


                choosegame_dialog.dismiss();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


//        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        choosegame_dialog.setContentView(view);
        final Window window = choosegame_dialog.getWindow();
        choosegame_dialog.setCancelable(true);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        choosegame_dialog.show();


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


                    current_latitude = resp.getLat();

                    current_longetude = resp.getLng();
                    current_radius = resp.getRadius();

                    minage_filter = resp.getMinage();
                    maxage_filter = resp.getMaxage();


                    if (resp.getOnlineflag().equals("online")) {
                        online_flag = 1;
                        ivLight.setColorFilter(ContextCompat.getColor(Activity_Badminton.this, android.R.color.holo_green_dark), PorterDuff.Mode.SRC_IN);
                    } else {

                        online_flag = 0;
                        ivLight.setColorFilter(ContextCompat.getColor(Activity_Badminton.this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    }


                    if (!resp.getMaxage().equals("") && !resp.getMinage().equals("")) {
                        if (Integer.parseInt(resp.getMaxage()) <= 65) {
                            ageSeekbar.setMaxValue(65);
                            ageSeekbar.setMaxStartValue(65).apply();
                        } else {
                            ageSeekbar.setMaxValue(Integer.parseInt(resp.getMaxage()));

                            ageSeekbar.setMaxStartValue(Integer.parseInt(resp.getMaxage())).apply();

                        }

                        if (Integer.parseInt(resp.getMinage()) <= 10) {
                            ageSeekbar.setMinValue(10);

                            ageSeekbar.setMinStartValue(10).apply();
                        } else {
                            ageSeekbar.setMinValue(Integer.parseInt(resp.getMinage()));
                            ageSeekbar.setMinStartValue(Integer.parseInt(resp.getMinage())).apply();

                        }

//                        if (iscomformsavedlocation) {

//                        getLocationName(Double.parseDouble(current_latitude), Double.parseDouble(current_longetude));

//                        }


                    }

                    fetchOpponents(sharedPreference.getSharedPreferenceString(Activity_Badminton.this, SharedprefKeys.USER_ID, ""), selected_cat, selected_level, current_latitude, current_longetude, current_radius);

                    fetchDualMatchlist(sharedPreference.getSharedPreferenceString(Activity_Badminton.this, SharedprefKeys.USER_ID, ""), current_latitude, current_longetude, current_radius, String.valueOf(selected_cat));

//                    dialogServerNotConnect();
                } else {


//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<FetchSavedLocationRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void fetchOpponents(String user_id, int selected_cat, int selected_level, String current_lat, String current_lng, String radius) {

        swipreferesh.setRefreshing(true);
        Apis apis = RestAdapter.createAPI();
        Call<FetchOpponentsRespo> callbackCall = apis.fetchOpponets(user_id, selected_cat, selected_level, current_lat, current_lng, radius);
        callbackCall.enqueue(new Callback<FetchOpponentsRespo>() {
            @Override
            public void onResponse(Call<FetchOpponentsRespo> call, Response<FetchOpponentsRespo> response) {


                FetchOpponentsRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));
                opponetsDatumList.clear();

                if (resp != null && !resp.getError()) {

                    swipreferesh.setRefreshing(false);
                    for (int i = 0; i < resp.getData().size(); i++) {

                        OpponetsDatum opponetsDatum = new OpponetsDatum(
                                resp.getData().get(i).getUserid(),
                                resp.getData().get(i).getLevelid(),
                                resp.getData().get(i).getCatid(),
                                resp.getData().get(i).getName(),
                                resp.getData().get(i).getEmail(),
                                resp.getData().get(i).getAddress(),
                                resp.getData().get(i).getGender(),
                                resp.getData().get(i).getLat(),
                                resp.getData().get(i).getLng(),
                                resp.getData().get(i).getAddress(),
                                resp.getData().get(i).getRadius(),
                                resp.getData().get(i).getImage(),
                                resp.getData().get(i).getStatusflag(),
                                resp.getData().get(i).getTime(),
                                resp.getData().get(i).getPoints()


                        );

                        opponetsDatumList.add(opponetsDatum);

                    }

                    if (issingleclicked) {
                        llSingles.setVisibility(View.VISIBLE);
                        llDoubles.setVisibility(View.GONE);
                        view1.setBackgroundResource(R.color.color_white);
                        view2.setBackgroundResource(R.color.colorPrimary);

                    }

                    /*else {
                        llSingles.setVisibility(View.GONE);
                        llDoubles.setVisibility(View.VISIBLE);
                        view1.setBackgroundResource(R.color.colorPrimary);
                        view2.setBackgroundResource(R.color.color_white);
                    }*/

                    if (opponetsDatumList.size() > 0) {
                        lyt_include.setVisibility(View.GONE);
                        singles_rv.setVisibility(View.VISIBLE);

                        if (issingleclicked) {
                            tvAvaPlayers.setText(String.valueOf(opponetsDatumList.size()) + " Players available");

                            singlesRVAdapter = new SinglesRvAdapter(Activity_Badminton.this, opponetsDatumList);

                            singles_rv.setAdapter(singlesRVAdapter);

                        }


                    } else {
                        lyt_include.setVisibility(View.VISIBLE);
                        singles_rv.setVisibility(View.GONE);

                        if (issingleclicked) {
                            tvAvaPlayers.setText("0 Players available");
                        }
                        if (issingleclicked) {
                            llSingles.setVisibility(View.VISIBLE);
                            llDoubles.setVisibility(View.GONE);
                            view1.setBackgroundResource(R.color.color_white);
                            view2.setBackgroundResource(R.color.colorPrimary);
                        }

                        /*else{
                            llSingles.setVisibility(View.GONE);
                            llDoubles.setVisibility(View.VISIBLE);
                            view1.setBackgroundResource(R.color.colorPrimary);
                            view2.setBackgroundResource(R.color.color_white);
                        }*/


                    }


//                    dialogServerNotConnect();
                } else {


                    swipreferesh.setRefreshing(false);


                    lyt_include.setVisibility(View.VISIBLE);
                    singles_rv.setVisibility(View.GONE);

                    if (issingleclicked) {
                        tvAvaPlayers.setText("0 Players available");

                    }


//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<FetchOpponentsRespo> call, Throwable t) {

                swipreferesh.setRefreshing(false);

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void filteropponets(String catid, String levelid, String gender, String minAge, String maxAge, String time, String userid) {

        Apis apis = RestAdapter.createAPI();
        Call<FetchOpponentsRespo> callbackCall = apis.filteropponets(catid, levelid, gender, minAge, maxAge, time, userid);
        callbackCall.enqueue(new Callback<FetchOpponentsRespo>() {
            @Override
            public void onResponse(Call<FetchOpponentsRespo> call, Response<FetchOpponentsRespo> response) {


                FetchOpponentsRespo resp = response.body();
                Log.d("RESPO_FILTER", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    opponetsDatumList.clear();


                    for (int i = 0; i < resp.getData().size(); i++) {

                        OpponetsDatum opponetsDatum = new OpponetsDatum(
                                resp.getData().get(i).getUserid(),
                                resp.getData().get(i).getLevelid(),
                                resp.getData().get(i).getCatid(),
                                resp.getData().get(i).getName(),
                                resp.getData().get(i).getEmail(),
                                resp.getData().get(i).getAddress(),
                                resp.getData().get(i).getGender(),
                                resp.getData().get(i).getLat(),
                                resp.getData().get(i).getLng(),
                                resp.getData().get(i).getAddress(),
                                resp.getData().get(i).getRadius(),
                                resp.getData().get(i).getImage(),
                                resp.getData().get(i).getStatusflag(),
                                resp.getData().get(i).getTime(),
                                resp.getData().get(i).getPoints()

                        );

                        opponetsDatumList.add(opponetsDatum);

                    }
                    Log.d("SIZEDATA", opponetsDatumList.size() + "");

                    if (opponetsDatumList.size() > 0) {

                        lyt_include.setVisibility(View.GONE);
                        singles_rv.setVisibility(View.VISIBLE);

                        tvAvaPlayers.setText(String.valueOf(opponetsDatumList.size()) + " Players available");


                        singlesRVAdapter = new SinglesRvAdapter(Activity_Badminton.this, opponetsDatumList);

                        singles_rv.setAdapter(singlesRVAdapter);
//                        singles_rv.notify();
                        singlesRVAdapter.notifyDataSetChanged();


                    } else {
                        tvAvaPlayers.setText("0 Players available");

                        lyt_include.setVisibility(View.VISIBLE);
                        singles_rv.setVisibility(View.GONE);
                    }

                    ll_filter.setVisibility(View.INVISIBLE);
                    hidden = true;


//                    dialogServerNotConnect();
                } else {

                    ll_filter.setVisibility(View.INVISIBLE);
                    hidden = true;

//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<FetchOpponentsRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void filtermatchDoubles(String catid, String levelid, String gender, String minAge, String maxAge, String time, String userid) {

        Apis apis = RestAdapter.createAPI();
        Call<MatchListDualResponse> callbackCall = apis.doublematchbyfilter(catid, levelid, gender, minAge, maxAge, time, userid);
        callbackCall.enqueue(new Callback<MatchListDualResponse>() {
            @Override
            public void onResponse(Call<MatchListDualResponse> call, Response<MatchListDualResponse> response) {


                MatchListDualResponse resp = response.body();
                Log.d("RESPO_FILTER_DUAL", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    dualMatchListDatumList.clear();
                    for (int i = 0; i < resp.getData().size(); i++) {

                        DualMatchListDatum opponetsDatum = new DualMatchListDatum(
                                resp.getData().get(i).getMatchid(),
                                resp.getData().get(i).getMid(),
                                resp.getData().get(i).getMidname(),
                                resp.getData().get(i).getMidimage(),
                                resp.getData().get(i).getMatchname(),
                                resp.getData().get(i).getLat(),
                                resp.getData().get(i).getLng(),
                                resp.getData().get(i).getAddress(),
                                resp.getData().get(i).getDate(),
                                resp.getData().get(i).getStartTime(),
                                resp.getData().get(i).getEndTime(),
                                resp.getData().get(i).getMidlevel(),
                                resp.getData().get(i).getFidlevel(),
                                resp.getData().get(i).getStartflag(),
                                resp.getData().get(i).getCatid(),
                                resp.getData().get(i).getStatus(),
                                resp.getData().get(i).getMidpoint(),
                                resp.getData().get(i).getFidpoint(),
                                resp.getData().get(i).getType(),
                                resp.getData().get(i).getValidatetime(),
                                resp.getData().get(i).getGroupid(),
                                resp.getData().get(i).getFidarray(),
                                resp.getData().get(i).getMatchtype(),
                                resp.getData().get(i).getMatchstatus(),
                                resp.getData().get(i).getGender()
                        );

                        dualMatchListDatumList.add(opponetsDatum);

                    }
                    Log.d("SIZEDATA", dualMatchListDatumList.size() + "");

                    if (dualMatchListDatumList.size() > 0) {

                        lyt_include_two.setVisibility(View.GONE);
                        doubless_rv.setVisibility(View.VISIBLE);

                        tvAvaPlayers.setText(String.valueOf(dualMatchListDatumList.size()) + " Matches available");


                        doublesRVAdapter = new DoublesRvAdapter(Activity_Badminton.this, dualMatchListDatumList);

                        doubless_rv.setAdapter(doublesRVAdapter);
//                        singles_rv.notify();
                        doublesRVAdapter.notifyDataSetChanged();


                    } else {
                        tvAvaPlayers.setText("0 Matches available");

                        lyt_include_two.setVisibility(View.VISIBLE);
                        doubless_rv.setVisibility(View.GONE);
                    }

                    ll_filter.setVisibility(View.INVISIBLE);
                    hidden = true;


//                    dialogServerNotConnect();
                } else {

                    ll_filter.setVisibility(View.INVISIBLE);
                    hidden = true;

                    tvAvaPlayers.setText("0 Matches available");

                    lyt_include_two.setVisibility(View.VISIBLE);
                    doubless_rv.setVisibility(View.GONE);

//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<MatchListDualResponse> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {


        if (issearchvisible) {

            search_et.setVisibility(View.GONE);
            issearchvisible = false;
            doubleBackToExitPressedOnce = false;
            return;

        }

        if (!hidden) {

            ll_filter.setVisibility(View.INVISIBLE);

            hidden = true;
            doubleBackToExitPressedOnce = false;
            return;

        }


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);

    }


    public void runFetchlocation() {
        fetchDualMatchlist(SharedPreference.getSharedPreferenceString(Activity_Badminton.this, SharedprefKeys.USER_ID, ""), current_latitude, current_longetude, current_radius, String.valueOf(selected_cat));

    }

    private void fetchDualMatchlist(String user_id, String current_latitude, String current_longetude, String current_radius, String cat_id) {

        Apis apis = RestAdapter.createAPI();
        Call<MatchListDualResponse> callbackCall = apis.getAllMatchesDuals(cat_id, user_id, current_latitude, current_longetude, current_radius);
        callbackCall.enqueue(new Callback<MatchListDualResponse>() {
            @Override
            public void onResponse(Call<MatchListDualResponse> call, Response<MatchListDualResponse> response) {


                dualMatchListDatumList.clear();
                MatchListDualResponse resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

//                issingleclicked = true;
//                isdoubleclicked = false;

                if (isdoubleclicked) {
                    llSingles.setVisibility(View.GONE);
                    llDoubles.setVisibility(View.VISIBLE);
                    view1.setBackgroundResource(R.color.colorPrimary);
                    view2.setBackgroundResource(R.color.color_white);
                }

                /*else {
                    llSingles.setVisibility(View.VISIBLE);
                    llDoubles.setVisibility(View.GONE);
                    view1.setBackgroundResource(R.color.color_white);
                    view2.setBackgroundResource(R.color.colorPrimary);

                }*/


//                view1.setBackgroundResource(R.color.color_white);
//                view2.setBackgroundResource(R.color.colorPrimary);


                if (resp != null && !resp.getError()) {

                    for (int i = 0; i < resp.getData().size(); i++) {

                        DualMatchListDatum dualMatchListDatum = new DualMatchListDatum(
                                resp.getData().get(i).getMatchid(),
                                resp.getData().get(i).getMid(),
                                resp.getData().get(i).getMidname(),
                                resp.getData().get(i).getMidimage(),
                                resp.getData().get(i).getMatchname(),
                                resp.getData().get(i).getLat(),
                                resp.getData().get(i).getLng(),
                                resp.getData().get(i).getAddress(),
                                resp.getData().get(i).getDate(),
                                resp.getData().get(i).getStartTime(),
                                resp.getData().get(i).getEndTime(),
                                resp.getData().get(i).getMidlevel(),
                                resp.getData().get(i).getFidlevel(),
                                resp.getData().get(i).getStartflag(),
                                resp.getData().get(i).getCatid(),
                                resp.getData().get(i).getStatus(),
                                resp.getData().get(i).getMidpoint(),
                                resp.getData().get(i).getFidpoint(),
                                resp.getData().get(i).getType(),
                                resp.getData().get(i).getValidatetime(),
                                resp.getData().get(i).getGroupid(),
                                resp.getData().get(i).getFidarray(),
                                resp.getData().get(i).getMatchtype(),
                                resp.getData().get(i).getMatchstatus(),
                                resp.getData().get(i).getGender()
                        );


                        dualMatchListDatumList.add(dualMatchListDatum);

                    }

                    if (dualMatchListDatumList.size() > 0) {

                        lyt_include_two.setVisibility(View.GONE);
                        doubless_rv.setVisibility(View.VISIBLE);

//                        tvAvaPlayers.setText(String.valueOf(dualMatchListDatumList.size()) + " matches available");

                        doublesRVAdapter = new DoublesRvAdapter(Activity_Badminton.this, dualMatchListDatumList);

                        doubless_rv.setAdapter(doublesRVAdapter);

                        if (isdoubleclicked) {
                            tvAvaPlayers.setText(String.valueOf(dualMatchListDatumList.size()) + " Matches available");

                        }


                    } else {
                        lyt_include_two.setVisibility(View.VISIBLE);
                        doubless_rv.setVisibility(View.GONE);

//                        tvAvaPlayers.setText("0 matches available");

                        if (isdoubleclicked) {
                            tvAvaPlayers.setText(String.valueOf("0 Matches available"));

                        }


                    }


//                    dialogServerNotConnect();
                } else {

                    lyt_include_two.setVisibility(View.VISIBLE);
                    doubless_rv.setVisibility(View.GONE);
                    if (isdoubleclicked) {
                        tvAvaPlayers.setText(String.valueOf("0 Matches available"));

                    }

//                    tvAvaPlayers.setText("0 matches available");

//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<MatchListDualResponse> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void addtokentoserver(String user_id, String token, String gamenotipush, String cancelnotipush, String scorenotipush, String gamenotiemail, String cancelnotiemail, String scorenotiemail, String sound, String chat) {

        Apis apis = RestAdapter.createAPI();
        Call<CreateMatchRespo> callbackCall = apis.setToken(user_id, token, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);
        callbackCall.enqueue(new Callback<CreateMatchRespo>() {
            @Override
            public void onResponse(Call<CreateMatchRespo> call, Response<CreateMatchRespo> response) {


                CreateMatchRespo resp = response.body();
//                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {


//                    dialogServerNotConnect();
                } else {


                }
            }

            @Override
            public void onFailure(Call<CreateMatchRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        return false;
    }


    @Override
    public void onLocationChanged(Location location) {


        current_latitude_user = String.valueOf(location.getLatitude());
        current_longetude_user = String.valueOf(location.getLongitude());
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


    private void getLocationName(double latitude, double longitude) {
//        swipreferesh.setRefreshing(true);
        Apis apis = RestAdapter.creategoogleAPI();
        Call<FetchLocationRespo> callbackCall = apis.getLocationName(latitude + "," + longitude, true, "AIzaSyCY8z3-HeF4uogoq6IDXI1gm1YcnnXR8dc");
        callbackCall.enqueue(new Callback<FetchLocationRespo>() {
            @Override
            public void onResponse(Call<FetchLocationRespo> call, Response<FetchLocationRespo> response) {
                FetchLocationRespo resp = response.body();

//                swipreferesh.setRefreshing(false);

                tvLocName.setText(resp.getformatedaddress());


//                resp.getformatedaddress();


            }

            @Override
            public void onFailure(Call<FetchLocationRespo> call, Throwable t) {

//                swipreferesh.setRefreshing(false);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void showracknowpop(View v, String msg_, String status) {


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.dialog_rackpop, null);


        Typeface typeface = Typeface.createFromAsset(this.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");


        TextView msg = (TextView) popupView.findViewById(R.id.msg);


        if (status.equals("online")) {
            msg.setTextColor(Color.parseColor("#ff669900"));

        } else {
            msg.setTextColor(Color.parseColor("#FF0000"));

        }

        msg.setTypeface(face2);

        msg.setText(msg_);


        onlineornot(status, SharedPreference.getSharedPreferenceString(Activity_Badminton.this, SharedprefKeys.USER_ID, ""));

        racknowpopup = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true

        );

//        profilePopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        racknowpopup.setBackgroundDrawable(new BitmapDrawable());
        racknowpopup.setOutsideTouchable(true);
        racknowpopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {


            }
        });

        racknowpopup.showAsDropDown(v);
    }


    private void onlineornot(String status, String user_id) {

//        loader_dialog(this);

        Apis apis = RestAdapter.createAPI();
        Call<UserOnlineStatus> callbackCall = apis.saveonlineorNot(status, user_id);
        callbackCall.enqueue(new Callback<UserOnlineStatus>() {
            @Override
            public void onResponse(Call<UserOnlineStatus> call, Response<UserOnlineStatus> response) {


                UserOnlineStatus resp = response.body();
                Log.d("LOGINRESPO", new Gson().toJson(resp));
                if (resp != null && !resp.getError()) {

                    if (status.equals("offline")) {
//                        stopSelf();
                    }


//                    dialogServerNotConnect();
                } else {


//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<UserOnlineStatus> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

}
