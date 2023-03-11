package com.example.tripster;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(@Nullable Context context) {
        super(context, "PlaceDB.sqlite",null,1);
    }

    public void insertData(String name, String description, byte[] image, String location, String state, String mapslink, Float rating) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO PLACE VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, NULL)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,name);
        statement.bindString(2,description);
        statement.bindBlob(3,image);
        statement.bindString(4,location);
        statement.bindString(5,state);
        statement.bindString(6,mapslink);
        statement.bindDouble(7,rating);

        statement.executeInsert();
    }

    public void updateData(String name, String description, byte[] image, String location, String state, String mapslink, Float rating, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE PLACE SET name=?, description=?, image=?, location=?, state=?, mapslink=?, rating=? WHERE id=?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1,name);
        statement.bindString(2,description);
        statement.bindBlob(3,image);
        statement.bindString(4,location);
        statement.bindString(5,state);
        statement.bindString(6,mapslink);
        statement.bindDouble(7,rating);
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM PLACE WHERE ID = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public void addFavourite(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE PLACE SET favourite='1' WHERE id=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public void removeFavourite(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE PLACE SET favourite='0' WHERE id=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void newUser(String username, String useremail, String userpassword, byte[] userimage) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO USER VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,username);
        statement.bindString(2,useremail);
        statement.bindString(3,userpassword);
        statement.bindBlob(4,userimage);

        statement.executeInsert();
    }

    public void deleteUser(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM USER WHERE Id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Boolean checkuser(String useremail, String userpassword) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM USER WHERE useremail=? AND userpassword=?", new  String[] {useremail,userpassword});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE PLACE(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, description VARCHAR, image BLOB, location VARCHAR, state VARCHAR, mapslink VARCHAR, rating FLOAT, favourite INTEGER DEFAULT 0)";
        String query2 = "CREATE TABLE USER(Id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, useremail VARCHAR, userpassword VARCHAR, userimage BLOB)";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PLACE" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USER" );
        // Create tables again
        onCreate(sqLiteDatabase);
    }
}
