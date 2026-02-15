package dev.artixdev.libs.com.mongodb.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.PACKAGE, ElementType.TYPE})
@Documented
@Beta({Beta.Reason.CLIENT})
public @interface Beta {
   Beta.Reason[] value();

   public static enum Reason {
      CLIENT,
      SERVER;

      // $FF: synthetic method
      private static Beta.Reason[] $values() {
         return new Beta.Reason[]{CLIENT, SERVER};
      }
   }
}
