package dev.artixdev.libs.org.simpleyaml.configuration.implementation.api;

import java.util.Objects;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;

public class QuoteValue<T> {
   protected final T value;
   protected final QuoteStyle quoteStyle;

   public QuoteValue(T value, QuoteStyle quoteStyle) {
      this.value = value;
      this.quoteStyle = quoteStyle;
   }

   public T getValue() {
      return this.value;
   }

   public QuoteStyle getQuoteStyle() {
      return this.quoteStyle;
   }

   public String toString() {
      return this.quoteStyle.toString() + "=" + (this.value == null ? "!!null" : StringUtils.quoteNewLines(this.value.toString()));
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         QuoteValue<?> that = (QuoteValue)o;
         return Objects.equals(this.value, that.value) && this.quoteStyle == that.quoteStyle;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.value, this.quoteStyle});
   }
}
