package com.sportskonnect;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_RequestTour extends AppCompatActivity implements View.OnClickListener {

    TextView btnSubmit;
    Typeface face;
    LinearLayout llshare;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesttour);

        init();
    }

    private void init() {

        btnSubmit = (TextView)findViewById(R.id.btnSubmit);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/Calibre Bold.otf");

        btnSubmit.setTypeface(face);

        llshare = (LinearLayout)findViewById(R.id.llshare);
        llshare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llshare:

                Intent intent = new Intent(Activity_RequestTour.this,Activity_TourOver.class);
                startActivity(intent);
                break;
        }
    }
}
