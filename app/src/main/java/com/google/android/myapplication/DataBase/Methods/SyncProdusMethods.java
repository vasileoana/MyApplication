package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.DataBase.Model.Rating;
import com.google.android.myapplication.DataBase.Model.SyncProdus;
import com.google.android.myapplication.Utilities.DataBase.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 02-Jul-17.
 */

public class SyncProdusMethods {

    public static String create() {
        return "CREATE TABLE IF NOT EXISTS " + SyncProdus.TABLE + " ( " + SyncProdus.label_idProdus + " INTEGER, " + SyncProdus.label_editat + " BOOLEAN, " + SyncProdus.label_sters + " BOOLEAN);";

    }


    public long insert(SyncProdus syncProdus) {
        long code = 0;
        try {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(SyncProdus.label_idProdus, syncProdus.getIdProdus());
            values.put(SyncProdus.label_editat, syncProdus.isEditat());
            values.put(SyncProdus.label_sters, syncProdus.isSters());

            // Inserting Row
            code = db.insertWithOnConflict(SyncProdus.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return code;
    }


    public List<Integer> selectEditate() {
        List<Integer> listaEditate = new ArrayList<>();
        int id;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + SyncProdus.TABLE + " WHERE " + SyncProdus.label_editat + " = 1";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SyncProdus.label_idProdus)));
                listaEditate.add(id);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return listaEditate;
    }

    public List<Integer> selectSterse() {
        List<Integer> listaEditate = new ArrayList<>();
        int id;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + SyncProdus.TABLE + " WHERE " + SyncProdus.label_sters + " = 1";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SyncProdus.label_idProdus)));
                listaEditate.add(id);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return listaEditate;
    }


    public void delete() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(SyncProdus.TABLE, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

}
