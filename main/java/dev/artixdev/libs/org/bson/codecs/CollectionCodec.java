package dev.artixdev.libs.org.bson.codecs;

import java.util.Collection;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.Transformer;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

final class CollectionCodec<C extends Collection<Object>> extends AbstractCollectionCodec<Object, C> implements OverridableUuidRepresentationCodec<C> {
   private final CodecRegistry registry;
   private final BsonTypeCodecMap bsonTypeCodecMap;
   private final Transformer valueTransformer;
   private final UuidRepresentation uuidRepresentation;

   CollectionCodec(CodecRegistry registry, BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer, Class<C> clazz) {
      this(registry, new BsonTypeCodecMap((BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap), registry), valueTransformer, clazz, UuidRepresentation.UNSPECIFIED);
   }

   private CollectionCodec(CodecRegistry registry, BsonTypeCodecMap bsonTypeCodecMap, Transformer valueTransformer, Class<C> clazz, UuidRepresentation uuidRepresentation) {
      super(clazz);
      this.registry = (CodecRegistry)Assertions.notNull("registry", registry);
      this.bsonTypeCodecMap = bsonTypeCodecMap;
      this.valueTransformer = valueTransformer != null ? valueTransformer : (value) -> {
         return value;
      };
      this.uuidRepresentation = uuidRepresentation;
   }

   public Codec<C> withUuidRepresentation(UuidRepresentation uuidRepresentation) {
      return this.uuidRepresentation.equals(uuidRepresentation) ? this : new CollectionCodec(this.registry, this.bsonTypeCodecMap, this.valueTransformer, this.getEncoderClass(), uuidRepresentation);
   }

   Object readValue(BsonReader reader, DecoderContext decoderContext) {
      return ContainerCodecHelper.readValue(reader, decoderContext, this.bsonTypeCodecMap, this.uuidRepresentation, this.registry, this.valueTransformer);
   }

   void writeValue(BsonWriter writer, Object value, EncoderContext encoderContext) {
      Codec codec = this.registry.get(value.getClass());
      encoderContext.encodeWithChildContext(codec, writer, value);
   }
}
