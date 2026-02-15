package dev.artixdev.libs.net.kyori.adventure.text.serializer.json;

import java.util.Optional;
import java.util.function.Supplier;
import dev.artixdev.libs.net.kyori.adventure.util.Services;

final class JSONComponentSerializerAccessor {
   private static final Optional<JSONComponentSerializer.Provider> SERVICE = Services.serviceWithFallback(JSONComponentSerializer.Provider.class);

   private JSONComponentSerializerAccessor() {
   }

   static final class Instances {
      static final JSONComponentSerializer INSTANCE;
      static final Supplier<JSONComponentSerializer.Builder> BUILDER_SUPPLIER;

      static {
         INSTANCE = (JSONComponentSerializer)JSONComponentSerializerAccessor.SERVICE.map(JSONComponentSerializer.Provider::instance).orElse(DummyJSONComponentSerializer.INSTANCE);
         BUILDER_SUPPLIER = (Supplier)JSONComponentSerializerAccessor.SERVICE.map(JSONComponentSerializer.Provider::builder).orElse(DummyJSONComponentSerializer.BuilderImpl::new);
      }
   }
}
