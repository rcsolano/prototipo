package com.example.prototipo;

import android.app.Application;
import android.content.SharedPreferences;

public class clsGlobal extends Application {

    public static final String BASE_URL = "http://c1990672.ferozo.com/denunciavial/";

    private static clsGlobal singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

    }

    public static clsGlobal getInstance() {
        return singleton;
    }
}
