package dev.artixdev.libs.org.bson.json;

import java.io.IOException;
import java.io.Writer;
import dev.artixdev.libs.org.bson.BSONException;
import dev.artixdev.libs.org.bson.BsonInvalidOperationException;
import dev.artixdev.libs.org.bson.assertions.Assertions;

public final class StrictCharacterStreamJsonWriter implements StrictJsonWriter {
   private final Writer writer;
   private final StrictCharacterStreamJsonWriterSettings settings;
   private StrictCharacterStreamJsonWriter.StrictJsonContext context;
   private StrictCharacterStreamJsonWriter.State state;
   private int curLength;
   private boolean isTruncated;

   public StrictCharacterStreamJsonWriter(Writer writer, StrictCharacterStreamJsonWriterSettings settings) {
      this.context = new StrictCharacterStreamJsonWriter.StrictJsonContext((StrictCharacterStreamJsonWriter.StrictJsonContext)null, StrictCharacterStreamJsonWriter.JsonContextType.TOP_LEVEL, "");
      this.state = StrictCharacterStreamJsonWriter.State.INITIAL;
      this.writer = writer;
      this.settings = settings;
   }

   public int getCurrentLength() {
      return this.curLength;
   }

   public void writeStartObject(String name) {
      this.writeName(name);
      this.writeStartObject();
   }

   public void writeStartArray(String name) {
      this.writeName(name);
      this.writeStartArray();
   }

   public void writeBoolean(String name, boolean value) {
      Assertions.notNull("name", name);
      this.writeName(name);
      this.writeBoolean(value);
   }

   public void writeNumber(String name, String value) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", value);
      this.writeName(name);
      this.writeNumber(value);
   }

   public void writeString(String name, String value) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", value);
      this.writeName(name);
      this.writeString(value);
   }

   public void writeRaw(String name, String value) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", value);
      this.writeName(name);
      this.writeRaw(value);
   }

   public void writeNull(String name) {
      this.writeName(name);
      this.writeNull();
   }

   public void writeName(String name) {
      Assertions.notNull("name", name);
      this.checkState(StrictCharacterStreamJsonWriter.State.NAME);
      if (this.context.hasElements) {
         this.write(",");
      }

      if (this.settings.isIndent()) {
         this.write(this.settings.getNewLineCharacters());
         this.write(this.context.indentation);
      } else if (this.context.hasElements) {
         this.write(" ");
      }

      this.writeStringHelper(name);
      this.write(": ");
      this.state = StrictCharacterStreamJsonWriter.State.VALUE;
   }

   public void writeBoolean(boolean value) {
      this.checkState(StrictCharacterStreamJsonWriter.State.VALUE);
      this.preWriteValue();
      this.write(value ? "true" : "false");
      this.setNextState();
   }

   public void writeNumber(String value) {
      Assertions.notNull("value", value);
      this.checkState(StrictCharacterStreamJsonWriter.State.VALUE);
      this.preWriteValue();
      this.write(value);
      this.setNextState();
   }

   public void writeString(String value) {
      Assertions.notNull("value", value);
      this.checkState(StrictCharacterStreamJsonWriter.State.VALUE);
      this.preWriteValue();
      this.writeStringHelper(value);
      this.setNextState();
   }

   public void writeRaw(String value) {
      Assertions.notNull("value", value);
      this.checkState(StrictCharacterStreamJsonWriter.State.VALUE);
      this.preWriteValue();
      this.write(value);
      this.setNextState();
   }

   public void writeNull() {
      this.checkState(StrictCharacterStreamJsonWriter.State.VALUE);
      this.preWriteValue();
      this.write("null");
      this.setNextState();
   }

   public void writeStartObject() {
      if (this.state != StrictCharacterStreamJsonWriter.State.INITIAL && this.state != StrictCharacterStreamJsonWriter.State.VALUE) {
         throw new BsonInvalidOperationException("Invalid state " + this.state);
      } else {
         this.preWriteValue();
         this.write("{");
         this.context = new StrictCharacterStreamJsonWriter.StrictJsonContext(this.context, StrictCharacterStreamJsonWriter.JsonContextType.DOCUMENT, this.settings.getIndentCharacters());
         this.state = StrictCharacterStreamJsonWriter.State.NAME;
      }
   }

   public void writeStartArray() {
      this.preWriteValue();
      this.write("[");
      this.context = new StrictCharacterStreamJsonWriter.StrictJsonContext(this.context, StrictCharacterStreamJsonWriter.JsonContextType.ARRAY, this.settings.getIndentCharacters());
      this.state = StrictCharacterStreamJsonWriter.State.VALUE;
   }

   public void writeEndObject() {
      this.checkState(StrictCharacterStreamJsonWriter.State.NAME);
      if (this.settings.isIndent() && this.context.hasElements) {
         this.write(this.settings.getNewLineCharacters());
         this.write(this.context.parentContext.indentation);
      }

      this.write("}");
      this.context = this.context.parentContext;
      if (this.context.contextType == StrictCharacterStreamJsonWriter.JsonContextType.TOP_LEVEL) {
         this.state = StrictCharacterStreamJsonWriter.State.DONE;
      } else {
         this.setNextState();
      }

   }

   public void writeEndArray() {
      this.checkState(StrictCharacterStreamJsonWriter.State.VALUE);
      if (this.context.contextType != StrictCharacterStreamJsonWriter.JsonContextType.ARRAY) {
         throw new BsonInvalidOperationException("Can't end an array if not in an array");
      } else {
         if (this.settings.isIndent() && this.context.hasElements) {
            this.write(this.settings.getNewLineCharacters());
            this.write(this.context.parentContext.indentation);
         }

         this.write("]");
         this.context = this.context.parentContext;
         if (this.context.contextType == StrictCharacterStreamJsonWriter.JsonContextType.TOP_LEVEL) {
            this.state = StrictCharacterStreamJsonWriter.State.DONE;
         } else {
            this.setNextState();
         }

      }
   }

   public boolean isTruncated() {
      return this.isTruncated;
   }

   void flush() {
      try {
         this.writer.flush();
      } catch (IOException e) {
         this.throwBSONException(e);
      }

   }

   Writer getWriter() {
      return this.writer;
   }

   private void preWriteValue() {
      if (this.context.contextType == StrictCharacterStreamJsonWriter.JsonContextType.ARRAY) {
         if (this.context.hasElements) {
            this.write(",");
         }

         if (this.settings.isIndent()) {
            this.write(this.settings.getNewLineCharacters());
            this.write(this.context.indentation);
         } else if (this.context.hasElements) {
            this.write(" ");
         }
      }

      this.context.hasElements = true;
   }

   private void setNextState() {
      if (this.context.contextType == StrictCharacterStreamJsonWriter.JsonContextType.ARRAY) {
         this.state = StrictCharacterStreamJsonWriter.State.VALUE;
      } else {
         this.state = StrictCharacterStreamJsonWriter.State.NAME;
      }

   }

   private void writeStringHelper(String str) {
      this.write('"');

      for(int i = 0; i < str.length(); ++i) {
         char c = str.charAt(i);
         switch(c) {
         case '\b':
            this.write("\\b");
            break;
         case '\t':
            this.write("\\t");
            break;
         case '\n':
            this.write("\\n");
            break;
         case '\f':
            this.write("\\f");
            break;
         case '\r':
            this.write("\\r");
            break;
         case '"':
            this.write("\\\"");
            break;
         case '\\':
            this.write("\\\\");
            break;
         default:
            switch(Character.getType(c)) {
            case 1:
            case 2:
            case 3:
            case 5:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
               this.write(c);
               break;
            case 4:
            case 6:
            case 7:
            case 8:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
               this.write("\\u");
               this.write(Integer.toHexString((c & '\uf000') >> 12));
               this.write(Integer.toHexString((c & 3840) >> 8));
               this.write(Integer.toHexString((c & 240) >> 4));
               this.write(Integer.toHexString(c & 15));
            }
         }
      }

      this.write('"');
   }

   private void write(String str) {
      try {
         if (this.settings.getMaxLength() != 0 && str.length() + this.curLength >= this.settings.getMaxLength()) {
            this.writer.write(str.substring(0, this.settings.getMaxLength() - this.curLength));
            this.curLength = this.settings.getMaxLength();
            this.isTruncated = true;
         } else {
            this.writer.write(str);
            this.curLength += str.length();
         }
      } catch (IOException e) {
         this.throwBSONException(e);
      }

   }

   private void write(char c) {
      try {
         if (this.settings.getMaxLength() != 0 && this.curLength >= this.settings.getMaxLength()) {
            this.isTruncated = true;
         } else {
            this.writer.write(c);
            ++this.curLength;
         }
      } catch (IOException e) {
         this.throwBSONException(e);
      }

   }

   private void checkState(StrictCharacterStreamJsonWriter.State requiredState) {
      if (this.state != requiredState) {
         throw new BsonInvalidOperationException("Invalid state " + this.state);
      }
   }

   private void throwBSONException(IOException e) {
      throw new BSONException("Wrapping IOException", e);
   }

   private static class StrictJsonContext {
      private final StrictCharacterStreamJsonWriter.StrictJsonContext parentContext;
      private final StrictCharacterStreamJsonWriter.JsonContextType contextType;
      private final String indentation;
      private boolean hasElements;

      StrictJsonContext(StrictCharacterStreamJsonWriter.StrictJsonContext parentContext, StrictCharacterStreamJsonWriter.JsonContextType contextType, String indentChars) {
         this.parentContext = parentContext;
         this.contextType = contextType;
         this.indentation = parentContext == null ? indentChars : parentContext.indentation + indentChars;
      }
   }

   private static enum JsonContextType {
      TOP_LEVEL,
      DOCUMENT,
      ARRAY;

      // $FF: synthetic method
      private static StrictCharacterStreamJsonWriter.JsonContextType[] $values() {
         return new StrictCharacterStreamJsonWriter.JsonContextType[]{TOP_LEVEL, DOCUMENT, ARRAY};
      }
   }

   private static enum State {
      INITIAL,
      NAME,
      VALUE,
      DONE;

      // $FF: synthetic method
      private static StrictCharacterStreamJsonWriter.State[] $values() {
         return new StrictCharacterStreamJsonWriter.State[]{INITIAL, NAME, VALUE, DONE};
      }
   }
}
