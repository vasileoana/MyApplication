package com.google.android.myapplication.Utilities;

import android.app.Application;
import android.content.Context;

import com.google.android.myapplication.DataBase.DBHelper;
import com.google.android.myapplication.DataBase.DatabaseManager;

/**
 * Created by Oana on 26-Mar-17.
 */

public class App extends Application {

    private static Context context;
    private static DBHelper dbHelper;

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
