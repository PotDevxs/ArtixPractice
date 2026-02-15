package dev.artixdev.libs.com.mongodb.internal.validator;

import java.util.Arrays;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.FieldNameValidator;

public class ReplacingDocumentFieldNameValidator implements FieldNameValidator {
   private static final NoOpFieldNameValidator NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
   private static final List<String> EXCEPTIONS = Arrays.asList("$db", "$ref", "$id");

   public boolean validate(String fieldName) {
      return !fieldName.startsWith("$") || EXCEPTIONS.contains(fieldName);
   }

   public String getValidationErrorMessage(String fieldName) {
      Assertions.assertFalse(this.validate(fieldName));
      return String.format("Field names in a replacement document can not start with '$' but '%s' does", fieldName);
   }

   public FieldNameValidator getValidatorForField(String fieldName) {
      return NO_OP_FIELD_NAME_VALIDATOR;
   }
}
