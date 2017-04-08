package com.google.android.myapplication.Utilities.DataBase;

import android.app.Application;
import android.content.Context;

/**
 * Created by Oana on 26-Mar-17.
 */

public class InitializeDB extends Application {

    static  Context context;
    static DBHelper dbHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this.getApplicationContext();
        dbHelper = new DBHelper();
        DatabaseManager.initializeInstance(dbHelper);

    }

    public static Context getContext(){
        return context;
    }


}
