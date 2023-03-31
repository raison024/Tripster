package com.example.tripster;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    Context context;
    TextView add_title;
    TextInputEditText edittxtname, edittxtdesc, edittxtloc, edittxtmap, edittxtnearby;
    Button btnChoose, btnAdd;
    Spinner spinner1;
    RatingBar ratingBar1;
    ImageButton btnback;
    ImageView imageView;

    final int REQUEST_CODE_GALLERY = 999;

    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        Spinner dropdown = findViewById(R.id.spinner1);
            //create a list of items for the spinner.
        String[] items = new String[]{"Andhra Pradesh",
                "Arunachal Pradesh",
                "Assam",
                "Bihar",
                "Chhattisgarh",
                "Goa",
                "Gujarat",
                "Haryana",
                "Himachal Pradesh",
                "Jammu and Kashmir",
                "Jharkhand",
                "Karnataka",
                "Kerala"};
            //create an adapter to describe how the items are displayed, adapters are used in several places in android.
            //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        sqLiteHelper = new SQLiteHelper(this);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sqLiteHelper.insertData(
                            edittxtname.getText().toString().trim(),
                            edittxtdesc.getText().toString().trim(),
                            imageViewToByte(imageView),
                            edittxtloc.getText().toString().trim(),
                            spinner1.getSelectedItem().toString(),
                            edittxtmap.getText().toString().trim(),
                            ratingBar1.getRating(),
                            edittxtnearby.getText().toString().trim()
                    );
                    Toast.makeText(getApplicationContext(),"Added Successfully!", Toast.LENGTH_SHORT).show();
                    edittxtname.setText("");
                    edittxtdesc.setText("");
                    edittxtloc.setText("");
                    edittxtmap.setText("");
                    ratingBar1.setRating(0);
                    edittxtnearby.setText("");
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, PlaceList.class);
//                startActivity(intent);
                finish();
            }
        });

    }

    private byte[] imageViewToByte(ImageView image) {
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
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        edittxtname = (TextInputEditText) findViewById(R.id.edittxtname);
        edittxtdesc = (TextInputEditText) findViewById(R.id.edittxtdesc);
        edittxtloc = (TextInputEditText) findViewById(R.id.edittxtloc);
        edittxtmap = (TextInputEditText) findViewById(R.id.edittxtmap);
        edittxtnearby = (TextInputEditText) findViewById(R.id.edittxtnearby);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        imageView = (ImageView) findViewById(R.id.imageView);

        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnback = (ImageButton) findViewById(R.id.btnBack);
    }
}