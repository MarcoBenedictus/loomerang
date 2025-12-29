package com.example.loomerang;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    void registerUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUser(String username);

    @androidx.room.Query("UPDATE users SET itemsFoundCount = itemsFoundCount + 1 WHERE username = :username")
    void incrementPoints(String username);

    @androidx.room.Query("SELECT * FROM users ORDER BY itemsFoundCount DESC LIMIT 10")
    java.util.List<User> getTop10Users();

    @Query("UPDATE users SET itemsFoundCount = itemsFoundCount - 1 WHERE username = :username")
    void decrementPoints(String username);
}

// Property of Marco - https://github.com/MarcoBenedictus
