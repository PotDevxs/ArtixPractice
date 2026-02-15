package dev.artixdev.libs.org.simpleyaml.configuration.comments.format;

import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.KeyTree;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;

public class BlankLineYamlCommentFormatter extends YamlCommentFormatter {
   public BlankLineYamlCommentFormatter() {
      this(new YamlCommentFormatterConfiguration());
   }

   public BlankLineYamlCommentFormatter(YamlCommentFormatterConfiguration blockFormatter) {
      this(blockFormatter, new YamlSideCommentFormatterConfiguration());
   }

   public BlankLineYamlCommentFormatter(YamlCommentFormatterConfiguration blockFormatter, YamlSideCommentFormatterConfiguration sideFormatter) {
      super(blockFormatter, sideFormatter);
      this.stripPrefix(true).trim(false);
      blockFormatter.prefix('\n' + blockFormatter.prefixFirst(), blockFormatter.prefixMultiline());
   }

   public String dump(String comment, CommentType type, KeyTree.Node node) {
      if (type == CommentType.SIDE) {
         String defaultPrefixFirst = this.sideFormatter.prefixFirst();
         String blankLineSideFirstPrefix = '\n' + StringUtils.stripIndentation(defaultPrefixFirst);
         this.sideFormatter.prefix(blankLineSideFirstPrefix, this.sideFormatter.prefixMultiline());
         String dump = super.dump(comment, type, node);
         this.sideFormatter.prefix(defaultPrefixFirst, this.sideFormatter.prefixMultiline());
         return dump;
      } else {
         return super.dump(comment, type, node);
      }
   }
}
