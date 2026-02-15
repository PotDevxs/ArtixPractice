package dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionObject implements ReflectionObjectReader, ReflectionObjectWriter {
   private static final Map<Class<?>, Map<Class<?>, Field[]>> FIELD_CACHE = new ConcurrentHashMap();
   private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
   protected final Object object;
   private final Class<?> clazz;

   public ReflectionObject() {
      this.object = null;
      this.clazz = null;
   }

   public ReflectionObject(Object object) {
      this.object = object;
      this.clazz = object.getClass();
   }

   public ReflectionObject(Object object, Class<?> clazz) {
      this.object = object;
      this.clazz = clazz;
   }

   public boolean readBoolean(int index) {
      return (Boolean)this.read(index, Boolean.TYPE);
   }

   public byte readByte(int index) {
      return (Byte)this.read(index, Byte.TYPE);
   }

   public short readShort(int index) {
      return (Short)this.read(index, Short.TYPE);
   }

   public int readInt(int index) {
      return (Integer)this.read(index, Integer.TYPE);
   }

   public long readLong(int index) {
      return (Long)this.read(index, Long.TYPE);
   }

   public float readFloat(int index) {
      return (Float)this.read(index, Float.TYPE);
   }

   public double readDouble(int index) {
      return (Double)this.read(index, Double.TYPE);
   }

   public boolean[] readBooleanArray(int index) {
      return (boolean[])this.read(index, boolean[].class);
   }

   public byte[] readByteArray(int index) {
      return (byte[])this.read(index, byte[].class);
   }

   public short[] readShortArray(int index) {
      return (short[])this.read(index, short[].class);
   }

   public int[] readIntArray(int index) {
      return (int[])this.read(index, int[].class);
   }

   public long[] readLongArray(int index) {
      return (long[])this.read(index, long[].class);
   }

   public float[] readFloatArray(int index) {
      return (float[])this.read(index, float[].class);
   }

   public double[] readDoubleArray(int index) {
      return (double[])this.read(index, double[].class);
   }

   public String[] readStringArray(int index) {
      return (String[])this.read(index, String[].class);
   }

   public String readString(int index) {
      return (String)this.read(index, String.class);
   }

   public Object readAnyObject(int index) {
      try {
         Field f = this.clazz.getDeclaredFields()[index];
         if (!f.isAccessible()) {
            f.setAccessible(true);
         }

         try {
            return f.get(this.object);
         } catch (NullPointerException | ArrayIndexOutOfBoundsException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
         }
      } catch (ArrayIndexOutOfBoundsException ex) {
         throw new IllegalStateException("PacketEvents failed to find any field indexed " + index + " in the " + this.clazz.getSimpleName() + " class!");
      }
   }

   public <T> T readObject(int index, Class<? extends T> type) {
      return this.read(index, type);
   }

   public Enum<?> readEnumConstant(int index, Class<? extends Enum<?>> type) {
      return (Enum)this.read(index, type);
   }

   public <T> T read(int index, Class<? extends T> type) {
      try {
         Field field = this.getField(type, index);
         @SuppressWarnings("unchecked")
         T result = (T) field.get(this.object);
         return result;
      } catch (NullPointerException | ArrayIndexOutOfBoundsException | IllegalAccessException ex) {
         throw new IllegalStateException("PacketEvents failed to find a " + type.getSimpleName() + " indexed " + index + " by its type in the " + this.clazz.getName() + " class!");
      }
   }

   public void writeBoolean(int index, boolean value) {
      this.write(Boolean.TYPE, index, value);
   }

   public void writeByte(int index, byte value) {
      this.write(Byte.TYPE, index, value);
   }

   public void writeShort(int index, short value) {
      this.write(Short.TYPE, index, value);
   }

   public void writeInt(int index, int value) {
      this.write(Integer.TYPE, index, value);
   }

   public void writeLong(int index, long value) {
      this.write(Long.TYPE, index, value);
   }

   public void writeFloat(int index, float value) {
      this.write(Float.TYPE, index, value);
   }

   public void writeDouble(int index, double value) {
      this.write(Double.TYPE, index, value);
   }

   public void writeString(int index, String value) {
      this.write(String.class, index, value);
   }

   public void writeObject(int index, Object value) {
      this.write(value.getClass(), index, value);
   }

   public void writeBooleanArray(int index, boolean[] array) {
      this.write(boolean[].class, index, array);
   }

   public void writeByteArray(int index, byte[] value) {
      this.write(byte[].class, index, value);
   }

   public void writeShortArray(int index, short[] value) {
      this.write(short[].class, index, value);
   }

   public void writeIntArray(int index, int[] value) {
      this.write(int[].class, index, value);
   }

   public void writeLongArray(int index, long[] value) {
      this.write(long[].class, index, value);
   }

   public void writeFloatArray(int index, float[] value) {
      this.write(float[].class, index, value);
   }

   public void writeDoubleArray(int index, double[] value) {
      this.write(double[].class, index, value);
   }

   public void writeStringArray(int index, String[] value) {
      this.write(String[].class, index, value);
   }

   public void writeAnyObject(int index, Object value) {
      try {
         Field f = this.clazz.getDeclaredFields()[index];
         f.set(this.object, value);
      } catch (Exception ex) {
         throw new IllegalStateException("PacketEvents failed to find any field indexed " + index + " in the " + this.clazz.getSimpleName() + " class!");
      }
   }

   public void writeEnumConstant(int index, Enum<?> enumConstant) {
      try {
         this.write(enumConstant.getClass(), index, enumConstant);
      } catch (IllegalStateException ex) {
         this.write(enumConstant.getDeclaringClass(), index, enumConstant);
      }

   }

   public void write(Class<?> type, int index, Object value) throws IllegalStateException {
      Field field = this.getField(type, index);
      if (field == null) {
         throw new IllegalStateException("PacketEvents failed to find a " + type.getSimpleName() + " indexed " + index + " by its type in the " + this.clazz.getName() + " class!");
      } else {
         try {
            field.set(this.object, value);
         } catch (NullPointerException | IllegalAccessException ex) {
            ex.printStackTrace();
         }

      }
   }

   public <T> List<T> readList(int index) {
      return (List)this.read(index, List.class);
   }

   public void writeList(int index, List<?> list) {
      this.write(List.class, index, list);
   }

   private Field getField(Class<?> type, int index) {
      Map<Class<?>, Field[]> cached = (Map)FIELD_CACHE.computeIfAbsent(this.clazz, (k) -> {
         return new ConcurrentHashMap();
      });
      Field[] fields = (Field[])cached.computeIfAbsent(type, (typeClass) -> {
         return this.getFields(typeClass, this.clazz.getDeclaredFields());
      });
      if (fields.length >= index + 1) {
         return fields[index];
      } else {
         throw new IllegalStateException("PacketEvents failed to find a " + type.getSimpleName() + " indexed " + index + " by its type in the " + this.clazz.getName() + " class!");
      }
   }

   private Field[] getFields(Class<?> type, Field[] fields) {
      List<Field> ret = new ArrayList();
      for (Field field : fields) {
         if (field.getType().equals(type)) {
            if (!field.isAccessible()) {
               field.setAccessible(true);
            }
            ret.add(field);
         }
      }
      return (Field[])ret.toArray(EMPTY_FIELD_ARRAY);
   }
}
