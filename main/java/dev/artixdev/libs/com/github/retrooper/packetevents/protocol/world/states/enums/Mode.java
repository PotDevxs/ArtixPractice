package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums;

public enum Mode {
   CORNER,
   COMPARE,
   DATA,
   LOAD,
   SAVE,
   SUBTRACT;

   // $FF: synthetic method
   private static Mode[] $values() {
      return new Mode[]{CORNER, COMPARE, DATA, LOAD, SAVE, SUBTRACT};
   }
}
