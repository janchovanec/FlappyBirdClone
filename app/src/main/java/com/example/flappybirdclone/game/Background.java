package com.example.flappybirdclone.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

// Background class to handle the background image using two instances of the same image
public class Background {
    private Bitmap bitmap;
    private int x1, x2;
    private int y;

    private int screenWidth;
    private int screenHeight;

    private static final int SCROLL_SPEED = 3;

    public Background(int sourceImg, Context context) {
        Bitmap sourceBitmap = BitmapFactory.decodeResource(context.getResources(), sourceImg);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        float scale = (float) screenHeight / sourceBitmap.getHeight();
        int scaledWidth = Math.round(scale * sourceBitmap.getWidth());
        int scaledHeight = screenHeight;
        bitmap = Bitmap.createScaledBitmap(sourceBitmap, scaledWidth, scaledHeight, true);

        this.y = 0;
        x1 = 0;
        x2 = bitmap.getWidth();
    }

    public void update() {
        x1 -= SCROLL_SPEED;
        x2 -= SCROLL_SPEED;

        if (x1 + bitmap.getWidth() < 0) {
            x1 = x2 + bitmap.getWidth();
        }

        if (x2 + bitmap.getWidth() < 0) {
            x2 = x1 + bitmap.getWidth();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x1, y, null);
        canvas.drawBitmap(bitmap, x2, y, null);
    }

}
