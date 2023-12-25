package com.example.pemilubersama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DashboardAdminActivity extends AppCompatActivity {
    private TextView totalSuaraPresiden1, totalSuaraPresiden2, totalSuaraPresiden3;
    private DatabaseSuara dbSuara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        Button btnlihatdata = findViewById(R.id.btndata);
        dbSuara = new DatabaseSuara(this);

        totalSuaraPresiden1 = findViewById(R.id.totalSuaraPresiden1);
        totalSuaraPresiden2 = findViewById(R.id.totalSuaraPresiden2);
        totalSuaraPresiden3 = findViewById(R.id.totalSuaraPresiden3);

        btnlihatdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btndataall();
            }
        });
    }

    public void btndataall() {
        Intent intent = new Intent(DashboardAdminActivity.this, AdminDataActivity.class);
        startActivity(intent);
    }

    public void btnlogout(){
        Intent intent = new Intent(DashboardAdminActivity.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTotalVotes();
    }

    private void updateTotalVotes() {
        int totalVotes1 = dbSuara.getTotalVotesByPresident(1);
        int totalVotes2 = dbSuara.getTotalVotesByPresident(2);
        int totalVotes3 = dbSuara.getTotalVotesByPresident(3);

        totalSuaraPresiden1.setText("Total Suara Presiden 1: " + totalVotes1);
        totalSuaraPresiden2.setText("Total Suara Presiden 2: " + totalVotes2);
        totalSuaraPresiden3.setText("Total Suara Presiden 3: " + totalVotes3);
    }
}