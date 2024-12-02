package com.example.flappybirdclone.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;

import com.example.flappybirdclone.R;
import com.example.flappybirdclone.utils.PreferenceManager;

// Class to keep and draw the in-game score
public class Score {
    private int screenWidth, screenHeight;
    private int score;
    private MediaPlayer scoreSound;

        public Score(Context context) {
            this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            this.screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            score = 0;
            scoreSound = MediaPlayer.create(context, R.raw.score);
            float volume = PreferenceManager.getInstance(context).getVolume() / 100f;
            scoreSound.setVolume(volume, volume);
        }

        public void incrementScore() {
            score++;
            scoreSound.start();
        }

        public int getScore() {
            return score;
        }

        public void resetScore() {
            score = 0;
        }

        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(screenWidth*0.2f);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setFakeBoldText(true);
            paint.setShadowLayer(10, 5, 5, Color.BLACK);
            canvas.drawText(String.valueOf(score), screenWidth * 0.5f, screenHeight * 0.2f, paint);
        }
}
