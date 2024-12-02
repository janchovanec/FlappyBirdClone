package com.example.flappybirdclone.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Pipe {
    private Bitmap bitmap;
    private float x, y;
    private RectF hitbox;
    private boolean scored;

    private Paint debugPaint = new Paint();

    private static final float SCROLL_SPEED = 5f;

    public Pipe(Bitmap bitmap, float x, float y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.scored = false;
        updateHitbox();

        debugPaint.setStyle(Paint.Style.STROKE);
        debugPaint.setStrokeWidth(5);
        debugPaint.setColor(0xFFFF0000);
    }

    public void update() {
        x -= SCROLL_SPEED;
        updateHitbox();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
        canvas.drawRect(hitbox, debugPaint);
    }

    private void updateHitbox() {
        hitbox = new RectF(x + bitmap.getWidth() * 0.1f, y, x + bitmap.getWidth() * 0.9f, y + bitmap.getHeight());
    }

    public RectF getHitbox() {
        return hitbox;
    }

    public boolean isOffScreen() {
        return x + bitmap.getWidth() < 0;
    }

    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }

    public float getX() {
        return x;
    }

    public float getWidth() {
        return bitmap.getWidth();
    }
}
