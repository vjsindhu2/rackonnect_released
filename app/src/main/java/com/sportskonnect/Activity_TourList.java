package com.sportskonnect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.RemoteViews;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.sportskonnect.activities.ExploreMatchesActivity;
import com.sportskonnect.adapters.DoublesRvAdapter;
import com.sportskonnect.adapters.MatchListAdapter;
import com.sportskonnect.adapters.SelectedCityAdapter;
import com.sportskonnect.adapters.SelectedGamesAdapter;
import com.sportskonnect.adapters.SinglesRvAdapter;
import com.sportskonnect.adapters.TourMatchListAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.Citynamearray;
import com.sportskonnect.api.Callbacks.FetchSavedLocationRespo;
import com.sportskonnect.api.Callbacks.FetchTourMatchRespo;
import com.sportskonnect.api.Callbacks.MatchListDualResponse;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.DualMatchListDatum;
import com.sportskonnect.modal.OpponetsDatum;
import com.sportskonnect.modal.SelectedGameModal;
import com.sportskonnect.modal.TourDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.sportskonnect.utility.RecyclerTouchListener;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.codetail.widget.RevealFrameLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_TourList extends AppCompatActivity implements View.OnClickListener {


    Typeface face2;

    RecyclerView tourlist_rv;
    private List<TourDatum> tourDatumList = new ArrayList<>();
    private TourMatchListAdapter tourmatchListAdapter;
    private String user_id = "";
    private String current_latitude = "";
    private String current_longetude = "";
    private String current_radius = "";
    private String selected_cat = "";
    private View lyt_item;
    private TextView totaltours_tv;
    private ImageView search_opp;
    private ImageView ivFilter;
    private ImageView colse;
    private RevealFrameLayout revell_ll;
    private LinearLayout tourlist_ll;

    private List<Citynamearray> citynamearrayList = new ArrayList<>();


    private SwipeRefreshLayout swipreferesh;
    private boolean issearchvisible = false;
    private EditText search_et;
    private boolean isfilterClicked = false;
    private NestedScrollView ll_filter;
    private boolean hidden = true;
    private CardView male_cv;
    private CardView female_cv;
    private CardView allplayer_cv;
    private TextView range_lbl,cityname_tv;
    private TextView age_minval;
    private TextView age_maxval;
    private CrystalRangeSeekbar ageSeekbar;
    private CardView anytym_cv;
    private CardView morning_cv;
    private CardView afternoon_cv;
    private CardView evening_cv;
    private TextView lbl_anytym, lbl_tvMorning;
    private TextView lbl_tvAfternoon, lbl_tvevening;
    private Button applyfilter_btn;
    private String gender = "All Players";
    private TextView lbl_tvMale;
    private TextView lbl_tvFemale;
    private TextView lbl_allplayer;
    private String minAge = "";
    private String maxAge = "";
    private String timePref = "";

    private LinearLayout city_select_ll;
    private Dialog choosegame_dialog;
    private SelectedCityAdapter mAdapter;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourlist);


        init();
    }

    private void init() {

        tourlist_rv = (RecyclerView) findViewById(R.id.tourlist_rv);
        totaltours_tv = findViewById(R.id.totaltours_tv);
        swipreferesh = findViewById(R.id.swipreferesh);
        lyt_item = findViewById(R.id.lyt_item);
        search_opp = findViewById(R.id.search_opp);
        search_et = findViewById(R.id.search_et);
        ivFilter = findViewById(R.id.ivFilter);
        lbl_allplayer = findViewById(R.id.lbl_allplayer);
        cityname_tv = findViewById(R.id.cityname_tv);
        lbl_tvFemale = findViewById(R.id.lbl_tvFemale);
        city_select_ll = findViewById(R.id.city_select_ll);
        range_lbl = (TextView) findViewById(R.id.range_lbl);

        ll_filter = findViewById(R.id.ll_filter);
        lbl_tvMale = findViewById(R.id.lbl_tvMale);
        colse = findViewById(R.id.colse);
        tourlist_ll = findViewById(R.id.tourlist_ll);
        applyfilter_btn = (Button) findViewById(R.id.applyfilter_btn);

        age_minval = (TextView) findViewById(R.id.age_minval);
        age_maxval = (TextView) findViewById(R.id.age_maxval);
        revell_ll = (RevealFrameLayout) findViewById(R.id.revell_ll);

        male_cv = (CardView) findViewById(R.id.male_cv);
        female_cv = (CardView) findViewById(R.id.female_cv);
        allplayer_cv = (CardView) findViewById(R.id.bothgender_cv);
        ageSeekbar = (CrystalRangeSeekbar) findViewById(R.id.ageSeekbar);


        anytym_cv = findViewById(R.id.anytym_cv);
        morning_cv = findViewById(R.id.morning_cv);
        afternoon_cv = findViewById(R.id.afternoon_cv);
        evening_cv = findViewById(R.id.evening_cv);

        lbl_anytym = findViewById(R.id.lbl_anytym);
        lbl_tvMorning = findViewById(R.id.lbl_tvMorning);
        lbl_tvAfternoon = findViewById(R.id.lbl_tvAfternoon);
        lbl_tvevening = findViewById(R.id.lbl_tvevening);

        male_cv.setOnClickListener(this);
        female_cv.setOnClickListener(this);
        allplayer_cv.setOnClickListener(this);

        applyfilter_btn.setOnClickListener(this);
        face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        tourlist_rv.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        tourlist_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        tourlist_rv.setItemAnimator(new DefaultItemAnimator());

        user_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");
        selected_cat = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "0");

        fetchlocationfromdb(user_id);
        tourmatchListAdapter = new TourMatchListAdapter(Activity_TourList.this, tourDatumList);


        colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_filter.setVisibility(View.INVISIBLE);
                tourlist_ll.setVisibility(View.VISIBLE);

                hidden = true;
                isfilterClicked = false;
            }
        });

        swipreferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchlocationfromdb(user_id);

            }
        });

        search_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issearchvisible = true;
                search_et.setVisibility(View.VISIBLE);


            }
        });


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

                if (tourmatchListAdapter.getItemCount() > 0) {

//                    singlesRVAdapter.getFilter().filter(s);

                    List<TourDatum> filteredList = new ArrayList<>();
                    for (TourDatum row : tourDatumList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getMatchname().toLowerCase().contains(s.toString().toLowerCase())) {


                            filteredList.add(row);
//                            Log.d("CHARSTRINGINIF",row.getName().toLowerCase()+"=="+charString.toLowerCase());

                        }
                    }

                    if (filteredList.size() > 0) {
                        tourlist_rv.setVisibility(View.VISIBLE);
                        lyt_item.setVisibility(View.GONE);
                        tourmatchListAdapter = new TourMatchListAdapter(Activity_TourList.this, filteredList);
                        tourlist_rv.setAdapter(tourmatchListAdapter);

                    } else {

                        tourlist_rv.setVisibility(View.GONE);
                        lyt_item.setVisibility(View.VISIBLE);
                    }

//                    singles_rv.notify();

                }

            }
        });


        city_select_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosegameDialog(Activity_TourList.this,"");
            }
        });

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                tourlist_ll.setVisibility(View.GONE);

//                revell_ll.setVisibility(View.VISIBLE);
//                ll_filter.setVisibility(View.VISIBLE);


                int cx = ll_filter.getRight();
                int cy = ll_filter.getTop();
                makeEffect(ll_filter, cx, cy);
                isfilterClicked = true;
//                hidden = false;
                Log.d("LCIK", "CLID");            }
        });


        // set listener
        ageSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {


                age_minval.setText(" Rs." + minValue );
                range_lbl.setText("" + minValue + " - " + maxValue);
                age_maxval.setText(" Rs."+ maxValue );

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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tvJoinMa:
//
//                Intent intent = new Intent(Activity_TourList.this,Activity_TourForm.class);
//                startActivity(intent);
//                break;


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


//                    filteropponets(String.valueOf(selected_cat), level_id, gender, minAge, maxAge, timePref, user_id);
                feltertourlist(selected_cat,cityname_tv.getText().toString().trim(),minAge,maxAge,user_id);

                break;
        }
    }


    private void makeEffect(final NestedScrollView layout, int cx, int cy) {

        int radius = Math.max(layout.getWidth(), layout.getHeight());

        if (hidden) {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, radius);
            layout.setVisibility(View.VISIBLE);
            anim.start();
            invalidateOptionsMenu();
            hidden = false;

        } else {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(layout, cx, cy, radius, 0);
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


    private void fetchlocationfromdb(String user_id) {

        swipreferesh.setRefreshing(true);
        Apis apis = RestAdapter.createAPI();
        Call<FetchSavedLocationRespo> callbackCall = apis.fetchsavedlocation(user_id);
        callbackCall.enqueue(new Callback<FetchSavedLocationRespo>() {
            @Override
            public void onResponse(Call<FetchSavedLocationRespo> call, Response<FetchSavedLocationRespo> response) {
                FetchSavedLocationRespo resp = response.body();
                if (resp != null && !resp.getError()) {

                    swipreferesh.setRefreshing(false);
                    Log.d("DATALOCATION", new Gson().toJson(resp));

                    citynamearrayList.clear();

                    current_latitude = resp.getLat();

                    current_longetude = resp.getLng();
                    current_radius = resp.getRadius();


                    for (int i=0;i<resp.getCitynamearray().size();i++){
                        Citynamearray citynamearray = new Citynamearray(resp.getCitynamearray().get(i).getCityname());



                        citynamearrayList.add(citynamearray);
                    }


                    try{
                        if (!resp.getMaxfees().equals("") && !resp.getMinfees().equals("")) {

                            ageSeekbar.setMaxValue(Integer.parseInt(resp.getMaxfees()));

                            ageSeekbar.setMaxStartValue(Integer.parseInt(resp.getMaxfees())).apply();


                            ageSeekbar.setMinValue(Integer.parseInt(resp.getMinfees()));
                            ageSeekbar.setMinStartValue(Integer.parseInt(resp.getMinfees())).apply();

                        }


                    }catch (Exception ex){ex.printStackTrace();}



                    fetchTourMatchlist(user_id, current_latitude, current_longetude, current_radius, String.valueOf(selected_cat));

//                    dialogServerNotConnect();
                } else {

                    swipreferesh.setRefreshing(false);

//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<FetchSavedLocationRespo> call, Throwable t) {

                swipreferesh.setRefreshing(false);

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    public void finishTourList(View view) {
        this.finish();
    }


    private void fetchTourMatchlist(String user_id, String current_latitude, String current_longetude, String current_radius, String cat_id) {

        swipreferesh.setRefreshing(true);

        Apis apis = RestAdapter.createAPI();
        Call<FetchTourMatchRespo> callbackCall = apis.getAllTour(cat_id, user_id, current_latitude, current_longetude, current_radius);
        callbackCall.enqueue(new Callback<FetchTourMatchRespo>() {
            @Override
            public void onResponse(Call<FetchTourMatchRespo> call, Response<FetchTourMatchRespo> response) {

                tourDatumList.clear();
                FetchTourMatchRespo resp = response.body();
                Log.d("TOUR_RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    swipreferesh.setRefreshing(false);

                    for (int i = 0; i < resp.getData().size(); i++) {

                        TourDatum tourDatum = new TourDatum(
                                resp.getData().get(i).getMatchid(),
                                resp.getData().get(i).getMid(),
                                resp.getData().get(i).getMidname(),
                                resp.getData().get(i).getMidimage(),
                                resp.getData().get(i).getMatchname(),
                                resp.getData().get(i).getLat(),
                                resp.getData().get(i).getLng(),
                                resp.getData().get(i).getAddress(),
                                resp.getData().get(i).getDate(),
                                resp.getData().get(i).getTime(),
                                resp.getData().get(i).getMidlevel(),
                                resp.getData().get(i).getStartflag(),
                                resp.getData().get(i).getCatid(),
                                resp.getData().get(i).getStatus(),
                                resp.getData().get(i).getMidpoint(),
                                resp.getData().get(i).getFidpoint(),
                                resp.getData().get(i).getType(),
                                resp.getData().get(i).getValidatetime(),
                                resp.getData().get(i).getMatchstatus(),
                                resp.getData().get(i).getMatchtype(),
                                resp.getData().get(i).getGroupid(),
                                resp.getData().get(i).getFidarray(),
                                resp.getData().get(i).getGroupMember(),
                                resp.getData().get(i).getAmount(),
                                resp.getData().get(i).getTotalAmount(),
                                resp.getData().get(i).getTxnId(),
                                resp.getData().get(i).getRunnerupAmount(),
                                resp.getData().get(i).getWinnerAmount(),
                                resp.getData().get(i).getCompanyAmount(),
                                resp.getData().get(i).getHostAmount(),
                                resp.getData().get(i).getWinnerarray(),
                                resp.getData().get(i).getEndgame(),
                                resp.getData().get(i).getGender()

                        );


                        tourDatumList.add(tourDatum);


                    }

                    if (tourDatumList.size() > 0) {

//                        lyt_include_two.setVisibility(View.GONE);
                        tourlist_rv.setVisibility(View.VISIBLE);

//                        tvAvaPlayers.setText(String.valueOf(dualMatchListDatumList.size()) + " matches available");


                        tourlist_rv.setAdapter(tourmatchListAdapter);

                        totaltours_tv.setText(tourDatumList.size() + " tournaments available");

                    } else {

                        swipreferesh.setRefreshing(false);

                        lyt_item.setVisibility(View.VISIBLE);
                        tourlist_rv.setVisibility(View.GONE);
//
                        totaltours_tv.setText("0 Matches available");


                    }


//                    dialogServerNotConnect();
                } else {
                    swipreferesh.setRefreshing(false);

                    lyt_item.setVisibility(View.VISIBLE);
                    tourlist_rv.setVisibility(View.GONE);
//
                    totaltours_tv.setText("0 Matches available");


//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<FetchTourMatchRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }



    private void feltertourlist(String catid, String cityname, String minfee, String maxfee, String userid) {

        Apis apis = RestAdapter.createAPI();
        Call<FetchTourMatchRespo> callbackCall = apis.tournamentfilter(catid, cityname, minfee, maxfee, userid);
        callbackCall.enqueue(new Callback<FetchTourMatchRespo>() {
            @Override
            public void onResponse(Call<FetchTourMatchRespo> call, Response<FetchTourMatchRespo> response) {


                tourDatumList.clear();
                FetchTourMatchRespo resp = response.body();
                Log.d("TOUR_RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    swipreferesh.setRefreshing(false);

                    for (int i = 0; i < resp.getData().size(); i++) {

                        TourDatum tourDatum = new TourDatum(
                                resp.getData().get(i).getMatchid(),
                                resp.getData().get(i).getMid(),
                                resp.getData().get(i).getMidname(),
                                resp.getData().get(i).getMidimage(),
                                resp.getData().get(i).getMatchname(),
                                resp.getData().get(i).getLat(),
                                resp.getData().get(i).getLng(),
                                resp.getData().get(i).getAddress(),
                                resp.getData().get(i).getDate(),
                                resp.getData().get(i).getTime(),
                                resp.getData().get(i).getMidlevel(),
                                resp.getData().get(i).getStartflag(),
                                resp.getData().get(i).getCatid(),
                                resp.getData().get(i).getStatus(),
                                resp.getData().get(i).getMidpoint(),
                                resp.getData().get(i).getFidpoint(),
                                resp.getData().get(i).getType(),
                                resp.getData().get(i).getValidatetime(),
                                resp.getData().get(i).getMatchstatus(),
                                resp.getData().get(i).getMatchtype(),
                                resp.getData().get(i).getGroupid(),
                                resp.getData().get(i).getFidarray(),
                                resp.getData().get(i).getGroupMember(),
                                resp.getData().get(i).getAmount(),
                                resp.getData().get(i).getTotalAmount(),
                                resp.getData().get(i).getTxnId(),
                                resp.getData().get(i).getRunnerupAmount(),
                                resp.getData().get(i).getWinnerAmount(),
                                resp.getData().get(i).getCompanyAmount(),
                                resp.getData().get(i).getHostAmount(),
                                resp.getData().get(i).getWinnerarray(),
                                resp.getData().get(i).getEndgame(),
                                resp.getData().get(i).getGender()

                        );


                        tourDatumList.add(tourDatum);


                    }

                    if (tourDatumList.size() > 0) {

//                        lyt_include_two.setVisibility(View.GONE);
                        tourlist_rv.setVisibility(View.VISIBLE);

//                        tvAvaPlayers.setText(String.valueOf(dualMatchListDatumList.size()) + " matches available");


                        tourlist_rv.setAdapter(tourmatchListAdapter);

                        totaltours_tv.setText(tourDatumList.size() + " tournaments available");

                    } else {

                        swipreferesh.setRefreshing(false);

                        lyt_item.setVisibility(View.VISIBLE);
                        tourlist_rv.setVisibility(View.GONE);
//
                        totaltours_tv.setText("0 Matches available");


                    }
                    ll_filter.setVisibility(View.INVISIBLE);
                    hidden = true;

//                    dialogServerNotConnect();
                } else {
                    swipreferesh.setRefreshing(false);

                    lyt_item.setVisibility(View.VISIBLE);
                    tourlist_rv.setVisibility(View.GONE);
//
                    totaltours_tv.setText("0 Matches available");

                    ll_filter.setVisibility(View.INVISIBLE);
                    hidden = true;

//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<FetchTourMatchRespo> call, Throwable t) {

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
               return;

        }

        if (!hidden) {

            ll_filter.setVisibility(View.INVISIBLE);
            hidden = true;
            return;

        }

        super.onBackPressed();

//        if (hidden && issearchvisible) {
//
//            super.onBackPressed();
//        }


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




        mAdapter = new SelectedCityAdapter(this, citynamearrayList);

        selected_game_list_rv.setAdapter(mAdapter);


        selected_game_list_rv.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), selected_game_list_rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Citynamearray citynamearray = citynamearrayList.get(position);
//                Toast.makeText(getApplicationContext(), selectedGameModal.getCatId() + " is selected!", Toast.LENGTH_SHORT).show();


                cityname_tv.setText(citynamearray.getCityname());




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


    public void showErrorPopup(View v, String msg) {


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.validationpoplyt, null);


        TextView error_msg = popupView.findViewById(R.id.error_msg);


        Typeface typeface = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");


        error_msg.setTypeface(typeface);
        error_msg.setText(msg);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT

        );


        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {


            }
        });

        popupWindow.showAsDropDown(v);
    }

}
