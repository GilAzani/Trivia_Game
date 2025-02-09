package com.gilazani.trivia_sdk.api;

import android.util.Log;

import com.gilazani.trivia_sdk.sdk.callbacks.TriviaCallback;
import com.gilazani.trivia_sdk.sdk.callbacks.TriviaOneQuestionCallback;
import com.gilazani.trivia_sdk.model.Category;
import com.gilazani.trivia_sdk.model.DifficultyLevel;
import com.gilazani.trivia_sdk.model.InternalCallbacksFactory;
import com.gilazani.trivia_sdk.model.ObjectIdResponse;
import com.gilazani.trivia_sdk.model.Question;
import com.gilazani.trivia_sdk.model.QuestionType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;


public class TriviaApiClient {
    private static final String BASE_URL = "http://172.20.25.65:5000";  // TODO get this value from local.properties
    private TriviaCallback triviaCallback;

     private Gson gson;

     private Retrofit retrofit;

     private TriviaApiService triviaApiService;

     private InternalCallbacksFactory internalCallbacksFactory;


    public TriviaApiClient(TriviaCallback triviaCallback) {
        this.gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(Category.class, new Category.Deserializer())
                .registerTypeAdapter(DifficultyLevel.class, new DifficultyLevel.Deserializer())
                .registerTypeAdapter(QuestionType.class, new QuestionType.Deserializer())
                .registerTypeAdapter(QuestionType.class, new QuestionType.QuestionTypeSerializer())
                .registerTypeAdapter(Category.class, new Category.CategorySerializer())
                .registerTypeAdapter(DifficultyLevel.class, new DifficultyLevel.DifficultyLevelSerializer())
                .create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(this.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.triviaApiService = this.retrofit.create(TriviaApiService.class);

        this.triviaCallback = triviaCallback;

        this.internalCallbacksFactory = new InternalCallbacksFactory(triviaCallback);
    }

    public void getQuestions(Category category, DifficultyLevel difficulty, QuestionType type, int amount) {

        Call<List<Question>> call = this.triviaApiService.getQuestions(category, difficulty, type, amount);
        call.enqueue(this.internalCallbacksFactory.createQuestionsCallback());
    }

    public void suggestQuestion(Question question) {
        Call<ObjectIdResponse> call = this.triviaApiService.suggestQuestion(question);
        call.enqueue(this.internalCallbacksFactory.createObjectIdCallback());
    }

    public void getQuestionById(String questionId) {
        Call<Question> call = this.triviaApiService.getQuestionById(questionId);
        call.enqueue(this.internalCallbacksFactory.createOneQuestionCallback());
    }

    public void deleteQuestion(String questionId) {
        Call<Void> call = this.triviaApiService.deleteQuestion(questionId);
        call.enqueue(this.internalCallbacksFactory.createVoidCallback());
    }

}