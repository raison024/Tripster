package com.example.tripster;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class Search extends AppCompatActivity implements CustomAdapter.ClickListener{

    RecyclerView search_recycler;
    ArrayList<Place> placeList = new ArrayList<>();
    CustomAdapter customAdapter;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_recycler = findViewById(R.id.search_recycler);
        sqLiteHelper = new SQLiteHelper(this);

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM PLACE");
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

        customAdapter = new CustomAdapter(Search.this, placeList, this::onClick);
        search_recycler.setAdapter(customAdapter);
        search_recycler.setLayoutManager(new GridLayoutManager(Search.this, 2));

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return false;
            }
        });

        FloatingActionButton fabadd2 = (FloatingActionButton) findViewById(R.id.float_add2);
        fabadd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Search.this, MainActivity.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.search);

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
        Intent intent = new Intent(Search.this,PlaceDetails.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

}