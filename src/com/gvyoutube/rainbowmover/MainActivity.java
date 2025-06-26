package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;
import java.util.Random;
import java.util.Calendar;

public class MainActivity extends Activity {

    private FrameLayout rootLayout;
    private LinearLayout splashLayout;
    private LinearLayout titleLayout;
    private VideoView videoView;
    private ProgressBar progressBar;
    private TextView loadingText;
    private LinearLayout optionsLayout;
    private CheckBox rotationCheckbox;
    private CheckBox movementCheckbox;
    private CheckBox normalPlaybackCheckbox;
    private Button startButton;
    private Button optionsButton;

    private Handler handler;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootLayout = new FrameLayout(this);
        setContentView(rootLayout);

        handler = new Handler(Looper.getMainLooper());
        random = new Random();

        setupSplashScreen();
    }

    private void setupSplashScreen() {
        splashLayout = new LinearLayout(this);
        splashLayout.setOrientation(LinearLayout.VERTICAL);
        splashLayout.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        splashLayout.setGravity(android.view.Gravity.CENTER);

        android.widget.ImageView splashImage = new android.widget.ImageView(this);
        int imageResId = getResources().getIdentifier("splash", "drawable", getPackageName());
        if (isAprilFirst()) {
            int altImageResId = getResources().getIdentifier("splash_afd", "drawable", getPackageName());
            if (altImageResId != 0) {
                imageResId = altImageResId;
            }
        }
        splashImage.setImageResource(imageResId);
        splashLayout.addView(splashImage);

        TextView splashText = new TextView(this);
        splashText.setText(isAprilFirst() ? "made bye gvyubtubeeer" : "Made by GvYoutube");
        splashText.setTextSize(24);
        splashText.setPadding(0, 20, 0, 0);
        splashLayout.addView(splashText);

        rootLayout.addView(splashLayout);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeOutSplash();
            }
        }, 3000);
    }

    private boolean isAprilFirst() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) == Calendar.APRIL && c.get(Calendar.DAY_OF_MONTH) == 1;
    }

    private void fadeOutSplash() {
        splashLayout.animate().alpha(0f).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                rootLayout.removeView(splashLayout);
                showTitleScreen();
            }
        });
    }

    private void showTitleScreen() {
        titleLayout = new LinearLayout(this);
        titleLayout.setOrientation(LinearLayout.VERTICAL);
        titleLayout.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        titleLayout.setGravity(android.view.Gravity.CENTER_HORIZONTAL);

        TextView appName = new TextView(this);
        appName.setText("RainbowMover v2.2");
        appName.setTextSize(32);
        appName.setPadding(0, 100, 0, 50);
        titleLayout.addView(appName);

        startButton = new Button(this);
        startButton.setText("Start");
        LinearLayout.LayoutParams startParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        startParams.setMargins(0, 20, 0, 20);
        startButton.setLayoutParams(startParams);
        titleLayout.addView(startButton);

        optionsButton = new Button(this);
        optionsButton.setText("Options");
        optionsButton.setLayoutParams(startParams);
        titleLayout.addView(optionsButton);

        rootLayout.addView(titleLayout);

        setupOptionsLayout();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setEnabled(false);
                optionsButton.setEnabled(false);
                titleLayout.removeView(startButton);
                titleLayout.removeView(optionsButton);
                showLoadingScreen();
                startDownloadAndPlay();
            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optionsLayout.getParent() == null) {
                    titleLayout.addView(optionsLayout);
                } else {
                    titleLayout.removeView(optionsLayout);
                }
            }
        });
    }

    private void setupOptionsLayout() {
        optionsLayout = new LinearLayout(this);
        optionsLayout.setOrientation(LinearLayout.VERTICAL);
        optionsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        optionsLayout.setPadding(0, 20, 0, 0);

        rotationCheckbox = new CheckBox(this);
        rotationCheckbox.setText("Enable Rotation");
        optionsLayout.addView(rotationCheckbox);

        movementCheckbox = new CheckBox(this);
        movementCheckbox.setText("Enable Movement");
        optionsLayout.addView(movementCheckbox);

        normalPlaybackCheckbox = new CheckBox(this);
        normalPlaybackCheckbox.setText("Normal Playback (disables other options)");
        optionsLayout.addView(normalPlaybackCheckbox);

        normalPlaybackCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean normalChecked = normalPlaybackCheckbox.isChecked();
                rotationCheckbox.setEnabled(!normalChecked);
                movementCheckbox.setEnabled(!normalChecked);
                if (normalChecked) {
                    rotationCheckbox.setChecked(false);
                    movementCheckbox.setChecked(false);
                }
            }
        });
    }

    private void showLoadingScreen() {
        loadingText = new TextView(this);
        loadingText.setText("Downloading Video, please wait...");
        loadingText.setTextSize(20);
        loadingText.setPadding(0, 50, 0, 20);
        titleLayout.addView(loadingText);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(false);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        LinearLayout.LayoutParams pbParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pbParams.setMargins(50, 0, 50, 0);
        progressBar.setLayoutParams(pbParams);
        titleLayout.addView(progressBar);
    }

    private void startDownloadAndPlay() {
        final String videoUrl = "https://ia601508.us.archive.org/16/items/rainbow-trololol/Rainbow%20Trololol.mp4";

        new Thread(new Runnable() {
            @Override
            public void run() {
                final java.io.File videoFile = new java.io.File(getFilesDir(), "appvid.mp4");
                try {
                    java.net.URL url = new java.net.URL(videoUrl);
                    java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                    connection.connect();
                    int contentLength = connection.getContentLength();
                    java.io.InputStream input = connection.getInputStream();
                    java.io.FileOutputStream output = new java.io.FileOutputStream(videoFile);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    int totalRead = 0;

                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                        totalRead += bytesRead;
                        final int progress = (int)((totalRead / (float)contentLength) * 100);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progress);
                            }
                        });
                    }
                    output.flush();
                    output.close();
                    input.close();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            titleLayout.removeView(loadingText);
                            titleLayout.removeView(progressBar);
                            rootLayout.removeView(titleLayout);
                            setupVideoPlayer(videoFile.getAbsolutePath());
                        }
                    });
                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error downloading video", Toast.LENGTH_LONG).show();
                            startButton.setEnabled(true);
                            optionsButton.setEnabled(true);
                        }
                    });
                }
            }
        }).start();
    }

    private void setupVideoPlayer(String videoPath) {
        videoView = new VideoView(this);
        videoView.setVideoPath(videoPath);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        rootLayout.addView(videoView);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                videoView.start();
                if (rotationCheckbox.isChecked()) {
                    startRotationAnimation();
                }
                if (movementCheckbox.isChecked()) {
                    startMovementAnimation();
                }
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(MainActivity.this, "Cannot play this video", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    private void startRotationAnimation() {
        videoView.animate().rotationBy(360f).setDuration(5000).withEndAction(new Runnable() {
            @Override
            public void run() {
                startRotationAnimation();
            }
        });
    }

    private void startMovementAnimation() {
        final int parentWidth = rootLayout.getWidth();
        final int parentHeight = rootLayout.getHeight();
        if (parentWidth == 0 || parentHeight == 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMovementAnimation();
                }
            }, 500);
            return;
        }
        int videoWidth = videoView.getWidth();
        int videoHeight = videoView.getHeight();

        int maxX = parentWidth - videoWidth;
        int maxY = parentHeight - videoHeight;

        int nextX = random.nextInt(Math.max(maxX, 1));
        int nextY = random.nextInt(Math.max(maxY, 1));

        videoView.animate().x(nextX).y(nextY).setDuration(3000).withEndAction(new Runnable() {
            @Override
            public void run() {
                startMovementAnimation();
            }
        });
    }
}
