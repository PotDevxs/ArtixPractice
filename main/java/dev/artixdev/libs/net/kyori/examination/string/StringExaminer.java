package dev.artixdev.libs.net.kyori.examination.string;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.AbstractExaminer;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class StringExaminer extends AbstractExaminer<String> {
   private static final Function<String, String> DEFAULT_ESCAPER = (string) -> {
      return string.replace("\"", "\\\"").replace("\\", "\\\\").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
   };
   private static final Collector<CharSequence, ?, String> COMMA_CURLY = Collectors.joining(", ", "{", "}");
   private static final Collector<CharSequence, ?, String> COMMA_SQUARE = Collectors.joining(", ", "[", "]");
   private final Function<String, String> escaper;

   @NotNull
   public static StringExaminer simpleEscaping() {
      return StringExaminer.Instances.SIMPLE_ESCAPING;
   }

   public StringExaminer(@NotNull Function<String, String> escaper) {
      this.escaper = escaper;
   }

   @NotNull
   protected <E> String array(@NotNull E[] array, @NotNull Stream<String> elements) {
      return (String)elements.collect(COMMA_SQUARE);
   }

   @NotNull
   protected <E> String collection(@NotNull Collection<E> collection, @NotNull Stream<String> elements) {
      return (String)elements.collect(COMMA_SQUARE);
   }

   @NotNull
   protected String examinable(@NotNull String name, @NotNull Stream<Entry<String, String>> properties) {
      return name + (String)properties.map((property) -> {
         return (String)property.getKey() + '=' + (String)property.getValue();
      }).collect(COMMA_CURLY);
   }

   @NotNull
   protected <K, V> String map(@NotNull Map<K, V> map, @NotNull Stream<Entry<String, String>> entries) {
      return (String)entries.map((entry) -> {
         return (String)entry.getKey() + '=' + (String)entry.getValue();
      }).collect(COMMA_CURLY);
   }

   @NotNull
   protected String nil() {
      return "null";
   }

   @NotNull
   protected String scalar(@NotNull Object value) {
      return String.valueOf(value);
   }

   @NotNull
   public String examine(boolean value) {
      return String.valueOf(value);
   }

   @NotNull
   public String examine(byte value) {
      return String.valueOf(value);
   }

   @NotNull
   public String examine(char value) {
      return Strings.wrapIn((String)this.escaper.apply(String.valueOf(value)), '\'');
   }

   @NotNull
   public String examine(double value) {
      return Strings.withSuffix(String.valueOf(value), 'd');
   }

   @NotNull
   public String examine(float value) {
      return Strings.withSuffix(String.valueOf(value), 'f');
   }

   @NotNull
   public String examine(int value) {
      return String.valueOf(value);
   }

   @NotNull
   public String examine(long value) {
      return String.valueOf(value);
   }

   @NotNull
   public String examine(short value) {
      return String.valueOf(value);
   }

   @NotNull
   protected <T> String stream(@NotNull Stream<T> stream) {
      return (String)stream.map(this::examine).collect(COMMA_SQUARE);
   }

   @NotNull
   protected String stream(@NotNull DoubleStream stream) {
      return (String)stream.mapToObj(this::examine).collect(COMMA_SQUARE);
   }

   @NotNull
   protected String stream(@NotNull IntStream stream) {
      return (String)stream.mapToObj(this::examine).collect(COMMA_SQUARE);
   }

   @NotNull
   protected String stream(@NotNull LongStream stream) {
      return (String)stream.mapToObj(this::examine).collect(COMMA_SQUARE);
   }

   @NotNull
   public String examine(@Nullable String value) {
      return value == null ? this.nil() : Strings.wrapIn((String)this.escaper.apply(value), '"');
   }

   @NotNull
   protected String array(int length, IntFunction<String> value) {
      StringBuilder sb = new StringBuilder();
      sb.append('[');

      for(int i = 0; i < length; ++i) {
         sb.append((String)value.apply(i));
         if (i + 1 < length) {
            sb.append(", ");
         }
      }

      sb.append(']');
      return sb.toString();
   }

   private static final class Instances {
      static final StringExaminer SIMPLE_ESCAPING;

      static {
         SIMPLE_ESCAPING = new StringExaminer(StringExaminer.DEFAULT_ESCAPER);
      }
   }
}
