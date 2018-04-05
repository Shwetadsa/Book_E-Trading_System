package com.shwetadsa.bets;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText etFname, etLastname, etMail,  etContact, etUname, etPass, etCpass, etAge;
    Button btnSignup;
    Spinner spnLoc;

    DatabaseHelper db = new DatabaseHelper(this);

    private String fname, lname, email, contact, uname, pass, confirm_pass, location, age;
    String[] loc;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFname = findViewById(R.id.etFname);
        etLastname = findViewById(R.id.etLastname);
        etMail = findViewById(R.id.etMail);
        etContact = findViewById(R.id.etContact);
        etUname = findViewById(R.id.etUname);
        etPass = findViewById(R.id.etPass);
        etCpass = findViewById(R.id.etCpass);
        etAge = findViewById(R.id.etAge);
        btnSignup = findViewById(R.id.btnSignup);
        spnLoc = findViewById(R.id.spnLoc);

        loc = new String[]{"Mumbai", "Chennai", "Kolkata", "Bangalore", "Mangalore"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, loc);
        spnLoc.setAdapter(adapter);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }//end of onCreate

    public void register(){
        initialize();
        if (!validate()){
            Toast.makeText(this, "Signup has failed", Toast.LENGTH_SHORT).show();
        }
        else {
            onSignupSuccess();
        }
    }

    public void initialize(){
        fname = etFname.getText().toString().trim();
        lname = etLastname.getText().toString().trim();
        email = etMail.getText().toString().trim();
        contact = etContact.getText().toString().trim();
        uname = etUname.getText().toString().trim();
        pass = etPass.getText().toString().trim();
        confirm_pass = etCpass.getText().toString().trim();
        age = etAge.getText().toString().trim();
    }

    public boolean validate(){
        boolean valid = true;
        if (fname.isEmpty()){
            etFname.setError("Please enter valid first name");
            valid=false;
        }
        if (lname.isEmpty()){
            etLastname.setError("Please enter valid last name");
            valid=false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etMail.setError("Invalid Email ID");
            valid = false;
        }
        if (contact.isEmpty() || contact.length()!=10){
            etContact.setError("Please enter a valid mobile number");
            valid=false;
        }
        if (age.isEmpty() || age.length()>2){
            etAge.setError("Please enter a valid age");
            valid = false;
        }
        if (uname.isEmpty()){
            etUname.setError("Please enter a username");
            valid=false;
        }
        if (pass.isEmpty()){
            etPass.setError("Please enter a password");
            valid=false;
        }
        if (confirm_pass.isEmpty()){
            etCpass.setError("Please enter a password");
            valid=false;
        }
        if (!(pass.equals(confirm_pass))){
            etCpass.setError("please enter the same passwords at both places");
            valid=false;
        }
        return valid;
    }

    public void onSignupSuccess(){

        Boolean chkemail = db.chkemail(email);
     if (chkemail == true) {
       Boolean chkuname = db.chkuname(uname);
       if (chkuname == true) {
           //to add rs.100
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setMessage("You will need to add Rs 100 into your account to carry out transactions.\nDo you wish to proceed?");
           builder.setCancelable(false);

           builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.cancel();
                   Toast.makeText(Register.this, "You need to have minimum Rs. 100 in your wallet", Toast.LENGTH_SHORT).show();
               }
           });

           builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   Toast.makeText(Register.this, "Rs. 100 has been added into your wallet", Toast.LENGTH_SHORT).show();

                   int p = spnLoc.getSelectedItemPosition();
                   location = loc[p];

                   int newage = Integer.parseInt(age);
                   int amount = 100;
                   Boolean insert = db.insert(fname, lname, email, contact, location, newage, uname, pass, amount);
                   if (insert == true) {
                       Toast.makeText(getApplicationContext(), "Account Created, Login now", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(Register.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                       finish();
                   }
               }
           });

           AlertDialog alert = builder.create();
           alert.setTitle("Confirm!");
           alert.show();
       }else {
           Toast.makeText(this, "this username already exixts", Toast.LENGTH_SHORT).show();
       }
     }else {
         Toast.makeText(this, "this email already exists", Toast.LENGTH_SHORT).show();
     }

    }
}
