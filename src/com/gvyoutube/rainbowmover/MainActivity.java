package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.VideoView;
import android.widget.ProgressBar;
import android.widget.MediaController;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private VideoView videoView;
    private ProgressBar progressBar;

    private static final String VIDEO_URL = "https://ia601508.us.archive.org/16/items/rainbow-trololol/Rainbow%20Trololol.mp4";
    private static final String LOCAL_FILE_NAME = "rainbow_trololol.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);

        File localVideo = new File(getFilesDir(), LOCAL_FILE_NAME);

        if (localVideo.exists()) {
            playVideo(localVideo);
        } else {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            downloadVideo(VIDEO_URL, localVideo);
        }
    }

    private void downloadVideo(String urlString, File outputFile) {
        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                InputStream in = conn.getInputStream();
                FileOutputStream out = new FileOutputStream(outputFile);

                byte[] buffer = new byte[4096];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }

                in.close();
                out.close();

                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    playVideo(outputFile);
                });

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    Toast.makeText(this, "Download failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void playVideo(File videoFile) {
        Uri videoUri = Uri.fromFile(videoFile);
        videoView.setVideoURI(videoUri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(mp -> videoView.start());
        videoView.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(this, "Can't play the downloaded video.", Toast.LENGTH_LONG).show();
            return true;
        });
    }
}
