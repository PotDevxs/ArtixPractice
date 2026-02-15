package dev.artixdev.libs.com.google.gson.internal.sql;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.TypeAdapterFactory;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;

class SqlTimestampTypeAdapter extends TypeAdapter<Timestamp> {
   static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
         if (typeToken.getRawType() == Timestamp.class) {
            TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);
            return (TypeAdapter<T>) new SqlTimestampTypeAdapter(dateTypeAdapter);
         } else {
            return null;
         }
      }
   };
   private final TypeAdapter<Date> dateTypeAdapter;

   private SqlTimestampTypeAdapter(TypeAdapter<Date> dateTypeAdapter) {
      this.dateTypeAdapter = dateTypeAdapter;
   }

   public Timestamp read(JsonReader in) throws IOException {
      Date date = (Date)this.dateTypeAdapter.read(in);
      return date != null ? new Timestamp(date.getTime()) : null;
   }

   public void write(JsonWriter out, Timestamp value) throws IOException {
      this.dateTypeAdapter.write(out, value);
   }

   // $FF: synthetic method
   SqlTimestampTypeAdapter(TypeAdapter x0, Object x1) {
      this(x0);
   }
}
