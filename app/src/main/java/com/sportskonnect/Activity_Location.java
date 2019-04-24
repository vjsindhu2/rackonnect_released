package com.sportskonnect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;


public class Activity_Location extends AppCompatActivity {

    private static final int TAKE_PICTURE = 1;
    private static final int REQUEST_READ_PERMISSION = 100;
    private Uri capturedImageUri;
    private String selectedImagePath;
    private Bitmap bitmap;
    private ExifInterface exifObject;

    private LocationTrack locationTrack;
    Button btn;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getLocation(v);
            }
        });

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLocation(View view) {
        locationTrack = new LocationTrack(Activity_Location.this);
        if (locationTrack.canGetLocation()) {
            if (ContextCompat.checkSelfPermission(Activity_Location.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Activity_Location.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

            } else {

                captureImage();
            }
            latitude = locationTrack.getLatitude();
            longitude = locationTrack.getLongitude();
            // Toast.makeText(Activity_Location.this, latitude + " " + longitude, Toast.LENGTH_SHORT).show();

        } else {
            showSettingsAlert();
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            String result = data.toURI();
//            capturedImageUri = data.getData();
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_Location.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
                } else {
//                    selectedImagePath = getRealPathFromURIPath(capturedImageUri);
//                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), capturedImageUri);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    capturedImageUri = getImageUri(getApplicationContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(capturedImageUri));
                    selectedImagePath = getRealPathFromURI(capturedImageUri);

                    Log.d("selectedImage", selectedImagePath);
//                    try{
//                        Log.d("latiDouble", ""+latitude);
//                        Log.d("longiDouble", ""+longitude);
//                        exifObject = new ExifInterface(selectedImagePath);
//                        if (exifObject != null) {
//                            double latitu = latitude;
//                            double longitu = longitude;
//                            double alat = Math.abs(latitu);
//                            double along = Math.abs(longitu);
//
//
//                            exifObject.setAttribute(ExifInterface.TAG_GPS_LATITUDE, String.valueOf(alat));
//                            exifObject.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, String.valueOf(along));
//
//                            exifObject.saveAttributes();
//
//                            String lati = exifObject.getAttribute (ExifInterface.TAG_GPS_LATITUDE);
//                            String longi = exifObject.getAttribute (ExifInterface.TAG_GPS_LONGITUDE);
//                            Log.d("latiResult", ""+ lati);
//                            Log.d("longiResult", ""+ longi);
//                        }
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    capturedPhoto.setImageBitmap(photo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PERMISSION) {
            if (grantResults.length == 0) {
                // permission denied
            } else {
                // permission granted
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_Location.this);
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}

