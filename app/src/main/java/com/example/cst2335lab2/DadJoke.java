package com.example.cst2335lab2;

import android.os.Bundle;
import android.widget.FrameLayout;

public class DadJoke extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrame = findViewById(R.id.content_frame);

        getLayoutInflater().inflate(
                R.layout.activity_dad_joke,
                contentFrame,
                true
        );
    }
}