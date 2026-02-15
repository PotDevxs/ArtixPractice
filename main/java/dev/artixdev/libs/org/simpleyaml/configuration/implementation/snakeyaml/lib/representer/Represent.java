package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.representer;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;

public interface Represent {
   Node representData(Object data);
}
