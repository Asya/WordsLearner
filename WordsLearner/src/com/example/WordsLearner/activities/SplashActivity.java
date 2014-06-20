package com.example.WordsLearner.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.WordsLearner.R;

public class SplashActivity extends Activity {

    private static final int SPLASH_LENGTH = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, PersonActivity.class));
                finish();
            }
        }, SPLASH_LENGTH);
    }
}