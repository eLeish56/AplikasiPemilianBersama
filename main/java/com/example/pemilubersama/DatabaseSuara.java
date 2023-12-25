package com.example.pemilubersama;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSuara extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "suara.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "votes";
    public static final String COLUMN_NIK = "nik";
    public static final String COLUMN_PRESIDENT_NUMBER = "president_number";

    public DatabaseSuara(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NIK + " TEXT," +
                COLUMN_PRESIDENT_NUMBER + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertVote(String nik, int presidentNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NIK, nik);
        values.put(COLUMN_PRESIDENT_NUMBER, presidentNumber);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int getVotesCountByPresident(String nik, int presidentNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_NIK};
        String selection = COLUMN_NIK + " = ? AND " + COLUMN_PRESIDENT_NUMBER + " = ?";
        String[] selectionArgs = {nik, String.valueOf(presidentNumber)};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public int getTotalVotesByPresident(int presidentNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_NIK};
        String selection = COLUMN_PRESIDENT_NUMBER + " = ?";
        String[] selectionArgs = {String.valueOf(presidentNumber)};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}