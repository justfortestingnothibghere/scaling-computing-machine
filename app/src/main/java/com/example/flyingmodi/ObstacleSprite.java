package com.example.flyingmodi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.content.res.Resources;
import android.view.SurfaceView;

public class ObstacleSprite {
    private Bitmap upper;
    private Bitmap lower;
    public int x, y;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private static final int OBSTACLE_WIDTH = 200;
    private static final int GAP_HEIGHT = 600;

    public ObstacleSprite(Context context, int startX, int startY) {
        upper = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.rahul), OBSTACLE_WIDTH, screenHeight / 2, false);
        lower = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.vijay), OBSTACLE_WIDTH, screenHeight / 2, false);
        x = startX;
        y = startY;
    }

    public void update(int velocity) {
        x -= velocity;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(upper, x, y - (screenHeight / 2) - (GAP_HEIGHT / 2), null);
        canvas.drawBitmap(lower, x, y + (GAP_HEIGHT / 2), null);
    }

    public boolean intersects(CharacterSprite sprite) {
        Rect spriteRect = sprite.getBounds();
        Rect upperRect = new Rect(x, y - (screenHeight / 2) - (GAP_HEIGHT / 2), x + OBSTACLE_WIDTH, y - (GAP_HEIGHT / 2));
        Rect lowerRect = new Rect(x, y + (GAP_HEIGHT / 2), x + OBSTACLE_WIDTH, y + (GAP_HEIGHT / 2) + (screenHeight / 2));
        return spriteRect.intersect(upperRect) || spriteRect.intersect(lowerRect);
    }
}