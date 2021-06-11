package com.example.tittle_tattle.ui.homeScreen;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final SavedStateHandle state;

    public SharedViewModel(SavedStateHandle stateHandle) {
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

//    public void setFullName(@NotNull String fullName) {
//        state.set("full_name", fullName);
//    }

    public LiveData<String> getFullName() {
        if (!state.contains("full_name")) {
            AccessToken accessToken = getAccessToken();

            new GraphRequest(
                accessToken,
                "/" + accessToken.getUserId() +"/",
                null,
                HttpMethod.GET,
                response -> {
                    JSONObject responseJson = response.getJSONObject();
                    if ( responseJson != null && responseJson.has("name") ) {
                        try {
                            state.set("full_name", responseJson.get("name").toString());
                        } catch (Exception e) {
                            Log.e("[GRAPH API] exception", e.getMessage());
                        }
                    }
                }
            ).executeAsync();
        }

        return state.getLiveData("full_name");
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
}
