package com.gilazani.triviagame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gilazani.trivia_sdk.model.Question;
import com.gilazani.trivia_sdk.sdk.TriviaSDK;
import com.gilazani.trivia_sdk.sdk.callbacks.TriviaOneQuestionCallback;

public class MenuActivity extends AppCompatActivity {

    private Button leaderboardButton;
    private Button playButton; // New play button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        
        initViews();

        setBtnOnClickListener();
    }

    private void setBtnOnClickListener() {
        // Set OnClickListener for leaderboard button
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LeaderboardActivity when leaderboard button is clicked
                startActivity(new Intent(MenuActivity.this, LeaderboardActivity.class));
            }
        });

        // Set OnClickListener for play button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start GameSettingsActivity when play button is clicked
                startActivity(new Intent(MenuActivity.this, GameSettingsActivity.class));
            }
        });
    }

    private void initViews() {
        // Initialize leaderboard button
        leaderboardButton = findViewById(R.id.buttonLeaderboard);

        // Initialize play button
        playButton = findViewById(R.id.buttonPlay);
    }
}