package com.example.loomerang;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LoomoireAdapter extends RecyclerView.Adapter<LoomoireAdapter.ViewHolder> {

    List<FoundItem> itemList;
    Context context;
    AppDatabase db;
    String currentUser;

    public LoomoireAdapter(Context context, List<FoundItem> itemList, AppDatabase db, String currentUser) {
        this.context = context;
        this.itemList = itemList;
        this.db = db;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loomoire, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoundItem item = itemList.get(position);

        holder.tvItemName.setText(item.itemName.toUpperCase());

        if (item.imagePath != null && !item.imagePath.isEmpty()) {
            try {
                holder.ivImage.setImageURI(Uri.parse(item.imagePath));
            } catch (Exception e) {
                holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        if ("LOST".equals(item.type)) {
            holder.tvStatus.setText("DICARI");
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#FF5252"));
            holder.container.setBackgroundResource(R.drawable.bg_card_red);
        } else {
            holder.tvStatus.setText("DITEMUKAN");
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#69F0AE"));
            holder.container.setBackgroundResource(R.drawable.bg_card_green);
        }

        holder.btnNotify.setOnClickListener(v -> {
            if (item.finderUsername.equals(currentUser)) {
                Toast.makeText(context, "This is your own post", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                int count = db.notificationDao().checkNotificationExists(currentUser, item.finderUsername, item.itemName);

                if (count > 0) {
                    ((android.app.Activity) context).runOnUiThread(() -> {
                        Toast.makeText(context, "You have already notified this user", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                String msg = "LOST".equals(item.type) ?
                        currentUser + " claims they found: " + item.itemName :
                        currentUser + " claims they own: " + item.itemName;

                Notification notif = new Notification(item.finderUsername, currentUser, msg, item.itemName);
                db.notificationDao().insertNotification(notif);

                ((android.app.Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "Notification sent", Toast.LENGTH_SHORT).show();
                });
            }).start();
        });
        holder.container.setOnClickListener(v -> {
            Intent intent = new Intent(context, detailactivity.class);
            intent.putExtra("ITEM_NAME", item.itemName);
            intent.putExtra("ITEM_DESC", item.description);
            intent.putExtra("ITEM_LOC", item.location);
            intent.putExtra("ITEM_IMAGE", item.imagePath);
            intent.putExtra("ITEM_TYPE", item.type);
            intent.putExtra("ITEM_REPORTER", item.finderUsername);
            intent.putExtra("CURRENT_USER_SENDING", currentUser);
            intent.putExtra("ITEM_IMAGE", item.imagePath);
            intent.putExtra("ITEM_TYPE", item.type);
            intent.putExtra("ITEM_REPORTER", item.finderUsername);
            intent.putExtra("ITEM_ID", item.uid);
            intent.putExtra("CURRENT_USER_SENDING", currentUser);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvItemName;
        ImageView ivImage;
        ImageButton btnNotify;
        ConstraintLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            ivImage = itemView.findViewById(R.id.ivItemImage);
            btnNotify = itemView.findViewById(R.id.btnNotify);
            container = itemView.findViewById(R.id.cardContainer);
        }
    }
}

// Property of Marco - https://github.com/MarcoBenedictus