package com.google.android.myapplication.Utilities.Ocr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 26-Apr-17.
 */

public class LevenshteinDistanceSearch {

    public static int LevenshteinDistance(String src, String dest)
    {
        int[][] d = new int[src.length() + 1][ dest.length() + 1];
        int i, j, cost;
        char[] str1 = src.toCharArray();
        char[] str2 = dest.toCharArray();

        for (i = 0; i <= str1.length; i++)
        {
            d[i][ 0] = i;
        }
        for (j = 0; j <= str2.length; j++)
        {
            d[0][ j] = j;
        }
        for (i = 1; i <= str1.length; i++)
        {
            for (j = 1; j <= str2.length; j++)
            {

                if (str1[i - 1] == str2[j - 1])
                    cost = 0;
                else
                    cost = 1;

                d[i][j] =
                        Math.min(
                                d[i - 1][j] + 1,              // Deletion
                                Math.min(
                                        d[i][ j - 1] + 1,          // Insertion
                                        d[i - 1][j - 1] + cost)); // Substitution

                if ((i > 1) && (j > 1) && (str1[i - 1] ==
                        str2[j - 2]) && (str1[i - 2] == str2[j - 1]))
                {
                    d[i][j] = Math.min(d[i][ j], d[i - 2][j - 2] + cost);
                }
            }
        }

        return d[str1.length][str2.length];
    }


    public static List<String> Search(String word, List<String> wordList,double fuzzyness)
    {
        List<String> foundWords = new ArrayList<>();
        double maxFuzy = 0;
        for (String s : wordList)
        {
            // Calculate the Levenshtein-distance:
            int levenshteinDistance =
                    LevenshteinDistance(word, s);

            // Length of the longer string:
            int length = Math.max(word.length(), s.length());

            // Calculate the score:
            double score = 1.0 - (double)levenshteinDistance / length;

            // Match?
            /*if (score > fuzzyness) {
                foundWords.add(s);
            }*/
            if (score > maxFuzy) {
                foundWords.clear();
                foundWords.add(s);
                maxFuzy = score;
            } else if (score == maxFuzy){
                foundWords.add(s);
            }
        }
        return foundWords;
    }


}
