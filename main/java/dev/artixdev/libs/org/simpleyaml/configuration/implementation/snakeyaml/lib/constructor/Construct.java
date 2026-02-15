package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.constructor;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;

public interface Construct {
   Object construct(Node node);

   void construct2ndStep(Node node, Object object);
}
