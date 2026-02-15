package dev.artixdev.libs.org.simpleyaml.configuration.comments.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.KeyTree;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class YamlHeaderFormatter implements CommentFormatter {
   protected YamlCommentFormatterConfiguration configuration;

   protected YamlHeaderFormatter(YamlCommentFormatterConfiguration configuration) {
      Validate.notNull(configuration);
      this.configuration = configuration;
   }

   public YamlHeaderFormatter(String commentPrefix, boolean strip) {
      this((new YamlCommentFormatterConfiguration()).prefix(commentPrefix).stripPrefix(strip).suffix("\n\n"));
   }

   public YamlHeaderFormatter() {
      this("# ", false);
   }

   public String parse(String raw, CommentType type, KeyTree.Node node) throws IOException {
      return raw == null ? null : this.parse((Reader)(new StringReader(raw)), type, node);
   }

   public String parse(Reader raw, CommentType type, KeyTree.Node node) throws IOException {
      if (raw == null) {
         return null;
      } else {
         BufferedReader reader = raw instanceof BufferedReader ? (BufferedReader)raw : new BufferedReader(raw);
         Throwable thrown = null;

         String result;
         try {
            StringBuilder headerBuilder = new StringBuilder();
            boolean headerFound = false;

            String line;
            while((line = reader.readLine()) != null) {
               String trim = line.trim();
               if (trim.isEmpty()) {
                  if (headerFound) {
                     break;
                  }

                  headerBuilder.append('\n');
               } else {
                  if (!trim.startsWith("#")) {
                     headerFound = false;
                     break;
                  }

                  if (this.stripPrefix()) {
                     line = StringUtils.stripPrefix(trim, this.commentPrefix(), "#");
                  }

                  if (headerFound) {
                     headerBuilder.append('\n');
                  } else {
                     headerFound = true;
                  }

                  headerBuilder.append(line);
               }
            }

            if (!headerFound) {
               return null;
            }

            result = headerBuilder.toString();
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

   public String dump(String header, CommentType type, KeyTree.Node node) {
      if (header == null) {
         return null;
      } else {
         String prefixFirst = null;
         String prefixMultiline = null;
         String suffixLast = null;
         String suffixMultiline = null;
         String suffixLastPrefix;
         if (!StringUtils.allLinesArePrefixed(header, "#")) {
            prefixMultiline = this.commentPrefix();
            prefixFirst = this.configuration.prefixFirst(prefixMultiline);
            suffixMultiline = this.configuration.suffixMultiline();
            if (!prefixFirst.equals(prefixMultiline)) {
               suffixLastPrefix = '\n' + prefixMultiline;
               if (!prefixFirst.endsWith(suffixLastPrefix)) {
                  prefixFirst = prefixFirst + suffixLastPrefix;
               }
            }
         }

         if (!header.endsWith(this.configuration.suffixLast())) {
            suffixLast = this.configuration.suffixLast();
         }

         if (suffixLast != null && suffixMultiline != null && !suffixMultiline.isEmpty()) {
            suffixLastPrefix = suffixMultiline + '\n';
            if (!suffixLast.startsWith(suffixLastPrefix)) {
               suffixLast = suffixLastPrefix + suffixLast;
            }
         }

         return CommentFormatter.format(prefixFirst, prefixMultiline, header, suffixMultiline, suffixLast);
      }
   }

   public boolean stripPrefix() {
      return this.configuration.stripPrefix();
   }

   public YamlHeaderFormatter stripPrefix(boolean stripPrefix) {
      this.configuration.stripPrefix(stripPrefix);
      return this;
   }

   public String commentPrefix() {
      return this.configuration.prefixMultiline("# ");
   }

   public YamlHeaderFormatter commentPrefix(String commentPrefix) {
      String prefixFirst = this.configuration.prefixFirst("# ");
      if (prefixFirst.equals("# ")) {
         prefixFirst = commentPrefix;
      }

      this.configuration.prefix(prefixFirst, commentPrefix);
      return this;
   }

   public YamlHeaderFormatter prefixFirst(String commentPrefixFirst) {
      this.configuration.prefix(commentPrefixFirst, this.commentPrefix());
      return this;
   }

   public YamlHeaderFormatter commentSuffix(String suffixMultiline) {
      this.configuration.suffix(this.configuration.suffixLast("\n\n"), suffixMultiline);
      return this;
   }

   public YamlHeaderFormatter suffixLast(String suffixLast) {
      if (suffixLast == null) {
         suffixLast = "\n\n";
      } else if (!suffixLast.endsWith("\n\n")) {
         suffixLast = suffixLast + "\n\n";
      }

      this.configuration.suffix(suffixLast);
      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         YamlHeaderFormatter that = (YamlHeaderFormatter)o;
         return Objects.equals(this.configuration, that.configuration);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.configuration});
   }
}
