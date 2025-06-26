package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;
import android.widget.ProgressBar;
import android.net.Uri;
import android.widget.MediaController;
import android.media.MediaPlayer;
import android.view.View;

public class MainActivity extends Activity {
    private VideoView videoView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);

        String videoUrl = "https://file.garden/Z4ry-cpnZQh7VmhL/Rainbow%20Trololol.mp4";
        Uri videoUri = Uri.parse(videoUrl);

        videoView.setVideoURI(videoUri);
        videoView.setMediaController(new MediaController(this));
        progressBar.setVisibility(View.VISIBLE);

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
                return false;
            }
        });
    }
}
