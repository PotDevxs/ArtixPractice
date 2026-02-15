package dev.artixdev.libs.org.simpleyaml.configuration.comments;

import java.io.Closeable;
import java.io.IOException;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfigurationOptions;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;

public abstract class YamlCommentReader extends YamlCommentMapper implements Closeable {
   protected String currentLine;
   protected String trim;
   protected int indent;
   protected String key;
   protected int position = -1;
   protected char currentChar;
   protected boolean isEscaping = false;
   protected boolean isLiteral = false;
   protected YamlCommentReader.ReadingQuoteStyle quoteNotation;
   protected boolean beginExplicit;
   protected YamlCommentReader.ReadingExplicitStyle explicitNotation;
   protected KeyTree.Node currentNode;
   protected KeyTree.Node currentList;
   protected boolean isListElement;
   protected YamlCommentReader.ReaderStage stage;

   protected YamlCommentReader(YamlConfigurationOptions options) {
      super(options);
      this.quoteNotation = YamlCommentReader.ReadingQuoteStyle.NONE;
      this.beginExplicit = false;
      this.isListElement = false;
      this.stage = YamlCommentReader.ReaderStage.START;
   }

   protected abstract String readLine() throws IOException;

   protected synchronized boolean nextLine() throws IOException {
      this.currentLine = this.readLine();
      this.position = -1;
      this.currentChar = 0;
      this.isListElement = false;
      if (this.currentLine != null) {
         this.stage = YamlCommentReader.ReaderStage.NEW_LINE;
         boolean literal = this.isLiteral;
         int indent = this.readIndent();
         this.trim = this.currentLine.substring(indent).trim();
         this.checkSpecialLines(literal, indent);
         return true;
      } else {
         this.indent = 0;
         this.trim = null;
         this.stage = YamlCommentReader.ReaderStage.END_OF_FILE;
         this.endExplicitNotation();
         return false;
      }
   }

   protected boolean nextChar() {
      if (this.hasNext()) {
         ++this.position;
         this.currentChar = this.currentLine.charAt(this.position);
         return this.checkSpecialChars();
      } else {
         this.stage = YamlCommentReader.ReaderStage.END_OF_LINE;
         return false;
      }
   }

   protected boolean hasChar() {
      return this.stage != YamlCommentReader.ReaderStage.END_OF_LINE && this.stage != YamlCommentReader.ReaderStage.END_OF_FILE;
   }

   protected boolean hasNext() {
      return this.position + 1 < this.currentLine.length();
   }

   protected char peek(int offset) {
      return this.currentLine.charAt(this.position + offset);
   }

   protected boolean isBlank() {
      return this.trim.isEmpty();
   }

   public static boolean isSpace(char c) {
      return c == ' ' || c == '\t';
   }

   protected boolean isComment() {
      if (this.stage == YamlCommentReader.ReaderStage.COMMENT) {
         return true;
      } else if (this.currentChar == '#' && this.canStartComment()) {
         this.stage = YamlCommentReader.ReaderStage.COMMENT;
         return true;
      } else {
         return false;
      }
   }

   protected boolean canStartComment() {
      return this.position == 0 || this.stage == YamlCommentReader.ReaderStage.QUOTE_CLOSE || !this.isInQuote() && this.position > 0 && isSpace(this.peek(-1));
   }

   protected boolean isInQuote() {
      return this.quoteNotation != YamlCommentReader.ReadingQuoteStyle.NONE;
   }

   protected boolean isExplicit() {
      return this.explicitNotation != null;
   }

   protected void endExplicitNotation() throws IOException {
      if (this.isExplicit()) {
         this.explicitNotation.finish();
         this.currentNode = this.explicitNotation.getNode();
         this.endExplicitBlock();
      }

      this.explicitNotation = null;
   }

   protected void processLine() throws IOException {
   }

   protected void processMultiline(boolean inQuoteBlock) throws IOException {
   }

   protected void endExplicitBlock() throws IOException {
   }

   protected boolean isLiteralChar() {
      return this.currentChar == '|' || this.currentChar == '>';
   }

   protected void checkSpecialLines(boolean wasLiteral, int indent) throws IOException {
      if (wasLiteral && this.isLiteral) {
         if (this.quoteNotation != YamlCommentReader.ReadingQuoteStyle.LITERAL) {
            this.quoteNotation = YamlCommentReader.ReadingQuoteStyle.LITERAL;
         } else if (indent <= this.indent) {
            this.quoteNotation = YamlCommentReader.ReadingQuoteStyle.NONE;
            this.isLiteral = false;
            this.indent = indent;
            this.checkSpecialChars();
         }
      } else {
         this.indent = indent;
      }

      if (this.beginExplicit) {
         this.beginExplicit = false;
         this.endExplicitNotation();
         this.explicitNotation = new YamlCommentReader.ReadingExplicitStyle(indent);
      } else if (this.isExplicit() && !this.isBlank() && indent <= this.explicitNotation.getIndentation() && this.trim.charAt(0) != ':') {
         this.endExplicitNotation();
      }

      if (this.currentList != null) {
         int currentListIndent = this.currentList.getIndentation();
         if (indent < currentListIndent || !this.isListElement && indent == currentListIndent) {
            this.currentList = null;
         }
      }

   }

   protected boolean checkSpecialChars() {
      if (this.quoteNotation == YamlCommentReader.ReadingQuoteStyle.NONE) {
         if (!this.isLiteral && (this.stage == YamlCommentReader.ReaderStage.NEW_LINE || this.stage == YamlCommentReader.ReaderStage.AFTER_KEY)) {
            if (this.currentChar == YamlCommentReader.ReadingQuoteStyle.SINGLE.getChar()) {
               this.inQuote(YamlCommentReader.ReadingQuoteStyle.SINGLE);
               return this.nextChar();
            }

            if (this.currentChar == YamlCommentReader.ReadingQuoteStyle.DOUBLE.getChar()) {
               this.inQuote(YamlCommentReader.ReadingQuoteStyle.DOUBLE);
               return this.nextChar();
            }

            if (this.isLiteralChar()) {
               this.isLiteral = true;
            }
         }
      } else if (this.quoteNotation == YamlCommentReader.ReadingQuoteStyle.SINGLE) {
         if (!this.isEscaping) {
            if (this.currentChar == this.quoteNotation.getChar()) {
               this.isEscaping = true;
               boolean hasChar = this.nextChar();
               if (!hasChar || this.currentChar != this.quoteNotation.getChar()) {
                  this.inQuote(YamlCommentReader.ReadingQuoteStyle.NONE);
                  this.isEscaping = false;
               }

               return hasChar;
            }
         } else {
            this.isEscaping = false;
         }
      } else if (this.quoteNotation == YamlCommentReader.ReadingQuoteStyle.DOUBLE) {
         if (!this.isEscaping) {
            if (this.currentChar == this.quoteNotation.getChar()) {
               this.inQuote(YamlCommentReader.ReadingQuoteStyle.NONE);
               return this.nextChar();
            }

            if (this.currentChar == '\\') {
               this.isEscaping = true;
               return this.nextChar();
            }
         } else {
            this.isEscaping = false;
         }
      }

      return true;
   }

   protected void inQuote(YamlCommentReader.ReadingQuoteStyle quoteStyle) {
      this.quoteNotation = quoteStyle;
      if (this.hasChar()) {
         if (quoteStyle == YamlCommentReader.ReadingQuoteStyle.NONE) {
            this.stage = YamlCommentReader.ReaderStage.QUOTE_CLOSE;
         } else {
            this.stage = YamlCommentReader.ReaderStage.QUOTE_OPEN;
         }
      }

   }

   protected boolean isSectionKey() {
      if (this.currentChar != ':' || this.stage != YamlCommentReader.ReaderStage.KEY && this.stage != YamlCommentReader.ReaderStage.QUOTE_CLOSE) {
         return false;
      } else if (this.hasNext()) {
         if (isSpace(this.peek(1))) {
            this.nextChar();
            this.stage = YamlCommentReader.ReaderStage.AFTER_KEY;
            this.readTag();
            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   protected void readTag() {
      this.readIndent(false);
      if (this.hasChar() && this.currentChar == '!' && this.hasNext() && this.peek(1) == '!') {
         this.nextChar();

         while(this.nextChar() && !isSpace(this.currentChar)) {
         }

         this.readIndent(false);
      }

   }

   protected int readIndent(boolean special) {
      int indent;
      for(indent = 0; this.nextChar() && this.stage != YamlCommentReader.ReaderStage.QUOTE_OPEN; ++indent) {
         if (!isSpace(this.currentChar)) {
            if (special && this.canStartSpecialIndent(indent) && (this.isListChar() || this.isExplicitChar())) {
               this.nextChar();
               this.readIndent(!this.isListElement);
            }
            break;
         }
      }

      return indent;
   }

   protected int readIndent() {
      return this.readIndent(true);
   }

   protected boolean canStartSpecialIndent(int indent) {
      return this.stage == YamlCommentReader.ReaderStage.NEW_LINE && (this.quoteNotation == YamlCommentReader.ReadingQuoteStyle.NONE || this.quoteNotation == YamlCommentReader.ReadingQuoteStyle.LITERAL && indent <= this.indent);
   }

   protected boolean isListChar() {
      if (this.currentChar == '-') {
         this.isListElement = this.nextIsSpace();
         return this.isListElement;
      } else {
         return false;
      }
   }

   protected boolean isExplicitChar() {
      if (this.currentChar == '?' && this.nextIsSpace()) {
         this.beginExplicit = true;
         return true;
      } else if (this.isExplicit() && this.currentChar == ':' && this.nextIsSpace()) {
         this.explicitNotation.valueStep();
         return true;
      } else {
         return false;
      }
   }

   protected final boolean nextIsSpace() {
      return this.hasNext() && isSpace(this.peek(1));
   }

   protected final boolean isMultiline() {
      return !this.hasChar() && this.isInQuote();
   }

   protected boolean hasKey() {
      if (!this.isExplicit()) {
         return !this.isLiteral;
      } else {
         return this.explicitNotation.isKey() || this.explicitNotation.isListKey;
      }
   }

   protected String readKey() throws IOException {
      String key = null;
      boolean hasChar = this.hasChar();
      if (hasChar && this.hasKey()) {
         StringBuilder keyBuilder = new StringBuilder();
         boolean withinQuotes = this.isInQuote();
         this.stage = YamlCommentReader.ReaderStage.KEY;
         boolean explicitLiteral = this.isLiteral && this.isExplicit();
         if (this.quoteNotation == YamlCommentReader.ReadingQuoteStyle.LITERAL) {
            keyBuilder.append(this.currentLine.substring(this.position));
            this.skipToEnd();
         } else if (explicitLiteral) {
            if (this.isLiteralChar()) {
               this.nextChar();
            }

            this.stage = YamlCommentReader.ReaderStage.COMMENT;
            this.processMultiline(true);
            this.skipToEnd();
         } else {
            while(hasChar && !this.isSectionKey() && this.stage != YamlCommentReader.ReaderStage.QUOTE_CLOSE && !this.isComment()) {
               keyBuilder.append(this.currentChar);
               hasChar = this.nextChar();
            }
         }

         if (explicitLiteral || this.isMultiline()) {
            this.readKeyMultiline(keyBuilder);
         }

         key = keyBuilder.toString();
         if (!withinQuotes) {
            key = key.trim();
         }
      }

      return key;
   }

   protected void readKeyMultiline(StringBuilder keyBuilder) throws IOException {
      YamlCommentReader.ReadingQuoteStyle lastQuote = this.quoteNotation;
      if (this.nextLine() && (!this.isExplicit() || this.explicitNotation.isKey())) {
         boolean inQuoteBlock = lastQuote != YamlCommentReader.ReadingQuoteStyle.LITERAL || this.quoteNotation == lastQuote;
         this.processMultiline(inQuoteBlock);
         if (inQuoteBlock) {
            String nextKey = this.readKey();
            if (nextKey != null) {
               if (keyBuilder.length() > 0) {
                  keyBuilder.append(' ');
               }

               keyBuilder.append(nextKey);
            }
         } else {
            this.processLine();
         }
      }

   }

   protected void readValue() throws IOException {
      boolean hasChar = this.hasChar();
      if (hasChar) {
         if (this.stage != YamlCommentReader.ReaderStage.QUOTE_CLOSE) {
            this.stage = YamlCommentReader.ReaderStage.VALUE;
         }

         if (this.quoteNotation == YamlCommentReader.ReadingQuoteStyle.LITERAL) {
            this.skipToEnd();
         } else if (!this.isComment()) {
            while(hasChar && !this.isComment()) {
               hasChar = this.nextChar();
            }
         }
      }

      if (this.isMultiline()) {
         this.readValueMultiline();
      }

      if (this.isComment()) {
         while(this.position > 0 && isSpace(this.peek(-1))) {
            --this.position;
         }
      }

   }

   protected void readValueMultiline() throws IOException {
      YamlCommentReader.ReadingQuoteStyle lastQuote = this.quoteNotation;
      if (this.nextLine() && (!this.isExplicit() || this.explicitNotation.isValue())) {
         boolean inQuoteBlock = lastQuote != YamlCommentReader.ReadingQuoteStyle.LITERAL || this.quoteNotation == lastQuote;
         this.processMultiline(inQuoteBlock);
         if (inQuoteBlock) {
            this.readValue();
         } else {
            this.processLine();
         }
      }

   }

   protected void skipToEnd() {
      this.position = this.currentLine.length() - 1;
      this.currentChar = this.peek(0);
      this.nextChar();
   }

   protected boolean isSectionEnd() {
      return this.currentNode != null && this.indent <= this.currentNode.getIndentation() - this.options().indent();
   }

   protected KeyTree.Node track() throws IOException {
      if (this.quoteNotation == YamlCommentReader.ReadingQuoteStyle.LITERAL) {
         return null;
      } else if (this.isExplicit()) {
         return this.trackExplicit();
      } else {
         this.key = this.readKey();
         if (this.isListElement) {
            this.trackListElement();
         } else {
            this.track(this.indent, this.key);
         }

         return this.currentNode;
      }
   }

   protected void trackListElement() {
      if (this.currentList == null || this.currentNode != null && this.indent > this.currentNode.indent) {
         this.currentList = this.keyTree.findParent(this.indent + 2);
         if (this.currentList.listSize == null || this.currentList.size() == 0) {
            this.currentList.isList(0);
         }
      }

      if (this.isExplicit() && this.indent == this.explicitNotation.getIndentation()) {
         this.currentNode = this.currentList.add(this.key);
      } else {
         this.currentNode = this.currentList.add(this.indent, this.key);
      }

      this.currentList.isList(this.currentList.listSize + 1);
      this.currentNode.setElementIndex(this.currentList.listSize - 1);
   }

   protected KeyTree.Node trackExplicit() throws IOException {
      boolean addKey = this.explicitNotation.isKey();
      boolean isListKey = addKey && this.isListElement;
      this.key = this.readKey();
      if (addKey) {
         this.explicitNotation.addKey(this.key, isListKey);
         if (this.explicitNotation.isKey()) {
            return null;
         }
      }

      this.currentNode = this.explicitNotation.track();
      if (!addKey && this.isListElement) {
         this.trackListElement();
      }

      return this.currentNode;
   }

   protected KeyTree.Node track(int indent, String key) {
      KeyTree.Node parent = this.keyTree.findParent(indent);
      this.currentNode = parent.add(indent, key);
      return this.currentNode;
   }

   protected void clearCurrentNode() {
      super.clearNode(this.currentNode);
      this.currentNode = null;
   }

   protected void clearCurrentNodeIfNoComments() {
      super.clearNodeIfNoComments(this.currentNode);
      this.currentNode = null;
   }

   public String toString() {
      return "YamlCommentReader{currentLine='" + this.currentLine + '\'' + ", trim='" + this.trim + '\'' + ", stage=" + this.stage + ", indent=" + this.indent + ", key='" + this.key + '\'' + ", position=" + this.position + ", currentChar=" + this.currentChar + ", isEscaping=" + this.isEscaping + ", isLiteral=" + this.isLiteral + ", quoteNotation=" + this.quoteNotation + ", explicit= " + this.explicitNotation + ", keyTree=" + this.keyTree + '}';
   }

   public final class ReadingExplicitStyle {
      public static final char KEY = '?';
      public static final char VALUE = ':';
      private char step = '?';
      private final int indent;
      private StringBuilder key;
      private StringBuilder keyComment;
      private StringBuilder valueComment;
      private KeyTree.Node node;
      private boolean finished = false;
      private boolean isListKey = false;

      ReadingExplicitStyle(int indent) {
         this.indent = indent;
      }

      public boolean isKey() {
         return this.step == '?';
      }

      public boolean isValue() {
         return this.step == ':';
      }

      public void valueStep() {
         this.step = ':';
      }

      public boolean isFinished() {
         return this.finished;
      }

      public KeyTree.Node track() {
         if (this.node == null) {
            this.node = YamlCommentReader.this.track(this.getIndentation(), this.getKey());
         }

         return this.node;
      }

      public void finish() {
         this.track();
         this.finished = true;
      }

      public int getIndentation() {
         return this.indent;
      }

      public KeyTree.Node getNode() {
         return this.node;
      }

      public String getKey() {
         if (this.key == null) {
            return "";
         } else {
            String key = this.key.toString();
            return this.isListKey ? key + ']' : key;
         }
      }

      public void addKey(String key, boolean isListKey) {
         if (key != null && !key.isEmpty()) {
            if (this.key == null) {
               if (isListKey) {
                  this.isListKey = true;
                  key = '[' + key;
               }

               this.key = new StringBuilder(key);
            } else {
               this.key.append(this.isListKey ? ", " : " ").append(key);
            }
         }

      }

      public String getKeyComment() {
         return this.keyComment != null ? this.keyComment.toString() : null;
      }

      public String getValueComment() {
         return this.valueComment != null ? this.valueComment.toString() : null;
      }

      public void addComment(String comment) {
         if (this.isKey()) {
            this.keyComment = this.addComment(comment, this.keyComment);
         } else {
            this.valueComment = this.addComment(comment, this.valueComment);
         }

      }

      private StringBuilder addComment(String comment, StringBuilder commentBuilder) {
         if (commentBuilder == null) {
            commentBuilder = new StringBuilder();
            if (this.isKey()) {
               this.appendIndentComment(comment, commentBuilder);
            } else {
               commentBuilder.append(comment);
            }
         } else {
            commentBuilder.append('\n');
            this.appendIndentComment(comment, commentBuilder);
         }

         return commentBuilder;
      }

      private void appendIndentComment(String comment, StringBuilder commentBuilder) {
         commentBuilder.append(StringUtils.indentation(this.getIndentation()));
         commentBuilder.append(StringUtils.stripIndentation(comment));
      }

      public String getStep() {
         return this.isFinished() ? "END" : (this.isKey() ? "KEY" : "VALUE");
      }

      public String toString() {
         return "{step = " + this.getStep() + ", indent = " + this.getIndentation() + ", key = " + this.getKey() + ", keyComment = " + this.getKeyComment() + ", valueComment = " + this.getValueComment() + "}";
      }
   }

   public static enum ReadingQuoteStyle {
      NONE('\u0000'),
      SINGLE('\''),
      DOUBLE('"'),
      LITERAL('|');

      private final char quote;

      private ReadingQuoteStyle(char quote) {
         this.quote = quote;
      }

      public char getChar() {
         return this.quote;
      }
   }

   protected static enum ReaderStage {
      START,
      NEW_LINE,
      KEY,
      AFTER_KEY,
      VALUE,
      COMMENT,
      QUOTE_OPEN,
      QUOTE_CLOSE,
      END_OF_LINE,
      END_OF_FILE;
   }
}
