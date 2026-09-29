package com.example.cst2335lab2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText todoEditText;
    private Switch urgentSwitch;
    private Button addButton;
    private ListView todoListView;

    private List<TodoItem> todoList;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    v.getPaddingLeft(),
                    systemBars.top,
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );
            return insets;
        });
        todoEditText = findViewById(R.id.todoEditText);
        urgentSwitch = findViewById(R.id.urgentSwitch);
        addButton = findViewById(R.id.addButton);
        todoListView = findViewById(R.id.todoListView);

        todoList = new ArrayList<>();

        adapter = new TodoAdapter(this, todoList);

        todoListView.setAdapter(adapter);

        addButton.setOnClickListener(view -> {

            String text = todoEditText.getText().toString();

            if (!text.isEmpty()) {

                boolean urgent = urgentSwitch.isChecked();

                TodoItem item = new TodoItem(text, urgent);

                todoList.add(item);

                todoEditText.setText("");

                urgentSwitch.setChecked(false);

                adapter.notifyDataSetChanged();
            }
        });

        todoListView.setOnItemLongClickListener((parent, view, position, id) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.delete_title);

            builder.setMessage(
                    getString(R.string.selected_row, position)
            );

            builder.setPositiveButton(R.string.yes, (dialog, which) -> {

                todoList.remove(position);

                adapter.notifyDataSetChanged();
            });

            builder.setNegativeButton(R.string.no, null);

            builder.show();

            return true;
        });
    }
}