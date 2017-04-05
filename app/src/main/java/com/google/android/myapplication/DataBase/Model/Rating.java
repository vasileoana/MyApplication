package com.google.android.myapplication.DataBase.Model;

/**
 * Created by Oana on 06-Mar-17.
 */

public class Rating {

    public static final String TABLE = "Ratings";


    public static final String label_idRating="IdRating";
    public static final String label_rating="Description";

    private int idRating;
    private String rating;

    public Rating() {
    }


    public int getIdRating() {
        return idRating;
    }

    public void setIdRating(int idRating) {
        this.idRating = idRating;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "idRating=" + idRating +
                ", rating='" + rating + '\'' +
                '}';
    }
}
