package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.types.ObjectId;

class ShellObjectIdConverter implements Converter<ObjectId> {
   public void convert(ObjectId value, StrictJsonWriter writer) {
      writer.writeRaw(String.format("ObjectId(\"%s\")", value.toHexString()));
   }
}
