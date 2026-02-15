package dev.artixdev.libs.com.google.gson.internal.bind;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import dev.artixdev.libs.com.google.gson.JsonSyntaxException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.TypeAdapterFactory;
import dev.artixdev.libs.com.google.gson.internal.JavaVersion;
import dev.artixdev.libs.com.google.gson.internal.PreJava9DateFormatProvider;
import dev.artixdev.libs.com.google.gson.internal.bind.util.ISO8601Utils;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonToken;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;

public final class DefaultDateTypeAdapter<T extends Date> extends TypeAdapter<T> {
   private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";
   private final DefaultDateTypeAdapter.DateType<T> dateType;
   private final List<DateFormat> dateFormats;

   private DefaultDateTypeAdapter(DefaultDateTypeAdapter.DateType<T> dateType, String datePattern) {
      this.dateFormats = new ArrayList();
      this.dateType = (DefaultDateTypeAdapter.DateType)Objects.requireNonNull(dateType);
      this.dateFormats.add(new SimpleDateFormat(datePattern, Locale.US));
      if (!Locale.getDefault().equals(Locale.US)) {
         this.dateFormats.add(new SimpleDateFormat(datePattern));
      }

   }

   private DefaultDateTypeAdapter(DefaultDateTypeAdapter.DateType<T> dateType, int style) {
      this.dateFormats = new ArrayList();
      this.dateType = (DefaultDateTypeAdapter.DateType)Objects.requireNonNull(dateType);
      this.dateFormats.add(DateFormat.getDateInstance(style, Locale.US));
      if (!Locale.getDefault().equals(Locale.US)) {
         this.dateFormats.add(DateFormat.getDateInstance(style));
      }

      if (JavaVersion.isJava9OrLater()) {
         this.dateFormats.add(PreJava9DateFormatProvider.getUSDateFormat(style));
      }

   }

   private DefaultDateTypeAdapter(DefaultDateTypeAdapter.DateType<T> dateType, int dateStyle, int timeStyle) {
      this.dateFormats = new ArrayList();
      this.dateType = (DefaultDateTypeAdapter.DateType)Objects.requireNonNull(dateType);
      this.dateFormats.add(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US));
      if (!Locale.getDefault().equals(Locale.US)) {
         this.dateFormats.add(DateFormat.getDateTimeInstance(dateStyle, timeStyle));
      }

      if (JavaVersion.isJava9OrLater()) {
         this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(dateStyle, timeStyle));
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

   public T read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
         in.nextNull();
         return null;
      } else {
         Date date = this.deserializeToDate(in);
         return this.dateType.deserialize(date);
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

   public String toString() {
      DateFormat defaultFormat = (DateFormat)this.dateFormats.get(0);
      return defaultFormat instanceof SimpleDateFormat ? "DefaultDateTypeAdapter(" + ((SimpleDateFormat)defaultFormat).toPattern() + ')' : "DefaultDateTypeAdapter(" + defaultFormat.getClass().getSimpleName() + ')';
   }

   DefaultDateTypeAdapter(DefaultDateTypeAdapter.DateType dateType, String datePattern, Object ignored) {
      this(dateType, datePattern);
   }

   DefaultDateTypeAdapter(DefaultDateTypeAdapter.DateType dateType, int style, Object ignored) {
      this(dateType, style);
   }

   DefaultDateTypeAdapter(DefaultDateTypeAdapter.DateType dateType, int dateStyle, int timeStyle, Object ignored) {
      this(dateType, dateStyle, timeStyle);
   }

   public abstract static class DateType<T extends Date> {
      public static final DefaultDateTypeAdapter.DateType<Date> DATE = new DefaultDateTypeAdapter.DateType<Date>(Date.class) {
         protected Date deserialize(Date date) {
            return date;
         }
      };
      private final Class<T> dateClass;

      protected DateType(Class<T> dateClass) {
         this.dateClass = dateClass;
      }

      protected abstract T deserialize(Date date);

      private TypeAdapterFactory createFactory(DefaultDateTypeAdapter<T> adapter) {
         return TypeAdapters.newFactory((Class)this.dateClass, adapter);
      }

      public final TypeAdapterFactory createAdapterFactory(String datePattern) {
         return this.createFactory(new DefaultDateTypeAdapter(this, datePattern));
      }

      public final TypeAdapterFactory createAdapterFactory(int style) {
         return this.createFactory(new DefaultDateTypeAdapter(this, style));
      }

      public final TypeAdapterFactory createAdapterFactory(int dateStyle, int timeStyle) {
         return this.createFactory(new DefaultDateTypeAdapter(this, dateStyle, timeStyle));
      }

      public final TypeAdapterFactory createDefaultsAdapterFactory() {
         return this.createFactory(new DefaultDateTypeAdapter(this, 2, 2));
      }
   }
}
