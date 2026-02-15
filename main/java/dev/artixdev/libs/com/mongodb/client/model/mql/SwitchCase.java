package dev.artixdev.libs.com.mongodb.client.model.mql;

final class SwitchCase<R extends MqlValue> {
   private final MqlBoolean caseValue;
   private final R thenValue;

   SwitchCase(MqlBoolean caseValue, R thenValue) {
      this.caseValue = caseValue;
      this.thenValue = thenValue;
   }

   MqlBoolean getCaseValue() {
      return this.caseValue;
   }

   R getThenValue() {
      return this.thenValue;
   }
}
