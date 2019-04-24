package com.sportskonnect;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sportskonnect.api.Constant;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_matchScore extends AppCompatActivity implements View.OnClickListener {

    TextView tvMin, tvName,tvPl,game_name_tv;
    Typeface face1, face2;
    Button btnSubmit;
    TextView confirmation_msg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchscore);

        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {

        tvMin = (TextView) findViewById(R.id.tvMin);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPl = (TextView) findViewById(R.id.tvPl);
        confirmation_msg = (TextView) findViewById(R.id.confirmation_msg);
        game_name_tv = (TextView) findViewById(R.id.game_name_tv);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");
        tvMin.setTypeface(face1);

        face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");
        tvName.setTypeface(face2);
        tvPl.setTypeface(face2);


        tvName.setText(SharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_NAME,"")+" - " +SharedPreference.getSharedPreferenceString(this,SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME,""));
        game_name_tv.setText(Constant.getGameNameFromcatId(SharedPreference.getSharedPreferenceString(this,SharedprefKeys.CURRENT_MATCH_CAT_ID,"")));

        confirmation_msg.setText(SharedPreference.getSharedPreferenceString(this,SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME,"")+" FOR VALIDATION");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

                Intent intent = new Intent(Activity_matchScore.this,Activity_Badminton.class);
                startActivity(intent);
                finish();

//                tvPl.setVisibility(View.VISIBLE);
//                btnSubmit.setText("START SCORING");
//
//                final Dialog dialog = new Dialog(Activity_matchScore.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View v1 = li.inflate(R.layout.activity_matchdialog, null, false);
//                dialog.setContentView(v1);
//                dialog.setCancelable(true);
//
//                TextView et_ico = (TextView) v1.findViewById(R.id.et_ico);
//                TextView tvSingle = (TextView) v1.findViewById(R.id.tvSingle);
//                TextView tvThree = (TextView) v1.findViewById(R.id.tvThree);
//                TextView tvFive = (TextView) v1.findViewById(R.id.tvFive);
//                et_ico.setTypeface(face2);
//                tvSingle.setTypeface(face1);
//                tvFive.setTypeface(face1);
//                tvThree.setTypeface(face1);
//
//                Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
//                Button btn_buyBRNX = (Button) v1.findViewById(R.id.btn_buyBRNX);
//                btn_cancel.setTypeface(face1);
//                btn_buyBRNX.setTypeface(face1);
//
//                btn_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        tvPl.setVisibility(View.GONE);
//                        btnSubmit.setText("GO TO HOMEPAGE");
//                    }
//                });
//
//                btn_buyBRNX.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                            dialog.dismiss();
//                            Intent intent = new Intent(Activity_matchScore.this,Activity_StartScoring.class);
//                            startActivity(intent);
//                    }
//                });
//
//                dialog.show();
//                Window window = dialog.getWindow();
//                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                break;
        }
    }
}
