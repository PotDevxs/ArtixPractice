package dev.artixdev.libs.org.bson.codecs;

import java.util.ArrayList;
import java.util.Iterator;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.Transformer;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

/** @deprecated */
@Deprecated
public class IterableCodec implements Codec<Iterable>, OverridableUuidRepresentationCodec<Iterable> {
   private final CodecRegistry registry;
   private final BsonTypeCodecMap bsonTypeCodecMap;
   private final Transformer valueTransformer;
   private final UuidRepresentation uuidRepresentation;

   public IterableCodec(CodecRegistry registry, BsonTypeClassMap bsonTypeClassMap) {
      this(registry, bsonTypeClassMap, (Transformer)null);
   }

   public IterableCodec(CodecRegistry registry, BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer) {
      this(registry, new BsonTypeCodecMap((BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap), registry), valueTransformer, UuidRepresentation.UNSPECIFIED);
   }

   private IterableCodec(CodecRegistry registry, BsonTypeCodecMap bsonTypeCodecMap, Transformer valueTransformer, UuidRepresentation uuidRepresentation) {
      this.registry = (CodecRegistry)Assertions.notNull("registry", registry);
      this.bsonTypeCodecMap = bsonTypeCodecMap;
      this.valueTransformer = valueTransformer != null ? valueTransformer : (objectToTransform) -> {
         return objectToTransform;
      };
      this.uuidRepresentation = uuidRepresentation;
   }

   public Codec<Iterable> withUuidRepresentation(UuidRepresentation uuidRepresentation) {
      return new IterableCodec(this.registry, this.bsonTypeCodecMap, this.valueTransformer, uuidRepresentation);
   }

   public Iterable decode(BsonReader reader, DecoderContext decoderContext) {
      reader.readStartArray();
      ArrayList list = new ArrayList();

      while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
         list.add(ContainerCodecHelper.readValue(reader, decoderContext, this.bsonTypeCodecMap, this.uuidRepresentation, this.registry, this.valueTransformer));
      }

      reader.readEndArray();
      return list;
   }

   public void encode(BsonWriter writer, Iterable value, EncoderContext encoderContext) {
      writer.writeStartArray();
      Iterator<?> iterator = value.iterator();

      while(iterator.hasNext()) {
         Object cur = iterator.next();
         this.writeValue(writer, encoderContext, cur);
      }

      writer.writeEndArray();
   }

   public Class<Iterable> getEncoderClass() {
      return Iterable.class;
   }

   private void writeValue(BsonWriter writer, EncoderContext encoderContext, Object value) {
      if (value == null) {
         writer.writeNull();
      } else {
         Codec codec = this.registry.get(value.getClass());
         encoderContext.encodeWithChildContext(codec, writer, value);
      }

   }
}
