package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.mapper;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;

public interface MappedEntity {
   ResourceLocation getName();

   int getId(ClientVersion var1);
}
