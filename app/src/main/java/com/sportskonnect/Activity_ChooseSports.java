package com.sportskonnect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastillo.FillableLoader;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.CategoriesDataResponse;
import com.sportskonnect.api.Callbacks.FetchLocationRespo;
import com.sportskonnect.api.Callbacks.SaveCatDataRespo;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.database.DatabaseHandler;
import com.sportskonnect.modal.SelectedGameModal;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import me.jessyan.progressmanager.body.ProgressInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;

public class Activity_ChooseSports extends AppCompatActivity implements View.OnClickListener {

    private static final String DOWNLOAD_URL = "http:\\/\\/thinklabs.io\\/rackconnect\\/assets\\/images\\/category\\/squash.png";
    Button btnSubmit;
    LinearLayout llBad1, llBad, llTableTenn, llTableTenn1, llSquash1, llSquash, llTennis1, llTennis, llBad11, llBad12, llBad13, llBad14, llTT1, llTT2, llTT3, llTT4, llsaq2, llsaq3, llsaq4, llten1, llten2, llten3, llten4;
    TextView main_text_title, tvSquash1, tvTable, tvBad1, tvTennis1, tvSq1, tvtb1, tvBad11, tvtt1;
    ImageView ivCrossBad, ivBad4, ivBad3, ivBad2, ivBad1, ivCrosstt, ivtt1, ivtt2, ivtt3, ivtt4, ivsaq2, ivsaq3, ivsaq4, ivCrossBadd, ivCrossTen, ivten1, ivten2, ivten3, ivten4;
    View viewBad1, viewBad2, viewBad3, viewtt1, viewtt2, viewtt3, viewsaq1, viewsaq2, viewsaq3, viewTen1, viewTen2, viewTen3;

    TextView tv_squashexperties, tv_tt_experties, tvBedmintorExperties, tvTennis_two_experties;
    int bad = 0;
    boolean badRev = false;
    int tt = 0;
    boolean ttRev = false;

    private String info_msg="";
    private String info_msg_two="";
    private String info_msg_three="";
    private String info_msg_four="";
    int squash = 0;
    boolean sqhRev = false;
    int ten = 0;
    boolean tenRev = false;
    private SharedPreference sharedPreferences;
    private Dialog dialog;
    private ImageView ivBad_minton, ivTennise, ivTableteniss, iv_squash;

    private List<SelectedGameModal> selectedGameModalList = new ArrayList<>();
    private String catOneId = "";
    private String catTwoId = "";
    private String catThreeId = "";
    private String catFourId = "";
    private DatabaseHandler db;
    private ImageView squash_i,tabletenis_i,bedmintan_i,tenis_i;
    private Dialog loader_dialog;
    private Handler mHandler = new Handler();

    private ProgressInfo mLastDownloadingInfo;


//    location update variablse

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    private String addresss="";
    private boolean isgameselected=false;
    private boolean isgameTwoselected= false;
    private boolean isgamethreeselected=false;
    private boolean isgamefourselected=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosesports);

        sharedPreferences = new SharedPreference(this);
        db = new DatabaseHandler(this);
//        loader_dialog(this);
        init();

        initlocation();

        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);


        Dexter.withActivity(this)
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


    }

    private void init() {

        info_msg=getResources().getString(R.string.amature_string);

        main_text_title = (TextView) findViewById(R.id.main_text_title);
        tvSquash1 = (TextView) findViewById(R.id.tvSquash1);
        tvTable = (TextView) findViewById(R.id.tvTable);
        tvBad1 = (TextView) findViewById(R.id.tvBad1);
        tvTennis1 = (TextView) findViewById(R.id.tvTennis1);
        tvtt1 = (TextView) findViewById(R.id.tvtt1);
        tvSq1 = (TextView) findViewById(R.id.tvSq1);
        tvtb1 = (TextView) findViewById(R.id.tvtb1);
        tvBad11 = (TextView) findViewById(R.id.tvBad11);
        ivCrossBad = (ImageView) findViewById(R.id.ivCrossBad);
        ivCrosstt = (ImageView) findViewById(R.id.ivCrosstt);
        ivCrossBadd = (ImageView) findViewById(R.id.ivCrossBadd);
        ivCrossTen = (ImageView) findViewById(R.id.ivCrossTen);
        squash_i = (ImageView) findViewById(R.id.squash_i);
        tabletenis_i = (ImageView) findViewById(R.id.tabletenis_i);
        bedmintan_i = (ImageView) findViewById(R.id.bedmintan_i);
        tenis_i = (ImageView) findViewById(R.id.tenis_i);
        ivBad2 = (ImageView) findViewById(R.id.ivBad2);
        ivBad3 = (ImageView) findViewById(R.id.ivBad3);
        ivBad4 = (ImageView) findViewById(R.id.ivBad4);
        ivten2 = (ImageView) findViewById(R.id.ivten2);
        ivten3 = (ImageView) findViewById(R.id.ivten3);
        ivten4 = (ImageView) findViewById(R.id.ivten4);
        ivtt2 = (ImageView) findViewById(R.id.ivtt2);
        ivtt3 = (ImageView) findViewById(R.id.ivtt3);
        ivtt4 = (ImageView) findViewById(R.id.ivtt4);
        ivsaq2 = (ImageView) findViewById(R.id.ivsaq2);
        ivsaq3 = (ImageView) findViewById(R.id.ivsaq3);
        ivsaq4 = (ImageView) findViewById(R.id.ivsaq4);
        tv_squashexperties = findViewById(R.id.tvBad);
        tv_tt_experties = findViewById(R.id.tvTbleTenn);
        tvBedmintorExperties = findViewById(R.id.tvSquash);
        tvTennis_two_experties = findViewById(R.id.tvTennis);
        ivCrossBad.setOnClickListener(this);
        ivCrosstt.setOnClickListener(this);
        ivCrossBadd.setOnClickListener(this);
        ivCrossTen.setOnClickListener(this);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Calibre Bold.otf");

        Typeface face1 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Regular.otf");

        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");


        main_text_title.setTypeface(face);
        tvSquash1.setTypeface(face);
        tvTable.setTypeface(face);
        tvBad1.setTypeface(face);
        tvTennis1.setTypeface(face);
        tvSq1.setTypeface(face);
        tvtb1.setTypeface(face);
        tvBad11.setTypeface(face);
        tvtt1.setTypeface(face);

        llBad1 = (LinearLayout) findViewById(R.id.llBad1);
        llBad = (LinearLayout) findViewById(R.id.llBad);
        llTableTenn = (LinearLayout) findViewById(R.id.llTableTenn);
        llTableTenn1 = (LinearLayout) findViewById(R.id.llTableTenn1);
        llSquash1 = (LinearLayout) findViewById(R.id.llSquash1);
        llSquash = (LinearLayout) findViewById(R.id.llSquash);
        llTennis1 = (LinearLayout) findViewById(R.id.llTennis1);
        llTennis = (LinearLayout) findViewById(R.id.llTennis);
        llBad12 = (LinearLayout) findViewById(R.id.llBad12);
        llBad13 = (LinearLayout) findViewById(R.id.llBad13);
        llBad14 = (LinearLayout) findViewById(R.id.llBad14);
        llTT2 = (LinearLayout) findViewById(R.id.llTT2);
        llTT3 = (LinearLayout) findViewById(R.id.llTT3);
        llTT4 = (LinearLayout) findViewById(R.id.llTT4);
        llsaq2 = (LinearLayout) findViewById(R.id.llsaq2);
        llsaq3 = (LinearLayout) findViewById(R.id.llsaq3);
        llsaq4 = (LinearLayout) findViewById(R.id.llsaq4);
        llten2 = (LinearLayout) findViewById(R.id.llten2);
        llten3 = (LinearLayout) findViewById(R.id.llten3);
        llten4 = (LinearLayout) findViewById(R.id.llten4);
        viewBad1 = (View) findViewById(R.id.viewBad1);
        viewBad2 = (View) findViewById(R.id.viewBad2);
        viewBad3 = (View) findViewById(R.id.viewBad3);
        viewtt1 = (View) findViewById(R.id.viewtt1);
        viewtt2 = (View) findViewById(R.id.viewtt2);
        viewtt3 = (View) findViewById(R.id.viewtt3);
        viewsaq1 = (View) findViewById(R.id.viewsaq1);
        viewsaq2 = (View) findViewById(R.id.viewsaq2);
        viewsaq3 = (View) findViewById(R.id.viewsaq3);
        viewTen1 = (View) findViewById(R.id.viewTen1);
        viewTen2 = (View) findViewById(R.id.viewTen2);
        viewTen3 = (View) findViewById(R.id.viewTen3);


        iv_squash = findViewById(R.id.ivBad);
        ivTableteniss = findViewById(R.id.ivTennis);
        ivBad_minton = findViewById(R.id.ivsquash);
        ivTennise = findViewById(R.id.ivTenniss_two);

        llTennis.setOnClickListener(this);
        llBad.setOnClickListener(this);
        llTableTenn.setOnClickListener(this);
        llBad12.setOnClickListener(this);

        squash_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showinfoOneOflevel();

            }
        });

        tabletenis_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showinfoTwoOflevel();

            }
        });
        bedmintan_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showinfoThreeOflevel();

            }
        });
        tenis_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showinfoFourOflevel();

            }
        });


        displayData();
        llBad1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    if (!badRev) {
                        bad++;


                        if (bad == 1) {
                            viewBad1.setBackgroundResource(R.color.color_white);
                            ivBad2.setBackgroundResource(R.drawable.rectangle_non);
                            tv_squashexperties.setText(R.string.intermediate_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catOneId), 2);
                            db.saveSelectedgames(selectedGameModal);

                            info_msg = getResources().getString(R.string.semipro_string);

                        }

                        if (bad == 2) {

                            ivBad3.setBackgroundResource(R.drawable.rectangle_non);
                            viewBad2.setBackgroundResource(R.color.color_white);
                            tv_squashexperties.setText(R.string.pro_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catOneId), 3);
                            db.saveSelectedgames(selectedGameModal);

                            info_msg = getResources().getString(R.string.pro_string);
                        }
                        if (bad == 3) {

                            badRev = true;
                            viewBad3.setBackgroundResource(R.color.color_white);
                            ivBad4.setBackgroundResource(R.drawable.rectangle_non);
                            tv_squashexperties.setText(R.string.expert_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catOneId), 4);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg = getResources().getString(R.string.expert_string);


                        }
                    } else if (badRev) {
                        bad--;
                        if (bad == 2) {
                            viewBad3.setBackgroundResource(R.color.color_blue);
                            ivBad4.setBackgroundResource(R.drawable.rectangle_non1);
                            tv_squashexperties.setText(R.string.pro_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catOneId), 3);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg = getResources().getString(R.string.pro_string);


                        }
                        if (bad == 1) {
                            ivBad3.setBackgroundResource(R.drawable.rectangle_non1);
                            viewBad2.setBackgroundResource(R.color.color_blue);
                            tv_squashexperties.setText(R.string.intermediate_str);
                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catOneId), 2);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg = getResources().getString(R.string.semipro_string);


                        }

                        if (bad == 0) {
                            viewBad1.setBackgroundResource(R.color.color_blue);
                            ivBad2.setBackgroundResource(R.drawable.rectangle_non1);
                            badRev = false;
                            tv_squashexperties.setText(R.string.amateure_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catOneId), 1);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg = getResources().getString(R.string.amature_string);

                        }
                    }
                }
                return true;
            }
        });

        llTableTenn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!ttRev) {
                        tt++;

                        if (tt == 1) {
                            viewtt1.setBackgroundResource(R.color.color_white);
                            ivtt2.setBackgroundResource(R.drawable.rectangle_non);
                            tv_tt_experties.setText(R.string.intermediate_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catTwoId), 2);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_two = getResources().getString(R.string.semipro_string);

                        }

                        if (tt == 2) {
                            ivtt3.setBackgroundResource(R.drawable.rectangle_non);
                            viewtt2.setBackgroundResource(R.color.color_white);
                            tv_tt_experties.setText(R.string.pro_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catTwoId), 3);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_two = getResources().getString(R.string.pro_string);

                        }
                        if (tt == 3) {
                            ttRev = true;
                            viewtt3.setBackgroundResource(R.color.color_white);
                            ivtt4.setBackgroundResource(R.drawable.rectangle_non);
                            tv_tt_experties.setText(R.string.expert_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catTwoId), 4);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_two = getResources().getString(R.string.expert_string);

                        }
                    } else if (ttRev) {
                        tt--;
                        if (tt == 2) {
                            viewtt3.setBackgroundResource(R.color.color_blue);
                            ivtt4.setBackgroundResource(R.drawable.rectangle_non1);
                            tv_tt_experties.setText(R.string.pro_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catTwoId), 3);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_two = getResources().getString(R.string.pro_string);


                        }
                        if (tt == 1) {
                            ivtt3.setBackgroundResource(R.drawable.rectangle_non1);
                            viewtt2.setBackgroundResource(R.color.color_blue);
                            tv_tt_experties.setText(R.string.intermediate_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catTwoId), 2);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_two = getResources().getString(R.string.semipro_string);


                        }

                        if (tt == 0) {
                            viewtt1.setBackgroundResource(R.color.color_blue);
                            ivtt2.setBackgroundResource(R.drawable.rectangle_non1);
                            ttRev = false;
                            tv_tt_experties.setText(R.string.amateure_str);


                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catTwoId), 1);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_two = getResources().getString(R.string.amature_string);

                        }
                    }
                }
                return true;
            }
        });

        llSquash1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!sqhRev) {
                        squash++;

                        if (squash == 1) {
                            viewsaq1.setBackgroundResource(R.color.color_white);
                            ivsaq2.setBackgroundResource(R.drawable.rectangle_non);
                            tvBedmintorExperties.setText(R.string.intermediate_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catThreeId), 2);
                            db.saveSelectedgames(selectedGameModal);

                            info_msg_three = getResources().getString(R.string.semipro_string);

                        }

                        if (squash == 2) {
                            ivsaq3.setBackgroundResource(R.drawable.rectangle_non);
                            viewsaq2.setBackgroundResource(R.color.color_white);
                            tvBedmintorExperties.setText(R.string.pro_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catThreeId), 3);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_three = getResources().getString(R.string.pro_string);


                        }
                        if (squash == 3) {
                            sqhRev = true;
                            viewsaq3.setBackgroundResource(R.color.color_white);
                            ivsaq4.setBackgroundResource(R.drawable.rectangle_non);
                            tvBedmintorExperties.setText(R.string.expert_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catThreeId), 4);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_three = getResources().getString(R.string.expert_string);


                        }
                    } else if (sqhRev) {
                        squash--;
                        if (squash == 2) {
                            viewsaq3.setBackgroundResource(R.color.color_blue);
                            ivsaq4.setBackgroundResource(R.drawable.rectangle_non1);
                            tvBedmintorExperties.setText(R.string.pro_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catThreeId), 3);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_three = getResources().getString(R.string.pro_string);



                        }
                        if (squash == 1) {
                            ivsaq3.setBackgroundResource(R.drawable.rectangle_non1);
                            viewsaq2.setBackgroundResource(R.color.color_blue);
                            tvBedmintorExperties.setText(R.string.intermediate_str);
                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catThreeId), 2);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_three = getResources().getString(R.string.semipro_string);

                        }

                        if (squash == 0) {
                            viewsaq1.setBackgroundResource(R.color.color_blue);
                            ivsaq2.setBackgroundResource(R.drawable.rectangle_non1);
                            sqhRev = false;
                            tvBedmintorExperties.setText(R.string.amateure_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catThreeId), 1);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_three = getResources().getString(R.string.amature_string);


                        }
                    }
                }

                return true;
            }
        });

        llTennis1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!tenRev) {
                        ten++;

                        if (ten == 1) {
                            viewTen1.setBackgroundResource(R.color.color_white);
                            ivten2.setBackgroundResource(R.drawable.rectangle_non);
                            tvTennis_two_experties.setText(R.string.intermediate_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catFourId), 2);
                            db.saveSelectedgames(selectedGameModal);

                            info_msg_four = getResources().getString(R.string.semipro_string);

                        }

                        if (ten == 2) {
                            ivten3.setBackgroundResource(R.drawable.rectangle_non);
                            viewTen2.setBackgroundResource(R.color.color_white);
                            tvTennis_two_experties.setText(R.string.pro_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catFourId), 3);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_four = getResources().getString(R.string.pro_string);


                        }
                        if (ten == 3) {
                            tenRev = true;
                            viewTen3.setBackgroundResource(R.color.color_white);
                            ivten4.setBackgroundResource(R.drawable.rectangle_non);
                            tvTennis_two_experties.setText(R.string.expert_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catFourId), 4);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_four = getResources().getString(R.string.expert_string);



                        }
                    } else if (tenRev) {
                        ten--;

                        if (ten == 2) {
                            viewTen3.setBackgroundResource(R.color.color_blue);
                            ivten4.setBackgroundResource(R.drawable.rectangle_non1);
                            tvTennis_two_experties.setText(R.string.pro_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catFourId), 3);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_four = getResources().getString(R.string.pro_string);


                        }
                        if (ten == 1) {
                            ivten3.setBackgroundResource(R.drawable.rectangle_non1);
                            viewTen2.setBackgroundResource(R.color.color_blue);
                            tvTennis_two_experties.setText(R.string.intermediate_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catFourId), 2);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_four = getResources().getString(R.string.semipro_string);


                        }

                        if (ten == 0) {
                            viewTen1.setBackgroundResource(R.color.color_blue);
                            ivten2.setBackgroundResource(R.drawable.rectangle_non1);
                            tenRev = false;
                            tvTennis_two_experties.setText(R.string.amateure_str);

                            SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catFourId), 1);
                            db.saveSelectedgames(selectedGameModal);
                            info_msg_four = getResources().getString(R.string.amature_string);


                        }
                    }
                }


                return true;
            }
        });


        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(face2);
        btnSubmit.setOnClickListener(this);
        llSquash.setOnClickListener(this);

        llTennis1.setOnClickListener(this);
        llBad1.setOnClickListener(this);
        llSquash1.setOnClickListener(this);
        llTableTenn1.setOnClickListener(this);

        loadHomePagedata();
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


                updateLocationUI();
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


    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {

            getLocationName(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());


        }

//        toggleButtons();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

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

                        updateLocationUI();
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
                                    rae.startResolutionForResult(Activity_ChooseSports.this, REQUEST_CHECK_SETTINGS);
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

                        updateLocationUI();
                    }
                });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSubmit:
                Dexter.withActivity(this)
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


                if (mRequestingLocationUpdates) {


                    try {
                        final List<SelectedGameModal> items = db.getActiveCartList();


                        Gson gson = new Gson();
                        // convert your list to json
                        String jsonCartList = gson.toJson(items);
                        // print your generated json
                        System.out.println("jsonCartList: " + jsonCartList);


                        Log.d("SELECTEDGAMEDATA", jsonCartList);


                        String userid = sharedPreferences.getSharedPreferenceString(Activity_ChooseSports.this, SharedprefKeys.USER_ID, "");


                        if (isgameselected||isgameTwoselected||isgamethreeselected||isgamefourselected){

                            senddatatoserver(userid, jsonCartList, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), addresss);

                        }else {

                            Toast.makeText(this,"Please Select a game first! ",Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception ex) {
//                        Toast.makeText(this,"Please Select a game first! ",Toast.LENGTH_LONG).show();

                        ex.printStackTrace();
                    }
                }

                break;

            case R.id.llBad:

                isgamethreeselected = true;
                llBad.setVisibility(View.GONE);
                llBad1.setVisibility(View.VISIBLE);


                SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catOneId), 1);
                db.saveSelectedgames(selectedGameModal);

//                SelectedGameModal selectedGameModal = new SelectedGameModal(Integer.parseInt(catOneId),1);
//
//                selectedGameModalList.add(selectedGameModal);
//
                break;

            case R.id.ivCrossBad:
                isgamethreeselected = false;
                llBad1.setVisibility(View.GONE);
                llBad.setVisibility(View.VISIBLE);


                    db.deleteActiveSelectedgames(Integer.parseInt(catOneId));




                break;

            case R.id.llTableTenn:
                isgameTwoselected = true;
                llTableTenn.setVisibility(View.GONE);
                llTableTenn1.setVisibility(View.VISIBLE);


                selectedGameModal = new SelectedGameModal(Integer.parseInt(catTwoId), 1);
                db.saveSelectedgames(selectedGameModal);

//                selectedGameModal = new SelectedGameModal(Integer.parseInt(catTwoId),1);
//
//                selectedGameModalList.add(selectedGameModal);

                break;

            case R.id.ivCrosstt:
                isgameTwoselected =false;
                llTableTenn1.setVisibility(View.GONE);
                llTableTenn.setVisibility(View.VISIBLE);

                db.deleteActiveSelectedgames(Integer.parseInt(catTwoId));


//                if (db.getActiveSelectegameSize()>0){
//
//                    db.deleteActiveSelectedgames(Integer.parseInt(catTwoId));
//                }

                break;

            case R.id.llSquash:
                isgameselected = true;
                llSquash.setVisibility(View.GONE);
                llSquash1.setVisibility(View.VISIBLE);

                selectedGameModal = new SelectedGameModal(Integer.parseInt(catThreeId), 1);
                db.saveSelectedgames(selectedGameModal);


                break;

            case R.id.ivCrossBadd:
                isgameselected =false;
                llSquash1.setVisibility(View.GONE);
                llSquash.setVisibility(View.VISIBLE);

//                if (db.getActiveSelectegameSize()>0){
                    db.deleteActiveSelectedgames(Integer.parseInt(catThreeId));
//
//                }

                break;

            case R.id.llTennis:

                llTennis.setVisibility(View.GONE);
                llTennis1.setVisibility(View.VISIBLE);

                isgamefourselected = true;
                selectedGameModal = new SelectedGameModal(Integer.parseInt(catFourId), 1);
                db.saveSelectedgames(selectedGameModal);


//                selectedGameModal = new SelectedGameModal(Integer.parseInt(catFourId),1);
//
//                selectedGameModalList.add(selectedGameModal);


                break;

            case R.id.ivCrossTen:
                isgamefourselected = false;
                llTennis1.setVisibility(View.GONE);
                llTennis.setVisibility(View.VISIBLE);

//                if (db.getActiveSelectegameSize()>0){
                    db.deleteActiveSelectedgames(Integer.parseInt(catFourId));
//
//                }

                break;


        }

    }



    private void showinfoOneOflevel() {

        final Dialog dialog = new Dialog(Activity_ChooseSports.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialog_forinfo, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);

        TextView msg = v1.findViewById(R.id.msg);



        msg.setText(info_msg);




        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    private void showinfoTwoOflevel() {

        final Dialog dialog = new Dialog(Activity_ChooseSports.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialog_forinfo, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);

        TextView msg = v1.findViewById(R.id.msg);

        msg.setText(info_msg_two);




        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private void showinfoThreeOflevel() {

        final Dialog dialog = new Dialog(Activity_ChooseSports.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialog_forinfo, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);

        TextView msg = v1.findViewById(R.id.msg);

        msg.setText(info_msg_three);




        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private void showinfoFourOflevel() {

        final Dialog dialog = new Dialog(Activity_ChooseSports.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialog_forinfo, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);

        TextView msg = v1.findViewById(R.id.msg);

        msg.setText(info_msg_four);




        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
//                        toggleButtons();
                    }
                });
    }

    private void displayData() {
        final List<SelectedGameModal> items = db.getActiveCartList();


        Gson gson = new Gson();
        // convert your list to json
        String jsonCartList = gson.toJson(items);
        // print your generated json
        System.out.println("jsonCartList: " + jsonCartList);


        Log.d("SELECTEDGAMEDATA", jsonCartList);
    }


    private void getLocationName(double latitude, double longitude) {

        Apis apis = RestAdapter.creategoogleAPI();
        Call<FetchLocationRespo> callbackCall = apis.getLocationName(latitude + "," + longitude, true, "AIzaSyCY8z3-HeF4uogoq6IDXI1gm1YcnnXR8dc");
        callbackCall.enqueue(new Callback<FetchLocationRespo>() {
            @Override
            public void onResponse(Call<FetchLocationRespo> call, Response<FetchLocationRespo> response) {
                FetchLocationRespo resp = response.body();


                addresss = resp.getformatedaddress();

            }

            @Override
            public void onFailure(Call<FetchLocationRespo> call, Throwable t) {


            }
        });
    }


    private void loadHomePagedata() {
        Apis apis = RestAdapter.createAPI();
        Call<CategoriesDataResponse> callbackCall = apis.getCategories();
        callbackCall.enqueue(new Callback<CategoriesDataResponse>() {
            @Override
            public void onResponse(Call<CategoriesDataResponse> call, Response<CategoriesDataResponse> response) {
                CategoriesDataResponse resp = response.body();
                if (resp != null && !resp.getError()) {

                    Picasso.get().load(resp.getCategoryData().get(0).getImage()).placeholder(R.drawable.circle).into(iv_squash);
                    Picasso.get().load(resp.getCategoryData().get(1).getImage()).placeholder(R.drawable.circle).into(ivTableteniss);
                    Picasso.get().load(resp.getCategoryData().get(2).getImage()).placeholder(R.drawable.circle).into(ivBad_minton);
                    Picasso.get().load(resp.getCategoryData().get(3).getImage()).placeholder(R.drawable.circle).into(ivTennise);

                    tvSquash1.setText(resp.getCategoryData().get(0).getName());
                    tvTable.setText(resp.getCategoryData().get(1).getName());
                    tvBad1.setText(resp.getCategoryData().get(2).getName());
                    tvTennis1.setText(resp.getCategoryData().get(3).getName());

                    catOneId = resp.getCategoryData().get(0).getId();
                    catTwoId = resp.getCategoryData().get(1).getId();
                    catThreeId = resp.getCategoryData().get(2).getId();
                    catFourId = resp.getCategoryData().get(3).getId();


                } else {
                    dialogServerNotConnect(Activity_ChooseSports.this, "NOT CONNECTED TO SERVER", 0);
                }
            }

            @Override
            public void onFailure(Call<CategoriesDataResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                dialogServerNotConnect(Activity_ChooseSports.this, t.getMessage(), 0);
            }
        });
    }

    private void senddatatoserver(String user_id, String selectedgames, double latitude, double longitude, String address) {

        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<SaveCatDataRespo> callbackCall = apis.setplayerdata(user_id, selectedgames, latitude, longitude, address);
        callbackCall.enqueue(new Callback<SaveCatDataRespo>() {
            @Override
            public void onResponse(Call<SaveCatDataRespo> call, Response<SaveCatDataRespo> response) {
                SaveCatDataRespo resp = response.body();
                if (resp != null && !resp.getError()) {


                    loader_dialog.dismiss();

                    sharedPreferences.setSharedPreferenceBoolean(Activity_ChooseSports.this, SharedprefKeys.ISLOGEDIN, true);


                    Intent intent = new Intent(Activity_ChooseSports.this, Activity_Badminton.class);
                    startActivity(intent);
                    finishAffinity();


                } else {
                    loader_dialog.dismiss();
                    sharedPreferences.setSharedPreferenceBoolean(Activity_ChooseSports.this, SharedprefKeys.ISLOGEDIN, false);


                    dialogServerNotConnect(Activity_ChooseSports.this, "data"+user_id+selectedgames+latitude+"{---}"+longitude+address, 1);
                }
            }

            @Override
            public void onFailure(Call<SaveCatDataRespo> call, Throwable t) {
                loader_dialog.dismiss();
                sharedPreferences.setSharedPreferenceBoolean(Activity_ChooseSports.this, SharedprefKeys.ISLOGEDIN, false);


                Log.e("onFailure", t.getMessage());
                dialogServerNotConnect(Activity_ChooseSports.this, t.getMessage(), 0);

            }
        });
    }


    public void dialogServerNotConnect(final Context context, final String needed_money, int flag) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_info, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/

//        final EditText et_money = view.findViewById(R.id.et_money);
//        final LinearLayout offerWindow = view.findViewById(R.id.offerWindow);
//        final TextView content = view.findViewById(R.id.content);
//        final TextView totalWallet_Amount = view.findViewById(R.id.totalWallet_Amount);
//        final TextView minium_amt = view.findViewById(R.id.minium_amt);

        Typeface face1 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Regular.otf");

        Button bt_positive = view.findViewById(R.id.bt_positive);
        TextView content = view.findViewById(R.id.content);

        content.setText(needed_money);
        content.setTypeface(face1);


        bt_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (flag == 1) {
//                    sharedPreferences.setSharedPreferenceBoolean(Activity_ChooseSports.this, SharedprefKeys.ISLOGEDIN, true);

//                    Intent intent = new Intent(Activity_ChooseSports.this,Activity_Badminton.class);
//                    startActivity(intent);
//                    finishAffinity();

                }
            }
        });


        ((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }


    public void loader_dialog(final Context context) {
        loader_dialog = new Dialog(context);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progress_dilog_lyt, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/
        final FillableLoader fillableLoader = view.findViewById(R.id.fillableLoader);
        fillableLoader.setSvgPath(MODAL);
        fillableLoader.start();


        ((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loader_dialog.setContentView(view);
        final Window window = loader_dialog.getWindow();
        loader_dialog.setCancelable(false);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        loader_dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e("TAG", "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e("TAG", "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
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

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }


}
