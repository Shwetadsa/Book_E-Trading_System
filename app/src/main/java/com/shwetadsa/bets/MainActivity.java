package com.shwetadsa.bets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    EditText etUname, etPass;
    Button btnLogin, btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUname = findViewById(R.id.etUname);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btnReg);
        // Access a Cloud Firestore instance from your Activity
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = etUname.getText().toString();
                final String pass = etPass.getText().toString();

                if (uname.length() == 0){
                    Snackbar.make(v, "Enter your username", Snackbar.LENGTH_LONG).show();
                    etUname.requestFocus();
                    return;
                }

                if (pass.length() == 0){
                    Snackbar.make(v, "Enter your password", Snackbar.LENGTH_LONG).show();
                    etPass.requestFocus();
                    return;
                }

                //check if uname and password exist in database and allow/disallow access accordingly; use finish
                DocumentReference ref =db.collection("users").document(uname);
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                if(document.get("Password").equals(pass)) {
                                    SharedPreferences userdata = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = userdata.edit();
                                    edit.putString("Username",document.getString("Username"));
                                    edit.putString("Password",document.getString("Password"));
                                    edit.putString("Email",document.getString("Email"));
                                    edit.putString("Contact",document.getString("Contact"));
                                    edit.putString("First Name",document.getString("First Name"));
                                    edit.putString("Last Name",document.getString("Last Name"));
                                    //edit.putString("Code",document.getString("Code"));
                                    edit.commit();
                                    startActivity(new Intent(MainActivity.this, firstpage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    finish();
                                }else
                                    Toast.makeText(getApplicationContext(),"Incorrect Password",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "No such user",Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "get failed with "+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

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
