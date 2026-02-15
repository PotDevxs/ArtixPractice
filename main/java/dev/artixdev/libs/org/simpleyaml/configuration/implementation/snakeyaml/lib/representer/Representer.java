package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.representer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.TypeDescription;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.introspector.Property;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.introspector.PropertyUtils;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.MappingNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.NodeId;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.NodeTuple;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.ScalarNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.SequenceNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Tag;

public class Representer extends SafeRepresenter {
   protected Map<Class<? extends Object>, TypeDescription> typeDefinitions = Collections.emptyMap();

   /** @deprecated */
   @Deprecated
   public Representer() {
      this.representers.put(null, new Representer.RepresentJavaBean());
   }

   public Representer(DumperOptions options) {
      super(options);
      this.representers.put(null, new Representer.RepresentJavaBean());
   }

   public TypeDescription addTypeDescription(TypeDescription td) {
      if (Collections.EMPTY_MAP == this.typeDefinitions) {
         this.typeDefinitions = new HashMap();
      }

      if (td.getTag() != null) {
         this.addClassTag(td.getType(), td.getTag());
      }

      td.setPropertyUtils(this.getPropertyUtils());
      return (TypeDescription)this.typeDefinitions.put(td.getType(), td);
   }

   public void setPropertyUtils(PropertyUtils propertyUtils) {
      super.setPropertyUtils(propertyUtils);
      Collection<TypeDescription> tds = this.typeDefinitions.values();
      Iterator<TypeDescription> tdIterator = tds.iterator();

      while (tdIterator.hasNext()) {
         TypeDescription typeDescription = tdIterator.next();
         typeDescription.setPropertyUtils(propertyUtils);
      }

   }

   protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
      List<NodeTuple> value = new ArrayList(properties.size());
      Tag customTag = (Tag)this.classTags.get(javaBean.getClass());
      Tag tag = customTag != null ? customTag : new Tag(javaBean.getClass());
      MappingNode node = new MappingNode(tag, value, DumperOptions.FlowStyle.AUTO);
      this.representedObjects.put(javaBean, node);
      DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
      Iterator<Property> propertyIterator = properties.iterator();

      while (true) {
         NodeTuple tuple;
         do {
            if (!propertyIterator.hasNext()) {
               if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
                  node.setFlowStyle(this.defaultFlowStyle);
               } else {
                  node.setFlowStyle(bestStyle);
               }

               return node;
            }

            Property property = propertyIterator.next();
            Object memberValue = property.get(javaBean);
            Tag customPropertyTag = memberValue == null ? null : (Tag)this.classTags.get(memberValue.getClass());
            tuple = this.representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag);
         } while(tuple == null);

         if (!((ScalarNode)tuple.getKeyNode()).isPlain()) {
            bestStyle = DumperOptions.FlowStyle.BLOCK;
         }

         Node nodeValue = tuple.getValueNode();
         if (!(nodeValue instanceof ScalarNode) || !((ScalarNode)nodeValue).isPlain()) {
            bestStyle = DumperOptions.FlowStyle.BLOCK;
         }

         value.add(tuple);
      }
   }

   protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
      ScalarNode nodeKey = (ScalarNode)this.representData(property.getName());
      boolean hasAlias = this.representedObjects.containsKey(propertyValue);
      Node nodeValue = this.representData(propertyValue);
      if (propertyValue != null && !hasAlias) {
         NodeId nodeId = nodeValue.getNodeId();
         if (customTag == null) {
            if (nodeId == NodeId.scalar) {
               if (property.getType() != Enum.class && propertyValue instanceof Enum) {
                  nodeValue.setTag(Tag.STR);
               }
            } else {
               if (nodeId == NodeId.mapping && property.getType() == propertyValue.getClass() && !(propertyValue instanceof Map) && !nodeValue.getTag().equals(Tag.SET)) {
                  nodeValue.setTag(Tag.MAP);
               }

               this.checkGlobalTag(property, nodeValue, propertyValue);
            }
         }
      }

      return new NodeTuple(nodeKey, nodeValue);
   }

   protected void checkGlobalTag(Property property, Node node, Object object) {
      if (!object.getClass().isArray() || !object.getClass().getComponentType().isPrimitive()) {
         Class<?>[] arguments = property.getActualTypeArguments();
         if (arguments != null) {
            Class t;
            Iterator iter;
            if (node.getNodeId() == NodeId.sequence) {
               t = arguments[0];
               SequenceNode snode = (SequenceNode)node;
               Iterable<Object> memberList = Collections.EMPTY_LIST;
               if (object.getClass().isArray()) {
                  memberList = Arrays.asList((Object[])((Object[])object));
               } else if (object instanceof Iterable) {
                  memberList = (Iterable)object;
               }

               iter = ((Iterable)memberList).iterator();
               if (iter.hasNext()) {
                  Iterator<Node> childIterator = snode.getValue().iterator();

                  while (childIterator.hasNext()) {
                     Node childNode = childIterator.next();
                     Object member = iter.next();
                     if (member != null && t.equals(member.getClass()) && childNode.getNodeId() == NodeId.mapping) {
                        childNode.setTag(Tag.MAP);
                     }
                  }
               }
            } else if (object instanceof Set) {
               t = arguments[0];
               MappingNode mnode = (MappingNode)node;
               iter = mnode.getValue().iterator();
               Set<?> set = (Set)object;
               Iterator<?> setIterator = set.iterator();

               while (setIterator.hasNext()) {
                  Object member = setIterator.next();
                  NodeTuple tuple = (NodeTuple)iter.next();
                  Node keyNode = tuple.getKeyNode();
                  if (t.equals(member.getClass()) && keyNode.getNodeId() == NodeId.mapping) {
                     keyNode.setTag(Tag.MAP);
                  }
               }
            } else if (object instanceof Map) {
               t = arguments[0];
               Class<?> valueType = arguments[1];
               MappingNode mnode = (MappingNode)node;
               iter = mnode.getValue().iterator();

               while(iter.hasNext()) {
                  NodeTuple tuple = (NodeTuple)iter.next();
                  this.resetTag(t, tuple.getKeyNode());
                  this.resetTag(valueType, tuple.getValueNode());
               }
            }
         }

      }
   }

   private void resetTag(Class<? extends Object> type, Node node) {
      Tag tag = node.getTag();
      if (tag.matches(type)) {
         if (Enum.class.isAssignableFrom(type)) {
            node.setTag(Tag.STR);
         } else {
            node.setTag(Tag.MAP);
         }
      }

   }

   protected Set<Property> getProperties(Class<? extends Object> type) {
      return this.typeDefinitions.containsKey(type) ? ((TypeDescription)this.typeDefinitions.get(type)).getProperties() : this.getPropertyUtils().getProperties(type);
   }

   protected class RepresentJavaBean implements Represent {
      public Node representData(Object data) {
         return Representer.this.representJavaBean(Representer.this.getProperties(data.getClass()), data);
      }
   }
}
