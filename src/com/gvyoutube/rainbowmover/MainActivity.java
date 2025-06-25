package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.VideoView;
import android.widget.MediaController;
import android.media.MediaPlayer;

public class MainActivity extends Activity {

    private VideoView videoView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        String videoUrl = "https://file.garden/Z4ry-cpnZQh7VmhL/Rainbow%20Trololol.mp4";
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(ProgressBar.GONE);
                videoView.start();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progressBar.setVisibility(ProgressBar.GONE);
                return true;
            }
        });
    }
}
