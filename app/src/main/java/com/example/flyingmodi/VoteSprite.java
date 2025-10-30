package com.example.flyingmodi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class VoteSprite {
    private Bitmap bitmap;
    private int x, y;
    private int width, height;

    public VoteSprite(Context context, int startX, int startY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.vote);
        x = startX;
        y = startY;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
    }

    public void update(int velocity) {
        x -= velocity;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public Rect getBounds() {
        return new Rect(x, y, x + width, y + height);
    }
}