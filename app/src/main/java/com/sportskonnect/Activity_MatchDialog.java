//package com.sportskonnect;
//
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.Button;
//import android.widget.TextView;
//
//public class Activity_MatchDialog extends AppCompatActivity {
//
//    TextView et_ico , tvSingle , tvThree ,tvFive,tvPl;
//    Typeface face1;
//    Button btn_cancel,btn_buyBRNX;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_matchdialog);
//
//        init();
//    }
//
//    private void init() {
//
//        et_ico = (TextView)findViewById(R.id.et_ico);
//        tvSingle = (TextView)findViewById(R.id.tvSingle);
//        tvThree = (TextView)findViewById(R.id.tvThree);
//        tvPl = (TextView)findViewById(R.id.tvPl);
//        tvFive = (TextView)findViewById(R.id.tvFive);
//        btn_cancel = (Button) findViewById(R.id.btn_cancel);
//        btn_buyBRNX = (Button) findViewById(R.id.btn_buyBRNX);
//
//        Typeface face2 = Typeface.createFromAsset(getAssets(),
//                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");
//
//        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");
//
//        et_ico.setTypeface(face2);
//        tvPl.setTypeface(face2);
//        tvSingle.setTypeface(face1);
//        tvThree.setTypeface(face1);
//        tvFive.setTypeface(face1);
//        btn_cancel.setTypeface(face1);
//        btn_buyBRNX.setTypeface(face1);
//    }
//}
