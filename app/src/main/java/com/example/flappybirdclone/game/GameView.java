package com.example.flappybirdclone.game;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

    public GameView(Context context) {
        super(context);
        Log.d("GameView","GameView created");
    }

    public void pause() {
        Log.d("GameView", "GameView paused");
    }

    public void resume() {
        Log.d("GameView", "GameView resumed");
    }
}
