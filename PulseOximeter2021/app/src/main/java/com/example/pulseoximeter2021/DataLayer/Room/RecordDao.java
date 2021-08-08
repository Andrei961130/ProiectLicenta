package com.example.pulseoximeter2021.DataLayer.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.pulseoximeter2021.DataLayer.Models.Room.RoomRecord;

import java.util.List;

@Dao
public interface RecordDao {
    @Query("SELECT * FROM RoomRecord")
    List<RoomRecord> getAll();

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<Record> loadAllByIds(int[] userIds);
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    Record findByName(String first, String last);

    @Insert
    void insertAll(RoomRecord... record);

    @Delete
    void delete(RoomRecord record);
}
