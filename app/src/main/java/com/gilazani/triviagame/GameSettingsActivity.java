package com.gilazani.triviagame;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gilazani.trivia_sdk.model.Category;
import com.gilazani.trivia_sdk.model.DifficultyLevel;


public class GameSettingsActivity extends AppCompatActivity {

    private RadioGroup categoryRadioGroup;
    private RadioGroup difficultyRadioGroup;
    private Button backToMainMenuButton;
    private Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        initViews();

        // Set options for category radio group
        setOptionsForCategory();

        // Set options for difficulty radio group
        setOptionsForDifficulty();

        setButtonsOnClickListener();
    }

    private void setButtonsOnClickListener() {
        // Set OnClickListener for back to main menu button
        backToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity and return to main menu
                finish();
            }
        });

        // Set OnClickListener for start game button
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve selected options and start game
                startGame();
            }
        });
    }

    private void initViews() {
        // Initialize radio groups
        categoryRadioGroup = findViewById(R.id.radioGroupCategory);
        difficultyRadioGroup = findViewById(R.id.radioGroupDifficulty);

        // Initialize buttons
        startGameButton = findViewById(R.id.buttonStartGame);
        backToMainMenuButton = findViewById(R.id.buttonBackToMainMenu);
    }

    private void setOptionsForCategory() {
        categoryRadioGroup.removeAllViews(); // Clear existing options
        for (Category category : Category.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(category.name());
            categoryRadioGroup.addView(radioButton);
        }
    }

    private void setOptionsForDifficulty() {
        difficultyRadioGroup.removeAllViews(); // Clear existing options
        for (DifficultyLevel difficultyLevel : DifficultyLevel.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(difficultyLevel.name());
            difficultyRadioGroup.addView(radioButton);
        }
    }


    private void startGame() {
        // Check if any of the radio groups are left unselected
        if (categoryRadioGroup.getCheckedRadioButtonId() == -1 ||
                difficultyRadioGroup.getCheckedRadioButtonId() == -1) {
            // Show a message to the user prompting them to make a selection
            Toast.makeText(this, "Please select options for all categories.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve selected options from radio groups
        int selectedCategoryId = categoryRadioGroup.getCheckedRadioButtonId();
        int selectedDifficultyId = difficultyRadioGroup.getCheckedRadioButtonId();

        // Get the text of the selected radio buttons
        RadioButton categoryRadioButton = findViewById(selectedCategoryId);
        RadioButton difficultyRadioButton = findViewById(selectedDifficultyId);

        // Get the text of the selected options
        String selectedCategory = categoryRadioButton.getText().toString();
        String selectedDifficulty = difficultyRadioButton.getText().toString();

        // Start the GameActivity and pass the selected choices
        Intent intent = new Intent(GameSettingsActivity.this, GameActivity.class);
        intent.putExtra("CATEGORY", selectedCategory);
        intent.putExtra("DIFFICULTY", selectedDifficulty);
        startActivity(intent);
    }
}