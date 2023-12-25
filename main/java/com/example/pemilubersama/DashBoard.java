package com.example.pemilubersama;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashBoard extends AppCompatActivity {
    private Button buttonPresiden1, buttonPresiden2, buttonPresiden3;
    private DatabaseSuara dbSuara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        dbSuara = new DatabaseSuara(this);

        buttonPresiden1 = findViewById(R.id.buttonPresiden1);
        buttonPresiden2 = findViewById(R.id.buttonPresiden2);
        buttonPresiden3 = findViewById(R.id.buttonPresiden3);

        buttonPresiden1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteForPresident(1);
                Intent intent = new Intent(DashBoard.this, Presiden1Activity.class);
                startActivity(intent);
            }
        });

        buttonPresiden2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteForPresident(2);
                Intent intent = new Intent(DashBoard.this, Presiden2Activity.class);
                startActivity(intent);
            }
        });

        buttonPresiden3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteForPresident(3);
                Intent intent = new Intent(DashBoard.this, Presiden3Activity.class);
                startActivity(intent);
            }
        });
    }

    private void voteForPresident(int presidentNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String nikAkunLogin = sharedPreferences.getString("NIK_LOGIN", "");

        if (!hasUserVoted(nikAkunLogin, presidentNumber)) {
            Log.d("VoteForPresident", "Voting for President: " + presidentNumber);

            dbSuara.insertVote(nikAkunLogin, presidentNumber);

            Log.d("VoteForPresident", "Vote successfully recorded.");
        } else {
            Log.d("VoteForPresident", "User has already voted.");
        }
    }

    private boolean hasUserVoted(String nik, int presidentNumber) {
        for (int i = 1; i <= 3; i++) {
            if (dbSuara.getVotesCountByPresident(nik, i) > 0) {
                return true;
            }
        }
        return false;
    }
}