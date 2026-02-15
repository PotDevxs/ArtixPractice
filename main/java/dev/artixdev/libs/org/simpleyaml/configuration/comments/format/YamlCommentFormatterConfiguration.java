package dev.artixdev.libs.org.simpleyaml.configuration.comments.format;

import java.util.Objects;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class YamlCommentFormatterConfiguration extends CommentFormatterConfiguration {
   public static final String COMMENT_INDICATOR = "#";
   public static final String DEFAULT_COMMENT_PREFIX = "# ";
   private boolean stripPrefix;
   private boolean trim;

   public YamlCommentFormatterConfiguration() {
      this("# ");
   }

   public YamlCommentFormatterConfiguration(String prefix) {
      this.stripPrefix = false;
      this.trim = true;
      this.prefix(prefix);
   }

   public YamlCommentFormatterConfiguration(String prefix, String prefixMultiline) {
      this.stripPrefix = false;
      this.trim = true;
      this.prefix(prefix, prefixMultiline);
   }

   public YamlCommentFormatterConfiguration prefix(String prefix) {
      this.checkCommentPrefix(prefix);
      super.prefix(prefix, prefix);
      return this;
   }

   public YamlCommentFormatterConfiguration prefix(String prefixFirst, String prefixMultiline) {
      this.checkCommentPrefix(prefixFirst);
      this.checkCommentPrefixMultiline(prefixMultiline);
      super.prefix(prefixFirst, prefixMultiline);
      return this;
   }

   public YamlCommentFormatterConfiguration suffix(String suffixLast) {
      this.checkCommentSuffix(suffixLast);
      super.suffix(suffixLast);
      return this;
   }

   public YamlCommentFormatterConfiguration suffix(String suffixLast, String suffixMultiline) {
      this.checkCommentSuffix(suffixLast);
      this.checkCommentSuffixMultiline(suffixMultiline);
      super.suffix(suffixLast, suffixMultiline);
      return this;
   }

   public YamlCommentFormatterConfiguration stripPrefix(boolean stripPrefix) {
      this.stripPrefix = stripPrefix;
      return this;
   }

   public boolean stripPrefix() {
      return this.stripPrefix;
   }

   public YamlCommentFormatterConfiguration trim(boolean trim) {
      this.trim = trim;
      return this;
   }

   public boolean trim() {
      return this.trim;
   }

   protected void checkCommentPrefix(String commentPrefix) {
      Validate.notNull(commentPrefix, "Comment prefix cannot be null");
      String[] prefixLines = StringUtils.lines(commentPrefix, false);
      int lastLineIndex = prefixLines.length - 1;

      for(int i = 0; i <= lastLineIndex; ++i) {
         String line = prefixLines[i].trim();
         if (i == lastLineIndex && !line.startsWith("#")) {
            throw new IllegalArgumentException("Last prefix line must be optional space followed by a #");
         }

         if (!line.isEmpty() && !line.startsWith("#")) {
            throw new IllegalArgumentException("All comment prefix lines must be blank or optional space followed by a #");
         }
      }

   }

   protected void checkCommentPrefixMultiline(String commentPrefix) {
      this.checkCommentPrefix(commentPrefix);
   }

   protected void checkCommentSuffix(String commentSuffix) {
      Validate.notNull(commentSuffix, "Comment suffix cannot be null");
      Validate.isTrue(StringUtils.allLinesArePrefixedOrBlank(StringUtils.afterNewLine(commentSuffix), "#"), "All comment suffix lines must be blank or optional space followed by a #");
   }

   protected void checkCommentSuffixMultiline(String commentSuffix) {
      this.checkCommentSuffix(commentSuffix);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            YamlCommentFormatterConfiguration that = (YamlCommentFormatterConfiguration)o;
            return this.stripPrefix == that.stripPrefix && this.trim == that.trim;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{super.hashCode(), this.stripPrefix, this.trim});
   }

   public String toString() {
      return "{stripPrefix=" + this.stripPrefix + ", trim=" + this.trim + ", " + super.toString() + '}';
   }
}
