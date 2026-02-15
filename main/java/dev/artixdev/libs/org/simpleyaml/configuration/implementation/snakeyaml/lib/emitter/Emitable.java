package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.emitter;

import java.io.IOException;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.Event;

public interface Emitable {
   void emit(Event event) throws IOException;
}
