package com.example.pemilubersama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private EditText editTextNik, editTextBirthPlace;
    private Button buttonLogin, buttonRegister;

    private DatabaseAcc dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseAcc(this);

        editTextNik = findViewById(R.id.editTextNik);
        editTextBirthPlace = findViewById(R.id.editTextBirthPlace);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrasiActivity.class);
                startActivity(intent);
            }
        });
        Button buttonAdminLogin = findViewById(R.id.buttonAdminLogin);

        buttonAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_admin_login.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String inputNik = editTextNik.getText().toString();
        String inputBirthPlace = editTextBirthPlace.getText().toString();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseAcc.COLUMN_NIK,
                DatabaseAcc.COLUMN_BIRTH_DATE
        };

        String selection = DatabaseAcc.COLUMN_NIK + " = ? AND " +
                DatabaseAcc.COLUMN_BIRTH_DATE + " = ?";
        String[] selectionArgs = {inputNik, inputBirthPlace};

        Cursor cursor = db.query(
                DatabaseAcc.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("NIK_LOGIN", inputNik);
            editor.apply();
            String nikToSend = editTextNik.getText().toString();

            Intent intent = new Intent(MainActivity.this, ComplateData.class);
            intent.putExtra("NIK_TO_DISPLAY", nikToSend);
            startActivity(intent);


            finish();
        } else {
            Toast.makeText(this, "Login gagal. Periksa NIK dan Tanggal Lahir Anda.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}