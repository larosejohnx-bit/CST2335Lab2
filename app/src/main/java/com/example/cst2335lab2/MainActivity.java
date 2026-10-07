package com.example.cst2335lab2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TodoDatabaseHelper dbHelper;
    private SQLiteDatabase db;

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

        // Open database
        dbHelper = new TodoDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Keeps content below the status bar
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            v.getPaddingLeft(),
                            systemBars.top,
                            v.getPaddingRight(),
                            v.getPaddingBottom()
                    );

                    return insets;
                });

        // Connect views
        todoEditText = findViewById(R.id.todoEditText);
        urgentSwitch = findViewById(R.id.urgentSwitch);
        addButton = findViewById(R.id.addButton);
        todoListView = findViewById(R.id.todoListView);

        // Create list and adapter
        todoList = new ArrayList<>();

        adapter = new TodoAdapter(this, todoList);

        todoListView.setAdapter(adapter);

        // Load saved todos from database
        loadTodos();

        // ADD BUTTON
        addButton.setOnClickListener(view -> {

            String text = todoEditText
                    .getText()
                    .toString()
                    .trim();

            if (!text.isEmpty()) {

                boolean urgent = urgentSwitch.isChecked();

                // Values to save in SQLite
                ContentValues values = new ContentValues();

                values.put(
                        TodoDatabaseHelper.COL_TODO,
                        text
                );

                values.put(
                        TodoDatabaseHelper.COL_URGENT,
                        urgent ? 1 : 0
                );

                // Insert into database
                long newId = db.insert(
                        TodoDatabaseHelper.TABLE_NAME,
                        null,
                        values
                );

                if (newId != -1) {

                    // Add to ListView
                    TodoItem item =
                            new TodoItem(newId, text, urgent);

                    todoList.add(item);

                    adapter.notifyDataSetChanged();

                    // Clear input
                    todoEditText.setText("");
                    urgentSwitch.setChecked(false);

                    Log.d(
                            "Database",
                            "Todo added with ID: " + newId
                    );
                }
            }
        });

        // LONG CLICK DELETE
        todoListView.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(this);

                    builder.setTitle(R.string.delete_title);

                    builder.setMessage(
                            getString(
                                    R.string.selected_row,
                                    position
                            )
                    );

                    builder.setPositiveButton(
                            R.string.yes,
                            (dialog, which) -> {

                                TodoItem selectedTodo =
                                        todoList.get(position);

                                // Delete from SQLite
                                int rowsDeleted = db.delete(
                                        TodoDatabaseHelper.TABLE_NAME,
                                        TodoDatabaseHelper.COL_ID + "=?",
                                        new String[]{
                                                String.valueOf(
                                                        selectedTodo.getId()
                                                )
                                        }
                                );

                                if (rowsDeleted > 0) {

                                    // Delete from ListView
                                    todoList.remove(position);

                                    adapter.notifyDataSetChanged();

                                    Log.d(
                                            "Database",
                                            "Todo deleted"
                                    );
                                }
                            });

                    builder.setNegativeButton(
                            R.string.no,
                            null
                    );

                    builder.show();

                    return true;
                });
    }

    // LOAD TODOS FROM DATABASE
    private void loadTodos() {

        todoList.clear();

        try (Cursor cursor = db.query(
                TodoDatabaseHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TodoDatabaseHelper.COL_ID + " ASC"
        )) {

            // Required Lab 5 debugging output
            printCursor(cursor);

            int idIndex =
                    cursor.getColumnIndexOrThrow(
                            TodoDatabaseHelper.COL_ID
                    );

            int textIndex =
                    cursor.getColumnIndexOrThrow(
                            TodoDatabaseHelper.COL_TODO
                    );

            int urgentIndex =
                    cursor.getColumnIndexOrThrow(
                            TodoDatabaseHelper.COL_URGENT
                    );

            while (cursor.moveToNext()) {

                long id =
                        cursor.getLong(idIndex);

                String text =
                        cursor.getString(textIndex);

                boolean urgent =
                        cursor.getInt(urgentIndex) == 1;

                todoList.add(
                        new TodoItem(
                                id,
                                text,
                                urgent
                        )
                );
            }
        }

        adapter.notifyDataSetChanged();
    }

    // REQUIRED printCursor METHOD
    private void printCursor(Cursor c) {

        Log.d(
                "Database",
                "Database version: " + db.getVersion()
        );

        Log.d(
                "Database",
                "Number of columns: " + c.getColumnCount()
        );

        String[] columnNames = c.getColumnNames();

        for (String columnName : columnNames) {

            Log.d(
                    "Database",
                    "Column name: " + columnName
            );
        }

        Log.d(
                "Database",
                "Number of results: " + c.getCount()
        );

        c.moveToPosition(-1);

        while (c.moveToNext()) {

            StringBuilder row =
                    new StringBuilder();

            for (int i = 0;
                 i < c.getColumnCount();
                 i++) {

                row.append(columnNames[i])
                        .append("=")
                        .append(c.getString(i))
                        .append(" ");
            }

            Log.d(
                    "Database",
                    "Row: " + row
            );
        }

        // Reset cursor so loadTodos can read it
        c.moveToPosition(-1);
    }
}