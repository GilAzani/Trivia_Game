package com.gilazani.triviagame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<LeaderboardItem> leaderboardItems;

    public LeaderboardAdapter(List<LeaderboardItem> leaderboardItems) {
        this.leaderboardItems = leaderboardItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardItem item = leaderboardItems.get(position);
        holder.playerName.setText(item.getPlayerName());
        holder.score.setText(String.valueOf(item.getScore()));
    }

    @Override
    public int getItemCount() {
        return leaderboardItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView playerName;
        public TextView score;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.textViewName);
            score = itemView.findViewById(R.id.textViewScore);
        }
    }
}
