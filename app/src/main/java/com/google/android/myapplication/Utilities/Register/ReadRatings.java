package com.google.android.myapplication.Utilities.Register;

import android.content.res.AssetManager;

import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Model.Rating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Oana on 25-Jun-17.
 */

public class ReadRatings {

    public void readRatings(AssetManager assetManager,RatingMethods ratingMethods){
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader reader;

        try {
            inputStream=assetManager.open("ratings.csv");
            inputStreamReader=new InputStreamReader(inputStream);
            reader=new BufferedReader(inputStreamReader);
            String line;
            Rating rating=new Rating();
            while((line = reader.readLine()) != null) {
                String[] entry = line.split(",");
                String ratingName = entry[0];
                int idRating = Integer.parseInt(entry[1]);
                rating.setRating(ratingName);
                rating.setIdRating(idRating);
                ratingMethods.insert(rating);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
