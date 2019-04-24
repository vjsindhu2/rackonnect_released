package com.sportskonnect;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.github.jorgecastillo.FillableLoader;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.sportskonnect.activities.SearchAddressActivity;
import com.sportskonnect.adapters.SelectedGamesAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.CreateMatchRespo;
import com.sportskonnect.api.Callbacks.MatchtourPercentRespo;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.JSONParser;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.database.DatabaseHandler;
import com.sportskonnect.modal.PlaceBean;
import com.sportskonnect.modal.SelectedGameModal;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.sportskonnect.utility.RecyclerTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.sportskonnect.Paths.MODAL;

public class Activity_TourForm extends AppCompatActivity implements View.OnClickListener, PaytmPaymentTransactionCallback {

    private static final int REQ_SEARCH_HOME = 0;
    final Calendar myCalendar = Calendar.getInstance();
    Button btnCreateTour;
    EditText etLocation;
    private PlaceBean homeBean;
    private CrystalSeekbar rangeSeekbar3;
    private TextView player_count_tv;
    private TextView players_tv;
    private TextView maxplayer_tv;
    private TextView total_reg_fee;
    private EditText etREGISTRATIONFEES, etFIRSTPRIZE, etSECONDPRIZE, etTOURNAMENTDATE, etTOURNAMENTTIME, etmatchType;
    private int no_of_player = 4;
    private TextView company_comission_tv, host_comission_tv;
    private int regfee = 0;
    private int firstprizeMoney = 0;
    private int secondprize = 0;
    private int company_comission = 0;
    private int host_comission = 0;
    private int fees = 0;
    private int startTimeselectedHour = 0;
    private int starttiemselectedmin = 0;
    private String _24hourstarttime;
    private AwesomeValidation mAwesomeValidation;
    private Dialog choosegame_dialog;
    private SelectedGamesAdapter mAdapter;
    private DatabaseHandler db;
    private TextView winner_percent_lbl, runnerup_percent_lbl;
    private int selected_cat = 0;
    private TextView company_percent_lbl;
    private int winnerpercent = 0;
    private int runnerpercent = 0;
    private int company_percent = 0;
    private int host_percent = 0;
    private Dialog loader_dialog;

    private EditText et_matchname;
    private String user_id = "";
    private String user_id_level = "";
    private String orderId = "";
    private String custId = "";
    private String mid = "TGMznm39416586320966";
    private String serverOrderId = "";
    private PopupWindow popupWindow;

    public static void hideSoftKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showSoftKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInputFromInputMethod(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourform);

        db = new DatabaseHandler(this);

        mAwesomeValidation = new AwesomeValidation(BASIC);

        AwesomeValidation.disableAutoFocusOnFirstFailure();


        init();
    }

    private void init() {

        btnCreateTour = (Button) findViewById(R.id.btnCreateTour);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etREGISTRATIONFEES = (EditText) findViewById(R.id.etREGISTRATIONFEES);
        etFIRSTPRIZE = (EditText) findViewById(R.id.etFIRSTPRIZE);
        etSECONDPRIZE = (EditText) findViewById(R.id.etSECONDPRIZE);
        etTOURNAMENTDATE = (EditText) findViewById(R.id.etTOURNAMENTDATE);
        etTOURNAMENTTIME = (EditText) findViewById(R.id.etTOURNAMENTTIME);
        etmatchType = (EditText) findViewById(R.id.etmatchType);
        et_matchname = (EditText) findViewById(R.id.et_matchname);
        rangeSeekbar3 = (CrystalSeekbar) findViewById(R.id.rangeSeekbar3);
        player_count_tv = (TextView) findViewById(R.id.player_count_tv);
        total_reg_fee = (TextView) findViewById(R.id.total_reg_fee);
        runnerup_percent_lbl = (TextView) findViewById(R.id.runnerup_percent_lbl);
        company_percent_lbl = (TextView) findViewById(R.id.company_percent_lbl);
        winner_percent_lbl = (TextView) findViewById(R.id.winner_percent_lbl);
        players_tv = (TextView) findViewById(R.id.players_tv);
        maxplayer_tv = (TextView) findViewById(R.id.maxplayer_tv);
        company_comission_tv = (TextView) findViewById(R.id.company_comission_tv);
        host_comission_tv = (TextView) findViewById(R.id.host_comission_tv);
        btnCreateTour.setOnClickListener(this);
        etLocation.setOnClickListener(this);

        selected_cat = Integer.parseInt(SharedPreference.getSharedPreferenceString(Activity_TourForm.this, SharedprefKeys.USER_SELECTED_CAT, "0"));
        etmatchType.setText(Constant.getGameNameFromcatId(String.valueOf(selected_cat)));

        user_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");
        user_id_level = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_LEVEL, "");


        custId = "customer" + getRandomNumber(5, 100) + SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");
        orderId = "order" + getRandomNumber(5, 100) + SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");

        serverOrderId = orderId;

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_matchname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftKeyboard(Activity_TourForm.this);
            }
        });

        etTOURNAMENTDATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(Activity_TourForm.this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);
//                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 2*24*60*60*1000);
                dialog.show();
            }
        });


        etmatchType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosegameDialog(Activity_TourForm.this, "");
            }
        });

        etTOURNAMENTTIME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeFragment();
            }
        });

        rangeSeekbar3.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                player_count_tv.setText("" + value + "");
//                players_tv.setText("" + minValue + " players");
//                maxplayer_tv.setText("" + maxValue + " players");

                if (regfee != 0) {

                    regfee = fees * no_of_player;


                    if (winnerpercent != 0 && runnerpercent != 0 && company_percent != 0 && host_percent != 0) {

                        firstprizeMoney = (regfee * winnerpercent) / 100;
                        secondprize = (regfee * runnerpercent) / 100;
                        company_comission = (regfee * company_percent) / 100;
                        host_comission = (regfee * host_percent) / 100;


                    }

//               etFIRSTPRIZE.setText();
                    total_reg_fee.setText("Rs. " + regfee);

                    etFIRSTPRIZE.setText("Rs. " + firstprizeMoney + "");
                    etSECONDPRIZE.setText("Rs. " + secondprize);
                    company_comission_tv.setText("Rs. " + company_comission + "");
                    host_comission_tv.setText("Rs. " + host_comission + "");
                }


            }
        });


// set final value listener
        rangeSeekbar3.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {

                no_of_player = Integer.parseInt(String.valueOf(value));

            }
        });


        etREGISTRATIONFEES.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals("")) {
                    regfee = Integer.parseInt(s.toString()) * no_of_player;

                    fees = Integer.parseInt(s.toString());
                    if (winnerpercent != 0 && runnerpercent != 0 && company_percent != 0 && host_percent != 0) {

                        firstprizeMoney = (regfee * winnerpercent) / 100;
                        secondprize = (regfee * runnerpercent) / 100;
                        company_comission = (regfee * company_percent) / 100;
                        host_comission = (regfee * host_percent) / 100;


                    }

//               etFIRSTPRIZE.setText();
                    total_reg_fee.setText("Rs. " + regfee);

                    etFIRSTPRIZE.setText("Rs. " + firstprizeMoney + "");
                    etSECONDPRIZE.setText("Rs. " + secondprize);
                    company_comission_tv.setText("Rs. " + company_comission + "");
                    host_comission_tv.setText("Rs. " + host_comission + "");


                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        fetchtourpercentages();


    }

    public void finishtourform(View view) {
        finish();
    }

    public void choosegameDialog(final Context context, String validation_msg) {
        choosegame_dialog = new Dialog(context);
        choosegame_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.choosegame_dilog_lyt, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/

        RecyclerView selected_game_list_rv = view.findViewById(R.id.selected_game_list_rv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        selected_game_list_rv.setLayoutManager(mLayoutManager);
        selected_game_list_rv.setItemAnimator(new DefaultItemAnimator());
        selected_game_list_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        mAdapter = new SelectedGamesAdapter(this, db.getActiveCartList());
        selected_game_list_rv.setAdapter(mAdapter);


        selected_game_list_rv.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), selected_game_list_rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SelectedGameModal selectedGameModal = db.getActiveCartList().get(position);
//                Toast.makeText(getApplicationContext(), selectedGameModal.getCatId() + " is selected!", Toast.LENGTH_SHORT).show();


                if (selectedGameModal.getCatId() == 1) {

                    etmatchType.setText("SQUASH");
                    selected_cat = 1;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "1");


                } else if (selectedGameModal.getCatId() == 2) {

                    etmatchType.setText("TABLE TENNIS");
                    selected_cat = 2;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "2");


                } else if (selectedGameModal.getCatId() == 3) {

                    etmatchType.setText("BADMINTON");
                    selected_cat = 3;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "3");


                } else if (selectedGameModal.getCatId() == 4) {

                    etmatchType.setText("TENNIS");
                    selected_cat = 4;
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "4");

                } else {
                    etmatchType.setText("Select Game");
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "");


                }


                choosegame_dialog.dismiss();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


//        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        choosegame_dialog.setContentView(view);
        final Window window = choosegame_dialog.getWindow();
        choosegame_dialog.setCancelable(true);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        choosegame_dialog.show();


    }


    void openTimeFragment() {
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog mTimePicker;

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        Date today = c.getTime();


        mTimePicker = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
                        String time = hourOfDay + ":" + minute;

                        startTimeselectedHour = hourOfDay;
                        starttiemselectedmin = minute;

                        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                        Date date = null;
                        try {
                            date = fmt.parse(time);

                            _24hourstarttime = fmt.format(date);


                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                        String formattedTime = fmtOut.format(date);


                        if (myCalendar.getTime().after(today)) {

                            etTOURNAMENTTIME.setText(formattedTime);

                        } else {


                            if (hourOfDay >= hour && minute >= minute) {

                                etTOURNAMENTTIME.setText(formattedTime);
                            } else {
                                //Display a toast or something to inform the user that he can't pick a past time.


                                showErrorPopup(etTOURNAMENTTIME, "You can't select past time");

//                        Toast.makeText(CreateMatch.this, "", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
        );
        mTimePicker.setVersion(com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_1);

        mTimePicker.show(getSupportFragmentManager(), "requireFragmentManager");


    }


    public void showErrorPopup(View v, String msg) {


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.validationpoplyt, null);


        TextView error_msg = popupView.findViewById(R.id.error_msg);


        Typeface typeface = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");


        error_msg.setTypeface(typeface);
        error_msg.setText(msg);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT

        );


        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {


            }
        });

        popupWindow.showAsDropDown(v);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateTour:

                if (et_matchname.getText().toString().isEmpty()) {

                    showErrorPopup(et_matchname, getResources().getString(R.string.err_mempty));
//                        createMatches(cat_id, homeBean.getLatitude(), homeBean.getLongitude(), homeBean.getAddress(), etmatchName.getText().toString(), etMatchdate.getText().toString(), _24hourstarttime, _24hourendtime, user_id, opponet_id, user_level, opponet_level);

//                        ^[a-zA-Z0-9]+$
                } else if (!et_matchname.getText().toString().matches("^[a-zA-Z0-9 ]+$")) {

                    showErrorPopup(et_matchname, getResources().getString(R.string.err_mName));

                } else if (etLocation.getText().toString().isEmpty()) {
                    showErrorPopup(etLocation, getResources().getString(R.string.err_location_not_empty));

                } else if (etTOURNAMENTDATE.getText().toString().isEmpty()) {
                    showErrorPopup(etTOURNAMENTDATE, getResources().getString(R.string.err_date_not_empty));
                } else if (etTOURNAMENTTIME.getText().toString().isEmpty()) {
                    showErrorPopup(etTOURNAMENTTIME, getResources().getString(R.string.err_startime_not_empty));
                } else if (etREGISTRATIONFEES.getText().toString().isEmpty()) {
                    showErrorPopup(etREGISTRATIONFEES, getResources().getString(R.string.err_regfee_not_empty));
                } else if (Integer.parseInt(etREGISTRATIONFEES.getText().toString()) < 20) {
                    showErrorPopup(etREGISTRATIONFEES, getResources().getString(R.string.err_invalidefee));

                } else if (Integer.parseInt(etREGISTRATIONFEES.getText().toString()) > 100) {
                    showErrorPopup(etREGISTRATIONFEES, getResources().getString(R.string.err_invalidefee));
                } else {

                    confirmdialog();

                }
                break;

            case R.id.etLocation:


                Intent intent_two = new Intent(Activity_TourForm.this, SearchAddressActivity.class);

                startActivityForResult(intent_two, REQ_SEARCH_HOME);


                break;

        }
    }


    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etTOURNAMENTDATE.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SEARCH_HOME && resultCode == RESULT_OK) {

            homeBean = (PlaceBean) data.getSerializableExtra("location");

            etLocation.setText(homeBean.getAddress());


        }
    }


    private void fetchtourpercentages() {

        Apis apis = RestAdapter.createAPI();
        Call<MatchtourPercentRespo> callbackCall = apis.gettourpercentage();
        callbackCall.enqueue(new Callback<MatchtourPercentRespo>() {
            @Override
            public void onResponse(Call<MatchtourPercentRespo> call, Response<MatchtourPercentRespo> response) {


                MatchtourPercentRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {


                    winner_percent_lbl.setText("WINNER: " + resp.getWinnerPer() + "% of Total Fee Collected");
                    runnerup_percent_lbl.setText("RUNNER UP: " + resp.getRunnerupPer() + "% of Total Fee Collected");
                    company_percent_lbl.setText("COMPANY: " + resp.getCompanyPer() + "% of Total Fee Collected");

                    winnerpercent = resp.getWinnerPer();
                    runnerpercent = resp.getRunnerupPer();
                    company_percent = resp.getCompanyPer();
                    host_percent = resp.getHostPer();

//                    dialogServerNotConnect();
                } else {

                }
            }

            @Override
            public void onFailure(Call<MatchtourPercentRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void joinTour(int catid, String lat, String lng, String address, String matchname, String date, String time, String mid, String midlevel, String matchtype, int amount, int total_amount, String txn_id, int winner_amount, int runnerup_amount, int company_amount, int host_amount, int group_member, String orderId, String cityname) {
        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<CreateMatchRespo> callbackCall = apis.createTour(catid, lat, lng, address, matchname, date, time, mid, midlevel, matchtype, amount, total_amount, txn_id, winner_amount, runnerup_amount, company_amount, host_amount, group_member, orderId, cityname);
        callbackCall.enqueue(new Callback<CreateMatchRespo>() {
            @Override
            public void onResponse(Call<CreateMatchRespo> call, Response<CreateMatchRespo> response) {


                CreateMatchRespo resp = response.body();
                Log.d("RESPO", new Gson().toJson(resp));

                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();

                    show_gifdialog(resp.getErrorMsg());


//                    dialogServerNotConnect();
                } else {

                    loader_dialog.dismiss();
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


    public void show_gifdialog(String msg) {

        new TTFancyGifDialog.Builder(this)
                .setTitle("Tournament")
                .setMessage(msg)
                .setPositiveBtnText("Ok")
                .setPositiveBtnBackground("#3F51B5")
                .isCancellable(false)
                .setGifResource(R.drawable.success)      //pass your gif, png or jpg
                .isCancellable(true)
                .OnPositiveClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {


//                        Toast.makeText(Activity_ScoreSingles.this,"Ok",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_TourForm.this, Activity_Badminton.class);
                        startActivity(intent);
                        finishAffinity();

                    }
                })

                .build();
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
        msg.setText("Are you sure? Do you want to Proceed...");
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


                sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
                dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//                joinTour(selected_cat, homeBean.getLatitude(), homeBean.getLongitude(), homeBean.getAddress(), et_matchname.getText().toString().trim(), etTOURNAMENTDATE.getText().toString(), _24hourstarttime, user_id, user_id_level, "3", fees, regfee, "123456", firstprizeMoney, secondprize, company_comission, host_comission, no_of_player);

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private String getRandomNumber(int min, int max) {
        return String.valueOf((new Random()).nextInt((max - min) + 1) + min);
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        Log.d("checksum ", " respon true " + inResponse.toString());
//        Toast.makeText(this,inResponse+"",Toast.LENGTH_LONG).show();

        if (inResponse.getString("STATUS").equals("TXN_SUCCESS")) {


            String transaction_id = inResponse.getString("TXNID");

            joinTour(selected_cat, homeBean.getLatitude(), homeBean.getLongitude(), homeBean.getAddress(), et_matchname.getText().toString().trim(), etTOURNAMENTDATE.getText().toString(), _24hourstarttime, user_id, user_id_level, "3", fees, regfee, transaction_id, firstprizeMoney, secondprize, company_comission, host_comission, no_of_player, serverOrderId, homeBean.getCityname());

        }


    }

    @Override
    public void networkNotAvailable() {

        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();

    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {

        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Log.d("checksum ", " ui fail respon  " + inErrorMessage);
        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

        Log.d("checksum ", " error loading pagerespon true " + iniErrorCode + "  s1 " + inErrorMessage);

        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.d("checksum ", " cancel call back respon  ");
        Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

        Log.d("checksum ", "  transaction cancel ");
        Toast.makeText(getApplicationContext(), "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();

    }

    public boolean checkzero() {
        try {
            if (Integer.parseInt(etREGISTRATIONFEES.getText().toString()) == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean checklessthan100() {
        try {
            if (Integer.parseInt(etREGISTRATIONFEES.getText().toString()) < 10) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

//            unregisterReceiver(instamojoPay);

        /*
        orderId = "order" + getRandomNumber(10, 1000) + SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");

*/
    }

    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        //private String orderId , mid, custid, amt;
        String url = "http://synramprojects.com/rackonnect/generateChecksum.php";
        //                String callbackurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        String callbackurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId;
        //        String callbackurl = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderId;
        String varifyurl = "https://morningbucket.in/verifyChecksum.php";
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH = "";
        private ProgressDialog dialog = new ProgressDialog(Activity_TourForm.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParser jsonParser = new JSONParser(Activity_TourForm.this);


            String param =
                    "MID=" + mid +
                            "&ORDER_ID=" + orderId +
                            "&CUST_ID=" + custId +
                            "&CALLBACK_URL=" + callbackurl +
                            "&CHANNEL_ID=WAP&TXN_AMOUNT=" + fees + "&WEBSITE=WEBSTAGING" +
                            "&INDUSTRY_TYPE_ID=Retail";


            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            // yaha per checksum ke saht order id or status receive hoga..
//          if (jsonObject!=null)
//            Log.e("CheckSum result >>",jsonObject.toString());

            Log.d("CheckSum result >>", jsonObject.toString());

            if (jsonObject != null) {
                Log.d("CheckSum result >>", jsonObject.toString());
                try {
                    CHECKSUMHASH = jsonObject.has("CHECKSUMHASH") ? jsonObject.getString("CHECKSUMHASH") : "";
                    Log.d("CheckSum result >>", CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ", "  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            PaytmPGService Service = PaytmPGService.getStagingService();
            // when app is ready to publish use production service
//            PaytmPGService Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custId);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", String.valueOf(fees));
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL", callbackurl);
//            paramMap.put( "EMAIL" , "username@emailprovider.com");   // no need
//            paramMap.put( "MOBILE_NO" , "7777777777");  // no need
            paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");

            PaytmOrder Order = new PaytmOrder(paramMap);

            Log.d("CHECKMY--", CHECKSUMHASH);
            Log.e("checksum ", "param " + paramMap.toString());

            Service.initialize(Order, null);
            // start payment service call here
            Service.startPaymentTransaction(Activity_TourForm.this, true, true,
                    Activity_TourForm.this);
        }
    }


}
