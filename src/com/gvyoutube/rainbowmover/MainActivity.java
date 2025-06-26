package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MainActivity extends Activity {

    private VideoView videoView;
    private ProgressBar progressBar;
    private TextView loadingText;
    private LinearLayout startScreen;
    private Button startButton;
    private Button optionsButton;
    private LinearLayout optionsLayout;
    private CheckBox rotationCheckbox;
    private CheckBox movementCheckbox;
    private CheckBox normalPlaybackCheckbox;

    private final String videoUrl = "https://ia601508.us.archive.org/16/items/rainbow-trololol/Rainbow%20Trololol.mp4";
    private final String localFileName = "appvid.mp4";

    private Handler handler = new Handler();
    private Random random = new Random();

    private Runnable moveRunnable;
    private Runnable rotateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = (VideoView) findViewById(R.id.videoView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingText = (TextView) findViewById(R.id.loadingText);
        startScreen = (LinearLayout) findViewById(R.id.startScreen);
        startButton = (Button) findViewById(R.id.startButton);
        optionsButton = (Button) findViewById(R.id.optionsButton);
        optionsLayout = (LinearLayout) findViewById(R.id.optionsLayout);
        rotationCheckbox = (CheckBox) findViewById(R.id.rotationCheckbox);
        movementCheckbox = (CheckBox) findViewById(R.id.movementCheckbox);
        normalPlaybackCheckbox = (CheckBox) findViewById(R.id.normalPlaybackCheckbox);

        movementCheckbox.setChecked(true);
        updateOptionsState();

        normalPlaybackCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateOptionsState();
            }
        });

        rotationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (normalPlaybackCheckbox.isChecked()) return;
                // No extra action needed here
            }
        });

        movementCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (normalPlaybackCheckbox.isChecked()) return;
                // No extra action needed here
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScreen.setVisibility(View.GONE);
                loadingText.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                loadingText.setText("Downloading Video, please wait...");
                downloadAndPlayVideo();
            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionsLayout.getVisibility() == View.VISIBLE) {
                    optionsLayout.setVisibility(View.GONE);
                } else {
                    optionsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateOptionsState() {
        boolean normalChecked = normalPlaybackCheckbox.isChecked();
        rotationCheckbox.setEnabled(!normalChecked);
        movementCheckbox.setEnabled(!normalChecked);
        if (normalChecked) {
            rotationCheckbox.setChecked(false);
            movementCheckbox.setChecked(false);
        }
    }

    private void downloadAndPlayVideo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(getFilesDir(), localFileName);
                    if (!file.exists()) {
                        URL url = new URL(videoUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        int fileLength = connection.getContentLength();

                        InputStream input = connection.getInputStream();
                        FileOutputStream output = new FileOutputStream(file);

                        byte[] data = new byte[4096];
                        int total = 0;
                        int count;
                        while ((count = input.read(data)) != -1) {
                            total += count;
                            output.write(data, 0, count);

                            final int progress = (int) (total * 100L / fileLength);
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
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            loadingText.setVisibility(View.GONE);
                            playVideo(new File(getFilesDir(), localFileName).getAbsolutePath());
                        }
                    });

                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error downloading video: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            loadingText.setText("Failed to download video.");
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }).start();
    }

    private void playVideo(String path) {
        videoView.setVideoPath(path);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                if (movementCheckbox.isChecked()) {
                    startMovingVideo();
                } else {
                    stopMovingVideo();
                }
                if (rotationCheckbox.isChecked()) {
                    startRotatingVideo();
                } else {
                    stopRotatingVideo();
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

    private void startMovingVideo() {
        final int screenWidth = getResources().getDisplayMetrics().widthPixels;
        final int screenHeight = getResources().getDisplayMetrics().heightPixels;

        if (moveRunnable != null) handler.removeCallbacks(moveRunnable);

        moveRunnable = new Runnable() {
            @Override
            public void run() {
                if (!videoView.isShown()) return;

                int x = random.nextInt(Math.max(screenWidth - videoView.getWidth(), 1));
                int y = random.nextInt(Math.max(screenHeight - videoView.getHeight(), 1));

                videoView.animate().x(x).y(y).setDuration(3000).start();

                handler.postDelayed(this, 3500);
            }
        };
        handler.post(moveRunnable);
    }

    private void stopMovingVideo() {
        if (moveRunnable != null) {
            handler.removeCallbacks(moveRunnable);
            moveRunnable = null;
        }
    }

    private void startRotatingVideo() {
        if (rotateRunnable != null) handler.removeCallbacks(rotateRunnable);

        rotateRunnable = new Runnable() {
            float rotation = 0f;

            @Override
            public void run() {
                rotation += 10f;
                videoView.setRotation(rotation % 360);
                handler.postDelayed(this, 50);
            }
        };
        handler.post(rotateRunnable);
    }

    private void stopRotatingVideo() {
        if (rotateRunnable != null) {
            handler.removeCallbacks(rotateRunnable);
            rotateRunnable = null;
            videoView.setRotation(0f);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMovingVideo();
        stopRotatingVideo();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
