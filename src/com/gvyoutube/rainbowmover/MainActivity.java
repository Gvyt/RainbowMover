package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;
import android.widget.ProgressBar;
import android.widget.MediaController;
import android.media.MediaPlayer;
import android.view.View;
import android.net.Uri;
import android.widget.Toast;

public class MainActivity extends Activity {

    private VideoView videoView;
    private ProgressBar progressBar;

    private final String videoUrl = "https://ia601508.us.archive.org/16/items/rainbow-trololol/Rainbow%20Trololol.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                videoView.start();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Unable to play the video.", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }
}
