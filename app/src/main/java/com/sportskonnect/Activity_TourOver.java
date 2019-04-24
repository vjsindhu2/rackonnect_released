package com.sportskonnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_TourOver extends AppCompatActivity implements View.OnClickListener {

    TextView tvOver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourover);

        init();
    }

    private void init() {
        tvOver = (TextView)findViewById(R.id.tvOver);
        tvOver.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvOver:
                Intent intent = new Intent(Activity_TourOver.this,Activity_TourList.class);
                startActivity(intent);
                break;
        }
    }
}
