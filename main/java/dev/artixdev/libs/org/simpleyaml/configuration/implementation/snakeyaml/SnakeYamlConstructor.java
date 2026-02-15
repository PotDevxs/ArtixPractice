package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.constructor.SafeConstructor;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.YAMLException;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.MappingNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.NodeTuple;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.ScalarNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Tag;
import dev.artixdev.libs.org.simpleyaml.configuration.serialization.ConfigurationSerialization;

public class SnakeYamlConstructor extends SafeConstructor {
   public SnakeYamlConstructor() {
      this.yamlConstructors.put(Tag.MAP, new SnakeYamlConstructor.ConstructCustomObject());
   }

   public void flattenMapping(MappingNode node) {
      super.flattenMapping(node);
   }

   public Object construct(Node node) {
      return super.constructObject(node);
   }

   protected boolean hasSerializedTypeKey(MappingNode node) {
      Iterator<NodeTuple> tupleIterator = node.getValue().iterator();

      while (tupleIterator.hasNext()) {
         NodeTuple nodeTuple = tupleIterator.next();
         Node keyNode = nodeTuple.getKeyNode();
         if (keyNode instanceof ScalarNode) {
            String key = ((ScalarNode)keyNode).getValue();
            if (key.equals("==")) {
               return true;
            }
         }
      }

      return false;
   }

   private final class ConstructCustomObject extends SafeConstructor.ConstructYamlMap {
      private ConstructCustomObject() {
         super();
      }

      public Object construct(Node node) {
         if (node.isTwoStepsConstruction()) {
            throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
         } else {
            Map<?, ?> raw = (Map)super.construct(node);
            if (!raw.containsKey("==")) {
               return raw;
            } else {
               Map<String, Object> typed = new LinkedHashMap(raw.size());
               for (Entry<?, ?> entry : raw.entrySet()) {
                  typed.put(entry.getKey().toString(), entry.getValue());
               }

               try {
                  return ConfigurationSerialization.deserializeObject(typed);
               } catch (IllegalArgumentException e) {
                  throw new YAMLException("Could not deserialize object", e);
               }
            }
         }
      }

      public void construct2ndStep(Node node, Object object) {
         throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
      }

      // $FF: synthetic method
      ConstructCustomObject(Object x1) {
         this();
      }
   }
}
