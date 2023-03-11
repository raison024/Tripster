package com.example.tripster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    TextInputEditText login_email, login_password;
    Button loginpage_skip, loginpage_login, loginpage_reg;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email = (TextInputEditText) findViewById(R.id.login_email);
        login_password = (TextInputEditText) findViewById(R.id.login_password);

        loginpage_skip = findViewById(R.id.loginpage_skip);
        loginpage_login = findViewById(R.id.loginpage_login);
        loginpage_reg = findViewById(R.id.loginpage_reg);

        sqLiteHelper = new SQLiteHelper(this);

        loginpage_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Search.class);
                startActivity(intent);
            }
        });

        loginpage_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String email = login_email.getText().toString();
               String pass = login_password.getText().toString();

               if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
                   Toast.makeText(Login.this, "Fill all details!", Toast.LENGTH_SHORT).show();
               else {
                   Boolean checkuserpass = sqLiteHelper.checkuser(email,pass);
                   if(checkuserpass == true) {
                       //Updating Shared Preference
                       SharedPreferences sharedPreferences = getSharedPreferences("Userpreference", MODE_PRIVATE);
                       SharedPreferences.Editor myEdit = sharedPreferences.edit();
                       myEdit.putString("usermode","true");
                       myEdit.putString("useremail",email);
                       myEdit.apply();

                       Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(Login.this, Splash.class);
                       startActivity(intent);
                   }
                   else{
                       Toast.makeText(Login.this, "Invalid Details!", Toast.LENGTH_SHORT).show();
                   }
               }
            }
        });

        loginpage_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });
    }
}