package dev.artixdev.libs.com.mongodb.client.gridfs.codecs;

import java.util.Date;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.gridfs.model.GridFSFile;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDateTime;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentReader;
import dev.artixdev.libs.org.bson.BsonDocumentWrapper;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public final class GridFSFileCodec implements Codec<GridFSFile> {
   private final Codec<Document> documentCodec;
   private final Codec<BsonDocument> bsonDocumentCodec;

   public GridFSFileCodec(CodecRegistry registry) {
      this.documentCodec = (Codec)Assertions.notNull("DocumentCodec", ((CodecRegistry)Assertions.notNull("registry", registry)).get(Document.class));
      this.bsonDocumentCodec = (Codec)Assertions.notNull("BsonDocumentCodec", registry.get(BsonDocument.class));
   }

   public GridFSFile decode(BsonReader reader, DecoderContext decoderContext) {
      BsonDocument bsonDocument = (BsonDocument)this.bsonDocumentCodec.decode(reader, decoderContext);
      BsonValue id = bsonDocument.get("_id");
      String filename = bsonDocument.get("filename", new BsonString("")).asString().getValue();
      long length = bsonDocument.getNumber("length").longValue();
      int chunkSize = bsonDocument.getNumber("chunkSize").intValue();
      Date uploadDate = new Date(bsonDocument.getDateTime("uploadDate").getValue());
      BsonDocument metadataBsonDocument = bsonDocument.getDocument("metadata", new BsonDocument());
      Document optionalMetadata = this.asDocumentOrNull(metadataBsonDocument);
      return new GridFSFile(id, filename, length, chunkSize, uploadDate, optionalMetadata);
   }

   public void encode(BsonWriter writer, GridFSFile value, EncoderContext encoderContext) {
      BsonDocument bsonDocument = new BsonDocument();
      bsonDocument.put("_id", value.getId());
      bsonDocument.put((String)"filename", (BsonValue)(new BsonString(value.getFilename())));
      bsonDocument.put((String)"length", (BsonValue)(new BsonInt64(value.getLength())));
      bsonDocument.put((String)"chunkSize", (BsonValue)(new BsonInt32(value.getChunkSize())));
      bsonDocument.put((String)"uploadDate", (BsonValue)(new BsonDateTime(value.getUploadDate().getTime())));
      Document metadata = value.getMetadata();
      if (metadata != null) {
         bsonDocument.put((String)"metadata", (BsonValue)(new BsonDocumentWrapper(metadata, this.documentCodec)));
      }

      this.bsonDocumentCodec.encode(writer, bsonDocument, encoderContext);
   }

   public Class<GridFSFile> getEncoderClass() {
      return GridFSFile.class;
   }

   @Nullable
   private Document asDocumentOrNull(BsonDocument bsonDocument) {
      if (bsonDocument.isEmpty()) {
         return null;
      } else {
         BsonDocumentReader reader = new BsonDocumentReader(bsonDocument);
         return (Document)this.documentCodec.decode(reader, DecoderContext.builder().build());
      }
   }
}
