package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.mapper;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;

public interface LegacyMappedEntity {
   ResourceLocation getName();

   int getLegacyId(ClientVersion var1);
}
