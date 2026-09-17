package com.example.cst2335lab2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    Button pressButton;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_linear);

        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        pressButton = findViewById(R.id.pressButton);
        checkBox = findViewById(R.id.checkBox);

        pressButton.setOnClickListener(view -> {

            textView.setText(editText.getText().toString());

            Toast.makeText(
                    this,
                    getResources().getString(R.string.toast_message),
                    Toast.LENGTH_SHORT
            ).show();
        });

        checkBox.setOnCheckedChangeListener(
                (CompoundButton cb, boolean b) -> {

                    String state;

                    if (b) {
                        state = getResources().getString(R.string.on);
                    } else {
                        state = getResources().getString(R.string.off);
                    }

                    String message =
                            getResources().getString(R.string.checkbox_message)
                                    + " " + state;

                    Snackbar.make(
                            cb,
                            message,
                            Snackbar.LENGTH_LONG
                    ).setAction(
                            getResources().getString(R.string.undo),
                            click -> cb.setChecked(!b)
                    ).show();
                }
        );
    }
}