package dev.artixdev.libs.org.bson.codecs;

import java.util.UUID;
import dev.artixdev.libs.org.bson.UuidRepresentation;

public class OverridableUuidRepresentationUuidCodec extends UuidCodec implements OverridableUuidRepresentationCodec<UUID> {
   public OverridableUuidRepresentationUuidCodec() {
   }

   public OverridableUuidRepresentationUuidCodec(UuidRepresentation uuidRepresentation) {
      super(uuidRepresentation);
   }

   public Codec<UUID> withUuidRepresentation(UuidRepresentation uuidRepresentation) {
      return this.getUuidRepresentation().equals(uuidRepresentation) ? this : new OverridableUuidRepresentationUuidCodec(uuidRepresentation);
   }
}
