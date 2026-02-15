package dev.artixdev.libs.com.mongodb.internal.validator;

import java.util.Map;
import dev.artixdev.libs.org.bson.FieldNameValidator;

public class MappedFieldNameValidator implements FieldNameValidator {
   private final FieldNameValidator defaultValidator;
   private final Map<String, FieldNameValidator> fieldNameToValidatorMap;

   public MappedFieldNameValidator(FieldNameValidator defaultValidator, Map<String, FieldNameValidator> fieldNameToValidatorMap) {
      this.defaultValidator = defaultValidator;
      this.fieldNameToValidatorMap = fieldNameToValidatorMap;
   }

   public boolean validate(String fieldName) {
      return this.defaultValidator.validate(fieldName);
   }

   public String getValidationErrorMessage(String fieldName) {
      return this.defaultValidator.getValidationErrorMessage(fieldName);
   }

   public FieldNameValidator getValidatorForField(String fieldName) {
      return this.fieldNameToValidatorMap.containsKey(fieldName) ? (FieldNameValidator)this.fieldNameToValidatorMap.get(fieldName) : this.defaultValidator;
   }
}
