package dev.artixdev.libs.org.simpleyaml.configuration.comments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfigurationOptions;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class YamlCommentParser extends YamlCommentReader {
   protected final BufferedReader reader;
   protected StringBuilder blockComment;
   protected boolean blockCommentStarted = false;
   protected boolean headerParsed = false;

   public YamlCommentParser(YamlConfigurationOptions options, Reader reader) {
      super(options);
      Validate.notNull(reader, "Reader is null!");
      this.reader = reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
   }

   public void parse() throws IOException {
      while(this.nextLine()) {
         this.processLine();
      }

      this.track();
      this.close();
   }

   protected String readLine() throws IOException {
      return this.reader.readLine();
   }

   protected void processLine() throws IOException {
      if (this.isBlank()) {
         this.appendLine();
      } else if (this.isComment()) {
         this.appendCommentLine();
      } else {
         this.track();
      }

   }

   protected void appendLine() {
      if (!this.isExplicit()) {
         if (this.blockComment == null) {
            this.blockComment = new StringBuilder();
         }

         this.blockComment.append('\n');
      }

   }

   protected void appendCommentLine() {
      this.trackSideCommentBelow();
      if (this.isExplicit()) {
         this.explicitNotation.addComment(this.currentLine);
      } else {
         if (this.blockComment == null) {
            this.blockComment = new StringBuilder(this.currentLine);
         } else {
            if (this.blockCommentStarted) {
               this.blockComment.append('\n');
            }

            this.blockComment.append(this.currentLine);
         }

         this.blockCommentStarted = true;
      }

   }

   protected KeyTree.Node track() throws IOException {
      this.trackSideCommentBelow();
      this.currentNode = super.track();
      this.trackBlockComment(this.currentNode);
      this.trackSideComment(this.currentNode);
      return this.currentNode;
   }

   protected void endExplicitBlock() throws IOException {
      this.trackBlockCommentExplicit(this.currentNode);
      this.trackSideComment(this.currentNode);
   }

   protected String trackBlockComment(KeyTree.Node node) {
      String blockComment = null;
      if (node != null && this.blockComment != null && (!this.isExplicit() || this.explicitNotation.getNode() == node)) {
         blockComment = this.blockComment.toString();
         if (!this.headerParsed) {
            blockComment = removeHeader(blockComment, this.options());
            this.headerParsed = true;
         }

         this.setRawComment(node, blockComment, CommentType.BLOCK);
         this.blockComment = null;
         this.blockCommentStarted = false;
      }

      return blockComment;
   }

   protected void trackBlockCommentExplicit(KeyTree.Node node) {
      String blockComment = this.trackBlockComment(node);
      String explicitBlockComment = this.explicitNotation.getKeyComment();
      if (explicitBlockComment != null) {
         if (blockComment == null) {
            blockComment = node.getComment();
         }

         if (blockComment == null) {
            blockComment = explicitBlockComment;
         } else {
            blockComment = blockComment + '\n' + explicitBlockComment;
         }

         this.setRawComment(node, blockComment, CommentType.BLOCK);
      }

   }

   public static String removeHeader(String blockComment, YamlConfigurationOptions options) {
      String header = options.headerFormatter().dump(options.header());
      if (header != null && !header.isEmpty()) {
         boolean headerWithBlankLine = header.endsWith("\n\n");
         if (headerWithBlankLine) {
            blockComment = blockComment + '\n';
         }

         blockComment = blockComment.replaceFirst(Pattern.quote(header), "");
         if (blockComment.isEmpty()) {
            blockComment = null;
         } else if (headerWithBlankLine) {
            blockComment = blockComment.substring(0, blockComment.length() - 1);
         }
      }

      return blockComment;
   }

   protected void trackSideComment(KeyTree.Node node) throws IOException {
      if (this.isExplicit()) {
         if (this.currentLine != null && !this.explicitNotation.isFinished()) {
            this.readValue();
            if (this.isComment() && this.isExplicit()) {
               String comment = this.currentLine.substring(this.position);
               if (node != null && node != this.explicitNotation.getNode()) {
                  this.setSideComment(node, comment);
               } else {
                  this.explicitNotation.addComment(comment);
               }
            }
         } else if (node != null) {
            this.setSideComment(node, this.explicitNotation.getValueComment());
         }
      } else if (this.currentLine != null && node != null) {
         this.readValue();
         if (this.isComment()) {
            this.setSideComment(node, this.currentLine.substring(this.position));
         }
      }

   }

   protected void setSideComment(KeyTree.Node node, String sideComment) {
      if (sideComment != null && !sideComment.isEmpty() && !isSpace(sideComment.charAt(0))) {
         sideComment = " " + sideComment;
      }

      this.setRawComment(node, sideComment, CommentType.SIDE);
   }

   protected void trackSideCommentBelow() {
      if (this.isSectionEnd()) {
         if (this.blockComment != null && this.blockCommentStarted) {
            String sideComment = this.getRawComment(this.currentNode, CommentType.SIDE);
            if (sideComment == null) {
               sideComment = "";
            }

            sideComment = sideComment + '\n';
            String[] split = StringUtils.splitTrailingNewLines(this.blockComment.toString());
            sideComment = sideComment + split[0];
            if (split[1].isEmpty()) {
               this.blockComment = null;
            } else {
               this.blockComment = new StringBuilder(split[1]);
            }

            this.blockCommentStarted = false;
            this.setRawComment(this.currentNode, sideComment, CommentType.SIDE);
         }

         this.clearCurrentNodeIfNoComments();
      }

   }

   protected void processMultiline(boolean inQuoteBlock) {
      if (this.isExplicit() && this.isComment()) {
         this.explicitNotation.addComment(this.currentLine.substring(this.position));
      }

   }

   public void close() throws IOException {
      this.reader.close();
   }
}
