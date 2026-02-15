package dev.artixdev.libs.org.simpleyaml.configuration.comments.format;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.KeyTree;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;

public interface CommentFormatter {
   String parse(Reader raw, CommentType type, KeyTree.Node node) throws IOException;

   String dump(String comment, CommentType type, KeyTree.Node node);

   default String parse(String raw, CommentType type, KeyTree.Node node) throws IOException {
      return this.parse((Reader)(new StringReader(raw)), type, node);
   }

   default String parse(Reader raw, CommentType type) throws IOException {
      return this.parse((Reader)raw, type, (KeyTree.Node)null);
   }

   default String parse(String raw, CommentType type) throws IOException {
      return this.parse((String)raw, type, (KeyTree.Node)null);
   }

   default String parse(Reader raw) throws IOException {
      return this.parse(raw, CommentType.BLOCK);
   }

   default String parse(String raw) throws IOException {
      return this.parse(raw, CommentType.BLOCK);
   }

   default String dump(String comment, CommentType type) {
      return this.dump(comment, type, (KeyTree.Node)null);
   }

   default String dump(String comment) {
      return this.dump(comment, CommentType.BLOCK);
   }

   static String format(int indent, String prefixFirst, String prefixMultiline, String comment, CommentType type, String suffixMultiline, String suffixLast) {
      if (comment == null) {
         return "";
      } else {
         Stream<String> stream = Arrays.stream(StringUtils.lines(comment, comment.trim().isEmpty()));
         String indentation = StringUtils.indentation(indent);
         String indentLine = "\n" + indentation;
         String delimiter;
         if (suffixMultiline == null) {
            delimiter = indentLine;
         } else {
            delimiter = String.join(indentLine, StringUtils.lines(suffixMultiline, false)) + indentLine;
         }

         if (prefixFirst == null) {
            prefixFirst = "";
         } else {
            prefixFirst = String.join(indentLine, StringUtils.lines(prefixFirst, false));
         }

         if (prefixMultiline == null) {
            prefixMultiline = prefixFirst;
         } else {
            prefixMultiline = String.join(indentLine, StringUtils.lines(prefixMultiline, false));
         }

         if (type == CommentType.BLOCK) {
            prefixFirst = indentation + prefixFirst;
         }

         delimiter = delimiter + prefixMultiline;
         if (suffixLast == null) {
            suffixLast = "";
         } else {
            suffixLast = String.join(indentLine, StringUtils.lines(suffixLast, false));
         }

         return (String)stream.collect(Collectors.joining(delimiter, prefixFirst, suffixLast));
      }
   }

   static String format(String prefixFirst, String prefixMultiline, String comment, String suffixMultiline, String suffixLast) {
      return format(0, prefixFirst, prefixMultiline, comment, CommentType.BLOCK, suffixMultiline, suffixLast);
   }

   static String format(int indent, String comment, CommentType type, CommentFormatterConfiguration configuration) {
      return format(indent, configuration.prefixFirst(), configuration.prefixMultiline(), comment, type, configuration.suffixMultiline(), configuration.suffixLast());
   }
}
