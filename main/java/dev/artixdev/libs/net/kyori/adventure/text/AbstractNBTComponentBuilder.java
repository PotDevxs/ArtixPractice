package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.Objects;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

abstract class AbstractNBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponentBuilder<C, B> implements NBTComponentBuilder<C, B> {
   @Nullable
   protected String nbtPath;
   protected boolean interpret = false;
   @Nullable
   protected Component separator;

   AbstractNBTComponentBuilder() {
   }

   AbstractNBTComponentBuilder(@NotNull C component) {
      super(component);
      this.nbtPath = component.nbtPath();
      this.interpret = component.interpret();
      this.separator = component.separator();
   }

   @NotNull
   public B nbtPath(@NotNull String nbtPath) {
      this.nbtPath = (String)Objects.requireNonNull(nbtPath, "nbtPath");
      return self();
   }

   @NotNull
   public B interpret(boolean interpret) {
      this.interpret = interpret;
      return self();
   }

   @NotNull
   public B separator(@Nullable ComponentLike separator) {
      this.separator = ComponentLike.unbox(separator);
      return self();
   }
}
