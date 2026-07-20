package com.example.todoapp;   // <-- change to your actual package

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DBHelper(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUpActivity.this,
                        "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String hash = HashUtils.sha256(password);
            boolean success = dbHelper.registerUser(name, email, hash);

            if (success) {
                Toast.makeText(SignUpActivity.this,
                        "Registered, please login", Toast.LENGTH_SHORT).show();
                finish(); // back to login
            } else {
                Toast.makeText(SignUpActivity.this,
                        "Email already used or error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}