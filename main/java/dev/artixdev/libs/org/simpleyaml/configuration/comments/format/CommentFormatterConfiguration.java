package dev.artixdev.libs.org.simpleyaml.configuration.comments.format;

import java.util.Objects;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;

public class CommentFormatterConfiguration {
   private String prefixFirst;
   private String prefixMultiline;
   private String suffixMultiline;
   private String suffixLast;

   public CommentFormatterConfiguration prefix(String prefix) {
      return this.prefix(prefix, prefix);
   }

   public CommentFormatterConfiguration prefix(String prefixFirst, String prefixMultiline) {
      this.prefixFirst = prefixFirst;
      this.prefixMultiline = prefixMultiline;
      return this;
   }

   public CommentFormatterConfiguration suffix(String suffixLast) {
      this.suffixLast = suffixLast;
      return this;
   }

   public CommentFormatterConfiguration suffix(String suffixLast, String suffixMultiline) {
      this.suffixLast = suffixLast;
      this.suffixMultiline = suffixMultiline;
      return this;
   }

   public String prefixFirst(String defaultPrefix) {
      return this.prefixFirst != null ? this.prefixFirst : defaultPrefix;
   }

   public String prefixFirst() {
      return this.prefixFirst("");
   }

   public String prefixMultiline(String defaultPrefix) {
      return this.prefixMultiline != null ? this.prefixMultiline : this.prefixFirst(defaultPrefix);
   }

   public String prefixMultiline() {
      return this.prefixMultiline("");
   }

   public String suffixMultiline(String defaultSuffix) {
      return this.suffixMultiline != null ? this.suffixMultiline : defaultSuffix;
   }

   public String suffixMultiline() {
      return this.suffixMultiline("");
   }

   public String suffixLast(String defaultSuffix) {
      return this.suffixLast != null ? this.suffixLast : defaultSuffix;
   }

   public String suffixLast() {
      return this.suffixLast("");
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         CommentFormatterConfiguration that = (CommentFormatterConfiguration)o;
         return Objects.equals(this.prefixFirst, that.prefixFirst) && Objects.equals(this.prefixMultiline, that.prefixMultiline) && Objects.equals(this.suffixMultiline, that.suffixMultiline) && Objects.equals(this.suffixLast, that.suffixLast);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.prefixFirst, this.prefixMultiline, this.suffixMultiline, this.suffixLast});
   }

   public String toString() {
      return StringUtils.quoteNewLines("{prefixFirst='" + this.prefixFirst + '\'' + ", prefixMultiline='" + this.prefixMultiline + '\'' + ", suffixMultiline='" + this.suffixMultiline + '\'' + ", suffixLast='" + this.suffixLast + '\'' + '}');
   }
}
