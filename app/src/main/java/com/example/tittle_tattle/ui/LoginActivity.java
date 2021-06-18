package com.example.tittle_tattle.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.ui.homeScreen.HomeActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private SharedPreferences sharedPreferences;

    private final List<String> permissions = new ArrayList<String>(){{
            add("email");
            add("user_friends");
            add("user_likes");
        }};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions(permissions);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken accessToken = loginResult.getAccessToken();
                String serialized_token = new Gson().toJson(accessToken);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("access_token", serialized_token);
                editor.putLong("user_id", Long.parseLong(accessToken.getUserId()));
                editor.apply();

                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                intent.putExtra("access_token", serialized_token);
                startActivity(intent);
            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("error_login", exception.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}