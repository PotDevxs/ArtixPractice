package dev.artixdev.libs.net.kyori.adventure.key;

import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface Namespaced {
   @NotNull
   @KeyPattern.Namespace
   String namespace();
}
