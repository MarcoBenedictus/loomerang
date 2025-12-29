package com.example.loomerang;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FoundItemDao {
    @Insert
    void insertItem(FoundItem item);
    @androidx.room.Query("SELECT * FROM found_items ORDER BY uid DESC")
    java.util.List<FoundItem> getAllItems();

    @Query("DELETE FROM found_items WHERE uid = :id")
    void deleteById(int id);

    @Query("UPDATE found_items SET itemName = :name, description = :desc, location = :loc, imagePath = :img WHERE uid = :id")
    void updateItem(int id, String name, String desc, String loc, String img);
}

// Property of Marco - https://github.com/MarcoBenedictus