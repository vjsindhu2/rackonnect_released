package com.sportskonnect;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_MatchScoringDoubles extends AppCompatActivity implements View.OnClickListener {

    TextView tvMin, tvName,tvPl;
    Typeface face1, face2;
    Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchscoringdoubles);

        init();
    }

    private void init() {

        tvMin = (TextView) findViewById(R.id.tvMin);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPl = (TextView) findViewById(R.id.tvPl);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");
        tvMin.setTypeface(face1);

        face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");
        tvName.setTypeface(face2);
        tvPl.setTypeface(face2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

                tvPl.setVisibility(View.VISIBLE);
                btnSubmit.setText("START SCORING");

                final Dialog dialog = new Dialog(Activity_MatchScoringDoubles.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v1 = li.inflate(R.layout.activity_matchdialog, null, false);
                dialog.setContentView(v1);
                dialog.setCancelable(true);

                TextView et_ico = (TextView) v1.findViewById(R.id.et_ico);
                TextView tvSingle = (TextView) v1.findViewById(R.id.tvSingle);
                TextView tvThree = (TextView) v1.findViewById(R.id.tvThree);
                TextView tvFive = (TextView) v1.findViewById(R.id.tvFive);
                et_ico.setTypeface(face2);
                tvSingle.setTypeface(face1);
                tvFive.setTypeface(face1);
                tvThree.setTypeface(face1);

                Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
                Button btn_buyBRNX = (Button) v1.findViewById(R.id.btn_buyBRNX);
                btn_cancel.setTypeface(face1);
                btn_buyBRNX.setTypeface(face1);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        tvPl.setVisibility(View.GONE);
                        btnSubmit.setText("GO TO HOMEPAGE");
                    }
                });

                btn_buyBRNX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        final Dialog dialog1 = new Dialog(Activity_MatchScoringDoubles.this);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        LayoutInflater li = (LayoutInflater) Activity_MatchScoringDoubles.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v1 = li.inflate(R.layout.activity_doubledialog, null, false);
                        dialog1.setContentView(v1);
                        dialog1.setCancelable(true);

                        Button btn_buyBRNX = (Button)v1.findViewById(R.id.btn_buyBRNX);
                        btn_buyBRNX.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Activity_MatchScoringDoubles.this,Activity_ScoreDouble.class);
                                startActivity(intent);
                            }
                        });

                        dialog1.show();
                        Window window = dialog1.getWindow();
                        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                break;
        }

    }
}
