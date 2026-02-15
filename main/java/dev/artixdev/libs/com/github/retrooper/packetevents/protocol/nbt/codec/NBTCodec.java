package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.codec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBT;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer;
import dev.artixdev.libs.com.google.gson.JsonArray;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonNull;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonPrimitive;
import dev.artixdev.libs.com.google.gson.internal.LazilyParsedNumber;

public class NBTCodec {
   public static NBT jsonToNBT(JsonElement element) {
      if (element instanceof JsonPrimitive) {
         if (((JsonPrimitive)element).isBoolean()) {
            return new NBTByte(element.getAsBoolean());
         }

         if (((JsonPrimitive)element).isString()) {
            return new NBTString(element.getAsString());
         }

         if (((JsonPrimitive)element).isNumber()) {
            Number num = element.getAsNumber();
            if (num instanceof Float) {
               return new NBTFloat(num.floatValue());
            }

            if (num instanceof Double) {
               return new NBTDouble(num.doubleValue());
            }

            if (num instanceof Byte) {
               return new NBTByte(num.byteValue());
            }

            if (num instanceof Short) {
               return new NBTShort(num.shortValue());
            }

            if (num instanceof Integer || num instanceof LazilyParsedNumber) {
               return new NBTInt(num.intValue());
            }

            if (num instanceof Long) {
               return new NBTLong(num.longValue());
            }
         }
      } else {
         Iterator var3;
         if (element instanceof JsonArray) {
            List<NBT> list = new ArrayList();
            Iterator var7 = ((JsonArray)element).iterator();

            while(var7.hasNext()) {
               JsonElement var = (JsonElement)var7.next();
               list.add(jsonToNBT(var));
            }

            if (list.isEmpty()) {
               return new NBTList(NBTType.COMPOUND);
            }

            NBTList<? extends NBT> l = new NBTList(((NBT)list.get(0)).getType());
            var3 = list.iterator();

            while(var3.hasNext()) {
               NBT nbt = (NBT)var3.next();
               l.addTagUnsafe(nbt);
            }

            return l;
         }

         if (element instanceof JsonObject) {
            JsonObject obj = (JsonObject)element;
            NBTCompound compound = new NBTCompound();
            var3 = obj.entrySet().iterator();

            while(var3.hasNext()) {
               Entry<String, JsonElement> jsonEntry = (Entry)var3.next();
               compound.setTag((String)jsonEntry.getKey(), jsonToNBT((JsonElement)jsonEntry.getValue()));
            }

            return compound;
         }

         if (element instanceof JsonNull || element == null) {
            return new NBTCompound();
         }
      }

      throw new IllegalStateException("Failed to convert JSON to NBT " + element.toString());
   }

   public static JsonElement nbtToJson(NBT nbt, boolean parseByteAsBool) {
      if (nbt instanceof NBTNumber) {
         if (nbt instanceof NBTByte && parseByteAsBool) {
            byte val = ((NBTByte)nbt).getAsByte();
            if (val == 0) {
               return new JsonPrimitive(false);
            }

            if (val == 1) {
               return new JsonPrimitive(true);
            }
         }

         return new JsonPrimitive(((NBTNumber)nbt).getAsNumber());
      } else if (nbt instanceof NBTString) {
         return new JsonPrimitive(((NBTString)nbt).getValue());
      } else if (nbt instanceof NBTList) {
         NBTList<? extends NBT> list = (NBTList)nbt;
         JsonArray jsonArray = new JsonArray();
         list.getTags().forEach((tagx) -> {
            jsonArray.add(nbtToJson(tagx, parseByteAsBool));
         });
         return jsonArray;
      } else if (nbt instanceof NBTEnd) {
         throw new IllegalStateException("Encountered the NBTEnd tag during the NBT to JSON conversion: " + nbt.toString());
      } else if (!(nbt instanceof NBTCompound)) {
         throw new IllegalStateException("Failed to convert NBT to JSON.");
      } else {
         JsonObject jsonObject = new JsonObject();
         Map<String, NBT> compoundTags = ((NBTCompound)nbt).getTags();
         Iterator var4 = compoundTags.keySet().iterator();

         while(var4.hasNext()) {
            String tagName = (String)var4.next();
            NBT tag = (NBT)compoundTags.get(tagName);
            JsonElement jsonValue = nbtToJson(tag, parseByteAsBool);
            jsonObject.add(tagName, jsonValue);
         }

         return jsonObject;
      }
   }

   public static NBTCompound readNBTFromBuffer(Object byteBuf, ServerVersion serverVersion) {
      return (NBTCompound)readRawNBTFromBuffer(byteBuf, serverVersion);
   }

   public static NBT readRawNBTFromBuffer(Object byteBuf, ServerVersion serverVersion) {
      if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         try {
            boolean named = serverVersion.isOlderThan(ServerVersion.V_1_20_2);
            return DefaultNBTSerializer.INSTANCE.deserializeTag(new ByteBufInputStream(byteBuf), named);
         } catch (IOException e) {
            e.printStackTrace();
            return null;
         }
      } else {
         try {
            short length = ByteBufHelper.readShort(byteBuf);
            if (length < 0) {
               return null;
            } else {
               Object slicedBuffer = ByteBufHelper.readSlice(byteBuf, length);
               DataInputStream stream = new DataInputStream(new GZIPInputStream(new ByteBufInputStream(slicedBuffer)));

               NBT var5;
               try {
                  var5 = DefaultNBTSerializer.INSTANCE.deserializeTag(stream);
               } catch (Throwable e) {
                  try {
                     stream.close();
                  } catch (Throwable suppressed) {
                     e.addSuppressed(suppressed);
                  }

                  throw e;
               }

               stream.close();
               return var5;
            }
         } catch (IOException e) {
            throw new IllegalStateException(e);
         }
      }
   }

   public static void writeNBTToBuffer(Object byteBuf, ServerVersion serverVersion, NBTCompound tag) {
      writeNBTToBuffer(byteBuf, serverVersion, (NBT)tag);
   }

   public static void writeNBTToBuffer(Object byteBuf, ServerVersion serverVersion, NBT tag) {
      if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         try {
            ByteBufOutputStream outputStream = new ByteBufOutputStream(byteBuf);

            try {
               if (tag != null) {
                  boolean named = serverVersion.isOlderThan(ServerVersion.V_1_20_2);
                  DefaultNBTSerializer.INSTANCE.serializeTag(outputStream, tag, named);
               } else {
                  DefaultNBTSerializer.INSTANCE.serializeTag(outputStream, NBTEnd.INSTANCE);
               }
            } catch (Throwable e) {
               try {
                  outputStream.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }

               throw e;
            }

            outputStream.close();
         } catch (IOException e) {
            throw new IllegalStateException(e);
         }
      } else if (tag == null) {
         ByteBufHelper.writeShort(byteBuf, -1);
      } else {
         int lengthWriterIndex = ByteBufHelper.writerIndex(byteBuf);
         ByteBufHelper.writeShort(byteBuf, 0);
         int writerIndexDataStart = ByteBufHelper.writerIndex(byteBuf);

         try {
            DataOutputStream outputstream = new DataOutputStream(new GZIPOutputStream(new ByteBufOutputStream(byteBuf)));

            try {
               DefaultNBTSerializer.INSTANCE.serializeTag(outputstream, tag);
            } catch (Throwable e) {
               try {
                  outputstream.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }

               throw e;
            }

            outputstream.close();
         } catch (Exception e) {
            throw new IllegalStateException(e);
         }

         int writerIndexDataEnd = ByteBufHelper.writerIndex(byteBuf);
         ByteBufHelper.writerIndex(byteBuf, lengthWriterIndex);
         ByteBufHelper.writeShort(byteBuf, writerIndexDataEnd - writerIndexDataStart);
         ByteBufHelper.writerIndex(byteBuf, writerIndexDataEnd);
      }

   }
}
