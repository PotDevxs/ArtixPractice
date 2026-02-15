package dev.artixdev.libs.org.simpleyaml.configuration.serialization;

import java.util.Map;

public interface ConfigurationSerializable {
   Map<String, Object> serialize();
}
