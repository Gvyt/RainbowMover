package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashImage = findViewById(R.id.splashImage);
        TextView splashText = findViewById(R.id.splashText);

        Animation fade = new AlphaAnimation(0, 1);
        fade.setDuration(1000);
        splashImage.startAnimation(fade);
        splashText.startAnimation(fade);

        new Handler().postDelayed(() -> {
            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setDuration(1000);
            splashImage.startAnimation(fadeOut);
            splashText.startAnimation(fadeOut);
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }, 1000);
        }, 2000);
    }
}
