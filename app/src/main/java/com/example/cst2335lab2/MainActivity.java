package com.example.cst2335lab2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView catImageView;
    private ProgressBar progressBar;
    private CatImages catTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catImageView = findViewById(R.id.catImageView);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setMax(100);
        progressBar.setProgress(0);

        catTask = new CatImages();
        catTask.execute();
    }

    @Override
    protected void onDestroy() {
        if (catTask != null) {
            catTask.cancel(true);
        }
        super.onDestroy();
    }

    // Downloads cat pictures and updates the slideshow
    private class CatImages extends AsyncTask<String, Integer, String> {

        private Bitmap currentCat;

        @Override
        protected String doInBackground(String... strings) {

            while (!isCancelled()) {
                HttpURLConnection connection = null;

                try {
                    // Request random cat information
                    URL apiUrl = new URL(
                            "https://cataas.com/cat?json=true"
                    );

                    connection = (HttpURLConnection)
                            apiUrl.openConnection();

                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    connection.setUseCaches(false);

                    StringBuilder response = new StringBuilder();

                    try (InputStream input = connection.getInputStream();
                         java.util.Scanner scanner =
                                 new java.util.Scanner(input, "UTF-8")) {

                        scanner.useDelimiter("\\A");

                        if (scanner.hasNext()) {
                            response.append(scanner.next());
                        }
                    }

                    JSONObject json = new JSONObject(
                            response.toString()
                    );

                    String id = json.getString("id");
                    String imageUrl = json.getString("url");

                    File imageFile = new File(
                            getFilesDir(), id + ".jpg"
                    );

                    if (imageFile.exists()) {

                        // Load the saved picture
                        currentCat = BitmapFactory.decodeFile(
                                imageFile.getAbsolutePath()
                        );

                        Log.d("CatImages", "Loaded cached cat: " + id);

                    } else {

                        // Download a new picture
                        URL downloadUrl = new URL(
                                new URL("https://cataas.com/"),
                                imageUrl
                        );

                        HttpURLConnection imageConnection =
                                (HttpURLConnection)
                                        downloadUrl.openConnection();

                        imageConnection.setConnectTimeout(10000);
                        imageConnection.setReadTimeout(10000);

                        try {
                            currentCat = BitmapFactory.decodeStream(
                                    imageConnection.getInputStream()
                            );
                        } finally {
                            imageConnection.disconnect();
                        }

                        if (currentCat != null) {
                            try (FileOutputStream output =
                                         new FileOutputStream(imageFile)) {

                                currentCat.compress(
                                        Bitmap.CompressFormat.JPEG,
                                        90,
                                        output
                                );
                            }

                            Log.d("CatImages", "Downloaded cat: " + id);
                        }
                    }

                    if (currentCat != null && !isCancelled()) {

                        // Display the newly selected cat
                        publishProgress(0);

                        // Show each picture for about 3 seconds
                        for (int i = 1; i <= 100 && !isCancelled(); i++) {
                            Thread.sleep(30);
                            publishProgress(i);
                        }
                    }

                } catch (Exception e) {

                    Log.e("CatImages", "Error loading cat", e);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException interrupted) {
                        return null;
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            int progress = values[0];

            progressBar.setProgress(progress);

            if (progress == 0 && currentCat != null) {
                catImageView.setImageBitmap(currentCat);
            }
        }
    }
}
