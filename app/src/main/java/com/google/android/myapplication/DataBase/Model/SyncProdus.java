package com.google.android.myapplication.DataBase.Model;

/**
 * Created by Oana on 02-Jul-17.
 */

public class SyncProdus {

    public static final String TABLE = "SyncProdus";


    public static final String label_idProdus="idProdus";
    public static final String label_editat="editat";
    public static final String label_sters="sters";

    int idProdus;
    boolean editat;
    boolean sters;

    public SyncProdus(int idProdus, boolean editat, boolean sters) {
        this.idProdus = idProdus;
        this.editat = editat;
        this.sters = sters;
    }

    public SyncProdus() {
    }


    public int getIdProdus() {
        return idProdus;
    }

    public void setIdProdus(int intIdProdus) {
        this.idProdus = intIdProdus;
    }

    public boolean isEditat() {
        return editat;
    }

    public void setEditat(boolean editat) {
        this.editat = editat;
    }

    public boolean isSters() {
        return sters;
    }

    public void setSters(boolean sters) {
        this.sters = sters;
    }
}
