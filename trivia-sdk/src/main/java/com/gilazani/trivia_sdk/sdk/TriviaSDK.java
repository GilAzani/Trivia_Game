package com.gilazani.trivia_sdk.sdk;

import com.gilazani.trivia_sdk.api.TriviaApiClient;
import com.gilazani.trivia_sdk.model.Category;
import com.gilazani.trivia_sdk.model.DifficultyLevel;
import com.gilazani.trivia_sdk.model.Question;
import com.gilazani.trivia_sdk.model.QuestionType;
import com.gilazani.trivia_sdk.sdk.callbacks.TriviaObjectIdCallback;
import com.gilazani.trivia_sdk.sdk.callbacks.TriviaOneQuestionCallback;
import com.gilazani.trivia_sdk.sdk.callbacks.TriviaQuestionsCallback;
import com.gilazani.trivia_sdk.sdk.callbacks.TriviaVoidCallback;

import java.lang.reflect.Type;

public class TriviaSDK {
    public TriviaSDK() {
    }

    public void getQuestions(Category category, DifficultyLevel difficulty, QuestionType type, int amount, final TriviaQuestionsCallback callback) {
        new TriviaApiClient(callback).getQuestions(category, difficulty, type, amount);
    }

    public void suggestQuestion(Question question, final TriviaObjectIdCallback callback) {
        new TriviaApiClient(callback).suggestQuestion(question);
    }

    public void getQuestionById(String questionId, final TriviaOneQuestionCallback callback) {
        new TriviaApiClient(callback).getQuestionById(questionId);
    }

    public void deleteQuestion(String questionId, final TriviaVoidCallback callback) {
        new TriviaApiClient(callback).deleteQuestion(questionId);
    }
}
