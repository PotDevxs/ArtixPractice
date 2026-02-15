package dev.artixdev.libs.net.kyori.adventure.key;

import java.util.Comparator;
import java.util.OptionalInt;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface Key extends Comparable<Key>, Keyed, Namespaced, Examinable {
   String MINECRAFT_NAMESPACE = "minecraft";
   char DEFAULT_SEPARATOR = ':';

   @NotNull
   static Key key(@NotNull @KeyPattern String string) {
      return key(string, ':');
   }

   @NotNull
   static Key key(@NotNull String string, char character) {
      int index = string.indexOf(character);
      String namespace = index >= 1 ? string.substring(0, index) : "minecraft";
      String value = index >= 0 ? string.substring(index + 1) : string;
      return key(namespace, value);
   }

   @NotNull
   static Key key(@NotNull Namespaced namespaced, @NotNull @KeyPattern.Value String value) {
      return key(namespaced.namespace(), value);
   }

   @NotNull
   static Key key(@NotNull @KeyPattern.Namespace String namespace, @NotNull @KeyPattern.Value String value) {
      return new KeyImpl(namespace, value);
   }

   @NotNull
   static Comparator<? super Key> comparator() {
      return KeyImpl.COMPARATOR;
   }

   static boolean parseable(@Nullable String string) {
      if (string == null) {
         return false;
      } else {
         int index = string.indexOf(58);
         String namespace = index >= 1 ? string.substring(0, index) : "minecraft";
         String value = index >= 0 ? string.substring(index + 1) : string;
         return parseableNamespace(namespace) && parseableValue(value);
      }
   }

   static boolean parseableNamespace(@NotNull String namespace) {
      return !checkNamespace(namespace).isPresent();
   }

   @NotNull
   static OptionalInt checkNamespace(@NotNull String namespace) {
      int i = 0;

      for(int length = namespace.length(); i < length; ++i) {
         if (!allowedInNamespace(namespace.charAt(i))) {
            return OptionalInt.of(i);
         }
      }

      return OptionalInt.empty();
   }

   static boolean parseableValue(@NotNull String value) {
      return !checkValue(value).isPresent();
   }

   @NotNull
   static OptionalInt checkValue(@NotNull String value) {
      int i = 0;

      for(int length = value.length(); i < length; ++i) {
         if (!allowedInValue(value.charAt(i))) {
            return OptionalInt.of(i);
         }
      }

      return OptionalInt.empty();
   }

   static boolean allowedInNamespace(char character) {
      return KeyImpl.allowedInNamespace(character);
   }

   static boolean allowedInValue(char character) {
      return KeyImpl.allowedInValue(character);
   }

   @NotNull
   @KeyPattern.Namespace
   String namespace();

   @NotNull
   @KeyPattern.Value
   String value();

   @NotNull
   String asString();

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("namespace", this.namespace()), ExaminableProperty.of("value", this.value()));
   }

   default int compareTo(@NotNull Key that) {
      return comparator().compare(this, that);
   }

   @NotNull
   default Key key() {
      return this;
   }
}
