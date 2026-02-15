package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentLine;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.emitter.Emitable;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.AliasEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.CommentEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.DocumentEndEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.DocumentStartEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.ImplicitTuple;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.MappingEndEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.MappingStartEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.ScalarEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.SequenceEndEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.SequenceStartEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.StreamEndEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.StreamStartEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.AnchorNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.MappingNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.NodeId;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.NodeTuple;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.ScalarNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.SequenceNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Tag;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.resolver.Resolver;

public final class Serializer {
   private final Emitable emitter;
   private final Resolver resolver;
   private final boolean explicitStart;
   private final boolean explicitEnd;
   private DumperOptions.Version useVersion;
   private final Map<String, String> useTags;
   private final Set<Node> serializedNodes;
   private final Map<Node, String> anchors;
   private final AnchorGenerator anchorGenerator;
   private Boolean closed;
   private final Tag explicitRoot;

   public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag) {
      this.emitter = emitter;
      this.resolver = resolver;
      this.explicitStart = opts.isExplicitStart();
      this.explicitEnd = opts.isExplicitEnd();
      if (opts.getVersion() != null) {
         this.useVersion = opts.getVersion();
      }

      this.useTags = opts.getTags();
      this.serializedNodes = new HashSet();
      this.anchors = new HashMap();
      this.anchorGenerator = opts.getAnchorGenerator();
      this.closed = null;
      this.explicitRoot = rootTag;
   }

   public void open() throws IOException {
      if (this.closed == null) {
         this.emitter.emit(new StreamStartEvent((Mark)null, (Mark)null));
         this.closed = Boolean.FALSE;
      } else if (Boolean.TRUE.equals(this.closed)) {
         throw new SerializerException("serializer is closed");
      } else {
         throw new SerializerException("serializer is already opened");
      }
   }

   public void close() throws IOException {
      if (this.closed == null) {
         throw new SerializerException("serializer is not opened");
      } else {
         if (!Boolean.TRUE.equals(this.closed)) {
            this.emitter.emit(new StreamEndEvent((Mark)null, (Mark)null));
            this.closed = Boolean.TRUE;
            this.serializedNodes.clear();
            this.anchors.clear();
         }

      }
   }

   public void serialize(Node node) throws IOException {
      if (this.closed == null) {
         throw new SerializerException("serializer is not opened");
      } else if (this.closed) {
         throw new SerializerException("serializer is closed");
      } else {
         this.emitter.emit(new DocumentStartEvent((Mark)null, (Mark)null, this.explicitStart, this.useVersion, this.useTags));
         this.anchorNode(node);
         if (this.explicitRoot != null) {
            node.setTag(this.explicitRoot);
         }

         this.serializeNode(node, (Node)null);
         this.emitter.emit(new DocumentEndEvent((Mark)null, (Mark)null, this.explicitEnd));
         this.serializedNodes.clear();
         this.anchors.clear();
      }
   }

   private void anchorNode(Node node) {
      if (node.getNodeId() == NodeId.anchor) {
         node = ((AnchorNode)node).getRealNode();
      }

      if (this.anchors.containsKey(node)) {
         String anchor = (String)this.anchors.get(node);
         if (null == anchor) {
            anchor = this.anchorGenerator.nextAnchor(node);
            this.anchors.put(node, anchor);
         }
      } else {
         this.anchors.put(node, node.getAnchor() != null ? this.anchorGenerator.nextAnchor(node) : null);
         switch(node.getNodeId()) {
         case sequence:
            SequenceNode seqNode = (SequenceNode)node;
            List<Node> list = seqNode.getValue();
            Iterator<Node> listIterator = list.iterator();

            while (listIterator.hasNext()) {
               Node item = listIterator.next();
               this.anchorNode(item);
            }

            return;
         case mapping:
            MappingNode mnode = (MappingNode)node;
            List<NodeTuple> map = mnode.getValue();
            Iterator<NodeTuple> tupleIterator = map.iterator();

            while (tupleIterator.hasNext()) {
               NodeTuple object = tupleIterator.next();
               Node key = object.getKeyNode();
               Node value = object.getValueNode();
               this.anchorNode(key);
               this.anchorNode(value);
            }
         }
      }

   }

   private void serializeNode(Node node, Node parent) throws IOException {
      if (node.getNodeId() == NodeId.anchor) {
         node = ((AnchorNode)node).getRealNode();
      }

      String tAlias = (String)this.anchors.get(node);
      if (this.serializedNodes.contains(node)) {
         this.emitter.emit(new AliasEvent(tAlias, (Mark)null, (Mark)null));
      } else {
         this.serializedNodes.add(node);
         switch(node.getNodeId()) {
         case sequence:
            SequenceNode seqNode = (SequenceNode)node;
            this.serializeComments(node.getBlockComments());
            boolean implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence, (String)null, true));
            this.emitter.emit(new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, (Mark)null, (Mark)null, seqNode.getFlowStyle()));
            List<Node> list = seqNode.getValue();
            Iterator<Node> seqIterator = list.iterator();

            while (seqIterator.hasNext()) {
               Node item = seqIterator.next();
               this.serializeNode(item, node);
            }

            this.emitter.emit(new SequenceEndEvent((Mark)null, (Mark)null));
            this.serializeComments(node.getInLineComments());
            this.serializeComments(node.getEndComments());
            break;
         case scalar:
            ScalarNode scalarNode = (ScalarNode)node;
            this.serializeComments(node.getBlockComments());
            Tag detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
            Tag defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
            ImplicitTuple tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
            ScalarEvent event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), (Mark)null, (Mark)null, scalarNode.getScalarStyle());
            this.emitter.emit(event);
            this.serializeComments(node.getInLineComments());
            this.serializeComments(node.getEndComments());
            break;
         default:
            this.serializeComments(node.getBlockComments());
            Tag implicitTag = this.resolver.resolve(NodeId.mapping, (String)null, true);
            boolean implicitM = node.getTag().equals(implicitTag);
            MappingNode mnode = (MappingNode)node;
            List<NodeTuple> map = mnode.getValue();
            if (mnode.getTag() != Tag.COMMENT) {
               this.emitter.emit(new MappingStartEvent(tAlias, mnode.getTag().getValue(), implicitM, (Mark)null, (Mark)null, mnode.getFlowStyle()));
               Iterator<NodeTuple> mapIterator = map.iterator();

               while (mapIterator.hasNext()) {
                  NodeTuple row = mapIterator.next();
                  Node key = row.getKeyNode();
                  Node value = row.getValueNode();
                  this.serializeNode(key, mnode);
                  this.serializeNode(value, mnode);
               }

               this.emitter.emit(new MappingEndEvent((Mark)null, (Mark)null));
               this.serializeComments(node.getInLineComments());
               this.serializeComments(node.getEndComments());
            }
         }
      }

   }

   private void serializeComments(List<CommentLine> comments) throws IOException {
      if (comments != null) {
         Iterator<CommentLine> commentIterator = comments.iterator();

         while (commentIterator.hasNext()) {
            CommentLine line = commentIterator.next();
            CommentEvent commentEvent = new CommentEvent(line.getCommentType(), line.getValue(), line.getStartMark(), line.getEndMark());
            this.emitter.emit(commentEvent);
         }

      }
   }
}
