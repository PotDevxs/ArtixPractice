package dev.artixdev.libs.net.kyori.adventure.key;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import dev.artixdev.libs.org.intellij.lang.annotations.Pattern;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER})
@Pattern("(?:([a-z0-9_\\-.]+:)?|:)[a-z0-9_\\-./]+")
public @interface KeyPattern {
   @Documented
   @Retention(RetentionPolicy.CLASS)
   @Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER})
   @Pattern("[a-z0-9_\\-./]+")
   public @interface Value {
   }

   @Documented
   @Retention(RetentionPolicy.CLASS)
   @Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER})
   @Pattern("[a-z0-9_\\-.]+")
   public @interface Namespace {
   }
}
