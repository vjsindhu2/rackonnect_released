package com.sportskonnect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.sportskonnect.activities.AddMoreGame;
import com.sportskonnect.activities.MatchHistoryActivity;
import com.sportskonnect.adapters.MatchListHistoryAdapter;
import com.sportskonnect.adapters.UserRankAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.MatchListDualResponse;
import com.sportskonnect.api.Callbacks.SaveuserPrefRespo;
import com.sportskonnect.api.Callbacks.UserRankingResponse;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.DualMatchListDatum;
import com.sportskonnect.modal.UserRankDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_UserProfile extends AppCompatActivity {


    public static TextView tvusername;
    public static TextView tvAge;
    public static CircleImageView profile_image;
    public TextView add_game;
    ImageView update_profile;
    TextView tvRank, main_text_title;
    Typeface face1;
    Button age_cal;
    Button pref_gender_btn;
    Button anytypebtn;
    TextView score_tv, showall_tv;
    private String username = "";
    private String age = "";
    private String level = "";
    private TextView history_count_tv;
    private TextView tvLevel;
    private PopupWindow popupWindow;
    private PopupWindow agepopupWindow;
    private RecyclerView ranking_rv;
    private List<UserRankDatum> userRankDatumList = new ArrayList<>();
    private UserRankAdapter userRankAdapter;
    private String userid = "";
    private String minAge = "10";
    private String maxAge = "65";
    private String gender = "";
    private SwipeRefreshLayout refreshLayout;
    private String timePref = "";
    private PopupWindow timepopupWindow;
    private String profile_pic = "";
    private RecyclerView history_rv;
    private List<DualMatchListDatum> matchDatumList = new ArrayList<>();
    private View not_itm_lyt;
    private MatchListHistoryAdapter matchListHistoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);


        username = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_NAME, "");
        age = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.AGE, "");
        level = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_LEVEL, "");
        userid = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");
        profile_pic = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.PROFILE_PIC_URL, "");
        init();
    }

    private void init() {


        tvAge = (TextView) findViewById(R.id.tvAge);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        score_tv = (TextView) findViewById(R.id.score_tv);
        add_game = (TextView) findViewById(R.id.add_game);
        showall_tv = (TextView) findViewById(R.id.showall_tv);
        history_count_tv = (TextView) findViewById(R.id.history_count_tv);
        update_profile = (ImageView) findViewById(R.id.update_profile);
        pref_gender_btn = (Button) findViewById(R.id.pref_gender_btn);
        age_cal = (Button) findViewById(R.id.age_cal);
        anytypebtn = (Button) findViewById(R.id.anytymbtn);
        ranking_rv = (RecyclerView) findViewById(R.id.ranking_rv);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        not_itm_lyt = findViewById(R.id.not_itm_lyt);
        refreshLayout = findViewById(R.id.swipreferesh);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAlluserRanking(userid);
                fetchAllMatchesHistory(userid);
            }
        });


        try {

            if (SharedPreference.getSharedPreferenceString(this, SharedprefKeys.GENDER, "").equals("Male")) {
                Picasso.get().load(profile_pic).placeholder(R.drawable.male).into(profile_image);

            } else {
                Picasso.get().load(profile_pic).placeholder(R.drawable.female).into(profile_image);

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        age_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showagePopup(v);
            }
        });

        anytypebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showtimePopup(v);
            }
        });


        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference.setSharedPreferenceString(Activity_UserProfile.this, SharedprefKeys.PROFILE_COME_FROM, "fromUpdateProfile");

                Intent intent = new Intent(Activity_UserProfile.this, Activity_updateProfile.class);
                startActivity(intent);
            }
        });

        pref_gender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });


        showall_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_UserProfile.this, MatchHistoryActivity.class);
                startActivity(intent);
            }
        });
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Calibre Black.otf");

        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");

        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");

        Typeface typeface = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        tvusername = (TextView) findViewById(R.id.tvusername);
        tvRank = (TextView) findViewById(R.id.tvRank);


        main_text_title = (TextView) findViewById(R.id.main_text_title);
        history_rv = (RecyclerView) findViewById(R.id.history_rv);


//        tvBadmintn.setTypeface(face);


        tvusername.setTypeface(face2);


        tvRank.setTypeface(face2);

        add_game.setTypeface(face2);

        main_text_title.setTypeface(face1);


        tvusername.setText(username);
        tvAge.setText(age + " Y");
        tvLevel.setText(Constant.getGameLevelFromlevelId(level));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        ranking_rv.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        ranking_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        ranking_rv.setItemAnimator(new DefaultItemAnimator());

        fetchAlluserRanking(userid);

        gender = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_PREF_GENDER, "");

        if (!SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_PREF_MIN_AGE, "").equals(""))
        {
            minAge = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_PREF_MIN_AGE, "");

        }

        if (!SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_PREF_MAX_AGE, "").equals(""))
        {
            maxAge = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_PREF_MAX_AGE, "");

        }
        timePref = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_PREF_TIME, "");

        if (gender.equals("")) {
            pref_gender_btn.setText("All Player");

        } else {
            pref_gender_btn.setText(gender);


        }




        if (!minAge.equals("") && !maxAge.equals("")){
            age_cal.setText(minAge + "-" + maxAge);
        }else{

            age_cal.setText("10" + "-" + "65");
        }


        if (timePref.equals("")) {
            anytypebtn.setText("Anytime");

        } else {
            if (timePref.equals("1")) {
                anytypebtn.setText("Morning");

            } else if (timePref.equals("2")) {
                anytypebtn.setText("Afternoon");

            } else if (timePref.equals("3")) {
                anytypebtn.setText("Evening");

            }

        }


        RecyclerView.LayoutManager mLayoutManager_history = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        history_rv.setLayoutManager(mLayoutManager_history);

        // adding inbuilt divider line
        history_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        history_rv.setItemAnimator(new DefaultItemAnimator());

        add_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_UserProfile.this, AddMoreGame.class);

                startActivity(intent);
            }
        });


        fetchAllMatchesHistory(userid);


    }


    private void fetchAllMatchesHistory(String user_id) {

        refreshLayout.setRefreshing(true);
        Apis apis = RestAdapter.createAPI();
        Call<MatchListDualResponse> callbackCall = apis.getAllMatchesHistory(user_id);
        callbackCall.enqueue(new Callback<MatchListDualResponse>() {
            @Override
            public void onResponse(Call<MatchListDualResponse> call, Response<MatchListDualResponse> response) {


                MatchListDualResponse resp = response.body();
                Log.d("RESPOHiSTORY", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    refreshLayout.setRefreshing(false);
                    matchDatumList.clear();
                    for (int i = 0; i < resp.getData().size(); i++) {

                        DualMatchListDatum matchDatum = new DualMatchListDatum(
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
                        matchDatumList.add(matchDatum);


                    }

                    if (matchDatumList.size() == 1) {
                        ViewGroup.LayoutParams params = history_rv.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        history_rv.setLayoutParams(params);
                    }


                    if (matchDatumList.size() > 0) {

                        history_count_tv.setText("(" + matchDatumList.size() + ")");
                        not_itm_lyt.setVisibility(View.GONE);
                        history_rv.setVisibility(View.VISIBLE);
                        matchListHistoryAdapter = new MatchListHistoryAdapter(Activity_UserProfile.this, matchDatumList, 0);

                        history_rv.setAdapter(matchListHistoryAdapter);

                    } else {

                        history_count_tv.setText("(" + 0 + ")");
                        not_itm_lyt.setVisibility(View.VISIBLE);
                        history_rv.setVisibility(View.GONE);

                    }


//                    dialogServerNotConnect();
                } else {
                    refreshLayout.setRefreshing(false);

                    history_count_tv.setText("(" + 0 + ")");

                    not_itm_lyt.setVisibility(View.VISIBLE);
                    history_rv.setVisibility(View.GONE);
//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<MatchListDualResponse> call, Throwable t) {

                refreshLayout.setRefreshing(false);

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void fetchAlluserRanking(String user_id) {
        refreshLayout.setRefreshing(true);

        Apis apis = RestAdapter.createAPI();
        Call<UserRankingResponse> callbackCall = apis.getuserRanking(user_id);
        callbackCall.enqueue(new Callback<UserRankingResponse>() {
            @Override
            public void onResponse(Call<UserRankingResponse> call, Response<UserRankingResponse> response) {


                UserRankingResponse resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    refreshLayout.setRefreshing(false);

                    userRankDatumList.clear();
                    if (!resp.getScoretotal().equals("")) {
                        score_tv.setText(resp.getScoretotal());
                    }
                    for (int i = 0; i < resp.getData().size(); i++) {

                        UserRankDatum userRankDatum = new UserRankDatum(
                                resp.getData().get(i).getCatname(),
                                resp.getData().get(i).getCatid(),
                                resp.getData().get(i).getGames(),
                                resp.getData().get(i).getLoss(),
                                resp.getData().get(i).getWin(),
                                resp.getData().get(i).getPer(),
                                resp.getData().get(i).getScore()

                        );
                        userRankDatumList.add(userRankDatum);


                    }

                    if (userRankDatumList.size() > 0) {

                        userRankAdapter = new UserRankAdapter(Activity_UserProfile.this, userRankDatumList, 0);

                        ranking_rv.setAdapter(userRankAdapter);

                    }


//                    dialogServerNotConnect();
                } else {
                    refreshLayout.setRefreshing(false);


//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<UserRankingResponse> call, Throwable t) {
                refreshLayout.setRefreshing(false);

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    public void finishuserProfile(View view) {
        Intent intent = new Intent(Activity_UserProfile.this, Activity_Badminton.class);
        startActivity(intent);
        finishAffinity();
    }

    public void showPopup(View v) {


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.genderprefdialog, null);

        CardView bothgender_cv = popupView.findViewById(R.id.bothgender_cv);
        CardView male_cv = popupView.findViewById(R.id.male_cv);
        CardView female_cv = popupView.findViewById(R.id.female_cv);
        TextView lbl_allplayer = popupView.findViewById(R.id.lbl_allplayer);
        TextView lbl_tvMale = popupView.findViewById(R.id.lbl_tvMale);
        TextView lbl_tvFemale = popupView.findViewById(R.id.lbl_tvFemale);


        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (gender.equals("Male")) {
            male_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            female_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            bothgender_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvMale.setTextColor(getResources().getColor(R.color.color_white));
            lbl_tvFemale.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_allplayer.setTextColor(getResources().getColor(R.color.colorPrimary));
            pref_gender_btn.setText("Male");
        } else if (gender.equals("Female")) {
            female_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            bothgender_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            male_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvMale.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvFemale.setTextColor(getResources().getColor(R.color.color_white));
            lbl_allplayer.setTextColor(getResources().getColor(R.color.colorPrimary));
            pref_gender_btn.setText("Female");
        } else {

            bothgender_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            male_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            female_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvMale.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvFemale.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_allplayer.setTextColor(getResources().getColor(R.color.color_white));

            pref_gender_btn.setText("All Player");

        }


        bothgender_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "";

                bothgender_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                male_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                female_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMale.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvFemale.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_allplayer.setTextColor(getResources().getColor(R.color.color_white));

                pref_gender_btn.setText("All Player");

                saveUserPrefrence(userid, timePref, minAge, maxAge, gender);
                popupWindow.dismiss();


            }
        });

        male_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Male";

                male_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                female_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                bothgender_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMale.setTextColor(getResources().getColor(R.color.color_white));
                lbl_tvFemale.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_allplayer.setTextColor(getResources().getColor(R.color.colorPrimary));
                pref_gender_btn.setText("Male");

                saveUserPrefrence(userid, timePref, minAge, maxAge, gender);
                popupWindow.dismiss();

            }
        });

        female_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Female";

                female_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                bothgender_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                male_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMale.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvFemale.setTextColor(getResources().getColor(R.color.color_white));
                lbl_allplayer.setTextColor(getResources().getColor(R.color.colorPrimary));
                pref_gender_btn.setText("Female");

                saveUserPrefrence(userid, timePref, minAge, maxAge, gender);

                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {


            }
        });

        popupWindow.showAsDropDown(v);
    }


    public void showagePopup(View v) {


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.prefagedialog, null);


        CrystalRangeSeekbar ageSeekbar = popupView.findViewById(R.id.ageSeekbar);
        TextView age_minval = popupView.findViewById(R.id.age_minval);
        TextView range_lbl = popupView.findViewById(R.id.range_lbl);
        TextView age_maxval = popupView.findViewById(R.id.age_maxval);

        String selectedminage = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_PREF_MIN_AGE, "10");
        String selectedmaxage = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_PREF_MAX_AGE, "65");

        agepopupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        ageSeekbar.setMinStartValue(Integer.parseInt(selectedminage)).apply();
        ageSeekbar.setMaxStartValue(Integer.parseInt(selectedmaxage)).apply();

        ageSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {


                age_minval.setText("" + minValue + " YEARS");
                range_lbl.setText("" + minValue + " - " + maxValue);
                age_maxval.setText("" + maxValue + " YEARS");

                age_cal.setText(minValue + "-" + maxValue);

                saveUserPrefrence(userid, timePref, minAge, maxAge, gender);

            }
        });

// set final value listener
        ageSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
//                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));

                minAge = String.valueOf(minValue);
                maxAge = String.valueOf(maxValue);

                saveUserPrefrence(userid, timePref, minAge, maxAge, gender);

            }
        });

        agepopupWindow.setBackgroundDrawable(new BitmapDrawable());
        agepopupWindow.setOutsideTouchable(true);
        agepopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss


            }
        });

        agepopupWindow.showAsDropDown(v);
    }


    public void showtimePopup(View v) {


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.timeprefdialog, null);


        CardView anytym_cv = popupView.findViewById(R.id.anytym_cv);
        CardView morning_cv = popupView.findViewById(R.id.morning_cv);
        CardView afternoon_cv = popupView.findViewById(R.id.afternoon_cv);
        CardView evening_cv = popupView.findViewById(R.id.evening_cv);

        TextView lbl_anytym = popupView.findViewById(R.id.lbl_anytym);
        TextView lbl_tvMorning = popupView.findViewById(R.id.lbl_tvMorning);
        TextView lbl_tvAfternoon = popupView.findViewById(R.id.lbl_tvAfternoon);
        TextView lbl_tvevening = popupView.findViewById(R.id.lbl_tvevening);

        timepopupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        if (timePref.equals("1")) {
            morning_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvMorning.setTextColor(getResources().getColor(R.color.color_white));
            lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));

            anytypebtn.setText("Morning");
        } else if (timePref.equals("2")) {
            afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.color_white));
            lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));

            anytypebtn.setText("After Noon");
        } else if (timePref.equals("3")) {
            evening_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvevening.setTextColor(getResources().getColor(R.color.color_white));
            lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));


            anytypebtn.setText("Evening");
        } else {
            anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_anytym.setTextColor(getResources().getColor(R.color.color_white));

            anytypebtn.setText("Anytime");
        }


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

                anytypebtn.setText("Anytime");

                saveUserPrefrence(userid, timePref, minAge, maxAge, gender);
                timepopupWindow.dismiss();

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

                anytypebtn.setText("Morning");

                saveUserPrefrence(userid, timePref, minAge, maxAge, gender);
                timepopupWindow.dismiss();

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

                anytypebtn.setText("After Noon");

                saveUserPrefrence(userid, timePref, minAge, maxAge, gender);
                timepopupWindow.dismiss();

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


                anytypebtn.setText("Evening");

                saveUserPrefrence(userid, timePref, minAge, maxAge, gender);

                timepopupWindow.dismiss();
            }
        });


        timepopupWindow.setBackgroundDrawable(new BitmapDrawable());
        timepopupWindow.setOutsideTouchable(true);
        timepopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss


            }
        });

        timepopupWindow.showAsDropDown(v);
    }


    private void saveUserPrefrence(String user_id, String prtime, String pmin_age, String pmax_age, String pgender) {

        refreshLayout.setRefreshing(true);
        Apis apis = RestAdapter.createAPI();
        Call<SaveuserPrefRespo> callbackCall = apis.saveUserpreference(user_id, prtime, pgender, pmin_age, pmax_age);
        callbackCall.enqueue(new Callback<SaveuserPrefRespo>() {
            @Override
            public void onResponse(Call<SaveuserPrefRespo> call, Response<SaveuserPrefRespo> response) {


                SaveuserPrefRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {
                    refreshLayout.setRefreshing(false);


                    SharedPreference.setSharedPreferenceString(Activity_UserProfile.this, SharedprefKeys.USER_PREF_GENDER, gender);
                    SharedPreference.setSharedPreferenceString(Activity_UserProfile.this, SharedprefKeys.USER_PREF_MIN_AGE, minAge);
                    SharedPreference.setSharedPreferenceString(Activity_UserProfile.this, SharedprefKeys.USER_PREF_MAX_AGE, maxAge);
                    SharedPreference.setSharedPreferenceString(Activity_UserProfile.this, SharedprefKeys.USER_PREF_MAX_AGE, maxAge);
                    SharedPreference.setSharedPreferenceString(Activity_UserProfile.this, SharedprefKeys.USER_PREF_TIME, timePref);


//                    dialogServerNotConnect();
                } else {
                    refreshLayout.setRefreshing(false);


//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<SaveuserPrefRespo> call, Throwable t) {
                refreshLayout.setRefreshing(false);

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    public void redirectto_settingActivity(View view) {
        Intent intent = new Intent(Activity_UserProfile.this, Activity_Setting.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Activity_UserProfile.this, Activity_Badminton.class);
        startActivity(intent);
        finishAffinity();
    }
}
