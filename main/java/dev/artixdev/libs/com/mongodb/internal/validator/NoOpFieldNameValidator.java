package dev.artixdev.libs.com.mongodb.internal.validator;

import dev.artixdev.libs.org.bson.FieldNameValidator;

public class NoOpFieldNameValidator implements FieldNameValidator {
   public boolean validate(String fieldName) {
      return true;
   }

   public FieldNameValidator getValidatorForField(String fieldName) {
      return this;
   }
}
