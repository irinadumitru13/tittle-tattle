package com.example.tittle_tattle.ui.homeScreen;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.tittle_tattle.algorithm.ISUser;
import com.example.tittle_tattle.data.AppDatabase;
import com.example.tittle_tattle.data.models.Subscription;
import com.example.tittle_tattle.data.models.User;
import com.example.tittle_tattle.ui.homeScreen.fragments.topicsRecycler.models.Subcategory;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SharedViewModel extends AndroidViewModel {
    private final AppDatabase database;
    private final SavedStateHandle state;
    private Single<List<Subscription>> subscriptions;

    public SharedViewModel(Application application, SavedStateHandle stateHandle) {
        super(application);
        database = AppDatabase.getInstance(application.getApplicationContext());
        this.state = stateHandle;
        state.set("notifications", "This is notifications fragment");
        state.set("dashboard", "This is dashboard fragment");
    }

    private AppDatabase getDatabase() {
        return database;
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

    @SuppressLint("StaticFieldLeak")
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

    public void updateSubscriptions() {
        new CompositeDisposable().add(database.subscriptionDAO().findAllByUserId(getAccessToken().getUserId())
                .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .subscribe(subscriptions -> {
            for (Subscription subscription : subscriptions) {
                ISUser.getUser().subscribe(
                        new Subcategory(
                                subscription.getName(),
                                subscription.getSubscription_id(),
                                subscription.getCategory_id()));
            }
        }, throwable -> Log.i("[SUBSCRIPTIONS]", "No subscriptions yet.")));
    }

    public Observable<List<Subscription>> getSubscriptions() {
        return database.subscriptionDAO().findAllByUserId(getAccessToken().getUserId()).toObservable();
    }
}
