package dev.artixdev.libs.com.mongodb.internal.connection;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class DecimalFormatHelper {
   private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = initializeDecimalFormatSymbols();

   private DecimalFormatHelper() {
   }

   private static DecimalFormatSymbols initializeDecimalFormatSymbols() {
      DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
      decimalFormatSymbols.setDecimalSeparator('.');
      return decimalFormatSymbols;
   }

   public static String format(String pattern, double number) {
      return (new DecimalFormat(pattern, DECIMAL_FORMAT_SYMBOLS)).format(number);
   }
}
