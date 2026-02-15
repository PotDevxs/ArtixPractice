package dev.artixdev.libs.org.simpleyaml.configuration.comments.format;

import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class YamlSideCommentFormatterConfiguration extends YamlCommentFormatterConfiguration {
   public static final String DEFAULT_SIDE_COMMENT_PREFIX = " # ";

   public YamlSideCommentFormatterConfiguration() {
      this(" # ");
   }

   public YamlSideCommentFormatterConfiguration(String sidePrefix) {
      super(sidePrefix);
   }

   public YamlSideCommentFormatterConfiguration(String sidePrefix, String prefixMultiline) {
      super(sidePrefix, prefixMultiline);
   }

   public YamlSideCommentFormatterConfiguration prefix(String sidePrefix) {
      super.prefix(sidePrefix, "# ");
      return this;
   }

   public YamlSideCommentFormatterConfiguration prefix(String prefixFirst, String prefixMultiline) {
      super.prefix(prefixFirst, prefixMultiline);
      return this;
   }

   public YamlSideCommentFormatterConfiguration suffix(String suffixLast) {
      super.suffix(suffixLast);
      return this;
   }

   public YamlSideCommentFormatterConfiguration suffix(String suffixLast, String suffixMultiline) {
      super.suffix(suffixLast, suffixMultiline);
      return this;
   }

   public YamlSideCommentFormatterConfiguration trim(boolean trim) {
      super.trim(trim);
      return this;
   }

   public YamlSideCommentFormatterConfiguration stripPrefix(boolean stripPrefix) {
      super.stripPrefix(stripPrefix);
      return this;
   }

   protected void checkCommentPrefix(String sidePrefix) {
      Validate.isTrue(sidePrefix != null && !sidePrefix.isEmpty() && Character.isWhitespace(sidePrefix.charAt(0)), "Side comment prefix must start with space");
      super.checkCommentPrefix(sidePrefix);
   }

   protected void checkCommentPrefixMultiline(String commentPrefix) {
      super.checkCommentPrefix(commentPrefix);
   }
}
