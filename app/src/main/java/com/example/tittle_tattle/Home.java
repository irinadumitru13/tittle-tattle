package com.example.tittle_tattle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tittle_tattle.ui.login.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tittle_tattle.databinding.ActivityHomeBinding;
import com.google.gson.Gson;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupWithNavController(navView, navController);


        Button log_out_button = findViewById(R.id.btn_log_out);
        log_out_button.setOnClickListener(view -> {
            LoginManager.getInstance().logOut();

            Intent intentLogIn = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intentLogIn);
        });

        Intent intent = getIntent();
        String serializedAccessToken = intent.getStringExtra("access_token");
        Log.i("access_token", serializedAccessToken);
        AccessToken accessToken = new Gson().fromJson(serializedAccessToken, AccessToken.class);

        // extract data regarding the authenticated user - name and id
        // id can be used as id in the app
        new GraphRequest(
                accessToken,
                "/" + accessToken.getUserId() +"/",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Log.i("[GRAPH API]", response.getRawResponse());
                    }
                }
        ).executeAsync();

        // get friends of authenticated user
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/" + accessToken.getUserId() + "/friends",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                        Log.i("[GRAPH API]", response.getRawResponse());
                    }
                });

        request.executeAsync();
    }

}