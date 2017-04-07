package com.google.android.myapplication.DataBase.Model;

/**
 * Created by Oana on 05-Apr-17.
 */

public class ProductAnalysis {

    public static final String TABLE = "ProductAnalysis";

    public static final String label_idProductAnalysis="IdProductAnalysis";
    public static final String label_idProduct="IdProduct";
    public static final String label_idUser="IdUser";
    public static final String label_date="Date";


    private int idProductAnalysis;
    private int idProduct;
    private int idUser;
    private String date;

    public ProductAnalysis() {
    }

    public ProductAnalysis(int idProduct, int idUser, String date) {
        this.idProduct = idProduct;
        this.idUser = idUser;
        this.date = date;
    }

    public int getIdProductAnalysis() {
        return idProductAnalysis;
    }

    public void setIdProductAnalysis(int idProductAnalysis) {
        this.idProductAnalysis = idProductAnalysis;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ProductAnalysis{" +
                "idProductAnalysis=" + idProductAnalysis +
                ", idProduct=" + idProduct +
                ", idUser=" + idUser +
                ", date='" + date + '\'' +
                '}';
    }
}
