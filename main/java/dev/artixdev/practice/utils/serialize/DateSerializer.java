package dev.artixdev.practice.utils.serialize;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import dev.artixdev.libs.com.google.gson.JsonDeserializationContext;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonPrimitive;
import dev.artixdev.libs.com.google.gson.JsonSerializationContext;
import dev.artixdev.libs.com.google.gson.JsonSerializer;

public class DateSerializer implements JsonDeserializer<Date>, JsonSerializer<Date> {
    private static final DateFormat PRIMARY_FORMAT = new SimpleDateFormat("MMM d, yyyy, h:mm:ss a");
    private static final DateFormat SECONDARY_FORMAT = new SimpleDateFormat("MMM d, yyyy h:mm:ss a");

    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String string = jsonElement.getAsString();
        return deserialize(string);
    }

    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(PRIMARY_FORMAT.format(date));
    }

    public static Date deserialize(String string) {
        try {
            return PRIMARY_FORMAT.parse(string);
        } catch (ParseException e) {
            try {
                return SECONDARY_FORMAT.parse(string);
            } catch (ParseException e2) {
                try {
                    return DateFormat.getDateInstance().parse(string);
                } catch (ParseException e3) {
                    return null;
                }
            }
        }
    }
}
