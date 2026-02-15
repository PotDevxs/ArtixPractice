package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.org.bson.BsonWriter;

abstract class LevelCountingBsonWriter extends BsonWriterDecorator {
   private int level = -1;

   LevelCountingBsonWriter(BsonWriter bsonWriter) {
      super(bsonWriter);
   }

   int getCurrentLevel() {
      return this.level;
   }

   public void writeStartDocument(String name) {
      ++this.level;
      super.writeStartDocument(name);
   }

   public void writeStartDocument() {
      ++this.level;
      super.writeStartDocument();
   }

   public void writeEndDocument() {
      --this.level;
      super.writeEndDocument();
   }

   public void writeStartArray() {
      ++this.level;
      super.writeStartArray();
   }

   public void writeStartArray(String name) {
      ++this.level;
      super.writeStartArray(name);
   }

   public void writeEndArray() {
      --this.level;
      super.writeEndArray();
   }
}
