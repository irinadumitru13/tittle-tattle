package com.example.tittle_tattle.data.models;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class SetToStringConverter {
    @TypeConverter
    public static String fromSet(@NotNull Set<Integer> interests) {
        if (interests.size() == 0) {
            return "";
        }

        return new Gson().toJson(interests, new TypeToken<Set<Integer>>() {}.getType());
    }

    @TypeConverter
    public static Set<Integer> fromString(@NotNull String interests) {
        if (interests.equals("")) {
            return new HashSet<>();
        }

        return new Gson().fromJson(interests, new TypeToken<Set<Integer>>() {}.getType());
    }

}
