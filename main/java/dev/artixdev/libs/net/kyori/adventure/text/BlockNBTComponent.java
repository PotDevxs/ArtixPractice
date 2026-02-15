package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.regex.Matcher;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface BlockNBTComponent extends NBTComponent<BlockNBTComponent, BlockNBTComponent.Builder>, ScopedComponent<BlockNBTComponent> {
   @NotNull
   BlockNBTComponent.Pos pos();

   @Contract(
      pure = true
   )
   @NotNull
   BlockNBTComponent pos(@NotNull BlockNBTComponent.Pos var1);

   @Contract(
      pure = true
   )
   @NotNull
   default BlockNBTComponent localPos(double left, double up, double forwards) {
      return this.pos(BlockNBTComponent.LocalPos.localPos(left, up, forwards));
   }

   @Contract(
      pure = true
   )
   @NotNull
   default BlockNBTComponent worldPos(@NotNull BlockNBTComponent.WorldPos.Coordinate x, @NotNull BlockNBTComponent.WorldPos.Coordinate y, @NotNull BlockNBTComponent.WorldPos.Coordinate z) {
      return this.pos(BlockNBTComponent.WorldPos.worldPos(x, y, z));
   }

   @Contract(
      pure = true
   )
   @NotNull
   default BlockNBTComponent absoluteWorldPos(int x, int y, int z) {
      return this.worldPos(BlockNBTComponent.WorldPos.Coordinate.absolute(x), BlockNBTComponent.WorldPos.Coordinate.absolute(y), BlockNBTComponent.WorldPos.Coordinate.absolute(z));
   }

   @Contract(
      pure = true
   )
   @NotNull
   default BlockNBTComponent relativeWorldPos(int x, int y, int z) {
      return this.worldPos(BlockNBTComponent.WorldPos.Coordinate.relative(x), BlockNBTComponent.WorldPos.Coordinate.relative(y), BlockNBTComponent.WorldPos.Coordinate.relative(z));
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("pos", (Object)this.pos())), NBTComponent.super.examinableProperties());
   }

   public interface WorldPos extends BlockNBTComponent.Pos {
      @NotNull
      static BlockNBTComponent.WorldPos worldPos(@NotNull BlockNBTComponent.WorldPos.Coordinate x, @NotNull BlockNBTComponent.WorldPos.Coordinate y, @NotNull BlockNBTComponent.WorldPos.Coordinate z) {
         return new BlockNBTComponentImpl.WorldPosImpl(x, y, z);
      }

      /** @deprecated */
      @Deprecated
      @ApiStatus.ScheduledForRemoval(
         inVersion = "5.0.0"
      )
      @NotNull
      static BlockNBTComponent.WorldPos of(@NotNull BlockNBTComponent.WorldPos.Coordinate x, @NotNull BlockNBTComponent.WorldPos.Coordinate y, @NotNull BlockNBTComponent.WorldPos.Coordinate z) {
         return new BlockNBTComponentImpl.WorldPosImpl(x, y, z);
      }

      @NotNull
      BlockNBTComponent.WorldPos.Coordinate x();

      @NotNull
      BlockNBTComponent.WorldPos.Coordinate y();

      @NotNull
      BlockNBTComponent.WorldPos.Coordinate z();

      public interface Coordinate extends Examinable {
         @NotNull
         static BlockNBTComponent.WorldPos.Coordinate absolute(int value) {
            return coordinate(value, BlockNBTComponent.WorldPos.Coordinate.Type.ABSOLUTE);
         }

         @NotNull
         static BlockNBTComponent.WorldPos.Coordinate relative(int value) {
            return coordinate(value, BlockNBTComponent.WorldPos.Coordinate.Type.RELATIVE);
         }

         @NotNull
         static BlockNBTComponent.WorldPos.Coordinate coordinate(int value, @NotNull BlockNBTComponent.WorldPos.Coordinate.Type type) {
            return new BlockNBTComponentImpl.WorldPosImpl.CoordinateImpl(value, type);
         }

         /** @deprecated */
         @Deprecated
         @ApiStatus.ScheduledForRemoval(
            inVersion = "5.0.0"
         )
         @NotNull
         static BlockNBTComponent.WorldPos.Coordinate of(int value, @NotNull BlockNBTComponent.WorldPos.Coordinate.Type type) {
            return new BlockNBTComponentImpl.WorldPosImpl.CoordinateImpl(value, type);
         }

         int value();

         @NotNull
         BlockNBTComponent.WorldPos.Coordinate.Type type();

         public static enum Type {
            ABSOLUTE,
            RELATIVE;
         }
      }
   }

   public interface LocalPos extends BlockNBTComponent.Pos {
      @NotNull
      static BlockNBTComponent.LocalPos localPos(double left, double up, double forwards) {
         return new BlockNBTComponentImpl.LocalPosImpl(left, up, forwards);
      }

      /** @deprecated */
      @Deprecated
      @ApiStatus.ScheduledForRemoval(
         inVersion = "5.0.0"
      )
      @NotNull
      static BlockNBTComponent.LocalPos of(double left, double up, double forwards) {
         return new BlockNBTComponentImpl.LocalPosImpl(left, up, forwards);
      }

      double left();

      double up();

      double forwards();
   }

   public interface Pos extends Examinable {
      @NotNull
      static BlockNBTComponent.Pos fromString(@NotNull String input) throws IllegalArgumentException {
         Matcher localMatch = BlockNBTComponentImpl.Tokens.LOCAL_PATTERN.matcher(input);
         if (localMatch.matches()) {
            return BlockNBTComponent.LocalPos.localPos(Double.parseDouble(localMatch.group(1)), Double.parseDouble(localMatch.group(3)), Double.parseDouble(localMatch.group(5)));
         } else {
            Matcher worldMatch = BlockNBTComponentImpl.Tokens.WORLD_PATTERN.matcher(input);
            if (worldMatch.matches()) {
               return BlockNBTComponent.WorldPos.worldPos(BlockNBTComponentImpl.Tokens.deserializeCoordinate(worldMatch.group(1), worldMatch.group(2)), BlockNBTComponentImpl.Tokens.deserializeCoordinate(worldMatch.group(3), worldMatch.group(4)), BlockNBTComponentImpl.Tokens.deserializeCoordinate(worldMatch.group(5), worldMatch.group(6)));
            } else {
               throw new IllegalArgumentException("Cannot convert position specification '" + input + "' into a position");
            }
         }
      }

      @NotNull
      String asString();
   }

   public interface Builder extends NBTComponentBuilder<BlockNBTComponent, BlockNBTComponent.Builder> {
      @Contract("_ -> this")
      @NotNull
      BlockNBTComponent.Builder pos(@NotNull BlockNBTComponent.Pos var1);

      @Contract("_, _, _ -> this")
      @NotNull
      default BlockNBTComponent.Builder localPos(double left, double up, double forwards) {
         return this.pos(BlockNBTComponent.LocalPos.localPos(left, up, forwards));
      }

      @Contract("_, _, _ -> this")
      @NotNull
      default BlockNBTComponent.Builder worldPos(@NotNull BlockNBTComponent.WorldPos.Coordinate x, @NotNull BlockNBTComponent.WorldPos.Coordinate y, @NotNull BlockNBTComponent.WorldPos.Coordinate z) {
         return this.pos(BlockNBTComponent.WorldPos.worldPos(x, y, z));
      }

      @Contract("_, _, _ -> this")
      @NotNull
      default BlockNBTComponent.Builder absoluteWorldPos(int x, int y, int z) {
         return this.worldPos(BlockNBTComponent.WorldPos.Coordinate.absolute(x), BlockNBTComponent.WorldPos.Coordinate.absolute(y), BlockNBTComponent.WorldPos.Coordinate.absolute(z));
      }

      @Contract("_, _, _ -> this")
      @NotNull
      default BlockNBTComponent.Builder relativeWorldPos(int x, int y, int z) {
         return this.worldPos(BlockNBTComponent.WorldPos.Coordinate.relative(x), BlockNBTComponent.WorldPos.Coordinate.relative(y), BlockNBTComponent.WorldPos.Coordinate.relative(z));
      }
   }
}
