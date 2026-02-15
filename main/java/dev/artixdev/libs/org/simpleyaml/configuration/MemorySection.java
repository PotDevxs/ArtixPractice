package dev.artixdev.libs.org.simpleyaml.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import dev.artixdev.libs.org.simpleyaml.configuration.serialization.ConfigurationSerializable;
import dev.artixdev.libs.org.simpleyaml.utils.NumberConversions;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class MemorySection implements ConfigurationSection {
   protected final Map<String, Object> map = new LinkedHashMap();
   private final Configuration root;
   private final ConfigurationSection parent;
   private final String path;
   private final String fullPath;

   protected MemorySection() {
      if (!(this instanceof Configuration)) {
         throw new IllegalStateException("Cannot construct a root MemorySection when not a Configuration");
      } else {
         this.path = "";
         this.fullPath = "";
         this.parent = null;
         this.root = (Configuration)this;
      }
   }

   protected MemorySection(ConfigurationSection parent, String path) {
      Validate.notNull(parent, "Parent cannot be null");
      Validate.notNull(path, "Path cannot be null");
      this.path = path;
      this.parent = parent;
      this.root = parent.getRoot();
      Validate.notNull(this.root, "Path cannot be orphaned");
      this.fullPath = createPath(parent, path);
   }

   public static String createPath(ConfigurationSection section, String key) {
      return createPath(section, key, section == null ? null : section.getRoot());
   }

   public static String createPath(ConfigurationSection section, String key, ConfigurationSection relativeTo) {
      Validate.notNull(section, "Cannot create path without a section");
      Configuration root = section.getRoot();
      if (root == null) {
         throw new IllegalStateException("Cannot create path without a root");
      } else {
         char separator = root.options().pathSeparator();
         StringBuilder builder = new StringBuilder();

         for(ConfigurationSection parent = section; parent != null && parent != relativeTo; parent = parent.getParent()) {
            if (builder.length() > 0) {
               builder.insert(0, separator);
            }

            builder.insert(0, parent.getName());
         }

         if (key != null && key.length() > 0) {
            if (builder.length() > 0) {
               builder.append(separator);
            }

            builder.append(key);
         }

         return builder.toString();
      }
   }

   public Set<String> getKeys(boolean deep) {
      Set<String> result = new LinkedHashSet();
      Configuration root = this.getRoot();
      if (root != null && root.options().copyDefaults()) {
         ConfigurationSection defaults = this.getDefaultSection();
         if (defaults != null) {
            result.addAll(defaults.getKeys(deep));
         }
      }

      this.mapChildrenKeys(result, this, deep);
      return result;
   }

   public Map<String, Object> getValues(boolean deep) {
      Map<String, Object> result = new LinkedHashMap();
      Configuration root = this.getRoot();
      if (root != null && root.options().copyDefaults()) {
         ConfigurationSection defaults = this.getDefaultSection();
         if (defaults != null) {
            result.putAll(defaults.getValues(deep));
         }
      }

      this.mapChildrenValues(result, this, deep);
      return result;
   }

   public Map<String, Object> getMapValues(boolean deep) {
      return (Map)this.getValues(deep).entrySet().stream().map((entry) -> {
         String key = (String)entry.getKey();
         Object value = entry.getValue();
         return value instanceof ConfigurationSection ? new SimpleEntry(key, ((ConfigurationSection)value).getMapValues(deep)) : new SimpleEntry(key, value);
      }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
   }

   public boolean contains(String path) {
      return this.get(path) != null;
   }

   public boolean isSet(String path) {
      Configuration root = this.getRoot();
      if (root == null) {
         return false;
      } else if (root.options().copyDefaults()) {
         return this.contains(path);
      } else {
         return this.get(path, (Object)null) != null;
      }
   }

   public int size() {
      return this.map.size();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   public String getCurrentPath() {
      return this.fullPath;
   }

   public String getName() {
      return this.path;
   }

   public Configuration getRoot() {
      return this.root;
   }

   public ConfigurationSection getParent() {
      return this.parent;
   }

   public Object get(String path) {
      return this.get(path, this.getDefault(path));
   }

   public Object get(String path, Object def) {
      Validate.notNull(path, "Path cannot be null");
      if (path.length() == 0) {
         return this;
      } else {
         Configuration root = this.getRoot();
         if (root == null) {
            throw new IllegalStateException("Cannot access section without a root");
         } else {
            char separator = root.options().pathSeparator();
            int i1 = -1;
            Object section = this;

            do {
               int i2;
               String node;
               if ((i1 = StringUtils.firstSeparatorIndex(path, separator, i2 = i1 + 1)) == -1) {
                  node = path.substring(i2);
                  return this.getObject(section, node, def);
               }

               node = path.substring(i2, i1);
               section = this.getSection(section, node);
            } while(section != null);

            return def;
         }
      }
   }

   public void set(String path, Object value) {
      Validate.notNull(path, "Path cannot be null");
      Configuration root = this.getRoot();
      if (root == null) {
         throw new IllegalStateException("Cannot use section without a root");
      } else {
         char separator = root.options().pathSeparator();
         int i1 = -1;
         Object section = this;

         int i2;
         String node;
         while((i1 = StringUtils.firstSeparatorIndex(path, separator, i2 = i1 + 1)) != -1) {
            node = path.substring(i2, i1);
            Object subSection = this.getSection(section, node);
            if (subSection == null) {
               if (!(section instanceof ConfigurationSection)) {
                  return;
               }

               section = ((ConfigurationSection)section).createSection(node);
            } else {
               section = subSection;
            }
         }

         node = path.substring(i2);
         this.setObject(section, node, value);
      }
   }

   public ConfigurationSection createSection(String path) {
      Validate.notEmpty(path, "Cannot create section at empty path");
      Configuration root = this.getRoot();
      if (root == null) {
         throw new IllegalStateException("Cannot create section without a root");
      } else {
         char separator = root.options().pathSeparator();
         int i1 = -1;
         Object section = this;

         int i2;
         String key;
         while((i1 = StringUtils.firstSeparatorIndex(path, separator, i2 = i1 + 1)) != -1) {
            key = path.substring(i2, i1);
            Object subSection = this.getSection(section, key);
            if (subSection == null) {
               if (!(section instanceof ConfigurationSection)) {
                  return null;
               }

               section = ((ConfigurationSection)section).createSection(key);
            } else {
               section = subSection;
            }
         }

         key = path.substring(i2);
         if (section == this) {
            ConfigurationSection result = new MemorySection(this, key);
            this.map.put(key, result);
            return result;
         } else if (section instanceof ConfigurationSection) {
            return ((ConfigurationSection)section).createSection(key);
         } else {
            return null;
         }
      }
   }

   private Object getObject(Object section, String node, Object def) {
      Matcher listIndex = StringUtils.LIST_INDEX.matcher(node);
      if (!listIndex.matches()) {
         return this.getObjectRaw(section, node, def);
      } else {
         Object object = findIndexed(section, listIndex.group(1), Integer.parseInt(listIndex.group(2)));
         return object != null ? object : def;
      }
   }

   private Object getObjectRaw(Object section, String node, Object def) {
      if (section == this) {
         section = this.map;
      }

      if (section instanceof ConfigurationSection) {
         return ((ConfigurationSection)section).get(node, def);
      } else {
         if (section instanceof ConfigurationSerializable) {
            section = ((ConfigurationSerializable)section).serialize();
         }

         return section instanceof Map ? ((Map)section).getOrDefault(node, def) : def;
      }
   }

   private <K, V> void setObject(Object section, K node, V value) {
      Matcher listIndex = StringUtils.LIST_INDEX.matcher((CharSequence)node);
      if (!listIndex.matches()) {
         this.setObjectRaw(section, node, value);
      } else {
         Object it = null;
         String iterableNode = listIndex.group(1);
         if (iterableNode != null && !iterableNode.isEmpty()) {
            it = find(section, iterableNode);
         }

         if (it instanceof MemorySection) {
            it = ((MemorySection)section).map;
         } else if (it instanceof ConfigurationSection) {
            it = ((ConfigurationSection)section).getValues(false);
         }

         if (it != null) {
            int index = Integer.parseInt(listIndex.group(2));
            if (it instanceof Map) {
               int len = ((Map)it).size();
               index = asListIndex(index, len);
               if (index >= 0 && index < len) {
                  K key = null;
                  Iterator<K> iterator = ((Map)it).keySet().iterator();

                  for(int j = -1; iterator.hasNext(); key = iterator.next()) {
                     ++j;
                     if (j > index) {
                        break;
                     }
                  }

                  this.setObjectRaw(section, key, value);
               }
            } else {
               int len;
               if (it instanceof List) {
                  List<V> list = (List)it;
                  len = list.size();
                  if (value == null && index == -1 && !list.isEmpty()) {
                     list.remove(len - 1);
                  } else if (value == null || index != -1 && index != len) {
                     index = asListIndex(index, len);
                     if (index >= 0 && index < len) {
                        if (value == null) {
                           list.remove(index);
                        } else {
                           list.set(index, value);
                        }
                     }
                  } else {
                     list.add(value);
                  }
               } else if (it instanceof Collection && value != null) {
                  Collection<V> collection = (Collection)it;
                  len = collection.size();
                  if (index == -1 || index == 0 || index == len) {
                     ((Collection)it).add(value);
                  }
               }
            }
         }
      }

   }

   private <K, V> void setObjectRaw(Object section, K key, V value) {
      if (key != null) {
         if (section == this) {
            section = this.map;
         }

         if (section instanceof ConfigurationSection) {
            ((ConfigurationSection)section).set(String.valueOf(key), value);
         } else if (section instanceof Map) {
            if (value == null) {
               ((Map)section).remove(key);
            } else {
               ((Map)section).put(key, value);
            }
         }
      }

   }

   private Object getSection(Object parent, String node) {
      Matcher listIndex = StringUtils.LIST_INDEX.matcher(node);
      if (!listIndex.matches()) {
         return findSection(parent, node);
      } else {
         Object section = findIndexed(parent, listIndex.group(1), Integer.parseInt(listIndex.group(2)));
         return isSection(section) ? section : null;
      }
   }

   private static boolean isSection(Object section) {
      return section instanceof ConfigurationSection || section instanceof ConfigurationSerializable || section instanceof Map;
   }

   private static Object findSection(Object section, String node) {
      if (section instanceof ConfigurationSection) {
         ConfigurationSection configurationSection = (ConfigurationSection)section;
         section = configurationSection.get(node, (Object)null);
         if (section == null && configurationSection instanceof MemorySection) {
            section = ((MemorySection)configurationSection).getDefault(node);
            if (section instanceof ConfigurationSection) {
               section = configurationSection.createSection(node);
            }
         }
      } else if (section instanceof Map) {
         section = ((Map)section).get(node);
      }

      return isSection(section) ? section : null;
   }

   private static Object find(Object section, String node) {
      Object it = null;
      if (section instanceof ConfigurationSection) {
         ConfigurationSection configurationSection = (ConfigurationSection)section;
         it = configurationSection.get(node, (Object)null);
         if (it == null && configurationSection instanceof MemorySection) {
            it = ((MemorySection)configurationSection).getDefault(node);
         }
      } else if (section instanceof Map) {
         it = ((Map)section).get(node);
      }

      return it;
   }

   private static Iterable<?> getIterable(Object section, String node) {
      if (node != null && !node.isEmpty()) {
         section = find(section, node);
      }

      if (section instanceof ConfigurationSection) {
         section = ((ConfigurationSection)section).getValues(false).values();
      }

      return section instanceof Iterable ? (Iterable)section : null;
   }

   private static Object findIndexed(Object section, String iterableNode, int index) {
      return getIndexed(getIterable(section, iterableNode), index);
   }

   private static Object getIndexed(Iterable<?> iterable, int index) {
      if (iterable == null) {
         return null;
      } else {
         if (iterable instanceof Collection) {
            int len = ((Collection)iterable).size();
            index = asListIndex(index, len);
            if (index < 0 || index >= len) {
               return null;
            }
         }

         if (iterable instanceof List) {
            return ((List)iterable).get(index);
         } else {
            Object value = null;
            Iterator<?> it = iterable.iterator();
            if (index >= 0) {
               int i = -1;

               while(it.hasNext()) {
                  value = it.next();
                  ++i;
                  if (i == index) {
                     break;
                  }
               }

               if (i != index) {
                  return null;
               }
            } else {
               LinkedList<Object> window = new LinkedList();
               int windowSize = -index;
               int filled = 0;

               while(it.hasNext()) {
                  window.add(it.next());
                  if (filled == windowSize) {
                     window.removeFirst();
                  } else {
                     ++filled;
                  }
               }

               if (filled < windowSize) {
                  return null;
               }

               value = window.getFirst();
            }

            return value;
         }
      }
   }

   private static int asListIndex(int i, int size) {
      return i < 0 ? size + i : i;
   }

   public String getString(String path) {
      Object def = this.getDefault(path);
      return this.getString(path, def != null ? def.toString() : null);
   }

   public String getString(String path, String def) {
      Object val = this.get(path, def);
      return val != null ? val.toString() : def;
   }

   public boolean isString(String path) {
      Object val = this.get(path);
      return val instanceof String;
   }

   public int getInt(String path) {
      Object def = this.getDefault(path);
      return this.getInt(path, def instanceof Number ? NumberConversions.toInt(def) : 0);
   }

   public int getInt(String path, int def) {
      Object val = this.get(path, def);
      return val instanceof Number ? NumberConversions.toInt(val) : def;
   }

   public boolean isInt(String path) {
      Object val = this.get(path);
      return val instanceof Integer;
   }

   public boolean getBoolean(String path) {
      Object def = this.getDefault(path);
      return this.getBoolean(path, def instanceof Boolean ? (Boolean)def : false);
   }

   public boolean getBoolean(String path, boolean def) {
      Object val = this.get(path, def);
      return val instanceof Boolean ? (Boolean)val : def;
   }

   public boolean isBoolean(String path) {
      Object val = this.get(path);
      return val instanceof Boolean;
   }

   public double getDouble(String path) {
      Object def = this.getDefault(path);
      return this.getDouble(path, def instanceof Number ? NumberConversions.toDouble(def) : 0.0D);
   }

   public double getDouble(String path, double def) {
      Object val = this.get(path, def);
      return val instanceof Number ? NumberConversions.toDouble(val) : def;
   }

   public boolean isDouble(String path) {
      Object val = this.get(path);
      return val instanceof Double;
   }

   public long getLong(String path) {
      Object def = this.getDefault(path);
      return this.getLong(path, def instanceof Number ? NumberConversions.toLong(def) : 0L);
   }

   public long getLong(String path, long def) {
      Object val = this.get(path, def);
      return val instanceof Number ? NumberConversions.toLong(val) : def;
   }

   public boolean isLong(String path) {
      Object val = this.get(path);
      return val instanceof Long;
   }

   public List<?> getList(String path) {
      Object def = this.getDefault(path);
      return this.getList(path, def instanceof List ? (List)def : null);
   }

   public boolean isList(String path) {
      Object val = this.get(path);
      return val instanceof List;
   }

   public List<String> getStringList(String path) {
      List<?> list = this.getList(path);
      if (list == null) {
         return new ArrayList(0);
      } else {
         List<String> result = new ArrayList();
         Iterator<?> iterator = list.iterator();

         while (true) {
            Object object;
            do {
               if (!iterator.hasNext()) {
                  return result;
               }

               object = iterator.next();
            } while (!(object instanceof String) && !this.isPrimitiveWrapper(object));

            result.add(String.valueOf(object));
         }
      }
   }

   public List<Integer> getIntegerList(String path) {
      List<?> list = this.getList(path);
      if (list == null) {
         return new ArrayList(0);
      } else {
         List<Integer> result = new ArrayList();
         Iterator<?> iterator = list.iterator();

         while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Integer) {
               result.add((Integer)object);
            } else if (object instanceof String) {
               try {
                  result.add(Integer.valueOf((String)object));
               } catch (Exception ignored) {
               }
            } else if (object instanceof Character) {
               result.add(Integer.valueOf((Character)object));
            } else if (object instanceof Number) {
               result.add(((Number)object).intValue());
            }
         }

         return result;
      }
   }

   public List<Boolean> getBooleanList(String path) {
      List<?> list = this.getList(path);
      if (list == null) {
         return new ArrayList(0);
      } else {
         List<Boolean> result = new ArrayList();
         Iterator<?> iterator = list.iterator();

         while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Boolean) {
               result.add((Boolean)object);
            } else if (object instanceof String) {
               if (Boolean.TRUE.toString().equals(object)) {
                  result.add(true);
               } else if (Boolean.FALSE.toString().equals(object)) {
                  result.add(false);
               }
            }
         }

         return result;
      }
   }

   public List<Double> getDoubleList(String path) {
      List<?> list = this.getList(path);
      if (list == null) {
         return new ArrayList(0);
      } else {
         List<Double> result = new ArrayList();
         Iterator<?> iterator = list.iterator();

         while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Double) {
               result.add((Double)object);
            } else if (object instanceof String) {
               try {
                  result.add(Double.valueOf((String)object));
               } catch (Exception ignored) {
               }
            } else if (object instanceof Character) {
               result.add((double)(Character)object);
            } else if (object instanceof Number) {
               result.add(((Number)object).doubleValue());
            }
         }

         return result;
      }
   }

   public List<Float> getFloatList(String path) {
      List<?> list = this.getList(path);
      if (list == null) {
         return new ArrayList(0);
      } else {
         List<Float> result = new ArrayList();
         Iterator<?> iterator = list.iterator();

         while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Float) {
               result.add((Float)object);
            } else if (object instanceof String) {
               try {
                  result.add(Float.valueOf((String)object));
               } catch (Exception ignored) {
               }
            } else if (object instanceof Character) {
               result.add((float)(Character)object);
            } else if (object instanceof Number) {
               result.add(((Number)object).floatValue());
            }
         }

         return result;
      }
   }

   public List<Long> getLongList(String path) {
      List<?> list = this.getList(path);
      if (list == null) {
         return new ArrayList(0);
      } else {
         List<Long> result = new ArrayList();
         Iterator<?> iterator = list.iterator();

         while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Long) {
               result.add((Long)object);
            } else if (object instanceof String) {
               try {
                  result.add(Long.valueOf((String)object));
               } catch (Exception ignored) {
               }
            } else if (object instanceof Character) {
               result.add((long)(Character)object);
            } else if (object instanceof Number) {
               result.add(((Number)object).longValue());
            }
         }

         return result;
      }
   }

   public List<Byte> getByteList(String path) {
      List<?> list = this.getList(path);
      if (list == null) {
         return new ArrayList(0);
      } else {
         List<Byte> result = new ArrayList();
         Iterator<?> iterator = list.iterator();

         while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Byte) {
               result.add((Byte)object);
            } else if (object instanceof String) {
               try {
                  result.add(Byte.valueOf((String)object));
               } catch (Exception ignored) {
               }
            } else if (object instanceof Character) {
               result.add((byte) ((Character) object).charValue());
            } else if (object instanceof Number) {
               result.add(((Number)object).byteValue());
            }
         }

         return result;
      }
   }

   public List<Character> getCharacterList(String path) {
      List<?> list = this.getList(path);
      if (list == null) {
         return new ArrayList(0);
      } else {
         List<Character> result = new ArrayList();
         Iterator<?> iterator = list.iterator();

         while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Character) {
               result.add((Character)object);
            } else if (object instanceof String) {
               String str = (String)object;
               if (str.length() == 1) {
                  result.add(str.charAt(0));
               }
            } else if (object instanceof Number) {
               result.add((char)((Number)object).intValue());
            }
         }

         return result;
      }
   }

   public List<Short> getShortList(String path) {
      List<?> list = this.getList(path);
      if (list == null) {
         return new ArrayList(0);
      } else {
         List<Short> result = new ArrayList();
         Iterator<?> iterator = list.iterator();

         while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Short) {
               result.add((Short)object);
            } else if (object instanceof String) {
               try {
                  result.add(Short.valueOf((String)object));
               } catch (Exception ignored) {
               }
            } else if (object instanceof Character) {
               result.add((short) ((Character) object).charValue());
            } else if (object instanceof Number) {
               result.add(((Number)object).shortValue());
            }
         }

         return result;
      }
   }

   public List<Map<?, ?>> getMapList(String path) {
      List<?> list = this.getList(path);
         List<Map<?, ?>> result = new ArrayList();
      if (list == null) {
         return result;
      } else {
         Iterator<?> iterator = list.iterator();

         while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Map) {
               result.add((Map)object);
            }
         }

         return result;
      }
   }

   public ConfigurationSection getConfigurationSection(String path) {
      Object val = this.get(path, (Object)null);
      if (val != null) {
         return val instanceof ConfigurationSection ? (ConfigurationSection)val : null;
      } else {
         val = this.get(path, this.getDefault(path));
         return val instanceof ConfigurationSection ? this.createSection(path) : null;
      }
   }

   public boolean isConfigurationSection(String path) {
      Object val = this.get(path);
      return val instanceof ConfigurationSection;
   }

   public ConfigurationSection getDefaultSection() {
      Configuration root = this.getRoot();
      Configuration defaults = root == null ? null : root.getDefaults();
      return defaults != null && defaults.isConfigurationSection(this.getCurrentPath()) ? defaults.getConfigurationSection(this.getCurrentPath()) : null;
   }

   public void addDefault(String path, Object value) {
      Validate.notNull(path, "Path cannot be null");
      Configuration root = this.getRoot();
      if (root == null) {
         throw new IllegalStateException("Cannot add default without root");
      } else if (root == this) {
         throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation");
      } else {
         root.addDefault(createPath(this, path), value);
      }
   }

   public ConfigurationSection createSection(String path, Map<?, ?> map) {
      ConfigurationSection section = this.createSection(path);
      Iterator<? extends Entry<?, ?>> entryIterator = map.entrySet().iterator();

      while (entryIterator.hasNext()) {
         Entry<?, ?> entry = entryIterator.next();
         if (entry.getValue() instanceof Map) {
            section.createSection(entry.getKey().toString(), (Map)entry.getValue());
         } else {
            section.set(entry.getKey().toString(), entry.getValue());
         }
      }

      return section;
   }

   public List<?> getList(String path, List<?> def) {
      Object val = this.get(path, def);
      return (List)((List)(val instanceof List ? val : def));
   }

   public String toString() {
      Configuration root = this.getRoot();
      return this.getClass().getSimpleName() + "[path='" + this.getCurrentPath() + "', root='" + (root == null ? null : root.getClass().getSimpleName()) + "']";
   }

   protected boolean isPrimitiveWrapper(Object input) {
      return input instanceof Integer || input instanceof Boolean || input instanceof Character || input instanceof Byte || input instanceof Short || input instanceof Double || input instanceof Long || input instanceof Float;
   }

   protected Object getDefault(String path) {
      Validate.notNull(path, "Path cannot be null");
      Configuration root = this.getRoot();
      Configuration defaults = root == null ? null : root.getDefaults();
      return defaults == null ? null : defaults.get(createPath(this, path));
   }

   protected void mapChildrenKeys(Set<String> output, ConfigurationSection section, boolean deep) {
      Iterator<Entry<String, Object>> entryIterator;
      if (section instanceof MemorySection) {
         MemorySection sec = (MemorySection)section;
         entryIterator = sec.map.entrySet().iterator();

         while (entryIterator.hasNext()) {
            Entry<String, Object> entry = entryIterator.next();
            output.add(createPath(section, entry.getKey(), this));
            if (deep && entry.getValue() instanceof ConfigurationSection) {
               ConfigurationSection subsection = (ConfigurationSection)entry.getValue();
               this.mapChildrenKeys(output, subsection, deep);
            }
         }
      } else {
         Set<String> keys = section.getKeys(deep);
         Iterator<String> keyIterator = keys.iterator();

         while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            output.add(createPath(section, key, this));
         }
      }

   }

   protected void mapChildrenValues(Map<String, Object> output, ConfigurationSection section, boolean deep) {
      Iterator<Entry<String, Object>> entryIterator;
      Entry<String, Object> entry;
      if (section instanceof MemorySection) {
         MemorySection sec = (MemorySection)section;
         entryIterator = sec.map.entrySet().iterator();

         while (entryIterator.hasNext()) {
            entry = entryIterator.next();
            output.put(createPath(section, entry.getKey(), this), entry.getValue());
            if (deep && entry.getValue() instanceof ConfigurationSection) {
               this.mapChildrenValues(output, (ConfigurationSection)entry.getValue(), true);
            }
         }
      } else {
         Map<String, Object> values = section.getValues(deep);
         entryIterator = values.entrySet().iterator();

         while (entryIterator.hasNext()) {
            entry = entryIterator.next();
            output.put(createPath(section, entry.getKey(), this), entry.getValue());
         }
      }

   }
}
