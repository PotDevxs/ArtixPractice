package dev.artixdev.libs.com.mongodb.client.model.mql;

import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

@Immutable
@Beta({Beta.Reason.CLIENT})
public final class ExpressionCodecProvider implements CodecProvider {
   @Nullable
   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return MqlExpression.class.equals(clazz) ? (Codec<T>) new MqlExpressionCodec(registry) : null;
   }
}
