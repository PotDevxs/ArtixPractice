package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml;

import java.util.LinkedHashMap;
import java.util.Map;
import dev.artixdev.libs.org.simpleyaml.configuration.ConfigurationSection;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.QuoteValue;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Tag;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.representer.Represent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.representer.Representer;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.representer.SafeRepresenter;
import dev.artixdev.libs.org.simpleyaml.configuration.serialization.ConfigurationSerializable;
import dev.artixdev.libs.org.simpleyaml.configuration.serialization.ConfigurationSerialization;

public class SnakeYamlRepresenter extends Representer {
   public SnakeYamlRepresenter() {
      this.multiRepresenters.put(ConfigurationSection.class, new SnakeYamlRepresenter.RepresentConfigurationSection());
      this.multiRepresenters.put(ConfigurationSerializable.class, new SnakeYamlRepresenter.RepresentConfigurationSerializable());
      this.multiRepresenters.put(QuoteValue.class, new SnakeYamlRepresenter.RepresentQuoteValue());
   }

   private final class RepresentQuoteValue implements Represent {
      private RepresentQuoteValue() {
      }

      public Node representData(Object data) {
         QuoteValue<?> quoteValue = (QuoteValue)data;
         DumperOptions.ScalarStyle quoteScalarStyle = SnakeYamlQuoteValue.getQuoteScalarStyle(quoteValue.getQuoteStyle());
         Object value = quoteValue.getValue();
         if (value == null) {
            return SnakeYamlRepresenter.this.representScalar(Tag.NULL, "", quoteScalarStyle);
         } else {
            DumperOptions.ScalarStyle defaultScalarStyle = SnakeYamlRepresenter.this.getDefaultScalarStyle();
            SnakeYamlRepresenter.this.setDefaultScalarStyle(quoteScalarStyle);
            Node node = SnakeYamlRepresenter.this.representData(value);
            SnakeYamlRepresenter.this.setDefaultScalarStyle(defaultScalarStyle);
            return node;
         }
      }

      // $FF: synthetic method
      RepresentQuoteValue(Object x1) {
         this();
      }
   }

   private final class RepresentConfigurationSerializable extends SafeRepresenter.RepresentMap {
      private RepresentConfigurationSerializable() {
         super();
      }

      public Node representData(Object data) {
         ConfigurationSerializable serializable = (ConfigurationSerializable)data;
         Map<String, Object> values = new LinkedHashMap();
         values.put("==", ConfigurationSerialization.getAlias(serializable.getClass()));
         values.putAll(serializable.serialize());
         return super.representData(values);
      }

      // $FF: synthetic method
      RepresentConfigurationSerializable(Object x1) {
         this();
      }
   }

   private final class RepresentConfigurationSection extends SafeRepresenter.RepresentMap {
      private RepresentConfigurationSection() {
         super();
      }

      public Node representData(Object data) {
         return super.representData(((ConfigurationSection)data).getValues(false));
      }

      // $FF: synthetic method
      RepresentConfigurationSection(Object x1) {
         this();
      }
   }
}
