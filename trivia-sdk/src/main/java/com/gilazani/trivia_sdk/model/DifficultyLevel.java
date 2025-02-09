package com.gilazani.trivia_sdk.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public enum DifficultyLevel {
    EASY,
    MEDIUM,
    HARD;

    public static class Deserializer implements JsonDeserializer<DifficultyLevel> {
        @Override
        public DifficultyLevel deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String difficultyLevelString = json.getAsString();
            try {
                return DifficultyLevel.valueOf(difficultyLevelString.toUpperCase()); // Assuming the enum values are in uppercase
            } catch (IllegalArgumentException e) {
                // Handle invalid enum values
                throw new JsonParseException("Invalid DifficultyLevel enum value: " + difficultyLevelString);
            }
        }
    }

    public static class DifficultyLevelSerializer implements JsonSerializer<DifficultyLevel> {
        @Override
        public JsonElement serialize(DifficultyLevel difficultyLevel, Type typeOfSrc, JsonSerializationContext context) {
            // Serialize the enum to the expected string value for the API
            switch (difficultyLevel) {
                case EASY:
                    return context.serialize("Easy");
                case MEDIUM:
                    return context.serialize("Medium");
                case HARD:
                    return context.serialize("Hard");
                default:
                    throw new IllegalArgumentException("Unknown QuestionType enum value: " + difficultyLevel);
            }
        }
    }
}