package com.example.flappybirdclone.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private static GameThread instance;

    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean paused;
    private volatile boolean running;
    private GameThread(SurfaceHolder holder, GameView gameView) {
        this.gameView = gameView;
        this.surfaceHolder = holder;
        this.paused = true;
    }

    public void pauseGame() {
        paused = true;
    }

    public void resumeGame() {
        paused = false;
    }

    public static GameThread getInstance(SurfaceHolder holder, GameView gameView) {
        if (instance == null) {
            instance = new GameThread(holder, gameView);
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public void setRunning(boolean isRunning) {
        synchronized (this) {
            this.running = isRunning;
        }
    }

    @Override
    public void run() {
        while (running) {
            while (paused) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gameView.update();
                    gameView.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }
    }
}
