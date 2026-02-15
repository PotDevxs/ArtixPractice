package dev.artixdev.libs.com.mongodb.client.model.geojson;

import dev.artixdev.libs.com.mongodb.annotations.Immutable;

@Immutable
public abstract class CoordinateReferenceSystem {
   public abstract CoordinateReferenceSystemType getType();
}
