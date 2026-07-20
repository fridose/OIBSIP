# Task 2 – To-Do List Android App

This is a simple To-Do List Android application built using **Java**, **XML layouts**, and a local **SQLite** database. It was developed as part of the OIBSIP Android App Development internship.[web:143][web:146][web:203]

## Demo video



https://github.com/user-attachments/assets/e4982194-114d-4604-8e2b-ddafe6b837e0




## Features

- User **registration** and **login** using email and password (hashed with SHA-256).
- Session management with **SharedPreferences** so users stay logged in until logout.
- Per-user task list with:
  - Add new task (title + optional notes).
  - Mark task as **completed**.
  - Delete task from the list.
- Tasks are stored offline in a local **SQLite** database and displayed using a **RecyclerView**.

## Project Structure

- `LoginActivity` – Handles user login and navigation to the main screen.
- `SignUpActivity` – Allows new users to register.
- `MainActivity` – Shows the list of tasks and provides Add / Logout actions.
- `DBHelper` – Manages SQLite tables and CRUD operations for users and tasks.
- `Task` & `TaskAdapter` – Model and adapter for binding tasks to the RecyclerView.[web:143][web:145]

