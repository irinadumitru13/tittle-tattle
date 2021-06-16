package com.example.tittle_tattle.ui.homeScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.algorithm.DisseminationService;
import com.example.tittle_tattle.databinding.ActivityHomeBinding;
import com.example.tittle_tattle.ui.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        decorView.setSystemUiVisibility(uiOptions);

        // start service for dissemination
//        Intent intentService = new Intent(this, DisseminationService.class);
//        startService(intentService);

        Intent intent = getIntent();
        String serializedAccessToken = intent.getStringExtra("access_token");
        AccessToken accessToken = new Gson().fromJson(serializedAccessToken, AccessToken.class);

        SharedViewModel sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        sharedViewModel.setAccessToken(accessToken);
        sharedViewModel.updateSubscriptions();

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = ((NavHostFragment) Objects.requireNonNull(
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_home)))
                    .getNavController();
        NavigationUI.setupWithNavController(navView, navController);

        // logout button
        Button log_out_button = findViewById(R.id.btn_log_out);
        log_out_button.setPaintFlags(log_out_button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        log_out_button.setOnClickListener(view -> {
            // so that the login button won't show logout when user already logged out
            LoginManager.getInstance().logOut();

            // delete the access token from shared preferences
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("access_token", null);
            editor.apply();

            // redirect to login page
            Intent intentLogIn = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intentLogIn);
        });
        //
//        // get pages of authenticated user
//        new GraphRequest(
//                accessToken,
//                "/" + accessToken.getUserId() + "/likes",
//                null,
//                HttpMethod.GET,
//                response -> Log.i("[GRAPH API] likes", response.getRawResponse())
//        ).executeAsync();
    }



}