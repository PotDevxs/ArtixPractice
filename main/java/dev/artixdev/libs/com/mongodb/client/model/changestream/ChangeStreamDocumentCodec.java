package dev.artixdev.libs.com.mongodb.client.model.changestream;

import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.codecs.pojo.ClassModel;
import dev.artixdev.libs.org.bson.codecs.pojo.ClassModelBuilder;
import dev.artixdev.libs.org.bson.codecs.pojo.PojoCodecProvider;

final class ChangeStreamDocumentCodec<TResult> implements Codec<ChangeStreamDocument<TResult>> {
   private static final OperationTypeCodec OPERATION_TYPE_CODEC = new OperationTypeCodec();
   private final Codec<ChangeStreamDocument<TResult>> codec;

   @SuppressWarnings({"unchecked", "rawtypes"})
   ChangeStreamDocumentCodec(Class<TResult> fullDocumentClass, CodecRegistry codecRegistry) {
      ClassModelBuilder<ChangeStreamDocument> classModelBuilder = ClassModel.builder(ChangeStreamDocument.class);
      Codec<TResult> fullDocumentCodec = codecRegistry.get(fullDocumentClass);
      classModelBuilder.getProperty("fullDocument").codec((Codec) fullDocumentCodec);
      classModelBuilder.getProperty("fullDocumentBeforeChange").codec((Codec) fullDocumentCodec);
      ClassModel<ChangeStreamDocument> changeStreamDocumentClassModel = classModelBuilder.build();
      PojoCodecProvider provider = PojoCodecProvider.builder().register(MongoNamespace.class).register(UpdateDescription.class).register(SplitEvent.class).register(TruncatedArray.class).register(changeStreamDocumentClassModel).build();
      CodecRegistry registry = CodecRegistries.fromRegistries(CodecRegistries.fromProviders(provider, new BsonValueCodecProvider()), codecRegistry);
      this.codec = (Codec<ChangeStreamDocument<TResult>>) (Codec) registry.get(ChangeStreamDocument.class);
   }

   public ChangeStreamDocument<TResult> decode(BsonReader reader, DecoderContext decoderContext) {
      return (ChangeStreamDocument)this.codec.decode(reader, decoderContext);
   }

   public void encode(BsonWriter writer, ChangeStreamDocument<TResult> value, EncoderContext encoderContext) {
      this.codec.encode(writer, value, encoderContext);
   }

   public Class<ChangeStreamDocument<TResult>> getEncoderClass() {
      return (Class<ChangeStreamDocument<TResult>>) (Class<?>) ChangeStreamDocument.class;
   }
}
