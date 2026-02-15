package dev.artixdev.libs.org.bson.codecs;

import java.util.ArrayList;
import java.util.Iterator;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class BsonArrayCodec implements Codec<BsonArray> {
   private static final CodecRegistry DEFAULT_REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
   private final CodecRegistry codecRegistry;

   public BsonArrayCodec() {
      this(DEFAULT_REGISTRY);
   }

   public BsonArrayCodec(CodecRegistry codecRegistry) {
      this.codecRegistry = (CodecRegistry)Assertions.notNull("codecRegistry", codecRegistry);
   }

   public BsonArray decode(BsonReader reader, DecoderContext decoderContext) {
      reader.readStartArray();
      ArrayList list = new ArrayList();

      while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
         list.add(this.readValue(reader, decoderContext));
      }

      reader.readEndArray();
      return new BsonArray(list);
   }

   public void encode(BsonWriter writer, BsonArray array, EncoderContext encoderContext) {
      writer.writeStartArray();
      Iterator<BsonValue> iterator = array.iterator();

      while(iterator.hasNext()) {
         BsonValue value = iterator.next();
         Codec codec = this.codecRegistry.get(value.getClass());
         encoderContext.encodeWithChildContext(codec, writer, value);
      }

      writer.writeEndArray();
   }

   public Class<BsonArray> getEncoderClass() {
      return BsonArray.class;
   }

   protected BsonValue readValue(BsonReader reader, DecoderContext decoderContext) {
      return (BsonValue)this.codecRegistry.get(BsonValueCodecProvider.getClassForBsonType(reader.getCurrentBsonType())).decode(reader, decoderContext);
   }
}
