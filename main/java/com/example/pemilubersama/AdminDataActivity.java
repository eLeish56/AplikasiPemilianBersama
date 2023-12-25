package com.example.pemilubersama;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AdminDataActivity extends AppCompatActivity {

    private ListView listViewAccounts;
    private DatabaseAcc dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_data);

        dbHelper = new DatabaseAcc(this);
        listViewAccounts = findViewById(R.id.listViewAccounts);

        showDataInListView();
    }

    private List<Account> fetchDataFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseAcc.COLUMN_NIK,
                DatabaseAcc.COLUMN_FULL_NAME,
                DatabaseAcc.COLUMN_ADDRESS,
                DatabaseAcc.COLUMN_BIRTH_DATE,
                DatabaseAcc.COLUMN_ORIGIN,
                DatabaseAcc.COLUMN_OCCUPATION,
                DatabaseAcc.COLUMN_GENDER,
                DatabaseAcc.COLUMN_PHONE_NUMBER
        };

        Cursor cursor = db.query(
                DatabaseAcc.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Account> accountList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String nik = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_NIK));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_FULL_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_ADDRESS));
            String birthDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_BIRTH_DATE));
            String origin = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_ORIGIN));
            String occupation = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_OCCUPATION));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_GENDER));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_PHONE_NUMBER));

            Account account = new Account(nik, fullName, origin, birthDate, address, occupation, gender, phoneNumber);
            accountList.add(account);
        }
        cursor.close();
        db.close();

        return accountList;
    }

    private void showDataInListView() {
        List<Account> accountList = fetchDataFromDatabase();

        AdapterCustom adapter = new AdapterCustom(this, accountList);
        listViewAccounts.setAdapter(adapter);
    }
}