package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.List;
import dev.artixdev.libs.org.bson.BsonBinaryWriter;
import dev.artixdev.libs.org.bson.BsonElement;
import dev.artixdev.libs.org.bson.BsonReader;

public class ElementExtendingBsonWriter extends LevelCountingBsonWriter {
   private final BsonBinaryWriter writer;
   private final List<BsonElement> extraElements;

   public ElementExtendingBsonWriter(BsonBinaryWriter writer, List<BsonElement> extraElements) {
      super(writer);
      this.writer = writer;
      this.extraElements = extraElements;
   }

   public void writeEndDocument() {
      if (this.getCurrentLevel() == 0) {
         BsonWriterHelper.writeElements(this.writer, this.extraElements);
      }

      super.writeEndDocument();
   }

   public void pipe(BsonReader reader) {
      if (this.getCurrentLevel() == -1) {
         this.writer.pipe(reader, this.extraElements);
      } else {
         this.writer.pipe(reader);
      }

   }
}
