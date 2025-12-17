package com.example.loomerang;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface FoundItemDao {
    @Insert
    void insertItem(FoundItem item);
    @androidx.room.Query("SELECT * FROM found_items ORDER BY uid DESC")
    java.util.List<FoundItem> getAllItems();
}