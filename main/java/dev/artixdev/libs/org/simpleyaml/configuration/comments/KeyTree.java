package dev.artixdev.libs.org.simpleyaml.configuration.comments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import dev.artixdev.libs.org.simpleyaml.configuration.ConfigurationOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.ConfigurationSection;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class KeyTree implements Iterable<KeyTree.Node> {
   protected final KeyTree.Node root;
   protected final ConfigurationOptions options;

   public KeyTree(ConfigurationOptions options) {
      Validate.notNull(options);
      this.options = options;
      this.root = this.createNode((KeyTree.Node)null, 0, "");
   }

   public KeyTree.Node findParent(int indent) {
      return this.findParent(this.root, indent);
   }

   public KeyTree.Node getRoot() {
      return this.root;
   }

   public KeyTree.Node get(String path) {
      return this.root.get(path, false, false);
   }

   public KeyTree.Node getPriority(String path) {
      return this.root.get(path, false, true);
   }

   public KeyTree.Node getOrAdd(String path) {
      return this.root.get(path, true, false);
   }

   public KeyTree.Node add(String path) {
      return this.root.get(path, true, true);
   }

   public Set<String> keys() {
      return this.root.keys();
   }

   public List<KeyTree.Node> children() {
      return this.root.children();
   }

   public Set<Entry<String, KeyTree.Node>> entries() {
      return this.root.entries();
   }

   public ConfigurationOptions options() {
      return this.options;
   }

   public String toString() {
      return this.root.toString();
   }

   public Iterator<KeyTree.Node> iterator() {
      return this.root.iterator();
   }

   protected KeyTree.Node findParent(KeyTree.Node parent, int indent) {
      KeyTree.Node last = parent.getLast();
      return last != null && last.indent < indent ? this.findParent(last, indent) : parent;
   }

   protected KeyTree.Node createNode(KeyTree.Node parent, int indent, String key) {
      return new KeyTree.Node(parent, indent, key);
   }

   public class Node implements Iterable<KeyTree.Node> {
      protected final KeyTree.Node parent;
      protected String name;
      protected int indent;
      protected List<KeyTree.Node> children;
      protected Map<String, KeyTree.Node> indexByName;
      protected Map<String, KeyTree.Node> priorityIndex;
      protected Map<Integer, KeyTree.Node> indexByElementIndex;
      protected String comment;
      protected String sideComment;
      protected boolean isList;
      protected Integer listSize;
      protected Integer elementIndex;

      Node(KeyTree.Node parent, int indent, String name) {
         this.parent = parent;
         this.indent = indent;
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public String getComment() {
         return this.comment;
      }

      public void setComment(String comment) {
         this.comment = comment;
      }

      public String getSideComment() {
         return this.sideComment;
      }

      public void setSideComment(String sideComment) {
         this.sideComment = sideComment;
      }

      public KeyTree.Node getParent() {
         return this.parent;
      }

      public boolean isRootNode() {
         return this.parent == null;
      }

      public boolean isFirstNode() {
         if (!this.isRootNode() && this.parent.isRootNode()) {
            KeyTree.Node first = this.parent.getFirst();
            if (first.getName() == null && this.parent.children.size() > 1) {
               first = (KeyTree.Node)this.parent.children.get(1);
            }

            if (first == this) {
               Iterator<String> keys = KeyTree.this.options.configuration().getKeys(false).iterator();
               return !keys.hasNext() || ((String)keys.next()).equals(first.getName());
            }
         }

         return false;
      }

      public int getIndentation() {
         return this.indent;
      }

      protected KeyTree.Node get(String path, boolean add, boolean priority) {
         KeyTree.Node node = null;
         if (path != null && (this.indexByName == null || !this.indexByName.containsKey(path))) {
            int i = StringUtils.firstSeparatorIndex(path, KeyTree.this.options.pathSeparator());
            if (i >= 0) {
               String childPath = path.substring(0, i);
               KeyTree.Node childx = this.get(childPath, add, priority);
               if (childx == null) {
                  return null;
               }

               return childx.get(path.substring(i + 1), add, priority);
            }

            Matcher listIndex = StringUtils.LIST_INDEX.matcher(path);
            if (listIndex.matches()) {
               String child = listIndex.group(1);
               if (child != null && !child.isEmpty()) {
                  node = this.get(child, add, priority);
                  if (node == null) {
                     return null;
                  }
               } else {
                  node = this;
               }

               return node.getElement(Integer.parseInt(listIndex.group(2)), add);
            }
         }

         if (priority && this.isList) {
            node = this.priorityIndex != null ? (KeyTree.Node)this.priorityIndex.get(path) : null;
            if (add && node == null && this.indexByName != null) {
               node = (KeyTree.Node)this.indexByName.get(path);
               if (node != null) {
                  this.setPriority(path, node);
               }
            }
         } else if (this.indexByName != null) {
            node = (KeyTree.Node)this.indexByName.get(path);
         }

         if (node == null && add) {
            node = this.add(path, priority);
         }

         return node;
      }

      public KeyTree.Node get(String path) {
         return this.get(path, false, false);
      }

      public KeyTree.Node getPriority(String path) {
         return this.get(path, false, true);
      }

      protected KeyTree.Node getElement(int i, boolean add) {
         KeyTree.Node child = null;
         if (this.isList) {
            if (this.indexByElementIndex != null) {
               child = (KeyTree.Node)this.indexByElementIndex.get(i);
               if (child == null && !add) {
                  if (i < 0) {
                     child = (KeyTree.Node)this.indexByElementIndex.get(this.listSize + i);
                  } else {
                     child = (KeyTree.Node)this.indexByElementIndex.get(i - this.listSize);
                  }
               }
            }
         } else if (!add) {
            child = this.get(i);
         }

         if (child == null && add) {
            child = this.addIndexed(i);
         }

         return child;
      }

      public KeyTree.Node getElement(int i) {
         return this.getElement(i, false);
      }

      public KeyTree.Node get(int i) {
         KeyTree.Node child = null;
         if (this.hasChildren()) {
            i = this.asListIndex(i, this.children.size());
            if (i >= 0 && i < this.children.size()) {
               child = (KeyTree.Node)this.children.get(i);
            }
         }

         return child;
      }

      public KeyTree.Node getFirst() {
         return !this.hasChildren() ? null : (KeyTree.Node)this.children.get(0);
      }

      public KeyTree.Node getLast() {
         return !this.hasChildren() ? null : (KeyTree.Node)this.children.get(this.children.size() - 1);
      }

      public KeyTree.Node add(String key) {
         return this.add(key, false);
      }

      public KeyTree.Node add(int indent, String key) {
         return this.add(indent, key, false);
      }

      protected KeyTree.Node add(String key, boolean priority) {
         int indent = this == KeyTree.this.root ? 0 : this.indent + KeyTree.this.options.indent();
         return this.add(indent, key, priority);
      }

      protected KeyTree.Node add(int indent, String key, boolean priority) {
         KeyTree.Node child = KeyTree.this.createNode(this, indent, key);
         if (this.children == null) {
            this.children = new ArrayList();
         }

         this.children.add(child);
         if (this.indexByName == null) {
            this.indexByName = new LinkedHashMap();
         }

         this.indexByName.putIfAbsent(key, child);
         if (priority) {
            this.setPriority(key, child);
         }

         child.checkList();
         return child;
      }

      protected void setPriority(String key, KeyTree.Node child) {
         if (this.priorityIndex == null) {
            this.priorityIndex = new LinkedHashMap();
         }

         this.priorityIndex.putIfAbsent(key, child);
      }

      protected void checkList() {
         if (this.name != null || this.elementIndex != null) {
            Object value = this.getValue();
            if (value instanceof Collection) {
               this.isList(((Collection)value).size());
            }
         }

      }

      public Object getValue() {
         String path = this.getPath();
         return path != null ? KeyTree.this.options.configuration().get(path) : null;
      }

      public boolean hasChildren() {
         return this.children != null && !this.children.isEmpty();
      }

      public List<KeyTree.Node> children() {
         return this.hasChildren() ? Collections.unmodifiableList(this.children) : Collections.emptyList();
      }

      public Set<String> keys() {
         return this.indexByName != null ? Collections.unmodifiableSet(this.indexByName.keySet()) : Collections.emptySet();
      }

      public Set<Entry<String, KeyTree.Node>> entries() {
         return this.indexByName != null ? Collections.unmodifiableSet(this.indexByName.entrySet()) : Collections.emptySet();
      }

      public int size() {
         return this.hasChildren() ? this.children.size() : 0;
      }

      public boolean isList() {
         return this.isList;
      }

      public void isList(int listSize) {
         this.isList = true;
         this.listSize = listSize;
      }

      public Integer getListSize() {
         return this.listSize;
      }

      public void setElementIndex(int elementIndex) {
         if (this.parent != null) {
            if (this.parent.indexByElementIndex == null) {
               this.parent.indexByElementIndex = new HashMap();
            } else if (this.elementIndex != null) {
               this.parent.indexByElementIndex.remove(this.elementIndex);
            }

            this.elementIndex = elementIndex;
            this.parent.indexByElementIndex.put(this.elementIndex, this);
         }

      }

      public Integer getElementIndex() {
         return this.elementIndex;
      }

      public String getPath() {
         if (this.parent != null && this.parent != KeyTree.this.root) {
            return this.parent.isList && this.elementIndex != null ? this.indexedName(this.parent.getPath(), this.elementIndex) : this.getPathWithNameUnchecked();
         } else {
            return this.name;
         }
      }

      public String getPathWithName() {
         return this.parent != null && this.parent != KeyTree.this.root ? this.getPathWithNameUnchecked() : this.name;
      }

      private String getPathWithNameUnchecked() {
         char sep = KeyTree.this.options.pathSeparator();
         return this.parent.getPath() + sep + StringUtils.escape(this.name);
      }

      private String indexedName(String name, int listIndex) {
         return name + "[" + listIndex + "]";
      }

      private KeyTree.Node addIndexed(int i) {
         KeyTree.Node child = null;
         Object value = this.getValue();
         if (value != null) {
            int size;
            int index;
            Object key;
            if (value instanceof Collection) {
               size = ((Collection)value).size();
               if (!this.isList) {
                  this.isList(size);
               }

               if (value instanceof List) {
                  index = this.asListIndex(i, size);
                  if (index >= 0 && index < size) {
                     key = ((List)value).get(index);
                     String name = !(key instanceof String) && !(key instanceof Number) && !(key instanceof Boolean) ? null : String.valueOf(key);
                     child = this.add(name);
                  }
               }
            } else {
               if (value instanceof ConfigurationSection) {
                  value = ((ConfigurationSection)value).getValues(false);
               }

               if (value instanceof Map) {
                  size = ((Map)value).size();
                  index = this.asListIndex(i, size);
                  if (index >= 0 && index < size) {
                     key = null;
                     Iterator<?> it = ((Map)value).keySet().iterator();

                     for(int j = -1; it.hasNext(); key = it.next()) {
                        ++j;
                        if (j > index) {
                           break;
                        }
                     }

                     if (key != null) {
                        child = this.add(String.valueOf(key));
                     }
                  }
               }
            }
         }

         if (child == null) {
            child = this.add((String)null);
         }

         child.setElementIndex(i);
         child.checkList();
         return child;
      }

      private int asListIndex(int i, int size) {
         return i < 0 ? size + i : i;
      }

      protected void clearNode() {
         if (this.children != null) {
            this.children.clear();
            this.children = null;
         }

         if (this.indexByName != null) {
            this.indexByName.clear();
            this.indexByName = null;
         }

         if (this.priorityIndex != null) {
            this.priorityIndex.clear();
            this.priorityIndex = null;
         }

         if (this.indexByElementIndex != null) {
            this.indexByElementIndex.clear();
            this.indexByElementIndex = null;
         }

         if (this.parent != null && this.parent.indexByName != null && this.parent.indexByName.get(this.name) == this) {
            this.parent.indexByName.remove(this.name);
            if (this.parent.priorityIndex != null) {
               this.parent.priorityIndex.remove(this.name);
            }

            if (this.parent.indexByElementIndex != null && this.elementIndex != null) {
               this.parent.indexByElementIndex.remove(this.elementIndex);
            }
         }

      }

      protected boolean clearIf(Predicate<KeyTree.Node> condition, boolean removeFromParent) {
         if (this.children != null) {
            this.children.removeIf((child) -> {
               return child.clearIf(condition, false);
            });
         }

         if (!this.hasChildren() && condition.test(this)) {
            this.clearNode();
            if (removeFromParent && this.parent != null) {
               this.parent.children.remove(this);
            }

            return true;
         } else {
            return false;
         }
      }

      public boolean clearIf(Predicate<KeyTree.Node> condition) {
         return this.clearIf(condition, true);
      }

      public void clear() {
         this.clearNode();
         if (this.parent != null) {
            this.parent.children.remove(this);
         }

      }

      public Iterator<KeyTree.Node> iterator() {
         return this.hasChildren() ? this.children.iterator() : Collections.emptyIterator();
      }

      public String toString() {
         StringBuilder builder = new StringBuilder("{");
         builder.append("indent=").append(this.indent).append(", path=").append(StringUtils.wrap(this.getPath())).append(", name=").append(StringUtils.wrap(this.name)).append(", comment=").append(StringUtils.wrap(this.comment)).append(", side=").append(StringUtils.wrap(this.sideComment));
         builder.append(", isList=").append(this.isList);
         if (this.isList) {
            builder.append("(").append(this.listSize).append(")");
         }

         builder.append(", children=");
         if (this.children != null) {
            builder.append('(').append(this.children.size()).append(')');
            builder.append((String)this.children.stream().map(KeyTree.Node::getName).collect(Collectors.joining(", ", "[", "]")));
         } else {
            builder.append("[]");
         }

         return builder.append('}').toString();
      }
   }
}
