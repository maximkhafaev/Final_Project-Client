package com.maximkhafaev.pilotquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAuthorized()) {
            Intent intent = new Intent(getApplicationContext(), TestsList.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private boolean isAuthorized() {
        SharedPreferences preferences = getSharedPreferences("com.maximkhafaev.pilotquiz_SHPR", Context.MODE_PRIVATE);
        String token = preferences.getString("USER_TOKEN", "");
        if (token == "") {
            return false;
        } else {
            return true;
        }
    }
}
