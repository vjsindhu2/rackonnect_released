package com.sportskonnect.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sportskonnect.Activity_MatchScoringReq;
import com.sportskonnect.Activity_StartScoring;
import com.sportskonnect.R;
import com.sportskonnect.adapters.MatchListAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.MatchListDualResponse;
import com.sportskonnect.api.Callbacks.MatchListResponse;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.DualMatchListDatum;
import com.sportskonnect.modal.MatchDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.sportskonnect.utility.RecyclerTouchListener;

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

public class ScoringRequestsActivity extends AppCompatActivity {

    private RecyclerView matches_list_rv;
    private List<DualMatchListDatum> matchDatumList = new ArrayList();
    private MatchListAdapter matchListAdapter;
    private SharedPreference sharedPreference;
    private View not_itm_lyt;

    @SuppressLint("StaticFieldLeak")
    public static SwipeRefreshLayout swipreferesh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring_requests);


        sharedPreference = new SharedPreference(this);
        matches_list_rv = findViewById(R.id.matches_list_rv);
        not_itm_lyt = findViewById(R.id.not_itm_lyt);
        swipreferesh = findViewById(R.id.swipreferesh);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        matches_list_rv.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        matches_list_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        matches_list_rv.setItemAnimator(new DefaultItemAnimator());

        fetchAllMatchesRequests(sharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, ""));


        swipreferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAllMatchesRequests(sharedPreference.getSharedPreferenceString(ScoringRequestsActivity.this, SharedprefKeys.USER_ID, ""));

            }
        });

        matches_list_rv.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), matches_list_rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                DualMatchListDatum matchDatum = matchDatumList.get(position);


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void finishactivity(View view) {
        finish();
    }

    private void fetchAllMatchesRequests(String user_id) {

        swipreferesh.setRefreshing(true);
        Apis apis = RestAdapter.createAPI();
        Call<MatchListDualResponse> callbackCall = apis.getAllMatchesRequests(user_id);
        callbackCall.enqueue(new Callback<MatchListDualResponse>() {
            @Override
            public void onResponse(Call<MatchListDualResponse> call, Response<MatchListDualResponse> response) {


                MatchListDualResponse resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    swipreferesh.setRefreshing(false);
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

                    if (matchDatumList.size() > 0) {
                        not_itm_lyt.setVisibility(View.GONE);
                        matches_list_rv.setVisibility(View.VISIBLE);
                        matchListAdapter = new MatchListAdapter(ScoringRequestsActivity.this, matchDatumList, 1);

                        matches_list_rv.setAdapter(matchListAdapter);

                    } else {
                        not_itm_lyt.setVisibility(View.VISIBLE);
                        matches_list_rv.setVisibility(View.GONE);
                    }


//                    dialogServerNotConnect();
                } else {

                    swipreferesh.setRefreshing(false);
                    not_itm_lyt.setVisibility(View.VISIBLE);
                    matches_list_rv.setVisibility(View.GONE);
//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<MatchListDualResponse> call, Throwable t) {
                swipreferesh.setRefreshing(false);
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

}
