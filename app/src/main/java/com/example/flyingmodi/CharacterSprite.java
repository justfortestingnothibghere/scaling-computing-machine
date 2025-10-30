package com.example.flyingmodi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class CharacterSprite {
    private Bitmap bitmap;
    public double x, y;
    private double yVelocity = 0;
    private double gravity;
    private int width, height;

    public CharacterSprite(Context context, double gravity, int startX, int startY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.modi);
        this.gravity = gravity;
        x = startX;
        y = startY;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
    }

    public void update() {
        yVelocity += gravity;
        y += yVelocity;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, (float) x, (float) y, null);
    }

    public void jump(double strength) {
        yVelocity = strength;
    }

    public Rect getBounds() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public int getHeight() {
        return height;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }
}