package com.commit451.commitspiration;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Jawn on 5/22/2016.
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
