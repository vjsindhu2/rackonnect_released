package com.sportskonnect;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.jorgecastillo.FillableLoader;
import com.google.gson.Gson;
import com.sportskonnect.activities.Activity_ScoreSingles;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.PlayerTurnUpRespo;
import com.sportskonnect.api.Callbacks.StartScoringResponse;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;


public class Activity_StartScoring extends AppCompatActivity implements View.OnClickListener {

    TextView tvMin, tvName, tv1, tv2, tvPl, game_name_tv, tvMatchDate;
    Typeface face1, face2;
    RelativeLayout relLayout;

    private SharedPreference sharedPreference;
    private String current_match_user_name = "";
    private String current_match_Opponent_name = "";
    private String current_match_Date = "";
    private String current_match_start_time = "";
    private String current_match_end_time = "";
    private TextView time_left_tv, start_scoring_msg_tv;
    private Runnable runnable;
    private Handler handler;
    private String current_match_id = "";
    private String current_match_cat_id = "";
    private int game_Type_counter = 7;
    private String current_match_type = "";
    private String current_match_Opponent_name_two = "";
    private String current_match_Opponent_name_three = "";
    private String current_match_NAME = "";
    private boolean isplayeronetrunup = false;
    private boolean isplayertwotrunup = false;
    private boolean isplayerthreetrunup = false;
    private String current_match_Opponent_name_level = "";
    private String current_match_Opponent_name_two_level = "";
    private String current_match_Opponent_name_three_level = "";
    private String current_match_Opponent_img = "";
    private String current_match_Opponent_img_two = "";
    private String current_match_Opponent_img_three = "";
    private String current_match_Opponent_id = "";
    private String current_match_Opponent_id_two = "";
    private String current_match_Opponent_id_three = "";

    private String playeroneTurnupString = "";
    private String playertwoTurnupString = "";
    private String playerthreeTurnupString = "";

    private List<String> playerturnuparray = new ArrayList<>();
    private Dialog loader_dialog;

    /**
     * Returns a formatted string containing the amount of time (days, hours,
     * minutes, seconds) between the current time and the specified future date.
     *
     * @param context
     * @param futureDate
     * @return
     */
    public static CharSequence getCountdownText(Context context, Date futureDate) {
        StringBuilder countdownText = new StringBuilder();

        // Calculate the time between now and the future date.
        long timeRemaining = futureDate.getTime() - new Date().getTime();

        // If there is no time between (ie. the date is now or in the past), do nothing
        if (timeRemaining > 0) {
            Resources resources = context.getResources();

            // Calculate the days/hours/minutes/seconds within the time difference.
            //
            // It's important to subtract the value from the total time remaining after each is calculated.
            // For example, if we didn't do this and the time was 25 hours from now,
            // we would get `1 day, 25 hours`.
            int days = (int) TimeUnit.MILLISECONDS.toDays(timeRemaining);
            timeRemaining -= TimeUnit.DAYS.toMillis(days);
            int hours = (int) TimeUnit.MILLISECONDS.toHours(timeRemaining);
            timeRemaining -= TimeUnit.HOURS.toMillis(hours);
            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
            timeRemaining -= TimeUnit.MINUTES.toMillis(minutes);
            int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(timeRemaining);

            // For each time unit, add the quantity string to the output, with a space.
            if (days > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.days, days, days));
                countdownText.append(" ");
            }
            if (days > 0 || hours > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.hours, hours, hours));
                countdownText.append(" ");
            }
            if (days > 0 || hours > 0 || minutes > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.minutes, minutes, minutes));
                countdownText.append(" ");
            }
            if (days > 0 || hours > 0 || minutes > 0 || seconds > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.seconds, seconds, seconds));
                countdownText.append(" ");
            }
        }

        return countdownText.toString();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscoring);

        sharedPreference = new SharedPreference(this);
        current_match_user_name = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_USER_NAME, "");

        current_match_Opponent_name = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, "");
        current_match_Opponent_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_MEMBERID, "");
        current_match_Opponent_name_level = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, "");
        current_match_Opponent_img = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, "");

        current_match_Opponent_name_two = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_TWO, "");
        current_match_Opponent_id_two = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_MEMBERID_TWO, "");
        current_match_Opponent_name_two_level = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_TWO, "");
        current_match_Opponent_img_two = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_TWO, "");


        current_match_Opponent_name_three = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_THREE, "");
        current_match_Opponent_id_three = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_MEMBERID_THREE, "");
        current_match_Opponent_name_three_level = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_THREE, "");
        current_match_Opponent_img_three = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_THREE, "");


        current_match_Date = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_DATE, "");
        current_match_start_time = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_START_TIME, "");
        current_match_end_time = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_END_TIME, "");
        current_match_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_ID, "");
        current_match_Date = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_DATE, "");
        current_match_cat_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_CAT_ID, "");
        current_match_type = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.MATCH_MATCHTYPEOF_GAME, "");
        current_match_NAME = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_NAME, "");


        init();
    }

    private void init() {

        tvMin = (TextView) findViewById(R.id.tvMin);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPl = (TextView) findViewById(R.id.tvPl);
        tv1 = (TextView) findViewById(R.id.tv1);
        start_scoring_msg_tv = (TextView) findViewById(R.id.start_scoring_msg_tv);
        time_left_tv = (TextView) findViewById(R.id.time_left_tv);
        tvMatchDate = (TextView) findViewById(R.id.tvMatchDate);
        game_name_tv = (TextView) findViewById(R.id.game_name_tv);
        relLayout = (RelativeLayout) findViewById(R.id.relLayout);
        relLayout.setOnClickListener(this);
        tv2 = (TextView) findViewById(R.id.tv2);
        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");
        tvMin.setTypeface(face1);

        face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");
        tvName.setTypeface(face2);
        tv1.setTypeface(face2);
        tv2.setTypeface(face2);
        tvPl.setTypeface(face2);

        tvPl.setOnClickListener(this);

        if (current_match_type.equals("1")) {
            tvName.setText(current_match_user_name + " v " + current_match_Opponent_name);
        } else {
            tvName.setText(current_match_NAME);

        }

        tv2.setText(Constant.convet24hourTo12hour(current_match_start_time) + " - " + Constant.convet24hourTo12hour(current_match_end_time));
        tvMatchDate.setText(current_match_Date);

        game_name_tv.setText(Constant.getGameNameFromcatId(current_match_cat_id));

        try {

            getscroing_flag(current_match_end_time, current_match_id, formatdateReturnDate(current_match_Date), current_match_type);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relLayout:

//                Intent intent = new Intent(Activity_StartScoring.this, Activity_StartScoring1.class);
//                startActivity(intent);

                openselectGameType();

                break;

            case R.id.tvPl:


                if (current_match_type.equals("1")) {

                    playersuritydialog();

                } else {

                    playerdidntturnup();

                }

                break;

        }
    }

    private void getscroing_flag(String endtime, String match_id, String date, String matchtype) {
        Apis apis = RestAdapter.createAPI();
        Call<StartScoringResponse> callbackCall = apis.checkstartscoring(endtime, match_id, date, matchtype);
        callbackCall.enqueue(new Callback<StartScoringResponse>() {
            @Override
            public void onResponse(Call<StartScoringResponse> call, Response<StartScoringResponse> response) {


                StartScoringResponse resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {


                    if (resp.getStartflag().equals("1")) {
                        relLayout.setClickable(true);

                        relLayout.setAlpha(1f);
                        time_left_tv.setVisibility(View.GONE);
                        start_scoring_msg_tv.setText("The match time is over.\nYou can start scoring now. ");

                        start_scoring_msg_tv.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {

                        relLayout.setClickable(false);
                        relLayout.setAlpha(0.7f);
                        time_left_tv.setVisibility(View.VISIBLE);
                        start_scoring_msg_tv.setText("You can start scoring once the\ngame is over. ");

                        start_scoring_msg_tv.setTypeface(Typeface.DEFAULT);

                        try {
                            startTimer(resp.getDiff() * 1000);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<StartScoringResponse> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

    private void startTimer(int noOfMinutes) {
        CountDownTimer countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;

                StringBuilder countdownText = new StringBuilder();

                long timeRemaining = noOfMinutes;
                if (timeRemaining > 0) {

                    Resources resources = Activity_StartScoring.this.getResources();

                    int days = (int) TimeUnit.MILLISECONDS.toDays(timeRemaining);
                    timeRemaining -= TimeUnit.DAYS.toMillis(days);
                    int hours = (int) TimeUnit.MILLISECONDS.toHours(timeRemaining);
                    timeRemaining -= TimeUnit.HOURS.toMillis(hours);
                    int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
                    timeRemaining -= TimeUnit.MINUTES.toMillis(minutes);
                    int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(timeRemaining);


                    //Convert milliseconds into hour,minute and seconds
                    String hms = String.format("%02d:%02d:%02d:%02d", TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

//                    String hms = String.format("%2d:%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                    // For each time unit, add the quantity string to the output, with a space.
//                    if (days > 0) {
//                        countdownText.append(resources.getQuantityString(R.plurals.days, days, days));
//                        countdownText.append(" ");
//                    }
//                    if (days > 0 || hours > 0) {
//                        countdownText.append(resources.getQuantityString(R.plurals.hours, hours, hours));
//                        countdownText.append(" ");
//                    }
//                    if (days > 0 || hours > 0 || minutes > 0) {
//                        countdownText.append(resources.getQuantityString(R.plurals.minutes, minutes, minutes));
//                        countdownText.append(" ");
//                    }
//                    if (days > 0 || hours > 0 || minutes > 0 || seconds > 0) {
//                        countdownText.append(resources.getQuantityString(R.plurals.seconds, seconds, seconds));
//                        countdownText.append(" ");
//                    }
                    time_left_tv.setText(hms);//set text

                }

            }

            public void onFinish() {
                relLayout.setClickable(true);

                relLayout.setAlpha(1f);
                time_left_tv.setVisibility(View.GONE);
                start_scoring_msg_tv.setText("The match time is over.\nYou can start scoring now. ");

                start_scoring_msg_tv.setTypeface(Typeface.DEFAULT_BOLD);


            }
        }.start();

    }

    public void finishStartScore(View view) {
        finish();
    }

    public String formatdateReturnDate(String date_str) throws ParseException {


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

        Date date = new Date(sdf.parse(date_str).getTime());
        date.setMonth(date.getMonth());

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String convertedate = sdf2.format(date);

        return convertedate;
    }


    private void openselectGameType() {

        final Dialog dialog = new Dialog(Activity_StartScoring.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.singlematch_dialog, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);


        TextView et_ico = (TextView) v1.findViewById(R.id.et_ico);
        TextView tvSingle = (TextView) v1.findViewById(R.id.tvSingle);
        TextView tvThree = (TextView) v1.findViewById(R.id.tvThree);
        TextView tvFive = (TextView) v1.findViewById(R.id.tvFive);
        TextView cs_count = (TextView) v1.findViewById(R.id.cs_count);
        TextView cs_minus_btn = (TextView) v1.findViewById(R.id.cs_minus_btn);
        TextView cs_plus_btn = (TextView) v1.findViewById(R.id.cs_plus_btn);
        LinearLayout cs_counter = (LinearLayout) v1.findViewById(R.id.cs_counter);
        et_ico.setTypeface(face2);
        tvSingle.setTypeface(face1);
        tvFive.setTypeface(face1);
        tvThree.setTypeface(face1);

        Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
        Button btn_buyBRNX = (Button) v1.findViewById(R.id.btn_buyBRNX);
        btn_cancel.setTypeface(face1);
        btn_buyBRNX.setTypeface(face1);

        SharedPreference.setSharedPreferenceString(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE, "BEST OF " + "3");
        SharedPreference.setSharedPreferenceInt(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE_COUNTER, 3);

        cs_minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game_Type_counter > 7) {

                    game_Type_counter = game_Type_counter - 2;
                    cs_count.setText(game_Type_counter + "");

                    SharedPreference.setSharedPreferenceString(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE, "BEST OF " + game_Type_counter);
                    SharedPreference.setSharedPreferenceInt(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE_COUNTER, game_Type_counter);

                }


            }


        });
        cs_plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game_Type_counter < 1000) {

                    game_Type_counter = game_Type_counter + 2;
                    cs_count.setText(game_Type_counter + "");


                }

                SharedPreference.setSharedPreferenceString(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE, "BEST OF " + game_Type_counter);
                SharedPreference.setSharedPreferenceInt(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE_COUNTER, game_Type_counter);

            }
        });

        tvSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSingle.setAlpha(1f);
                tvThree.setAlpha(0.5f);
                tvFive.setAlpha(0.5f);
                cs_counter.setVisibility(View.GONE);
                SharedPreference.setSharedPreferenceString(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE, "BEST OF 3");
                SharedPreference.setSharedPreferenceInt(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE_COUNTER, 3);

            }
        });

        tvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSingle.setAlpha(0.5f);
                tvThree.setAlpha(1f);
                tvFive.setAlpha(0.5f);
                cs_counter.setVisibility(View.GONE);

                SharedPreference.setSharedPreferenceString(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE, "BEST OF 5");
                SharedPreference.setSharedPreferenceInt(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE_COUNTER, 5);


            }
        });


        tvFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSingle.setAlpha(0.5f);
                tvThree.setAlpha(0.5f);
                tvFive.setAlpha(1f);
                cs_counter.setVisibility(View.VISIBLE);

                SharedPreference.setSharedPreferenceString(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE, "BEST OF 7");
                SharedPreference.setSharedPreferenceInt(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE_COUNTER, 7);



            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                tvPl.setVisibility(View.GONE);
//                btnSubmit.setText("GO TO HOMEPAGE");
            }
        });

        btn_buyBRNX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


                Intent intent = new Intent(Activity_StartScoring.this, Activity_ScoreSingles.class);
                startActivity(intent);


            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    private void playerdidntturnup() {

        final Dialog dialog = new Dialog(Activity_StartScoring.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.activity_doubledialog, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);

        TextView opp_one_name = (TextView) v1.findViewById(R.id.opp_one_name);
        TextView opp_one_level = (TextView) v1.findViewById(R.id.opp_one_level);
        TextView opp_two_name = (TextView) v1.findViewById(R.id.opp_two_name);
        TextView opp_two_level = (TextView) v1.findViewById(R.id.opp_two_level);
        TextView opp_three_name = (TextView) v1.findViewById(R.id.opp_three_name);
        TextView opp_three_level = (TextView) v1.findViewById(R.id.opp_three_level);


        CircleImageView opp_one_img = (CircleImageView) v1.findViewById(R.id.opp_one_img);
        CircleImageView opp_two_img = (CircleImageView) v1.findViewById(R.id.opp_two_img);
        CircleImageView opp_three_img = (CircleImageView) v1.findViewById(R.id.opp_three_img);

        ToggleButton opp_one_toggle = (ToggleButton) v1.findViewById(R.id.opp_one_toggle);
        ToggleButton opp_two_toggle = (ToggleButton) v1.findViewById(R.id.opp_two_toggle);
        ToggleButton opp_three_toggle = (ToggleButton) v1.findViewById(R.id.opp_three_toggle);


        try {
            opp_one_name.setText(current_match_Opponent_name);
            opp_one_level.setText(Constant.getGameLevelFromlevelId(current_match_Opponent_name_level));
            Picasso.get().load(current_match_Opponent_img).placeholder(R.drawable.male).into(opp_one_img);

            opp_two_name.setText(current_match_Opponent_name_two);
            opp_two_level.setText(Constant.getGameLevelFromlevelId(current_match_Opponent_name_two_level));
            Picasso.get().load(current_match_Opponent_img_two).placeholder(R.drawable.male).into(opp_two_img);

            opp_three_name.setText(current_match_Opponent_name_three);
            opp_three_level.setText(Constant.getGameLevelFromlevelId(current_match_Opponent_name_three_level));
            Picasso.get().load(current_match_Opponent_img_three).placeholder(R.drawable.male).into(opp_three_img);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        playeroneTurnupString = "";
        playertwoTurnupString = "";
        playerthreeTurnupString = "";
        isplayeronetrunup = false;
        isplayerthreetrunup = false;
        isplayertwotrunup = false;

        opp_one_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (opp_one_toggle.isChecked()) {
                    isplayeronetrunup = true;

                    playeroneTurnupString = current_match_Opponent_id;
                    playerturnuparray.add(current_match_Opponent_id);

                } else {
                    isplayeronetrunup = false;
                    playeroneTurnupString = "";
//                    playerturnuparray.remove(current_match_Opponent_id);

                }
            }
        });

        opp_two_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (opp_two_toggle.isChecked()) {
                    isplayertwotrunup = true;
                    playertwoTurnupString = current_match_Opponent_id_two;
                    playerturnuparray.add(current_match_Opponent_id_two);
                } else {
                    isplayertwotrunup = false;
                    playertwoTurnupString = "";
//                    playerturnuparray.remove(current_match_Opponent_id_two);

                }
            }
        });

        opp_three_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (opp_three_toggle.isChecked()) {
                    isplayerthreetrunup = true;
                    playerthreeTurnupString = current_match_Opponent_id_three;
                    playerturnuparray.add(current_match_Opponent_id_three);


                } else {
                    isplayerthreetrunup = false;
                    playerthreeTurnupString = "";
//                    playerturnuparray.remove(current_match_Opponent_id_three);

                }
            }
        });


        Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) v1.findViewById(R.id.btn_submit);
        btn_cancel.setTypeface(face1);
        btn_submit.setTypeface(face1);

        SharedPreference.setSharedPreferenceString(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE, "BEST OF " + "3");
        SharedPreference.setSharedPreferenceInt(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE_COUNTER, 3);


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


                if (isplayeronetrunup || isplayertwotrunup || isplayerthreetrunup) {


                    playerDoublegameTurnup(current_match_id, playeroneTurnupString, playertwoTurnupString, playerthreeTurnupString);


                } else {

                    Toast.makeText(Activity_StartScoring.this, "Please Select Player", Toast.LENGTH_LONG).show();
                }


            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    private void playerSinglegameTurnup(String match_id) {
        Apis apis = RestAdapter.createAPI();
        Call<PlayerTurnUpRespo> callbackCall = apis.playerturnup(match_id);
        callbackCall.enqueue(new Callback<PlayerTurnUpRespo>() {
            @Override
            public void onResponse(Call<PlayerTurnUpRespo> call, Response<PlayerTurnUpRespo> response) {


                PlayerTurnUpRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {


                    Intent intent = new Intent(Activity_StartScoring.this, Activity_Badminton.class);
                    startActivity(intent);
                    finishAffinity();
                    Toast.makeText(Activity_StartScoring.this, resp.getErrorMsg(), Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(Activity_StartScoring.this, resp.getErrorMsg(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<PlayerTurnUpRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void playerDoublegameTurnup(String match_id, String m1, String m2, String m3) {

        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<PlayerTurnUpRespo> callbackCall = apis.playerDoublesturnup(match_id, m1, m2, m3);
        callbackCall.enqueue(new Callback<PlayerTurnUpRespo>() {
            @Override
            public void onResponse(Call<PlayerTurnUpRespo> call, Response<PlayerTurnUpRespo> response) {


                PlayerTurnUpRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {


                    loader_dialog.dismiss();
                    Intent intent = new Intent(Activity_StartScoring.this, Activity_Badminton.class);
                    startActivity(intent);
                    finishAffinity();
                    Toast.makeText(Activity_StartScoring.this, resp.getErrorMsg(), Toast.LENGTH_LONG).show();


                } else {
                    loader_dialog.dismiss();
                    Toast.makeText(Activity_StartScoring.this, resp.getErrorMsg(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<PlayerTurnUpRespo> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void playersuritydialog() {

        final Dialog dialog = new Dialog(Activity_StartScoring.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialog_forsurity, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);


        Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) v1.findViewById(R.id.btn_submit);
        btn_cancel.setTypeface(face1);
        btn_submit.setTypeface(face1);

        SharedPreference.setSharedPreferenceString(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE, "BEST OF " + "3");
        SharedPreference.setSharedPreferenceInt(Activity_StartScoring.this, SharedprefKeys.CURRENT_GAME_TYPE_COUNTER, 3);


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

                playerSinglegameTurnup(current_match_id);

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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

//        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loader_dialog.setContentView(view);
        final Window window = loader_dialog.getWindow();
        loader_dialog.setCancelable(false);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        loader_dialog.show();
    }

}
