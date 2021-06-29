package com.example.tittle_tattle.algorithm;

import android.util.SparseArray;

import com.example.tittle_tattle.data.models.Subscription;
import com.example.tittle_tattle.ui.homeScreen.fragments.topics.models.Subcategory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ISUser {

    private static ISUser ISUser;

    private Long id;

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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Subscription> getSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();
        int catSize = categoryTopics.size();

        for (int i = 0; i < catSize; i++) {
            for (Integer subcategory : categoryTopics.get(categoryTopics.keyAt(i))) {
                subscriptions.add(new Subscription(
                        subcategory,
                        mySubscriptions.get(subcategory),
                        categoryTopics.keyAt(i),
                        id));
            }
        }

        return subscriptions;
    }

    public HashSet<Integer> getTopics() {
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

        DisseminationService.addInterest(getId(), subcategory.getSubcategoryId());
    }

    public void unsubscribe(@NotNull Subcategory subcategory) {
        HashSet<Integer> subcategories = categoryTopics.get(subcategory.getCategoryId());
        subcategories.remove(subcategory.getSubcategoryId());

        if (subcategories.isEmpty()) {
            categoryTopics.delete(subcategory.getCategoryId());
        }

        mySubscriptions.delete(subcategory.getSubcategoryId());
        DisseminationService.deleteInterest(getId(), subcategory.getSubcategoryId());
    }

    public Set<String> getSocialNetwork() {
        return this.socialNetwork;
    }

    public void setSocialNetwork(ArrayList<String> friends) {
        this.socialNetwork.addAll(friends);
    }
}
