package com.sportskonnect.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.sportskonnect.Activity_Otp;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySMSBroadcastReceiver extends BroadcastReceiver {

    public String smsotp;
    public static String val="";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents

                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);

                    Pattern pattern = Pattern.compile("(\\d{4})");
                    Matcher matcher = pattern.matcher(message);

                    if (matcher.find()) {
                        val = matcher.group(0);  // 4 digit number
                    }


//                    SharedPreference.setSharedPreferenceString(context,SharedprefKeys.CURRENT_SMS_OTP,val);


                    setsmsotp(val);

                    Activity_Otp.etMobile.setText(val+"");
/*                    Intent hhtpIntent = new Intent(context, Activity_Otp.class);
                    hhtpIntent.putExtra("otp", val);
                    context.startService(hhtpIntent);*/
                    Log.d("TAG", "OTP: "+val+"");

                    break;
                case CommonStatusCodes.TIMEOUT:
                    Log.d("TAG", "timed out (5 minutes)");
                    break;
            }
        }
    }

    public void setsmsotp(String msg) {
        this.smsotp = msg;
    }

    public String getSmsotp (){
        return smsotp;
    }
}