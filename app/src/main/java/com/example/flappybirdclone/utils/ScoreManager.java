// ScoreManager.java
package com.example.flappybirdclone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ScoreManager {
    private static final String PREFS_NAME = "HighScores";
    private static final String SCORES_KEY = "Scores";

    public static void saveScore(Context context, int score) {
        Log.d("ScoreManager", "Saving score: " + score);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        List<Pair<Date, Integer>> scores = getScores(context);
        scores.add(new Pair<>(new Date(), score));
        Collections.sort(scores, (a, b) -> b.second.compareTo(a.second));

        if (scores.size() > 20) {
            scores = scores.subList(0, 20);
        }

        StringBuilder scoresString = new StringBuilder();
        for (Pair<Date, Integer> scorePair : scores) {
            scoresString.append(scorePair.first.getTime()).append(":").append(scorePair.second).append(",");
        }

        editor.putString(SCORES_KEY, scoresString.toString());
        editor.apply();
    }

    public static List<Pair<Date, Integer>> getScores(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String scoresString = prefs.getString(SCORES_KEY, "");

            List<Pair<Date, Integer>> scores = new ArrayList<>();
            if (!scoresString.isEmpty()) {
                String[] scoresArray = scoresString.split(",");
                for (String score : scoresArray) {
                    if (score.isEmpty()) continue;
                    String[] scoreParts = score.split(":");
                    scores.add(new Pair<>(new Date(Long.parseLong(scoreParts[0])), Integer.parseInt(scoreParts[1])));
                }
            }

            return scores;
        } catch (Exception e) {
            Log.e("ScoreManager", "Error getting scores", e);
            return new ArrayList<>();
        }
    }
}