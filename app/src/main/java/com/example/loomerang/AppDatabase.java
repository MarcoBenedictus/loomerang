package com.example.loomerang;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.loomerang.User;
import com.example.loomerang.UserDao;

@Database(entities = {User.class, FoundItem.class, Notification.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract FoundItemDao foundItemDao();
    public abstract NotificationDao notificationDao();
}

// Property of Marco - https://github.com/MarcoBenedictus
