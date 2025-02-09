package com.gilazani.triviagame;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import com.gilazani.trivia_sdk.model.Category;
import com.gilazani.trivia_sdk.model.DifficultyLevel;
import com.gilazani.trivia_sdk.model.Question;
import com.gilazani.trivia_sdk.model.QuestionType;
import com.gilazani.trivia_sdk.sdk.TriviaSDK;
import com.gilazani.trivia_sdk.sdk.callbacks.TriviaQuestionsCallback;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private TextView textViewScore;
    private TextView textViewQuestion;
    private LinearLayout linearAnswerOptions;
    private Button selectedAnswerButton;

    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private CountDownTimer countDownTimer;

    private LottieAnimationView loadingAnimation;

    private Category selectedCategory;
    private DifficultyLevel selectedDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initViews();

        // Start the loading animation
        startLoadingAnimation();

        getUserSettings();

        // Get questions from the SDK and start the game after the questions returned
        getQuestionsAndStartGame(selectedCategory, selectedDifficulty);
    }

    private void getUserSettings() {
        // Retrieve selected options passed from GameSettingsActivity
        String selectedCategoryString = getIntent().getStringExtra("CATEGORY");
        String selectedDifficultyString = getIntent().getStringExtra("DIFFICULTY");

        // Convert string values to enum values
        selectedCategory = Category.valueOf(selectedCategoryString);
        selectedDifficulty = DifficultyLevel.valueOf(selectedDifficultyString);
    }

    private void initViews() {
        // Initialize views
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        linearAnswerOptions = findViewById(R.id.linearAnswerOptions);
        loadingAnimation = findViewById(R.id.lottieAnimation);
    }

    private void startLoadingAnimation() {
        loadingAnimation.setVisibility(View.VISIBLE);
        loadingAnimation.playAnimation();

        // Hide other UI elements
        textViewTimer.setVisibility(View.GONE);
        textViewScore.setVisibility(View.GONE);
        textViewQuestion.setVisibility(View.GONE);
        linearAnswerOptions.setVisibility(View.GONE);
    }
    private void stopLoadingAnimation(){
        loadingAnimation.setVisibility(View.GONE);
        loadingAnimation.cancelAnimation();

        // Show other UI elements
        textViewTimer.setVisibility(View.VISIBLE);
        textViewScore.setVisibility(View.VISIBLE);
        textViewQuestion.setVisibility(View.VISIBLE);
        linearAnswerOptions.setVisibility(View.VISIBLE);

    }

    private void startGame(List<Question> questions) {
        this.questions = questions;
        currentQuestionIndex = 0;
        score = 0;
        displayQuestion(); // Display the first question
    }

    private int setDifficultyFactor() {
        int difficultyFactor = 1;
        switch (selectedDifficulty) {
            case EASY:
                difficultyFactor = 1;
                break;
            case MEDIUM:
                difficultyFactor = 2;
                break;
            case HARD:
                difficultyFactor = 3;
                break;
        }
        return difficultyFactor;
    }

    private void getQuestionsAndStartGame(Category selectedCategory, DifficultyLevel selectedDifficulty) {
        TriviaSDK triviaSDK = new TriviaSDK();
        triviaSDK.getQuestions(selectedCategory, selectedDifficulty, QuestionType.MULTIPLE_CHOICE, 10, new TriviaQuestionsCallback() {
            @Override
            public void success(List<Question> questions) {
                // Stop the loading animation
                stopLoadingAnimation();
                startGame(questions);
            }

            @Override
            public void error(String s) {
                // Stop the loading animation
                stopLoadingAnimation();
                List<Question> defaultQuestions = loadDefaultQuestion();
                startGame(defaultQuestions);
            }
        });
    }

    private List<Question> loadDefaultQuestion() {
        List<Question> questions = new ArrayList<>();

        Question question3 = new Question();
        question3.set_id("3");
        question3.setQuestion("Is the Earth flat?");
        List<String> answers3 = new ArrayList<>();
        answers3.add("Yes");
        answers3.add("No");
        question3.setAnswers(answers3);
        question3.setCorrect(1); // Index of correct answer
        question3.setCategory(Category.SCIENCE);
        question3.setDifficulty(DifficultyLevel.HARD);
        question3.setType(QuestionType.YES_NO);
        question3.setIs_approved(true);
        questions.add(question3);

        return questions;
    }


    private void startTimer() {
        countDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                textViewTimer.setText(String.valueOf(secondsLeft)); // Display remaining seconds
            }

            @Override
            public void onFinish() {
                // Timer finished, user didn't answer in time
                // Automatically select the first answer as wrong
                Toast.makeText(GameActivity.this, "Time's up! You didn't select an answer.", Toast.LENGTH_SHORT).show();
                currentQuestionIndex++;
                displayQuestion();
            }
        };
        countDownTimer.start();
    }


    private void displayQuestion() {
        // Display the current question and its answers
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            textViewQuestion.setText(currentQuestion.getQuestion());
            List<String> answers = currentQuestion.getAnswers();
            linearAnswerOptions.removeAllViews(); // Clear previous answer buttons
            for (String answer : answers) {
                Button answerButton = new Button(this);
                answerButton.setText(answer);
                linearAnswerOptions.addView(answerButton);
                // Set up OnClickListener for answer buttons
                answerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAnswer((Button) v);
                    }
                });
            }
            // Start timer for the current question
            startTimer();
        } else {
            // No more questions, game over
            // Display final score
            Toast.makeText(this, "Game Over! Your final score is: " + score, Toast.LENGTH_SHORT).show();

            // Navigate to leaderboard activity
            Intent intent = new Intent(GameActivity.this, LeaderboardActivity.class);
            intent.putExtra("FINAL_SCORE", score);
            startActivity(intent);
            finish(); // Optional: Close the current activity to prevent returning to it with back button
        }
    }

    private void checkAnswer(Button selectedButton) {
        // Stop the timer
        countDownTimer.cancel();
        selectedAnswerButton = selectedButton;

        // Retrieve the remaining seconds from the timer display
        long secondsLeft = Long.parseLong(textViewTimer.getText().toString());

        // Calculate time bonus (assuming 1 point per second)
        int timeBonus = (int) secondsLeft;

        int difficultyFactor = setDifficultyFactor();

        // Retrieve the index of the selected answer
        int selectedAnswerIndex = linearAnswerOptions.indexOfChild(selectedAnswerButton);

        // Retrieve the current question
        Question currentQuestion = questions.get(currentQuestionIndex);

        // Check if the selected answer is correct
        if (selectedAnswerIndex == currentQuestion.getCorrect()) {
            // Correct answer
            score += difficultyFactor * (1 + timeBonus); // Increase score with time bonus
            textViewScore.setText("Score: " + score);
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            // Incorrect answer
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }

        // Move to the next question
        currentQuestionIndex++;
        displayQuestion(); // Display the next question
    }


}
