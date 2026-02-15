package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums;

public enum TrialSpawnerState {
   INACTIVE,
   WAITING_FOR_PLAYERS,
   ACTIVE,
   WAITING_FOR_REWARD_EJECTION,
   EJECTING_REWARD,
   COOLDOWN;

   // $FF: synthetic method
   private static TrialSpawnerState[] $values() {
      return new TrialSpawnerState[]{INACTIVE, WAITING_FOR_PLAYERS, ACTIVE, WAITING_FOR_REWARD_EJECTION, EJECTING_REWARD, COOLDOWN};
   }
}
