package com.example.recoleta;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDataSession {

    private static final String PREF_NAME = "MyAppPrefs";
    private SharedPreferences sharedPreferences;

    public  UserDataSession(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getJwtToken() {
        return sharedPreferences.getString("jwt_token", null);
    }

    public String getUserId() {
        return sharedPreferences.getString("user_id", null);
    }

    public String getFirstName() {
        return sharedPreferences.getString("firstName", null);
    }

    public String getLastName() {
        return sharedPreferences.getString("lastName", null);
    }

    public boolean isAdmin() {
        return sharedPreferences.getBoolean("is_admin", false);
    }

    public String getUserType() {
        return sharedPreferences.getString("user_type", "null"); // Retorna "null" se não houver valor
    }
}
