package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;
import android.widget.ProgressBar;
import android.widget.MediaController;
import android.net.Uri;
import java.io.File;

public class MainActivity extends Activity {

    private VideoView videoView;
    private ProgressBar progressBar;
    private final String videoUrl = "https://file.garden/Z4ry-cpnZQh7VmhL/Rainbow%20Trololol.mp4";
    private final String localFileName = "appvid.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(ProgressBar.VISIBLE);

        downloadVideo(videoUrl, new File(getFilesDir(), localFileName));
    }

    private void downloadVideo(final String url, final File outputFile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    java.net.URL videoUrl = new java.net.URL(url);
                    java.net.URLConnection conn = videoUrl.openConnection();
                    java.io.InputStream in = conn.getInputStream();
                    java.io.FileOutputStream out = new java.io.FileOutputStream(outputFile);

                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }

                    in.close();
                    out.close();

                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(ProgressBar.GONE);
                            playVideo(outputFile);
                        }
                    });

                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void playVideo(File file) {
        Uri uri = Uri.fromFile(file);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.start();
    }
}
