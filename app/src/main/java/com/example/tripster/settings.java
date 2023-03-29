package com.example.tripster;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class settings extends AppCompatActivity {

    TextView settings_title, settings_username;
    Button settings_changepfp, settings_login, settings_deleteacc;
    ImageView settings_profileimg;
    SQLiteHelper sqLiteHelper;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sqLiteHelper = new SQLiteHelper(this);

        settings_username = (TextView) findViewById(R.id.settings_username);
        settings_profileimg = (ImageView) findViewById(R.id.settings_profileimg);

        settings_changepfp = findViewById(R.id.settings_changepfp);
        settings_login = findViewById(R.id.settings_login);
        settings_deleteacc = findViewById(R.id.settings_deleteacc);

        boolean uservalue = SharedPref.getUserMode(this);
        if(uservalue) {
            String useremail = SharedPref.getUserEmail(this);
            Cursor cursor = sqLiteHelper.getData("SELECT * FROM USER WHERE useremail='"+useremail+"'");
            cursor.moveToNext();
            settings_username.setText(cursor.getString(cursor.getColumnIndex("username")));

            byte[] placeImage = cursor.getBlob(cursor.getColumnIndex("userimage"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(placeImage, 0, placeImage.length);
            settings_profileimg.setImageBitmap(bitmap);
        }
        else {
            settings_username.setText("Kindly login!");
            settings_login.setText("Login");
            settings_changepfp.setVisibility(GONE);
            settings_deleteacc.setVisibility(GONE);
        }

        settings_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(settings.this)
                        .setTitle("Logout")
//                        .setIcon(R.drawable.password)
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(settings.this, "User Logged Out", Toast.LENGTH_SHORT).show();
                                SharedPref.setUserMode(settings.this,false);
                                SharedPref.setUserEmail(settings.this,"");
                                Intent intent = new Intent(settings.this, Login.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        settings_changepfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        settings_deleteacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(settings.this)
                        .setTitle("Delete Account")
//                        .setIcon(R.drawable.delete)
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(settings.this, "Account Deleted!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.settings);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.favourites:
                        startActivity(new Intent(getApplicationContext(),Favourites.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),PlaceList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        return true;
                }
                return false;
            }
        });
    }
}