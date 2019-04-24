package com.sportskonnect.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.github.jorgecastillo.FillableLoader;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;
import com.google.gson.Gson;
import com.sportskonnect.Activity_Badminton;
import com.sportskonnect.R;
import com.sportskonnect.adapters.SelectedGamesAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.CreateMatchRespo;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.database.DatabaseHandler;
import com.sportskonnect.helpers.AppConstants;
import com.sportskonnect.modal.PlaceBean;
import com.sportskonnect.modal.SelectedGameModal;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.sportskonnect.utility.RecyclerTouchListener;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;

public class CreateMatch extends AppCompatActivity implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    private static final int REQ_SEARCH_HOME = 0;
    final Calendar myCalendar = Calendar.getInstance();
    EditText etmatchName, et_matchLocation, etMatchdate, et_match_starttime, et_match_endtime;
    Button btnCreateMatch;
    private PlaceBean homeBean;
    private SharedPreference sharedPreference;
    private String user_id = "";
    private String cat_id = "";
    private String user_level = "";
    private String opponet_level = "";
    private String opponet_id = "";
    private Dialog matchcreate_dialog;
    private String matchCreatedFrom = "";
    private DatabaseHandler db;
    private int selected_cat = 0;
    private EditText etmatchType;
    private Dialog choosegame_dialog;
    private SelectedGamesAdapter mAdapter;
    private AwesomeValidation awesomeValidation;
    private Calendar mcurrentTime;
    private Calendar mcurrentTime_end;
    private int startTimeselectedHour = 0;
    private int starttiemselectedmin = 0;
    private String _24hourstarttime = "";
    private String _24hourendtime = "";
    private Dialog loader_dialog;
    private PopupWindow popupWindow;
    private String cityName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        sharedPreference = new SharedPreference(this);
        matchcreate_dialog = new Dialog(this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        db = new DatabaseHandler(this);

//^[a-zA-Z ]+$

        awesomeValidation.addValidation(CreateMatch.this, R.id.etmatchName, "^[a-zA-Z0-9]+$", R.string.err_mName);
        awesomeValidation.addValidation(CreateMatch.this, R.id.etmatchName, RegexTemplate.NOT_EMPTY, R.string.err_mempty);

        awesomeValidation.addValidation(CreateMatch.this, R.id.et_matchLocation, RegexTemplate.NOT_EMPTY, R.string.err_location_not_empty);
        awesomeValidation.addValidation(CreateMatch.this, R.id.etMatchdate, RegexTemplate.NOT_EMPTY, R.string.err_date_not_empty);
        awesomeValidation.addValidation(CreateMatch.this, R.id.et_match_starttime, RegexTemplate.NOT_EMPTY, R.string.err_startime_not_empty);
        awesomeValidation.addValidation(CreateMatch.this, R.id.et_match_endtime, RegexTemplate.NOT_EMPTY, R.string.err_endtime_not_empty);

        init();

    }

    private void init() {
        etmatchName = findViewById(R.id.etmatchName);
        et_matchLocation = findViewById(R.id.et_matchLocation);
        etMatchdate = findViewById(R.id.etMatchdate);
        et_match_starttime = findViewById(R.id.et_match_starttime);
        et_match_endtime = findViewById(R.id.et_match_endtime);
        btnCreateMatch = findViewById(R.id.btnCreateMatch);
        etmatchType = findViewById(R.id.etmatchType);


        opponet_id = sharedPreference.getSharedPreferenceString(this, SharedprefKeys.OPPONENT_ID, "");
        opponet_level = sharedPreference.getSharedPreferenceString(this, SharedprefKeys.OPPONENT_LEVEL, "");
        user_level = sharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_LEVEL, "");
        cat_id = sharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "");
        user_id = sharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");

        etmatchType.setText(Constant.getGameNameFromcatId(SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "")));


        matchCreatedFrom = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.MATCH_CREATED_FROM, "");


//setupAutoCompleteFragment();
        et_matchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CreateMatch.this, SearchAddressActivity.class);

                startActivityForResult(intent, REQ_SEARCH_HOME);
                setupAutoCompleteFragment();


            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabel();
            }

        };

        etMatchdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
//                DatePickerDialog dialog = new DatePickerDialog(CreateMatch.this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
//                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                dialog.show();

                showDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), R.style.DatePickerSpinner);
            }
        });


        etmatchType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosegameDialog(CreateMatch.this, "");
            }
        });

        et_match_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etMatchdate.getText().toString().isEmpty()) {
                    showErrorPopup(et_match_starttime, "Make sure you have selected Match Date");
                } else {
                    openTimeFragment();
                }


            }
        });


        et_match_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_match_starttime.getText().toString().isEmpty()) {
                    showErrorPopup(et_match_endtime, "Make sure you have choose start time");

                } else {
                    openEndTimeFragment();

                }

            }
        });

        btnCreateMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (matchCreatedFrom.equals("1")) {


                    if (etmatchName.getText().toString().isEmpty()) {

                        Log.d("CLICK", "CLICKED");
                        showErrorPopup(etmatchName, getResources().getString(R.string.err_mempty));
//                        createMatches(cat_id, homeBean.getLatitude(), homeBean.getLongitude(), homeBean.getAddress(), etmatchName.getText().toString(), etMatchdate.getText().toString(), _24hourstarttime, _24hourendtime, user_id, opponet_id, user_level, opponet_level);

//                        ^[a-zA-Z0-9]+$
                    } else if (!etmatchName.getText().toString().matches("^[a-zA-Z0-9 ]+$")) {

                        showErrorPopup(etmatchName, getResources().getString(R.string.err_mName));

                    } else if (et_matchLocation.getText().toString().isEmpty()) {
                        showErrorPopup(et_matchLocation, getResources().getString(R.string.err_location_not_empty));

                    } else if (etMatchdate.getText().toString().isEmpty()) {
                        showErrorPopup(etMatchdate, getResources().getString(R.string.err_date_not_empty));

                    } else if (et_match_starttime.getText().toString().isEmpty()) {
                        showErrorPopup(et_match_starttime, getResources().getString(R.string.err_startime_not_empty));

                    } else if (et_match_endtime.getText().toString().isEmpty()) {
                        showErrorPopup(et_match_endtime, getResources().getString(R.string.err_endtime_not_empty));

                    } else {

                        createMatches(cat_id, homeBean.getLatitude(), homeBean.getLongitude(), homeBean.getAddress(), etmatchName.getText().toString(), etMatchdate.getText().toString(), _24hourstarttime, _24hourendtime, user_id, opponet_id, user_level, opponet_level);


                    }

                } else if (matchCreatedFrom.equals("2")) {

                    if (etmatchName.getText().toString().isEmpty()) {

                        Log.d("CLICK", "CLICKED");
                        showErrorPopup(etmatchName, getResources().getString(R.string.err_mempty));
//                        createMatches(cat_id, homeBean.getLatitude(), homeBean.getLongitude(), homeBean.getAddress(), etmatchName.getText().toString(), etMatchdate.getText().toString(), _24hourstarttime, _24hourendtime, user_id, opponet_id, user_level, opponet_level);

//                        ^[a-zA-Z0-9]+$
                    } else if (!etmatchName.getText().toString().matches("^[a-zA-Z0-9 ]+$")) {

                        showErrorPopup(etmatchName, getResources().getString(R.string.err_mName));

                    } else if (et_matchLocation.getText().toString().isEmpty()) {
                        showErrorPopup(et_matchLocation, getResources().getString(R.string.err_location_not_empty));

                    } else if (etMatchdate.getText().toString().isEmpty()) {
                        showErrorPopup(etMatchdate, getResources().getString(R.string.err_date_not_empty));

                    } else if (et_match_starttime.getText().toString().isEmpty()) {
                        showErrorPopup(et_match_starttime, getResources().getString(R.string.err_startime_not_empty));

                    } else if (et_match_endtime.getText().toString().isEmpty()) {
                        showErrorPopup(et_match_endtime, getResources().getString(R.string.err_endtime_not_empty));

                    } else {

                        createDualMatches(cat_id, homeBean.getLatitude(), homeBean.getLongitude(), homeBean.getAddress(), etmatchName.getText().toString(), etMatchdate.getText().toString(), _24hourstarttime, _24hourendtime, user_id, user_level);


                    }


                }

            }
        });


//        if (db.getActiveCartList().size() > 0) {
//
//
//            if (db.getActiveCartList().get(0).getCatId() == 1) {
//
//                etmatchType.setText("SQUASH");
//                selected_cat = 1;
//                sharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "1");
//
//
//            } else if (db.getActiveCartList().get(0).getCatId() == 2) {
//
//                etmatchType.setText("TABLE TENNIS");
//                selected_cat = 2;
//                sharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "2");
//
//            } else if (db.getActiveCartList().get(0).getCatId() == 3) {
//
//                etmatchType.setText("BADMINTON");
//                selected_cat = 3;
//                sharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "3");
//
//
//            } else if (db.getActiveCartList().get(0).getCatId() == 4) {
//
//                etmatchType.setText("TENNIS");
//                selected_cat = 4;
//                sharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "4");
//
//            } else {
//                etmatchType.setText("Select Game");
//                sharedPreference.setSharedPreferenceString(this, SharedprefKeys.USER_SELECTED_CAT, "");
//
//            }


//        }

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etMatchdate.setText(sdf.format(myCalendar.getTime()));
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
                    sharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "1");


                } else if (selectedGameModal.getCatId() == 2) {

                    etmatchType.setText("TABLE TENNIS");
                    selected_cat = 2;
                    sharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "2");


                } else if (selectedGameModal.getCatId() == 3) {

                    etmatchType.setText("BADMINTON");
                    selected_cat = 3;
                    sharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "3");


                } else if (selectedGameModal.getCatId() == 4) {

                    etmatchType.setText("TENNIS");
                    selected_cat = 4;
                    sharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "4");

                } else {
                    etmatchType.setText("Select Game");
                    sharedPreference.setSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_CAT, "");


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SEARCH_HOME && resultCode == RESULT_OK) {

            homeBean = (PlaceBean) data.getSerializableExtra("location");

            et_matchLocation.setText(homeBean.getAddress());

//            Toast.makeText(getApplicationContext(), "Home Location Added",
//                    Toast.LENGTH_LONG).show();

//            performLocationSave();

        }
    }


    public void finishCreatematch(View view) {
        finish();
    }

    void openTimeFragment() {
        TimePickerDialog mTimePicker;

        final Calendar c = Calendar.getInstance();
        int current_hour = c.get(Calendar.HOUR_OF_DAY);
        int current_minute = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);
        Date today = c.getTime();

        mTimePicker = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
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

                            et_match_starttime.setText(formattedTime);

                        } else {


                          /*  if (hourOfDay > current_hour) {



                                et_match_starttime.setText(formattedTime);
                            } else {
                                //Display a toast or something to inform the user that he can't pick a past time.


                                showErrorPopup(et_match_starttime, "You can't select past time");

//                        Toast.makeText(CreateMatch.this, "", Toast.LENGTH_LONG).show();
                            }

*/

                            if (hourOfDay > current_hour) {

                                et_match_starttime.setText(formattedTime);

                            } else {
                                if (hourOfDay < current_hour) {
                                    showErrorPopup(et_match_starttime, "Please make sure startime must be greater than end time.");

                                } else {

                                    if (minute > current_minute) {
                                        et_match_starttime.setText(formattedTime);

                                    } else {

                                        et_match_starttime.setText("");

                                        showErrorPopup(et_match_starttime, "You can't select past time.");


                                    }


                                }

                            }

                        }
                    }
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
        );
        mTimePicker.setVersion(TimePickerDialog.Version.VERSION_1);

        mTimePicker.show(getSupportFragmentManager(), "requireFragmentManager");

//        mTimePicker = new TimePickerDialog(CreateMatch.this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//
//
//                String time = selectedHour + ":" + selectedMinute;
//
//                startTimeselectedHour = selectedHour;
//                starttiemselectedmin = selectedMinute;
//
//                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
//                Date date = null;
//                try {
//                    date = fmt.parse(time);
//
//                    _24hourstarttime = fmt.format(date);
//
//
//                } catch (ParseException e) {
//
//                    e.printStackTrace();
//                }
//
//                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");
//
//                String formattedTime = fmtOut.format(date);
//
//
//                if (myCalendar.getTime().after(today)) {
//
//                    et_match_starttime.setText(formattedTime);
//
//                } else {
//
//
//                    if (selectedHour >= hour && selectedMinute >= minute) {
//
//                        et_match_starttime.setText(formattedTime);
//                    } else {
//                        //Display a toast or something to inform the user that he can't pick a past time.
//
//
//                        showErrorPopup(et_match_starttime, "You can't select past time");
//
////                        Toast.makeText(CreateMatch.this, "", Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//
//            }
//        }, hour, minute, false);//No 24 hour time
//        mTimePicker.setTitle("Select Time");
//
//
//        mTimePicker.show();
    }


    void openEndTimeFragment() {
        TimePickerDialog mTimePicker;

        final Calendar c = Calendar.getInstance();


        int current_hour = c.get(Calendar.HOUR_OF_DAY);
        int current_minute = c.get(Calendar.MINUTE);
        Date today = c.getTime();


        mTimePicker = TimePickerDialog.
                newInstance(new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                    String time = hourOfDay + ":" + minute;

//                                    startTimeselectedHour = hourOfDay;
//                                    starttiemselectedmin = minute;

                                    SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                    Date date = null;
                                    try {
                                        date = fmt.parse(time);

                                        _24hourendtime = fmt.format(date);


                                    } catch (ParseException e) {

                                        e.printStackTrace();
                                    }

                                    SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                    String formattedTime = fmtOut.format(date);


                                    if (hourOfDay > startTimeselectedHour) {
                                        et_match_endtime.setText(formattedTime);


                                    } else {
                                        //Display a toast or something to inform the user that he can't pick a past time.

                                        if (hourOfDay < startTimeselectedHour) {
                                            showErrorPopup(et_match_endtime, "Please make sure startime must be greater than end time.");


                                        } else {

                                            if (minute > starttiemselectedmin) {
                                                et_match_endtime.setText(formattedTime);

                                            } else {

                                                et_match_endtime.setText("");

                                                showErrorPopup(et_match_endtime, "Please make sure star time must be greater than end time. your end time is: " + Constant.convet24hourTo12hour(formattedTime));


                                            }


                                        }
                                    }
                                }
                            },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        false
                );
//        final Calendar c = Calendar.getInstance();
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);

//        mTimePicker = new TimePickerDialog(CreateMatch.this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//
//
//                String time = selectedHour + ":" + selectedMinute;
//
//                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
//                Date date = null;
//                try {
//                    date = fmt.parse(time);
//
//
//                    _24hourendtime = fmt.format(date);
//
////                    Log.d("TIMEEND",fmt.parse(time)+"");
//
//                } catch (ParseException e) {
//
//                    e.printStackTrace();
//                }
//
//                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");
//
//                String formattedTime = fmtOut.format(date);
////                if (selectedHour > startTimeselectedHour || selectedMinute > starttiemselectedmin) {
////
////                    if (selectedHour >= hour && selectedMinute >= minute) {
////
////                        et_match_endtime.setText(formattedTime);
////                    } else {
////                        //Display a toast or something to inform the user that he can't pick a past time.
////                        showErrorPopup(et_match_endtime, "You can't select past time");
////
//////                        Toast.makeText(CreateMatch.this, "", Toast.LENGTH_LONG).show();
////                    }
////
////                } else {
////
////                    showErrorPopup(et_match_endtime, "Please make sure startime must be grater than end time.");
////
////
//////                    Toast.makeText(CreateMatch.this, "", Toast.LENGTH_LONG).show();
////
////                }
//
//
//                if (selectedHour > startTimeselectedHour) {
//                    et_match_endtime.setText(formattedTime);
//
//
//                } else {
//                    //Display a toast or something to inform the user that he can't pick a past time.
//
//                    if (selectedHour < startTimeselectedHour) {
//                        showErrorPopup(et_match_endtime, "Please make sure startime must be grater than end time.");
//
//
//                    } else {
//
//                        if (selectedMinute > starttiemselectedmin) {
//                            et_match_endtime.setText(formattedTime);
//
//                        } else {
//
//                            et_match_endtime.setText("");
//
//                            showErrorPopup(et_match_endtime, "Please make sure star time must be grater than end time. your end time is: " + Constant.convet24hourTo12hour(formattedTime));
//
//
//                        }
//
//
//                    }
//                }
//
//
//            }
//        }, hour, minute, false);//No 24 hour time
//        mTimePicker.setTitle("Select Time");

        mTimePicker.setVersion(TimePickerDialog.Version.VERSION_1);
        mTimePicker.show(getSupportFragmentManager(), "requireFragmentManager");
    }

    private void createMatches(String catid, String lat, String lng, String address,
                               String matchname,
                               String date,
                               String start_time,
                               String end_time,
                               String mid,
                               String fid,
                               String midlevel,
                               String fidlevel) {

        loader_dialog(this);

        Apis apis = RestAdapter.createAPI();
        Call<CreateMatchRespo> callbackCall = apis.createMatch(catid, lat, lng, address, matchname, date, start_time, end_time, mid, fid, midlevel, fidlevel, "1");
        callbackCall.enqueue(new Callback<CreateMatchRespo>() {
            @Override
            public void onResponse(Call<CreateMatchRespo> call, Response<CreateMatchRespo> response) {


                CreateMatchRespo resp = response.body();
                Log.d("LOGINRESPO", new Gson().toJson(resp));
                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();

                    createMatchSuccessfullDialog(CreateMatch.this, resp.getErrorMsg() + "");
//                    dialogServerNotConnect();
                } else {

                    loader_dialog.dismiss();

//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<CreateMatchRespo> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                loader_dialog.dismiss();
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    private void createDualMatches(String catid, String lat, String lng, String address,
                                   String matchname,
                                   String date,
                                   String start_time,
                                   String end_time,
                                   String mid,

                                   String midlevel
    ) {

        loader_dialog(this);

        Apis apis = RestAdapter.createAPI();
        Call<CreateMatchRespo> callbackCall = apis.createDualMatch(catid, lat, lng, address, matchname, date, start_time, end_time, mid, midlevel, "2");
        callbackCall.enqueue(new Callback<CreateMatchRespo>() {
            @Override
            public void onResponse(Call<CreateMatchRespo> call, Response<CreateMatchRespo> response) {


                CreateMatchRespo resp = response.body();
                Log.d("LOGINRESPO", new Gson().toJson(resp));
                if (resp != null && !resp.getError()) {

                    loader_dialog.dismiss();
                    createMatchSuccessfullDialog(CreateMatch.this, resp.getErrorMsg() + "");
//                    dialogServerNotConnect();
                } else {

                    loader_dialog.dismiss();
//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<CreateMatchRespo> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                loader_dialog.dismiss();
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }


    public void createMatchSuccessfullDialog(final Context context, String message) {
        matchcreate_dialog = new Dialog(context);
        matchcreate_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.matchcreate_successful_dialog_lyt, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/

        TextView validation_content = view.findViewById(R.id.validation_content);

        Button bt_positive = view.findViewById(R.id.bt_positive);
        ImageView validation_img = view.findViewById(R.id.validation_img);

//        validation_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_mobile_validation));


        bt_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateMatch.this, Activity_Badminton.class);
                startActivity(intent);
                finish();
                matchcreate_dialog.dismiss();
            }
        });

        validation_content.setText(message);
//        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        matchcreate_dialog.setContentView(view);
        final Window window = matchcreate_dialog.getWindow();
        matchcreate_dialog.setCancelable(false);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        matchcreate_dialog.show();
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


    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(CreateMatch.this)
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .minDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }


    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }



    /**
     * Sets up the autocomplete fragment to show place details when a place is selected.
     */
    private void setupAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                showPlace(getString(R.string.place_autocomplete_search_hint), place);

            }

            @Override
            public void onError(Status status) {
                showResponse("An error occurred: " + status);
            }
        });
    }


    /**
     * Shows some text and clears any previously set image.
     */
    private void showResponse(String response) {
        /*textView.setText(response);
        imageView.setImageResource(0);*/




    }

    /**
     * Shows the name of a place, and its photo.
     */
    private void showPlace(String source, Place place) {
//        locationSelect = AppConstants.LOCATION_SELECTED_ONITEMCLICK;

        PlaceBean placeBean = new PlaceBean();

        placeBean.setAddress(String.valueOf(place.getAddress()));
        placeBean.setLatitude(String.valueOf(place.getLatLng().latitude));
        placeBean.setLongitude(String.valueOf(place.getLatLng().longitude));
        placeBean.setName((String) place.getName());
        placeBean.setCityname(cityName);

        /*Intent intent = new Intent();
        intent.putExtra("location", placeBean);
//                    intent.putExtra("locationSelect", locationSelect);
        setResult(RESULT_OK, intent);
        finish();*/

    }

}
