package com.sportskonnect.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sportskonnect.Activity_Badminton;
import com.sportskonnect.Activity_ChooseSports;
import com.sportskonnect.R;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.AddGameInfoData;
import com.sportskonnect.api.Callbacks.CategoriesDataResponse;
import com.sportskonnect.api.Callbacks.GameAddDatum;
import com.sportskonnect.api.Callbacks.UpdateGameInfoData;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.database.DatabaseHandler;
import com.sportskonnect.modal.SelectedGameModal;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMoreGame extends AppCompatActivity implements View.OnClickListener {


    private static final String DOWNLOAD_URL = "http:\\/\\/thinklabs.io\\/rackconnect\\/assets\\/images\\/category\\/squash.png";
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    List<GameAddDatum> gameAddDatumList = new ArrayList<>();
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
    int squash = 0;
    boolean sqhRev = false;

    private String info_msg_two="";
    private String info_msg_three="";
    private String info_msg_four="";
    LinearLayout squesh_lvlpoint;
    int ten = 0;
    boolean tenRev = false;
    private String info_msg = "";
    private SharedPreference sharedPreferences;
    private Dialog dialog;
    private ImageView ivBad_minton, ivTennise, ivTableteniss, iv_squash;
    private List<SelectedGameModal> selectedGameModalList = new ArrayList<>();
    private String catOneId = "";
    private String catTwoId = "";
    private String catThreeId = "";
    private String catFourId = "";
    private DatabaseHandler db;
    private TextView gameOne_lbl,gameTwo_lbl,gameThree_lbl,gameFour_lbl;


    //    location update variablse
    private ImageView squash_i, tabletenis_i, bedmintan_i, tenis_i;
    private Dialog loader_dialog;
    private Handler mHandler = new Handler();
    // location last updated time
    private String mLastUpdateTime;
    private boolean isgameselected = false;
    private boolean isgameTwoselected = false;
    private boolean isgamethreeselected = false;
    private boolean isgamefourselected = false;
    private String userid = "";
    private String jsonCartList = "";
    private LinearLayout tt_llpoint;
    private LinearLayout bad_ll_point;
    private LinearLayout tenis_llPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_game);


        db = new DatabaseHandler(this);
//        loader_dialog(this);
        init();


    }

    private void init() {

        info_msg = getResources().getString(R.string.amature_string);

        main_text_title = (TextView) findViewById(R.id.main_text_title);
        tvSquash1 = (TextView) findViewById(R.id.tvSquash1);
        tvTable = (TextView) findViewById(R.id.tvTable);
        tvBad1 = (TextView) findViewById(R.id.tvBad1);
        tvTennis1 = (TextView) findViewById(R.id.tvTennis1);
        tvtt1 = (TextView) findViewById(R.id.tvtt1);
        tvSq1 = (TextView) findViewById(R.id.tvSq1);
        tvtb1 = (TextView) findViewById(R.id.tvtb1);
        tvBad11 = (TextView) findViewById(R.id.tvBad11);
        gameOne_lbl = (TextView) findViewById(R.id.gameOne_lbl);
        gameTwo_lbl = (TextView) findViewById(R.id.gameTwo_lbl);
        gameThree_lbl = (TextView) findViewById(R.id.gameThree_lbl);
        gameFour_lbl = (TextView) findViewById(R.id.gameFour_lbl);
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
        squesh_lvlpoint = findViewById(R.id.squesh_lvlpoint);
        tvBedmintorExperties = findViewById(R.id.tvSquash);
        tt_llpoint = findViewById(R.id.tt_llpoint);
        bad_ll_point = findViewById(R.id.bad_ll_point);
        tenis_llPoints = findViewById(R.id.tenis_llPoints);
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
        getgameInfo(SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, ""));


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

//
//                if (isgameselected || isgameTwoselected || isgamethreeselected || isgamefourselected) {
//
////                    senddatatoserver(userid, jsonCartList, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), addresss);
//
//                } else {
//
//                    Toast.makeText(this, "Please Select a game first! ", Toast.LENGTH_LONG).show();
//                }

                try {

                    final List<SelectedGameModal> items = db.getActiveCartList();


                    Gson gson = new Gson();
                    // convert your list to json
                    jsonCartList = gson.toJson(items);
                    // print your generated json
                    System.out.println("jsonCartList: " + jsonCartList);


                    Log.d("SELECTEDGAMEDATA", jsonCartList);

                    userid = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");


                    confirmdialog();


                } catch (Exception ex) {
                    ex.printStackTrace();
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


                break;

            case R.id.ivCrosstt:
                isgameTwoselected = false;
                llTableTenn1.setVisibility(View.GONE);
                llTableTenn.setVisibility(View.VISIBLE);

                db.deleteActiveSelectedgames(Integer.parseInt(catTwoId));


                break;

            case R.id.llSquash:
                isgameselected = true;
                llSquash.setVisibility(View.GONE);
                llSquash1.setVisibility(View.VISIBLE);

                selectedGameModal = new SelectedGameModal(Integer.parseInt(catThreeId), 1);
                db.saveSelectedgames(selectedGameModal);


                break;

            case R.id.ivCrossBadd:
                isgameselected = false;
                llSquash1.setVisibility(View.GONE);
                llSquash.setVisibility(View.VISIBLE);


                db.deleteActiveSelectedgames(Integer.parseInt(catThreeId));

                break;

            case R.id.llTennis:

                llTennis.setVisibility(View.GONE);
                llTennis1.setVisibility(View.VISIBLE);

                isgamefourselected = true;
                selectedGameModal = new SelectedGameModal(Integer.parseInt(catFourId), 1);
                db.saveSelectedgames(selectedGameModal);

                break;

            case R.id.ivCrossTen:
                isgamefourselected = false;
                llTennis1.setVisibility(View.GONE);
                llTennis.setVisibility(View.VISIBLE);

                db.deleteActiveSelectedgames(Integer.parseInt(catFourId));


                break;


        }
    }


    private void showinfoOneOflevel() {

        final Dialog dialog = new Dialog(AddMoreGame.this);
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

        final Dialog dialog = new Dialog(AddMoreGame.this);
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

        final Dialog dialog = new Dialog(AddMoreGame.this);
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

        final Dialog dialog = new Dialog(AddMoreGame.this);
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




    private void displayData() {
        final List<SelectedGameModal> items = db.getActiveCartList();


        Gson gson = new Gson();
        // convert your list to json
        String jsonCartList = gson.toJson(items);
        // print your generated json
        System.out.println("jsonCartList: " + jsonCartList);


        Log.d("SELECTEDGAMEDATA", jsonCartList);
    }


    private void senddatatoserver(String user_id, String selectedgames) {


        Apis apis = RestAdapter.createAPI();
        Call<UpdateGameInfoData> callbackCall = apis.updateGameInfo(user_id, selectedgames);
        callbackCall.enqueue(new Callback<UpdateGameInfoData>() {
            @Override
            public void onResponse(Call<UpdateGameInfoData> call, Response<UpdateGameInfoData> response) {
                UpdateGameInfoData resp = response.body();
                if (resp != null && !resp.getError()) {


                    Intent intent = new Intent(AddMoreGame.this, Activity_Badminton.class);
                    startActivity(intent);
                    finishAffinity();


                } else {


                }
            }

            @Override
            public void onFailure(Call<UpdateGameInfoData> call, Throwable t) {
                loader_dialog.dismiss();

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
//                    dialogServerNotConnect(Activity_ChooseSports.this, "NOT CONNECTED TO SERVER", 0);
                }
            }

            @Override
            public void onFailure(Call<CategoriesDataResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_ChooseSports.this, t.getMessage(), 0);
            }
        });
    }


    private void getgameInfo(String user_id) {
        Apis apis = RestAdapter.createAPI();
        Call<AddGameInfoData> callbackCall = apis.getgameInfo(user_id);
        callbackCall.enqueue(new Callback<AddGameInfoData>() {
            @Override
            public void onResponse(Call<AddGameInfoData> call, Response<AddGameInfoData> response) {
                AddGameInfoData resp = response.body();
                if (resp != null && !resp.getError()) {

                    Log.d("RESPOGAME", new Gson().toJson(resp.getData()));

                    for (int i = 0; i < resp.getData().size(); i++) {
                       /* GameAddDatum gameAddDatum = new GameAddDatum(
                                resp.getData().get(i).getId(),
                                resp.getData().get(i).getCatid(),
                                resp.getData().get(i).getLevelid(),
                                resp.getData().get(i).getUserid()

                        );
                        gameAddDatumList.add(gameAddDatum);
*/
                        if (resp.getData().get(i).getCatid().equals("1")) {

                            llBad.setClickable(false);
                            llBad1.setVisibility(View.VISIBLE);
                            llBad1.setClickable(false);

                            llBad1.setEnabled(false);
                            llBad.setVisibility(View.GONE);


                            gameOne_lbl.setVisibility(View.INVISIBLE);
//                            squesh_lvlpoint.setVisibility(View.GONE);


                            ivCrossBad.setVisibility(View.GONE);

                            if (resp.getData().get(i).getLevelid().equals("2")) {

                                viewBad1.setBackgroundResource(R.color.color_white);
                                ivBad2.setBackgroundResource(R.drawable.rectangle_non);
                                info_msg = getResources().getString(R.string.semipro_string);


                            } else if (resp.getData().get(i).getLevelid().equals("3")) {

                                ivBad3.setBackgroundResource(R.drawable.rectangle_non);
                                viewBad2.setBackgroundResource(R.color.color_white);
                                info_msg = getResources().getString(R.string.pro_string);


                            }else if(resp.getData().get(i).getLevelid().equals("4")){
                                viewBad3.setBackgroundResource(R.color.color_white);
                                ivBad4.setBackgroundResource(R.drawable.rectangle_non);
                                info_msg = getResources().getString(R.string.expert_string);


                            }else if(resp.getData().get(i).getLevelid().equals("1")){
                                viewBad1.setBackgroundResource(R.color.color_blue);
                                ivBad2.setBackgroundResource(R.drawable.rectangle_non1);
                                info_msg = getResources().getString(R.string.amature_string);

                            }

                            tv_squashexperties.setText(Constant.getGameLevelFromlevelId(resp.getData().get(i).getLevelid()));

//                            info_msg = getResources().getString(R.string.amature_string);


                        } else {

//                            llBad.setClickable(true);
//                            llBad.setVisibility(View.VISIBLE);
//
//                            llBad1.setVisibility(View.GONE);
//                            llBad1.setClickable(true);
//                            ivCrossBad.setVisibility(View.VISIBLE);


                        }
                        if (resp.getData().get(i).getCatid().equals("2")) {

                            gameTwo_lbl.setVisibility(View.INVISIBLE);

                            llTableTenn.setClickable(false);
                            llTableTenn.setVisibility(View.GONE);

                            llTableTenn1.setVisibility(View.VISIBLE);
                            llTableTenn1.setClickable(false);
                            llTableTenn1.setEnabled(false);
                            ivCrosstt.setVisibility(View.GONE);
                           /* tt_llpoint.setVisibility(View.GONE);*/


                            if (resp.getData().get(i).getLevelid().equals("2")) {

                                viewtt1.setBackgroundResource(R.color.color_white);
                                ivtt2.setBackgroundResource(R.drawable.rectangle_non);
                                info_msg_two = getResources().getString(R.string.semipro_string);

                            } else if (resp.getData().get(i).getLevelid().equals("3")) {

                                viewtt3.setBackgroundResource(R.color.color_white);
                                ivtt4.setBackgroundResource(R.drawable.rectangle_non);
                                info_msg_two = getResources().getString(R.string.pro_string);

                            }else if(resp.getData().get(i).getLevelid().equals("4")){
                                viewtt3.setBackgroundResource(R.color.color_blue);
                                ivtt4.setBackgroundResource(R.drawable.rectangle_non1);
                                info_msg_two = getResources().getString(R.string.expert_string);

                            }else if(resp.getData().get(i).getLevelid().equals("1")){
                                viewtt1.setBackgroundResource(R.color.color_blue);
                                ivtt2.setBackgroundResource(R.drawable.rectangle_non1);
                                info_msg_two = getResources().getString(R.string.amature_string);

                            }



                            tv_tt_experties.setText(Constant.getGameLevelFromlevelId(resp.getData().get(i).getLevelid()));


                        } else {
//
//                            llTableTenn.setClickable(true);
//                            llTableTenn.setVisibility(View.VISIBLE);
//                            llTableTenn1.setVisibility(View.GONE);
//                            llTableTenn1.setClickable(true);
//                            ivCrosstt.setVisibility(View.VISIBLE);


                        }
                        if (resp.getData().get(i).getCatid().equals("3")) {
                            gameThree_lbl.setVisibility(View.INVISIBLE);

                            llSquash.setClickable(false);
                            llSquash1.setVisibility(View.VISIBLE);
                            llSquash.setVisibility(View.GONE);

                            llSquash1.setClickable(false);
                            llSquash1.setEnabled(false);

                            ivCrossBadd.setVisibility(View.GONE);
                           /* bad_ll_point.setVisibility(View.GONE);*/



                            if (resp.getData().get(i).getLevelid().equals("2")) {

                                viewsaq1.setBackgroundResource(R.color.color_white);
                                ivsaq2.setBackgroundResource(R.drawable.rectangle_non);
                                info_msg_three = getResources().getString(R.string.semipro_string);


                            } else if (resp.getData().get(i).getLevelid().equals("3")) {

                                ivsaq3.setBackgroundResource(R.drawable.rectangle_non);
                                viewsaq2.setBackgroundResource(R.color.color_white);
                                info_msg_three = getResources().getString(R.string.pro_string);


                            }else if(resp.getData().get(i).getLevelid().equals("4")){
                                viewsaq3.setBackgroundResource(R.color.color_white);
                                ivsaq4.setBackgroundResource(R.drawable.rectangle_non);
                                info_msg_three = getResources().getString(R.string.expert_string);


                            }else if(resp.getData().get(i).getLevelid().equals("1")){
                                viewsaq1.setBackgroundResource(R.color.color_blue);
                                ivsaq2.setBackgroundResource(R.drawable.rectangle_non1);
                                info_msg_three = getResources().getString(R.string.amature_string);

                            }


                            tvBedmintorExperties.setText(Constant.getGameLevelFromlevelId(resp.getData().get(i).getLevelid()));

                        } else {



                        }

                        if (resp.getData().get(i).getCatid().equals("4")) {
                            gameFour_lbl.setVisibility(View.INVISIBLE);

                            llTennis.setClickable(false);
                            llTennis1.setVisibility(View.VISIBLE);
                            llTennis.setVisibility(View.GONE);
                            llTennis1.setClickable(false);
                            llTennis1.setEnabled(false);
                            ivCrossTen.setVisibility(View.GONE);
                            /*tenis_llPoints.setVisibility(View.GONE);
*/

                            if (resp.getData().get(i).getLevelid().equals("2")) {

                                viewTen1.setBackgroundResource(R.color.color_white);
                                ivten2.setBackgroundResource(R.drawable.rectangle_non);
                                info_msg_four = getResources().getString(R.string.semipro_string);

                            } else if (resp.getData().get(i).getLevelid().equals("3")) {

                                ivten3.setBackgroundResource(R.drawable.rectangle_non);
                                viewTen2.setBackgroundResource(R.color.color_white);
                                info_msg_four = getResources().getString(R.string.pro_string);

                            }else if(resp.getData().get(i).getLevelid().equals("4")){
                                viewTen3.setBackgroundResource(R.color.color_white);
                                ivten4.setBackgroundResource(R.drawable.rectangle_non);
                                info_msg_four = getResources().getString(R.string.expert_string);

                            }else if(resp.getData().get(i).getLevelid().equals("1")){
                                viewTen1.setBackgroundResource(R.color.color_blue);
                                ivten2.setBackgroundResource(R.drawable.rectangle_non1);
                                info_msg_four = getResources().getString(R.string.amature_string);

                            }


                            tvTennis_two_experties.setText(Constant.getGameLevelFromlevelId(resp.getData().get(i).getLevelid()));

                        } else {
//                            llTennis.setClickable(true);
//                            llTennis.setVisibility(View.VISIBLE);
//
//                            llTableTenn1.setVisibility(View.GONE);
//                            llTableTenn1.setClickable(true);
//                            ivCrossTen.setVisibility(View.VISIBLE);

                        }
                    }


                } else {
//                    dialogServerNotConnect(Activity_ChooseSports.this, "NOT CONNECTED TO SERVER", 0);
                }
            }

            @Override
            public void onFailure(Call<AddGameInfoData> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_ChooseSports.this, t.getMessage(), 0);
            }
        });
    }


    private void confirmdialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialog_forsurity, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);


        TextView msg = (TextView) v1.findViewById(R.id.msg);
        msg.setText("Are you sure? Do you want to Proceed...");
        Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) v1.findViewById(R.id.btn_submit);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                tvPl.setVisibility(View.GONE);
//                btnSubmit.setText("GO TO HOMEPAGE");
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                senddatatoserver(userid, jsonCartList);


            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }
}
