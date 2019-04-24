package com.sportskonnect.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.github.jorgecastillo.FillableLoader;
import com.google.gson.Gson;
import com.sportskonnect.Activity_matchScore;
import com.sportskonnect.R;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.SaveScoreResponse;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;

public class Activity_ScoreSingles extends AppCompatActivity implements View.OnClickListener {

    Typeface face1, face2;
    TextView tvuser_minus_pnt, tvuser_pls_pnt, tvuser_pnt;
    Button save_score_btn;
    TextView opponent_minus_pnts;
    private TextView match_name_tv, tvName, tvMatchDate, currnetuser_name, currentuser_level, current_opponet_name, current_opponet_level;
    private TextView tvSc, tvMin, tvopponent_plus_pnts;
    private ImageView ivC;
    private CircleImageView current_opponnent_img, currentuser_img, group_a_member_two_img, group_b_member_two_img;
    private int user_points_counter = 0;
    private int opponent_points_counter = 0;

    private int maxpoints = 0;
    private int totalGamePoints = 0;

    private String current_match_user_name = "";
    private String current_match_Opponent_name = "";
    private String current_match_Date = "";
    private String current_match_start_time = "";
    private String current_match_end_time = "";
    private TextView time_left_tv, start_scoring_msg_tv, tv_opponet_pnts, lbl_user_win, lbl_opponet_win;

    private String current_match_id = "";
    private String current_match_cat_id = "";
    private String current_match_User_img = "";
    private String current_match_Opponent_img = "";
    private String current_game_type = "";
    private String current_match_user_level_id = "";
    private String current_match_opponent_level_id = "";
    private Dialog loader_dialog;
    private String match_typeofGame = "";
    private String current_match_Opponent_img_two = "";
    private String current_match_Opponent_img_three = "";
    private String currentgameName = "";
    private String current_match_Opponent_name_two = "";
    private String current_match_Opponent_name_three = "";

    private TextView guide_three, guide_one, guide_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_singles);


        current_match_user_name = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_USER_NAME, "");

        current_match_Opponent_name = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, "");
        current_match_Opponent_name_two = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_TWO, "");
        current_match_Opponent_name_three = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_THREE, "");

        current_match_Date = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_DATE, "");
        current_match_start_time = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_START_TIME, "");
        current_match_end_time = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_END_TIME, "");
        current_match_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_ID, "");
        current_match_Date = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_DATE, "");
        current_match_cat_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_CAT_ID, "");

        current_match_User_img = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_USER_IMAGE, "");

        current_match_Opponent_img = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, "");
        current_match_Opponent_img_two = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_TWO, "");
        current_match_Opponent_img_three = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_THREE, "");


        current_game_type = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_GAME_TYPE, "");
        current_match_user_level_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_USER_LEVEL, "");
        current_match_opponent_level_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, "");
        maxpoints = SharedPreference.getSharedPreferenceInt(this, SharedprefKeys.CURRENT_GAME_TYPE_COUNTER, 0);

        match_typeofGame = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.MATCH_MATCHTYPEOF_GAME, "");
        currentgameName = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_NAME, "");


        init();

    }


    private void init() {
        match_name_tv = (TextView) findViewById(R.id.match_name_tv);
        guide_three = (TextView) findViewById(R.id.guide_three);
        guide_one = (TextView) findViewById(R.id.guid_on);
        guide_two = (TextView) findViewById(R.id.guide_two);
        tvMatchDate = (TextView) findViewById(R.id.tvMatchDate);
        currnetuser_name = (TextView) findViewById(R.id.currnetuser_name);
        currentuser_level = (TextView) findViewById(R.id.currentuser_level);
        current_opponet_level = (TextView) findViewById(R.id.current_opponet_level);
        current_opponet_name = (TextView) findViewById(R.id.current_opponet_name);
        tvuser_minus_pnt = (TextView) findViewById(R.id.tvuser_minus_pnt);
        tvuser_pnt = (TextView) findViewById(R.id.tvuser_pnt);
        tvuser_pls_pnt = (TextView) findViewById(R.id.tvuser_pls_pnt);
        tvopponent_plus_pnts = (TextView) findViewById(R.id.tvopponent_plus_pnts);
        lbl_user_win = (TextView) findViewById(R.id.lbl_user_win);
        lbl_opponet_win = (TextView) findViewById(R.id.lbl_opponet_win);
        opponent_minus_pnts = (TextView) findViewById(R.id.opponent_minus_pnts);
        tv_opponet_pnts = (TextView) findViewById(R.id.tv_opponet_pnts);
        currentuser_img = (CircleImageView) findViewById(R.id.currentuser_img);
        current_opponnent_img = (CircleImageView) findViewById(R.id.current_opponnent_img);
        group_b_member_two_img = (CircleImageView) findViewById(R.id.group_b_member_two_img);
        group_a_member_two_img = (CircleImageView) findViewById(R.id.group_a_member_two_img);
        save_score_btn = (Button) findViewById(R.id.save_score_btn);

        tvSc = (TextView) findViewById(R.id.tvSc);

        tvMin = (TextView) findViewById(R.id.tvMin);

        tvName = (TextView) findViewById(R.id.tvName);

        ivC = (ImageView) findViewById(R.id.ivC);


        tvMatchDate.setText(current_match_Date);

        ivC.setOnClickListener(this);
        save_score_btn.setOnClickListener(this);

        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");
        tvSc.setTypeface(face1);
        face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");
        tvName.setTypeface(face2);
        tvMin.setTypeface(face1);


        if (match_typeofGame.equals("1")) {

            tvName.setText(current_match_user_name + " v " + current_match_Opponent_name);
            currnetuser_name.setText(current_match_user_name);
            current_opponet_name.setText(current_match_Opponent_name);

            currentuser_level.setText(Constant.getGameLevelFromlevelId(current_match_user_level_id));
            current_opponet_level.setText(Constant.getGameLevelFromlevelId(current_match_opponent_level_id));

            guide_one.setText("Do not put score for each game, instead put overall score of your match. Example A & B play, A wins by 21-12,15-21, 21-18 x and 2-1 tick or enter number of games won");

            guide_two.setText("Scores must be correct as it will be validated by the opponent.");
            guide_three.setText("If the other player doesn’t accept the score they have to give a valid reason and the match will not be updated on the leaderboard.");

            if (!currentuser_img.equals("")) {
                Picasso.get().load(current_match_User_img).placeholder(R.drawable.male).into(currentuser_img);
                currentuser_img.setVisibility(View.VISIBLE);

            } else {
                currentuser_img.setVisibility(View.GONE);

            }


            if (!current_match_Opponent_img.equals("")) {
                Picasso.get().load(current_match_Opponent_img).placeholder(R.drawable.male).into(current_opponnent_img);
                current_opponnent_img.setVisibility(View.VISIBLE);

            } else {
                current_opponnent_img.setVisibility(View.GONE);

            }


            if (!current_match_Opponent_img_two.equals("")) {
                Picasso.get().load(current_match_Opponent_img_two).placeholder(R.drawable.male).into(group_a_member_two_img);
                group_a_member_two_img.setVisibility(View.GONE);

            } else {
                group_a_member_two_img.setVisibility(View.GONE);

            }

            if (!current_match_Opponent_img_three.equals("")) {
                Picasso.get().load(current_match_Opponent_img_three).placeholder(R.drawable.male).into(group_b_member_two_img);
                group_b_member_two_img.setVisibility(View.GONE);

            } else {
                group_b_member_two_img.setVisibility(View.GONE);

            }


        } else {

            tvName.setText(currentgameName);
            currnetuser_name.setText("Group 'A'");
            current_opponet_name.setText("Group 'B'");

            guide_one.setText("Do not put score for each game, instead put overall score of your match. Example Group A & Group B play, Group A wins by 21-12,15-21, 21-18 x and 2-1 tick or enter number of games won");

            guide_two.setText("Scores must be correct as it will be validated by the opponents.");
            guide_three.setText("If the other players doesn’t accept the score they have to give a valid reason and the match will not be updated on the leaderboard.");

            currentuser_level.setText(current_match_user_name + "\n" + current_match_Opponent_name_two);
            current_opponet_level.setText(current_match_Opponent_name + "\n" + current_match_Opponent_name_three);


            if (!currentuser_img.equals("")) {
                Picasso.get().load(current_match_User_img).placeholder(R.drawable.male).into(currentuser_img);
                currentuser_img.setVisibility(View.VISIBLE);

            } else {
                currentuser_img.setVisibility(View.GONE);

            }


            if (!current_match_Opponent_img.equals("")) {
                Picasso.get().load(current_match_Opponent_img).placeholder(R.drawable.male).into(current_opponnent_img);
                current_opponnent_img.setVisibility(View.VISIBLE);

            } else {
                current_opponnent_img.setVisibility(View.GONE);

            }


            if (!current_match_Opponent_img_two.equals("")) {
                Picasso.get().load(current_match_Opponent_img_two).placeholder(R.drawable.male).into(group_a_member_two_img);
                group_a_member_two_img.setVisibility(View.VISIBLE);

            } else {
                group_a_member_two_img.setVisibility(View.GONE);

            }

            if (!current_match_Opponent_img_three.equals("")) {
                Picasso.get().load(current_match_Opponent_img_three).placeholder(R.drawable.male).into(group_b_member_two_img);
                group_b_member_two_img.setVisibility(View.VISIBLE);

            } else {
                group_b_member_two_img.setVisibility(View.GONE);

            }

        }

        tvSc.setText("SCORING " + "(" + current_game_type + ")");
//        tv2.setText(current_match_start_time + " - " + current_match_end_time);
        tvMatchDate.setText(current_match_Date);

        match_name_tv.setText(Constant.getGameNameFromcatId(current_match_cat_id));


        tvuser_minus_pnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user_points_counter > 0) {
                    if (totalGamePoints > 0) {
                        totalGamePoints--;
                        user_points_counter--;
                        Log.d("TOTALGAMEPOINTS", totalGamePoints + "-"+maxpoints);

                        tvuser_pnt.setText(user_points_counter + "");

                    }

                    if (totalGamePoints == maxpoints) {
                        if (user_points_counter > opponent_points_counter) {
                            lbl_user_win.setVisibility(View.VISIBLE);
                            lbl_opponet_win.setVisibility(View.GONE);
                        }else{
                            lbl_user_win.setVisibility(View.GONE);
                            lbl_opponet_win.setVisibility(View.VISIBLE);
                        }

                    }else{
                        lbl_user_win.setVisibility(View.GONE);
                        lbl_opponet_win.setVisibility(View.GONE);

                    }

                    if (opponent_points_counter == user_points_counter) {
                        lbl_user_win.setVisibility(View.GONE);
                        lbl_opponet_win.setVisibility(View.GONE);
                    }
                   /* if (user_points_counter < opponent_points_counter) {
                        lbl_user_win.setVisibility(View.GONE);
                        lbl_opponet_win.setVisibility(View.GONE);
                    }*/


                    /*else if (opponent_points_counter == user_points_counter) {
                        lbl_user_win.setVisibility(View.GONE);
                        lbl_opponet_win.setVisibility(View.GONE);
                    }*/

                }


            }
        });

        tvuser_pls_pnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_points_counter < maxpoints) {
                    if (totalGamePoints < maxpoints) {
                        totalGamePoints++;
                        user_points_counter++;
                        Log.d("TOTALGAMEPOINTS", totalGamePoints + "-"+maxpoints);
                        tvuser_pnt.setText(user_points_counter + "");


                    }


                    if (totalGamePoints==maxpoints){
                        if (user_points_counter > opponent_points_counter) {
                            lbl_user_win.setVisibility(View.VISIBLE);
                            lbl_opponet_win.setVisibility(View.GONE);

                        }else{
                            lbl_user_win.setVisibility(View.GONE);
                            lbl_opponet_win.setVisibility(View.VISIBLE);
                        }

                    }else {
                        lbl_user_win.setVisibility(View.GONE);
                        lbl_opponet_win.setVisibility(View.GONE);

                    }
                    if (opponent_points_counter == user_points_counter) {
                        lbl_user_win.setVisibility(View.GONE);
                        lbl_opponet_win.setVisibility(View.GONE);

                    }

                  /*  if (user_points_counter < opponent_points_counter) {
                        lbl_user_win.setVisibility(View.GONE);
                        lbl_opponet_win.setVisibility(View.GONE);

                    }*/
//                    else if (opponent_points_counter == user_points_counter) {
//                        lbl_user_win.setVisibility(View.GONE);
//                        lbl_opponet_win.setVisibility(View.GONE);
//
//                    }


                }


            }
        });


        opponent_minus_pnts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (opponent_points_counter > 0) {
                    if (totalGamePoints > 0) {
                        totalGamePoints--;
                        opponent_points_counter--;

                        Log.d("TOTALGAMEPOINTS", totalGamePoints + "-"+maxpoints);
                        tv_opponet_pnts.setText(opponent_points_counter + "");


                    }


                    if (totalGamePoints==maxpoints){

                        if (opponent_points_counter > user_points_counter) {
                            lbl_opponet_win.setVisibility(View.VISIBLE);
                            lbl_user_win.setVisibility(View.GONE);

                        }else{
                            lbl_user_win.setVisibility(View.VISIBLE);
                            lbl_opponet_win.setVisibility(View.GONE);
                        }


                    }else {
                        lbl_user_win.setVisibility(View.GONE);
                        lbl_opponet_win.setVisibility(View.GONE);

                    }
                    if (opponent_points_counter == user_points_counter) {
                        lbl_opponet_win.setVisibility(View.GONE);
                        lbl_user_win.setVisibility(View.GONE);

                    }

                  /*  if (opponent_points_counter < user_points_counter) {
                        lbl_opponet_win.setVisibility(View.GONE);
                        lbl_user_win.setVisibility(View.GONE);

                    }*/
                }


            }
        });

        tvopponent_plus_pnts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opponent_points_counter < maxpoints) {
                    if (totalGamePoints < maxpoints) {
                        totalGamePoints++;
                        opponent_points_counter++;

                        Log.d("TOTALGAMEPOINTS", totalGamePoints + "-"+maxpoints);
                        tv_opponet_pnts.setText(opponent_points_counter + "");


                    }

                    if (totalGamePoints==maxpoints){

                        if (opponent_points_counter > user_points_counter) {
                            lbl_opponet_win.setVisibility(View.VISIBLE);
                            lbl_user_win.setVisibility(View.GONE);

                        }else{
                            lbl_user_win.setVisibility(View.VISIBLE);
                            lbl_opponet_win.setVisibility(View.GONE);
                        }
                    }else {
                        lbl_user_win.setVisibility(View.GONE);
                        lbl_opponet_win.setVisibility(View.GONE);

                    }

                    if (opponent_points_counter == user_points_counter) {
                        lbl_opponet_win.setVisibility(View.GONE);
                        lbl_user_win.setVisibility(View.GONE);

                    }

                   /* if (opponent_points_counter < user_points_counter) {
                        lbl_opponet_win.setVisibility(View.GONE);
                        lbl_user_win.setVisibility(View.GONE);

                    }*/
                }

//
            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.save_score_btn:

                if (totalGamePoints == maxpoints) {

                    saveScore(user_points_counter, opponent_points_counter, SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_ID, ""), current_game_type, match_typeofGame);

                } else {
                    Toast.makeText(this, "Please Finish your game Score", Toast.LENGTH_LONG).show();
                }


                break;
        }
    }

public void scoresingleback(View view){
        finish();
}
    private void saveScore(int midpoint, int fidpoint, String matchid, String type, String mathtype) {
        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<SaveScoreResponse> callbackCall = apis.savescore(midpoint, fidpoint, matchid, type, mathtype);
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

        new TTFancyGifDialog.Builder(Activity_ScoreSingles.this)
                .setTitle("Success")
                .setMessage("Game Score Updated Successfully...")
                .setPositiveBtnText("Ok")
                .setPositiveBtnBackground("#3F51B5")
                .isCancellable(false)
                .setGifResource(R.drawable.success)      //pass your gif, png or jpg
                .isCancellable(true)
                .OnPositiveClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {


//                        Toast.makeText(Activity_ScoreSingles.this,"Ok",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_ScoreSingles.this, Activity_matchScore.class);
                        startActivity(intent);

                    }
                })

                .build();
    }
}
