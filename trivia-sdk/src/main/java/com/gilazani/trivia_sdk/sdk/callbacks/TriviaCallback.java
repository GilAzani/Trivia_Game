package com.gilazani.trivia_sdk.sdk.callbacks;

public interface TriviaCallback<Type>{
    void success(Type data);
    void error(String errorMessage);
}
