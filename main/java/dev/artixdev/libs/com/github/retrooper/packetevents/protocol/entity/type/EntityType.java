package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.type;

import java.util.Optional;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.mapper.LegacyMappedEntity;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;

public interface EntityType extends LegacyMappedEntity, MappedEntity {
   Optional<EntityType> getParent();
}
