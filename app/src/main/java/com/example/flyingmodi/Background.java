package com.example.flyingmodi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.content.res.Resources;

public class Background {
    private Bitmap bitmap;
    private int x = 0;
    private int width;

    public Background(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        width = bitmap.getWidth();
    }

    public void update(int velocity) {
        x -= velocity / 2; // slower scroll
        if (x < -width) {
            x += width;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, 0, null);
        canvas.drawBitmap(bitmap, x + width, 0, null);
    }
}