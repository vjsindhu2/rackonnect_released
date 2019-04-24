package com.sportskonnect;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_Tour1 extends AppCompatActivity implements View.OnClickListener {

    TextView main_text_title;
    Typeface face;
    ImageView ivShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour1);

        init();
    }

    private void init() {

        main_text_title = (TextView)findViewById(R.id.main_text_title);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/Calibre Bold.otf");
        main_text_title.setTypeface(face);
        ivShare = (ImageView)findViewById(R.id.ivShare);
        ivShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivShare:

                Intent intent = new Intent(Activity_Tour1.this,Activity_RequestTour.class);
                startActivity(intent);
                break;
        }
    }
}
