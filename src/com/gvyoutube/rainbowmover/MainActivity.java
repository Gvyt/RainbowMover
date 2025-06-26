package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;
import android.widget.ProgressBar;
import android.widget.MediaController;
import android.view.View;
import android.net.Uri;
import android.widget.Toast;

public class MainActivity extends Activity {

    private VideoView videoView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);

        String videoUrl = "https://ia601508.us.archive.org/16/items/rainbow-trololol/Rainbow%20Trololol.mp4";
        videoView.setVideoURI(Uri.parse(videoUrl));

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.setOnPreparedListener(mp -> {
            progressBar.setVisibility(View.GONE);
            videoView.start();
        });

        videoView.setOnErrorListener((mp, what, extra) -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Unable to play the video.", Toast.LENGTH_LONG).show();
            return true;
        });

        progressBar.setVisibility(View.VISIBLE);
    }
}
