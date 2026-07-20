package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to manage SQLite database for users and tasks.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "todo_app.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password_hash TEXT NOT NULL" +
                ")";

        String createTasks = "CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "title TEXT NOT NULL," +
                "notes TEXT," +
                "is_completed INTEGER DEFAULT 0," +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ")";

        db.execSQL(createUsers);
        db.execSQL(createTasks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // ---------------- USER METHODS ----------------

    public boolean registerUser(String name, String email, String passwordHash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("email", email);
        cv.put("password_hash", passwordHash);
        long result = db.insert("users", null, cv);
        return result != -1;
    }

    public int loginUser(String email, String passwordHash) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                new String[]{"id"},
                "email = ? AND password_hash = ?",
                new String[]{email, passwordHash},
                null, null, null
        );
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
            return userId;
        }
        if (cursor != null) cursor.close();
        return -1;
    }



    public long addTask(int userId, String title, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_id", userId);
        cv.put("title", title);
        cv.put("notes", notes);
        cv.put("is_completed", 0);
        return db.insert("tasks", null, cv);
    }

    public Cursor getTasksForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                "tasks",
                null,
                "user_id = ?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );
    }

    public void markTaskCompleted(int taskId, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("is_completed", completed ? 1 : 0);
        db.update("tasks", cv, "id = ?", new String[]{String.valueOf(taskId)});
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tasks", "id = ?", new String[]{String.valueOf(taskId)});
    }
}