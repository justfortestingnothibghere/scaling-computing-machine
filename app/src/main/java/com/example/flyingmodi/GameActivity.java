package com.example.flyingmodi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class GameActivity extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_layout);

        gameView = findViewById(R.id.game_view);
        Button pauseButton = findViewById(R.id.pause_button);
        final boolean[] isPaused = {false};
        pauseButton.setOnClickListener(v -> {
            if (isPaused[0]) {
                gameView.resume();
                pauseButton.setText("Pause");
            } else {
                gameView.pause();
                pauseButton.setText("Resume");
            }
            isPaused[0] = !isPaused[0];
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}