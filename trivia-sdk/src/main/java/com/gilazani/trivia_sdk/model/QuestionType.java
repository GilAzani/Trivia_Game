package com.gilazani.trivia_sdk.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public enum QuestionType {
    YES_NO,
    MULTIPLE_CHOICE;

    public static class Deserializer implements JsonDeserializer<QuestionType> {
        @Override
        public QuestionType deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String questionTypeString = json.getAsString().toLowerCase();
            if ("multiple choice".equals(questionTypeString)) {
                return MULTIPLE_CHOICE;
            } else if ("yes/no".equals(questionTypeString)) {
                return YES_NO;
            } else {
                throw new JsonParseException("Invalid QuestionType enum value: " + questionTypeString);
            }
        }
    }

    public static class QuestionTypeSerializer implements JsonSerializer<QuestionType> {
        @Override
        public JsonElement serialize(QuestionType questionType, Type typeOfSrc, JsonSerializationContext context) {
            // Serialize the enum to the expected string value for the API
            switch (questionType) {
                case YES_NO:
                    return context.serialize("yes/no");
                case MULTIPLE_CHOICE:
                    return context.serialize("multiple choice");
                default:
                    throw new IllegalArgumentException("Unknown QuestionType enum value: " + questionType);
            }
        }
    }
}