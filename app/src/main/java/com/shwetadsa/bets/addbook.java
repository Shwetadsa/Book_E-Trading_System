package com.shwetadsa.bets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class addbook extends AppCompatActivity {

    EditText etAuthName, etBkname;
    Button btnChooseImg, btnAddBook;
    Spinner spnGenre;
    ImageView ivPhoto;

    DatabaseHelper db = new DatabaseHelper(this);

    private String gen;
    String[] genre;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        final SharedPreferences userdata = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        etAuthName = findViewById(R.id.etAuthName);
        etBkname = findViewById(R.id.etBkName);
        btnChooseImg = findViewById(R.id.btnChooseImg);
        btnAddBook = findViewById(R.id.btnAddBook);
        spnGenre = findViewById(R.id.spnGenre);
        ivPhoto = findViewById(R.id.ivPhoto);

        genre = new String[]{"Romance", "Fiction", "Sci-fi", "Action", "Thriller"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, genre);
        spnGenre.setAdapter(adapter);

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etBkname.getText().toString().length() == 0){
                    etBkname.setError("Enter Book Name");
                    return;
                }
                if (etAuthName.getText().toString().length() == 0){
                    etAuthName.setError("Enter Author Name");
                    return;
                }
                int p = spnGenre.getSelectedItemPosition();
                gen = genre[p];

                String uname = userdata.getString("Username", "null");
                try{
                    Boolean val = db.insertData(uname, etBkname.getText().toString().trim(),
                                   etAuthName.getText().toString().trim(),
                                   gen, imageViewToByte(ivPhoto));
                    if (val == true) {
                        Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
                        etBkname.setText("");
                        etAuthName.setText("");
                        ivPhoto.setImageResource(R.mipmap.ic_launcher);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 123);
            }
        });

    }

    public static byte[] imageViewToByte(ImageView ivPhoto) {
        Bitmap photo = ((BitmapDrawable)ivPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 123){
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            ivPhoto.setImageBitmap(photo);
        }
    }
}
