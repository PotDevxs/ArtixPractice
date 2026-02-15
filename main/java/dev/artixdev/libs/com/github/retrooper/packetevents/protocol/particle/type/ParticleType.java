package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.type;

import java.util.function.BiConsumer;
import java.util.function.Function;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public interface ParticleType extends MappedEntity {
   Function<PacketWrapper<?>, ParticleData> readDataFunction();

   BiConsumer<PacketWrapper<?>, ParticleData> writeDataFunction();
}
