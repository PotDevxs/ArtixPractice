package dev.artixdev.libs.net.kyori.adventure.bossbar;

import java.util.Set;
import dev.artixdev.libs.net.kyori.adventure.audience.Audience;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.ComponentLike;
import dev.artixdev.libs.net.kyori.adventure.util.Index;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.UnmodifiableView;

@ApiStatus.NonExtendable
public interface BossBar extends Examinable {
   float MIN_PROGRESS = 0.0F;
   float MAX_PROGRESS = 1.0F;
   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   float MIN_PERCENT = 0.0F;
   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   float MAX_PERCENT = 1.0F;

   @NotNull
   static BossBar bossBar(@NotNull ComponentLike name, float progress, @NotNull BossBar.Color color, @NotNull BossBar.Overlay overlay) {
      BossBarImpl.checkProgress(progress);
      return bossBar(name.asComponent(), progress, color, overlay);
   }

   @NotNull
   static BossBar bossBar(@NotNull Component name, float progress, @NotNull BossBar.Color color, @NotNull BossBar.Overlay overlay) {
      BossBarImpl.checkProgress(progress);
      return new BossBarImpl(name, progress, color, overlay);
   }

   @NotNull
   static BossBar bossBar(@NotNull ComponentLike name, float progress, @NotNull BossBar.Color color, @NotNull BossBar.Overlay overlay, @NotNull Set<BossBar.Flag> flags) {
      BossBarImpl.checkProgress(progress);
      return bossBar(name.asComponent(), progress, color, overlay, flags);
   }

   @NotNull
   static BossBar bossBar(@NotNull Component name, float progress, @NotNull BossBar.Color color, @NotNull BossBar.Overlay overlay, @NotNull Set<BossBar.Flag> flags) {
      BossBarImpl.checkProgress(progress);
      return new BossBarImpl(name, progress, color, overlay, flags);
   }

   @NotNull
   Component name();

   @Contract("_ -> this")
   @NotNull
   default BossBar name(@NotNull ComponentLike name) {
      return this.name(name.asComponent());
   }

   @Contract("_ -> this")
   @NotNull
   BossBar name(@NotNull Component var1);

   float progress();

   @Contract("_ -> this")
   @NotNull
   BossBar progress(float var1);

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   default float percent() {
      return this.progress();
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract("_ -> this")
   @NotNull
   default BossBar percent(float progress) {
      return this.progress(progress);
   }

   @NotNull
   BossBar.Color color();

   @Contract("_ -> this")
   @NotNull
   BossBar color(@NotNull BossBar.Color var1);

   @NotNull
   BossBar.Overlay overlay();

   @Contract("_ -> this")
   @NotNull
   BossBar overlay(@NotNull BossBar.Overlay var1);

   @NotNull
   @UnmodifiableView
   Set<BossBar.Flag> flags();

   @Contract("_ -> this")
   @NotNull
   BossBar flags(@NotNull Set<BossBar.Flag> var1);

   boolean hasFlag(@NotNull BossBar.Flag var1);

   @Contract("_ -> this")
   @NotNull
   BossBar addFlag(@NotNull BossBar.Flag var1);

   @Contract("_ -> this")
   @NotNull
   BossBar removeFlag(@NotNull BossBar.Flag var1);

   @Contract("_ -> this")
   @NotNull
   BossBar addFlags(@NotNull BossBar.Flag... var1);

   @Contract("_ -> this")
   @NotNull
   BossBar removeFlags(@NotNull BossBar.Flag... var1);

   @Contract("_ -> this")
   @NotNull
   BossBar addFlags(@NotNull Iterable<BossBar.Flag> var1);

   @Contract("_ -> this")
   @NotNull
   BossBar removeFlags(@NotNull Iterable<BossBar.Flag> var1);

   @Contract("_ -> this")
   @NotNull
   BossBar addListener(@NotNull BossBar.Listener var1);

   @Contract("_ -> this")
   @NotNull
   BossBar removeListener(@NotNull BossBar.Listener var1);

   @NotNull
   @UnmodifiableView
   Iterable<? extends BossBarViewer> viewers();

   @NotNull
   default BossBar addViewer(@NotNull Audience viewer) {
      viewer.showBossBar(this);
      return this;
   }

   @NotNull
   default BossBar removeViewer(@NotNull Audience viewer) {
      viewer.hideBossBar(this);
      return this;
   }

   public static enum Overlay {
      PROGRESS("progress"),
      NOTCHED_6("notched_6"),
      NOTCHED_10("notched_10"),
      NOTCHED_12("notched_12"),
      NOTCHED_20("notched_20");

      public static final Index<String, BossBar.Overlay> NAMES = Index.create(BossBar.Overlay.class, (overlay) -> {
         return overlay.name;
      });
      private final String name;

      private Overlay(String name) {
         this.name = name;
      }
   }

   public static enum Flag {
      DARKEN_SCREEN("darken_screen"),
      PLAY_BOSS_MUSIC("play_boss_music"),
      CREATE_WORLD_FOG("create_world_fog");

      public static final Index<String, BossBar.Flag> NAMES = Index.create(BossBar.Flag.class, (flag) -> {
         return flag.name;
      });
      private final String name;

      private Flag(String name) {
         this.name = name;
      }
   }

   public static enum Color {
      PINK("pink"),
      BLUE("blue"),
      RED("red"),
      GREEN("green"),
      YELLOW("yellow"),
      PURPLE("purple"),
      WHITE("white");

      public static final Index<String, BossBar.Color> NAMES = Index.create(BossBar.Color.class, (color) -> {
         return color.name;
      });
      private final String name;

      private Color(String name) {
         this.name = name;
      }
   }

   @ApiStatus.OverrideOnly
   public interface Listener {
      default void bossBarNameChanged(@NotNull BossBar bar, @NotNull Component oldName, @NotNull Component newName) {
      }

      default void bossBarProgressChanged(@NotNull BossBar bar, float oldProgress, float newProgress) {
         this.bossBarPercentChanged(bar, oldProgress, newProgress);
      }

      /** @deprecated */
      @Deprecated
      @ApiStatus.ScheduledForRemoval(
         inVersion = "5.0.0"
      )
      default void bossBarPercentChanged(@NotNull BossBar bar, float oldProgress, float newProgress) {
      }

      default void bossBarColorChanged(@NotNull BossBar bar, @NotNull BossBar.Color oldColor, @NotNull BossBar.Color newColor) {
      }

      default void bossBarOverlayChanged(@NotNull BossBar bar, @NotNull BossBar.Overlay oldOverlay, @NotNull BossBar.Overlay newOverlay) {
      }

      default void bossBarFlagsChanged(@NotNull BossBar bar, @NotNull Set<BossBar.Flag> flagsAdded, @NotNull Set<BossBar.Flag> flagsRemoved) {
      }
   }
}
