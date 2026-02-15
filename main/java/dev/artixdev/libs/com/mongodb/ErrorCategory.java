package dev.artixdev.libs.com.mongodb;

import java.util.Arrays;
import java.util.List;

public enum ErrorCategory {
   UNCATEGORIZED,
   DUPLICATE_KEY,
   EXECUTION_TIMEOUT;

   private static final List<Integer> DUPLICATE_KEY_ERROR_CODES = Arrays.asList(11000, 11001, 12582);
   private static final List<Integer> EXECUTION_TIMEOUT_ERROR_CODES = Arrays.asList(50);

   public static ErrorCategory fromErrorCode(int code) {
      if (DUPLICATE_KEY_ERROR_CODES.contains(code)) {
         return DUPLICATE_KEY;
      } else {
         return EXECUTION_TIMEOUT_ERROR_CODES.contains(code) ? EXECUTION_TIMEOUT : UNCATEGORIZED;
      }
   }

   // $FF: synthetic method
   private static ErrorCategory[] $values() {
      return new ErrorCategory[]{UNCATEGORIZED, DUPLICATE_KEY, EXECUTION_TIMEOUT};
   }
}
