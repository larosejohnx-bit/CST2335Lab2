package com.example.cst2335lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private SharedPreferences preferences;

    private final ActivityResultLauncher<Intent> nameActivityLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == 0) {
                            nameEditText.requestFocus();
                        } else if (result.getResultCode() == 1) {
                            finish();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        Button nextButton = findViewById(R.id.nextButton);

        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        String savedName = preferences.getString("name", "");

        if (!savedName.isEmpty()) {
            nameEditText.setText(savedName);
        }

        nextButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();

            Intent intent = new Intent(MainActivity.this, NameActivity.class);
            intent.putExtra("name", name);

            nameActivityLauncher.launch(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        String name = nameEditText.getText().toString();

        preferences.edit()
                .putString("name", name)
                .apply();
    }
}