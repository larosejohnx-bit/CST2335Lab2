package com.example.cst2335lab2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        Button thankYouButton = findViewById(R.id.thankYouButton);
        Button dontCallMeButton = findViewById(R.id.dontCallMeButton);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        welcomeTextView.setText(
                getString(R.string.welcome_name, name)
        );

        thankYouButton.setOnClickListener(v -> {
            setResult(1);
            finish();
        });

        dontCallMeButton.setOnClickListener(v -> {
            setResult(0);
            finish();
        });
    }
}