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

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.sportskonnect.R;
import com.sportskonnect.adapters.ChatListHistoryAdapter;
import com.sportskonnect.adapters.MatchListHistoryAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.ChatListHistResponse;
import com.sportskonnect.api.Callbacks.MatchListDualResponse;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.ChatHistDatum;
import com.sportskonnect.modal.DualMatchListDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import java.util.ArrayList;
import java.util.List;

public class ChatHistoryActivity extends AppCompatActivity {


    RecyclerView history_matches_list_rv;
    SwipeRefreshLayout swipreferesh;
    private View not_itm_lyt;
    private List<ChatHistDatum> matchDatumList = new ArrayList<>();
    private ChatListHistoryAdapter matchListHistoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);
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

        fetchAllChatHistory(SharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_ID,""));



        swipreferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAllChatHistory(SharedPreference.getSharedPreferenceString(ChatHistoryActivity.this,SharedprefKeys.USER_ID,""));

            }
        });


//        history_matches_list_rv.setClickable(false);

    }



    public void finishchathistoryactivity(View view){
        finish();

    }

    private void fetchAllChatHistory(String user_id) {

        swipreferesh.setRefreshing(true);
        Apis apis = RestAdapter.createAPI();
        Call<ChatListHistResponse> callbackCall = apis.getAllchatlistHistory(user_id,SharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_SELECTED_CAT,""));
        callbackCall.enqueue(new Callback<ChatListHistResponse>() {
            @Override
            public void onResponse(Call<ChatListHistResponse> call, Response<ChatListHistResponse> response) {


                ChatListHistResponse resp = response.body();
                Log.d("RESPOHiSTORY", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {
                    matchDatumList.clear();

                    swipreferesh.setRefreshing(false);
                    for (int i = 0; i < resp.getData().size(); i++) {

                        ChatHistDatum matchDatum = new ChatHistDatum(
                                resp.getData().get(i).getUserid(),
                                resp.getData().get(i).getName(),
                                resp.getData().get(i).getGender(),

                                resp.getData().get(i).getImage(),
                                resp.getData().get(i).getTime(),
                                resp.getData().get(i).getLastmsg(),
                                resp.getData().get(i).getLevelid()

                        );
                        matchDatumList.add(matchDatum);



                    }

                    if (matchDatumList.size() > 0) {

                        not_itm_lyt.setVisibility(View.GONE);
                        history_matches_list_rv.setVisibility(View.VISIBLE);
                        matchListHistoryAdapter = new ChatListHistoryAdapter(ChatHistoryActivity.this, matchDatumList,1);

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
            public void onFailure(Call<ChatListHistResponse> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

}
