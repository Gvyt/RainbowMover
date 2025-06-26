package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

    private static final int SPLASH_DURATION = 3500; // milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RelativeLayout splashRoot = findViewById(R.id.splash_root);

        // Fade in animation
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);

        // Fade out animation
        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setDuration(1000);
        fadeOut.setStartOffset(SPLASH_DURATION - 1000);
        fadeOut.setFillAfter(true);

        splashRoot.startAnimation(fadeIn);
        splashRoot.startAnimation(fadeOut);

        // Move to TitleActivity after splash duration
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, TitleActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}
