package com.pma.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.pma.model.Location;

import java.util.Date;
import java.util.List;

@Dao
public interface LocationDao {
    @Insert
    void insert(Location location);

    @Update
    void update(Location location);

    @Delete
    void delete(Location location);

    @Query("SELECT * FROM location")
    List<Location> getAll();

    @Query("SELECT * FROM location WHERE datetime(dateAndTime) BETWEEN  datetime(:from) AND datetime(:to)")
    List<Location> getLocationsInTimeRange(Date from, Date to);

    @Query("SELECT * FROM location WHERE isSynced != 1")
    List<Location> getNotSyncedLocations();
}
