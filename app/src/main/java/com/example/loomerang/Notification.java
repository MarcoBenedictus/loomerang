package com.example.loomerang;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String recipientUsername;
    public String senderUsername;
    public String message;
    public String itemName;

    public Notification(String recipientUsername, String senderUsername, String message, String itemName) {
        this.recipientUsername = recipientUsername;
        this.senderUsername = senderUsername;
        this.message = message;
        this.itemName = itemName;
    }
}

// Property of Marco - https://github.com/MarcoBenedictus