package com.google.android.myapplication.DataBase.Model;

/**
 * Created by Oana on 06-Mar-17.
 */

public class Ingredient {

    public static final String TABLE = "Ingredient";

    public static final String label_idIngredient="IdIngredient";
    public static final String label_idRating="IdRating";
    public static final String label_name="Name";
    public static final String label_description="Description";

    private int idIngredient;
    private int idRating;
    private String description;
    private String name;


    public Ingredient() {
    }

    public int getIdIngredient() {
        return idIngredient;
    }

    public int getIdRating() {
        return idRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdRating(int idRating) {
        this.idRating = idRating;
    }

    public void setIdIngredient(int idIngredient) {
        this.idIngredient = idIngredient;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "idIngredient=" + idIngredient +
                ", idRating=" + idRating +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
