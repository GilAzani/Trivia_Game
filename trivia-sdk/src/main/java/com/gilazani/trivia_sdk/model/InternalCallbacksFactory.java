package com.gilazani.trivia_sdk.model;

import android.text.style.QuoteSpan;

import com.gilazani.trivia_sdk.sdk.callbacks.TriviaCallback;
import com.gilazani.trivia_sdk.sdk.callbacks.TriviaOneQuestionCallback;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONException;
import org.json.JSONObject;


public class InternalCallbacksFactory {
    private TriviaCallback triviaCallback;

    public InternalCallbacksFactory(TriviaCallback triviaCallback) {
        this.triviaCallback = triviaCallback;
    }

    public Callback<ObjectIdResponse> createObjectIdCallback() {
        return new Callback<ObjectIdResponse>() {
            @Override
            public void onResponse(Call<ObjectIdResponse> call, Response<ObjectIdResponse> response) {
                if (response.isSuccessful()) {
                    ObjectIdResponse objectIdResponse = response.body();
                    triviaCallback.success(objectIdResponse);
                } else {
                    try {
                        // Parse the error response body as JSON
                        JSONObject errorBodyJson = new JSONObject(response.errorBody().string());
                        // Extract the error message
                        String errorMessage = errorBodyJson.getString("error");
                        // Pass the error message to the callback
                        triviaCallback.error(errorMessage);
                    } catch (JSONException e) {
                        // Handle JSON parsing errors
                        e.printStackTrace();
                        // Pass a generic error message to the callback
                        triviaCallback.error("Error parsing JSON response");
                    } catch (IOException e) {
                        // Handle IO errors
                        e.printStackTrace();
                        // Pass a generic error message to the callback
                        triviaCallback.error("Error reading response body");
                    }
                }
            }

            @Override
            public void onFailure(Call<ObjectIdResponse> call, Throwable t) {
                triviaCallback.error(t.getMessage());
                t.printStackTrace();
            }
        };
    }

    public Callback<List<Question>> createQuestionsCallback() {
        return new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    List<Question> questions = response.body();
                    triviaCallback.success(questions);
                } else {
                    triviaCallback.error("" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                triviaCallback.error(t.getMessage());
            }
        };
    }

    public Callback<Question> createOneQuestionCallback() {
        return new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful()) {
                    Question question = response.body();
                    triviaCallback.success(question);
                } else {
                    triviaCallback.error("" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                triviaCallback.error(t.getMessage());
                t.printStackTrace();
            }
        };
    }

    public Callback<Void> createVoidCallback(){
        return new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    triviaCallback.success(null);
                } else {
                    triviaCallback.error("" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                triviaCallback.error(t.getMessage());
                t.printStackTrace();
            }
        };
    }
}
