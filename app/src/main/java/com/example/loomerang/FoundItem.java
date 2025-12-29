package com.example.loomerang;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "found_items")
public class FoundItem {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String itemName;
    public String description;
    public String location;
    public String finderUsername;
    public String contactPhone;
    public String imagePath;
    public String type;

    public FoundItem(String itemName, String description, String location, String finderUsername, String contactPhone, String imagePath, String type) {
        this.itemName = itemName;
        this.description = description;
        this.location = location;
        this.finderUsername = finderUsername;
        this.contactPhone = contactPhone;
        this.imagePath = imagePath;
        this.type = type;
    }
}

// Property of Marco - https://github.com/MarcoBenedictus