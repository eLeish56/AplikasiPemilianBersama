package com.example.pemilubersama;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAcc extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database_akun";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "akun";

    public static final String COLUMN_NIK = "nik";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_ORIGIN = "origin";

    public static final String COLUMN_BIRTH_DATE = "birth_date";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_OCCUPATION = "occupation";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_GENDER = "gender";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NIK + " TEXT PRIMARY KEY," +
                    COLUMN_FULL_NAME + " TEXT," +
                    COLUMN_ORIGIN + " TEXT," +
                    COLUMN_BIRTH_DATE + " TEXT," +
                    COLUMN_ADDRESS + " TEXT," +
                    COLUMN_OCCUPATION + " TEXT," +
                    COLUMN_PHONE_NUMBER + " TEXT," +
                    COLUMN_GENDER + " TEXT)";


    public DatabaseAcc(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<Account> fetchDataFromDatabase() {
        List<Account> dataList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_NIK,
                COLUMN_FULL_NAME,
                COLUMN_ORIGIN,
                COLUMN_BIRTH_DATE,
                COLUMN_ADDRESS,
                COLUMN_OCCUPATION,
                COLUMN_GENDER,
                COLUMN_PHONE_NUMBER,

        };

        String sortOrder = COLUMN_NIK + " ASC";

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String nik = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIK));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME));
            String origin = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORIGIN));
            String birthDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTH_DATE));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS));
            String occupation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OCCUPATION));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));

            Account account = new Account(nik, fullName, origin, birthDate, address, occupation, gender, phoneNumber);
            dataList.add(account);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return dataList;
    }
}
