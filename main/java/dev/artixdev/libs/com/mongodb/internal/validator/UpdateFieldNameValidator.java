package dev.artixdev.libs.com.mongodb.internal.validator;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.FieldNameValidator;

public class UpdateFieldNameValidator implements FieldNameValidator {
   private int numFields = 0;

   public boolean validate(String fieldName) {
      ++this.numFields;
      return fieldName.startsWith("$");
   }

   public String getValidationErrorMessage(String fieldName) {
      Assertions.assertFalse(fieldName.startsWith("$"));
      return String.format("All update operators must start with '$', but '%s' does not", fieldName);
   }

   public FieldNameValidator getValidatorForField(String fieldName) {
      return new NoOpFieldNameValidator();
   }

   public void start() {
      this.numFields = 0;
   }

   public void end() {
      if (this.numFields == 0) {
         throw new IllegalArgumentException("Invalid BSON document for an update. The document may not be empty.");
      }
   }
}
