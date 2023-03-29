package com.example.tripster;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class PlaceList extends AppCompatActivity implements CustomAdapter.ClickListener {

    RecyclerView toppicks_recycler, newadded_recycler   ;
    ArrayList<Place> placeList = new ArrayList<>();
    ArrayList<Place> placeList2 = new ArrayList<>();
    CustomAdapter customAdapter;
    SQLiteHelper sqLiteHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_list_activity);

//        gridView = (GridView) findViewById(R.id.gridView);
//        list = new ArrayList<>();
//        sqLiteHelper = new SQLiteHelper(this);
//        Cursor cursor = sqLiteHelper.getData("SELECT * FROM PLACE");
//        list.clear();
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            String name = cursor.getString(1);
//            String desc = cursor.getString(4);
//            byte[] image = cursor.getBlob(3);
//            float rating = cursor.getFloat(7);
//            int favourite = cursor.getInt(8);
//
//            list.add(new Place(id, name, desc, image, rating, favourite));
//        }
//        adapter = new PlaceListAdapter(this, R.layout.place_items, list, this::onClick);
//        gridView.setAdapter(adapter);

        //TopRated_Recycler
        toppicks_recycler = findViewById(R.id.toppicks_recycler);
        sqLiteHelper = new SQLiteHelper(this);
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM PLACE ORDER BY rating DESC LIMIT 4");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String desc = cursor.getString(4);
            byte[] image = cursor.getBlob(3);
            float rating = cursor.getFloat(7);
            int favourite = cursor.getInt(8);

            placeList.add(new Place(id, name, desc, image, rating, favourite));
        }
        customAdapter = new CustomAdapter(PlaceList.this, placeList, this::onClick);
        toppicks_recycler.setAdapter(customAdapter);
        toppicks_recycler.setLayoutManager(new LinearLayoutManager(PlaceList.this, LinearLayoutManager.HORIZONTAL, false));

        //NewlyAdded Recycler
        newadded_recycler = findViewById(R.id.newadded_recycler);
        placeList2.clear();
        cursor = sqLiteHelper.getData("SELECT * FROM PLACE ORDER BY id DESC LIMIT 4");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String desc = cursor.getString(4);
            byte[] image = cursor.getBlob(3);
            float rating = cursor.getFloat(7);
            int favourite = cursor.getInt(8);

            placeList2.add(new Place(id, name, desc, image, rating, favourite));
        }
        customAdapter = new CustomAdapter(PlaceList.this, placeList2, this::onClick);
        newadded_recycler.setAdapter(customAdapter);
        newadded_recycler.setLayoutManager(new LinearLayoutManager(PlaceList.this, LinearLayoutManager.HORIZONTAL, false));

        FloatingActionButton fabadd = (FloatingActionButton) findViewById(R.id.float_add);
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlaceList.this, MainActivity.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

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
            }
        });

    }
    @Override
    public void onClick(int id) {
        Intent intent = new Intent(PlaceList.this,PlaceDetails.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

}
