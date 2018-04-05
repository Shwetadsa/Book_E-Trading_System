package com.shwetadsa.bets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etUname, etPass;
    Button btnLogin, btnReg;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUname = findViewById(R.id.etUname);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btnReg);
        final SharedPreferences userdata = getSharedPreferences("UserData", Context.MODE_PRIVATE);


        try{
            if (userdata.getString("Type", null).equals("LoggedIn")){
                startActivity(new Intent(MainActivity.this, firstpage.class));
                finish();
            }
        } catch (Exception e) {

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uname = etUname.getText().toString();
                    String pass = etPass.getText().toString();

                    if (uname.length() == 0) {
                        Snackbar.make(v, "Enter your username", Snackbar.LENGTH_LONG).show();
                        etUname.requestFocus();
                        return;
                    }

                    if (pass.length() == 0) {
                        Snackbar.make(v, "Enter your password", Snackbar.LENGTH_LONG).show();
                        etPass.requestFocus();
                        return;
                    }

                    //check if uname and password exist in database and allow/disallow access accordingly; use finish
                    Boolean chkunamepass = db.chkunamepass(uname,pass);
                    if (chkunamepass == true) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        String fname = db.getfname(uname);
                        String lname = db.getlname(uname);
                        SharedPreferences.Editor edit = userdata.edit();
                        edit.putString("Username", uname);
                        edit.putString("Password", pass);
                        edit.putString("First Name", fname);
                        edit.putString("Last Name", lname);
                        //edit.putInt("Amount", );
                        //edit.putInt("Age", );
                        edit.putString("Type", "LoggedIn");
                        edit.commit();
                        startActivity(new Intent(MainActivity.this, firstpage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Check your credentials", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            btnReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, Register.class);
                    startActivity(i);
                }
            });
        }
    }
}
