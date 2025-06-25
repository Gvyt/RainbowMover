package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;
import android.widget.MediaController;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

    private VideoView videoView;
    private ProgressBar progressBar;
    private float dX, dY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);

        Uri videoUri = Uri.parse("https://file.garden/Z4ry-cpnZQh7VmhL/Rainbow%20Trololol.mp4");
        videoView.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        progressBar.setVisibility(View.VISIBLE);

        videoView.setOnPreparedListener(mp -> {
            progressBar.setVisibility(View.GONE);
            videoView.start();
        });

        videoView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    v.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                    return true;
                default:
                    return false;
            }
        });
    }
}
