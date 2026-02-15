package dev.artixdev.libs.org.simpleyaml.configuration.comments.format;

import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.KeyTree;

public class PrettyYamlCommentFormatter extends YamlCommentFormatter {
   public PrettyYamlCommentFormatter() {
      this(new YamlCommentFormatterConfiguration());
   }

   public PrettyYamlCommentFormatter(YamlCommentFormatterConfiguration blockFormatter) {
      this(blockFormatter, new YamlSideCommentFormatterConfiguration());
   }

   public PrettyYamlCommentFormatter(YamlCommentFormatterConfiguration blockFormatter, YamlSideCommentFormatterConfiguration sideFormatter) {
      super(blockFormatter, sideFormatter);
      this.stripPrefix(true).trim(true);
   }

   public String dump(String comment, CommentType type, KeyTree.Node node) {
      if (type == CommentType.BLOCK && node != null && node.getIndentation() == 0 && !node.isFirstNode()) {
         YamlCommentFormatterConfiguration blockCommentFormatterConfiguration = this.formatterConfiguration(CommentType.BLOCK);
         String defaultPrefixFirst = blockCommentFormatterConfiguration.prefixFirst();
         String defaultPrefixMultiline = blockCommentFormatterConfiguration.prefixMultiline();
         blockCommentFormatterConfiguration.prefix('\n' + defaultPrefixFirst, defaultPrefixMultiline);
         String dump = super.dump(comment, type, node);
         blockCommentFormatterConfiguration.prefix(defaultPrefixFirst, defaultPrefixMultiline);
         return dump;
      } else {
         return super.dump(comment, type, node);
      }
   }
}
