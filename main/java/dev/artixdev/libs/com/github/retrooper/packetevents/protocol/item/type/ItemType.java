package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.type;

import java.util.Set;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface ItemType extends MappedEntity {
   int getMaxAmount();

   int getMaxDurability();

   boolean isMusicDisc();

   ItemType getCraftRemainder();

   @Nullable
   StateType getPlacedType();

   Set<ItemTypes.ItemAttribute> getAttributes();

   boolean hasAttribute(ItemTypes.ItemAttribute var1);
}
