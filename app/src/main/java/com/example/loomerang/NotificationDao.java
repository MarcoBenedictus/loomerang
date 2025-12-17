package com.example.loomerang;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    void insertNotification(Notification notification);

    @Query("SELECT * FROM notifications WHERE recipientUsername = :username ORDER BY id DESC")
    List<Notification> getMyNotifications(String username);

    @Query("SELECT COUNT(*) FROM notifications WHERE senderUsername = :sender AND recipientUsername = :recipient AND itemName = :item")
    int checkNotificationExists(String sender, String recipient, String item);
}