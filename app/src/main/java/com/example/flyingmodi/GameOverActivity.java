package com.example.flyingmodi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_over);

        int score = getIntent().getIntExtra("score", 0);
        SharedPreferences sp = getSharedPreferences("game", MODE_PRIVATE);
        int best = sp.getInt("best", 0);
        if (score > best) {
            Editor ed = sp.edit();
            ed.putInt("best", score);
            ed.apply();
            best = score;
        }

        TextView tvScore = findViewById(R.id.tv_score);
        tvScore.setText("Votes Collected: " + score);

        TextView tvBest = findViewById(R.id.tv_best);
        tvBest.setText("Best: " + best);

        Button restart = findViewById(R.id.restart_button);
        restart.setOnClickListener(v -> {
            Intent i = new Intent(this, GameActivity.class);
            startActivity(i);
            finish();
        });

        Button home = findViewById(R.id.home_button);
        home.setOnClickListener(v -> {
            Intent i = new Intent(this, MainMenuActivity.class);
            startActivity(i);
            finish();
        });
    }
}