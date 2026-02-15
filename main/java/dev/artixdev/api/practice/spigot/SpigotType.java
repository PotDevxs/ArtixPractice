package dev.artixdev.api.practice.spigot;

import java.util.Arrays;
import dev.artixdev.api.practice.spigot.event.IListener;
import dev.artixdev.api.practice.spigot.knockback.IKnockbackType;
import dev.artixdev.api.practice.spigot.knockback.impl.atom.AtomSpigotKnockback;
import dev.artixdev.api.practice.spigot.knockback.impl.atom.AtomSpigotListener;
import dev.artixdev.api.practice.spigot.knockback.impl.carbon.CarbonSpigotKnockback;
import dev.artixdev.api.practice.spigot.knockback.impl.carbon.CarbonSpigotListener;
import dev.artixdev.api.practice.spigot.knockback.impl.fox.FoxSpigotKnockback;
import dev.artixdev.api.practice.spigot.knockback.impl.fox.FoxSpigotListener;
import dev.artixdev.api.practice.spigot.knockback.impl.ispigot.iSpigotKnockback;
import dev.artixdev.api.practice.spigot.knockback.impl.ispigot.iSpigotListener;
import dev.artixdev.api.practice.spigot.knockback.impl.paper.PaperKnockback;
import dev.artixdev.api.practice.spigot.knockback.impl.zortex.ZortexSpigotKnockback;
import dev.artixdev.api.practice.spigot.knockback.impl.zortex.ZortexSpigotListener;

public enum SpigotType {
   CarbonSpigot("CarbonSpigot", "dev.artixdev.spigot.api.knockback.KnockbackAPI", new CarbonSpigotKnockback(), new CarbonSpigotListener()),
   FoxSpigot("FoxSpigot", "pt.foxspigot.jar.knockback.KnockbackModule", new FoxSpigotKnockback(), new FoxSpigotListener()),
   AtomSpigot("AtomSpigot", "xyz.yooniks.atomspigot.AtomSpigot", new AtomSpigotKnockback(), new AtomSpigotListener()),
   iSpigot("iSpigot", "spg.lgdev.iSpigot", new iSpigotKnockback(), new iSpigotListener()),
   ZortexSpigot("ZortexSpigot", "club.zortex.spigot.ZortexSpigot", new ZortexSpigotKnockback(), new ZortexSpigotListener()),
   Default("Paper", "", new PaperKnockback(), (IListener)null);

   private final String name;
   private final String classToCheck;
   private final IKnockbackType knockbackType;
   private final IListener listener;

   public static SpigotType get() {
      return (SpigotType)Arrays.stream(values()).filter((spigot) -> {
         return !spigot.equals(Default) && check(spigot.getClassToCheck());
      }).findFirst().orElse(Default);
   }

   public static boolean check(String string) {
      if (string.isEmpty()) {
         return false;
      } else {
         try {
            Class.forName(string);
            return true;
} catch (Exception e) {
         return false;
         }
      }
   }

   public String getName() {
      return this.name;
   }

   public String getClassToCheck() {
      return this.classToCheck;
   }

   public IKnockbackType getKnockbackType() {
      return this.knockbackType;
   }

   public IListener getListener() {
      return this.listener;
   }

   private SpigotType(String name, String classToCheck, IKnockbackType knockbackType, IListener listener) {
      this.name = name;
      this.classToCheck = classToCheck;
      this.knockbackType = knockbackType;
      this.listener = listener;
   }
}
