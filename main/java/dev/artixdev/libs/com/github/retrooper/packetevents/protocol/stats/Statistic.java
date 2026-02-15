package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.stats;

import dev.artixdev.libs.net.kyori.adventure.text.Component;

public interface Statistic {
   String getId();

   Component display();
}
