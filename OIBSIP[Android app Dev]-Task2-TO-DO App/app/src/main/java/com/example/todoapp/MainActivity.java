package com.example.todoapp;   // <-- use your actual package name

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private TextView tvEmpty;
    private Button btnAddTask, btnLogout;
    private DBHelper dbHelper;
    private int currentUserId;
    private TaskAdapter adapter;
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        currentUserId = prefs.getInt("user_id", -1);
        if (currentUserId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        dbHelper = new DBHelper(this);

        rvTasks = findViewById(R.id.rvTasks);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnLogout = findViewById(R.id.btnLogout);

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(taskList, dbHelper);
        rvTasks.setAdapter(adapter);

        loadTasks();

        btnAddTask.setOnClickListener(v -> showAddTaskDialog());

        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadTasks() {
        taskList.clear();
        Cursor cursor = dbHelper.getTasksForUser(currentUserId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"));
                int completed = cursor.getInt(cursor.getColumnIndexOrThrow("is_completed"));

                taskList.add(new Task(id, currentUserId, title, notes, completed == 1));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (taskList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etNotes = view.findViewById(R.id.etNotes);

        builder.setView(view);
        builder.setTitle("Add Task");
        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();
            if (!title.isEmpty()) {
                dbHelper.addTask(currentUserId, title, notes);
                loadTasks();
            } else {
                Toast.makeText(MainActivity.this,
                        "Task title required", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}