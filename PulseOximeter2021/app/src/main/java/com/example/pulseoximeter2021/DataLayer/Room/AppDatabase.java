package com.example.pulseoximeter2021.DataLayer.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pulseoximeter2021.DataLayer.Models.Room.RoomRecord;
import com.example.pulseoximeter2021.DataLayer.Models.Room.RoomUser;


@Database(entities = {RoomUser.class, RoomRecord.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "roomDb";
    private static volatile AppDatabase instance;

    public abstract UserDao userDao();
    public abstract RecordDao recordDao();

    public AppDatabase() {
    }

    static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .build();
    }
}
