package dev.artixdev.libs.net.kyori.adventure.bossbar;

import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.UnmodifiableView;

public interface BossBarViewer {
   @NotNull
   @UnmodifiableView
   Iterable<? extends BossBar> activeBossBars();
}
