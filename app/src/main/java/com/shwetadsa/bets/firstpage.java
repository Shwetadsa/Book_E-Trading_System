package com.shwetadsa.bets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;

public class firstpage extends AppCompatActivity {

    Button btnAdd, btnBrowse, btnReturn;
    TextView tvWel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        tvWel = findViewById(R.id.tvWel);
        btnAdd = findViewById(R.id.btnAdd);
        btnBrowse = findViewById(R.id.btnBrowse);
        btnReturn = findViewById(R.id.btnReturn);
        final SharedPreferences userdata = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String fname = userdata.getString("First Name", "");
        String lname = userdata.getString("Last Name", "");
        tvWel.setText("Welcome " + fname + " " + lname);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(firstpage.this, addbook.class);
                startActivity(i);
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }//end of onCreate

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(firstpage.this);
        builder.setMessage("Do you really want to exit this application?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.setTitle("Exit");
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.About){
            Toast.makeText(this, "App developed by Ms. Shweta D'Sa", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.Contact){
            Toast.makeText(this, "For any queries, dial 9769190801", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.issued){

        }
        if (item.getItemId() == R.id.Logout){
            SharedPreferences userdata = getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = userdata.edit();
            edit.clear();
            edit.apply();
            startActivity(new Intent(firstpage.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
