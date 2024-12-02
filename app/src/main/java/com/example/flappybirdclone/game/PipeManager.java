package com.example.flappybirdclone.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

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
    private static final int VER_GAP = 400;
    private static final int HOR_GAP = 800;
    public PipeManager(Context context) {
        this.context = context;
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

        Bitmap invertedPipe = Bitmap.createBitmap(pipeBitmap, 0, 0, pipeBitmap.getWidth(), pipeBitmap.getHeight(), null, true);
        pipes.add(new Pipe(invertedPipe, screenWidth, yPosition - VER_GAP - pipeBitmap.getHeight()));

        pipes.add(new Pipe(pipeBitmap, screenWidth, yPosition));
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
