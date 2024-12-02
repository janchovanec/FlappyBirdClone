package com.example.flappybirdclone.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.flappybirdclone.game.GameView;

public class GameActivity extends Activity {
    private GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("GameActivity", "GameActivity created");
        gameView = new GameView(this);

        setContentView(gameView);
    }

    @Override
    public void onPause() {
        super.onPause();

        gameView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        gameView.resume();
    }

}
