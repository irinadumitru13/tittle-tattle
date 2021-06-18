package com.example.tittle_tattle.data.models;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Set;

public class SetToStringConverter {
    @TypeConverter
    public static String fromSet(Set<Integer> interests) {
        if (interests == null) {
            return null;
        }

        return new Gson().toJson(interests, new TypeToken<Set<Integer>>() {}.getType());
    }

    @TypeConverter
    public static Set<Integer> fromString(String interests) {
        if (interests == null) {
            return null;
        }

        return new Gson().fromJson(interests, new TypeToken<Set<Integer>>() {}.getType());
    }

}
