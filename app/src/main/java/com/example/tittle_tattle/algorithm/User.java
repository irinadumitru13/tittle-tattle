package com.example.tittle_tattle.algorithm;

import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class User {

    private static User user;

    private final String id;

    private SparseIntArray mySubscriptions;

    private Set<String> socialNetwork;

    //TODO add more fields :)

    //TODO modify so no parameter given... would be a pain in the ass...
    public static User getUser(String id) {
        if (user == null) {
            user = new User(id);
        }

        return user;
    }

    private User(String id) {
        // the FB id is used
        this.id = id;

        // at first, no topics that the user is interested in (key=subcategory, value=category)
        // TODO think about this more... NU - TOT ARRAY E MAI BINE... sau doua structuri din astea de date...
        this.mySubscriptions = new SparseIntArray();

        // add the friends that are already connected to the app in the social network
        this.socialNetwork = new HashSet<>();
    }

    public String getId() {
        return this.id;
    }

    public SparseIntArray getMySubscriptions() {
        return this.mySubscriptions;
    }

    // TODO nu asa! maine dimineata!!!
    public void addSubscription(int categoryId, int subcategoryId) {
        if (mySubscriptions.indexOfKey(subcategoryId) == -1) {
            if (mySubscriptions.keyAt(mySubscriptions.indexOfValue(categoryId)) == -1) {
                mySubscriptions.removeAt(mySubscriptions.indexOfValue(categoryId));
            }

            mySubscriptions.append(subcategoryId, categoryId);
        }
    }

    public Set<String> getSocialNetwork() {
        return this.socialNetwork;
    }

    public void setSocialNetwork(ArrayList<String> friends) {
        this.socialNetwork.addAll(friends);
    }
}
