package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.villager;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfession;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfessions;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerTypes;

public class VillagerData {
   private VillagerType type;
   private VillagerProfession profession;
   private int level;

   public VillagerData(VillagerType type, VillagerProfession profession, int level) {
      this.type = type;
      this.profession = profession;
      this.level = level;
   }

   public VillagerData(int typeId, int professionId, int level) {
      this(VillagerTypes.getById(typeId), VillagerProfessions.getById(professionId), level);
   }

   public VillagerType getType() {
      return this.type;
   }

   public void setType(VillagerType type) {
      this.type = type;
   }

   public VillagerProfession getProfession() {
      return this.profession;
   }

   public void setProfession(VillagerProfession profession) {
      this.profession = profession;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }
}
