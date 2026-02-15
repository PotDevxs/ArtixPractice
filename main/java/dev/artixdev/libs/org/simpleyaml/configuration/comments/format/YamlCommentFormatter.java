package dev.artixdev.libs.org.simpleyaml.configuration.comments.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.KeyTree;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class YamlCommentFormatter implements CommentFormatter {
   protected final YamlCommentFormatterConfiguration blockFormatter;
   protected final YamlSideCommentFormatterConfiguration sideFormatter;

   public YamlCommentFormatter(YamlCommentFormatterConfiguration blockFormatter, YamlSideCommentFormatterConfiguration sideFormatter) {
      Validate.notNull(blockFormatter, "blockFormatter configuration cannot be null!");
      Validate.notNull(blockFormatter, "sideFormatter configuration cannot be null!");
      this.blockFormatter = blockFormatter;
      this.sideFormatter = sideFormatter;
   }

   public YamlCommentFormatter(YamlCommentFormatterConfiguration blockFormatter) {
      this(blockFormatter, new YamlSideCommentFormatterConfiguration());
      this.stripPrefix(true);
   }

   public YamlCommentFormatter() {
      this(new YamlCommentFormatterConfiguration());
   }

   public String parse(Reader raw, CommentType type, KeyTree.Node node) throws IOException {
      if (raw == null) {
         return null;
      } else {
         YamlCommentFormatterConfiguration formatterConfiguration = this.formatterConfiguration(type);
         String prefixFirst = StringUtils.stripIndentation(formatterConfiguration.prefixFirst());
         String prefixMultiline = StringUtils.stripIndentation(formatterConfiguration.prefixMultiline());
         BufferedReader reader = raw instanceof BufferedReader ? (BufferedReader)raw : new BufferedReader(raw);
         Throwable thrown = null;

         String result;
         try {
            StringBuilder commentBuilder = new StringBuilder();
            boolean strip = formatterConfiguration.stripPrefix();
            String firstLine = reader.readLine();
            if (firstLine != null) {
               commentBuilder.append(this.parseCommentLine(firstLine, prefixFirst, strip));
            }

            String line;
            while((line = reader.readLine()) != null) {
               commentBuilder.append('\n').append(this.parseCommentLine(line, prefixMultiline, strip));
            }

            String comment = commentBuilder.toString();
            result = formatterConfiguration.trim() ? comment.trim() : comment;
         } catch (Throwable t) {
            thrown = t;
            throw t;
         } finally {
            if (reader != null) {
               if (thrown != null) {
                  try {
                     reader.close();
                  } catch (Throwable closeException) {
                     thrown.addSuppressed(closeException);
                  }
               } else {
                  reader.close();
               }
            }

         }

         return result;
      }
   }

   protected String parseCommentLine(String line, String prefix, boolean strip) {
      String commentLine = StringUtils.stripIndentation(line);
      if (strip) {
         commentLine = StringUtils.stripPrefix(commentLine, prefix, "#");
      }

      return commentLine;
   }

   public String dump(String comment, CommentType type, KeyTree.Node node) {
      YamlCommentFormatterConfiguration formatterConfiguration = this.formatterConfiguration(type);
      String prefix = null;
      String prefixMultiline = null;
      if (comment != null) {
         if (StringUtils.allLinesArePrefixedOrBlank(comment, "#")) {
            if (type == CommentType.SIDE && !comment.startsWith(" ")) {
               prefix = " ";
               prefixMultiline = "";
            }
         } else {
            prefix = formatterConfiguration.prefixFirst();
            prefixMultiline = formatterConfiguration.prefixMultiline();
         }
      }

      return CommentFormatter.format(node.getIndentation(), prefix, prefixMultiline, comment, type, formatterConfiguration.suffixMultiline(), formatterConfiguration.suffixLast());
   }

   public final YamlCommentFormatterConfiguration blockFormatter() {
      return this.blockFormatter;
   }

   public final YamlSideCommentFormatterConfiguration sideFormatter() {
      return this.sideFormatter;
   }

   public final YamlCommentFormatterConfiguration formatterConfiguration(CommentType type) {
      return (YamlCommentFormatterConfiguration)(type == CommentType.BLOCK ? this.blockFormatter : this.sideFormatter);
   }

   public YamlCommentFormatter stripPrefix(boolean strip) {
      this.blockFormatter.stripPrefix(strip);
      this.sideFormatter.stripPrefix(strip);
      return this;
   }

   public YamlCommentFormatter trim(boolean trim) {
      this.blockFormatter.trim(trim);
      this.sideFormatter.trim(trim);
      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         YamlCommentFormatter that = (YamlCommentFormatter)o;
         return Objects.equals(this.blockFormatter, that.blockFormatter) && Objects.equals(this.sideFormatter, that.sideFormatter);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.blockFormatter, this.sideFormatter});
   }

   public String toString() {
      return this.getClass().getSimpleName() + "{\nblockFormatter=" + this.blockFormatter + ",\nsideFormatter=" + this.sideFormatter + "\n}";
   }
}
