package dev.artixdev.libs.com.google.gson.internal.sql;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.JsonSyntaxException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.TypeAdapterFactory;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonToken;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;

final class SqlTimeTypeAdapter extends TypeAdapter<Time> {
   static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
         return typeToken.getRawType() == Time.class ? (TypeAdapter<T>) new SqlTimeTypeAdapter() : null;
      }
   };
   private final DateFormat format;

   private SqlTimeTypeAdapter() {
      this.format = new SimpleDateFormat("hh:mm:ss a");
   }

   public Time read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
         in.nextNull();
         return null;
      } else {
         String s = in.nextString();

         try {
            synchronized(this) {
               Date date = this.format.parse(s);
               return new Time(date.getTime());
            }
         } catch (ParseException e) {
            throw new JsonSyntaxException("Failed parsing '" + s + "' as SQL Time; at path " + in.getPreviousPath(), e);
         }
      }
   }

   public void write(JsonWriter out, Time value) throws IOException {
      if (value == null) {
         out.nullValue();
      } else {
         String timeString;
         synchronized(this) {
            timeString = this.format.format(value);
         }

         out.value(timeString);
      }
   }

   // $FF: synthetic method
   SqlTimeTypeAdapter(Object x0) {
      this();
   }
}
