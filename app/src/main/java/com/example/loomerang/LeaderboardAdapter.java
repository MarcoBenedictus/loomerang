package com.example.loomerang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    List<User> userList;
    Context context;

    public LeaderboardAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.tvRank.setText("#" + (position + 1));

        holder.tvUsername.setText(user.username);

        holder.tvPoints.setText(user.itemsFoundCount + " PTS");

        // Optional: Make the Top 3 colors special!
        if (position == 0) {
            holder.tvRank.setTextColor(android.graphics.Color.parseColor("#FFD700")); // Gold
        } else if (position == 1) {
            holder.tvRank.setTextColor(android.graphics.Color.parseColor("#C0C0C0")); // Silver
        } else if (position == 2) {
            holder.tvRank.setTextColor(android.graphics.Color.parseColor("#CD7F32")); // Bronze
        } else {
            holder.tvRank.setTextColor(android.graphics.Color.parseColor("#1976D2")); // Blue
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvUsername, tvPoints;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPoints = itemView.findViewById(R.id.tvPoints);
        }
    }
}

// Property of Marco - https://github.com/MarcoBenedictus