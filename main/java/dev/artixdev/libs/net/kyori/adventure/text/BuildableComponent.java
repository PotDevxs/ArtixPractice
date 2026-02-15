package dev.artixdev.libs.net.kyori.adventure.text;

import dev.artixdev.libs.net.kyori.adventure.util.Buildable;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface BuildableComponent<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> extends Component, Buildable<C, B> {
   @NotNull
   B toBuilder();
}
