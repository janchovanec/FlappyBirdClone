package com.example.flappybirdclone.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;

import com.example.flappybirdclone.utils.PreferenceManager;

public class Bird {
    private Bitmap bitmap;
    private float x, y, velocity;
    private RectF hitbox;
    private PreferenceManager preferenceManager;
    private Context context;

    private float rotation;

    private Paint debugPaint = new Paint();

    private static final float GRAVITY = 0.5f;
    private static final float FLAP_ACCL = -10f;
    public Bird(Context context) {
        this.context = context;
        preferenceManager = PreferenceManager.getInstance(context);
        loadBirdSkin();
        x = context.getResources().getDisplayMetrics().widthPixels / 3f - bitmap.getWidth() / 2f;
        y = 500;
        rotation = 0;
        velocity = 0;
        updateHitbox();


        debugPaint.setStyle(Paint.Style.STROKE);
        debugPaint.setStrokeWidth(5);
        debugPaint.setColor(0xFFFF0000);
    }

    private void loadBirdSkin() {
        String path = preferenceManager.getBirdSkinPath();
        if (path.startsWith("android.resource://")) {
            Uri uri = Uri.parse(path);
            int resourceId = Integer.parseInt(uri.getLastPathSegment());
            bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        } else {
            bitmap = BitmapFactory.decodeFile(path);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
    }

    public void update() {
        velocity += GRAVITY;
        y += velocity;
        rotation = velocity * 3;
        updateHitbox();
    }

    public void draw(Canvas canvas) {
        // rotate the bird
        canvas.save();
        canvas.rotate(rotation, x + bitmap.getWidth() / 2f, y + bitmap.getHeight() / 2f);
        canvas.drawBitmap(bitmap, x, y, null);

        //canvas.drawRect(hitbox, debugPaint);

        canvas.restore();
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
