package dev.artixdev.libs.net.kyori.adventure.sound;

import java.util.Objects;
import java.util.OptionalLong;
import java.util.function.Consumer;
import java.util.function.Supplier;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.key.Keyed;
import dev.artixdev.libs.net.kyori.adventure.util.Index;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Range;

@ApiStatus.NonExtendable
public interface Sound extends Examinable {
   @NotNull
   static Sound.Builder sound() {
      return new SoundImpl.BuilderImpl();
   }

   @NotNull
   static Sound.Builder sound(@NotNull Sound existing) {
      return new SoundImpl.BuilderImpl(existing);
   }

   @NotNull
   static Sound sound(@NotNull Consumer<Sound.Builder> configurer) {
      return (Sound)AbstractBuilder.configureAndBuild(sound(), configurer);
   }

   @NotNull
   static Sound sound(@NotNull Key name, @NotNull Sound.Source source, float volume, float pitch) {
      return (Sound)sound().type(name).source(source).volume(volume).pitch(pitch).build();
   }

   @NotNull
   static Sound sound(@NotNull Sound.Type type, @NotNull Sound.Source source, float volume, float pitch) {
      Objects.requireNonNull(type, "type");
      return sound(type.key(), source, volume, pitch);
   }

   @NotNull
   static Sound sound(@NotNull Supplier<? extends Sound.Type> type, @NotNull Sound.Source source, float volume, float pitch) {
      return (Sound)sound().type(type).source(source).volume(volume).pitch(pitch).build();
   }

   @NotNull
   static Sound sound(@NotNull Key name, @NotNull Sound.Source.Provider source, float volume, float pitch) {
      return sound(name, source.soundSource(), volume, pitch);
   }

   @NotNull
   static Sound sound(@NotNull Sound.Type type, @NotNull Sound.Source.Provider source, float volume, float pitch) {
      return sound(type, source.soundSource(), volume, pitch);
   }

   @NotNull
   static Sound sound(@NotNull Supplier<? extends Sound.Type> type, @NotNull Sound.Source.Provider source, float volume, float pitch) {
      return sound(type, source.soundSource(), volume, pitch);
   }

   @NotNull
   Key name();

   @NotNull
   Sound.Source source();

   float volume();

   float pitch();

   @NotNull
   OptionalLong seed();

   @NotNull
   SoundStop asStop();

   public interface Builder extends AbstractBuilder<Sound> {
      @NotNull
      Sound.Builder type(@NotNull Key var1);

      @NotNull
      Sound.Builder type(@NotNull Sound.Type var1);

      @NotNull
      Sound.Builder type(@NotNull Supplier<? extends Sound.Type> var1);

      @NotNull
      Sound.Builder source(@NotNull Sound.Source var1);

      @NotNull
      Sound.Builder source(@NotNull Sound.Source.Provider var1);

      @NotNull
      Sound.Builder volume(@Range(from = 0L,to = 2147483647L) float var1);

      @NotNull
      Sound.Builder pitch(@Range(from = -1L,to = 1L) float var1);

      @NotNull
      Sound.Builder seed(long var1);

      @NotNull
      Sound.Builder seed(@NotNull OptionalLong var1);
   }

   public interface Emitter {
      @NotNull
      static Sound.Emitter self() {
         return SoundImpl.EMITTER_SELF;
      }
   }

   public interface Type extends Keyed {
      @NotNull
      Key key();
   }

   public static enum Source {
      MASTER("master"),
      MUSIC("music"),
      RECORD("record"),
      WEATHER("weather"),
      BLOCK("block"),
      HOSTILE("hostile"),
      NEUTRAL("neutral"),
      PLAYER("player"),
      AMBIENT("ambient"),
      VOICE("voice");

      public static final Index<String, Sound.Source> NAMES = Index.create(Sound.Source.class, (source) -> {
         return source.name;
      });
      private final String name;

      private Source(String name) {
         this.name = name;
      }

      public interface Provider {
         @NotNull
         Sound.Source soundSource();
      }
   }
}
