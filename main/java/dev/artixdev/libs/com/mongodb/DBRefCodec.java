package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class DBRefCodec implements Codec<DBRef> {
   private final CodecRegistry registry;

   public DBRefCodec(CodecRegistry registry) {
      this.registry = (CodecRegistry)Assertions.notNull("registry", registry);
   }

   public void encode(BsonWriter writer, DBRef value, EncoderContext encoderContext) {
      writer.writeStartDocument();
      writer.writeString("$ref", value.getCollectionName());
      writer.writeName("$id");
      Codec codec = this.registry.get(value.getId().getClass());
      codec.encode(writer, value.getId(), encoderContext);
      if (value.getDatabaseName() != null) {
         writer.writeString("$db", value.getDatabaseName());
      }

      writer.writeEndDocument();
   }

   public Class<DBRef> getEncoderClass() {
      return DBRef.class;
   }

   public DBRef decode(BsonReader reader, DecoderContext decoderContext) {
      throw new UnsupportedOperationException("DBRefCodec does not support decoding");
   }
}
