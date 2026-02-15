package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;

public interface LegacyConvertible {
   LegacyParticleData toLegacy(ClientVersion var1);
}
