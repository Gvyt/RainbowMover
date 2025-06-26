package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;

public class MainActivity extends Activity {

    private VideoView videoView;
    private ProgressBar progressBar;
    private TextView loadingText;
    private LinearLayout splashScreen, titleScreen, optionLayout;
    private Button startButton, optionsButton;
    private CheckBox rotationBox, movementBox, normalBox;
    private boolean rotationEnabled = false;
    private boolean movementEnabled = false;

    private static final String VIDEO_URL = "https://ia601508.us.archive.org/16/items/rainbow-trololol/Rainbow%20Trololol.mp4";
    private static final String VIDEO_FILENAME = "video.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashScreen = findViewById(R.id.splashScreen);
        titleScreen = findViewById(R.id.titleScreen);
        optionLayout = findViewById(R.id.optionsLayout);
        startButton = findViewById(R.id.startButton);
        optionsButton = findViewById(R.id.optionsButton);
        rotationBox = findViewById(R.id.checkboxRotation);
        movementBox = findViewById(R.id.checkboxMovement);
        normalBox = findViewById(R.id.checkboxNormal);
        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);

        showSplash();
        setupButtons();
        setupCheckboxes();
    }

    private void showSplash() {
        splashScreen.setVisibility(View.VISIBLE);
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1000);
        fadeOut.setStartOffset(2000);

        splashScreen.startAnimation(fadeIn);
        splashScreen.startAnimation(fadeOut);

        new Handler().postDelayed(() -> {
            splashScreen.setVisibility(View.GONE);
            titleScreen.setVisibility(View.VISIBLE);
        }, 3000);
    }

    private void setupButtons() {
        startButton.setOnClickListener(v -> {
            titleScreen.setVisibility(View.GONE);
            loadingText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            downloadVideo(VIDEO_URL, new File(getFilesDir(), VIDEO_FILENAME));
        });

        optionsButton.setOnClickListener(v -> {
            if (optionLayout.getVisibility() == View.VISIBLE) {
                optionLayout.setVisibility(View.GONE);
            } else {
                optionLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupCheckboxes() {
        normalBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rotationBox.setEnabled(!isChecked);
            movementBox.setEnabled(!isChecked);
            if (isChecked) {
                rotationBox.setChecked(false);
                movementBox.setChecked(false);
            }
        });

        rotationBox.setOnCheckedChangeListener((b, isChecked) -> {
            if (!normalBox.isChecked()) rotationEnabled = isChecked;
        });

        movementBox.setOnCheckedChangeListener((b, isChecked) -> {
            if (!normalBox.isChecked()) movementEnabled = isChecked;
        });
    }

    private void downloadVideo(String urlStr, File outputFile) {
        new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
                conn.connect();

                int contentLength = conn.getContentLength();
                InputStream is = new BufferedInputStream(conn.getInputStream());
                FileOutputStream fos = new FileOutputStream(outputFile);

                byte[] buffer = new byte[1024];
                int len, downloaded = 0;

                while ((len = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                    downloaded += len;
                    int progress = (int) (downloaded * 100.0 / contentLength);
                    int finalProgress = progress;
                    new Handler(Looper.getMainLooper()).post(() -> progressBar.setProgress(finalProgress));
                }

                fos.flush();
                fos.close();
                is.close();

                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                    playVideo(Uri.fromFile(outputFile));
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void playVideo(Uri uri) {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(mp -> {
            if (rotationEnabled) {
                videoView.setRotation(90f);
            }
            videoView.start();
            if (movementEnabled) {
                animateVideo();
            }
        });
    }

    private void animateVideo() {
        videoView.animate()
                .translationXBy(100)
                .translationYBy(100)
                .setDuration(1000)
                .withEndAction(() -> {
                    videoView.animate()
                            .translationXBy(-200)
                            .translationYBy(-200)
                            .setDuration(1000)
                            .start();
                })
                .start();
    }
}
