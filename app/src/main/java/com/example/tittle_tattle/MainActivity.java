package com.example.tittle_tattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.example.tittle_tattle.ui.login.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final Integer DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String serialized_token = sharedPreferences.getString("access_token", null);

        new Handler().postDelayed(() -> {
            Intent intent;

            if (serialized_token == null
                    || new Gson().fromJson(serialized_token, AccessToken.class).isExpired()) {
                intent = new Intent(getBaseContext(), LoginActivity.class);
            } else {
                intent = new Intent(getBaseContext(), Home.class);
                intent.putExtra("access_token", serialized_token);
            }

            startActivity(intent);
            finish();
        }, DELAY);
    }
}