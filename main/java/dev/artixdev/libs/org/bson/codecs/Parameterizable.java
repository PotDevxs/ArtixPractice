package dev.artixdev.libs.org.bson.codecs;

import java.lang.reflect.Type;
import java.util.List;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

/** @deprecated */
@Deprecated
public interface Parameterizable {
   Codec<?> parameterize(CodecRegistry var1, List<Type> var2);
}
