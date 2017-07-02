package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.Utilities.DataBase.DatabaseManager;
import com.google.android.myapplication.DataBase.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 27-Feb-17.
 */

public class UserMethods {


    public static String create() {
        return "CREATE TABLE IF NOT EXISTS " + User.TABLE + " ( " + User.label_idUser + " INTEGER PRIMARY KEY, "
                + User.label_username + " TEXT UNIQUE, "
                + User.label_password + " TEXT, " + User.label_email + " TEXT UNIQUE);";

    }


    public long insert(User user) {
        long code;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(User.label_idUser, user.getIdUser());
        values.put(User.label_username, user.getUsername());
        values.put(User.label_email, user.getEmail());
        values.put(User.label_password, user.getPassword());

        // Inserting Row
        code = db.insertWithOnConflict(User.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        DatabaseManager.getInstance().closeDatabase();

        return code;
    }

    public void delete() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(User.TABLE, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<User> select() {
        List<User> users = new ArrayList<>();
        User user;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + User.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setIdUser(Integer.parseInt(cursor.getString(cursor.getColumnIndex(User.label_idUser))));
                user.setUsername(cursor.getString(cursor.getColumnIndex(User.label_username)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(User.label_password)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(User.label_email)));
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();


        return users;
    }


    public User selectUser(String username, String pass) {
        User user = null;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + User.TABLE + " WHERE " +
                User.label_username + " LIKE '" + username + "' and " +
                User.label_password + " LIKE '" + pass + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            user = new User();
            user.setIdUser(Integer.parseInt(cursor.getString(cursor.getColumnIndex(User.label_idUser))));
            user.setUsername(cursor.getString(cursor.getColumnIndex(User.label_username)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(User.label_password)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(User.label_email)));

        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return user;

    }


    public User selectUserById(int id) {
        User user = null;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + User.TABLE + " WHERE " + User.label_idUser + " LIKE '" + id + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            user = new User();
            user.setIdUser(Integer.parseInt(cursor.getString(cursor.getColumnIndex(User.label_idUser))));
            user.setUsername(cursor.getString(cursor.getColumnIndex(User.label_username)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(User.label_password)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(User.label_email)));

        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return user;

    }

    public long update(User user) {
        long code;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(User.label_username, user.getUsername());
        values.put(User.label_email, user.getEmail());
        values.put(User.label_password, user.getPassword());
        code = db.updateWithOnConflict(User.TABLE, values, User.label_idUser + "=?", new String[]{String.valueOf(user.getIdUser())}, SQLiteDatabase.CONFLICT_IGNORE);
        DatabaseManager.getInstance().closeDatabase();
        return code;
    }
}


