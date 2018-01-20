package com.example.mostafa.graduationproject;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by ahmed on 12/18/17.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
