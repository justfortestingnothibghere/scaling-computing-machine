package com.example.flyingmodi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private CharacterSprite modi;
    private List<ObstacleSprite> obstacles;
    private List<VoteSprite> votes;
    private Background background;
    private int score = 0;
    private long modiVotes = 1000000;
    private long rahulVotes = 900000;
    private long vijayVotes = 850000;
    private boolean isLeading = false;
    private Paint paint;
    private MediaPlayer dingPlayer;
    private MediaPlayer victoryPlayer;
    private MediaPlayer endPlayer;
    private Timer voteTimer;
    private int velocity;
    private double gravity;
    private double jumpStrength = -25;
    private long lastSpeedIncrease;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private static final int GAP_HEIGHT = 600;
    private Random random = new Random();

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        initGame();
        thread.setRunning(true);
        thread.start();
    }

    private void initGame() {
        SharedPreferences sp = getContext().getSharedPreferences("game", Context.MODE_PRIVATE);
        int speedLevel = sp.getInt("speed_level", 2);
        velocity = 5 + speedLevel * 3; // 8 slow, 11 normal, 14 fast
        gravity = 0.8 + speedLevel * 0.3; // adjust gravity

        background = new Background(getContext());
        modi = new CharacterSprite(getContext(), gravity, 100, screenHeight / 2);
        obstacles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int x = screenWidth + i * 1200;
            int y = random.nextInt(400) - 200;
            obstacles.add(new ObstacleSprite(getContext(), x, y));
        }
        votes = new ArrayList<>();

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);

        dingPlayer = MediaPlayer.create(getContext(), R.raw.ding);
        victoryPlayer = MediaPlayer.create(getContext(), R.raw.victory);
        endPlayer = MediaPlayer.create(getContext(), R.raw.modi_end);

        voteTimer = new Timer();
        voteTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                modiVotes += random.nextInt(501) + 500;
                rahulVotes += random.nextInt(401) + 300;
                vijayVotes += random.nextInt(401) + 200;
                checkLeading();
            }
        }, 3000, 3000); // every 3 seconds

        lastSpeedIncrease = System.currentTimeMillis();
    }

    private void checkLeading() {
        if (modiVotes > rahulVotes && modiVotes > vijayVotes && !isLeading) {
            isLeading = true;
            victoryPlayer.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        voteTimer.cancel();
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        background.update(velocity);
        modi.update();
        for (ObstacleSprite o : obstacles) {
            o.update(velocity);
        }
        List<VoteSprite> toRemove = new ArrayList<>();
        for (VoteSprite v : votes) {
            v.update(velocity);
            if (Rect.intersects(modi.getBounds(), v.getBounds())) {
                score++;
                dingPlayer.start();
                toRemove.add(v);
            }
        }
        votes.removeAll(toRemove);

        // Spawn vote randomly
        if (random.nextInt(100) < 3) { // ~3% chance per frame
            int x = screenWidth + 100;
            int y = random.nextInt(screenHeight - 100) + 50;
            votes.add(new VoteSprite(getContext(), x, y));
        }

        // Increase speed every 10 seconds
        if (System.currentTimeMillis() - lastSpeedIncrease > 10000) {
            velocity += 1;
            gravity += 0.1;
            modi.setGravity(gravity);
            lastSpeedIncrease = System.currentTimeMillis();
        }
    }

    public void logic() {
        // Obstacle reset
        for (ObstacleSprite o : obstacles) {
            if (o.x < -500) {
                o.x = screenWidth + random.nextInt(500) + 1000;
                o.y = random.nextInt(400) - 200;
            }
            if (o.intersects(modi)) {
                gameOver();
            }
        }

        // Screen bounds
        if (modi.y < 0 || modi.y + modi.getHeight() > screenHeight) {
            gameOver();
        }
    }

    private void gameOver() {
        endPlayer.start();
        voteTimer.cancel();
        Intent i = new Intent(getContext(), GameOverActivity.class);
        i.putExtra("score", score);
        getContext().startActivity(i);
        ((Activity) getContext()).finish();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            background.draw(canvas);
            modi.draw(canvas);
            for (ObstacleSprite o : obstacles) {
                o.draw(canvas);
            }
            for (VoteSprite v : votes) {
                v.draw(canvas);
            }
            canvas.drawText("Votes Collected: " + score, 20, 50, paint);
            canvas.drawText("Modi: " + modiVotes, 20, 100, paint);
            canvas.drawText("Rahul: " + rahulVotes, 20, 150, paint);
            canvas.drawText("Vijay: " + vijayVotes, 20, 200, paint);
            if (isLeading) {
                paint.setTextSize(60);
                canvas.drawText("Modi Leading Nationwide!", screenWidth / 2 - 300, screenHeight / 2, paint);
                paint.setTextSize(40);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            modi.jump(jumpStrength);
        }
        return true;
    }

    public void pause() {
        thread.setPaused(true);
    }

    public void resume() {
        thread.setPaused(false);
    }
}