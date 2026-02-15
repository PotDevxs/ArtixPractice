package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.serializer;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;

public interface AnchorGenerator {
   String nextAnchor(Node node);
}
