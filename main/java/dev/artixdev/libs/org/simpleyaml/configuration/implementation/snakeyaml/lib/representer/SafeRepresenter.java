package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.representer;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.YAMLException;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.external.biz.base64Coder.Base64Coder;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Tag;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.reader.StreamReader;

public class SafeRepresenter extends BaseRepresenter {
   protected Map<Class<? extends Object>, Tag> classTags;
   protected TimeZone timeZone;
   protected DumperOptions.NonPrintableStyle nonPrintableStyle;
   private static final Pattern MULTILINE_PATTERN = Pattern.compile("\n|\u0085|\u2028|\u2029");

   /** @deprecated */
   @Deprecated
   public SafeRepresenter() {
      this(new DumperOptions());
   }

   public SafeRepresenter(DumperOptions options) {
      this.timeZone = null;
      this.nullRepresenter = new SafeRepresenter.RepresentNull();
      this.representers.put(String.class, new SafeRepresenter.RepresentString());
      this.representers.put(Boolean.class, new SafeRepresenter.RepresentBoolean());
      this.representers.put(Character.class, new SafeRepresenter.RepresentString());
      this.representers.put(UUID.class, new SafeRepresenter.RepresentUuid());
      this.representers.put(byte[].class, new SafeRepresenter.RepresentByteArray());
      Represent primitiveArray = new SafeRepresenter.RepresentPrimitiveArray();
      this.representers.put(short[].class, primitiveArray);
      this.representers.put(int[].class, primitiveArray);
      this.representers.put(long[].class, primitiveArray);
      this.representers.put(float[].class, primitiveArray);
      this.representers.put(double[].class, primitiveArray);
      this.representers.put(char[].class, primitiveArray);
      this.representers.put(boolean[].class, primitiveArray);
      this.multiRepresenters.put(Number.class, new SafeRepresenter.RepresentNumber());
      this.multiRepresenters.put(List.class, new SafeRepresenter.RepresentList());
      this.multiRepresenters.put(Map.class, new SafeRepresenter.RepresentMap());
      this.multiRepresenters.put(Set.class, new SafeRepresenter.RepresentSet());
      this.multiRepresenters.put(Iterator.class, new SafeRepresenter.RepresentIterator());
      this.multiRepresenters.put((new Object[0]).getClass(), new SafeRepresenter.RepresentArray());
      this.multiRepresenters.put(Date.class, new SafeRepresenter.RepresentDate());
      this.multiRepresenters.put(Enum.class, new SafeRepresenter.RepresentEnum());
      this.multiRepresenters.put(Calendar.class, new SafeRepresenter.RepresentDate());
      this.classTags = new HashMap();
      this.nonPrintableStyle = options.getNonPrintableStyle();
   }

   protected Tag getTag(Class<?> clazz, Tag defaultTag) {
      return this.classTags.containsKey(clazz) ? (Tag)this.classTags.get(clazz) : defaultTag;
   }

   public Tag addClassTag(Class<? extends Object> clazz, Tag tag) {
      if (tag == null) {
         throw new NullPointerException("Tag must be provided.");
      } else {
         return (Tag)this.classTags.put(clazz, tag);
      }
   }

   public TimeZone getTimeZone() {
      return this.timeZone;
   }

   public void setTimeZone(TimeZone timeZone) {
      this.timeZone = timeZone;
   }

   protected class RepresentUuid implements Represent {
      public Node representData(Object data) {
         return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), new Tag(UUID.class)), data.toString());
      }
   }

   protected class RepresentByteArray implements Represent {
      public Node representData(Object data) {
         char[] binary = Base64Coder.encode((byte[])((byte[])data));
         return SafeRepresenter.this.representScalar(Tag.BINARY, String.valueOf(binary), DumperOptions.ScalarStyle.LITERAL);
      }
   }

   protected class RepresentEnum implements Represent {
      public Node representData(Object data) {
         Tag tag = new Tag(data.getClass());
         return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), ((Enum)data).name());
      }
   }

   protected class RepresentDate implements Represent {
      public Node representData(Object data) {
         Calendar calendar;
         if (data instanceof Calendar) {
            calendar = (Calendar)data;
         } else {
            calendar = Calendar.getInstance(SafeRepresenter.this.getTimeZone() == null ? TimeZone.getTimeZone("UTC") : SafeRepresenter.this.timeZone);
            calendar.setTime((Date)data);
         }

         int years = calendar.get(1);
         int months = calendar.get(2) + 1;
         int days = calendar.get(5);
         int hour24 = calendar.get(11);
         int minutes = calendar.get(12);
         int seconds = calendar.get(13);
         int millis = calendar.get(14);
         StringBuilder buffer = new StringBuilder(String.valueOf(years));

         while(buffer.length() < 4) {
            buffer.insert(0, "0");
         }

         buffer.append("-");
         if (months < 10) {
            buffer.append("0");
         }

         buffer.append(months);
         buffer.append("-");
         if (days < 10) {
            buffer.append("0");
         }

         buffer.append(days);
         buffer.append("T");
         if (hour24 < 10) {
            buffer.append("0");
         }

         buffer.append(hour24);
         buffer.append(":");
         if (minutes < 10) {
            buffer.append("0");
         }

         buffer.append(minutes);
         buffer.append(":");
         if (seconds < 10) {
            buffer.append("0");
         }

         buffer.append(seconds);
         if (millis > 0) {
            if (millis < 10) {
               buffer.append(".00");
            } else if (millis < 100) {
               buffer.append(".0");
            } else {
               buffer.append(".");
            }

            buffer.append(millis);
         }

         int gmtOffset = calendar.getTimeZone().getOffset(calendar.getTime().getTime());
         if (gmtOffset == 0) {
            buffer.append('Z');
         } else {
            if (gmtOffset < 0) {
               buffer.append('-');
               gmtOffset *= -1;
            } else {
               buffer.append('+');
            }

            int minutesOffset = gmtOffset / '\uea60';
            int hoursOffset = minutesOffset / 60;
            int partOfHour = minutesOffset % 60;
            if (hoursOffset < 10) {
               buffer.append('0');
            }

            buffer.append(hoursOffset);
            buffer.append(':');
            if (partOfHour < 10) {
               buffer.append('0');
            }

            buffer.append(partOfHour);
         }

         return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), Tag.TIMESTAMP), buffer.toString(), DumperOptions.ScalarStyle.PLAIN);
      }
   }

   protected class RepresentSet implements Represent {
      public Node representData(Object data) {
         Map<Object, Object> value = new LinkedHashMap();
         Set<Object> set = (Set)data;
         Iterator<Object> keyIterator = set.iterator();

         while (keyIterator.hasNext()) {
            Object key = keyIterator.next();
            value.put(key, (Object)null);
         }

         return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.SET), value, DumperOptions.FlowStyle.AUTO);
      }
   }

   public class RepresentMap implements Represent {
      public Node representData(Object data) {
         return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.MAP), (Map)data, DumperOptions.FlowStyle.AUTO);
      }
   }

   protected class RepresentPrimitiveArray implements Represent {
      public Node representData(Object data) {
         Class<?> type = data.getClass().getComponentType();
         if (Byte.TYPE == type) {
            return SafeRepresenter.this.representSequence(Tag.SEQ, this.asByteList(data), DumperOptions.FlowStyle.AUTO);
         } else if (Short.TYPE == type) {
            return SafeRepresenter.this.representSequence(Tag.SEQ, this.asShortList(data), DumperOptions.FlowStyle.AUTO);
         } else if (Integer.TYPE == type) {
            return SafeRepresenter.this.representSequence(Tag.SEQ, this.asIntList(data), DumperOptions.FlowStyle.AUTO);
         } else if (Long.TYPE == type) {
            return SafeRepresenter.this.representSequence(Tag.SEQ, this.asLongList(data), DumperOptions.FlowStyle.AUTO);
         } else if (Float.TYPE == type) {
            return SafeRepresenter.this.representSequence(Tag.SEQ, this.asFloatList(data), DumperOptions.FlowStyle.AUTO);
         } else if (Double.TYPE == type) {
            return SafeRepresenter.this.representSequence(Tag.SEQ, this.asDoubleList(data), DumperOptions.FlowStyle.AUTO);
         } else if (Character.TYPE == type) {
            return SafeRepresenter.this.representSequence(Tag.SEQ, this.asCharList(data), DumperOptions.FlowStyle.AUTO);
         } else if (Boolean.TYPE == type) {
            return SafeRepresenter.this.representSequence(Tag.SEQ, this.asBooleanList(data), DumperOptions.FlowStyle.AUTO);
         } else {
            throw new YAMLException("Unexpected primitive '" + type.getCanonicalName() + "'");
         }
      }

      private List<Byte> asByteList(Object in) {
         byte[] array = (byte[])((byte[])in);
         List<Byte> list = new ArrayList(array.length);

         for(int i = 0; i < array.length; ++i) {
            list.add(array[i]);
         }

         return list;
      }

      private List<Short> asShortList(Object in) {
         short[] array = (short[])((short[])in);
         List<Short> list = new ArrayList(array.length);

         for(int i = 0; i < array.length; ++i) {
            list.add(array[i]);
         }

         return list;
      }

      private List<Integer> asIntList(Object in) {
         int[] array = (int[])((int[])in);
         List<Integer> list = new ArrayList(array.length);

         for(int i = 0; i < array.length; ++i) {
            list.add(array[i]);
         }

         return list;
      }

      private List<Long> asLongList(Object in) {
         long[] array = (long[])((long[])in);
         List<Long> list = new ArrayList(array.length);

         for(int i = 0; i < array.length; ++i) {
            list.add(array[i]);
         }

         return list;
      }

      private List<Float> asFloatList(Object in) {
         float[] array = (float[])((float[])in);
         List<Float> list = new ArrayList(array.length);

         for(int i = 0; i < array.length; ++i) {
            list.add(array[i]);
         }

         return list;
      }

      private List<Double> asDoubleList(Object in) {
         double[] array = (double[])((double[])in);
         List<Double> list = new ArrayList(array.length);

         for(int i = 0; i < array.length; ++i) {
            list.add(array[i]);
         }

         return list;
      }

      private List<Character> asCharList(Object in) {
         char[] array = (char[])((char[])in);
         List<Character> list = new ArrayList(array.length);

         for(int i = 0; i < array.length; ++i) {
            list.add(array[i]);
         }

         return list;
      }

      private List<Boolean> asBooleanList(Object in) {
         boolean[] array = (boolean[])((boolean[])in);
         List<Boolean> list = new ArrayList(array.length);

         for(int i = 0; i < array.length; ++i) {
            list.add(array[i]);
         }

         return list;
      }
   }

   protected class RepresentArray implements Represent {
      public Node representData(Object data) {
         Object[] array = (Object[])((Object[])data);
         List<Object> list = Arrays.asList(array);
         return SafeRepresenter.this.representSequence(Tag.SEQ, list, DumperOptions.FlowStyle.AUTO);
      }
   }

   private static class IteratorWrapper implements Iterable<Object> {
      private final Iterator<Object> iter;

      public IteratorWrapper(Iterator<Object> iter) {
         this.iter = iter;
      }

      public Iterator<Object> iterator() {
         return this.iter;
      }
   }

   protected class RepresentIterator implements Represent {
      public Node representData(Object data) {
         Iterator<Object> iter = (Iterator)data;
         return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), new SafeRepresenter.IteratorWrapper(iter), DumperOptions.FlowStyle.AUTO);
      }
   }

   protected class RepresentList implements Represent {
      public Node representData(Object data) {
         return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), (List)data, DumperOptions.FlowStyle.AUTO);
      }
   }

   protected class RepresentNumber implements Represent {
      public Node representData(Object data) {
         Tag tag;
         String value;
         if (!(data instanceof Byte) && !(data instanceof Short) && !(data instanceof Integer) && !(data instanceof Long) && !(data instanceof BigInteger)) {
            Number number = (Number)data;
            tag = Tag.FLOAT;
            if (number.equals(Double.NaN)) {
               value = ".NaN";
            } else if (number.equals(Double.POSITIVE_INFINITY)) {
               value = ".inf";
            } else if (number.equals(Double.NEGATIVE_INFINITY)) {
               value = "-.inf";
            } else {
               value = number.toString();
            }
         } else {
            tag = Tag.INT;
            value = data.toString();
         }

         return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), value);
      }
   }

   protected class RepresentBoolean implements Represent {
      public Node representData(Object data) {
         String value;
         if (Boolean.TRUE.equals(data)) {
            value = "true";
         } else {
            value = "false";
         }

         return SafeRepresenter.this.representScalar(Tag.BOOL, value);
      }
   }

   protected class RepresentString implements Represent {
      public Node representData(Object data) {
         Tag tag = Tag.STR;
         DumperOptions.ScalarStyle style = null;
         String value = data.toString();
         if (SafeRepresenter.this.nonPrintableStyle == DumperOptions.NonPrintableStyle.BINARY && !StreamReader.isPrintable(value)) {
            tag = Tag.BINARY;
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            String checkValue = new String(bytes, StandardCharsets.UTF_8);
            if (!checkValue.equals(value)) {
               throw new YAMLException("invalid string value has occurred");
            }

            char[] binary = Base64Coder.encode(bytes);
            value = String.valueOf(binary);
            style = DumperOptions.ScalarStyle.LITERAL;
         }

         if (SafeRepresenter.this.defaultScalarStyle == DumperOptions.ScalarStyle.PLAIN && SafeRepresenter.MULTILINE_PATTERN.matcher(value).find()) {
            style = DumperOptions.ScalarStyle.LITERAL;
         }

         return SafeRepresenter.this.representScalar(tag, value, style);
      }
   }

   protected class RepresentNull implements Represent {
      public Node representData(Object data) {
         return SafeRepresenter.this.representScalar(Tag.NULL, "null");
      }
   }
}
