package com.example.tittle_tattle.ui.homeScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.algorithm.ISUser;
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

        Intent intent = getIntent();
        String serializedAccessToken = intent.getStringExtra("access_token");
        AccessToken accessToken = new Gson().fromJson(serializedAccessToken, AccessToken.class);

        ISUser.getUser().setId(Long.parseLong(accessToken.getUserId()));

        SharedViewModel sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        sharedViewModel.setAccessToken(accessToken);
        sharedViewModel.updateSubscriptions();
        sharedViewModel.getFriends();

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
    }

}