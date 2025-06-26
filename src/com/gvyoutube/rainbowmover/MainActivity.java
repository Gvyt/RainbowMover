package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;
import android.widget.MediaController;

public class MainActivity extends Activity {

    private ImageView splashImage;
    private TextView splashText;
    private RelativeLayout splashLayout;
    private LinearLayout startScreenLayout;
    private Button startButton, optionsButton;
    private LinearLayout optionsLayout;
    private CheckBox rotateCheckbox, moveCheckbox, normalCheckbox;
    private ProgressBar loadingBar;
    private TextView loadingText;
    private VideoView videoView;

    private final String videoUrl = "https://ia601508.us.archive.org/16/items/rainbow-trololol/Rainbow%20Trololol.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Splash screen
        splashImage = findViewById(R.id.splashImage);
        splashText = findViewById(R.id.splashText);
        splashLayout = findViewById(R.id.splashLayout);
        startScreenLayout = findViewById(R.id.startScreenLayout);
        startButton = findViewById(R.id.startButton);
        optionsButton = findViewById(R.id.optionsButton);
        optionsLayout = findViewById(R.id.optionsLayout);
        rotateCheckbox = findViewById(R.id.rotateCheckbox);
        moveCheckbox = findViewById(R.id.moveCheckbox);
        normalCheckbox = findViewById(R.id.normalCheckbox);
        loadingBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);
        videoView = findViewById(R.id.videoView);

        splashLayout.setVisibility(View.VISIBLE);
        startScreenLayout.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
        loadingBar.setVisibility(View.GONE);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setStartOffset(2500);
        fadeOut.setDuration(1000);

        splashLayout.startAnimation(fadeIn);
        splashLayout.setAnimation(fadeOut);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            splashLayout.setVisibility(View.GONE);
            startScreenLayout.setVisibility(View.VISIBLE);
        }, 3500);

        normalCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rotateCheckbox.setEnabled(!isChecked);
            moveCheckbox.setEnabled(!isChecked);
        });

        startButton.setOnClickListener(v -> {
            startScreenLayout.setVisibility(View.GONE);
            loadingText.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                loadingBar.setVisibility(View.GONE);
                loadingText.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);

                if (rotateCheckbox.isChecked()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }

                MediaController controller = new MediaController(this);
                controller.setAnchorView(videoView);
                videoView.setMediaController(controller);
                videoView.setVideoURI(Uri.parse(videoUrl));
                videoView.setOnPreparedListener(mp -> {
                    videoView.start();

                    if (moveCheckbox.isChecked()) {
                        animateVideo();
                    }
                });

            }, 3000); // Simulate download
        });

        optionsButton.setOnClickListener(v -> {
            if (optionsLayout.getVisibility() == View.VISIBLE) {
                optionsLayout.setVisibility(View.GONE);
            } else {
                optionsLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void animateVideo() {
        videoView.animate().translationXBy(100).translationYBy(100).set
