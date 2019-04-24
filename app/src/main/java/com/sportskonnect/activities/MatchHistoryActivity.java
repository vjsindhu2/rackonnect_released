package com.sportskonnect.activities;

import android.app.Notification;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.sportskonnect.Activity_UserProfile;
import com.sportskonnect.R;
import com.sportskonnect.adapters.MatchListHistoryAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.MatchListDualResponse;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.DualMatchListDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchHistoryActivity extends AppCompatActivity {


    RecyclerView history_matches_list_rv;
    SwipeRefreshLayout swipreferesh;
    private View not_itm_lyt;
    private List<DualMatchListDatum> matchDatumList = new ArrayList<>();
    private MatchListHistoryAdapter matchListHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);
        init();

    }



    public void init(){
        history_matches_list_rv = (RecyclerView) findViewById(R.id.history_matches_list_rv);
        not_itm_lyt =  findViewById(R.id.not_itm_lyt);
        swipreferesh =  findViewById(R.id.swipreferesh);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        history_matches_list_rv.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        history_matches_list_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        history_matches_list_rv.setItemAnimator(new DefaultItemAnimator());

        fetchAllMatchesHistory(SharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_ID,""));



        swipreferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAllMatchesHistory(SharedPreference.getSharedPreferenceString(MatchHistoryActivity.this,SharedprefKeys.USER_ID,""));

            }
        });


//        history_matches_list_rv.setClickable(false);

    }


    public void finishhistoryactivity(View view) {
        this.finish();
    }


    private void fetchAllMatchesHistory(String user_id) {

        swipreferesh.setRefreshing(true);
        Apis apis = RestAdapter.createAPI();
        Call<MatchListDualResponse> callbackCall = apis.getAllMatchesHistory(user_id);
        callbackCall.enqueue(new Callback<MatchListDualResponse>() {
            @Override
            public void onResponse(Call<MatchListDualResponse> call, Response<MatchListDualResponse> response) {


                MatchListDualResponse resp = response.body();
                Log.d("RESPOHiSTORY", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {
                    matchDatumList.clear();

                    swipreferesh.setRefreshing(false);
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

                    if (matchDatumList.size() > 0) {

                        not_itm_lyt.setVisibility(View.GONE);
                        history_matches_list_rv.setVisibility(View.VISIBLE);
                        matchListHistoryAdapter = new MatchListHistoryAdapter(MatchHistoryActivity.this, matchDatumList,1);

                        history_matches_list_rv.setAdapter(matchListHistoryAdapter);

                    }else {

                        not_itm_lyt.setVisibility(View.VISIBLE);
                        history_matches_list_rv.setVisibility(View.GONE);

                    }


//                    dialogServerNotConnect();
                } else {

                    swipreferesh.setRefreshing(false);


                    not_itm_lyt.setVisibility(View.VISIBLE);
                    history_matches_list_rv.setVisibility(View.GONE);
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


}
