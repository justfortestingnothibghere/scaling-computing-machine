package com.example.flyingmodi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_menu);

        Button play = findViewById(R.id.play_button);
        play.setOnClickListener(v -> showSpeedDialog());

        Button donate = findViewById(R.id.donate_button);
        donate.setOnClickListener(v -> showDonateDialog());

        Button credits = findViewById(R.id.credits_button);
        credits.setOnClickListener(v -> {
            Intent i = new Intent(this, CreditsActivity.class);
            startActivity(i);
        });

        Button exit = findViewById(R.id.exit_button);
        exit.setOnClickListener(v -> finish());
    }

    private void showSpeedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Initial Speed");
        String[] speeds = {"Slow 🐢", "Normal 🚀", "Fast ⚡"};
        builder.setItems(speeds, (dialog, which) -> {
            SharedPreferences sp = getSharedPreferences("game", MODE_PRIVATE);
            Editor ed = sp.edit();
            ed.putInt("speed_level", which + 1); // 1=slow, 2=normal, 3=fast
            ed.apply();
            Intent i = new Intent(MainMenuActivity.this, GameActivity.class);
            startActivity(i);
        });
        builder.show();
    }

    private void showDonateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Support the Developer ❤️");
        builder.setMessage("UPI ID: mr-arman-01@fam");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}