package com.sportskonnect.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import com.sportskonnect.Activity_Badminton;
import com.sportskonnect.Activity_TournamentChat;
import com.sportskonnect.R;
import com.sportskonnect.api.Constant;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;


public class NotificationsManager {


    private static NotificationManager mNotificationManager;
    private static String username;
    private static int numMessages = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void showUserNotification(Context mContext, Intent resultIntent, String phone, String text, int userId, String Avatar) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        // Adds the back stack
        stackBuilder.addParentStack(Activity_Badminton.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder mNotifyBuilder;
        try {
            String name = SharedPreference.getSharedPreferenceString(mContext,SharedprefKeys.NOTISENDERNAME,"");

            SharedPreference.setSharedPreferenceString(mContext, SharedprefKeys.OPPONENT_ID, String.valueOf(userId));
            SharedPreference.setSharedPreferenceString(mContext, SharedprefKeys.OPPONENT_NAME, name);
//            SharedPreference.setSharedPreferenceString(mContext, SharedprefKeys.OPPONENT_LEVEL, String.valueOf(opponetsDatum.getLevelid()));
            SharedPreference.setSharedPreferenceString(mContext,SharedprefKeys.CHATCOMEFROM,"0");
            if (name != null) {
                username = name;
            } else {
                username = phone;
            }

        } catch (Exception e) {
            Constant.LogCat(" " + e.getMessage());
        }
        ++numMessages;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyBuilder = new NotificationCompat.Builder(mContext)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.logo, mContext.getString(R.string.reply_message), resultPendingIntent)
                .setContentTitle(username)
                .setContentText(text)
                .setNumber(numMessages)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(resultPendingIntent)
                .setPriority(Notification.PRIORITY_HIGH);


/*        if (FilesManager.isFileImagesProfileExists(FilesManager.getProfileImage(String.valueOf(userId), username))) {

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mNotifyBuilder.setLargeIcon(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    mNotifyBuilder.setLargeIcon(drawableToBitmap(errorDrawable));
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    mNotifyBuilder.setLargeIcon(drawableToBitmap(placeHolderDrawable));
                }
            };
            Picasso.with(mContext)
                    .load(FilesManager.getFileImageProfile(String.valueOf(userId), username))
                    .transform(new com.sourcecanyon.whatsClone.ui.CropSquareTransformation())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(target);

        } else {

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mNotifyBuilder.setLargeIcon(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    mNotifyBuilder.setLargeIcon(drawableToBitmap(errorDrawable));
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    mNotifyBuilder.setLargeIcon(drawableToBitmap(placeHolderDrawable));
                }
            };
            Picasso.with(mContext)
                    .load(EndPoints.BASE_URL + Avatar)
                    .transform(new com.sourcecanyon.whatsClone.ui.CropSquareTransformation())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(target);
        }*/


        if (PreferenceSettingsManager.conversation_tones(mContext)) {

            Uri uri = PreferenceSettingsManager.getDefault_message_notifications_settings_tone(mContext);
            if (uri != null)
                mNotifyBuilder.setSound(uri);
            else {
                int defaults = 0;
                defaults = defaults | Notification.DEFAULT_SOUND;
                mNotifyBuilder.setDefaults(defaults);
            }


        }

        if (PreferenceSettingsManager.getDefault_message_notifications_settings_vibrate(mContext)) {
            long[] vibrate = new long[]{2000, 2000, 2000, 2000, 2000};
            mNotifyBuilder.setVibrate(vibrate);
        } else {
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            mNotifyBuilder.setDefaults(defaults);
        }


        String colorLight = PreferenceSettingsManager.getDefault_message_notifications_settings_light(mContext);
        if (colorLight != null) {
            mNotifyBuilder.setLights(Color.parseColor(colorLight), 1500, 1500);
        } else {
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            mNotifyBuilder.setDefaults(defaults);
        }


        mNotifyBuilder.setAutoCancel(true);

        mNotificationManager.notify(userId, mNotifyBuilder.build());

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void showGroupNotification(Context mContext, Intent resultIntent, String groupName, String text, int groupId, String Avatar) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        // Adds the back stack
        stackBuilder.addParentStack(Activity_Badminton.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder mNotifyBuilder;


        ++numMessages;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyBuilder = new NotificationCompat.Builder(mContext)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .addAction(R.drawable.logo, mContext.getString(R.string.reply_message), resultPendingIntent)
                .setContentTitle(groupName)
                .setContentText(text)
                .setNumber(numMessages)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(resultPendingIntent)
                .setPriority(Notification.PRIORITY_HIGH);

/*
        if (FilesManager.isFileImagesProfileExists(FilesManager.getProfileImage(String.valueOf(groupId), groupName))) {

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mNotifyBuilder.setLargeIcon(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    mNotifyBuilder.setLargeIcon(drawableToBitmap(errorDrawable));
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    mNotifyBuilder.setLargeIcon(drawableToBitmap(placeHolderDrawable));
                }
            };
            Picasso.with(mContext)
                    .load(FilesManager.getFileImageProfile(String.valueOf(groupId), username))
                    .transform(new com.sourcecanyon.whatsClone.ui.CropSquareTransformation())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(target);

        } else {

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mNotifyBuilder.setLargeIcon(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    mNotifyBuilder.setLargeIcon(drawableToBitmap(errorDrawable));
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    mNotifyBuilder.setLargeIcon(drawableToBitmap(placeHolderDrawable));
                }
            };
            Picasso.with(mContext)
                    .load(EndPoints.BASE_URL + Avatar)
                    .transform(new com.sourcecanyon.whatsClone.ui.CropSquareTransformation())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(target);
        }
*/

        mNotifyBuilder.setAutoCancel(true);


        if (PreferenceSettingsManager.conversation_tones(mContext)) {

            Uri uri = PreferenceSettingsManager.getDefault_message_group_notifications_settings_tone(mContext);
            if (uri != null)
                mNotifyBuilder.setSound(uri);
            else {
                int defaults = 0;
                defaults = defaults | Notification.DEFAULT_SOUND;
                mNotifyBuilder.setDefaults(defaults);
            }


        }

 /*       if (PreferenceSettingsManager.getDefault_message_group_notifications_settings_vibrate(mContext)) {
            long[] vibrate = new long[]{2000, 2000, 2000, 2000, 2000};
            mNotifyBuilder.setVibrate(vibrate);
        } else {
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            mNotifyBuilder.setDefaults(defaults);
        }
*/

      /*  String colorLight = PreferenceSettingsManager.getDefault_message_group_notifications_settings_light(mContext);
        if (colorLight != null) {
            mNotifyBuilder.setLights(Color.parseColor(colorLight), 1500, 1500);
        } else {
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            mNotifyBuilder.setDefaults(defaults);
        }
*/

        mNotificationManager.notify(groupId, mNotifyBuilder.build());

    }

    /**
     * method to get manager for notification
     */
    public static boolean getManager() {
        if (mNotificationManager != null) {
            return true;
        } else {
            return false;
        }

    }

    /***
     * method to cancel a specific notification
     *
     * @param index
     */
    public static void cancelNotification(int index) {
        numMessages = 0;
        mNotificationManager.cancel(index);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
