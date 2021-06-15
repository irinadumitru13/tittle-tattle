package com.example.tittle_tattle.ui.homeScreen;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.loader.content.AsyncTaskLoader;

import com.example.tittle_tattle.algorithm.ISUser;
import com.example.tittle_tattle.data.AppDatabase;
import com.example.tittle_tattle.data.models.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SharedViewModel extends AndroidViewModel {
    private final AppDatabase database;
    private MutableLiveData<String> fullName;
    private final SavedStateHandle state;

    public SharedViewModel(Application application, SavedStateHandle stateHandle) {
        super(application);
        database = AppDatabase.getInstance(application.getApplicationContext());
        this.state = stateHandle;
        state.set("notifications", "This is notifications fragment");
        state.set("dashboard", "This is dashboard fragment");
    }

    public void setAccessToken(@NotNull AccessToken accessToken) {
        state.set("access_token", accessToken);
    }

    public AccessToken getAccessToken() {
        return state.get("access_token");
    }

    public LiveData<String> getFullNameLive() {
        if (!state.contains("full_name")) {
            new GetUserTask(getAccessToken()).execute();
        }

        return state.getLiveData("full_name");
    }

    public String getFullName() {
        return state.get("full_name");
    }

    public void setFullName(String fullName) {
        state.set("full_name", fullName);
    }

    public LiveData<ArrayList<String>> getFriends() {
        if (!state.contains("friends")) {
            AccessToken accessToken = getAccessToken();
            List<String> friendIds = new ArrayList<>();

            // get friends of authenticated user
            new GraphRequest(
                    accessToken,
                    "/" + accessToken.getUserId() + "/friends",
                    null,
                    HttpMethod.GET,
                    response -> {
                        JSONObject responseJson = response.getJSONObject();

                        if (responseJson != null && responseJson.has("data")) {
                            try {
                                JSONArray friends = (JSONArray) responseJson.get("data");
                                for (int i = 0; i < friends.length(); i++) {
                                    friendIds.add(((JSONObject)friends.get(i)).get("id").toString());
                                }

                                state.set("friends", friendIds);
                            } catch (Exception e) {
                                Log.e("[GRAPH API] exception", e.getMessage());
                            }
                        }
                    }).executeAsync();
        }

        return state.getLiveData("friends");
    }

    public LiveData<String> getNotificationText() {
        return state.getLiveData("notifications");
    }

    public LiveData<String> getDashboardText() {
        return state.getLiveData("dashboard");
    }

    private class GetUserTask extends AsyncTask<Void, Void, User> {
        private final AccessToken accessToken;

        private GetUserTask(AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        protected User doInBackground(Void... voids) {
            return database.findUserById(getAccessToken().getUserId());
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                setFullName(user.getFull_name());
            } else {
                new GraphRequest(
                        accessToken,
                        "/" + accessToken.getUserId() + "/",
                        null,
                        HttpMethod.GET,
                        response -> {
                            JSONObject responseJson = response.getJSONObject();
                            if (responseJson != null && responseJson.has("name")) {
                                try {
                                    setFullName((String) responseJson.get("name"));
                                    AsyncTask.execute(() -> {
                                        try {
                                            database.insertUser(
                                                    new User(accessToken.getUserId(),
                                                            responseJson.get("name").toString()));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.e("[GRAPH API] exception", e.getMessage());
                                }
                            }
                        }
                ).executeAsync();
            }

            ISUser.getUser().setFullName(state.get("full_name"));
            ISUser.getUser().setId(accessToken.getUserId());
        }
    }
}
