package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes;

public class AnchorNode extends Node {
   private final Node realNode;

   public AnchorNode(Node realNode) {
      super(realNode.getTag(), realNode.getStartMark(), realNode.getEndMark());
      this.realNode = realNode;
   }

   public NodeId getNodeId() {
      return NodeId.anchor;
   }

   public Node getRealNode() {
      return this.realNode;
   }
}
