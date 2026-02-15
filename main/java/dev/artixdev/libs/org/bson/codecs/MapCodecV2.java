package dev.artixdev.libs.org.bson.codecs;

import java.util.Map;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.Transformer;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

final class MapCodecV2<M extends Map<String, Object>> extends AbstractMapCodec<Object, M> implements OverridableUuidRepresentationCodec<M> {
   private final BsonTypeCodecMap bsonTypeCodecMap;
   private final CodecRegistry registry;
   private final Transformer valueTransformer;
   private final UuidRepresentation uuidRepresentation;

   MapCodecV2(CodecRegistry registry, BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer, Class<M> clazz) {
      this(registry, new BsonTypeCodecMap((BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap), registry), valueTransformer, UuidRepresentation.UNSPECIFIED, clazz);
   }

   private MapCodecV2(CodecRegistry registry, BsonTypeCodecMap bsonTypeCodecMap, Transformer valueTransformer, UuidRepresentation uuidRepresentation, Class<M> clazz) {
      super(clazz);
      this.registry = (CodecRegistry)Assertions.notNull("registry", registry);
      this.bsonTypeCodecMap = bsonTypeCodecMap;
      this.valueTransformer = valueTransformer != null ? valueTransformer : (value) -> {
         return value;
      };
      this.uuidRepresentation = uuidRepresentation;
   }

   public Codec<M> withUuidRepresentation(UuidRepresentation uuidRepresentation) {
      return this.uuidRepresentation.equals(uuidRepresentation) ? this : new MapCodecV2(this.registry, this.bsonTypeCodecMap, this.valueTransformer, uuidRepresentation, this.getEncoderClass());
   }

   Object readValue(BsonReader reader, DecoderContext decoderContext) {
      return ContainerCodecHelper.readValue(reader, decoderContext, this.bsonTypeCodecMap, this.uuidRepresentation, this.registry, this.valueTransformer);
   }

   void writeValue(BsonWriter writer, Object value, EncoderContext encoderContext) {
      Codec codec = this.registry.get(value.getClass());
      encoderContext.encodeWithChildContext(codec, writer, value);
   }
}
