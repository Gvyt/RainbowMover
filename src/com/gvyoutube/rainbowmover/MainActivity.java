package com.gvyoutube.rainbowmover;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private ProgressBar progressBar;

    private float dX, dY;
    private int lastAction;

    private final String localFileName = "/assets/appvid.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);

        // Make VideoView draggable
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        view.setX(event.getRawX() + dX);
                        view.setY(event.getRawY() + dY);
                        lastAction = MotionEvent.ACTION_MOVE;
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            // Click action if needed
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });

        File localFile = new File(getFilesDir(), localFileName);
        if (localFile.exists()) {
            playVideo(localFile);
        } else {
            downloadVideo(videoUrl, localFile);
        }
    }

    private void playVideo(File videoFile) {
        progressBar.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);

        videoView.setVideoURI(Uri.fromFile(videoFile));
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.requestFocus();
        videoView.start();
    }

    private void downloadVideo(String urlStr, File outputFile) {
        progressBar.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);

        new Thread(() -> {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();

                InputStream input = connection.getInputStream();
                FileOutputStream output = new FileOutputStream(outputFile);

                byte[] data = new byte[4096];
                int total = 0;
                int count;

                Handler handler = new Handler(getMainLooper());

                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);

                    if (fileLength > 0) {
                        int progress = (int) (total * 100L / fileLength);
                        // Update progress bar on UI thread
                        handler.post(() -> progressBar.setProgress(progress));
                    }
                }

                output.flush();
                output.close();
                input.close();

                // Once done, play the video on UI thread
                handler.post(() -> playVideo(outputFile));

            } catch (Exception e) {
                e.printStackTrace();
                // Handle errors (show message or retry)
                Handler handler = new Handler(getMainLooper());
                handler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    // Optionally show an error message or retry button
                });
            }
        }).start();
    }
}
