package com.gilazani.triviagame;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gilazani.triviagame.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private List<LeaderboardItem> leaderboardItems = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        initViews();

        setBackBtnOnClickListener();

        // Load existing records from SharedPreferences
        loadRecords();

        handleUserScore();

        handleLeaderboard();
    }

    private void setBackBtnOnClickListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderboardActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleLeaderboard() {
        // Sort leaderboard items by score (descending order)
        Collections.sort(leaderboardItems, new Comparator<LeaderboardItem>() {
            @Override
            public int compare(LeaderboardItem o1, LeaderboardItem o2) {
                return Integer.compare(Integer.parseInt(o2.getScore()), Integer.parseInt(o1.getScore()));
            }
        });

        // Limit leaderboard to top 10 records
        if (leaderboardItems.size() > 10) {
            leaderboardItems = leaderboardItems.subList(0, 10);
        }
        setLeaderboardAdapter();
    }

    private void setLeaderboardAdapter() {
        adapter = new LeaderboardAdapter(leaderboardItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void handleUserScore() {
        // Get user's score from intent
        int userScore = getIntent().getIntExtra("FINAL_SCORE", 0);

        // Check if user's score is in top 10
        if (userScore > 0 && isScoreInTop10(userScore)) {
            // Prompt user to enter their name
            showNameInputDialog(userScore);
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_leaderboard);
        backButton = findViewById(R.id.btn_back_to_menu);
    }

    private void loadRecords() {
        sharedPreferences = getSharedPreferences("Leaderboard", MODE_PRIVATE);
        // Load existing records from SharedPreferences
        String recordsString = sharedPreferences.getString("records", "");
        if (!recordsString.isEmpty()) {
            leaderboardItems = fromJsonString(recordsString);
        }
    }

    private void saveRecords() {
        // Save leaderboard records to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("records", toJsonString(leaderboardItems));
        editor.apply();
    }

    private boolean isScoreInTop10(int score) {
        if (leaderboardItems.size() < 10){
            return true;
        }
        // Check if the user's score is in the top 10
        // You can modify this logic based on how you store and manage leaderboard data
        for (LeaderboardItem item : leaderboardItems) {
            if (score > Integer.parseInt(item.getScore())) {
                return true;
            }
        }
        return false;
    }

    private void showNameInputDialog(final int userScore) {
        // Show dialog to prompt user for their name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congratulations! You made it to the leaderboard!");
        builder.setMessage("Enter your name:");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playerName = input.getText().toString();
                if (!playerName.isEmpty()) {
                    // Add user's score to leaderboard
                    leaderboardItems.add(new LeaderboardItem(playerName, String.valueOf(userScore)));
                    // Notify adapter about data changes
                    adapter.notifyDataSetChanged();
                    // Sort leaderboard items by score (descending order)
                    Collections.sort(leaderboardItems, new Comparator<LeaderboardItem>() {
                        @Override
                        public int compare(LeaderboardItem o1, LeaderboardItem o2) {
                            return Integer.compare(Integer.parseInt(o2.getScore()), Integer.parseInt(o1.getScore()));
                        }
                    });
                    // Limit leaderboard to top 10 records
                    if (leaderboardItems.size() > 10) {
                        leaderboardItems = leaderboardItems.subList(0, 10);
                    }
                    // Save leaderboard records
                    saveRecords();
                    // Show toast message
                    Toast.makeText(LeaderboardActivity.this, "Your score has been saved to the leaderboard!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setCancelable(false); // Prevent dialog from being dismissed by tapping outside
        builder.show();
    }

    public static List<LeaderboardItem> fromJsonString(String jsonString) {
        List<LeaderboardItem> leaderboardItems = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String playerName = jsonObject.getString("playerName");
                String score = jsonObject.getString("score");
                leaderboardItems.add(new LeaderboardItem(playerName, score));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return leaderboardItems;
    }

    public static String toJsonString(List<LeaderboardItem> leaderboardItems) {
        JSONArray jsonArray = new JSONArray();
        for (LeaderboardItem item : leaderboardItems) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("playerName", item.getPlayerName());
                jsonObject.put("score", item.getScore());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }
}
