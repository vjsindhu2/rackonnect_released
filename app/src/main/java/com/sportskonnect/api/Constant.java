package com.sportskonnect.api;

import android.graphics.Bitmap;
import android.util.Log;

import com.sportskonnect.R;
import com.sportskonnect.helpers.AppConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constant {
    //    public static final String WEB_URL = "http://thinklabs.io/";
    public static final String WEB_URL = "http://rackonnect.com/";
    public static final String GOOGLE_WEB_URL = "https://maps.googleapis.com/";

    public static final String CHAT_SERVER_URL = "http://3.18.225.188:7007";
    private static final boolean DEBUGGING_MODE = true;


    public static String getGameNameFromcatId(String cat_id) {

        String game_name;
        if (cat_id.equals("1")) {

//            Picasso.get().load(R.drawable.squash2).into(holder.game_img);

            game_name = "SQUASH";
            return game_name;

        } else if (cat_id.equals("2")) {

//            Picasso.get().load(R.drawable.table_tennis).into(holder.game_img);
            game_name = "TABLE TENNIS";
            return game_name;

        } else if (cat_id.equals("3")) {

//            Picasso.get().load(R.drawable.badminton1).into(holder.game_img);
            game_name = "BADMINTON";
            return game_name;


        } else if (cat_id.equals("4")) {

//            Picasso.get().load(R.drawable.tennis2).into(holder.game_img);

            game_name = "TENNIS";
            return game_name;

        } else {

            return "";
        }
    }


    public static String getGameLevelFromlevelId(String level_id) {

        String level_name;
        if (level_id.equals("1")) {

//            Picasso.get().load(R.drawable.squash2).into(holder.game_img);

            level_name = "Amateur";
            return level_name;

        } else if (level_id.equals("2")) {

//            Picasso.get().load(R.drawable.table_tennis).into(holder.game_img);
            level_name = "Semi Pro";
            return level_name;

        } else if (level_id.equals("3")) {

//            Picasso.get().load(R.drawable.table_tennis).into(holder.game_img);
            level_name = "Pro";
            return level_name;

        } else if (level_id.equals("4")) {

//            Picasso.get().load(R.drawable.table_tennis).into(holder.game_img);
            level_name = "Expert";
            return level_name;

        } else {

            return "";
        }
    }


    public static int getGameImageFromcatId(String cat_id) {

        int game_image;
        if (cat_id.equals("1")) {

//            Picasso.get().load(R.drawable.squash2).into(holder.game_img);

            return R.drawable.squash2;

        } else if (cat_id.equals("2")) {

//            Picasso.get().load(R.drawable.table_tennis).into(holder.game_img);
            return R.drawable.table_tennis;

        } else if (cat_id.equals("3")) {

//            Picasso.get().load(R.drawable.badminton1).into(holder.game_img);
            return R.drawable.badminton1;


        } else if (cat_id.equals("4")) {

//            Picasso.get().load(R.drawable.tennis2).into(holder.game_img);

            return R.drawable.tennis2;


        } else {

            return 0;
        }
    }


    public static int getGamecoloredImgFromcatId(String cat_id) {

        int game_image;
        if (cat_id.equals("1")) {

//            Picasso.get().load(R.drawable.squash2).into(holder.game_img);

            return R.drawable.ic_3;

        } else if (cat_id.equals("2")) {

//            Picasso.get().load(R.drawable.table_tennis).into(holder.game_img);
            return R.drawable.ic_4;

        } else if (cat_id.equals("3")) {

//            Picasso.get().load(R.drawable.badminton1).into(holder.game_img);
            return R.drawable.badminton1;


        } else if (cat_id.equals("4")) {

//            Picasso.get().load(R.drawable.tennis2).into(holder.game_img);

            return R.drawable.tennis2;


        } else {

            return 0;
        }
    }


    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        // haversine great circle distance approximation, returns meters
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60; // 60 nautical miles per degree of seperation
        dist = dist * 1852; // 1852 meters per nautical mile
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



    public static String convet24hourTo12hour(String time) {
        String convertedtime = "";

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            final Date dateObj = sdf.parse(time);
            convertedtime = new SimpleDateFormat("hh:mm aa").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return convertedtime;
    }




    /**
     * method for LogCat
     *
     * @param Message this is  parameter for LogCat  method
     */
    public static void LogCat(String Message) {
        if (Constant.DEBUGGING_MODE)
            Log.d("CONNECTION_STATUS", Message);
    }


    public static InputStream bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }//from   w ww.j a v a  2  s.  c  o  m

}
