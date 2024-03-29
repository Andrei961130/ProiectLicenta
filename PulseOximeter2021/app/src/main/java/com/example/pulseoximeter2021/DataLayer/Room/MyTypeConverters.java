package com.example.pulseoximeter2021.DataLayer.Room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MyTypeConverters {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<Integer> stringToListOfInt(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Integer>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String listOfIntToString(List<Integer> someObjects) {
        return gson.toJson(someObjects);
    }
}
