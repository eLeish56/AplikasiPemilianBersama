package com.example.pemilubersama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class activity_admin_login extends AppCompatActivity {
    private EditText editTextAdminUsername, editTextAdminPassword;
    private Button buttonAdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        editTextAdminUsername = findViewById(R.id.editTextAdminUsername);
        editTextAdminPassword = findViewById(R.id.editTextAdminPassword);
        buttonAdminLogin = findViewById(R.id.buttonAdminLogin);
        buttonAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginAdmin();
            }
        });
    }
    private void loginAdmin() {
        String adminUsername = editTextAdminUsername.getText().toString();
        String adminPassword = editTextAdminPassword.getText().toString();
        if (adminUsername.equals("admin") && adminPassword.equals("admin")) {
            Intent intent = new Intent(activity_admin_login.this, DashboardAdminActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "BERHASIL LOGIN ADMIN", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "GAGAL LOGIN ADMIN", Toast.LENGTH_SHORT).show();
        }
    }
}