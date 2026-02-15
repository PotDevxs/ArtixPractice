package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonDbPointer;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonRegularExpression;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.types.Decimal128;
import dev.artixdev.libs.org.bson.types.ObjectId;

public class FieldTrackingBsonWriter extends BsonWriterDecorator {
   private boolean hasWrittenField;
   private boolean topLevelDocumentWritten;

   public FieldTrackingBsonWriter(BsonWriter bsonWriter) {
      super(bsonWriter);
   }

   public boolean hasWrittenField() {
      return this.hasWrittenField;
   }

   public void writeStartDocument(String name) {
      if (this.topLevelDocumentWritten) {
         this.hasWrittenField = true;
      }

      super.writeStartDocument(name);
   }

   public void writeStartDocument() {
      if (this.topLevelDocumentWritten) {
         this.hasWrittenField = true;
      }

      this.topLevelDocumentWritten = true;
      super.writeStartDocument();
   }

   public void writeStartArray(String name) {
      this.hasWrittenField = true;
      super.writeStartArray(name);
   }

   public void writeStartArray() {
      this.hasWrittenField = true;
      super.writeStartArray();
   }

   public void writeBinaryData(String name, BsonBinary binary) {
      this.hasWrittenField = true;
      super.writeBinaryData(name, binary);
   }

   public void writeBinaryData(BsonBinary binary) {
      this.hasWrittenField = true;
      super.writeBinaryData(binary);
   }

   public void writeBoolean(String name, boolean value) {
      this.hasWrittenField = true;
      super.writeBoolean(name, value);
   }

   public void writeBoolean(boolean value) {
      this.hasWrittenField = true;
      super.writeBoolean(value);
   }

   public void writeDateTime(String name, long value) {
      this.hasWrittenField = true;
      super.writeDateTime(name, value);
   }

   public void writeDateTime(long value) {
      this.hasWrittenField = true;
      super.writeDateTime(value);
   }

   public void writeDBPointer(String name, BsonDbPointer value) {
      this.hasWrittenField = true;
      super.writeDBPointer(name, value);
   }

   public void writeDBPointer(BsonDbPointer value) {
      this.hasWrittenField = true;
      super.writeDBPointer(value);
   }

   public void writeDouble(String name, double value) {
      this.hasWrittenField = true;
      super.writeDouble(name, value);
   }

   public void writeDouble(double value) {
      this.hasWrittenField = true;
      super.writeDouble(value);
   }

   public void writeInt32(String name, int value) {
      this.hasWrittenField = true;
      super.writeInt32(name, value);
   }

   public void writeInt32(int value) {
      this.hasWrittenField = true;
      super.writeInt32(value);
   }

   public void writeInt64(String name, long value) {
      super.writeInt64(name, value);
      this.hasWrittenField = true;
   }

   public void writeInt64(long value) {
      this.hasWrittenField = true;
      super.writeInt64(value);
   }

   public void writeDecimal128(Decimal128 value) {
      this.hasWrittenField = true;
      super.writeDecimal128(value);
   }

   public void writeDecimal128(String name, Decimal128 value) {
      this.hasWrittenField = true;
      super.writeDecimal128(name, value);
   }

   public void writeJavaScript(String name, String code) {
      this.hasWrittenField = true;
      super.writeJavaScript(name, code);
   }

   public void writeJavaScript(String code) {
      this.hasWrittenField = true;
      super.writeJavaScript(code);
   }

   public void writeJavaScriptWithScope(String name, String code) {
      super.writeJavaScriptWithScope(name, code);
      this.hasWrittenField = true;
   }

   public void writeJavaScriptWithScope(String code) {
      this.hasWrittenField = true;
      super.writeJavaScriptWithScope(code);
   }

   public void writeMaxKey(String name) {
      this.hasWrittenField = true;
      super.writeMaxKey(name);
   }

   public void writeMaxKey() {
      this.hasWrittenField = true;
      super.writeMaxKey();
   }

   public void writeMinKey(String name) {
      this.hasWrittenField = true;
      super.writeMinKey(name);
   }

   public void writeMinKey() {
      this.hasWrittenField = true;
      super.writeMinKey();
   }

   public void writeNull(String name) {
      this.hasWrittenField = true;
      super.writeNull(name);
   }

   public void writeNull() {
      this.hasWrittenField = true;
      super.writeNull();
   }

   public void writeObjectId(String name, ObjectId objectId) {
      this.hasWrittenField = true;
      super.writeObjectId(name, objectId);
   }

   public void writeObjectId(ObjectId objectId) {
      this.hasWrittenField = true;
      super.writeObjectId(objectId);
   }

   public void writeRegularExpression(String name, BsonRegularExpression regularExpression) {
      this.hasWrittenField = true;
      super.writeRegularExpression(name, regularExpression);
   }

   public void writeRegularExpression(BsonRegularExpression regularExpression) {
      this.hasWrittenField = true;
      super.writeRegularExpression(regularExpression);
   }

   public void writeString(String name, String value) {
      this.hasWrittenField = true;
      super.writeString(name, value);
   }

   public void writeString(String value) {
      this.hasWrittenField = true;
      super.writeString(value);
   }

   public void writeSymbol(String name, String value) {
      this.hasWrittenField = true;
      super.writeSymbol(name, value);
   }

   public void writeSymbol(String value) {
      this.hasWrittenField = true;
      super.writeSymbol(value);
   }

   public void writeTimestamp(String name, BsonTimestamp value) {
      this.hasWrittenField = true;
      super.writeTimestamp(name, value);
   }

   public void writeTimestamp(BsonTimestamp value) {
      this.hasWrittenField = true;
      super.writeTimestamp(value);
   }

   public void writeUndefined(String name) {
      this.hasWrittenField = true;
      super.writeUndefined(name);
   }

   public void writeUndefined() {
      this.hasWrittenField = true;
      super.writeUndefined();
   }

   public void pipe(BsonReader reader) {
      this.hasWrittenField = true;
      super.pipe(reader);
   }
}
