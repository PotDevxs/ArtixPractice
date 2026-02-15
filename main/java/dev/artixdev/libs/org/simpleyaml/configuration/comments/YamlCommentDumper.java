package dev.artixdev.libs.org.simpleyaml.configuration.comments;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import dev.artixdev.libs.org.simpleyaml.utils.DumperBus;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;

public class YamlCommentDumper extends YamlCommentReader {
   protected final YamlCommentMapper yamlCommentMapper;
   protected final DumperBus bus;
   protected BufferedWriter writer;
   protected StringWriter explicitBlock;
   protected KeyTree.Node commentNode;
   protected KeyTree.Node commentNodeFallback;
   protected KeyTree.Node firstListMapElement;

   public YamlCommentDumper(YamlCommentMapper yamlCommentMapper, DumperBus.Dumper source, Writer writer) {
      super(yamlCommentMapper.options());
      this.yamlCommentMapper = yamlCommentMapper;
      this.writer = writer instanceof BufferedWriter ? (BufferedWriter)writer : new BufferedWriter(writer);
      this.bus = new DumperBus(source);
   }

   public void dump() throws IOException {
      this.bus.dump();

      while(this.nextLine()) {
         this.processLine();
         this.writer.newLine();
      }

      this.clearSection();
      this.commentNode = this.getNode((String)null);
      this.appendBlockComment();
      this.close();
   }

   protected String readLine() throws IOException {
      return this.bus.await();
   }

   protected void processLine() throws IOException {
      this.clearSection();
      this.getCommentNode(this.track());
      this.appendBlockComment();
      this.writer.write(this.currentLine);
      this.appendSideComment();
   }

   protected void clearSection() {
      this.commentNode = this.commentNodeFallback = this.firstListMapElement = null;
      if (this.isSectionEnd()) {
         this.clearCurrentNode();
      }

   }

   public void getCommentNode(KeyTree.Node readerNode) {
      if (readerNode != null) {
         this.commentNode = this.getNode(readerNode.getPath());
         if (this.commentNode != null && this.commentNode.parent != null && this.commentNode.parent.isList && this.commentNode.size() == 1) {
            this.checkFirstListMapElement(this.commentNode, readerNode);
         }

         if ((this.commentNode == null || readerNode.name != null && (this.commentNode.comment == null || this.commentNode.sideComment == null)) && readerNode.parent != null && readerNode.parent.isList && readerNode.elementIndex != null) {
            this.commentNodeFallback = this.getNode(readerNode.getPathWithName());
         }
      }

   }

   protected void checkFirstListMapElement(KeyTree.Node commentNode, KeyTree.Node readerNode) {
      KeyTree.Node child = commentNode.getFirst();
      Integer elementIndex = child.getElementIndex();
      if (elementIndex == null) {
         String childName = child.getName();
         if (childName != null && childName.equals(readerNode.getName())) {
            this.firstListMapElement = child;
         }
      } else if (elementIndex == 0) {
         this.firstListMapElement = child;
      }

   }

   public KeyTree.Node getNode(String path) {
      return this.yamlCommentMapper.getPriorityNode(path);
   }

   protected void appendBlockComment() throws IOException {
      String blockComment = null;
      if (this.commentNode != null) {
         blockComment = this.commentNode.getComment();
      }

      if (blockComment == null && this.commentNodeFallback != null) {
         blockComment = this.commentNodeFallback.getComment();
      }

      this.appendBlockComment(blockComment);
      if (this.firstListMapElement != null) {
         this.appendBlockComment(this.firstListMapElement.getComment());
      }

      if (this.explicitBlock != null) {
         this.writer.write(this.explicitBlock.toString());
         this.writer.newLine();
         this.explicitBlock = null;
      }

   }

   protected void appendBlockComment(String comment) throws IOException {
      if (comment != null) {
         this.writer.write(comment);
         if (!comment.endsWith("\n")) {
            this.writer.newLine();
         }
      }

   }

   protected void appendSideComment() throws IOException {
      String sideComment = null;
      if (this.commentNode != null) {
         sideComment = this.commentNode.getSideComment();
      }

      if (sideComment == null && this.firstListMapElement != null) {
         sideComment = this.firstListMapElement.getSideComment();
      }

      if (sideComment == null && this.commentNodeFallback != null) {
         sideComment = this.commentNodeFallback.getSideComment();
      }

      this.readValue();
      if (sideComment != null && !sideComment.isEmpty()) {
         if (this.isLiteral) {
            this.appendSideCommentLiteral(sideComment);
         } else {
            this.writer.write(sideComment);
         }
      }

   }

   protected void appendSideCommentLiteral(String sideComment) throws IOException {
      String[] sideCommentParts = StringUtils.splitNewLines(sideComment, 2);
      this.writer.write(sideCommentParts[0]);
      if (sideCommentParts.length > 1 && this.nextLine()) {
         this.writer.newLine();
         this.writer.write(this.currentLine);

         while(this.nextLine() && this.isLiteral) {
            this.writer.newLine();
            this.writer.write(this.currentLine);
         }

         this.writer.newLine();
         this.writer.write(sideCommentParts[1]);
         if (this.stage != YamlCommentReader.ReaderStage.END_OF_FILE) {
            this.writer.newLine();
            this.processLine();
         }
      }

   }

   protected void readValue() throws IOException {
      if (this.hasChar()) {
         this.stage = YamlCommentReader.ReaderStage.VALUE;
         if (this.isInQuote() && !this.isLiteral) {
            this.skipMultiline();
         } else {
            this.skipToEnd();
         }
      }

   }

   protected void skipMultiline() throws IOException {
      for(boolean hasChar = this.hasChar() && this.nextChar(); hasChar; hasChar = this.nextChar()) {
      }

      if (this.isMultiline()) {
         this.readValueMultiline();
      }

   }

   protected void processMultiline(boolean inQuoteBlock) throws IOException {
      Object writer;
      if (this.isExplicit()) {
         if (this.explicitBlock == null) {
            this.explicitBlock = new StringWriter();
         }

         writer = this.explicitBlock;
      } else {
         writer = this.writer;
      }

      if (this.isLiteral && this.quoteNotation != YamlCommentReader.ReadingQuoteStyle.LITERAL) {
         ((Writer)writer).write(this.currentLine);
      } else {
         ((Writer)writer).write(System.lineSeparator());
         if (inQuoteBlock) {
            ((Writer)writer).write(this.currentLine);
         }
      }

   }

   public void close() throws IOException {
      this.writer.close();
   }
}
