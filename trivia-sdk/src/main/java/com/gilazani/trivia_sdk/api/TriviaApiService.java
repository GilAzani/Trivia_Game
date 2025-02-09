package com.gilazani.trivia_sdk.api;

import com.gilazani.trivia_sdk.model.Category;
import com.gilazani.trivia_sdk.model.DifficultyLevel;
import com.gilazani.trivia_sdk.model.ObjectIdResponse;
import com.gilazani.trivia_sdk.model.Question;
import com.gilazani.trivia_sdk.model.QuestionType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TriviaApiService {
    @GET("/questions")
    Call<List<Question>> getQuestions(@Query("category") Category category, @Query("difficulty") DifficultyLevel difficulty, @Query("type") QuestionType type, @Query("amount") int amount);

    @POST("/questions")
    Call<ObjectIdResponse> suggestQuestion(@Body Question question);

    @GET("/questions/{questionId}")
    Call<Question> getQuestionById(@Path("questionId") String questionId);

    @DELETE("/questions/{questionId}")
    Call<Void> deleteQuestion(@Path("questionId") String questionId);
}
