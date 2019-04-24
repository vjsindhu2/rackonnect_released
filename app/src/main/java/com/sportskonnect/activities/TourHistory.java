package com.sportskonnect.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.sportskonnect.Activity_TourList;
import com.sportskonnect.R;
import com.sportskonnect.adapters.TourMatchHistoryAdapter;
import com.sportskonnect.adapters.TourMatchListAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.Citynamearray;
import com.sportskonnect.api.Callbacks.FetchSavedLocationRespo;
import com.sportskonnect.api.Callbacks.FetchTourMatchRespo;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.TourDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import java.util.ArrayList;
import java.util.List;

public class TourHistory extends AppCompatActivity {

    RecyclerView tourhistory_rv;
    private String user_id="";
    private String selected_cat;
    private TourMatchHistoryAdapter tourmatchListAdapter;
    private SwipeRefreshLayout swipreferesh;
    private List<TourDatum> tourDatumList = new ArrayList<>();
    private View lyt_item;
    private String current_latitude="";
    private String current_longetude="";
    private String current_radius="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_history);

        tourhistory_rv = findViewById(R.id.tourhistory_rv);
        swipreferesh = findViewById(R.id.swipreferesh);
        lyt_item = findViewById(R.id.lyt_item);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        tourhistory_rv.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        tourhistory_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        tourhistory_rv.setItemAnimator(new DefaultItemAnimator());

        user_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");
        selected_cat = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "0");

        tourmatchListAdapter = new TourMatchHistoryAdapter(TourHistory.this, tourDatumList);



        swipreferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchlocationfromdb(user_id);

            }
        });

        fetchlocationfromdb(user_id);
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


                    current_latitude = resp.getLat();

                    current_longetude = resp.getLng();
                    current_radius = resp.getRadius();



                    fetchTourHistorylist(user_id, current_latitude, current_longetude, current_radius, String.valueOf(selected_cat));

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


    private void fetchTourHistorylist(String user_id, String current_latitude, String current_longetude, String current_radius, String cat_id) {

        swipreferesh.setRefreshing(true);

        Apis apis = RestAdapter.createAPI();
        Call<FetchTourMatchRespo> callbackCall = apis.getAllTourHistory(cat_id, user_id, current_latitude, current_longetude, current_radius);
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
                        tourhistory_rv.setVisibility(View.VISIBLE);

//                        tvAvaPlayers.setText(String.valueOf(dualMatchListDatumList.size()) + " matches available");


                        tourhistory_rv.setAdapter(tourmatchListAdapter);


                    } else {

                        swipreferesh.setRefreshing(false);

                        lyt_item.setVisibility(View.VISIBLE);
                        tourhistory_rv.setVisibility(View.GONE);
//


                    }


//                    dialogServerNotConnect();
                } else {
                    swipreferesh.setRefreshing(false);

                    lyt_item.setVisibility(View.VISIBLE);
                    tourhistory_rv.setVisibility(View.GONE);
//


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


    public void finishTourHistoryList(View view){
        finish();
    }
}
