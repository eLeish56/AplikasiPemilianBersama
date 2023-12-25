package com.example.pemilubersama;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class AdapterCustom extends ArrayAdapter<Account> {
    private ListView listView;
    private ArrayAdapter<Account> adapter;
    private DatabaseAcc dbHelper;
    public AdapterCustom(Context context, List<Account> accounts) {
        super(context, 0, accounts);
        this.listView = listView;
        this.adapter = adapter;
        this.dbHelper = new DatabaseAcc(context);
    }
    public void updateData(List<Account> newData) {
        clear();
        addAll(newData);
        notifyDataSetChanged();
    }
    public void refreshData() {
        List<Account> updatedData = dbHelper.fetchDataFromDatabase(); // Use dbHelper to fetch data
        updateData(updatedData);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Account account = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_account, parent, false);
        }

        TextView textViewNik, textViewFullName, textviewasal;
        TextView textviewtanggallahir, textviewalamat, textviewpekerjaan, textviewjk, textviewno;
        textViewNik = convertView.findViewById(R.id.textViewNik);
        textViewFullName = convertView.findViewById(R.id.textViewFullName);
        textviewasal = convertView.findViewById(R.id.textViewOrigin);
        textviewtanggallahir = convertView.findViewById(R.id.textViewBirthDate);
        textviewalamat = convertView.findViewById(R.id.textViewAddress);
        textviewpekerjaan = convertView.findViewById(R.id.textViewOccupation);
        textviewjk = convertView.findViewById(R.id.textViewGender);
        textviewno = convertView.findViewById(R.id.textViewPhoneNumber);
        Button buttonEdit = convertView.findViewById(R.id.buttonEdit);
        Button buttonDelete = convertView.findViewById(R.id.buttonDelete);

        if (account != null) {
            textViewNik.setText("NIK : " + account.getNik());
            textViewFullName.setText("Nama Lengkap : " + account.getFullName());
            textviewasal.setText("Asal : " + account.getOrigin());
            textviewtanggallahir.setText("Tanggal Lahir : " + account.getBirthDate());
            textviewalamat.setText("Alamat : " + account.getAddress());
            textviewpekerjaan.setText("Pekerjaan : " + account.getOccupation());
            textviewjk.setText("Jenis Kelamin : " + account.getGender());
            textviewno.setText("No Hp : " + account.getPhoneNumber());
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditDialog(account);
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmation(account);
                }
            });
        }
        return convertView;
    }
    private void showEditDialog(Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_edit_data, null);
        builder.setView(view);
        EditText editTextNik = view.findViewById(R.id.editTextNik);
        EditText editTextFullName = view.findViewById(R.id.editTextFullName);
        EditText editTextOrigin = view.findViewById(R.id.editTextOrigin);
        EditText editTextBirthDate = view.findViewById(R.id.editTextBirthDate);
        EditText editTextAddress = view.findViewById(R.id.editTextAddress);
        EditText editTextOccupation = view.findViewById(R.id.editTextOccupation);
        EditText editTextGender = view.findViewById(R.id.spinnerGenders);
        EditText editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);

        editTextNik.setText(account.getNik());
        editTextFullName.setText(account.getFullName());
        editTextOrigin.setText(account.getOrigin());
        editTextBirthDate.setText(account.getBirthDate());
        editTextAddress.setText(account.getAddress());
        editTextOccupation.setText(account.getOccupation());
        editTextGender.setText(account.getGender());
        editTextPhoneNumber.setText(account.getPhoneNumber());

        Button buttonSimpan = view.findViewById(R.id.buttonSave);

        // TOMBOL UNTUK IALOG
        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NILAI BARU
                String newNik = editTextNik.getText().toString();
                String newFullName = editTextFullName.getText().toString();
                String newOrigin = editTextOrigin.getText().toString();
                String newBirthDate = editTextBirthDate.getText().toString();
                String newAddress = editTextAddress.getText().toString();
                String newOccupation = editTextOccupation.getText().toString();
                String newGender = editTextGender.getText().toString();
                String newPhoneNumber = editTextPhoneNumber.getText().toString();
                updateAccount(account, newNik, newFullName, newOrigin, newBirthDate,
                        newAddress, newOccupation, newGender, newPhoneNumber);
            }
        });
        builder.show();
    }
    private void showDeleteConfirmation(Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Konfirmasi Hapus");
        builder.setMessage("Apakah Anda yakin ingin menghapus akun dengan NIK " + account.getNik() + "?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAccount(account);

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

    private void deleteAccount(Account account) {
        DatabaseAcc dbHelper = new DatabaseAcc(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = DatabaseAcc.COLUMN_NIK + " = ?";
        String[] selectionArgs = {account.getNik()};

        int deletedRows = db.delete(DatabaseAcc.TABLE_NAME, selection, selectionArgs);

        db.close();

        if (deletedRows > 0) {
            Toast.makeText(getContext(), "Akun berhasil dihapus", Toast.LENGTH_SHORT).show();
            List<Account> updatedData = dbHelper.fetchDataFromDatabase(); // Gantilah dengan metode sesuai kebutuhan
            updateData(updatedData);

        } else {
            Toast.makeText(getContext(), "Gagal menghapus akun", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAccount(Account oldAccount, String newNik, String newFullName,
                               String newOrigin, String newBirthDate, String newAddress,
                               String newOccupation, String newGender, String newPhoneNumber) {
        DatabaseAcc dbHelper = new DatabaseAcc(getContext());
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
        String[] selectionArgs = {oldAccount.getNik()};

        int updatedRows = db.update(DatabaseAcc.TABLE_NAME, values, selection, selectionArgs);

        db.close();

        if (updatedRows > 0) {
            Toast.makeText(getContext(), "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
            List<Account> updatedData = dbHelper.fetchDataFromDatabase();
            updateData(updatedData);
        } else {
            Toast.makeText(getContext(), "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
        }
    }
}
