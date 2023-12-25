package com.example.pemilubersama;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrasiActivity extends AppCompatActivity {
    private EditText editTextNik, editTextFullName, editTextOrigin, editTextBirthDate;
    private Button buttonRegisterUser;

    private DatabaseAcc dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        dbHelper = new DatabaseAcc(this);

        editTextNik = findViewById(R.id.editTextNik);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextOrigin = findViewById(R.id.editTextOrigin);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        buttonRegisterUser = findViewById(R.id.buttonRegisterUser);

        buttonRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String nik = editTextNik.getText().toString();
        String fullName = editTextFullName.getText().toString();
        String origin = editTextOrigin.getText().toString();
        String birthDate = editTextBirthDate.getText().toString();

        if (nik.isEmpty() && fullName.isEmpty() && origin.isEmpty() && birthDate.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseAcc.COLUMN_NIK, nik);
            values.put(DatabaseAcc.COLUMN_FULL_NAME, fullName);
            values.put(DatabaseAcc.COLUMN_ORIGIN, origin);
            values.put(DatabaseAcc.COLUMN_BIRTH_DATE, birthDate);

            long newRowId = db.insert(DatabaseAcc.TABLE_NAME, null, values);
            db.close();

            if (newRowId != -1) {
                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menyimpan ke database", Toast.LENGTH_SHORT).show();
            }
        }
    }
}