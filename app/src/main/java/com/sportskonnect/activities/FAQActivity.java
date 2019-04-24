package com.sportskonnect.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.sportskonnect.R;

import androidx.appcompat.app.AppCompatActivity;

public class FAQActivity extends AppCompatActivity {

    WebView faqweb;
    LinearLayout back_btn;
    private ProgressDialog prDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        faqweb = findViewById(R.id.faqweb);
        back_btn = findViewById(R.id.back_btn);

        prDialog = new ProgressDialog(this);
        faqweb.loadUrl("file:///android_asset/faq.html");
        faqweb.getSettings().setJavaScriptEnabled(true);


        faqweb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Also you can show the progress percentage using integer value 'progress'
                if(!prDialog.isShowing()){
                    prDialog = ProgressDialog.show(FAQActivity.this, null, "loading, please wait...");
                }


                if (progress == 100&&prDialog.isShowing()) {
                    prDialog.dismiss();
                }
            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void finishfaqActivity(View view){
        finish();
    }




}
