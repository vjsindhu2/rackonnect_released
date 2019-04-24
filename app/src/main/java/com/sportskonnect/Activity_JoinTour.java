package com.sportskonnect;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.github.jorgecastillo.FillableLoader;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.sportskonnect.adapters.JoinedOpponentsAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.CheckTourTimeRespo;
import com.sportskonnect.api.Callbacks.EndtourRespo;
import com.sportskonnect.api.Callbacks.JoinDualMatchResponse;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.JSONParser;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.TourDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;

public class Activity_JoinTour extends AppCompatActivity implements View.OnClickListener, PaytmPaymentTransactionCallback {

    Button btnSubmit;
    Typeface face;

    List<TourDatum> tourDatumList = new ArrayList<>();
    TextView game_name_n_players_tv, main_text_title, first_prize_tv, second_prize_tv, tv_host_name, tv_host_level, dateNtime_tv, playareanaAddress_TV, regfee_vt, payer_outofPlayer_Tv, remainplayer_tv;
    CircleImageView host_img;
    RecyclerView joindedopponents_rv;
    private int tourdatumposition = -1;
    private JoinedOpponentsAdapter joinedOppAdapter;
    private TourDatum tourDatum;
    private Dialog loader_dialog;

    private TextView btnreq_tv;

    private LinearLayout winners_ll;

    private String orderId = "";
    private String custId = "";
    private String mid = "TGMznm39416586320966";
    private int joinedflag = -1;
    private int endgameflag = -1;

    private Button end_tour;
    private CircleImageView winner_img, runner_img;
    private TextView winner_name_tv, runner_name_tv, winner_amt_tv, runner_amt_tv;
    private String gender = "";

    private TextView tvOver;
    private boolean iswinnerselected = true;
    private boolean isrunnerSelected = false;
    private String selectedwinner = "";
    private String selectedRunner = "";
    private TextView tvTime;

    private String serverOrderId="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jointour);

        tourDatumList = SharedPreference.getArrayList(this, SharedprefKeys.CURRENT_TOUR_DATUM);
        tourdatumposition = SharedPreference.getSharedPreferenceInt(this, SharedprefKeys.CURRENT_TOUR_POSITION, -1);
//        Log.d("DATUM", tourDatumList.get(tourdatumposition).getMatchname());
        init();
    }

    private void init() {

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        end_tour = (Button) findViewById(R.id.end_tour);
        game_name_n_players_tv = (TextView) findViewById(R.id.game_name_n_players_tv);
        main_text_title = (TextView) findViewById(R.id.main_text_title);
        first_prize_tv = (TextView) findViewById(R.id.first_prize_tv);
        second_prize_tv = (TextView) findViewById(R.id.second_prize_tv);
        regfee_vt = (TextView) findViewById(R.id.regfee_vt);
        tvTime = (TextView) findViewById(R.id.tvTime);
        winners_ll = (LinearLayout) findViewById(R.id.winners_ll);
        payer_outofPlayer_Tv = (TextView) findViewById(R.id.payer_outofPlayer_Tv);
        regfee_vt = (TextView) findViewById(R.id.regfee_vt);
        btnreq_tv = (TextView) findViewById(R.id.btnreq_tv);
        winner_name_tv = (TextView) findViewById(R.id.winner_name_tv);
        runner_name_tv = (TextView) findViewById(R.id.runner_name_tv);
        remainplayer_tv = (TextView) findViewById(R.id.remainplayer_tv);
        winner_amt_tv = (TextView) findViewById(R.id.winner_amt_tv);
        runner_amt_tv = (TextView) findViewById(R.id.runner_amt_tv);
        tvOver = (TextView) findViewById(R.id.tvOver);
        winner_img = findViewById(R.id.winner_img);
        runner_img = findViewById(R.id.runner_img);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/Calibre Bold.otf");

        btnSubmit.setTypeface(face);
        end_tour.setTypeface(face);

        tv_host_name = findViewById(R.id.tv_host_name);
        tv_host_level = findViewById(R.id.tv_host_level);
        host_img = findViewById(R.id.host_img);
        dateNtime_tv = findViewById(R.id.dateNtime_tv);
        joindedopponents_rv = findViewById(R.id.joindedopponents_rv);
        playareanaAddress_TV = findViewById(R.id.playareanaAddress_TV);


        gender = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.GENDER, "");

        custId = "customer" + getRandomNumber(5, 100) + SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");
        orderId = "order" + getRandomNumber(5, 100) + SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");

        serverOrderId = orderId;

        btnSubmit.setOnClickListener(this);


        tourDatum = tourDatumList.get(tourdatumposition);


        if (!tourDatum.getMatchstatus().equals("")) {
            tvOver.setVisibility(View.VISIBLE);

            tvOver.setText(tourDatum.getMatchstatus());

        } else {
            tvOver.setVisibility(View.INVISIBLE);
        }


        main_text_title.setText(tourDatum.getMatchname());
        game_name_n_players_tv.setText(Constant.getGameNameFromcatId(tourDatum.getCatid()) + " // " + tourDatum.getGroupMember() + " Players");

        first_prize_tv.setText("Rs. " + tourDatum.getWinnerAmount());
        second_prize_tv.setText("Rs. " + tourDatum.getRunnerupAmount());

        tv_host_name.setText(tourDatum.getMidname());
        tv_host_level.setText(Constant.getGameLevelFromlevelId(tourDatum.getMidlevel()));

        try{

            if (tourDatum.getGender().equals("Male")){
                Picasso.get().load(tourDatum.getMidimage()).placeholder(R.drawable.male).into(host_img);

            }else {
                Picasso.get().load(tourDatum.getMidimage()).placeholder(R.drawable.female).into(host_img);

            }


        }catch (Exception ex){ex.printStackTrace();}

        playareanaAddress_TV.setText(tourDatum.getAddress());


        dateNtime_tv.setText(tourDatum.getDate() + " | " + convet24hourTo12hour(tourDatum.getTime()) + " ONWARDS");

        regfee_vt.setText("Rs. " + tourDatum.getAmount());

        if (tourDatum.getFidarray().size() > 0) {

            payer_outofPlayer_Tv.setText(tourDatum.getFidarray().size() + " JOINED/" + tourDatum.getGroupMember());

            remainplayer_tv.setText(Integer.parseInt(tourDatum.getGroupMember()) - tourDatum.getFidarray().size() + " NEEDED FOR TOURNAMENT");

        }

        if (tourDatum.getWinnerarray().size() > 0) {
            winners_ll.setVisibility(View.VISIBLE);


            for (int i = 0; i < tourDatum.getWinnerarray().size(); i++) {

                try {


                    if (tourDatum.getWinnerarray().get(i).getType().equals("win")) {
                        if (tourDatum.getWinnerarray().get(i).getGender().equals("Male")) {


                            Picasso.get().load(tourDatum.getWinnerarray().get(i).getFidimage()).placeholder(R.drawable.male).into(winner_img);

                        } else {
                            Picasso.get().load(tourDatum.getWinnerarray().get(i).getFidimage()).placeholder(R.drawable.female).into(winner_img);

                        }

                        winner_name_tv.setText(tourDatum.getWinnerarray().get(i).getFidname());
                        winner_amt_tv.setText(tourDatum.getWinnerAmount());


                    } else {
                        if (tourDatum.getWinnerarray().get(i).getGender().equals("Male")) {


                            Picasso.get().load(tourDatum.getWinnerarray().get(i).getFidimage()).placeholder(R.drawable.male).into(runner_img);

                        } else {
                            Picasso.get().load(tourDatum.getWinnerarray().get(i).getFidimage()).placeholder(R.drawable.female).into(runner_img);

                        }

                        runner_name_tv.setText(tourDatum.getWinnerarray().get(i).getFidname());


                        runner_amt_tv.setText(tourDatum.getRunnerupAmount());


                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }


        } else {
            winners_ll.setVisibility(View.GONE);

        }


        joindedopponents_rv.setLayoutManager(new GridLayoutManager(this, 7));
        // adding inbuilt divider line

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        joindedopponents_rv.setItemAnimator(new DefaultItemAnimator());

        joinedOppAdapter = new JoinedOpponentsAdapter(Activity_JoinTour.this, tourDatum.getFidarray(), tourDatum.getWinnerAmount(), 0, 0, tourDatum.getRunnerupAmount());

        joindedopponents_rv.setAdapter(joinedOppAdapter);


        try {
            checkvalidatetime(tourDatum.getMatchid(), tourDatum.getTime(), formatdateReturnDate(tourDatum.getDate()), tourDatum.getGroupMember(), tourDatum.getValidatetime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

       /* for (int i = 0; i < tourDatum.getFidarray().size(); i++) {
            if (SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "").equals(tourDatum.getFidarray().get(i).getFid())) {

                btnSubmit.setVisibility(View.GONE);

//                if (SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "").equals(tourDatum.getMid())) {
//                    btnreq_tv.setText("Tournament Joined...");
//                }
                btnreq_tv.setVisibility(View.VISIBLE);

            } else {


//                btnreq_tv.setVisibility(View.GONE);

            }

        }*/


        end_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (tourDatum.getGroupMember().equals(tourDatum.getFidarray().size())){


                if (SharedPreference.getSharedPreferenceString(Activity_JoinTour.this, SharedprefKeys.USER_ID, "").equals(tourDatum.getMid())) {
                    if (iswinnerselected) {
                        winnerSlectionDialog();
                        isrunnerSelected = true;
                        iswinnerselected = false;

                    } else if (isrunnerSelected) {

                        runnerSlectionDialog();
                        iswinnerselected = true;
                        isrunnerSelected = false;

                    }


                } else {

                    verifyScoreConfirmationDialog();
                }


//                }

            }
        });

    }


    public String formatdateReturnDate(String date_str) throws ParseException {


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

        Date date = new Date(sdf.parse(date_str).getTime());
        date.setMonth(date.getMonth());

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String convertedate = sdf2.format(date);

        return convertedate;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

                confirmdialog();
                break;
        }
    }


    private void startTimer(int noOfMinutes) {
        CountDownTimer countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;

                StringBuilder countdownText = new StringBuilder();

                long timeRemaining = noOfMinutes;
                if (timeRemaining > 0) {

                    Resources resources = Activity_JoinTour.this.getResources();

                    int days = (int) TimeUnit.MILLISECONDS.toDays(timeRemaining);
                    timeRemaining -= TimeUnit.DAYS.toMillis(days);
                    int hours = (int) TimeUnit.MILLISECONDS.toHours(timeRemaining);
                    timeRemaining -= TimeUnit.HOURS.toMillis(hours);
                    int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
                    timeRemaining -= TimeUnit.MINUTES.toMillis(minutes);
                    int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(timeRemaining);


                    //Convert milliseconds into hour,minute and seconds
                    String hms = String.format("%02d:%02d:%02d:%02d", TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));


                    tvTime.setText(hms + " Left");//set text

                }

            }

            public void onFinish() {
                btnSubmit.setClickable(false);
//                btnSubmit.setAlpha(0.7f);
                tvTime.setText("00:00"); //On finish change timer text



                tvTime.setVisibility(View.GONE);

            }
        }.start();

    }

    public void loader_dialog(final Context context) {
        loader_dialog = new Dialog(context);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progress_dilog_lyt, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/

        FillableLoader fillableLoader = view.findViewById(R.id.fillableLoader);

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


    public void show_gifdialog(String msg) {

        new TTFancyGifDialog.Builder(this)
                .setTitle("Tournament")
                .setMessage(msg)
                .setPositiveBtnText("Ok")
                .setPositiveBtnBackground("#3F51B5")
                .isCancellable(false)
                .setGifResource(R.drawable.success)      //pass your gif, png or jpg
                .isCancellable(true)
                .OnPositiveClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {


//                        Toast.makeText(Activity_ScoreSingles.this,"Ok",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_JoinTour.this, Activity_Badminton.class);
                        startActivity(intent);
                        finishAffinity();

                    }
                })

                .build();
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


                if (!tourDatum.getGroupMember().equals(tourDatum.getFidarray().size())) {
                    sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
                    dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    btn_submit.setClickable(false);
                }

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    private void winnerSlectionDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialogselect_winner_runner, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);

        iswinnerselected = true;


        RecyclerView winner_runner_rv = (RecyclerView) v1.findViewById(R.id.winner_runner_rv);
        TextView et_ico = (TextView) v1.findViewById(R.id.et_ico);


        et_ico.setText("Please Select Match Winner");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        winner_runner_rv.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
//        winner_runner_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
//         rv.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        winner_runner_rv.setItemAnimator(new DefaultItemAnimator());


        joinedOppAdapter = new JoinedOpponentsAdapter(Activity_JoinTour.this, tourDatum.getFidarray(), tourDatum.getWinnerAmount(), 1, 1, tourDatum.getRunnerupAmount());

        winner_runner_rv.setAdapter(joinedOppAdapter);


        Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) v1.findViewById(R.id.btn_submit);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                iswinnerselected = true;
                isrunnerSelected = false;

//                tvPl.setVisibility(View.GONE);
//                btnSubmit.setText("GO TO HOMEPAGE");
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                joinedOppAdapter.notifyrowremoved();

                selectedwinner = joinedOppAdapter.getselectedWinner();
                runnerSlectionDialog();


            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    private void runnerSlectionDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialogselect_winner_runner, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);

        isrunnerSelected = true;


        RecyclerView winner_runner_rv = (RecyclerView) v1.findViewById(R.id.winner_runner_rv);
        TextView et_ico = (TextView) v1.findViewById(R.id.et_ico);


        et_ico.setText("Please Select Match Runner");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        winner_runner_rv.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
//        winner_runner_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
//         rv.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        winner_runner_rv.setItemAnimator(new DefaultItemAnimator());


        joinedOppAdapter = new JoinedOpponentsAdapter(Activity_JoinTour.this, tourDatum.getFidarray(), tourDatum.getWinnerAmount(), 1, 2, tourDatum.getRunnerupAmount());

        winner_runner_rv.setAdapter(joinedOppAdapter);


        Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) v1.findViewById(R.id.btn_submit);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                tvPl.setVisibility(View.GONE);
//                btnSubmit.setText("GO TO HOMEPAGE");
                iswinnerselected = true;
                isrunnerSelected = false;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                iswinnerselected = true;
                isrunnerSelected = false;

                selectedRunner = joinedOppAdapter.getselectedRunner();
                Log.d("SLECTEDWIN: ---", selectedwinner);

                Log.d("SLECTEDRUNNER: ---- ", selectedRunner);


                endTournament(tourDatum.getMatchid(), selectedwinner, selectedRunner, tourDatum.getMid());

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    private String getRandomNumber(int min, int max) {
        return String.valueOf((new Random()).nextInt((max - min) + 1) + min);
    }


    private void joinTournament(String user_id, String user_level, String group_id, String f_id, String txn_id, String orderId) {

        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<JoinDualMatchResponse> callbackCall = apis.joinTournament(user_id, user_level, group_id, f_id, txn_id, orderId);
        callbackCall.enqueue(new Callback<JoinDualMatchResponse>() {
            @Override
            public void onResponse(Call<JoinDualMatchResponse> call, Response<JoinDualMatchResponse> response) {


                JoinDualMatchResponse resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();

                    show_gifdialog(resp.getErrorMsg());


//                    dialogServerNotConnect();
                } else {

                    loader_dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JoinDualMatchResponse> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        Log.d("checksum ", " respon true " + inResponse.toString());
//        Toast.makeText(this,inResponse+"",Toast.LENGTH_LONG).show();

        if (inResponse.getString("STATUS").equals("TXN_SUCCESS")) {


            String transaction_id = inResponse.getString("TXNID");

            joinTournament(tourDatum.getMid(),

                    SharedPreference.getSharedPreferenceString(Activity_JoinTour.this, SharedprefKeys.USER_SELECTED_LEVEL, ""),
                    tourDatum.getMatchid(),
                    SharedPreference.getSharedPreferenceString(Activity_JoinTour.this, SharedprefKeys.USER_ID, ""), transaction_id, serverOrderId);

        }


    }

    @Override
    public void networkNotAvailable() {

        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();

    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {

        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Log.d("checksum ", " ui fail respon  " + inErrorMessage);
        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

        Log.d("checksum ", " error loading pagerespon true " + iniErrorCode + "  s1 " + inErrorMessage);

        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.d("checksum ", " cancel call back respon  ");
        Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

        Log.d("checksum ", "  transaction cancel ");
        Toast.makeText(getApplicationContext(), "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStop() {
        super.onStop();

//            unregisterReceiver(instamojoPay);

        orderId = "order" + getRandomNumber(5, 100) + SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");



    }

    private void checkvalidatetime(String tournament_id, String time, String date, String no_of_player, String validate_time) {

        Apis apis = RestAdapter.createAPI();
        Call<CheckTourTimeRespo> callbackCall = apis.checktournamenttime(tournament_id, time, date, no_of_player, validate_time);
        callbackCall.enqueue(new Callback<CheckTourTimeRespo>() {
            @Override
            public void onResponse(Call<CheckTourTimeRespo> call, Response<CheckTourTimeRespo> response) {

                CheckTourTimeRespo resp = response.body();


                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    if (!resp.getDiff().equals("")) {

                        try {

                            tvTime.setVisibility(View.VISIBLE);
                            startTimer(Integer.parseInt(resp.getDiff())* 1000);


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    } else {
                        tvTime.setVisibility(View.GONE);

                    }


                    if (!resp.getMatchstatus().equals("")) {

                        tvOver.setVisibility(View.VISIBLE);

                        tvOver.setText(resp.getMatchstatus());
                    } else {
                        tvOver.setVisibility(View.INVISIBLE);
                    }


                    joinedflag = resp.getJoin();

                    endgameflag = resp.getEndgame();


                    if (resp.getStatus().equals("0")) {
                        Log.d("ENDGAMEFLAG", tourDatumList.get(tourdatumposition).getEndgame() + "");
                        if (endgameflag == 1) {


                            if (SharedPreference.getSharedPreferenceString(Activity_JoinTour.this, SharedprefKeys.USER_ID, "").equals(tourDatum.getMid())) {


                                if (tourDatum.getEndgame().equals("1")) {
                                    end_tour.setVisibility(View.GONE);
                                    btnreq_tv.setVisibility(View.VISIBLE);
                                    btnreq_tv.setText("Waiting for Winner Validation");
                                } else {

                                    end_tour.setVisibility(View.VISIBLE);
                                    btnreq_tv.setVisibility(View.GONE);
                                }

                            } else {

                                if (tourDatum.getEndgame().equals("1")) {

                                    /*                                validdate Show*/

                                    for (int i = 0; i < tourDatum.getFidarray().size(); i++) {

                                        if (!SharedPreference.getSharedPreferenceString(Activity_JoinTour.this, SharedprefKeys.USER_ID, "").equals(tourDatum.getMid())) {

                                            if (SharedPreference.getSharedPreferenceString(Activity_JoinTour.this, SharedprefKeys.USER_ID, "").equals(tourDatum.getFidarray().get(i).getFid())) {


                                                if (tourDatum.getFidarray().get(i).getVerify().equals("1")) {

                                                    end_tour.setVisibility(View.VISIBLE);
                                                    btnreq_tv.setVisibility(View.GONE);

                                                    end_tour.setText("VERIFIED");
                                                    end_tour.setClickable(false);

                                                } else {
                                                    end_tour.setVisibility(View.VISIBLE);
                                                    btnreq_tv.setVisibility(View.GONE);
                                                    end_tour.setText("VERIFY");
                                                    end_tour.setClickable(true);

                                                }


                                            }

                                        } else {
                                            end_tour.setVisibility(View.GONE);
                                        }

                                    }


                                } else {

                                    btnreq_tv.setVisibility(View.VISIBLE);
                                    btnreq_tv.setText("TOURNAMENT RUNNING...");


                                }

                            }


                        } else {


                        }


                        if (joinedflag == 0) {
                            int value=0;
                            for (int i = 0; i < tourDatum.getFidarray().size(); i++) {
                                if (SharedPreference.getSharedPreferenceString(Activity_JoinTour.this, SharedprefKeys.USER_ID, "").equals(tourDatum.getFidarray().get(i).getFid())) {

                                    value = 1;

                                }
                            }
                            if (value==1) {


                                btnSubmit.setVisibility(View.GONE);

                                btnreq_tv.setText("Tournament Joined...");
                                btnreq_tv.setVisibility(View.VISIBLE);


                            } else {
                                btnSubmit.setVisibility(View.VISIBLE);

                                /*                                    TODO: WORK NEED TO BE IN THIS APP*/
//                                    btnSubmit.setText("TESTHT");

                            }

//                            }


                        } else {

                            btnSubmit.setVisibility(View.GONE);

                        }
                    } else {

                        btnSubmit.setVisibility(View.GONE);
                    }

                } else {


//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<CheckTourTimeRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

    public void finishJoinedTour(View view) {
        this.finish();
    }

    private void endTournament(String tour_id, String winner_id, String runner_id, String host_mid) {

        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<EndtourRespo> callbackCall = apis.endtournament(tour_id, winner_id, runner_id, host_mid);
        callbackCall.enqueue(new Callback<EndtourRespo>() {
            @Override
            public void onResponse(Call<EndtourRespo> call, Response<EndtourRespo> response) {


                EndtourRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();

                    show_gifdialog(resp.getErrorMsg());


//                    dialogServerNotConnect();
                } else {

                    loader_dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<EndtourRespo> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

    private void verifyTour(String tour_id, String mid, String noofplayer, String flag, String velidate_time, String date) {

        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<EndtourRespo> callbackCall = apis.verifytournament(tour_id, mid, noofplayer, flag, velidate_time, date);
        callbackCall.enqueue(new Callback<EndtourRespo>() {
            @Override
            public void onResponse(Call<EndtourRespo> call, Response<EndtourRespo> response) {


                EndtourRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();

                    show_gifdialog(resp.getErrorMsg());


//                    dialogServerNotConnect();
                } else {

                    loader_dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<EndtourRespo> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

    private void verifyScoreConfirmationDialog() {

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

        btn_cancel.setText("NO");
        btn_submit.setText("YES");


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                try {
                    verifyTour(tourDatum.getMatchid(), SharedPreference.getSharedPreferenceString(Activity_JoinTour.this, SharedprefKeys.USER_ID, ""), tourDatum.getGroupMember(), "2", tourDatum.getValidatetime(), formatdateReturnDate(tourDatum.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


                try {
                    verifyTour(tourDatum.getMatchid(), SharedPreference.getSharedPreferenceString(Activity_JoinTour.this, SharedprefKeys.USER_ID, ""), tourDatum.getGroupMember(), "1", tourDatum.getValidatetime(), formatdateReturnDate(tourDatum.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public String convet24hourTo12hour(String time) {
        String convertedtime = "";

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            final Date dateObj = sdf.parse(time);
            convertedtime = new SimpleDateFormat("hh:mm aa").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return convertedtime;
    }

    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        //private String orderId , mid, custid, amt;
        String url = "http://synramprojects.com/rackonnect/generateChecksum.php";
        //                String callbackurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        String callbackurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId;
        //        String callbackurl = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderId;
        String varifyurl = "https://morningbucket.in/verifyChecksum.php";
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH = "";
        private ProgressDialog dialog = new ProgressDialog(Activity_JoinTour.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParser jsonParser = new JSONParser(Activity_JoinTour.this);


            String param =
                    "MID=" + mid +
                            "&ORDER_ID=" + orderId +
                            "&CUST_ID=" + custId +
                            "&CALLBACK_URL=" + callbackurl +
                            "&CHANNEL_ID=WAP&TXN_AMOUNT=" + tourDatum.getAmount() + "&WEBSITE=WEBSTAGING" +
                            "&INDUSTRY_TYPE_ID=Retail";


            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            // yaha per checksum ke saht order id or status receive hoga..
//          if (jsonObject!=null)
//            Log.e("CheckSum result >>",jsonObject.toString());

            Log.d("CheckSum result >>", jsonObject.toString());

            if (jsonObject != null) {
                Log.d("CheckSum result >>", jsonObject.toString());
                try {
                    CHECKSUMHASH = jsonObject.has("CHECKSUMHASH") ? jsonObject.getString("CHECKSUMHASH") : "";
                    Log.d("CheckSum result >>", CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ", "  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            PaytmPGService Service = PaytmPGService.getStagingService();
            // when app is ready to publish use production service
//            PaytmPGService Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custId);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", String.valueOf(tourDatum.getAmount()));
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL", callbackurl);
//            paramMap.put( "EMAIL" , "username@emailprovider.com");   // no need
//            paramMap.put( "MOBILE_NO" , "7777777777");  // no need
            paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");

            PaytmOrder Order = new PaytmOrder(paramMap);

            Log.d("CHECKMY--", CHECKSUMHASH);
            Log.e("checksum ", "param " + paramMap.toString());

            Service.initialize(Order, null);
            // start payment service call here
            Service.startPaymentTransaction(Activity_JoinTour.this, true, true,
                    Activity_JoinTour.this);
        }
    }

}
