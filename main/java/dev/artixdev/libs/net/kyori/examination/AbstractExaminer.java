package dev.artixdev.libs.net.kyori.examination;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.function.IntFunction;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public abstract class AbstractExaminer<R> implements Examiner<R> {
   @NotNull
   public R examine(@Nullable Object value) {
      if (value == null) {
         return this.nil();
      } else if (value instanceof String) {
         return this.examine((String)((String)value));
      } else if (value instanceof Examinable) {
         return this.examine((Examinable)((Examinable)value));
      } else if (value instanceof Collection) {
         @SuppressWarnings("unchecked")
         Collection<Object> collection = (Collection<Object>) value;
         return this.collection(collection);
      } else if (value instanceof Map) {
         @SuppressWarnings("unchecked")
         Map<Object, Object> map = (Map<Object, Object>) value;
         return this.map(map);
      } else if (value.getClass().isArray()) {
         Class<?> type = value.getClass().getComponentType();
         if (type.isPrimitive()) {
            if (type == Boolean.TYPE) {
               return this.examine((boolean[])value);
            }

            if (type == Byte.TYPE) {
               return this.examine((byte[])value);
            }

            if (type == Character.TYPE) {
               return this.examine((char[])value);
            }

            if (type == Double.TYPE) {
               return this.examine((double[])value);
            }

            if (type == Float.TYPE) {
               return this.examine((float[])value);
            }

            if (type == Integer.TYPE) {
               return this.examine((int[])value);
            }

            if (type == Long.TYPE) {
               return this.examine((long[])value);
            }

            if (type == Short.TYPE) {
               return this.examine((short[])value);
            }
         }

         return this.array((Object[])value);
      } else if (value instanceof Boolean) {
         return this.examine((Boolean)value);
      } else if (value instanceof Character) {
         return this.examine((Character)value);
      } else {
         if (value instanceof Number) {
            if (value instanceof Byte) {
               return this.examine((Byte)value);
            }

            if (value instanceof Double) {
               return this.examine((Double)value);
            }

            if (value instanceof Float) {
               return this.examine((Float)value);
            }

            if (value instanceof Integer) {
               return this.examine((Integer)value);
            }

            if (value instanceof Long) {
               return this.examine((Long)value);
            }

            if (value instanceof Short) {
               return this.examine((Short)value);
            }
         } else if (value instanceof BaseStream) {
            if (value instanceof Stream) {
               @SuppressWarnings("unchecked")
               Stream<Object> stream = (Stream<Object>) value;
               return this.stream(stream);
            }

            if (value instanceof DoubleStream) {
               return this.stream((DoubleStream)value);
            }

            if (value instanceof IntStream) {
               return this.stream((IntStream)value);
            }

            if (value instanceof LongStream) {
               return this.stream((LongStream)value);
            }
         }

         return this.scalar(value);
      }
   }

   @NotNull
   private <E> R array(@NotNull E[] array) {
      return this.array(array, Arrays.stream(array).map(this::examine));
   }

   @NotNull
   protected abstract <E> R array(@NotNull E[] var1, @NotNull Stream<R> var2);

   @NotNull
   private <E> R collection(@NotNull Collection<E> collection) {
      return this.collection(collection, collection.stream().map(this::examine));
   }

   @NotNull
   protected abstract <E> R collection(@NotNull Collection<E> var1, @NotNull Stream<R> var2);

   @NotNull
   public R examine(@NotNull String name, @NotNull Stream<? extends ExaminableProperty> properties) {
      return this.examinable(name, properties.map((property) -> {
         return new SimpleImmutableEntry(property.name(), property.examine(this));
      }));
   }

   @NotNull
   protected abstract R examinable(@NotNull String var1, @NotNull Stream<Entry<String, R>> var2);

   @NotNull
   private <K, V> R map(@NotNull Map<K, V> map) {
      return this.map(map, map.entrySet().stream().map((entry) -> {
         return new SimpleImmutableEntry(this.examine(entry.getKey()), this.examine(entry.getValue()));
      }));
   }

   @NotNull
   protected abstract <K, V> R map(@NotNull Map<K, V> var1, @NotNull Stream<Entry<R, R>> var2);

   @NotNull
   protected abstract R nil();

   @NotNull
   protected abstract R scalar(@NotNull Object var1);

   @NotNull
   protected abstract <T> R stream(@NotNull Stream<T> var1);

   @NotNull
   protected abstract R stream(@NotNull DoubleStream var1);

   @NotNull
   protected abstract R stream(@NotNull IntStream var1);

   @NotNull
   protected abstract R stream(@NotNull LongStream var1);

   @NotNull
   public R examine(@Nullable boolean[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable byte[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable char[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable double[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable float[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable int[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable long[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable short[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   protected abstract R array(int var1, IntFunction<R> var2);
}
