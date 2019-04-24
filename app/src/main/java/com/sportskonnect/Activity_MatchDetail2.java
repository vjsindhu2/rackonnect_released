package com.sportskonnect;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_MatchDetail2 extends AppCompatActivity implements View.OnClickListener {

    TextView tvReq;
    TextView main_text_title;
    Typeface face1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchdetail2);

        init();
    }

    private void init() {

        main_text_title = (TextView) findViewById(R.id.main_text_title);
        tvReq = (TextView) findViewById(R.id.tvReq);
        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");
        main_text_title.setTypeface(face1);
        tvReq.setTypeface(face1);
        tvReq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvReq:

                Intent intent = new Intent(Activity_MatchDetail2.this,Activity_MatchDetails3.class);
                startActivity(intent);
                break;
        }
    }
}
