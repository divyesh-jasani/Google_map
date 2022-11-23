package com.project.room;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.project.model.PlacesModel;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Insert
    void insert(PlacesModel model);

    @Update
//    @Query("UPDATE place_tbl SET order_price=:price WHERE id = :model")
    void update(PlacesModel model);

    @Delete
    void delete(PlacesModel model);

    @Query("DELETE FROM place_tbl")
    void deleteAllCourses();

    @Query("SELECT * FROM place_tbl ORDER BY placeName ASC")
    LiveData<List<PlacesModel>> getAllCourses();

    @Query("SELECT * FROM place_tbl WHERE placeName = :name")
    LiveData<List<PlacesModel>> getSingleCourses(String name);
}
