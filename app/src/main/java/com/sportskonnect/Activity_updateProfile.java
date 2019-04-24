package com.sportskonnect;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.github.jorgecastillo.FillableLoader;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.SaveProfiledataResponse;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.sportskonnect.utility.FileUtil;
import com.squareup.picasso.Picasso;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;

//import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

public class Activity_updateProfile extends AppCompatActivity implements View.OnClickListener, com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    public static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    final Calendar myCalendar = Calendar.getInstance();
    Button btnSubmit, btnAdd;
    TextView main_text_title, tvHead, tvGender, tvMale, tvFemale;
    EditText etMobile, etreferal;
    private String userChoosenTask = "";
    private File photoFile;
    private String imageFilePath;
    private CircleImageView profile_image;
    private LinearLayout referal_ll;
    private InputStream profile_pic_ioStream;
    private LinearLayout container;
    private LinearLayout female_ll, male_ll;
    private String selected_gender = "Male";
    private SharedPreference sharedPreference;
    private String user_name = "";
    private String user_email = "";
    private Dialog dialog;
    private String profilePic_server;
    private Dialog loader_dialog;
    private LinearLayout time_ll;
    private String profile_come_from = "";
    private AwesomeValidation awesomeValidation;
    private ProgressInfo mLastDownloadingInfo;
    private Handler mHandler = new Handler();
    private Dialog validation_dialog;
    private EditText etage;
    private String timePref = "";
    private String dob = "";
    private String referal_code;
    private PopupWindow popupWindow;
    private String ptime = "";

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        sharedPreference = new SharedPreference(this);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        profile_come_from = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.PROFILE_COME_FROM, "");

// to validate with a simple custom validator function
        awesomeValidation.addValidation(Activity_updateProfile.this, R.id.etage, new SimpleCustomValidation() {
            @Override
            public boolean compare(String input) {
                // check if the age is >= 18
                try {
                    Calendar calendarBirthday = Calendar.getInstance();
                    Calendar calendarToday = Calendar.getInstance();
                    calendarBirthday.setTime(new SimpleDateFormat("dd MMM, yyyy", Locale.US).parse(input));
                    int yearOfToday = calendarToday.get(Calendar.YEAR);
                    int yearOfBirthday = calendarBirthday.get(Calendar.YEAR);
                    if (yearOfToday - yearOfBirthday > 10) {
                        return true;
                    } else if (yearOfToday - yearOfBirthday == 10) {
                        int monthOfToday = calendarToday.get(Calendar.MONTH);
                        int monthOfBirthday = calendarBirthday.get(Calendar.MONTH);
                        if (monthOfToday > monthOfBirthday) {
                            return true;
                        } else if (monthOfToday == monthOfBirthday) {
                            if (calendarToday.get(Calendar.DAY_OF_MONTH) >= calendarBirthday.get(Calendar.DAY_OF_MONTH)) {
                                return true;
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }, R.string.err_birth);

        awesomeValidation.addValidation(Activity_updateProfile.this, R.id.etage, RegexTemplate.NOT_EMPTY, R.string.err_age_not_empty);

        try {
            dob = formatdateReturnDate(SharedPreference.getSharedPreferenceString(this, SharedprefKeys.DOB, ""));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("DOB", dob);

        init();
    }

    private void init() {

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        main_text_title = (TextView) findViewById(R.id.main_text_title);
        etMobile = (EditText) findViewById(R.id.etName);
        etreferal = (EditText) findViewById(R.id.etreferal);

        tvHead = (TextView) findViewById(R.id.tvHead);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvMale = (TextView) findViewById(R.id.tvMale);
        tvFemale = (TextView) findViewById(R.id.tvFemale);
        profile_image = findViewById(R.id.profile_image);
        referal_ll = findViewById(R.id.referal_ll);
        time_ll = findViewById(R.id.time_ll);
        container = findViewById(R.id.container);
        female_ll = findViewById(R.id.female_ll);
        male_ll = findViewById(R.id.male_ll);
        etage = findViewById(R.id.etage);


        CardView anytym_cv = findViewById(R.id.anytym_cv);
        CardView morning_cv = findViewById(R.id.morning_cv);
        CardView afternoon_cv = findViewById(R.id.afternoon_cv);
        CardView evening_cv = findViewById(R.id.evening_cv);

        TextView lbl_anytym = findViewById(R.id.lbl_anytym);
        TextView lbl_tvMorning = findViewById(R.id.lbl_tvMorning);
        TextView lbl_tvAfternoon = findViewById(R.id.lbl_tvAfternoon);
        TextView lbl_tvevening = findViewById(R.id.lbl_tvevening);


        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Calibre Bold.otf");

        Typeface face1 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Regular.otf");

        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");

        btnAdd.setTypeface(face1);

        main_text_title.setTypeface(face);
        tvHead.setTypeface(face2);
        etMobile.setTypeface(face2);
        tvGender.setTypeface(face2);
        tvMale.setTypeface(face2);
        tvFemale.setTypeface(face2);
        btnSubmit.setTypeface(face2);

        btnSubmit.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        male_ll.setOnClickListener(this);
        female_ll.setOnClickListener(this);

        user_name = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_NAME, "");
        user_email = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.EMAIL_ID, "");
        profilePic_server = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.PROFILE_PIC_URL, "");
        etage.setText(SharedPreference.getSharedPreferenceString(this, SharedprefKeys.DOB, ""));

        etMobile.setText(user_name);

        try {
            Picasso.get().load(profilePic_server).into(profile_image);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

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


        etage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
//                DatePickerDialog dialog = new DatePickerDialog(Activity_updateProfile.this, date, 2009, c.get(Calendar.MONTH), c.get(Calendar.MONTH));
//
//                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
////                dialog.getDatePicker().setMinDate(System.currentTimeMillis()+20);
//
//                dialog.show();


                showDate(c.get(Calendar.DAY_OF_YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), R.style.NumberPickerStyle);
            }
        });


        ptime = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.PTIME, "");

        if (ptime.equals("1")) {
            timePref = "1";

            morning_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvMorning.setTextColor(getResources().getColor(R.color.color_white));
            lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));


        } else if (ptime.equals("2")) {

            timePref = "2";

            afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.color_white));
            lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));

        } else if (ptime.equals("3")) {
            timePref = "3";

            evening_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvevening.setTextColor(getResources().getColor(R.color.color_white));
            lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));

        } else {

            timePref = "";

            anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
            evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

            lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
            lbl_anytym.setTextColor(getResources().getColor(R.color.color_white));

        }


/*
        btnAdd.setClickable(true);


        Snackbar.make(container,"Permission Granted",Snackbar.LENGTH_LONG).show();
*/

//        requestCameraPermission();
        requestStoragePermission();


        if (profile_come_from.equals("fromlogin")) {

            time_ll.setVisibility(View.GONE);

            referal_ll.setVisibility(View.VISIBLE);

            btnSubmit.setText("NEXT");

        } else {
            referal_ll.setVisibility(View.GONE);
            time_ll.setVisibility(View.VISIBLE);

            btnSubmit.setText("UPDATE");
        }


        anytym_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePref = "";

                anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_anytym.setTextColor(getResources().getColor(R.color.color_white));

            }
        });

        morning_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePref = "1";

                morning_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMorning.setTextColor(getResources().getColor(R.color.color_white));
                lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        afternoon_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePref = "2";

                afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                evening_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.color_white));
                lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvevening.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        evening_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePref = "3";

                evening_cv.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                morning_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                anytym_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));
                afternoon_cv.setCardBackgroundColor(getResources().getColor(R.color.color_white));

                lbl_tvMorning.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvevening.setTextColor(getResources().getColor(R.color.color_white));
                lbl_anytym.setTextColor(getResources().getColor(R.color.colorPrimary));
                lbl_tvAfternoon.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        });


    }

    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {

        Calendar calendar = Calendar.getInstance();

        new SpinnerDatePickerDialogBuilder()
                .context(Activity_updateProfile.this)
                .callback((com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener) this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(1990, monthOfYear, dayOfMonth)
                .maxDate(calendar.get(Calendar.YEAR), 0, 1)
                .build()
                .show();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String myFormat2 = "dd MMM, yyyy"; //In which you need put here
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);

        dob = sdf.format(myCalendar.getTime());

        etage.setText(sdf2.format(myCalendar.getTime()));
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        if (is != null) {
            int len = 0;
            while ((len = is.read(buff)) != -1) {
                byteBuff.write(buff, 0, len);
            }
        }
        return byteBuff.toByteArray();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

                try {

                    referal_code = etreferal.getText().toString().trim();

                    int yearOfToday = 0;
                    int yearOfBirthday = 0;

                    if (!etage.getText().toString().isEmpty()) {
                        Calendar calendarBirthday = Calendar.getInstance();
                        Calendar calendarToday = Calendar.getInstance();


                        calendarBirthday.setTime(new SimpleDateFormat("dd MMM, yyyy", Locale.US).parse(etage.getText().toString().trim()));
                        yearOfToday = calendarToday.get(Calendar.YEAR);
                        yearOfBirthday = calendarBirthday.get(Calendar.YEAR);


                    }

                    if (etMobile.getText().toString().isEmpty()) {
                        showErrorPopup(etMobile, getResources().getString(R.string.err_fname_not_empty));
                    } else if (!etMobile.getText().toString().matches("^[a-zA-Z0-9 ]+$")) {
                        showErrorPopup(etMobile, getResources().getString(R.string.err_fname));

                    } else if (etage.getText().toString().isEmpty()) {
                        showErrorPopup(etage, getResources().getString(R.string.err_age_not_empty));


                    } else if (yearOfToday - yearOfBirthday < 10) {

                        showErrorPopup(etage, getResources().getString(R.string.err_age));

                    } else {

                        Log.d("IMAGEPATH", imageFilePath + "");
                        if (imageFilePath != null && !imageFilePath.equals("")) {


                            saveprofiledata(getBytes(profile_pic_ioStream));

                        } else {
                            saveprofiledata();
                        }
                    }


                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnAdd:
//                selectImage();


                onProfileImageClick();
                break;

            case R.id.male_ll:

                selected_gender = "Male";
                male_ll.setAlpha(1);
                female_ll.setAlpha(0.5f);


                break;
            case R.id.female_ll:

                selected_gender = "Female";
                male_ll.setAlpha(0.5f);
                female_ll.setAlpha(1);

                break;

        }
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


    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_updateProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                    openCameraIntent();
                    dialog.dismiss();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    //if(result)
                    galleryIntent();
                    dialog.dismiss();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "reckonnect";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void galleryIntent() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(getFileChooserIntent(), "Select File"), SELECT_FILE);
    }

    private Intent getFileChooserIntent() {
//        String[] mimeTypes = {"image/*", "application/pdf"};
        String[] mimeTypes = {"image/*"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        return intent;
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//
//            if (requestCode == SELECT_FILE) {
//
//                Uri uri = data.getData();
//
//                try {
//
//                    Bitmap profile_pic_bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//
//                    profile_pic_ioStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
//
//
//                    String profile_pic_fileExt = getMimeType(getApplicationContext(), uri);
//
//                    File file = FileUtil.from(this, uri);
////                    Log.e("PREVSIZE", getReadableFileSize(file.length()));
//
//                    File compressedImageBitmap = new Compressor(Activity_updateProfile.this).compressToFile(file);
////                        Log.e("SIZE", getReadableFileSize(compressedImageBitmap.length()));
//                    profile_pic_ioStream = new FileInputStream(compressedImageBitmap);
//
//                    profile_image.setImageBitmap(profile_pic_bitmap);
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else if (requestCode == REQUEST_CAMERA) {
//
//                profile_image.setImageURI(Uri.parse(imageFilePath));
//                Log.e("FILEPATH", imageFilePath);
//                try {
//                    File file = FileUtil.from(this, Uri.fromFile(new File(imageFilePath)));
//
//                    File compressedImageBitmap = new Compressor(this).compressToFile(file);
//                    profile_pic_ioStream = new FileInputStream(compressedImageBitmap);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        }
//
//
//    }

    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Requesting camera permission
     * This uses single permission model from dexter
     * Once the permission granted, opens the camera
     * On permanent denial opens settings dialog
     */
    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
//                        openCameraIntent();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
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


    private void saveprofiledata(byte[] imageBytes) {


        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();


        RequestBody mobile_no = RequestBody.create(MediaType.parse("text/plain"), sharedPreference.getSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.MOBILE_NO, ""));
        RequestBody email_id = RequestBody.create(MediaType.parse("text/plain"), sharedPreference.getSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.EMAIL_ID, ""));
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), selected_gender);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), etMobile.getText().toString().trim());
        RequestBody age = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody dob_server = RequestBody.create(MediaType.parse("text/plain"), dob);
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), timePref);
        RequestBody referal = RequestBody.create(MediaType.parse("text/plain"), referal_code);

        String filename = "file.jpg";

        MultipartBody.Part body = null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        body = MultipartBody.Part.createFormData("image", filename, requestFile);


        Call<SaveProfiledataResponse> callbackCall = apis.saveprofiledata(mobile_no, email_id, gender, name, body, age, time, dob_server, referal);
        callbackCall.enqueue(new Callback<SaveProfiledataResponse>() {
            @Override
            public void onResponse(Call<SaveProfiledataResponse> call, Response<SaveProfiledataResponse> response) {
                SaveProfiledataResponse resp = response.body();


                if (resp != null && !resp.getError()) {


                    Log.d("RESPOUPDATE", new Gson().toJson(resp));

                    loader_dialog.dismiss();

                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.USER_ID, resp.getId());

                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.EMAIL_ID, resp.getEmail());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.MOBILE_NO, resp.getMobileNumber());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.USER_NAME, resp.getName());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.PROFILE_PIC_URL, resp.getImage());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.GENDER, resp.getGender());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.AGE, resp.getAge());


                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.DOB, etage.getText().toString());


                    try {
                        if (Activity_UserProfile.profile_image != null) {

                            if (resp.getGender().equals("Male")) {
                                Picasso.get().load(resp.getImage()).placeholder(R.drawable.male).into(Activity_UserProfile.profile_image);

                            } else {
                                Picasso.get().load(resp.getImage()).placeholder(R.drawable.female).into(Activity_UserProfile.profile_image);

                            }


                        }

                        if (FragmentDrawer.user_img != null) {

                            Picasso.get().load(resp.getImage()).placeholder(R.drawable.male).into(FragmentDrawer.user_img);

                        }
                        if (FragmentDrawer.tvAge != null) {

                            FragmentDrawer.tvAge.setText(resp.getAge());

                        }

                        if (FragmentDrawer.tvUsername != null) {

                            FragmentDrawer.tvUsername.setText(resp.getName());

                        }


                        if (Activity_UserProfile.tvAge != null) {

                            Activity_UserProfile.tvAge.setText(resp.getAge());

                        }

                        if (Activity_UserProfile.tvusername != null) {

                            Activity_UserProfile.tvusername.setText(resp.getName());

                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (SharedPreference.getSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.PROFILE_COME_FROM, "").equals("fromlogin")) {
                        Intent intent = new Intent(Activity_updateProfile.this, Activity_ChooseSports.class);
                        startActivity(intent);
                    } else {

                        finish();
                    }

//                    dialogServerNotConnect();
                } else {
                    loader_dialog.dismiss();
                    dialogServerNotConnect(Activity_updateProfile.this, "NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<SaveProfiledataResponse> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
                dialogServerNotConnect(Activity_updateProfile.this, t.getMessage());
            }
        });
    }


    private void saveprofiledata() {


        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();

        String mobile_no = SharedPreference.getSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.MOBILE_NO, "");
        String email_id = SharedPreference.getSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.EMAIL_ID, "");
        String gender = selected_gender;
        String name = etMobile.getText().toString().trim();
        String age = "";


        Call<SaveProfiledataResponse> callbackCall = apis.without_imagesaveprofiledata(mobile_no, email_id, gender, name, "", age, timePref, dob, referal_code);
        callbackCall.enqueue(new Callback<SaveProfiledataResponse>() {
            @Override
            public void onResponse(Call<SaveProfiledataResponse> call, Response<SaveProfiledataResponse> response) {
                SaveProfiledataResponse resp = response.body();
                if (resp != null && !resp.getError()) {


                    Log.d("RESPOUPDATE", new Gson().toJson(resp));

                    loader_dialog.dismiss();

                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.USER_ID, resp.getId());

                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.EMAIL_ID, resp.getEmail());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.MOBILE_NO, resp.getMobileNumber());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.USER_NAME, etMobile.getText().toString());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.PROFILE_PIC_URL, resp.getImage());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.GENDER, resp.getGender());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.DOB, etage.getText().toString());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.AGE, resp.getAge());
                    SharedPreference.setSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.PTIME, resp.getPtime());


                    try {
                        if (Activity_UserProfile.profile_image != null) {

                            Picasso.get().load(resp.getImage()).placeholder(R.drawable.male).into(Activity_UserProfile.profile_image);

                        }

                        if (FragmentDrawer.user_img != null) {

                            Picasso.get().load(resp.getImage()).placeholder(R.drawable.male).into(FragmentDrawer.user_img);

                        }
                        if (FragmentDrawer.tvAge != null) {

                            FragmentDrawer.tvAge.setText(resp.getAge() + " Y");

                        }

                        if (FragmentDrawer.tvUsername != null) {

                            FragmentDrawer.tvUsername.setText(resp.getName());

                        }


                        if (Activity_UserProfile.tvAge != null) {

                            Activity_UserProfile.tvAge.setText(resp.getAge() + " Y");

                        }

                        if (Activity_UserProfile.tvusername != null) {

                            Activity_UserProfile.tvusername.setText(resp.getName());

                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (SharedPreference.getSharedPreferenceString(Activity_updateProfile.this, SharedprefKeys.PROFILE_COME_FROM, "").equals("fromlogin")) {
                        Intent intent = new Intent(Activity_updateProfile.this, Activity_ChooseSports.class);
                        startActivity(intent);
                    } else {

                        finish();
                    }

//                    dialogServerNotConnect();
                } else {
                    loader_dialog.dismiss();
                    dialogServerNotConnect(Activity_updateProfile.this, "NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<SaveProfiledataResponse> call, Throwable t) {
                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
                dialogServerNotConnect(Activity_updateProfile.this, t.getMessage());
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


    public String formatdateReturnDate(String date_str) throws ParseException {


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");

        Date date = new Date(sdf.parse(date_str).getTime());
        date.setMonth(date.getMonth());

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String convertedate = sdf2.format(date);

        return convertedate;
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

        validation_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_id_card));


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


    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        Activity_cam.showImagePickerOptions(this, new Activity_cam.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(Activity_updateProfile.this, Activity_cam.class);
        intent.putExtra(Activity_cam.INTENT_IMAGE_PICKER_OPTION, Activity_cam.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(Activity_cam.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(Activity_cam.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(Activity_cam.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(Activity_cam.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(Activity_cam.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(Activity_cam.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(Activity_updateProfile.this, Activity_cam.class);
        intent.putExtra(Activity_cam.INTENT_IMAGE_PICKER_OPTION, Activity_cam.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(Activity_cam.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(Activity_cam.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(Activity_cam.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    imageFilePath = bitmap.toString();
                    // loading profile image from local cache


//                    profile_pic_ioStream = Constant.bitmap2InputStream(bitmap);


                    String profile_pic_fileExt = getMimeType(getApplicationContext(), uri);

                    File file = FileUtil.from(this, uri);
//                    Log.e("PREVSIZE", getReadableFileSize(file.length()));

                    File compressedImageBitmap = new Compressor(Activity_updateProfile.this).compressToFile(file);
                    Log.e("SIZE", compressedImageBitmap.length() + "");

                    profile_pic_ioStream = new FileInputStream(compressedImageBitmap);


                    profile_image.setImageBitmap(bitmap);
//                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        updateLabel();
    }
}
