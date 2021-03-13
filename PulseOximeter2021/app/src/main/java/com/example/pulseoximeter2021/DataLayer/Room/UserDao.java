package com.example.pulseoximeter2021.DataLayer.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.pulseoximeter2021.DataLayer.Models.Room.RoomUser;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM RoomUser")
    List<RoomUser> getAll();

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<Record> loadAllByIds(int[] userIds);
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    Record findByName(String first, String last);

    @Insert
    void insertAll(RoomUser... users);

    @Delete
    void delete(RoomUser user);
}
