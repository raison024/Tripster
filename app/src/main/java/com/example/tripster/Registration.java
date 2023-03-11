package com.example.tripster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Registration extends AppCompatActivity {

    TextInputEditText reg_name, reg_email, reg_password;
    Button regpage_skip,regpage_choose,regpage_reg,regpage_login;
    ImageView reg_imageview;

    final int REQUEST_CODE_GALLERY = 999;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
        sqLiteHelper = new SQLiteHelper(this);

        regpage_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, settings.class);
                startActivity(intent);
            }
        });

        regpage_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        Registration.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        regpage_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(reg_name.getText().toString()) || TextUtils.isEmpty(reg_email.getText().toString()) || TextUtils.isEmpty(reg_password.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Fill all the details!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        sqLiteHelper.newUser(
                                reg_name.getText().toString().trim(),
                                reg_email.getText().toString().trim(),
                                reg_password.getText().toString().trim(),
                                reg_imageViewToByte(reg_imageview)
                        );
                        Toast.makeText(getApplicationContext(),"Registration Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registration.this, Login.class);
                        startActivity(intent);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        regpage_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private byte[] reg_imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file! ", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                reg_imageview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        reg_name = (TextInputEditText) findViewById(R.id.reg_name);
        reg_email = (TextInputEditText) findViewById(R.id.reg_email);
        reg_password = (TextInputEditText) findViewById(R.id.reg_password);
        reg_imageview = (ImageView) findViewById(R.id.reg_imageview);

        regpage_skip = findViewById(R.id.regpage_skip);
        regpage_choose = findViewById(R.id.regpage_choose);
        regpage_reg = findViewById(R.id.regpage_reg);
        regpage_login = findViewById(R.id.regpage_login);
    }
}