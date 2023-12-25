package com.example.pemilubersama;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.BreakIterator;

public class ComplateData extends AppCompatActivity {
    private EditText editTextNik, editTextFullName, editTextOrigin, editTextBirthDate,
            editTextAddress, editTextOccupation, editTextPhoneNumber;
    private Spinner spinnerGender;
    private Button buttonSaveData;

    private DatabaseAcc dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complate_data);


        dbHelper = new DatabaseAcc(this);

        editTextNik = findViewById(R.id.editTextNik);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextOrigin = findViewById(R.id.editTextOrigin);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextOccupation = findViewById(R.id.editTextOccupation);
        spinnerGender = findViewById(R.id.spinnerGenders);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonSaveData = findViewById(R.id.buttonSaveData);

        buttonSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToDatabase();
            }
        });

        dbHelper = new DatabaseAcc(this);

        Intent intent = getIntent();
        if (intent.hasExtra("NIK_TO_DISPLAY")) {
            String nikToDisplay = intent.getStringExtra("NIK_TO_DISPLAY");

            displayDataFromDatabase(nikToDisplay);
        }

    }

    private void displayDataFromDatabase(String nikToDisplay) {
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

        String selection = DatabaseAcc.COLUMN_NIK + " = ?";
        String[] selectionArgs = {nikToDisplay};

        Cursor cursor = db.query(
                DatabaseAcc.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToNext()) {
            int fullNameIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_FULL_NAME);
            String fullName = cursor.getString(fullNameIndex);

            int originIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_ORIGIN);
            String origin = cursor.getString(originIndex);

            int birthDateIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_BIRTH_DATE);
            String birthDate = cursor.getString(birthDateIndex);

            int addressIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_ADDRESS);
            String address = cursor.getString(addressIndex);

            int occupationIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_OCCUPATION);
            String occupation = cursor.getString(occupationIndex);

            int genderIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_GENDER);
            String gender = cursor.getString(genderIndex);

            int phoneNumberIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_PHONE_NUMBER);
            String phoneNumber = cursor.getString(phoneNumberIndex);
            editTextNik.setText(nikToDisplay);
            editTextFullName.setText(fullName);
            editTextOrigin.setText(origin);
            editTextBirthDate.setText(birthDate);
            editTextAddress.setText(address);
            editTextOccupation.setText(occupation);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.gender_array,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerGender.setAdapter(adapter);
            int position = adapter.getPosition(gender);
            spinnerGender.setSelection(position);

            editTextPhoneNumber.setText(phoneNumber);
        }

        cursor.close();
        db.close();
    }

    private void saveDataToDatabase() {
        String nik = editTextNik.getText().toString();
        String fullName = editTextFullName.getText().toString();
        String origin = editTextOrigin.getText().toString();
        String birthDate = editTextBirthDate.getText().toString();
        String address = editTextAddress.getText().toString();
        String occupation = editTextOccupation.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = {DatabaseAcc.COLUMN_NIK};
        String selection = DatabaseAcc.COLUMN_NIK + " = ?";
        String[] selectionArgs = {nik};

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
            ContentValues updateValues = new ContentValues();
            updateValues.put(DatabaseAcc.COLUMN_FULL_NAME, fullName);
            updateValues.put(DatabaseAcc.COLUMN_ORIGIN, origin);
            updateValues.put(DatabaseAcc.COLUMN_BIRTH_DATE, birthDate);
            updateValues.put(DatabaseAcc.COLUMN_ADDRESS, address);
            updateValues.put(DatabaseAcc.COLUMN_OCCUPATION, occupation);
            updateValues.put(DatabaseAcc.COLUMN_PHONE_NUMBER, phoneNumber);
            updateValues.put(DatabaseAcc.COLUMN_GENDER, gender);

            int rowsAffected = db.update(
                    DatabaseAcc.TABLE_NAME,
                    updateValues,
                    selection,
                    selectionArgs
            );

            if (rowsAffected > 0) {
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
            }
        } else {
            ContentValues insertValues = new ContentValues();
            insertValues.put(DatabaseAcc.COLUMN_NIK, nik);
            insertValues.put(DatabaseAcc.COLUMN_FULL_NAME, fullName);
            insertValues.put(DatabaseAcc.COLUMN_ORIGIN, origin);
            insertValues.put(DatabaseAcc.COLUMN_BIRTH_DATE, birthDate);
            insertValues.put(DatabaseAcc.COLUMN_ADDRESS, address);
            insertValues.put(DatabaseAcc.COLUMN_OCCUPATION, occupation);
            insertValues.put(DatabaseAcc.COLUMN_GENDER, gender);
            insertValues.put(DatabaseAcc.COLUMN_PHONE_NUMBER, phoneNumber);

            long newRowId = db.insert(DatabaseAcc.TABLE_NAME, null, insertValues);

            if (newRowId != -1) {
                Toast.makeText(this, "Data baru berhasil disimpan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal menyimpan data baru", Toast.LENGTH_SHORT).show();
            }
        }

        cursor.close();
        db.close();

        Intent intent = new Intent(ComplateData.this, DashBoard.class);
        startActivity(intent);

        finish();
    }
}