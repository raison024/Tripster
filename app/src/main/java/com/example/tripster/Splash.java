package com.example.tripster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Splash extends AppCompatActivity {
    Context context;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sqLiteHelper = new SQLiteHelper(this);

        File database = getApplicationContext().getDatabasePath(SQLiteHelper.DBName);
        if(false == database.exists()) {
//            sqLiteHelper.getReadableDatabase();
            //CopyDb
            if(copyDatabase(this)) {
//                Toast.makeText(this, "Copy db success", Toast.LENGTH_SHORT).show();
            }
            else {
//                Toast.makeText(this, "Copy db error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else {
//            Toast.makeText(this, "Already present db", Toast.LENGTH_SHORT).show();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sh = getSharedPreferences("OnboardMode", MODE_PRIVATE);
                String onboardvalue = sh.getString("onboardvalue", "");

//                SharedPreferences shuser = getSharedPreferences("Userpreference", MODE_PRIVATE);
//                String usermode = shuser.getString("usermode", "");

                boolean uservalue = SharedPref.getUserMode(Splash.this);
                if(onboardvalue.equals("false")) {
//                    if(usermode.equals("true")) {
//                        Intent i = new Intent(Splash.this, Search.class);
//                        startActivity(i);
//                    }
//                    else {
//                        Intent i = new Intent(Splash.this, Login.class);
//                        startActivity(i);
//                    }

                    if(uservalue) {
                        Intent i = new Intent(Splash.this, Search.class);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(Splash.this, Login.class);
                        startActivity(i);
                    }
                }
                else {
                    Intent i = new Intent(Splash.this, GetStarted.class);
                    startActivity(i);
                }


                finish();
            }
        }, 2000);
    }

    private boolean copyDatabase(Context context) {
        try {
            context = getApplicationContext();
            InputStream inputStream = context.getAssets().open(sqLiteHelper.DBName);
            String outFileName = sqLiteHelper.DBPath + sqLiteHelper.DBName;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            Log.w("SplashAcitvity","DBCopied");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}