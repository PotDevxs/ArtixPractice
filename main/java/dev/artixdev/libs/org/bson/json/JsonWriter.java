package dev.artixdev.libs.org.bson.json;

import java.io.Writer;
import dev.artixdev.libs.org.bson.AbstractBsonWriter;
import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonContextType;
import dev.artixdev.libs.org.bson.BsonDbPointer;
import dev.artixdev.libs.org.bson.BsonMaxKey;
import dev.artixdev.libs.org.bson.BsonMinKey;
import dev.artixdev.libs.org.bson.BsonNull;
import dev.artixdev.libs.org.bson.BsonRegularExpression;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonUndefined;
import dev.artixdev.libs.org.bson.types.Decimal128;
import dev.artixdev.libs.org.bson.types.ObjectId;

public class JsonWriter extends AbstractBsonWriter {
   private final JsonWriterSettings settings;
   private final StrictCharacterStreamJsonWriter strictJsonWriter;

   public JsonWriter(Writer writer) {
      this(writer, JsonWriterSettings.builder().build());
   }

   public JsonWriter(Writer writer, JsonWriterSettings settings) {
      super(settings);
      this.settings = settings;
      this.setContext(new JsonWriter.Context((JsonWriter.Context)null, BsonContextType.TOP_LEVEL));
      this.strictJsonWriter = new StrictCharacterStreamJsonWriter(writer, StrictCharacterStreamJsonWriterSettings.builder().indent(settings.isIndent()).newLineCharacters(settings.getNewLineCharacters()).indentCharacters(settings.getIndentCharacters()).maxLength(settings.getMaxLength()).build());
   }

   public Writer getWriter() {
      return this.strictJsonWriter.getWriter();
   }

   protected JsonWriter.Context getContext() {
      return (JsonWriter.Context)super.getContext();
   }

   protected void doWriteName(String name) {
      this.strictJsonWriter.writeName(name);
   }

   protected void doWriteStartDocument() {
      this.strictJsonWriter.writeStartObject();
      BsonContextType contextType = this.getState() == AbstractBsonWriter.State.SCOPE_DOCUMENT ? BsonContextType.SCOPE_DOCUMENT : BsonContextType.DOCUMENT;
      this.setContext(new JsonWriter.Context(this.getContext(), contextType));
   }

   protected void doWriteEndDocument() {
      this.strictJsonWriter.writeEndObject();
      if (this.getContext().getContextType() == BsonContextType.SCOPE_DOCUMENT) {
         this.setContext(this.getContext().getParentContext());
         this.writeEndDocument();
      } else {
         this.setContext(this.getContext().getParentContext());
      }

   }

   protected void doWriteStartArray() {
      this.strictJsonWriter.writeStartArray();
      this.setContext(new JsonWriter.Context(this.getContext(), BsonContextType.ARRAY));
   }

   protected void doWriteEndArray() {
      this.strictJsonWriter.writeEndArray();
      this.setContext(this.getContext().getParentContext());
   }

   protected void doWriteBinaryData(BsonBinary binary) {
      this.settings.getBinaryConverter().convert(binary, this.strictJsonWriter);
   }

   public void doWriteBoolean(boolean value) {
      this.settings.getBooleanConverter().convert(value, this.strictJsonWriter);
   }

   protected void doWriteDateTime(long value) {
      this.settings.getDateTimeConverter().convert(value, this.strictJsonWriter);
   }

   protected void doWriteDBPointer(BsonDbPointer value) {
      if (this.settings.getOutputMode() == JsonMode.EXTENDED) {
         this.strictJsonWriter.writeStartObject();
         this.strictJsonWriter.writeStartObject("$dbPointer");
         this.strictJsonWriter.writeString("$ref", value.getNamespace());
         this.strictJsonWriter.writeName("$id");
         this.doWriteObjectId(value.getId());
         this.strictJsonWriter.writeEndObject();
         this.strictJsonWriter.writeEndObject();
      } else {
         this.strictJsonWriter.writeStartObject();
         this.strictJsonWriter.writeString("$ref", value.getNamespace());
         this.strictJsonWriter.writeName("$id");
         this.doWriteObjectId(value.getId());
         this.strictJsonWriter.writeEndObject();
      }

   }

   protected void doWriteDouble(double value) {
      this.settings.getDoubleConverter().convert(value, this.strictJsonWriter);
   }

   protected void doWriteInt32(int value) {
      this.settings.getInt32Converter().convert(value, this.strictJsonWriter);
   }

   protected void doWriteInt64(long value) {
      this.settings.getInt64Converter().convert(value, this.strictJsonWriter);
   }

   protected void doWriteDecimal128(Decimal128 value) {
      this.settings.getDecimal128Converter().convert(value, this.strictJsonWriter);
   }

   protected void doWriteJavaScript(String code) {
      this.settings.getJavaScriptConverter().convert(code, this.strictJsonWriter);
   }

   protected void doWriteJavaScriptWithScope(String code) {
      this.writeStartDocument();
      this.writeString("$code", code);
      this.writeName("$scope");
   }

   protected void doWriteMaxKey() {
      this.settings.getMaxKeyConverter().convert(new BsonMaxKey(), this.strictJsonWriter);
   }

   protected void doWriteMinKey() {
      this.settings.getMinKeyConverter().convert(new BsonMinKey(), this.strictJsonWriter);
   }

   public void doWriteNull() {
      this.settings.getNullConverter().convert(new BsonNull(), this.strictJsonWriter);
   }

   public void doWriteObjectId(ObjectId objectId) {
      this.settings.getObjectIdConverter().convert(objectId, this.strictJsonWriter);
   }

   public void doWriteRegularExpression(BsonRegularExpression regularExpression) {
      this.settings.getRegularExpressionConverter().convert(regularExpression, this.strictJsonWriter);
   }

   public void doWriteString(String value) {
      this.settings.getStringConverter().convert(value, this.strictJsonWriter);
   }

   public void doWriteSymbol(String value) {
      this.settings.getSymbolConverter().convert(value, this.strictJsonWriter);
   }

   public void doWriteTimestamp(BsonTimestamp value) {
      this.settings.getTimestampConverter().convert(value, this.strictJsonWriter);
   }

   public void doWriteUndefined() {
      this.settings.getUndefinedConverter().convert(new BsonUndefined(), this.strictJsonWriter);
   }

   public void flush() {
      this.strictJsonWriter.flush();
   }

   public boolean isTruncated() {
      return this.strictJsonWriter.isTruncated();
   }

   protected boolean abortPipe() {
      return this.strictJsonWriter.isTruncated();
   }

   public class Context extends AbstractBsonWriter.Context {
      public Context(JsonWriter.Context parentContext, BsonContextType contextType) {
         super(parentContext, contextType);
      }

      public JsonWriter.Context getParentContext() {
         return (JsonWriter.Context)super.getParentContext();
      }
   }
}
