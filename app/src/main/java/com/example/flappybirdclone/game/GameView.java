package com.example.flappybirdclone.game;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.flappybirdclone.R;
import com.example.flappybirdclone.utils.PreferenceManager;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private Bird bird;
    private PipeManager pipeManager;

    private Background background;
    private PreferenceManager preferenceManager;

    public GameView(Context context) {
        super(context);
        Log.d("GameView","GameView created");
        getHolder().addCallback(this);
        preferenceManager = PreferenceManager.getInstance(context);
        background = new Background(R.drawable.background, context);
        bird = new Bird(context);
        pipeManager = new PipeManager(context);
        gameThread = GameThread.getInstance(getHolder(), this);
        setFocusable(true);
    }

    public void pause() {
        Log.d("GameView", "GameView paused");
        gameThread.setRunning(false);
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        Log.d("GameView", "GameView resumed");
        gameThread = GameThread.getInstance(getHolder(), this);
        gameThread.setRunning(true);
        if (!gameThread.isAlive()) {
            if (gameThread != null)
            gameThread.start();
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.d("GameView", "Surface created");
        gameThread = GameThread.getInstance(getHolder(), this);
        if (!gameThread.isAlive()) {
            gameThread.setRunning(true);
            gameThread.start();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.d("GameView", "Surface destroyed");
        gameThread.setRunning(false);
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                GameThread.destroyInstance();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameThread = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            gameThread.resumeGame();
            bird.flap();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            Log.d("GameView", "Drawing bird and pipes");
            background.draw(canvas);
            bird.draw(canvas);
            pipeManager.draw(canvas);
            // Draw other elements (score)
        }
    }

    public void update() {
        Log.d("GameView", "Update called");
        bird.update();
        pipeManager.update();
        background.update();
        checkCollisions();
    }

    private void checkCollisions() {
        if (pipeManager.checkCollision(bird) || bird.isOffScreen()) {
            gameOver();
        }
    }

    private void gameOver() {
        Log.d("GameView", "Game over");
        if (gameThread != null) gameThread.setRunning(false);
    }
}
