package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.*;
import android.widget.VideoView;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends Activity {

    private RelativeLayout splashLayout, titleScreenLayout;
    private VideoView videoView;
    private ProgressBar progressBar;
    private TextView loadingText;
    private Button startButton, optionsButton;
    private LinearLayout optionsPanel;
    private CheckBox rotationCheckbox, movementCheckbox, normalCheckbox;

    private boolean enableRotation = false;
    private boolean enableMovement = false;
    private final String videoUrl = "https://ia601508.us.archive.org/16/items/rainbow-trololol/Rainbow%20Trololol.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSplashScreen();
    }

    private void showSplashScreen() {
        splashLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_splash, null);
        setContentView(splashLayout);

        ImageView splashImage = splashLayout.findViewById(R.id.splashImage);
        TextView splashText = splashLayout.findViewById(R.id.splashText);

        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.MONTH) == Calendar.APRIL && cal.get(Calendar.DAY_OF_MONTH) == 1) {
            splashImage.setImageResource(R.drawable.splash_afd);
            splashText.setText("made bye gvyubtubeeer");
        } else {
            splashImage.setImageResource(R.drawable.splash);
            splashText.setText("Made by GvYoutube");
        }

        Animation fadeInOut = new AlphaAnimation(0, 1);
        fadeInOut.setDuration(1000);
        fadeInOut.setRepeatMode(Animation.REVERSE);
        fadeInOut.setRepeatCount(1);
        splashLayout.startAnimation(fadeInOut);

        new Handler(Looper.getMainLooper()).postDelayed(this::showTitleScreen, 3000);
    }

    private void showTitleScreen() {
        setContentView(R.layout.title_screen);

        startButton = findViewById(R.id.startButton);
        optionsButton = findViewById(R.id.optionsButton);
        optionsPanel = findViewById(R.id.optionsPanel);
        rotationCheckbox = findViewById(R.id.rotationCheckbox);
        movementCheckbox = findViewById(R.id.movementCheckbox);
        normalCheckbox = findViewById(R.id.normalCheckbox);

        startButton.setOnClickListener(v -> startVideoFlow());

        optionsButton.setOnClickListener(v -> {
            if (optionsPanel.getVisibility() == View.VISIBLE) {
                optionsPanel.setVisibility(View.GONE);
            } else {
                optionsPanel.setVisibility(View.VISIBLE);
            }
        });

        normalCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rotationCheckbox.setEnabled(!isChecked);
            movementCheckbox.setEnabled(!isChecked);
        });
    }

    private void startVideoFlow() {
        // Load new layout
        RelativeLayout videoLayout = new RelativeLayout(this);
        videoView = new VideoView(this);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        loadingText = new TextView(this);

        loadingText.setText("Downloading Video, please wait...");
        loadingText.setTextSize(18);
        loadingText.setPadding(20, 50, 20, 20);

        RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        videoView.setLayoutParams(videoParams);

        RelativeLayout.LayoutParams progressParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        progressParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        progressBar.setLayoutParams(progressParams);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        loadingText.setLayoutParams(textParams);

        videoLayout.addView(videoView);
        videoLayout.addView(loadingText);
        videoLayout.addView(progressBar);

        setContentView(videoLayout);

        enableRotation = rotationCheckbox.isChecked();
        enableMovement = movementCheckbox.isChecked();

        new Thread(this::downloadAndPlayVideo).start();
    }

    private void downloadAndPlayVideo() {
        try {
            File videoFile = new File(getFilesDir(), "downloaded_video.mp4");

            URL url = new URL(videoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int fileLength = connection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(videoFile);

            byte[] data = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                final int progress = (int) (total * 100 / fileLength);
                runOnUiThread(() -> progressBar.setProgress(progress));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            runOnUiThread(() -> playVideo(Uri.fromFile(videoFile)));

        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Failed to download video", Toast.LENGTH_LONG).show());
        }
    }

    private void playVideo(Uri uri) {
        loadingText.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(mp -> {
            videoView.start();
            if (enableMovement) {
                animateMovement();
            }
            if (enableRotation) {
                videoView.animate().rotationBy(360).setDuration(10000).start();
            }
        });
    }

    private void animateMovement() {
        int maxX = getResources().getDisplayMetrics().widthPixels - 200;
        int maxY = getResources().getDisplayMetrics().heightPixels - 200;

        videoView.animate()
                .translationXBy((float) (Math.random() * maxX))
                .translationYBy((float) (Math.random() * maxY))
                .setDuration(4000)
                .withEndAction(this::animateMovement)
                .start();
    }
}
