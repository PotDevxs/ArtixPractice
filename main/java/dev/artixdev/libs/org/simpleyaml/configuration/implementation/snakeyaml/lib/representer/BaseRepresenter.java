package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.representer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.introspector.PropertyUtils;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.AnchorNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.MappingNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.NodeTuple;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.ScalarNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.SequenceNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Tag;

public abstract class BaseRepresenter {
   protected final Map<Class<?>, Represent> representers = new HashMap();
   protected Represent nullRepresenter;
   protected final Map<Class<?>, Represent> multiRepresenters = new LinkedHashMap();
   protected DumperOptions.ScalarStyle defaultScalarStyle = null;
   protected DumperOptions.FlowStyle defaultFlowStyle;
   protected final Map<Object, Node> representedObjects;
   protected Object objectToRepresent;
   private PropertyUtils propertyUtils;
   private boolean explicitPropertyUtils;

   public BaseRepresenter() {
      this.defaultFlowStyle = DumperOptions.FlowStyle.AUTO;
      this.representedObjects = new IdentityHashMap<Object, Node>() {
         private static final long serialVersionUID = -5576159264232131854L;

         public Node put(Object key, Node value) {
            return (Node)super.put(key, new AnchorNode(value));
         }
      };
      this.explicitPropertyUtils = false;
   }

   public Node represent(Object data) {
      Node node = this.representData(data);
      this.representedObjects.clear();
      this.objectToRepresent = null;
      return node;
   }

   protected final Node representData(Object data) {
      this.objectToRepresent = data;
      Node node;
      if (this.representedObjects.containsKey(this.objectToRepresent)) {
         node = (Node)this.representedObjects.get(this.objectToRepresent);
         return node;
      } else if (data == null) {
         node = this.nullRepresenter.representData((Object)null);
         return node;
      } else {
         Class<?> clazz = data.getClass();
         Represent representer;
         if (this.representers.containsKey(clazz)) {
            representer = (Represent)this.representers.get(clazz);
            node = representer.representData(data);
         } else {
            Iterator<Class<?>> reprIterator = this.multiRepresenters.keySet().iterator();

            while (reprIterator.hasNext()) {
               Class<?> repr = reprIterator.next();
               if (repr != null && repr.isInstance(data)) {
                  representer = (Represent)this.multiRepresenters.get(repr);
                  node = representer.representData(data);
                  return node;
               }
            }

            if (this.multiRepresenters.containsKey((Object)null)) {
               representer = (Represent)this.multiRepresenters.get((Object)null);
               node = representer.representData(data);
            } else {
               representer = (Represent)this.representers.get((Object)null);
               node = representer.representData(data);
            }
         }

         return node;
      }
   }

   protected Node representScalar(Tag tag, String value, DumperOptions.ScalarStyle style) {
      if (style == null) {
         style = this.defaultScalarStyle;
      }

      Node node = new ScalarNode(tag, value, (Mark)null, (Mark)null, style);
      return node;
   }

   protected Node representScalar(Tag tag, String value) {
      return this.representScalar(tag, value, (DumperOptions.ScalarStyle)null);
   }

   protected Node representSequence(Tag tag, Iterable<?> sequence, DumperOptions.FlowStyle flowStyle) {
      int size = 10;
      if (sequence instanceof List) {
         size = ((List)sequence).size();
      }

      List<Node> value = new ArrayList(size);
      SequenceNode node = new SequenceNode(tag, value, flowStyle);
      this.representedObjects.put(this.objectToRepresent, node);
      DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;

      Node nodeItem;
      for (Iterator<?> sequenceIterator = sequence.iterator(); sequenceIterator.hasNext(); value.add(nodeItem)) {
         Object item = sequenceIterator.next();
         nodeItem = this.representData(item);
         if (!(nodeItem instanceof ScalarNode) || !((ScalarNode)nodeItem).isPlain()) {
            bestStyle = DumperOptions.FlowStyle.BLOCK;
         }
      }

      if (flowStyle == DumperOptions.FlowStyle.AUTO) {
         if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
            node.setFlowStyle(this.defaultFlowStyle);
         } else {
            node.setFlowStyle(bestStyle);
         }
      }

      return node;
   }

   protected Node representMapping(Tag tag, Map<?, ?> mapping, DumperOptions.FlowStyle flowStyle) {
      List<NodeTuple> value = new ArrayList(mapping.size());
      MappingNode node = new MappingNode(tag, value, flowStyle);
      this.representedObjects.put(this.objectToRepresent, node);
      DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;

      Node nodeKey;
      Node nodeValue;
      for (Entry<?, ?> entry : mapping.entrySet()) {
         nodeKey = this.representData(entry.getKey());
         nodeValue = this.representData(entry.getValue());
         if (!(nodeKey instanceof ScalarNode) || !((ScalarNode)nodeKey).isPlain()) {
            bestStyle = DumperOptions.FlowStyle.BLOCK;
         }

         if (!(nodeValue instanceof ScalarNode) || !((ScalarNode)nodeValue).isPlain()) {
            bestStyle = DumperOptions.FlowStyle.BLOCK;
         }
         value.add(new NodeTuple(nodeKey, nodeValue));
      }

      if (flowStyle == DumperOptions.FlowStyle.AUTO) {
         if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
            node.setFlowStyle(this.defaultFlowStyle);
         } else {
            node.setFlowStyle(bestStyle);
         }
      }

      return node;
   }

   public void setDefaultScalarStyle(DumperOptions.ScalarStyle defaultStyle) {
      this.defaultScalarStyle = defaultStyle;
   }

   public DumperOptions.ScalarStyle getDefaultScalarStyle() {
      return this.defaultScalarStyle == null ? DumperOptions.ScalarStyle.PLAIN : this.defaultScalarStyle;
   }

   public void setDefaultFlowStyle(DumperOptions.FlowStyle defaultFlowStyle) {
      this.defaultFlowStyle = defaultFlowStyle;
   }

   public DumperOptions.FlowStyle getDefaultFlowStyle() {
      return this.defaultFlowStyle;
   }

   public void setPropertyUtils(PropertyUtils propertyUtils) {
      this.propertyUtils = propertyUtils;
      this.explicitPropertyUtils = true;
   }

   public final PropertyUtils getPropertyUtils() {
      if (this.propertyUtils == null) {
         this.propertyUtils = new PropertyUtils();
      }

      return this.propertyUtils;
   }

   public final boolean isExplicitPropertyUtils() {
      return this.explicitPropertyUtils;
   }
}
