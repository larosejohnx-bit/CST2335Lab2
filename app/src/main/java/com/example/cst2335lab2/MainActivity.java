
package com.example.cst2335lab2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private ListView characterListView;
    private ArrayList<JSONObject> characters = new ArrayList<>();
    private CharacterAdapter adapter;
    private LoadCharacters loadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Fix Android 16 status bar and ActionBar overlap
        View root = findViewById(R.id.characterListView);

        ViewCompat.setOnApplyWindowInsetsListener(
                root, (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            v.getPaddingLeft(),
                            systemBars.top,
                            v.getPaddingRight(),
                            systemBars.bottom
                    );

                    // Apply the same insets to tablet details
                    View detailsFrame = findViewById(R.id.detailsFrame);

                    if (detailsFrame != null) {
                        detailsFrame.setPadding(
                                0,
                                systemBars.top,
                                0,
                                systemBars.bottom
                        );
                    }

                    return insets;
                });

        // Set up character ListView
        characterListView =
                findViewById(R.id.characterListView);

        adapter = new CharacterAdapter();
        characterListView.setAdapter(adapter);

        // Handle character selection
        characterListView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    JSONObject character = characters.get(position);

                    // Store selected character information
                    Bundle bundle = new Bundle();

                    bundle.putString(
                            "name",
                            character.optString("name", "")
                    );

                    bundle.putString(
                            "height",
                            character.optString("height", "")
                    );

                    bundle.putString(
                            "mass",
                            character.optString("mass", "")
                    );

                    if (findViewById(R.id.detailsFrame) == null) {

                        // Phone: open a separate activity
                        Intent intent = new Intent(
                                MainActivity.this,
                                EmptyActivity.class
                        );

                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else {

                        // Tablet: display details beside ListView
                        DetailsFragment fragment =
                                new DetailsFragment();

                        fragment.setArguments(bundle);

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.detailsFrame, fragment)
                                .commit();
                    }
                });

        // Download Star Wars characters
        loadTask = new LoadCharacters();
        loadTask.execute();
    }

    @Override
    protected void onDestroy() {

        if (loadTask != null) {
            loadTask.cancel(true);
        }

        super.onDestroy();
    }

    // AsyncTask for downloading Star Wars information
    private class LoadCharacters
            extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection connection = null;

            try {

                URL url = new URL(
                        "https://swapi.dev/api/people/?format=json"
                );

                connection =
                        (HttpURLConnection) url.openConnection();

                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                try (InputStream input =
                             connection.getInputStream();

                     Scanner scanner =
                             new Scanner(input, "UTF-8")) {

                    scanner.useDelimiter("\\A");

                    if (scanner.hasNext()) {
                        return scanner.next();
                    }
                }

            } catch (Exception e) {

                Log.e(
                        "StarWarsAPI",
                        "Error loading characters",
                        e
                );

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {

            if (response == null) {

                Toast.makeText(
                        MainActivity.this,
                        "Failed to load characters",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            try {

                JSONObject json = new JSONObject(response);

                JSONArray results =
                        json.getJSONArray("results");

                characters.clear();

                for (int i = 0; i < results.length(); i++) {

                    characters.add(
                            results.getJSONObject(i)
                    );
                }

                // Update ListView after downloading data
                adapter.notifyDataSetChanged();

            } catch (Exception e) {

                Log.e(
                        "StarWarsAPI",
                        "Error reading JSON",
                        e
                );
            }
        }
    }

    // Custom adapter for Star Wars character names
    private class CharacterAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return characters.size();
        }

        @Override
        public Object getItem(int position) {
            return characters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent) {

            TextView textView;

            if (convertView == null) {

                textView = new TextView(MainActivity.this);

                textView.setTextSize(20);

                textView.setPadding(
                        12, 12, 12, 12
                );

            } else {

                textView = (TextView) convertView;
            }

            JSONObject character =
                    characters.get(position);

            textView.setText(
                    character.optString("name", "Unknown")
            );

            return textView;
        }
    }
}
