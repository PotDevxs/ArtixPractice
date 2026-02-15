package dev.artixdev.libs.net.kyori.adventure.nbt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.Debug;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@Debug.Renderer(
   text = "\"CompoundBinaryTag[length=\" + this.tags.size() + \"]\"",
   childrenArray = "this.tags.entrySet().toArray()",
   hasChildren = "!this.tags.isEmpty()"
)
final class CompoundBinaryTagImpl extends AbstractBinaryTag implements CompoundBinaryTag {
   static final CompoundBinaryTag EMPTY = new CompoundBinaryTagImpl(Collections.emptyMap());
   private final Map<String, BinaryTag> tags;
   private final int hashCode;

   CompoundBinaryTagImpl(Map<String, BinaryTag> tags) {
      this.tags = Collections.unmodifiableMap(tags);
      this.hashCode = tags.hashCode();
   }

   public boolean contains(@NotNull String key, @NotNull BinaryTagType<?> type) {
      BinaryTag tag = (BinaryTag)this.tags.get(key);
      return tag != null && type.test(tag.type());
   }

   @NotNull
   public Set<String> keySet() {
      return Collections.unmodifiableSet(this.tags.keySet());
   }

   @Nullable
   public BinaryTag get(String key) {
      return (BinaryTag)this.tags.get(key);
   }

   @NotNull
   public CompoundBinaryTag put(@NotNull String key, @NotNull BinaryTag tag) {
      return this.edit((map) -> {
         map.put(key, tag);
      });
   }

   @NotNull
   public CompoundBinaryTag put(@NotNull CompoundBinaryTag tag) {
      return this.edit((map) -> {
         Iterator var2 = tag.keySet().iterator();

         while(var2.hasNext()) {
            String key = (String)var2.next();
            map.put(key, tag.get(key));
         }

      });
   }

   @NotNull
   public CompoundBinaryTag put(@NotNull Map<String, ? extends BinaryTag> tags) {
      return this.edit((map) -> {
         map.putAll(tags);
      });
   }

   @NotNull
   public CompoundBinaryTag remove(@NotNull String key, @Nullable Consumer<? super BinaryTag> removed) {
      return (CompoundBinaryTag)(!this.tags.containsKey(key) ? this : this.edit((map) -> {
         BinaryTag tag = (BinaryTag)map.remove(key);
         if (removed != null) {
            removed.accept(tag);
         }

      }));
   }

   public byte getByte(@NotNull String key, byte defaultValue) {
      return this.contains(key, BinaryTagTypes.BYTE) ? ((NumberBinaryTag)this.tags.get(key)).byteValue() : defaultValue;
   }

   public short getShort(@NotNull String key, short defaultValue) {
      return this.contains(key, BinaryTagTypes.SHORT) ? ((NumberBinaryTag)this.tags.get(key)).shortValue() : defaultValue;
   }

   public int getInt(@NotNull String key, int defaultValue) {
      return this.contains(key, BinaryTagTypes.INT) ? ((NumberBinaryTag)this.tags.get(key)).intValue() : defaultValue;
   }

   public long getLong(@NotNull String key, long defaultValue) {
      return this.contains(key, BinaryTagTypes.LONG) ? ((NumberBinaryTag)this.tags.get(key)).longValue() : defaultValue;
   }

   public float getFloat(@NotNull String key, float defaultValue) {
      return this.contains(key, BinaryTagTypes.FLOAT) ? ((NumberBinaryTag)this.tags.get(key)).floatValue() : defaultValue;
   }

   public double getDouble(@NotNull String key, double defaultValue) {
      return this.contains(key, BinaryTagTypes.DOUBLE) ? ((NumberBinaryTag)this.tags.get(key)).doubleValue() : defaultValue;
   }

   @NotNull
   public byte[] getByteArray(@NotNull String key) {
      return this.contains(key, BinaryTagTypes.BYTE_ARRAY) ? ((ByteArrayBinaryTag)this.tags.get(key)).value() : new byte[0];
   }

   @NotNull
   public byte[] getByteArray(@NotNull String key, @NotNull byte[] defaultValue) {
      return this.contains(key, BinaryTagTypes.BYTE_ARRAY) ? ((ByteArrayBinaryTag)this.tags.get(key)).value() : defaultValue;
   }

   @NotNull
   public String getString(@NotNull String key, @NotNull String defaultValue) {
      return this.contains(key, BinaryTagTypes.STRING) ? ((StringBinaryTag)this.tags.get(key)).value() : defaultValue;
   }

   @NotNull
   public ListBinaryTag getList(@NotNull String key, @NotNull ListBinaryTag defaultValue) {
      return this.contains(key, BinaryTagTypes.LIST) ? (ListBinaryTag)this.tags.get(key) : defaultValue;
   }

   @NotNull
   public ListBinaryTag getList(@NotNull String key, @NotNull BinaryTagType<? extends BinaryTag> expectedType, @NotNull ListBinaryTag defaultValue) {
      if (this.contains(key, BinaryTagTypes.LIST)) {
         ListBinaryTag tag = (ListBinaryTag)this.tags.get(key);
         if (expectedType.test(tag.elementType())) {
            return tag;
         }
      }

      return defaultValue;
   }

   @NotNull
   public CompoundBinaryTag getCompound(@NotNull String key, @NotNull CompoundBinaryTag defaultValue) {
      return this.contains(key, BinaryTagTypes.COMPOUND) ? (CompoundBinaryTag)this.tags.get(key) : defaultValue;
   }

   @NotNull
   public int[] getIntArray(@NotNull String key) {
      return this.contains(key, BinaryTagTypes.INT_ARRAY) ? ((IntArrayBinaryTag)this.tags.get(key)).value() : new int[0];
   }

   @NotNull
   public int[] getIntArray(@NotNull String key, @NotNull int[] defaultValue) {
      return this.contains(key, BinaryTagTypes.INT_ARRAY) ? ((IntArrayBinaryTag)this.tags.get(key)).value() : defaultValue;
   }

   @NotNull
   public long[] getLongArray(@NotNull String key) {
      return this.contains(key, BinaryTagTypes.LONG_ARRAY) ? ((LongArrayBinaryTag)this.tags.get(key)).value() : new long[0];
   }

   @NotNull
   public long[] getLongArray(@NotNull String key, @NotNull long[] defaultValue) {
      return this.contains(key, BinaryTagTypes.LONG_ARRAY) ? ((LongArrayBinaryTag)this.tags.get(key)).value() : defaultValue;
   }

   private CompoundBinaryTag edit(Consumer<Map<String, BinaryTag>> consumer) {
      Map<String, BinaryTag> tags = new HashMap(this.tags);
      consumer.accept(tags);
      return new CompoundBinaryTagImpl(tags);
   }

   public boolean equals(Object that) {
      return this == that || that instanceof CompoundBinaryTagImpl && this.tags.equals(((CompoundBinaryTagImpl)that).tags);
   }

   public int hashCode() {
      return this.hashCode;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("tags", (Object)this.tags));
   }

   @NotNull
   public Iterator<Entry<String, ? extends BinaryTag>> iterator() {
      @SuppressWarnings("unchecked")
      Iterator<Entry<String, ? extends BinaryTag>> result = (Iterator<Entry<String, ? extends BinaryTag>>) (Iterator<?>) this.tags.entrySet().iterator();
      return result;
   }

   public void forEach(@NotNull Consumer<? super Entry<String, ? extends BinaryTag>> action) {
      this.tags.entrySet().forEach((Consumer)Objects.requireNonNull(action, "action"));
   }
}
