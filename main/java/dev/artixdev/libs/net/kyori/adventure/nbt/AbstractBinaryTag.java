package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.net.kyori.examination.string.StringExaminer;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

abstract class AbstractBinaryTag implements BinaryTag {
   @NotNull
   public final String examinableName() {
      return this.type().toString();
   }

   public final String toString() {
      return (String)this.examine(StringExaminer.simpleEscaping());
   }
}
