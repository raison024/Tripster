package com.example.tripster;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static final String SP = "Userpreference";
    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(SP,Context.MODE_PRIVATE);
    }

    public static boolean getUserMode(Context context){
        return getSharedPreferences(context).getBoolean("usermode",false);
    }

    public static void setUserMode(Context context,Boolean b){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("usermode",b);
        editor.apply();
    }
    public static String getUserEmail(Context context){
        return getSharedPreferences(context).getString("useremail","");
    }

    public static void setUserEmail(Context context,String email){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("useremail",email);
        editor.apply();
    }
}
