package com.sportskonnect.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.sportskonnect.R;

public class TermsNconditions extends AppCompatActivity {

    WebView terms_webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_nconditions);
        terms_webview = findViewById(R.id.terms_webview);
        terms_webview.loadUrl("file:///android_asset/terms.html");
        terms_webview.getSettings().setJavaScriptEnabled(true);
    }



    public void finishTerms(View view){
        finish();
    }
}
