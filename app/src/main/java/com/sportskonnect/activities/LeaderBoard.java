package com.sportskonnect.activities;

import android.annotation.SuppressLint;
import android.app.Notification;

import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sportskonnect.R;
import com.sportskonnect.adapters.LeaderBoardRvAdapter;
import com.sportskonnect.adapters.MatchListAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.GlobalRankingRespo;
import com.sportskonnect.api.Callbacks.MatchListDualResponse;
import com.sportskonnect.api.Callbacks.UserRankingRespo;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.DualMatchListDatum;
import com.sportskonnect.modal.GlobalRankDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.sportskonnect.utility.RecyclerTouchListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderBoard extends AppCompatActivity {

    RecyclerView leaderboard_list_rv;
    private List<GlobalRankDatum> globalRankDatumList = new ArrayList<>();
    private View not_itm_lyt;
    private LeaderBoardRvAdapter leaderboardRvAdapter;

    TextView tv_totalgames,tv_total_win,tv_total_lose,game_percent,scoreing_tv;
    private ImageView user_img;
    private String gender = "";
    private String user_img_str = "";
    public static ImageView ivP;
    private String user_id = "";
    private TextView user_name_tv,level_tv;

    public static TextView tvRate;
    private String level="";
    private String user_name="";
    SwipeRefreshLayout swipreferesh;
    SeekBar seekBar_luminosite;
    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        init();
    }


    public void init() {

        leaderboard_list_rv = findViewById(R.id.leaderboard_list_rv);
        not_itm_lyt = findViewById(R.id.not_itm_lyt);
        user_img = findViewById(R.id.user_img);
        user_name_tv = findViewById(R.id.user_name_tv);
        level_tv = findViewById(R.id.level_tv);
        tv_totalgames = findViewById(R.id.tv_totalgames);
        tv_total_win = findViewById(R.id.tv_total_win);
        tv_total_lose = findViewById(R.id.tv_total_lose);
        game_percent = findViewById(R.id.game_percent);
        scoreing_tv = findViewById(R.id.scoreing_tv);
        msg = findViewById(R.id.msg);
        ivP = findViewById(R.id.ivP);
        swipreferesh = findViewById(R.id.swipreferesh);
        tvRate = findViewById(R.id.tvRate);
        seekBar_luminosite = findViewById(R.id.seekBar_luminosite);

        gender = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.GENDER, "");
        user_img_str = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.PROFILE_PIC_URL, "");
        user_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");
        level = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_LEVEL, "");
        user_name = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_NAME, "");


        level_tv.setText(Constant.getGameLevelFromlevelId(level));
        user_name_tv.setText(user_name);
        try {
            if (gender.equals("Male")) {

                Picasso.get().load(user_img_str).placeholder(R.drawable.male).into(user_img);
            }else {
                Picasso.get().load(user_img_str).placeholder(R.drawable.female).into(user_img);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        leaderboard_list_rv.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
//        leaderboard_list_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        leaderboard_list_rv.setItemAnimator(new DefaultItemAnimator());

        leaderboard_list_rv.setNestedScrollingEnabled(true);
        fetchAllRankData();
        getuserrank(user_id,"");


        seekBar_luminosite.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        swipreferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAllRankData();
                getuserrank(user_id,"");
            }
        });

    }


    private void fetchAllRankData() {

        swipreferesh.setRefreshing(true);
        Apis apis = RestAdapter.createAPI();
        Call<GlobalRankingRespo> callbackCall = apis.globalranking(SharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_SELECTED_CAT,""));
        callbackCall.enqueue(new Callback<GlobalRankingRespo>() {
            @Override
            public void onResponse(Call<GlobalRankingRespo> call, Response<GlobalRankingRespo> response) {


                GlobalRankingRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {
                    swipreferesh.setRefreshing(false);

                    globalRankDatumList.clear();
                    for (int i = 0; i < resp.getData().size(); i++) {

                        GlobalRankDatum globalRankDatum = new GlobalRankDatum(
                                resp.getData().get(i).getName(),
                                resp.getData().get(i).getProfileimage(),
                                resp.getData().get(i).getGames(),
                                resp.getData().get(i).getLoss(),
                                resp.getData().get(i).getWin(),
                                resp.getData().get(i).getPer(),
                                resp.getData().get(i).getScoretotal(),
                                resp.getData().get(i).getTurnup(),
                                resp.getData().get(i).getLevel(),
                                resp.getData().get(i).getUserid(),
                                resp.getData().get(i).getGender_opp()
                        );
                        globalRankDatumList.add(globalRankDatum);


                    }

                    if (globalRankDatumList.size() > 0) {

                        not_itm_lyt.setVisibility(View.GONE);
                        leaderboard_list_rv.setVisibility(View.VISIBLE);
                        leaderboardRvAdapter = new LeaderBoardRvAdapter(LeaderBoard.this, globalRankDatumList);

                        leaderboard_list_rv.setAdapter(leaderboardRvAdapter);

                    } else {
                        not_itm_lyt.setVisibility(View.VISIBLE);
                        leaderboard_list_rv.setVisibility(View.GONE);

                    }


//                    dialogServerNotConnect();
                } else {
                    swipreferesh.setRefreshing(false);

                    not_itm_lyt.setVisibility(View.VISIBLE);
                    leaderboard_list_rv.setVisibility(View.GONE);
//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<GlobalRankingRespo> call, Throwable t) {

                swipreferesh.setRefreshing(false);

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

    private void getuserrank(String userid,String cat_id) {
        swipreferesh.setRefreshing(true);

        Apis apis = RestAdapter.createAPI();
        Call<UserRankingRespo> callbackCall = apis.getuserrank(userid,SharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_SELECTED_CAT,""));
        callbackCall.enqueue(new Callback<UserRankingRespo>() {
            @Override
            public void onResponse(Call<UserRankingRespo> call, Response<UserRankingRespo> response) {


                UserRankingRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));
                if (resp != null && !resp.getError()) {
                    swipreferesh.setRefreshing(false);

                  tv_totalgames.setText(resp.getGames()+"");
                    tv_total_win.setText(resp.getWin()+"");
                    tv_total_lose.setText(resp.getLoss()+"");
                    game_percent.setText(resp.getPer()+"%");
                    scoreing_tv.setText(resp.getScoretotal()+"");


                    if (!resp.getMsg().equals("")){
                        msg.setText(resp.getMsg());

                        msg.setVisibility(View.VISIBLE);

                        tvRate.setVisibility(View.INVISIBLE);

                    }else {
                        msg.setVisibility(View.GONE);
                        tvRate.setVisibility(View.VISIBLE);

                    }

                    seekBar_luminosite.setProgress(resp.getPer());



//                    dialogServerNotConnect();
                } else {
                    swipreferesh.setRefreshing(false);

                    not_itm_lyt.setVisibility(View.VISIBLE);
                    leaderboard_list_rv.setVisibility(View.GONE);
//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<UserRankingRespo> call, Throwable t) {
                swipreferesh.setRefreshing(false);

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    public void finishleaerborad(View view){
        finish();
    }


}
