package com.example.tittle_tattle.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.ui.homeScreen.HomeActivity;
import com.facebook.AccessToken;
import com.google.gson.Gson;

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
                intent = new Intent(getBaseContext(), HomeActivity.class);
                intent.putExtra("access_token", serialized_token);
            }

            startActivity(intent);
            finish();
        }, DELAY);
    }
}