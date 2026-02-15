package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes;

import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public class SequenceNode extends CollectionNode<Node> {
   private final List<Node> value;

   public SequenceNode(Tag tag, boolean resolved, List<Node> value, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
      super(tag, startMark, endMark, flowStyle);
      if (value == null) {
         throw new NullPointerException("value in a Node is required.");
      } else {
         this.value = value;
         this.resolved = resolved;
      }
   }

   public SequenceNode(Tag tag, List<Node> value, DumperOptions.FlowStyle flowStyle) {
      this(tag, true, value, (Mark)null, (Mark)null, (DumperOptions.FlowStyle)flowStyle);
   }

   /** @deprecated */
   @Deprecated
   public SequenceNode(Tag tag, List<Node> value, Boolean style) {
      this(tag, value, DumperOptions.FlowStyle.fromBoolean(style));
   }

   /** @deprecated */
   @Deprecated
   public SequenceNode(Tag tag, boolean resolved, List<Node> value, Mark startMark, Mark endMark, Boolean style) {
      this(tag, resolved, value, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(style));
   }

   public NodeId getNodeId() {
      return NodeId.sequence;
   }

   public List<Node> getValue() {
      return this.value;
   }

   public void setListType(Class<? extends Object> listType) {
      Iterator<Node> nodeIterator = this.value.iterator();

      while (nodeIterator.hasNext()) {
         Node node = nodeIterator.next();
         node.setType(listType);
      }

   }

   public String toString() {
      return "<" + this.getClass().getName() + " (tag=" + this.getTag() + ", value=" + this.getValue() + ")>";
   }
}
