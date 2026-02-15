package dev.artixdev.libs.org.bson.conversions;

import java.util.Arrays;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.codecs.BsonCodecProvider;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.CollectionCodecProvider;
import dev.artixdev.libs.org.bson.codecs.DocumentCodecProvider;
import dev.artixdev.libs.org.bson.codecs.EnumCodecProvider;
import dev.artixdev.libs.org.bson.codecs.IterableCodecProvider;
import dev.artixdev.libs.org.bson.codecs.JsonObjectCodecProvider;
import dev.artixdev.libs.org.bson.codecs.MapCodecProvider;
import dev.artixdev.libs.org.bson.codecs.ValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.codecs.jsr310.Jsr310CodecProvider;

public interface Bson {
   CodecRegistry DEFAULT_CODEC_REGISTRY = CodecRegistries.fromProviders(Arrays.asList(new ValueCodecProvider(), new BsonValueCodecProvider(), new DocumentCodecProvider(), new CollectionCodecProvider(), new IterableCodecProvider(), new MapCodecProvider(), new Jsr310CodecProvider(), new JsonObjectCodecProvider(), new BsonCodecProvider(), new EnumCodecProvider()));

   <TDocument> BsonDocument toBsonDocument(Class<TDocument> var1, CodecRegistry var2);

   default BsonDocument toBsonDocument() {
      return this.toBsonDocument(BsonDocument.class, DEFAULT_CODEC_REGISTRY);
   }
}
