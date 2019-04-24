package com.sportskonnect;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_ScoreDouble extends AppCompatActivity implements View.OnClickListener {

    TextView tvSc , tvMin, tvName;
    Typeface face1,face2;
    ImageView ivC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoredouble);

        init();
    }

    private void init() {
        tvSc = (TextView)findViewById(R.id.tvSc);
        tvMin = (TextView) findViewById(R.id.tvMin);
        tvName = (TextView) findViewById(R.id.tvName);
        ivC = (ImageView) findViewById(R.id.ivC);
        ivC.setOnClickListener(this);

        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");
        tvSc.setTypeface(face1);
        face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");
        tvName.setTypeface(face2);
        tvMin.setTypeface(face1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivC:

                Intent intent = new Intent(Activity_ScoreDouble.this,Activity_ChatDoubles.class);
                startActivity(intent);
                break;
        }
    }
}
