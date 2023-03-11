package com.example.tripster;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlaceDetails extends AppCompatActivity {

    TextView edittxtname2, edittxtdesc2, edittxtloc2, edittxtstate2, edittxtrat2;
    Button mapsbutton;
    ImageButton btnBack2, btnDelete2, details_favbutton;
    ImageView imageView2;
    SQLiteHelper sqLiteHelper;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        edittxtname2 = findViewById(R.id.edittxtname2);
        edittxtdesc2 = findViewById(R.id.edittxtdesc2);
        edittxtloc2 = findViewById(R.id.edittxtloc2);
        edittxtstate2 = findViewById(R.id.edittxtstate2);
        edittxtrat2 = findViewById(R.id.edittxtrat2);
        imageView2 = findViewById(R.id.imageView2);
        btnBack2 = findViewById(R.id.btnBack2);
        btnDelete2 = findViewById(R.id.btnDelete2);
        mapsbutton = findViewById(R.id.mapsbutton);
        details_favbutton = findViewById(R.id.details_favbutton);

        sqLiteHelper = new SQLiteHelper(this);
        Intent intent = getIntent();
        int id  = intent.getIntExtra("id",0);
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM PLACE WHERE Id="+id);

        cursor.moveToNext();
        edittxtname2.setText(cursor.getString(cursor.getColumnIndex("name")));
        edittxtdesc2.setText(cursor.getString(cursor.getColumnIndex("description")));
        edittxtloc2.setText(cursor.getString(cursor.getColumnIndex("location")));
        edittxtstate2.setText(cursor.getString(cursor.getColumnIndex("state")));
        edittxtrat2.setText(cursor.getString(cursor.getColumnIndex("rating")));
        mapsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = cursor.getString(cursor.getColumnIndex("mapslink"));
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        int favvalue = cursor.getInt(cursor.getColumnIndex("favourite"));
        if(favvalue==1) {
            details_favbutton.setBackgroundResource(R.drawable.favorite_fill);
        }
        else {
            details_favbutton.setBackgroundResource(R.drawable.favorite);
        }

        byte[] placeImage = cursor.getBlob(3);
        Bitmap bitmap = BitmapFactory.decodeByteArray(placeImage, 0, placeImage.length);
        imageView2.setImageBitmap(bitmap);

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceDetails.this, PlaceList.class);
                startActivity(intent);
            }
        });

        btnDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlaceDetails.this);
                builder.setTitle("Choose an action");
                builder.setItems(new CharSequence[]
                                {"Update", "Delete"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //update
                                        Toast.makeText(getApplicationContext(), "clicked 1", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        //delete
                                        new AlertDialog.Builder(PlaceDetails.this)
                                                .setTitle("Delete Place")
                                                .setMessage("Are you sure?")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = getIntent();
                                                        int id  = intent.getIntExtra("id",0);
                                                        sqLiteHelper.deleteData(id);

                                                        Intent gointent = new Intent(PlaceDetails.this, PlaceList.class);
                                                        startActivity(gointent);
                                                        Toast.makeText(getApplicationContext(), "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, null)
                                                .show();
                                        break;
                                }
                            }
                        });
                builder.create().show();


            }
        });


        details_favbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int favvalue = cursor.getInt(cursor.getColumnIndex("favourite"));
                if(favvalue==1) {
                    Intent intent = getIntent();
                    int id  = intent.getIntExtra("id",0);
                    sqLiteHelper.removeFavourite(id);
                    Toast.makeText(getApplicationContext(), "Removed from Favourites", Toast.LENGTH_SHORT).show();
                    view.setBackgroundResource(R.drawable.favorite);
                }
                else {
                    Intent intent = getIntent();
                    int id  = intent.getIntExtra("id",0);
                    sqLiteHelper.addFavourite(id);
                    Toast.makeText(getApplicationContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                    view.setBackgroundResource(R.drawable.favorite_fill);
                }
            }
        });

    }
}