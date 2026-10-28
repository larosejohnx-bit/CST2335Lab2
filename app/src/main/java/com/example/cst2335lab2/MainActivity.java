package com.example.cst2335lab2;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrame = findViewById(R.id.content_frame);

        View homeView = getLayoutInflater().inflate(
                R.layout.activity_main,
                contentFrame,
                false
        );

        contentFrame.addView(homeView);

    }
}