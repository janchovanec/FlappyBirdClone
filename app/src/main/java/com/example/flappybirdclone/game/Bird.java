package com.example.flappybirdclone.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.flappybirdclone.R;
import com.example.flappybirdclone.utils.PreferenceManager;

public class Bird {
    private Bitmap bitmap;
    private float x, y, velocity;
    private RectF hitbox;
    private PreferenceManager preferenceManager;
    private Context context;

    private static final float GRAVITY = 0.5f;
    private static final float FLAP_ACCL = -10f;
    public Bird(Context context) {
        this.context = context;
        preferenceManager = PreferenceManager.getInstance(context);
        loadBirdSkin();
        x = 100;
        y = 500;
        velocity = 0;
        updateHitbox();
    }

    private void loadBirdSkin() {
        String path = preferenceManager.getBirdSkinPath();
        if (path != null) {
            bitmap = BitmapFactory.decodeFile(path);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.flappybird);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
    }

    public void update() {
        velocity += GRAVITY;
        y += velocity;
        updateHitbox();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void flap() {
        velocity += FLAP_ACCL;
    }

    private void updateHitbox() {
        float radius = bitmap.getWidth() / 2f;
        float centerX = x + radius;
        float centerY = y + radius;
        hitbox = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }

    public RectF getHitbox() {
        return hitbox;
    }

    public boolean isOffScreen() {
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        return y > screenHeight || y + bitmap.getHeight() < 0;
    }

}
