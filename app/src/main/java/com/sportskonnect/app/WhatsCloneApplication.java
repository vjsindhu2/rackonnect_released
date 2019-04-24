package com.sportskonnect.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.sportskonnect.helpers.AppConstants;
import com.sportskonnect.services.MainService;

import androidx.multidex.MultiDex;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Abderrahim El imame on 20/02/2016.
 * Email : abderrahim.elimame@gmail.com
 */
public class WhatsCloneApplication extends Application {
    private static Context AppContext;

    public RealmConfiguration config;

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
        AppContext = getApplicationContext();

        Realm.init(this);
        config = new RealmConfiguration.Builder()
                .name(AppConstants.DATABASE_LOCAL_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
//        if (PreferenceManager.getToken(this) != null) {
        startService(new Intent(this, MainService.class));
//        }


    }


    public static Context getAppContext() {
        return AppContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        MainService.disconnectSocket();


    }


}
