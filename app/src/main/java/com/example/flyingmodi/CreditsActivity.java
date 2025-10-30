package com.example.flyingmodi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class CreditsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.credits);

        TextView tvCredits = findViewById(R.id.tv_credits);
        tvCredits.setText("Developer – @Mr_Arman_08 (Telegram)\nInstagram – @iarmxn_khxn\nContact – +91 8011971924");
    }
}