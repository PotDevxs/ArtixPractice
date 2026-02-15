package dev.artixdev.libs.com.google.gson.internal.bind;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.JsonSyntaxException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.TypeAdapterFactory;
import dev.artixdev.libs.com.google.gson.internal.JavaVersion;
import dev.artixdev.libs.com.google.gson.internal.PreJava9DateFormatProvider;
import dev.artixdev.libs.com.google.gson.internal.bind.util.ISO8601Utils;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonToken;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;

public final class DateTypeAdapter extends TypeAdapter<Date> {
   public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
         return typeToken.getRawType() == Date.class ? (TypeAdapter<T>)new DateTypeAdapter() : null;
      }
   };
   private final List<DateFormat> dateFormats = new ArrayList();

   public DateTypeAdapter() {
      this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2, Locale.US));
      if (!Locale.getDefault().equals(Locale.US)) {
         this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2));
      }

      if (JavaVersion.isJava9OrLater()) {
         this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(2, 2));
      }

   }

   public Date read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
         in.nextNull();
         return null;
      } else {
         return this.deserializeToDate(in);
      }
   }

   private Date deserializeToDate(JsonReader in) throws IOException {
      String s = in.nextString();
      synchronized (this.dateFormats) {
         Iterator<DateFormat> formatIterator = this.dateFormats.iterator();

         while (formatIterator.hasNext()) {
            DateFormat dateFormat = formatIterator.next();

            Date parsed;
            try {
               parsed = dateFormat.parse(s);
            } catch (ParseException ignored) {
               continue;
            }

            return parsed;
         }
      }

      try {
         return ISO8601Utils.parse(s, new ParsePosition(0));
      } catch (ParseException e) {
         throw new JsonSyntaxException("Failed parsing '" + s + "' as Date; at path " + in.getPreviousPath(), e);
      }
   }

   public void write(JsonWriter out, Date value) throws IOException {
      if (value == null) {
         out.nullValue();
      } else {
         DateFormat dateFormat = (DateFormat)this.dateFormats.get(0);
         String dateFormatAsString;
         synchronized(this.dateFormats) {
            dateFormatAsString = dateFormat.format(value);
         }

         out.value(dateFormatAsString);
      }
   }
}
