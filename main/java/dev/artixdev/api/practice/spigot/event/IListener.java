package dev.artixdev.api.practice.spigot.event;

import org.bukkit.plugin.java.JavaPlugin;

public interface IListener {
   void register(JavaPlugin var1);

   boolean isApplicable();
}
