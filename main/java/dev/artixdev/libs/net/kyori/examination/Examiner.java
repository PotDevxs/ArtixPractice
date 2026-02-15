package dev.artixdev.libs.net.kyori.examination;

import java.util.stream.Stream;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface Examiner<R> {
   @NotNull
   default R examine(@NotNull Examinable examinable) {
      return this.examine(examinable.examinableName(), examinable.examinableProperties());
   }

   @NotNull
   R examine(@NotNull String var1, @NotNull Stream<? extends ExaminableProperty> var2);

   @NotNull
   R examine(@Nullable Object var1);

   @NotNull
   R examine(boolean var1);

   @NotNull
   R examine(@Nullable boolean[] var1);

   @NotNull
   R examine(byte var1);

   @NotNull
   R examine(@Nullable byte[] var1);

   @NotNull
   R examine(char var1);

   @NotNull
   R examine(@Nullable char[] var1);

   @NotNull
   R examine(double var1);

   @NotNull
   R examine(@Nullable double[] var1);

   @NotNull
   R examine(float var1);

   @NotNull
   R examine(@Nullable float[] var1);

   @NotNull
   R examine(int var1);

   @NotNull
   R examine(@Nullable int[] var1);

   @NotNull
   R examine(long var1);

   @NotNull
   R examine(@Nullable long[] var1);

   @NotNull
   R examine(short var1);

   @NotNull
   R examine(@Nullable short[] var1);

   @NotNull
   R examine(@Nullable String var1);
}
