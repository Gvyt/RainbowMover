package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class MainActivity extends Activity {

    private VideoView videoView;
    private ProgressBar progressBar;

    private final String videoUrl = "https://file.garden/Z4ry-cpnZQh7VmhL/Rainbow%20Trololol.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> {
            progressBar.setVisibility(View.GONE);
            videoView.start();
        });

        videoView.setOnInfoListener((mp, what, extra) -> {
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                progressBar.setVisibility(View.VISIBLE);
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                progressBar.setVisibility(View.GONE);
            }
            return false;
        });

        videoView.setOnCompletionListener(mp -> {
            // Optionally restart the video when it ends
            videoView.start();
        });
    }
}
