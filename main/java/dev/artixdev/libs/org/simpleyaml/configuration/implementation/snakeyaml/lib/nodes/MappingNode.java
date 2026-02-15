package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes;

import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public class MappingNode extends CollectionNode<NodeTuple> {
   private List<NodeTuple> value;
   private boolean merged;

   public MappingNode(Tag tag, boolean resolved, List<NodeTuple> value, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
      super(tag, startMark, endMark, flowStyle);
      this.merged = false;
      if (value == null) {
         throw new NullPointerException("value in a Node is required.");
      } else {
         this.value = value;
         this.resolved = resolved;
      }
   }

   public MappingNode(Tag tag, List<NodeTuple> value, DumperOptions.FlowStyle flowStyle) {
      this(tag, true, value, (Mark)null, (Mark)null, (DumperOptions.FlowStyle)flowStyle);
   }

   /** @deprecated */
   @Deprecated
   public MappingNode(Tag tag, boolean resolved, List<NodeTuple> value, Mark startMark, Mark endMark, Boolean flowStyle) {
      this(tag, resolved, value, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
   }

   /** @deprecated */
   @Deprecated
   public MappingNode(Tag tag, List<NodeTuple> value, Boolean flowStyle) {
      this(tag, value, DumperOptions.FlowStyle.fromBoolean(flowStyle));
   }

   public NodeId getNodeId() {
      return NodeId.mapping;
   }

   public List<NodeTuple> getValue() {
      return this.value;
   }

   public void setValue(List<NodeTuple> mergedValue) {
      this.value = mergedValue;
   }

   public void setOnlyKeyType(Class<? extends Object> keyType) {
      Iterator<NodeTuple> tupleIterator = this.value.iterator();

      while (tupleIterator.hasNext()) {
         NodeTuple nodes = tupleIterator.next();
         nodes.getKeyNode().setType(keyType);
      }

   }

   public void setTypes(Class<? extends Object> keyType, Class<? extends Object> valueType) {
      Iterator<NodeTuple> tupleIterator = this.value.iterator();

      while (tupleIterator.hasNext()) {
         NodeTuple nodes = tupleIterator.next();
         nodes.getValueNode().setType(valueType);
         nodes.getKeyNode().setType(keyType);
      }

   }

   public String toString() {
      StringBuilder buf = new StringBuilder();

      for (Iterator<NodeTuple> it = this.getValue().iterator(); it.hasNext(); buf.append(" }")) {
         NodeTuple node = it.next();
         buf.append("{ key=");
         buf.append(node.getKeyNode());
         buf.append("; value=");
         if (node.getValueNode() instanceof CollectionNode) {
            buf.append(System.identityHashCode(node.getValueNode()));
         } else {
            buf.append(node);
         }
      }

      String values = buf.toString();
      return "<" + this.getClass().getName() + " (tag=" + this.getTag() + ", values=" + values + ")>";
   }

   public void setMerged(boolean merged) {
      this.merged = merged;
   }

   public boolean isMerged() {
      return this.merged;
   }
}
