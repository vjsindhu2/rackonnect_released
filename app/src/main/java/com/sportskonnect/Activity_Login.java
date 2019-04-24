package com.sportskonnect;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.github.jorgecastillo.FillableLoader;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.sportskonnect.activities.TermsNconditions;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.LoginResponse;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.database.DatabaseHandler;
import com.sportskonnect.helpers.AppSignatureHelper;
import com.sportskonnect.helpers.PrefsHelper;
import com.sportskonnect.modal.SelectedGameModal;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Login extends AppCompatActivity implements View.OnClickListener, com.sportskonnect.Paths, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1;
    private final String TAG = Activity_Login.this.getClass().getName();
    TextView tvHead;
    EditText etMobile;
    String mobile;
    Button btnSubmit;
    CheckBox terms_check;
    FloatingActionButton fb, ivGoogle;
    ProgressDialog progressDialog;
    TextView terms_text_tv;
    String Facebook_email = "noemail";
    TextView main_text_title, give_us_a_review_landmine_text_2;
    String text = "<font color=#ffffff>RAC</font><font color=#00FF00>KONNECT</font>";
    Typeface face1;
    AppSignatureHelper appSignatureHelper;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build()
    );
    private DatabaseHandler db;
    private List<SelectedGameModal> gameinfoList = new ArrayList<>();
    private GoogleApiClient mGoogleApiClient;
    private SharedPreference sharedPreferences;
    private String socialloginType = "";
    private Dialog dialog;
    private Dialog loader_dialog;
    private Dialog validation_dialog;
    private int RESOLVE_HINT = 1000;

    private PhoneNumberUi ui;
    private PrefsHelper prefs;
    private boolean istermschecked = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        sharedPreferences = new SharedPreference(this);
        db = new DatabaseHandler(this);

appSignatureHelper = new AppSignatureHelper(this);
        ui = new PhoneNumberUi(findViewById(R.id.const_layout), "ActivityLogin");

        String defaultPhone = getPrefs().getPhoneNumber(null);
        if (defaultPhone != null) {
            ui.setPhoneNumber(defaultPhone);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        init();


//        loader_dialog(this);

    }

//    protected abstract String getActivityTitle();
//
//    protected abstract void doSubmit(String phoneValue);


    private void init() {


        main_text_title = (TextView) findViewById(R.id.main_text_title);
        terms_text_tv = (TextView) findViewById(R.id.terms_text_tv);
        terms_check = (CheckBox) findViewById(R.id.terms_check);
        give_us_a_review_landmine_text_2 = (TextView) findViewById(R.id.give_us_a_review_landmine_text_2);

        main_text_title.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Calibre Black.otf");

        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");

        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");

        give_us_a_review_landmine_text_2.setTypeface(face2);

        main_text_title.setTypeface(face);
//        tvHead.setTypeface(face1);


        fb = (FloatingActionButton) findViewById(R.id.ivFb);
        ivGoogle = (FloatingActionButton) findViewById(R.id.ivGoogle);


        tvHead = (TextView) findViewById(R.id.tvHead);
        etMobile = (EditText) findViewById(R.id.etMobile);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(face2);
        etMobile.setTypeface(face2);
        btnSubmit.setOnClickListener(this);
        fb.setOnClickListener(this);
        ivGoogle.setOnClickListener(this);

        terms_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    istermschecked = true;

                } else {
                    istermschecked = false;

                }
            }
        });


        terms_text_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Login.this,TermsNconditions.class);
                startActivity(intent);
            }
        });


//        doTask();
    }

    protected PrefsHelper getPrefs() {
        if (prefs == null) {
            prefs = new PrefsHelper(this);
        }
        return prefs;
    }







    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {

//        requestStoragePermission();
        super.onResume();
    }


    private void showHint() {
        ui.clearKeyboard();
        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent =
                Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.e("TAG", "Could not start hint picker Intent", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = "";
                String phone_number = "";
                String name = "";

                if (user.getPhoneNumber() != null) {
                    phone_number = user.getPhoneNumber();
                }
                if (user.getEmail() != null) {
                    email = user.getEmail();
                }
                if (user.getDisplayName() != null) {
                    name = user.getDisplayName();
                }

//                sharedPreferences.setSharedPreferenceBoolean(this, SharedprefKeys.ISLOGEDIN, true);
                sharedPreferences.setSharedPreferenceString(this, SharedprefKeys.USER_NAME, name);
                sharedPreferences.setSharedPreferenceString(this, SharedprefKeys.EMAIL_ID, email);
                sharedPreferences.setSharedPreferenceString(this, SharedprefKeys.MOBILE_NO, mobile);

                loginviasocial(email);


//                Intent intent= new Intent(LoginActivity.this,MainActivity.class);
//
//                runLoginSocialAPI(name, email, phone_number, socialloginType, "123456", "");
//
//                startActivity(intent);
                Log.d("USERDATA", email + "|" + phone_number + "|" + name);


            } else {


                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }

        } else if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                ui.setPhoneNumber(cred.getId().replace("+91", ""));


                Log.d("DATA", cred.getId());


            } else {
                ui.focusPhoneNumber();
            }
        }

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

                mobile = etMobile.getText().toString();

                if (!mobile.isEmpty()) {
                    if ((mobile.length() == 10)) {

                        if (istermschecked) {

                            loginviaotp(mobile,AppSignatureHelper.hashkey);

                        } else {

                            Toast.makeText(this, "Please check Terms and Conditions", Toast.LENGTH_LONG).show();
                        }


                    } else {

                        valiationDialogfn(this, "Please Enter 10 digits mobile number");


                    }
                } else {

                    valiationDialogfn(this, "Please Enter Mobile Number");

//                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.ivFb:

                if (istermschecked) {
                    fbsignin();

                } else {
                    Toast.makeText(this, "Please check Terms and Conditions", Toast.LENGTH_LONG).show();

                }
                break;

            case R.id.ivGoogle:

                if (istermschecked) {
                    signIn();

                } else {
                    Toast.makeText(this, "Please check Terms and Conditions", Toast.LENGTH_LONG).show();

                }

                break;
        }

    }

    private void signIn() {

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);

        socialloginType = "google";
    }


    private void fbsignin() {
        generatekeyhash();

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(

                                new AuthUI.IdpConfig.FacebookBuilder().build()

                        ))
                        .build(),
                RC_SIGN_IN);

        socialloginType = "facebook";
    }


    public void generatekeyhash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    private void loginviaotp(String mobile,String hashkey) {
        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<LoginResponse> callbackCall = apis.getlogin(mobile, hashkey);
        callbackCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {


                LoginResponse resp = response.body();
                assert resp != null;
                Log.d("LOGINRESPO", new Gson().toJson(resp));
                if (resp != null && !resp.getError()) {

                    if (loader_dialog != null) {
                        loader_dialog.dismiss();
                    }

                    if (resp.getStatus().equals("1") ) {

                        for (int i = 0; i < resp.getGameinfo().size(); i++) {

                            SelectedGameModal gameinfo = new SelectedGameModal(resp.getGameinfo().get(i).getCatId(), resp.getGameinfo().get(i).getLevelId());

                            gameinfoList.add(gameinfo);

                            db.saveSelectedgames(gameinfo);

                        }

                        SharedPreference.setSharedPreferenceString(Activity_Login.this,SharedprefKeys.USER_SELECTED_CAT, String.valueOf(resp.getGameinfo().get(0).getCatId()));
                        SharedPreference.setSharedPreferenceString(Activity_Login.this,SharedprefKeys.USER_SELECTED_LEVEL, String.valueOf(resp.getGameinfo().get(0).getLevelId()));

                    } else {

                    }

//                    sharedPreferences.setSharedPreferenceInt(Activity_Login.this, SharedprefKeys.CURRENT_OTP, resp.getOtp());
//                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.CURRENT_USER_STATUS, resp.getStatus());
//
//                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.USER_ID, resp.getId());
//
//                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.EMAIL_ID, resp.getEmail());
//                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.MOBILE_NO, resp.getMobileNumber());
//                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.USER_NAME, resp.getName());
//                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.PROFILE_PIC_URL, resp.getImage());
//                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.GENDER, resp.getGender());
//                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.PROFILE_COME_FROM, "fromlogin");


                    sharedPreferences.setSharedPreferenceInt(Activity_Login.this, SharedprefKeys.CURRENT_OTP, resp.getOtp());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.CURRENT_USER_STATUS, resp.getStatus());

                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.USER_ID, resp.getId());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.AGE, resp.getAge());

                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.EMAIL_ID, resp.getEmail());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.MOBILE_NO, resp.getMobileNumber());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.USER_NAME, resp.getName());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.PROFILE_PIC_URL, resp.getImage());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.GENDER, resp.getGender());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.PROFILE_COME_FROM, "fromlogin");

                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, resp.getGamenotipush());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, resp.getCancelnotipush());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, resp.getScorenotipush());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, resp.getGamenotiemail());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, resp.getCancelnotiemail());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, resp.getScorenotiemail());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, resp.getSound());
                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, resp.getChat());

                    sharedPreferences.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.USER_REFERAL_CODE, resp.getMyreferal());


                    Intent intent = new Intent(Activity_Login.this, Activity_Otp.class);
                    startActivity(intent);
                    finish();

//                    Toast.makeText(getApplicationContext(), String.valueOf(resp.getOtp()), Toast.LENGTH_LONG).show();


//                    dialogServerNotConnect();
                } else {
                    if (loader_dialog != null) {
                        loader_dialog.dismiss();
                    }

//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (loader_dialog != null) {
                    loader_dialog.dismiss();
                }

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void loginviasocial(String email) {

        loader_dialog(this);

        Apis apis = RestAdapter.createAPI();
        Call<LoginResponse> callbackCall = apis.sociallogin(email);
        callbackCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {


                LoginResponse resp = response.body();
                Log.d("LOGINRESPO", resp.toString());
                if (resp != null && !resp.getError()) {

                    if (loader_dialog != null) {
                        loader_dialog.dismiss();
                    }

                    if (resp.getStatus() != "0") {


                        for (int i = 0; i < resp.getGameinfo().size(); i++) {

                            SelectedGameModal gameinfo = new SelectedGameModal(resp.getGameinfo().get(i).getCatId(), resp.getGameinfo().get(i).getLevelId());

                            gameinfoList.add(gameinfo);

                            db.saveSelectedgames(gameinfo);

                        }
                    }

                    SharedPreference.setSharedPreferenceInt(Activity_Login.this, SharedprefKeys.CURRENT_OTP, resp.getOtp());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.CURRENT_USER_STATUS, resp.getStatus());

                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.USER_ID, resp.getId());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.AGE, resp.getAge());

                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.EMAIL_ID, resp.getEmail());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.MOBILE_NO, resp.getMobileNumber());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.USER_NAME, resp.getName());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.PROFILE_PIC_URL, resp.getImage());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.GENDER, resp.getGender());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.PROFILE_COME_FROM, "fromlogin");



                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, resp.getGamenotipush());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, resp.getCancelnotipush());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, resp.getScorenotipush());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, resp.getGamenotiemail());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, resp.getCancelnotiemail());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, resp.getScorenotiemail());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, resp.getSound());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, resp.getChat());
                    SharedPreference.setSharedPreferenceString(Activity_Login.this, SharedprefKeys.USER_REFERAL_CODE, resp.getMyreferal());


                    if (resp.getStatus().equals("0")) {
                        Intent intent = new Intent(Activity_Login.this, Activity_updateProfile.class);
                        startActivity(intent);
                        finish();

                    } else if (resp.getStatus().equals("1")) {
                        SharedPreference.setSharedPreferenceBoolean(Activity_Login.this, SharedprefKeys.ISLOGEDIN, true);
                        Intent intent = new Intent(Activity_Login.this, Activity_Badminton.class);
                        startActivity(intent);
                        finish();

                    } else {

                    }

//                    Toast.makeText(getApplicationContext(), String.valueOf(resp.getOtp()), Toast.LENGTH_LONG).show();


//                    dialogServerNotConnect();
                } else {
                    if (loader_dialog != null) {
                        loader_dialog.dismiss();
                    }

//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (loader_dialog != null) {
                    loader_dialog.dismiss();
                }

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
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


//        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
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

//        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loader_dialog.setContentView(view);
        final Window window = loader_dialog.getWindow();
        loader_dialog.setCancelable(false);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        loader_dialog.show();
    }


    public void valiationDialogfn(final Context context, String validation_msg) {
        validation_dialog = new Dialog(context);
        validation_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.validation_dilog_lyt, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/

        TextView validation_content = view.findViewById(R.id.validation_content);

        Button bt_positive = view.findViewById(R.id.bt_positive);
        ImageView validation_img = view.findViewById(R.id.validation_img);

        validation_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_mobile_validation));


        bt_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation_dialog.dismiss();
            }
        });

        validation_content.setText(validation_msg);
//        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        validation_dialog.setContentView(view);
        final Window window = validation_dialog.getWindow();
        validation_dialog.setCancelable(false);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        validation_dialog.show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    class PhoneNumberUi implements View.OnClickListener {
        private final FocusControl phoneFocus;
        private TextView title;
        private EditText phoneField;
        private Button submit;

        PhoneNumberUi(View root, String activityTitle) {
            title = (TextView) root.findViewById(R.id.tvHead);
            phoneField = (EditText) root.findViewById(R.id.etMobile);
            submit = (Button) root.findViewById(R.id.btnSubmit);
            phoneFocus = new FocusControl(phoneField);

//        title.setText(activityTitle);
            submit.setOnClickListener(this);
            phoneField.setOnClickListener(this);
            setSubmitEnabled(true);
        }

        @Override
        public void onClick(View view) {
            if (view.equals(submit)) {


//            doSubmit(getPhoneNumber());


            }
            if (view.equals(phoneField)) {
                phoneField.setEnabled(true);
                phoneField.requestFocus();
                if (TextUtils.isEmpty(getPhoneNumber())) {
                    showHint();
                }
            }
        }

        String getPhoneNumber() {
            return phoneField.getText().toString();
        }

        void setPhoneNumber(String phoneNumber) {
            phoneField.setText(phoneNumber);
        }

        void focusPhoneNumber() {
            phoneFocus.showKeyboard();
        }

        void clearKeyboard() {
            phoneFocus.hideKeyboard();
        }

        void setSubmitEnabled(boolean enabled) {
            submit.setEnabled(enabled);
        }
    }

    class FocusControl {
        static final int POST_DELAY = 250;
        private Handler handler;
        private InputMethodManager manager;
        private View focus;

        /**
         * Keyboard focus controller
         * <p>
         * Shows and hides the keyboard. Uses a runnable to do the showing as there are race
         * conditions with hiding the keyboard that this solves.
         *
         * @param focus The view element to focus and hide the keyboard from
         */
        public FocusControl(View focus) {
            handler = new Handler();
            manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            this.focus = focus;
        }

        /**
         * Focus the view and show the keyboard.
         */
        public void showKeyboard() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    focus.requestFocus();
                    manager.showSoftInput(focus, InputMethodManager.SHOW_IMPLICIT);
                }
            }, POST_DELAY);
        }

        /**
         * Hide the keyboard.
         */
        public void hideKeyboard() {
            View currentView = getCurrentFocus();
            if (currentView.equals(focus)) {
                manager.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
            }
        }


    }

}





