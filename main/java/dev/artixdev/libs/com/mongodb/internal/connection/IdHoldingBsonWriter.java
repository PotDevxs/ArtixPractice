package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Objects;
import java.util.function.Supplier;
import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonBinaryWriter;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDateTime;
import dev.artixdev.libs.org.bson.BsonDbPointer;
import dev.artixdev.libs.org.bson.BsonDecimal128;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDouble;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonJavaScript;
import dev.artixdev.libs.org.bson.BsonJavaScriptWithScope;
import dev.artixdev.libs.org.bson.BsonMaxKey;
import dev.artixdev.libs.org.bson.BsonMinKey;
import dev.artixdev.libs.org.bson.BsonNull;
import dev.artixdev.libs.org.bson.BsonObjectId;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonRegularExpression;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonSymbol;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonUndefined;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.io.BasicOutputBuffer;
import dev.artixdev.libs.org.bson.types.Decimal128;
import dev.artixdev.libs.org.bson.types.ObjectId;

public class IdHoldingBsonWriter extends LevelCountingBsonWriter {
   private static final String ID_FIELD_NAME = "_id";
   private LevelCountingBsonWriter idBsonBinaryWriter;
   private BasicOutputBuffer outputBuffer;
   private String currentFieldName;
   private BsonValue id;
   private boolean idFieldIsAnArray = false;

   public IdHoldingBsonWriter(BsonWriter bsonWriter) {
      super(bsonWriter);
   }

   public void writeStartDocument(String name) {
      this.setCurrentFieldName(name);
      if (this.isWritingId()) {
         this.getIdBsonWriter().writeStartDocument(name);
      }

      super.writeStartDocument(name);
   }

   public void writeStartDocument() {
      if (this.isWritingId()) {
         this.getIdBsonWriter().writeStartDocument();
      }

      super.writeStartDocument();
   }

   public void writeEndDocument() {
      if (this.isWritingId()) {
         if (this.getIdBsonWriterCurrentLevel() >= 0) {
            this.getIdBsonWriter().writeEndDocument();
         }

         if (this.getIdBsonWriterCurrentLevel() == -1) {
            if (this.id != null && this.id.isJavaScriptWithScope()) {
               this.id = new BsonJavaScriptWithScope(this.id.asJavaScriptWithScope().getCode(), new RawBsonDocument(this.getBytes()));
            } else if (this.id == null) {
               this.id = new RawBsonDocument(this.getBytes());
            }
         }
      }

      if (this.getCurrentLevel() == 0 && this.id == null) {
         this.id = new BsonObjectId();
         this.writeObjectId("_id", this.id.asObjectId().getValue());
      }

      super.writeEndDocument();
   }

   public void writeStartArray() {
      if (this.isWritingId()) {
         if (this.getIdBsonWriterCurrentLevel() == -1) {
            this.idFieldIsAnArray = true;
            this.getIdBsonWriter().writeStartDocument();
            this.getIdBsonWriter().writeName("_id");
         }

         this.getIdBsonWriter().writeStartArray();
      }

      super.writeStartArray();
   }

   public void writeStartArray(String name) {
      this.setCurrentFieldName(name);
      if (this.isWritingId()) {
         if (this.getIdBsonWriterCurrentLevel() == -1) {
            this.getIdBsonWriter().writeStartDocument();
         }

         this.getIdBsonWriter().writeStartArray(name);
      }

      super.writeStartArray(name);
   }

   public void writeEndArray() {
      if (this.isWritingId()) {
         this.getIdBsonWriter().writeEndArray();
         if (this.getIdBsonWriterCurrentLevel() == 0 && this.idFieldIsAnArray) {
            this.getIdBsonWriter().writeEndDocument();
            this.id = (new RawBsonDocument(this.getBytes())).get("_id");
         }
      }

      super.writeEndArray();
   }

   public void writeBinaryData(String name, BsonBinary binary) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return binary;
      }, () -> {
         this.getIdBsonWriter().writeBinaryData(name, binary);
      });
      super.writeBinaryData(name, binary);
   }

   public void writeBinaryData(BsonBinary binary) {
      this.addBsonValue(() -> {
         return binary;
      }, () -> {
         this.getIdBsonWriter().writeBinaryData(binary);
      });
      super.writeBinaryData(binary);
   }

   public void writeBoolean(String name, boolean value) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return BsonBoolean.valueOf(value);
      }, () -> {
         this.getIdBsonWriter().writeBoolean(name, value);
      });
      super.writeBoolean(name, value);
   }

   public void writeBoolean(boolean value) {
      this.addBsonValue(() -> {
         return BsonBoolean.valueOf(value);
      }, () -> {
         this.getIdBsonWriter().writeBoolean(value);
      });
      super.writeBoolean(value);
   }

   public void writeDateTime(String name, long value) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return new BsonDateTime(value);
      }, () -> {
         this.getIdBsonWriter().writeDateTime(name, value);
      });
      super.writeDateTime(name, value);
   }

   public void writeDateTime(long value) {
      this.addBsonValue(() -> {
         return new BsonDateTime(value);
      }, () -> {
         this.getIdBsonWriter().writeDateTime(value);
      });
      super.writeDateTime(value);
   }

   public void writeDBPointer(String name, BsonDbPointer value) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return value;
      }, () -> {
         this.getIdBsonWriter().writeDBPointer(name, value);
      });
      super.writeDBPointer(name, value);
   }

   public void writeDBPointer(BsonDbPointer value) {
      this.addBsonValue(() -> {
         return value;
      }, () -> {
         this.getIdBsonWriter().writeDBPointer(value);
      });
      super.writeDBPointer(value);
   }

   public void writeDouble(String name, double value) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return new BsonDouble(value);
      }, () -> {
         this.getIdBsonWriter().writeDouble(name, value);
      });
      super.writeDouble(name, value);
   }

   public void writeDouble(double value) {
      this.addBsonValue(() -> {
         return new BsonDouble(value);
      }, () -> {
         this.getIdBsonWriter().writeDouble(value);
      });
      super.writeDouble(value);
   }

   public void writeInt32(String name, int value) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return new BsonInt32(value);
      }, () -> {
         this.getIdBsonWriter().writeInt32(name, value);
      });
      super.writeInt32(name, value);
   }

   public void writeInt32(int value) {
      this.addBsonValue(() -> {
         return new BsonInt32(value);
      }, () -> {
         this.getIdBsonWriter().writeInt32(value);
      });
      super.writeInt32(value);
   }

   public void writeInt64(String name, long value) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return new BsonInt64(value);
      }, () -> {
         this.getIdBsonWriter().writeInt64(name, value);
      });
      super.writeInt64(name, value);
   }

   public void writeInt64(long value) {
      this.addBsonValue(() -> {
         return new BsonInt64(value);
      }, () -> {
         this.getIdBsonWriter().writeInt64(value);
      });
      super.writeInt64(value);
   }

   public void writeDecimal128(String name, Decimal128 value) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return new BsonDecimal128(value);
      }, () -> {
         this.getIdBsonWriter().writeDecimal128(name, value);
      });
      super.writeDecimal128(name, value);
   }

   public void writeDecimal128(Decimal128 value) {
      this.addBsonValue(() -> {
         return new BsonDecimal128(value);
      }, () -> {
         this.getIdBsonWriter().writeDecimal128(value);
      });
      super.writeDecimal128(value);
   }

   public void writeJavaScript(String name, String code) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return new BsonJavaScript(code);
      }, () -> {
         this.getIdBsonWriter().writeJavaScript(name, code);
      });
      super.writeJavaScript(name, code);
   }

   public void writeJavaScript(String code) {
      this.addBsonValue(() -> {
         return new BsonJavaScript(code);
      }, () -> {
         this.getIdBsonWriter().writeJavaScript(code);
      });
      super.writeJavaScript(code);
   }

   public void writeJavaScriptWithScope(String name, String code) {
      this.addBsonValue(() -> {
         return new BsonJavaScriptWithScope(code, new BsonDocument());
      }, () -> {
         this.getIdBsonWriter().writeJavaScriptWithScope(name, code);
      });
      super.writeJavaScriptWithScope(name, code);
   }

   public void writeJavaScriptWithScope(String code) {
      this.addBsonValue(() -> {
         return new BsonJavaScriptWithScope(code, new BsonDocument());
      }, () -> {
         this.getIdBsonWriter().writeJavaScriptWithScope(code);
      });
      super.writeJavaScriptWithScope(code);
   }

   public void writeMaxKey(String name) {
      this.setCurrentFieldName(name);
      this.addBsonValue(BsonMaxKey::new, () -> {
         this.getIdBsonWriter().writeMaxKey(name);
      });
      super.writeMaxKey(name);
   }

   public void writeMaxKey() {
      Supplier var10001 = BsonMaxKey::new;
      LevelCountingBsonWriter var10002 = this.getIdBsonWriter();
      Objects.requireNonNull(var10002);
      this.addBsonValue(var10001, var10002::writeMaxKey);
      super.writeMaxKey();
   }

   public void writeMinKey(String name) {
      this.setCurrentFieldName(name);
      this.addBsonValue(BsonMinKey::new, () -> {
         this.getIdBsonWriter().writeMinKey(name);
      });
      super.writeMinKey(name);
   }

   public void writeMinKey() {
      Supplier var10001 = BsonMinKey::new;
      LevelCountingBsonWriter var10002 = this.getIdBsonWriter();
      Objects.requireNonNull(var10002);
      this.addBsonValue(var10001, var10002::writeMinKey);
      super.writeMinKey();
   }

   public void writeName(String name) {
      this.setCurrentFieldName(name);
      if (this.getIdBsonWriterCurrentLevel() >= 0) {
         this.getIdBsonWriter().writeName(name);
      }

      super.writeName(name);
   }

   public void writeNull(String name) {
      this.setCurrentFieldName(name);
      this.addBsonValue(BsonNull::new, () -> {
         this.getIdBsonWriter().writeNull(name);
      });
      super.writeNull(name);
   }

   public void writeNull() {
      Supplier var10001 = BsonNull::new;
      LevelCountingBsonWriter var10002 = this.getIdBsonWriter();
      Objects.requireNonNull(var10002);
      this.addBsonValue(var10001, var10002::writeNull);
      super.writeNull();
   }

   public void writeObjectId(String name, ObjectId objectId) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return new BsonObjectId(objectId);
      }, () -> {
         this.getIdBsonWriter().writeObjectId(name, objectId);
      });
      super.writeObjectId(name, objectId);
   }

   public void writeObjectId(ObjectId objectId) {
      this.addBsonValue(() -> {
         return new BsonObjectId(objectId);
      }, () -> {
         this.getIdBsonWriter().writeObjectId(objectId);
      });
      super.writeObjectId(objectId);
   }

   public void writeRegularExpression(String name, BsonRegularExpression regularExpression) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return regularExpression;
      }, () -> {
         this.getIdBsonWriter().writeRegularExpression(name, regularExpression);
      });
      super.writeRegularExpression(name, regularExpression);
   }

   public void writeRegularExpression(BsonRegularExpression regularExpression) {
      this.addBsonValue(() -> {
         return regularExpression;
      }, () -> {
         this.getIdBsonWriter().writeRegularExpression(regularExpression);
      });
      super.writeRegularExpression(regularExpression);
   }

   public void writeString(String name, String value) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return new BsonString(value);
      }, () -> {
         this.getIdBsonWriter().writeString(name, value);
      });
      super.writeString(name, value);
   }

   public void writeString(String value) {
      this.addBsonValue(() -> {
         return new BsonString(value);
      }, () -> {
         this.getIdBsonWriter().writeString(value);
      });
      super.writeString(value);
   }

   public void writeSymbol(String name, String value) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return new BsonSymbol(value);
      }, () -> {
         this.getIdBsonWriter().writeSymbol(name, value);
      });
      super.writeSymbol(name, value);
   }

   public void writeSymbol(String value) {
      this.addBsonValue(() -> {
         return new BsonSymbol(value);
      }, () -> {
         this.getIdBsonWriter().writeSymbol(value);
      });
      super.writeSymbol(value);
   }

   public void writeTimestamp(String name, BsonTimestamp value) {
      this.setCurrentFieldName(name);
      this.addBsonValue(() -> {
         return value;
      }, () -> {
         this.getIdBsonWriter().writeTimestamp(name, value);
      });
      super.writeTimestamp(name, value);
   }

   public void writeTimestamp(BsonTimestamp value) {
      this.addBsonValue(() -> {
         return value;
      }, () -> {
         this.getIdBsonWriter().writeTimestamp(value);
      });
      super.writeTimestamp(value);
   }

   public void writeUndefined(String name) {
      this.setCurrentFieldName(name);
      this.addBsonValue(BsonUndefined::new, () -> {
         this.getIdBsonWriter().writeUndefined(name);
      });
      super.writeUndefined(name);
   }

   public void writeUndefined() {
      Supplier var10001 = BsonUndefined::new;
      LevelCountingBsonWriter var10002 = this.getIdBsonWriter();
      Objects.requireNonNull(var10002);
      this.addBsonValue(var10001, var10002::writeUndefined);
      super.writeUndefined();
   }

   public void pipe(BsonReader reader) {
      super.pipe(reader);
   }

   public void flush() {
      super.flush();
   }

   public BsonValue getId() {
      return this.id;
   }

   private void setCurrentFieldName(String name) {
      this.currentFieldName = name;
   }

   private boolean isWritingId() {
      return this.getIdBsonWriterCurrentLevel() >= 0 || this.getCurrentLevel() == 0 && this.currentFieldName != null && this.currentFieldName.equals("_id");
   }

   private void addBsonValue(Supplier<BsonValue> value, Runnable writeValue) {
      if (this.isWritingId()) {
         if (this.getIdBsonWriterCurrentLevel() >= 0) {
            writeValue.run();
         } else {
            this.id = (BsonValue)value.get();
         }
      }

   }

   private int getIdBsonWriterCurrentLevel() {
      return this.idBsonBinaryWriter == null ? -1 : this.idBsonBinaryWriter.getCurrentLevel();
   }

   private LevelCountingBsonWriter getIdBsonWriter() {
      if (this.idBsonBinaryWriter == null) {
         this.outputBuffer = new BasicOutputBuffer(128);
         this.idBsonBinaryWriter = new LevelCountingBsonWriter(new BsonBinaryWriter(this.outputBuffer)) {
         };
      }

      return this.idBsonBinaryWriter;
   }

   private byte[] getBytes() {
      return this.outputBuffer.getInternalBuffer();
   }
}
