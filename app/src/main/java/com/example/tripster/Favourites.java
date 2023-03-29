package com.example.tripster;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Favourites extends AppCompatActivity implements CustomAdapter.ClickListener{

    RecyclerView fav_recycler;
    ArrayList<Place> placeList = new ArrayList<>();
    CustomAdapter customAdapter;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        fav_recycler = findViewById(R.id.fav_recycler);
        sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM PLACE WHERE favourite='1'");
        placeList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String desc = cursor.getString(4);
            byte[] image = cursor.getBlob(3);
            float rating = cursor.getFloat(7);
            int favourite = cursor.getInt(8);

            placeList.add(new Place(id, name, desc, image, rating, favourite));
        }

        customAdapter = new CustomAdapter(this, placeList, this);
        fav_recycler.setAdapter(customAdapter);
        fav_recycler.setLayoutManager(new GridLayoutManager(this, 2));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.favourites);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId())
            {
                case R.id.favourites:
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
                    startActivity(new Intent(getApplicationContext(),settings.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(int id) {
        Intent intent = new Intent(Favourites.this,PlaceDetails.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}