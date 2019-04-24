package com.sportskonnect;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastillo.FillableLoader;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.LoginResponse;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.helpers.AppSignatureHelper;
import com.sportskonnect.services.MySMSBroadcastReceiver;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;

public class Activity_Otp extends AppCompatActivity implements View.OnClickListener {

    String mobile, numGet;
    TextView Tvphn, tvHead;
    Button btnSubmit;
    public static EditText etMobile;
    TextView main_text_title, give_us_a_review_landmine_text_2, tvResend;
    String text = "<font color=#ffffff>RAC</font><font color=#00FF00>KONNECT</font>";


    public static String OTPVAl = "";
    private SharedPreference sharedPreferences;
    private Dialog dialog;
    private Dialog loader_dialog;
    private SmsVerifyCatcher smsVerifyCatcher;
    private MySMSBroadcastReceiver mySMSBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        sharedPreferences = new SharedPreference(this);

        mySMSBroadcastReceiver = new MySMSBroadcastReceiver();
        init();


        SmsRetrieverClient client = SmsRetriever.getClient(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mySMSBroadcastReceiver, intentFilter);


        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task.
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Void aVoid) {



            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Failed to start retriever");
            }
        });


    }

    private void init() {

        main_text_title = (TextView) findViewById(R.id.main_text_title);
        tvHead = (TextView) findViewById(R.id.tvHead);
        give_us_a_review_landmine_text_2 = (TextView) findViewById(R.id.give_us_a_review_landmine_text_2);
        tvResend = (TextView) findViewById(R.id.tvResend);

        main_text_title.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Calibre Bold.otf");

        Typeface face1 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Regular.otf");

        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");
        main_text_title.setTypeface(face);
        tvHead.setTypeface(face1);

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            mobile = bundle.getString("mobile");
//            Log.d("mobileGet", mobile);
//        }

        mobile = sharedPreferences.getSharedPreferenceString(this, SharedprefKeys.MOBILE_NO, "");

        Tvphn = (TextView) findViewById(R.id.Tvphn);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        etMobile = (EditText) findViewById(R.id.etMobile);
        btnSubmit.setOnClickListener(this);
        Tvphn.setText("+91" + " " + mobile);
        Tvphn.setTypeface(face1);
        etMobile.setTypeface(face2);
        btnSubmit.setTypeface(face2);
        give_us_a_review_landmine_text_2.setTypeface(face2);
        tvResend.setTypeface(face2);

//
//        try{
//            etMobile.setText(String.valueOf(sharedPreferences.getSharedPreferenceInt(Activity_Otp.this, SharedprefKeys.CURRENT_OTP,0)));
//
//
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }

        tvResend.setOnClickListener(this);


//        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
//            @Override
//            public void onSmsCatch(String message) {
//                String code = parseCode(message);//Parse verification code
//                etMobile.setText(code);//set code in edit text
//                //then you can send verification code to server
//            }
//        });


        new CountDownTimer(30000, 1000) { // adjust the milli seconds here

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                give_us_a_review_landmine_text_2.setText("" + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
//                _tv.setText("done!");
            }
        }.start();

    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        smsVerifyCatcher.onStop();

        if (mySMSBroadcastReceiver != null) {
            getApplicationContext().unregisterReceiver(mySMSBroadcastReceiver);
            mySMSBroadcastReceiver = null;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSubmit:

                numGet = etMobile.getText().toString().trim();


                if (!numGet.isEmpty()) {

                    if (Integer.parseInt(numGet) == sharedPreferences.getSharedPreferenceInt(Activity_Otp.this, SharedprefKeys.CURRENT_OTP, 0)) {


                        if (sharedPreferences.getSharedPreferenceString(Activity_Otp.this, SharedprefKeys.CURRENT_USER_STATUS, "").equals("0")) {
                            Intent intent = new Intent(Activity_Otp.this, Activity_updateProfile.class);
                            startActivity(intent);
                            finish();

                        } else if (sharedPreferences.getSharedPreferenceString(Activity_Otp.this, SharedprefKeys.CURRENT_USER_STATUS, "").equals("1")) {
                            sharedPreferences.setSharedPreferenceBoolean(Activity_Otp.this, SharedprefKeys.ISLOGEDIN, true);
                            Intent intent = new Intent(Activity_Otp.this, Activity_Badminton.class);
                            startActivity(intent);
                            finish();

                        } else {

                        }


                    } else {

                        Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.tvResend:

                loginviaotp(mobile);

                break;
        }
    }


    private void loginviaotp(String mobile) {

        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<LoginResponse> callbackCall = apis.getlogin(mobile,AppSignatureHelper.hashkey);
        callbackCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse resp = response.body();
                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();

                    sharedPreferences.setSharedPreferenceInt(Activity_Otp.this, SharedprefKeys.CURRENT_OTP, resp.getOtp());

                    sharedPreferences.setSharedPreferenceString(Activity_Otp.this, SharedprefKeys.USER_ID, resp.getId());

                    sharedPreferences.setSharedPreferenceString(Activity_Otp.this, SharedprefKeys.EMAIL_ID, resp.getEmail());
                    sharedPreferences.setSharedPreferenceString(Activity_Otp.this, SharedprefKeys.MOBILE_NO, resp.getMobileNumber());
                    sharedPreferences.setSharedPreferenceString(Activity_Otp.this, SharedprefKeys.USER_NAME, resp.getName());
                    sharedPreferences.setSharedPreferenceString(Activity_Otp.this, SharedprefKeys.PROFILE_PIC_URL, resp.getImage());
                    sharedPreferences.setSharedPreferenceString(Activity_Otp.this, SharedprefKeys.GENDER, resp.getGender());


//                    etMobile.setText(String.valueOf(String.valueOf(resp.getOtp())));


//                    Toast.makeText(getApplicationContext(), String.valueOf(resp.getOtp()), Toast.LENGTH_LONG).show();


//                    dialogServerNotConnect();
                } else {
                    loader_dialog.dismiss();
                    dialogServerNotConnect(Activity_Otp.this, "NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
                dialogServerNotConnect(Activity_Otp.this, t.getMessage());
            }
        });
    }


    public void dialogServerNotConnect(final Context context, final String needed_money) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_info, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/

//        final EditText et_money = view.findViewById(R.id.et_money);
//        final LinearLayout offerWindow = view.findViewById(R.id.offerWindow);
//        final TextView offer = view.findViewById(R.id.offer);
//        final TextView totalWallet_Amount = view.findViewById(R.id.totalWallet_Amount);
//        final TextView minium_amt = view.findViewById(R.id.minium_amt);


        ((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }


    public void loader_dialog(final Context context) {
        loader_dialog = new Dialog(context);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progress_dilog_lyt, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/

        FillableLoader fillableLoader = view.findViewById(R.id.fillableLoader);

        fillableLoader.setSvgPath(MODAL);

        fillableLoader.start();

        ((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loader_dialog.setContentView(view);
        final Window window = loader_dialog.getWindow();
        loader_dialog.setCancelable(false);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        loader_dialog.show();
    }


}
