package com.example.flappybirdclone.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.flappybirdclone.R;
import com.example.flappybirdclone.activities.MainActivity;
import com.example.flappybirdclone.utils.PreferenceManager;
import com.example.flappybirdclone.utils.ScoreManager;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private Bird bird;
    private PipeManager pipeManager;
    private Score score;
    private Background background;
    private PreferenceManager preferenceManager;
    private Vibrator vibrator;
    private VibrationEffect gameoverVibrationEffect;
    private VibrationEffect flapVibrationEffect;
    private MediaPlayer backgroundMusic;
    private MediaPlayer hitSound;

    public GameView(Context context) {
        super(context);
        Log.d("GameView","GameView created");
        getHolder().addCallback(this);
        preferenceManager = PreferenceManager.getInstance(context);
        background = new Background(R.drawable.background, context);
        score = new Score(context);
        bird = new Bird(context);
        pipeManager = new PipeManager(context, score);
        gameThread = GameThread.getInstance(getHolder(), this);
        setFocusable(true);
        VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
        if (vibratorManager != null && PreferenceManager.getInstance(context).isVibrationEnabled()) {
            vibrator = vibratorManager.getDefaultVibrator();
        }
        gameoverVibrationEffect = VibrationEffect.createWaveform(new long[]{0, 100, 100, 200}, -1);
        flapVibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE);
        float volume = preferenceManager.getVolume() / 100f;
        backgroundMusic = MediaPlayer.create(context, R.raw.backgroundmusic);
        backgroundMusic.setVolume(volume, volume);
        backgroundMusic.setLooping(true);

        hitSound = MediaPlayer.create(context, R.raw.pipecollision);
        hitSound.setVolume(volume, volume);
    }

    public void pause() {
        Log.d("GameView", "GameView paused");
        gameThread.setRunning(false);
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void resume() {
        Log.d("GameView", "GameView resumed");
        gameThread = GameThread.getInstance(getHolder(), this);
        gameThread.setRunning(true);
        if (!gameThread.isAlive()) {
                gameThread.start();
        }
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.start();
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
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.start();
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
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
        backgroundMusic.release();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            gameThread.resumeGame();
            bird.flap();
            if (vibrator != null) {
                vibrator.vibrate(flapVibrationEffect);
            }
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
            score.draw(canvas);
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
        ScoreManager.saveScore(getContext(), score.getScore());
        showGameOverDialog();

        if (vibrator != null) {
            vibrator.vibrate(gameoverVibrationEffect);
        }

        hitSound.start();
    }

    private void showGameOverDialog() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View dialogView = inflater.inflate(R.layout.dialog_game_over, null);
                    builder.setView(dialogView)
                            .setCancelable(false);

                    AlertDialog dialog = builder.create();

                    dialogView.findViewById(R.id.returnButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            getContext().startActivity(intent);
                        }
                    });

                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
