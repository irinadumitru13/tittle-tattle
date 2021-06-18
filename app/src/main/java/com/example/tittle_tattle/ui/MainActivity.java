package com.example.tittle_tattle.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.ui.homeScreen.HomeActivity;
import com.facebook.AccessToken;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private final Integer DELAY = 2000;

    /**
     * These permissions are required before connecting to Nearby Connections. Only {@link
     * Manifest.permission#ACCESS_COARSE_LOCATION} is considered dangerous, so the others should be
     * granted just by having them in our AndroidManfiest.xml
     */
    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
//                    Manifest.permission.BLUETOOTH,
//                    Manifest.permission.BLUETOOTH_ADMIN,
//                    Manifest.permission.ACCESS_WIFI_STATE,
//                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    /**
     * An optional hook to pool any permissions the app needs with the permissions ConnectionsActivity
     * will request.
     *
     * @return All permissions required for the app to properly function.
     */
    protected String[] getRequiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }

//    private ActivityResultLauncher<String> requestPermissionLauncher =
//        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//            if (isGranted) {
//                // Permission is granted. Continue the action or workflow in your
//                // app.
//                Log.i("[PERMISSION]", "Granted");
//                init();
//            } else {
//                // Explain to the user that the feature is unavailable because the
//                // features requires a permission that the user has denied. At the
//                // same time, respect the user's decision. Don't link to system
//                // settings in an effort to convince the user to change their
//                // decision.
//                Log.i("[PERMISSION]", "Denied");
//                init();
//            }});

    // TODO to revise this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        if (!hasPermissions(this, getRequiredPermissions())) {
            if (!hasPermissions(this, getRequiredPermissions())) {
                if (Build.VERSION.SDK_INT < 23) {
                    ActivityCompat.requestPermissions(
                            this, getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
                } else {
                    requestPermissions(getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
                }
            } else {
                init();
            }
        } else {
            init();
        }
    }

//    /** Called when our Activity has been made visible to the user. */
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!hasPermissions(this, getRequiredPermissions())) {
//            if (!hasPermissions(this, getRequiredPermissions())) {
//                if (Build.VERSION.SDK_INT < 23) {
//                    ActivityCompat.requestPermissions(
//                            this, getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
//                } else {
//                    requestPermissions(getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
//                }
//            }
//        }
//    }

    private void init() {
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

//    private void requestPermission() {
//        //TODO add whole permissions :)
//        if (ContextCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
//                PackageManager.PERMISSION_GRANTED) {
//            // You can use the API that requires the permission.
//            Log.i("[PERMISSION]", "Already granted.");
//            init();
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                // In an educational UI, explain to the user why your app requires this
//                // permission for a specific feature to behave as expected. In this UI,
//                // include a "cancel" or "no thanks" button that allows the user to
//                // continue using your app without granting the permission.
//                //            showInContextUI(...);
//            } else {
//                requestPermissionLauncher.launch(
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//        } else {
//            // You can directly ask for the permission.
//            // The registered ActivityResultCallback gets the result of this request.
//            requestPermissionLauncher.launch(
//                    Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//    }

    /**
     * Returns {@code true} if the app was granted all the permissions. Otherwise, returns {@code
     * false}.
     */
    public static boolean hasPermissions(Context context, @NotNull String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /** Called when the user has accepted (or denied) our permission request. */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_REQUIRED_PERMISSIONS) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Missing permissions.", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
            recreate();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}