package dev.artixdev.libs.com.mongodb;

import java.util.Objects;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Immutable
public final class LoggerSettings {
   private final int maxDocumentLength;

   public static LoggerSettings.Builder builder() {
      return new LoggerSettings.Builder();
   }

   public static LoggerSettings.Builder builder(LoggerSettings loggerSettings) {
      return builder().applySettings(loggerSettings);
   }

   public int getMaxDocumentLength() {
      return this.maxDocumentLength;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         LoggerSettings that = (LoggerSettings)o;
         return this.maxDocumentLength == that.maxDocumentLength;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.maxDocumentLength});
   }

   public String toString() {
      return "LoggerSettings{maxDocumentLength=" + this.maxDocumentLength + '}';
   }

   private LoggerSettings(LoggerSettings.Builder builder) {
      this.maxDocumentLength = builder.maxDocumentLength;
   }

   // $FF: synthetic method
   LoggerSettings(LoggerSettings.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder {
      private int maxDocumentLength;

      private Builder() {
         this.maxDocumentLength = 1000;
      }

      public LoggerSettings.Builder applySettings(LoggerSettings loggerSettings) {
         Assertions.notNull("loggerSettings", loggerSettings);
         this.maxDocumentLength = loggerSettings.maxDocumentLength;
         return this;
      }

      public LoggerSettings.Builder maxDocumentLength(int maxDocumentLength) {
         this.maxDocumentLength = maxDocumentLength;
         return this;
      }

      public LoggerSettings build() {
         return new LoggerSettings(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
