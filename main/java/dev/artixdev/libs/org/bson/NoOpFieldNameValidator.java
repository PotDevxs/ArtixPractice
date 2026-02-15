package dev.artixdev.libs.org.bson;

class NoOpFieldNameValidator implements FieldNameValidator {
   public boolean validate(String fieldName) {
      return true;
   }

   public FieldNameValidator getValidatorForField(String fieldName) {
      return this;
   }
}
