package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonDbPointer;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonRegularExpression;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.types.Decimal128;
import dev.artixdev.libs.org.bson.types.ObjectId;

public class BsonWriterDecorator implements BsonWriter {
   private final BsonWriter bsonWriter;

   BsonWriterDecorator(BsonWriter bsonWriter) {
      this.bsonWriter = (BsonWriter)Assertions.notNull("bsonWriter", bsonWriter);
   }

   BsonWriter getBsonWriter() {
      return this.bsonWriter;
   }

   public void writeStartDocument(String name) {
      this.bsonWriter.writeStartDocument(name);
   }

   public void writeStartDocument() {
      this.bsonWriter.writeStartDocument();
   }

   public void writeEndDocument() {
      this.bsonWriter.writeEndDocument();
   }

   public void writeStartArray(String name) {
      this.bsonWriter.writeStartArray(name);
   }

   public void writeStartArray() {
      this.bsonWriter.writeStartArray();
   }

   public void writeEndArray() {
      this.bsonWriter.writeEndArray();
   }

   public void writeBinaryData(String name, BsonBinary binary) {
      this.bsonWriter.writeBinaryData(name, binary);
   }

   public void writeBinaryData(BsonBinary binary) {
      this.bsonWriter.writeBinaryData(binary);
   }

   public void writeBoolean(String name, boolean value) {
      this.bsonWriter.writeBoolean(name, value);
   }

   public void writeBoolean(boolean value) {
      this.bsonWriter.writeBoolean(value);
   }

   public void writeDateTime(String name, long value) {
      this.bsonWriter.writeDateTime(name, value);
   }

   public void writeDateTime(long value) {
      this.bsonWriter.writeDateTime(value);
   }

   public void writeDBPointer(String name, BsonDbPointer value) {
      this.bsonWriter.writeDBPointer(name, value);
   }

   public void writeDBPointer(BsonDbPointer value) {
      this.bsonWriter.writeDBPointer(value);
   }

   public void writeDouble(String name, double value) {
      this.bsonWriter.writeDouble(name, value);
   }

   public void writeDouble(double value) {
      this.bsonWriter.writeDouble(value);
   }

   public void writeInt32(String name, int value) {
      this.bsonWriter.writeInt32(name, value);
   }

   public void writeInt32(int value) {
      this.bsonWriter.writeInt32(value);
   }

   public void writeInt64(String name, long value) {
      this.bsonWriter.writeInt64(name, value);
   }

   public void writeInt64(long value) {
      this.bsonWriter.writeInt64(value);
   }

   public void writeDecimal128(Decimal128 value) {
      this.bsonWriter.writeDecimal128(value);
   }

   public void writeDecimal128(String name, Decimal128 value) {
      this.bsonWriter.writeDecimal128(name, value);
   }

   public void writeJavaScript(String name, String code) {
      this.bsonWriter.writeJavaScript(name, code);
   }

   public void writeJavaScript(String code) {
      this.bsonWriter.writeJavaScript(code);
   }

   public void writeJavaScriptWithScope(String name, String code) {
      this.bsonWriter.writeJavaScriptWithScope(name, code);
   }

   public void writeJavaScriptWithScope(String code) {
      this.bsonWriter.writeJavaScriptWithScope(code);
   }

   public void writeMaxKey(String name) {
      this.bsonWriter.writeMaxKey(name);
   }

   public void writeMaxKey() {
      this.bsonWriter.writeMaxKey();
   }

   public void writeMinKey(String name) {
      this.bsonWriter.writeMinKey(name);
   }

   public void writeMinKey() {
      this.bsonWriter.writeMinKey();
   }

   public void writeName(String name) {
      this.bsonWriter.writeName(name);
   }

   public void writeNull(String name) {
      this.bsonWriter.writeNull(name);
   }

   public void writeNull() {
      this.bsonWriter.writeNull();
   }

   public void writeObjectId(String name, ObjectId objectId) {
      this.bsonWriter.writeObjectId(name, objectId);
   }

   public void writeObjectId(ObjectId objectId) {
      this.bsonWriter.writeObjectId(objectId);
   }

   public void writeRegularExpression(String name, BsonRegularExpression regularExpression) {
      this.bsonWriter.writeRegularExpression(name, regularExpression);
   }

   public void writeRegularExpression(BsonRegularExpression regularExpression) {
      this.bsonWriter.writeRegularExpression(regularExpression);
   }

   public void writeString(String name, String value) {
      this.bsonWriter.writeString(name, value);
   }

   public void writeString(String value) {
      this.bsonWriter.writeString(value);
   }

   public void writeSymbol(String name, String value) {
      this.bsonWriter.writeSymbol(name, value);
   }

   public void writeSymbol(String value) {
      this.bsonWriter.writeSymbol(value);
   }

   public void writeTimestamp(String name, BsonTimestamp value) {
      this.bsonWriter.writeTimestamp(name, value);
   }

   public void writeTimestamp(BsonTimestamp value) {
      this.bsonWriter.writeTimestamp(value);
   }

   public void writeUndefined(String name) {
      this.bsonWriter.writeUndefined(name);
   }

   public void writeUndefined() {
      this.bsonWriter.writeUndefined();
   }

   public void pipe(BsonReader reader) {
      this.bsonWriter.pipe(reader);
   }

   public void flush() {
      this.bsonWriter.flush();
   }
}
