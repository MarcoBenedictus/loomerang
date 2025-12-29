package com.example.loomerang;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public String nim;
    public String email;
    public String password;
    public int itemsFoundCount;

    // Constructor
    public User(String username, String nim, String email, String password) {
        this.username = username;
        this.nim = nim;
        this.email = email;
        this.password = password;
        this.itemsFoundCount = 0;
    }
}

// Property of Marco - https://github.com/MarcoBenedictus
