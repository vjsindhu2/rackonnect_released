package com.sportskonnect;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_MatchDetails1 extends AppCompatActivity implements View.OnClickListener {

    TextView main_text_title;
    Typeface face1;
    ImageView ivShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchdetails1);

        init();
    }

    private void init() {

        main_text_title = (TextView) findViewById(R.id.main_text_title);
        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");
        main_text_title.setTypeface(face1);

        ivShare = (ImageView) findViewById(R.id.ivShare);
        ivShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivShare:
                Intent intent = new Intent(Activity_MatchDetails1.this, Activity_MatchDetail2.class);
                startActivity(intent);
                break;
        }
    }
}
