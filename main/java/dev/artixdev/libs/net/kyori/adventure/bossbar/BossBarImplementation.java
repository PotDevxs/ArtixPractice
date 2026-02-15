package dev.artixdev.libs.net.kyori.adventure.bossbar;

import java.util.Collections;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public interface BossBarImplementation {
   @ApiStatus.Internal
   @NotNull
   static <I extends BossBarImplementation> I get(@NotNull BossBar bar, @NotNull Class<I> type) {
      return BossBarImpl.ImplementationAccessor.get(bar, type);
   }

   @ApiStatus.Internal
   @NotNull
   default Iterable<? extends BossBarViewer> viewers() {
      return Collections.emptyList();
   }

   @ApiStatus.Internal
   public interface Provider {
      @ApiStatus.Internal
      @NotNull
      BossBarImplementation create(@NotNull BossBar var1);
   }
}
