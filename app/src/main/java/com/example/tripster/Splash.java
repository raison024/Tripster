package com.example.tripster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sh = getSharedPreferences("OnboardMode", MODE_PRIVATE);
                String onboardvalue = sh.getString("onboardvalue", "");

                SharedPreferences shuser = getSharedPreferences("Userpreference", MODE_PRIVATE);
                String usermode = shuser.getString("usermode", "");

                if(onboardvalue.equals("false")) {
                    if(usermode.equals("true")) {
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
        }, 3000);
    }
}