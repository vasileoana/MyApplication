package com.google.android.myapplication.DataBase.Model;

/**
 * Created by Oana on 06-Mar-17.
 */

public class IngredientAnalysis {

    public static final String TABLE = "IngredientAnalyses";

    public static final String label_idAnalysis="IdIngredientAnalysis";
    public static final String label_idIngredient="IdIngredient";
    public static final String label_idProduct="IdProduct";



    private int idAnalysis;
    private int idProduct;
    private int IdIngredient;

    public IngredientAnalysis() {
    }

    public int getIdAnalysis() {
        return idAnalysis;
    }

    public void setIdAnalysis(int idAnalysis) {
        this.idAnalysis = idAnalysis;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdIngredient() {
        return IdIngredient;
    }

    public void setIdIngredient(int idIngredient) {
        IdIngredient = idIngredient;
    }


}
