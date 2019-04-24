package com.sportskonnect;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_Chat extends AppCompatActivity implements View.OnClickListener {

    Typeface face1;
    TextView main_text_title;
    ImageView btnBack , ivSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    private void init() {

        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");

        main_text_title = (TextView) findViewById(R.id.main_text_title);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        ivSend = (ImageView)findViewById(R.id.ivSend);
        btnBack.setOnClickListener(this);
        ivSend.setOnClickListener(this);
        main_text_title.setTypeface(face1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:

                Intent intent = new Intent(Activity_Chat.this, Activity_Badminton.class);
                startActivity(intent);
                finish();
                break;

            case R.id.ivSend:

                Intent intent1 = new Intent(Activity_Chat.this, Activity_matchScore.class);
                startActivity(intent1);
                finish();

                break;
        }
    }
}
