package com.example.pemilubersama;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class EditDataActivity extends AppCompatActivity {
    private EditText editTextNik, editTextFullName, editTextOrigin, editTextBirthDate;
    private EditText editTextAddress, editTextOccupation, editTextPhoneNumber;
    private Button buttonSave;
    private EditText spinnerGenders;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        editTextNik = findViewById(R.id.editTextNik);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextOrigin = findViewById(R.id.editTextOrigin);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextOccupation = findViewById(R.id.editTextOccupation);
        spinnerGenders = findViewById(R.id.spinnerGenders);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonSave = findViewById(R.id.buttonSave);

        account = getIntent().getParcelableExtra("account");

        fetchDataFromDatabase(account.getNik());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditConfirmation();

            }
        });
    }

    private void fetchDataFromDatabase(String nik) {
        DatabaseAcc dbHelper = new DatabaseAcc(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseAcc.COLUMN_NIK,
                DatabaseAcc.COLUMN_FULL_NAME,
                DatabaseAcc.COLUMN_ORIGIN,
                DatabaseAcc.COLUMN_BIRTH_DATE,
                DatabaseAcc.COLUMN_ADDRESS,
                DatabaseAcc.COLUMN_OCCUPATION,
                DatabaseAcc.COLUMN_GENDER,
                DatabaseAcc.COLUMN_PHONE_NUMBER
        };
        // KONDISI
        String selection = DatabaseAcc.COLUMN_NIK + " = ?";
        String[] selectionArgs = {nik};

        // EKSEKUSI
        Cursor cursor = db.query(
                DatabaseAcc.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        // CEK DATA
        if (cursor.moveToFirst()) {
            editTextNik.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_NIK)));
            editTextFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_FULL_NAME)));
            editTextOrigin.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_ORIGIN)));
            editTextBirthDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_BIRTH_DATE)));
            editTextAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_ADDRESS)));
            editTextOccupation.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_OCCUPATION)));
            editTextPhoneNumber.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_PHONE_NUMBER)));

        }

        // Tutup cursor dan koneksi database
        cursor.close();
        db.close();
    }

    private void showEditConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Edit");
        builder.setMessage("Apakah Anda yakin ingin menyimpan perubahan?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNik = editTextNik.getText().toString();
                String newFullName = editTextFullName.getText().toString();
                String newOrigin = editTextOrigin.getText().toString();
                String newBirthDate = editTextBirthDate.getText().toString();
                String newAddress = editTextAddress.getText().toString();
                String newOccupation = editTextOccupation.getText().toString();
                String newGender = spinnerGenders.getText().toString();
                String newPhoneNumber = editTextPhoneNumber.getText().toString();

                updateAccount(newNik, newFullName, newOrigin, newBirthDate,
                        newAddress, newOccupation, newGender, newPhoneNumber);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void updateAccount(String newNik, String newFullName, String newOrigin,
                               String newBirthDate, String newAddress, String newOccupation,
                               String newGender, String newPhoneNumber) {
        DatabaseAcc dbHelper = new DatabaseAcc(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseAcc.COLUMN_NIK, newNik);
        values.put(DatabaseAcc.COLUMN_FULL_NAME, newFullName);
        values.put(DatabaseAcc.COLUMN_ORIGIN, newOrigin);
        values.put(DatabaseAcc.COLUMN_BIRTH_DATE, newBirthDate);
        values.put(DatabaseAcc.COLUMN_ADDRESS, newAddress);
        values.put(DatabaseAcc.COLUMN_OCCUPATION, newOccupation);
        values.put(DatabaseAcc.COLUMN_GENDER, newGender);
        values.put(DatabaseAcc.COLUMN_PHONE_NUMBER, newPhoneNumber);

        String selection = DatabaseAcc.COLUMN_NIK + " = ?";
        String[] selectionArgs = {account.getNik()};

        int updatedRows = db.update(DatabaseAcc.TABLE_NAME, values, selection, selectionArgs);

        db.close();

        if (updatedRows > 0) {
            Toast.makeText(EditDataActivity.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
            List<Account> updatedData = dbHelper.fetchDataFromDatabase();
            finish();
        } else {
            Toast.makeText(EditDataActivity.this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
        }
    }
}