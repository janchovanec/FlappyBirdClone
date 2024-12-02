package com.example.flappybirdclone.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import com.example.flappybirdclone.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PipeManager {
    private List<Pipe> pipes;
    private Bitmap pipeBitmap;
    private Context context;
    private Random random;
    private int screenWidth, screenHeight;
    private static final int VER_GAP = 500;
    private static final int HOR_GAP = 800;
    private Score score;
    public PipeManager(Context context, Score score) {
        this.context = context;
        this.score = score;
        pipes = new ArrayList<Pipe>();
        random = new Random();
        pipeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        pipeBitmap = Bitmap.createScaledBitmap(pipeBitmap, 200, screenHeight, false);
    }

    public void update() {
        if (pipes.isEmpty() || screenWidth - pipes.get(pipes.size() - 1).getX() > HOR_GAP) {
            addPipe();
        }

        for (Pipe pipe : pipes) {
            if (pipe.getX() + pipe.getWidth() < screenWidth / 3f && !pipe.isPassed() && !pipe.isInverted()) {
                score.incrementScore();
                pipe.setPassed(true);
            }
        }

        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe pipe = iterator.next();
            pipe.update();
            if (pipe.isOffScreen()) {
                iterator.remove();
            }
        }
    }

    public void draw(Canvas canvas) {
        for (Pipe pipe: pipes) {
            pipe.draw(canvas);
        }
    }

    private void addPipe() {
        float minY = VER_GAP;
        float maxY = screenHeight - VER_GAP;
        float yPosition = minY + random.nextFloat() * (maxY - minY);

        // Random coefficient 80% to 120%
        float randCoeff = random.nextFloat() * 0.4f + 0.8f;
        Log.wtf("PipeManager", "randCoeff: " + randCoeff);

        Bitmap invertedPipe = Bitmap.createBitmap(pipeBitmap, 0, 0, pipeBitmap.getWidth(), pipeBitmap.getHeight(), null, true);
        pipes.add(new Pipe(invertedPipe, screenWidth, yPosition - VER_GAP * randCoeff - pipeBitmap.getHeight(), true));

        pipes.add(new Pipe(pipeBitmap, screenWidth, yPosition, false));
    }

    public boolean checkCollision(Bird bird) {
        for (Pipe pipe : pipes) {
            if (RectF.intersects(bird.getHitbox(), pipe.getHitbox())) return true;
        }
        return false;
    }

    public List<Pipe> getPipes() {
        return pipes;
    }
}
