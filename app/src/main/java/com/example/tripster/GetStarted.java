package com.example.tripster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GetStarted extends AppCompatActivity {

    Button btn_getstarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        btn_getstarted = (Button) findViewById(R.id.btn_getstarted);
        btn_getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("OnboardMode", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("onboardvalue","false");
                myEdit.apply();

                Intent i = new Intent(GetStarted.this, Login.class);
                startActivity(i);
            }
        });
    }
}