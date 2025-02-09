package com.gilazani.trivia_sdk.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public enum Category {
    HISTORY,
    SCIENCE,
    LITERATURE,
    GEOGRAPHY,
    SPORTS,
    MOVIES,
    MUSIC,
    GENERAL_KNOWLEDGE;

    // Custom deserializer for Category
    public static class Deserializer implements JsonDeserializer<Category> {
        @Override
        public Category deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String categoryString = json.getAsString();
            try {
                return Category.valueOf(categoryString.toUpperCase()); // Assuming the enum values are in uppercase
            } catch (IllegalArgumentException e) {
                // Handle invalid enum values
                throw new JsonParseException("Invalid Category enum value: " + categoryString);
            }
        }

    }

    public static class CategorySerializer implements JsonSerializer<Category> {
        @Override
        public JsonElement serialize(Category category, Type typeOfSrc, JsonSerializationContext context) {
            // Serialize the enum to the expected string value for the API
            switch (category) {
                case HISTORY:
                    return context.serialize("History");
                case SCIENCE:
                    return context.serialize("Science");
                case LITERATURE:
                    return context.serialize("Literature");
                case GEOGRAPHY:
                    return context.serialize("Geography");
                case SPORTS:
                    return context.serialize("Sports");
                case MOVIES:
                    return context.serialize("Movies");
                case MUSIC:
                    return context.serialize("Music");
                case GENERAL_KNOWLEDGE:
                    return context.serialize("General Knowledge");
                default:
                    throw new IllegalArgumentException("Unknown Category enum value: " + category);
            }
        }
    }
}