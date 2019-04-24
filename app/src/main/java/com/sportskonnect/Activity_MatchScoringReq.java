package com.sportskonnect;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.github.jorgecastillo.FillableLoader;
import com.google.gson.Gson;
import com.sportskonnect.activities.ScoringRequestsActivity;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.CreateMatchRespo;
import com.sportskonnect.api.Callbacks.SaveScoreResponse;
import com.sportskonnect.api.Callbacks.StartScoringResponse;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;

public class Activity_MatchScoringReq extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmit;
    TextView lbl_left_tv;
    TextView tv_user_points, tv_opponents_point, host_won, opponent_won, note_warning;
    TextView game_type, tvName, tvdate, tv_host_name, tv_host_level, tv_opponet_name, tv_opponet_level;
    CircleImageView opponent_img, host_img, group_a_member_two_img, group_b_member_two_img;
    TextView tv_30minmsg, tvSc;
    LinearLayout validated_player_ll;

    private TextView resend_score_req;


    CircleImageView v_opponent_one_img, v_opponent_two_img, v_opponent_three_img;
    ImageView game_img;
    TextView tvTime;
    private String current_match_end_time = "";
    private String current_match_id = "";
    private String current_match_Date = "";
    private Dialog loader_dialog;
    private String validatestatus = "";
    private String match_type = "";
    private String matchname = "";
    private TextView verified_opponents_name, verified_opponents_name_two, verified_opponents_name_three;
    private String group_id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchscorerequest);

        init();
    }

    private void init() {

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        game_type = (TextView) findViewById(R.id.game_type);
        note_warning = (TextView) findViewById(R.id.note_warning);
        tv_30minmsg = (TextView) findViewById(R.id.tv_30minmsg);
        resend_score_req = (TextView) findViewById(R.id.resend_score_req);
        verified_opponents_name = (TextView) findViewById(R.id.verified_opponents_name);
        verified_opponents_name_three = (TextView) findViewById(R.id.verified_opponents_name_three);
        verified_opponents_name_two = (TextView) findViewById(R.id.verified_opponents_name_two);
        tv_user_points = (TextView) findViewById(R.id.user_points);
        tvSc = (TextView) findViewById(R.id.tvSc);
        tvTime = (TextView) findViewById(R.id.tvTime);
//        resend_score_req = (TextView) findViewById(R.id.resend_score_req);
        tv_opponents_point = (TextView) findViewById(R.id.opponents_point);
        v_opponent_one_img = (CircleImageView) findViewById(R.id.v_opponent_one_img);
        v_opponent_two_img = (CircleImageView) findViewById(R.id.v_opponet_two_img);
        v_opponent_three_img = (CircleImageView) findViewById(R.id.v_opponent_three_img);
        tvdate = (TextView) findViewById(R.id.tvdate);
        lbl_left_tv = (TextView) findViewById(R.id.lbl_left_tv);
        tvName = (TextView) findViewById(R.id.tvName);
        host_img = (CircleImageView) findViewById(R.id.host_img);
        opponent_img = (CircleImageView) findViewById(R.id.opponent_img);
        group_a_member_two_img = (CircleImageView) findViewById(R.id.team_a_person_two);
        group_b_member_two_img = (CircleImageView) findViewById(R.id.team_b_person_two);
        tv_host_name = (TextView) findViewById(R.id.tv_host_name);
        tv_host_level = (TextView) findViewById(R.id.tv_host_level);
        tv_opponet_name = (TextView) findViewById(R.id.tv_opponet_name);
        tv_opponet_level = (TextView) findViewById(R.id.tv_opponet_level);
        host_won = (TextView) findViewById(R.id.host_won);
        opponent_won = (TextView) findViewById(R.id.opponent_won);
        game_img = (ImageView) findViewById(R.id.game_img);
        validated_player_ll = (LinearLayout) findViewById(R.id.validated_player_ll);


        current_match_end_time = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_END_TIME, "");
        current_match_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_ID, "");
        current_match_Date = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_DATE, "");


        String host_name = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_USER_NAME, "");

        String opponent_name = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, "");

        String opponent_name_two = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_TWO, "");
        String opponent_name_three = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_THREE, "");

        String match_date = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_DATE, "");
        String gametype = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_CAT_ID, "");
        String opponet_level = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, "");
        String host_level = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_USER_LEVEL, "");

        String user_points = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_USER_POINTS, "");
        String opponents_point = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_POINTS, "");
        String opponents_img = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, "");
        String opponents_verify_one = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_VERIFY, "");
        String opponents_verify_two = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_VERIFY_TWO, "");
        String opponents_verify_three = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_VERIFY_THREE, "");


        String opponents_img_two = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_TWO, "");
        String opponents_img_three = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_THREE, "");


        String user_img = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_USER_IMAGE, "");
        String validatetime = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_VALIDATE_TIME, "");
        group_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_GROUP_ID, "");

        matchname = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_NAME, "");


        validatestatus = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_STATUS, "");
        match_type = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.MATCH_MATCHTYPEOF_GAME, "");

        tvSc.setText(SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_TYPE, ""));


        tvdate.setText(match_date);
        game_type.setText(Constant.getGameNameFromcatId(gametype));


        tv_user_points.setText(user_points);
        tv_opponents_point.setText(opponents_point);

        resend_score_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmdialog();

            }
        });


        if (match_type.equals("1")) {

            try {

                validated_player_ll.setVisibility(View.GONE);
                tvName.setText(host_name + " v " + opponent_name);


                tv_opponet_name.setText(opponent_name);
                tv_opponet_level.setText(Constant.getGameLevelFromlevelId(opponet_level));

                tv_host_name.setText(host_name);
                tv_host_level.setText(Constant.getGameLevelFromlevelId(host_level));


                if (!user_img.equals("")) {
                    Picasso.get().load(user_img).placeholder(R.drawable.male).into(host_img);
                    host_img.setVisibility(View.VISIBLE);

                } else {

                    host_img.setVisibility(View.GONE);

                }


                if (!opponents_img.equals("")) {
                    Picasso.get().load(opponents_img).placeholder(R.drawable.male).into(opponent_img);
                    opponent_img.setVisibility(View.VISIBLE);

                } else {
                    opponent_img.setVisibility(View.GONE);

                }


                group_a_member_two_img.setVisibility(View.GONE);
                group_b_member_two_img.setVisibility(View.GONE);

                getvalidateFlag(validatetime, current_match_id, formatdateReturnDate(current_match_Date), match_type, "");


            } catch (Exception ex) {
                ex.printStackTrace();
            }


        } else {

            try {


                if (opponents_verify_one.equals("1")) {

                    Picasso.get().load(opponents_img).placeholder(R.drawable.male).into(v_opponent_one_img);

                    verified_opponents_name.setText(" "+opponent_name+", ");

                }

                if (opponents_verify_two.equals("1")) {

                    Picasso.get().load(opponents_img_two).placeholder(R.drawable.male).into(v_opponent_two_img);

                    verified_opponents_name_two.setText(" "+opponent_name_two+", ");

                }

                if (opponents_verify_three.equals("1")) {

                    Picasso.get().load(opponents_img_three).placeholder(R.drawable.male).into(v_opponent_three_img);
                    verified_opponents_name_three.setText(" "+opponent_name_three);

                }


                validated_player_ll.setVisibility(View.VISIBLE);

                tvName.setText(matchname);


                tv_opponet_name.setText("Group 'B'");
                tv_opponet_level.setText(opponent_name + "\n" + opponent_name_three);

                tv_host_name.setText("GROUP 'A'");
                tv_host_level.setText(host_name + "\n" + opponent_name_two);


                if (!user_img.equals("")) {
                    Picasso.get().load(user_img).placeholder(R.drawable.male).into(host_img);
                    host_img.setVisibility(View.VISIBLE);

                } else {
                    host_img.setVisibility(View.GONE);

                }


                if (!opponents_img.equals("")) {
                    Picasso.get().load(opponents_img).placeholder(R.drawable.male).into(opponent_img);
                    opponent_img.setVisibility(View.VISIBLE);

                } else {
                    opponent_img.setVisibility(View.GONE);

                }


                if (!opponents_img_two.equals("")) {
                    Picasso.get().load(opponents_img_two).placeholder(R.drawable.male).into(group_a_member_two_img);
                    group_a_member_two_img.setVisibility(View.VISIBLE);

                } else {
                    group_a_member_two_img.setVisibility(View.GONE);

                }

                if (!opponents_img_three.equals("")) {
                    Picasso.get().load(opponents_img_three).placeholder(R.drawable.male).into(group_b_member_two_img);
                    group_b_member_two_img.setVisibility(View.VISIBLE);

                } else {
                    group_b_member_two_img.setVisibility(View.GONE);

                }

                getvalidateFlag(validatetime, current_match_id, formatdateReturnDate(current_match_Date), match_type, group_id);


            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }


        try {

//            Picasso.get().load(user_img).placeholder(R.drawable.male).into(host_img);
//            Picasso.get().load(opponents_img).placeholder(R.drawable.male).into(opponent_img);

            Picasso.get().load(Constant.getGameImageFromcatId(gametype)).into(game_img);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {

            if (!user_points.equals("") && !user_points.equals(null) && !opponents_point.equals("") && !opponents_point.equals(null)) {

                note_warning.setVisibility(View.GONE);
                btnSubmit.setClickable(true);
//                resend_score_req.setClickable(true);
                btnSubmit.setAlpha(1f);

                if (Integer.parseInt(user_points) > Integer.parseInt(opponents_point)) {

                    host_won.setVisibility(View.VISIBLE);
                    opponent_won.setVisibility(View.GONE);


                } else if (Integer.parseInt(user_points) < Integer.parseInt(opponents_point)) {
                    host_won.setVisibility(View.GONE);
                    opponent_won.setVisibility(View.VISIBLE);

                }

            } else {
                note_warning.setVisibility(View.VISIBLE);
                btnSubmit.setClickable(false);
                btnSubmit.setAlpha(0.5f);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (!validatestatus.equals("") && !validatestatus.equals("1")) {
            btnSubmit.setClickable(true);
            btnSubmit.setAlpha(1f);

            resend_score_req.setClickable(true);
            resend_score_req.setAlpha(1f);
            tvTime.setVisibility(View.VISIBLE);
            lbl_left_tv.setVisibility(View.VISIBLE);

            tv_30minmsg.setText(getResources().getString(R.string.please_confirm_the_score_within_30_minutes_of_the_request_otherwise_it_would_be_invalidated));


//            verifyScore(current_match_id);

        } else {
            btnSubmit.setClickable(false);
            btnSubmit.setAlpha(0.7f);

            resend_score_req.setClickable(false);
            resend_score_req.setAlpha(0.7f);

            tvTime.setVisibility(View.GONE);
            lbl_left_tv.setVisibility(View.GONE);

            tv_30minmsg.setText("Score already Verified ");


//            Toast.makeText(this,"You already Verified Score",Toast.LENGTH_LONG).show();
        }


        btnSubmit.setOnClickListener(this);


    }


    private void validateconfirmdialog() {

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

                if (match_type.equals("1")){


                    verifyScore(current_match_id);


                }else{
                    verifyScoreDoubles(current_match_id,group_id,SharedPreference.getSharedPreferenceString(Activity_MatchScoringReq.this,SharedprefKeys.USER_ID,""));


                }

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
                resendupdatedScore(current_match_id);

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

//                Intent intent = new Intent(Activity_MatchScoringReq.this, Activity_ValidScore.class);
//                startActivity(intent);


                if (!validatestatus.equals("") && !validatestatus.equals("1")) {
//                    btnSubmit.setClickable(true);
//                    btnSubmit.setAlpha(1f);
//
//                    resend_score_req.setClickable(true);
//                    resend_score_req.setAlpha(1f);

                    validateconfirmdialog();

                } else {
//                    btnSubmit.setClickable(false);
//                    btnSubmit.setAlpha(0.7f);
//
//                    resend_score_req.setClickable(false);
//                    resend_score_req.setAlpha(0.7f);


                    Toast.makeText(this, "You already Verified Score", Toast.LENGTH_LONG).show();
                }


                break;
        }
    }

    public String formatdateReturnDate(String date_str) throws ParseException {


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

        Date date = new Date(sdf.parse(date_str).getTime());
        date.setMonth(date.getMonth());

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String convertedate = sdf2.format(date);

        return convertedate;
    }


    private void getvalidateFlag(String velidatetime, String match_id, String date, String match_type, String group_id) {
        Apis apis = RestAdapter.createAPI();
        Call<StartScoringResponse> callbackCall = apis.checkvalidatetime(velidatetime, match_id, date, match_type, group_id);
        callbackCall.enqueue(new Callback<StartScoringResponse>() {
            @Override
            public void onResponse(Call<StartScoringResponse> call, Response<StartScoringResponse> response) {


                StartScoringResponse resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {


                    if (!resp.getDiff().equals("0")) {
                        btnSubmit.setClickable(true);

                        btnSubmit.setAlpha(1f);
//                        time_left_tv.setVisibility(View.GONE);
//                        start_scoring_msg_tv.setText("The match time is over.\nYou can start scoring now. ");
//                        start_scoring_msg_tv.setTypeface(Typeface.DEFAULT_BOLD);

                        try {
                            startTimer(resp.getDiff() * 1000);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    } else {

                        btnSubmit.setClickable(false);
                        btnSubmit.setAlpha(0.7f);
//                        time_left_tv.setVisibility(View.VISIBLE);
//                        start_scoring_msg_tv.setText("You can start scoring once the\ngame is over. ");
//
//                        start_scoring_msg_tv.setTypeface(Typeface.DEFAULT);


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

                    Resources resources = Activity_MatchScoringReq.this.getResources();

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
                    tvTime.setText(hms);//set text

                }

            }

            public void onFinish() {
                btnSubmit.setClickable(false);
                btnSubmit.setAlpha(0.7f);
                tvTime.setText("00:00"); //On finish change timer text
                lbl_left_tv.setVisibility(View.GONE);
            }
        }.start();

    }


    private void verifyScore(String matchid) {
        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<SaveScoreResponse> callbackCall = apis.verifyscore(matchid);
        callbackCall.enqueue(new Callback<SaveScoreResponse>() {
            @Override
            public void onResponse(Call<SaveScoreResponse> call, Response<SaveScoreResponse> response) {


                SaveScoreResponse resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();

                    show_gifdialog();


                } else {
                    loader_dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SaveScoreResponse> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }



    private void resendupdatedScore(String matchid) {
        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<CreateMatchRespo> callbackCall = apis.resendupdateScore(matchid);
        callbackCall.enqueue(new Callback<CreateMatchRespo>() {
            @Override
            public void onResponse(Call<CreateMatchRespo> call, Response<CreateMatchRespo> response) {


                CreateMatchRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();

                    show_gifdialogForeresend(resp.getErrorMsg());


                } else {
                    loader_dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CreateMatchRespo> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }



    private void verifyScoreDoubles(String matchid,String group_id,String mid) {
        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<SaveScoreResponse> callbackCall = apis.verifyscoredouble(matchid,group_id,mid);
        callbackCall.enqueue(new Callback<SaveScoreResponse>() {
            @Override
            public void onResponse(Call<SaveScoreResponse> call, Response<SaveScoreResponse> response) {


                SaveScoreResponse resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();

                    show_gifdialog();


                } else {
                    loader_dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SaveScoreResponse> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }



    public void loader_dialog(final Context context) {
        loader_dialog = new Dialog(context);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progress_dilog_lyt, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/
        final FillableLoader fillableLoader = view.findViewById(R.id.fillableLoader);
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

    public void show_gifdialog() {

        new TTFancyGifDialog.Builder(Activity_MatchScoringReq.this)
                .setTitle("Success")
                .setMessage("Game Score Verified Successfully...")
                .setPositiveBtnText("Ok")
                .setPositiveBtnBackground("#3F51B5")
                .isCancellable(false)
                .setGifResource(R.drawable.success)      //pass your gif, png or jpg
                .isCancellable(true)
                .OnPositiveClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {


//                        Toast.makeText(Activity_ScoreSingles.this,"Ok",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_MatchScoringReq.this, Activity_Badminton.class);
                        startActivity(intent);
                        finish();

                    }
                })

                .build();
    }


    public void show_gifdialogForeresend(String msg) {

        new TTFancyGifDialog.Builder(Activity_MatchScoringReq.this)
                .setTitle("Success")
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
                        Intent intent = new Intent(Activity_MatchScoringReq.this, Activity_Badminton.class);
                        startActivity(intent);
                        finish();

                    }
                })

                .build();
    }


    public void finishmatchscorReqActivity(View view) {
        finish();
    }


}
