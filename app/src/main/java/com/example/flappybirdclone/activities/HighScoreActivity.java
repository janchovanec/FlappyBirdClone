package com.example.flappybirdclone.activities;

import static android.graphics.Color.argb;
import static android.graphics.Color.rgb;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;

import com.example.flappybirdclone.utils.ScoreManager;
import com.example.flappybirdclone.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.util.Pair;
import android.widget.TableRow;
import android.widget.TextView;


public class HighScoreActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs);

        TableLayout tableLayout = findViewById(R.id.highScoresTable);
        List< Pair<Date, Integer> > highScores = ScoreManager.getScores(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        boolean isEven = true;

        for (Pair<Date, Integer> score : highScores) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            if (isEven) {
                row.setBackgroundColor(argb(127, 255, 255, 255));
                isEven = false;
            } else {
                row.setBackgroundColor(argb(127, 160, 160, 160));
                isEven = true;
            }

            TextView date = new TextView(this);
            date.setText(dateFormat.format(score.first));
            date.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            date.setGravity(Gravity.START);
            date.setTextColor(Color.BLACK);
            date.setTextSize(22);
            date.setPadding(20, 0, 10, 0);
            row.addView(date);


            TextView scoretxt = new TextView(this);
            scoretxt.setText(score.second.toString());
            scoretxt.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            scoretxt.setGravity(android.view.Gravity.END);
            scoretxt.setTextColor(Color.BLACK);
            scoretxt.setTextSize(22);
            scoretxt.setPadding(10, 0, 20, 0);
            row.addView(scoretxt);


            tableLayout.addView(row);
        }
    }
}
