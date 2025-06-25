package com.gvyoutube.rainbowmover;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private float dX, dY;
    private VideoView videoView;
    private ProgressBar progressBar;
    private FrameLayout rootLayout;

    private static final String VIDEO_URL = "https://file.garden/Z4ry-cpnZQh7VmhL/Rainbow%20Trololol.mp4";
    private static final String VIDEO_FILE_NAME = "RainbowTrololol.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.rootLayout);
        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);

        // Enable dragging of VideoView
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        v.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        // Check if video already downloaded
        File videoFile = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), VIDEO_FILE_NAME);
        if (videoFile.exists()) {
            playVideo(videoFile);
        } else {
            // Download video and then play
            new DownloadVideoTask(videoFile).execute(VIDEO_URL);
        }
    }

    private void playVideo(File videoFile) {
        Uri videoUri = Uri.fromFile(videoFile);
        videoView.setVideoURI(videoUri);
        videoView.start();
    }

    private class DownloadVideoTask extends AsyncTask<String, Integer, Boolean> {

        private File outputFile;

        DownloadVideoTask(File outputFile) {
            this.outputFile = outputFile;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String urlStr = strings[0];
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();

                InputStream input = new BufferedInputStream(connection.getInputStream());
                OutputStream output = new FileOutputStream(outputFile);

                byte[] data = new byte[1024];
                int total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                    if (fileLength > 0) {
                        publishProgress((total * 100) / fileLength);
                    }
                }

                output.flush();
                output.close();
                input.close();

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressBar.setVisibility(View.GONE);
            if (success) {
                playVideo(outputFile);
            } else {
                // Handle failure (could add a Toast here)
            }
        }
    }
}
