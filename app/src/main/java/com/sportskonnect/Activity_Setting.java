package com.sportskonnect;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.jorgecastillo.FillableLoader;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.CreateMatchRespo;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.database.DatabaseHandler;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;

public class Activity_Setting extends AppCompatActivity implements View.OnClickListener {

    Button btnPush_notification, btnEmail_notification, btnmatch_pushnoti, btnmatch_email_pushnoti;

    Button scoringpush_noti, score_email_noti;
    TextView main_text_title, tvRateUs;
    Typeface face1;
    TextView tvDel;
    private String gamenotipush = "";
    private String cancelnotipush = "";
    private String scorenotipush = "";
    private String gamenotiemail = "";
    private String cancelnotiemail = "";
    private String scorenotiemail = "";
    private String sound = "";
    private String chat = "";
    private boolean isgamepushnoti = false;
    private boolean iscancelgamepushnoti = false;
    private boolean isScorepushnoti = false;
    private boolean isgamenotiEmail = false;
    private boolean isCancelnotiemail = false;
    private boolean isScorenotiemail = false;

    private ToggleButton notification_toggle, chat_toggle;
    private boolean issoundNoti = false;
    private boolean ischatNoti = false;
    private String tokenID = "";

    private DatabaseHandler db;
    private String user_id = "";
    private Dialog loader_dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    private void init() {

        db = new DatabaseHandler(this);
        btnPush_notification = (Button) findViewById(R.id.btnPush_notification);
        btnEmail_notification = (Button) findViewById(R.id.btnEmail_notification);
        btnmatch_pushnoti = (Button) findViewById(R.id.btnmatch_pushnoti);
        btnmatch_email_pushnoti = (Button) findViewById(R.id.btnmatch_email_pushnoti);
        score_email_noti = (Button) findViewById(R.id.score_email_noti);
        tvRateUs = (TextView) findViewById(R.id.tvRateUs);

        scoringpush_noti = (Button) findViewById(R.id.scoringpush_noti);
        tvDel = findViewById(R.id.tvDel);
        notification_toggle = findViewById(R.id.notification_toggle);
        chat_toggle = findViewById(R.id.chat_toggle);
        main_text_title = (TextView) findViewById(R.id.main_text_title);
        face1 = Typeface.createFromAsset(getAssets(), "fonts/" + "Calibre Bold.otf");
        main_text_title.setTypeface(face1);

        btnPush_notification.setOnClickListener(this);
        btnEmail_notification.setOnClickListener(this);
        btnmatch_pushnoti.setOnClickListener(this);
        score_email_noti.setOnClickListener(this);
        btnmatch_email_pushnoti.setOnClickListener(this);
        scoringpush_noti.setOnClickListener(this);
        score_email_noti.setOnClickListener(this);

        tvDel.setOnClickListener(this);

        user_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");

        if (tokenID == null || tokenID.equals("")) {
            tokenID = FirebaseInstanceId.getInstance().getToken();
        }


        gamenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
        cancelnotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
        scorenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
        gamenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
        cancelnotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
        scorenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
        sound = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
        chat = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


        tvRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + Activity_Setting.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + Activity_Setting.this.getPackageName())));
                }
            }
        });

        if (gamenotipush.equals("0")) {
            isgamepushnoti = true;
            btnPush_notification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnPush_notification.setTextColor(getResources().getColor(R.color.white));
        } else {
            isgamepushnoti = false;
            btnPush_notification.setBackgroundColor(getResources().getColor(R.color.color_gray));
            btnPush_notification.setTextColor(getResources().getColor(R.color.colorPrimary));

        }

        if (cancelnotipush.equals("0")) {
            iscancelgamepushnoti = true;
            btnmatch_pushnoti.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnmatch_pushnoti.setTextColor(getResources().getColor(R.color.white));
        } else {
            iscancelgamepushnoti = false;
            btnmatch_pushnoti.setBackgroundColor(getResources().getColor(R.color.color_gray));
            btnmatch_pushnoti.setTextColor(getResources().getColor(R.color.colorPrimary));

        }

        if (scorenotipush.equals("0")) {
            isScorepushnoti = true;
            scoringpush_noti.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            scoringpush_noti.setTextColor(getResources().getColor(R.color.white));
        } else {
            isScorepushnoti = false;
            scoringpush_noti.setBackgroundColor(getResources().getColor(R.color.color_gray));
            scoringpush_noti.setTextColor(getResources().getColor(R.color.colorPrimary));

        }

        if (gamenotiemail.equals("0")) {
            isgamenotiEmail = true;
            btnEmail_notification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnEmail_notification.setTextColor(getResources().getColor(R.color.white));
        } else {
            isgamenotiEmail = false;
            btnEmail_notification.setBackgroundColor(getResources().getColor(R.color.color_gray));
            btnEmail_notification.setTextColor(getResources().getColor(R.color.colorPrimary));

        }

        if (cancelnotiemail.equals("0")) {
            isCancelnotiemail = true;
            btnmatch_email_pushnoti.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnmatch_email_pushnoti.setTextColor(getResources().getColor(R.color.white));
        } else {
            isCancelnotiemail = false;
            btnmatch_email_pushnoti.setBackgroundColor(getResources().getColor(R.color.color_gray));
            btnmatch_email_pushnoti.setTextColor(getResources().getColor(R.color.colorPrimary));

        }

        if (scorenotiemail.equals("0")) {
            isScorenotiemail = true;
            score_email_noti.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            score_email_noti.setTextColor(getResources().getColor(R.color.white));
        } else {
            isScorenotiemail = false;
            score_email_noti.setBackgroundColor(getResources().getColor(R.color.color_gray));
            score_email_noti.setTextColor(getResources().getColor(R.color.colorPrimary));

        }


        if (sound.equals("0")) {
            issoundNoti = true;
            notification_toggle.setChecked(true);

        } else {
            issoundNoti = false;
            notification_toggle.setChecked(false);
        }

        if (chat.equals("0")) {
            ischatNoti = true;
            chat_toggle.setChecked(true);

        } else {
            ischatNoti = false;
            chat_toggle.setChecked(false);

        }


        chat_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chat_toggle.isChecked()) {

                    ischatNoti = true;
                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "1");


                    gamenotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
                    cancelnotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
                    scorenotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
                    gamenotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
                    cancelnotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
                    scorenotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
                    sound = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
                    chat = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                    addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);

                } else {

                    ischatNoti = false;
                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                    gamenotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
                    cancelnotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
                    scorenotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
                    gamenotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
                    cancelnotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
                    scorenotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
                    sound = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
                    chat = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                    addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);

                }
            }
        });

        notification_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (notification_toggle.isChecked()) {

                    issoundNoti = true;
                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "1");

                    gamenotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
                    cancelnotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
                    scorenotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
                    gamenotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
                    cancelnotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
                    scorenotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
                    sound = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
                    chat = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                    addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);


                } else {

                    issoundNoti = false;
                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");


                    gamenotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
                    cancelnotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
                    scorenotipush = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
                    gamenotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
                    cancelnotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
                    scorenotiemail = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
                    sound = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
                    chat = SharedPreference.getSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                    addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);

                }
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPush_notification:

                if (isgamepushnoti) {
                    isgamepushnoti = false;
                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "1");

                    btnPush_notification.setBackgroundColor(getResources().getColor(R.color.color_gray));
                    btnPush_notification.setTextColor(getResources().getColor(R.color.colorPrimary));

                } else {

                    isgamepushnoti = true;
                    btnPush_notification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnPush_notification.setTextColor(getResources().getColor(R.color.white));


                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");

                }


                gamenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
                cancelnotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
                scorenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
                gamenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
                cancelnotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
                scorenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
                sound = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
                chat = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);


                break;
            case R.id.btnEmail_notification:

                if (isgamenotiEmail) {
                    isgamenotiEmail = false;
                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "1");

                    btnEmail_notification.setBackgroundColor(getResources().getColor(R.color.color_gray));
                    btnEmail_notification.setTextColor(getResources().getColor(R.color.colorPrimary));

                } else {

                    isgamenotiEmail = true;
                    btnEmail_notification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEmail_notification.setTextColor(getResources().getColor(R.color.white));


                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");


                }


                gamenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
                cancelnotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
                scorenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
                gamenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
                cancelnotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
                scorenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
                sound = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
                chat = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);


                break;
            case R.id.btnmatch_pushnoti:

                if (iscancelgamepushnoti) {
                    iscancelgamepushnoti = false;
                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "1");

                    btnmatch_pushnoti.setBackgroundColor(getResources().getColor(R.color.color_gray));
                    btnmatch_pushnoti.setTextColor(getResources().getColor(R.color.colorPrimary));

                } else {

                    iscancelgamepushnoti = true;
                    btnmatch_pushnoti.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnmatch_pushnoti.setTextColor(getResources().getColor(R.color.white));


                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");


                }

                gamenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
                cancelnotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
                scorenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
                gamenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
                cancelnotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
                scorenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
                sound = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
                chat = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);


                break;
            case R.id.btnmatch_email_pushnoti:
                if (isCancelnotiemail) {
                    isCancelnotiemail = false;
                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "1");

                    btnmatch_email_pushnoti.setBackgroundColor(getResources().getColor(R.color.color_gray));
                    btnmatch_email_pushnoti.setTextColor(getResources().getColor(R.color.colorPrimary));

                } else {

                    isCancelnotiemail = true;
                    btnmatch_email_pushnoti.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnmatch_email_pushnoti.setTextColor(getResources().getColor(R.color.white));


                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");

                }


                gamenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
                cancelnotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
                scorenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
                gamenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
                cancelnotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
                scorenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
                sound = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
                chat = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);


                break;

            case R.id.scoringpush_noti:

                if (isScorepushnoti) {
                    isScorepushnoti = false;
                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "1");

                    scoringpush_noti.setBackgroundColor(getResources().getColor(R.color.color_gray));
                    scoringpush_noti.setTextColor(getResources().getColor(R.color.colorPrimary));

                } else {

                    isScorepushnoti = true;
                    scoringpush_noti.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    scoringpush_noti.setTextColor(getResources().getColor(R.color.white));


                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");

                }


                gamenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
                cancelnotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
                scorenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
                gamenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
                cancelnotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
                scorenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
                sound = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
                chat = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);

                break;
            case R.id.score_email_noti:

                if (isScorenotiemail) {
                    isScorenotiemail = false;
                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "1");

                    score_email_noti.setBackgroundColor(getResources().getColor(R.color.color_gray));
                    score_email_noti.setTextColor(getResources().getColor(R.color.colorPrimary));

                } else {

                    isScorenotiemail = true;
                    score_email_noti.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    score_email_noti.setTextColor(getResources().getColor(R.color.white));


                    SharedPreference.setSharedPreferenceString(Activity_Setting.this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");

                }


                gamenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_GAMENOTIFICATION_PUSH, "0");
                cancelnotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCLENOTIFICATION_PUSH, "0");
                scorenotipush = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORENOTIFICATION_PUSH, "0");
                gamenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_EMAILNOTIFICATION_PUSH, "0");
                cancelnotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CANCEL_EMAIL_NOTIFICATION_PUSH, "0");
                scorenotiemail = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SCORE_EMAIL_NOTIFICATION_PUSH, "0");
                sound = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_SOUND_NOTIFICATION_PUSH, "0");
                chat = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.APP_SETTING_CHAT_NOTIFICATION_PUSH, "0");


                addtokentoserver(user_id, tokenID, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);


                break;


            case R.id.tvDel:


                confirmdialog();

                break;
        }
    }

    public void finishSetting(View view) {
        finish();

    }


    private void addtokentoserver(String user_id, String token, String gamenotipush, String cancelnotipush, String scorenotipush, String gamenotiemail, String cancelnotiemail, String scorenotiemail, String sound, String chat) {

        Apis apis = RestAdapter.createAPI();
        Call<CreateMatchRespo> callbackCall = apis.setToken(user_id, token, gamenotipush, cancelnotipush, scorenotipush, gamenotiemail, cancelnotiemail, scorenotiemail, sound, chat);
        callbackCall.enqueue(new Callback<CreateMatchRespo>() {
            @Override
            public void onResponse(Call<CreateMatchRespo> call, Response<CreateMatchRespo> response) {


                CreateMatchRespo resp = response.body();
//                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {


//                    dialogServerNotConnect();
                } else {


                }
            }

            @Override
            public void onFailure(Call<CreateMatchRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void deleteAccount(String user_id) {

        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<CreateMatchRespo> callbackCall = apis.delAccount(user_id);
        callbackCall.enqueue(new Callback<CreateMatchRespo>() {
            @Override
            public void onResponse(Call<CreateMatchRespo> call, Response<CreateMatchRespo> response) {


                CreateMatchRespo resp = response.body();
//                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();
                    SharedPreference.clearallPrefs(Activity_Setting.this);

                    db.deleteActiveCart();
                    db.close();


                    Intent intent = new Intent(Activity_Setting.this, Activity_Splash.class);

                    startActivity(intent);

                    finishAffinity();


//                    dialogServerNotConnect();
                } else {
                    loader_dialog.dismiss();
                    delAcWarning(resp.getErrorMsg());

                }
            }

            @Override
            public void onFailure(Call<CreateMatchRespo> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void confirmdialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialog_forsurity, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);


        TextView msg = (TextView) v1.findViewById(R.id.msg);
        msg.setText("Are you sure? Do you want to Delete Account...");
        Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) v1.findViewById(R.id.btn_submit);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                tvPl.setVisibility(View.GONE);
//                btnSubmit.setText("GO TO HOMEPAGE");
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                deleteAccount(user_id);
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    private void delAcWarning(String msg_str) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.delwarningdialog, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);


        TextView msg = (TextView) v1.findViewById(R.id.msg);
        msg.setText(msg_str);
        Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) v1.findViewById(R.id.btn_submit);

        btn_submit.setText("OK");
        btn_cancel.setVisibility(View.GONE);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                tvPl.setVisibility(View.GONE);
//                btnSubmit.setText("GO TO HOMEPAGE");
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
}
