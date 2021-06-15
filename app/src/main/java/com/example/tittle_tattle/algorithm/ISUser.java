package com.example.tittle_tattle.algorithm;

import android.util.Log;
import android.util.SparseArray;

import com.example.tittle_tattle.data.AppDatabase;
import com.example.tittle_tattle.ui.homeScreen.fragments.topicsRecycler.models.Subcategory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;

public class ISUser {

    private static ISUser ISUser;

    private String id;

    private String fullName;

    private final SparseArray<String> mySubscriptions;

    private final SparseArray<HashSet<Integer>> categoryTopics;

    private final Set<String> socialNetwork;

    //TODO add more fields :)

    public static ISUser getUser() {
        if (ISUser == null) {
            ISUser = new ISUser();
        }

        return ISUser;
    }

    private ISUser() {
        this.mySubscriptions = new SparseArray<>();
        this.categoryTopics = new SparseArray<>();

        // add the friends that are already connected to the app in the social network
        this.socialNetwork = new HashSet<>();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashSet<Integer> getSubscriptions() {
        HashSet<Integer> subscriptions = new HashSet<>();
        int size = mySubscriptions.size();

        for (int i = 0; i < size; i++) {
            subscriptions.add(mySubscriptions.keyAt(i));
        }

        return subscriptions;
    }

    public boolean isSubscribed(int subcategoryId) {
        return mySubscriptions.indexOfKey(subcategoryId) > -1;
    }

    public void subscribe(@NotNull Subcategory subcategory) {
        Log.i("[SUBSCRIBE]", subcategory.getName());
        // no element of category already subscribed to
        if (categoryTopics.indexOfKey(subcategory.getCategoryId()) < 0) {
            categoryTopics.append(subcategory.getCategoryId(), new HashSet<Integer>(){{
                add(subcategory.getSubcategoryId());
            }});
        } else {
            HashSet<Integer> subcategories = categoryTopics.get(subcategory.getCategoryId());
            subcategories.add(subcategory.getSubcategoryId());
            categoryTopics.put(subcategory.getCategoryId(), subcategories);
        }

        mySubscriptions.put(subcategory.getSubcategoryId(), subcategory.getName());
    }

    public void unsubscribe(@NotNull Subcategory subcategory) {
        Log.i("[UNSUBSCRIBE]", subcategory.getName());
        HashSet<Integer> subcategories = categoryTopics.get(subcategory.getCategoryId());
        subcategories.remove(subcategory.getSubcategoryId());

        if (subcategories.isEmpty()) {
            categoryTopics.delete(subcategory.getCategoryId());
        }

        mySubscriptions.delete(subcategory.getSubcategoryId());
    }

    public Set<String> getSocialNetwork() {
        return this.socialNetwork;
    }

    public void setSocialNetwork(ArrayList<String> friends) {
        this.socialNetwork.addAll(friends);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
