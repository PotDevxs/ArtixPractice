package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.Iterator;
import java.util.Map.Entry;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBT;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTByteArray;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTType;

public class DefaultNBTSerializer extends NBTSerializer<DataInput, DataOutput> {
   public static final DefaultNBTSerializer INSTANCE = new DefaultNBTSerializer();

   public DefaultNBTSerializer() {
      super(DataInput::readByte, DataOutput::writeByte, DataInput::readUTF, DataOutput::writeUTF);
      this.registerType(NBTType.END, 0, (stream) -> {
         return NBTEnd.INSTANCE;
      }, (stream, tag) -> {
      });
      this.registerType(NBTType.BYTE, 1, (stream) -> {
         return new NBTByte(stream.readByte());
      }, (stream, tag) -> {
         stream.writeByte(tag.getAsByte());
      });
      this.registerType(NBTType.SHORT, 2, (stream) -> {
         return new NBTShort(stream.readShort());
      }, (stream, tag) -> {
         stream.writeShort(tag.getAsShort());
      });
      this.registerType(NBTType.INT, 3, (stream) -> {
         return new NBTInt(stream.readInt());
      }, (stream, tag) -> {
         stream.writeInt(tag.getAsInt());
      });
      this.registerType(NBTType.LONG, 4, (stream) -> {
         return new NBTLong(stream.readLong());
      }, (stream, tag) -> {
         stream.writeLong(tag.getAsLong());
      });
      this.registerType(NBTType.FLOAT, 5, (stream) -> {
         return new NBTFloat(stream.readFloat());
      }, (stream, tag) -> {
         stream.writeFloat(tag.getAsFloat());
      });
      this.registerType(NBTType.DOUBLE, 6, (stream) -> {
         return new NBTDouble(stream.readDouble());
      }, (stream, tag) -> {
         stream.writeDouble(tag.getAsDouble());
      });
      this.registerType(NBTType.STRING, 8, (stream) -> {
         return new NBTString(stream.readUTF());
      }, (stream, tag) -> {
         stream.writeUTF(tag.getValue());
      });
      this.registerType(NBTType.BYTE_ARRAY, 7, (stream) -> {
         byte[] array = new byte[stream.readInt()];
         stream.readFully(array);
         return new NBTByteArray(array);
      }, (stream, tag) -> {
         byte[] array = tag.getValue();
         stream.writeInt(array.length);
         stream.write(array);
      });
      this.registerType(NBTType.INT_ARRAY, 11, (stream) -> {
         int[] array = new int[stream.readInt()];

         for(int i = 0; i < array.length; ++i) {
            array[i] = stream.readInt();
         }

         return new NBTIntArray(array);
      }, (stream, tag) -> {
         int[] array = tag.getValue();
         stream.writeInt(array.length);
         int[] var3 = array;
         int var4 = array.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int i = var3[var5];
            stream.writeInt(i);
         }

      });
      this.registerType(NBTType.LONG_ARRAY, 12, (stream) -> {
         long[] array = new long[stream.readInt()];

         for(int i = 0; i < array.length; ++i) {
            array[i] = stream.readLong();
         }

         return new NBTLongArray(array);
      }, (stream, tag) -> {
         long[] array = tag.getValue();
         stream.writeInt(array.length);
         long[] var3 = array;
         int var4 = array.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            long i = var3[var5];
            stream.writeLong(i);
         }

      });
      this.registerType(NBTType.COMPOUND, 10, (stream) -> {
         NBTCompound compound = new NBTCompound();
         NBTType valueType = null;

         while((valueType = this.readTagType(stream)) != NBTType.END) {
            compound.setTag(this.readTagName(stream), this.readTag(stream, valueType));
         }

         return compound;
      }, (stream, tag) -> {
         Iterator var3 = tag.getTags().entrySet().iterator();

         while(var3.hasNext()) {
            Entry<String, NBT> entry = (Entry)var3.next();
            NBT value = (NBT)entry.getValue();
            this.writeTagType(stream, value.getType());
            this.writeTagName(stream, (String)entry.getKey());
            this.writeTag(stream, value);
         }

         this.writeTagType(stream, NBTType.END);
      });
      this.registerType(NBTType.LIST, 9, (stream) -> {
         NBTType<? extends NBT> valueType = this.readTagType(stream);
         int size = stream.readInt();
         if (valueType == NBTType.END && size > 0) {
            throw new IllegalStateException("Missing nbt list values tag type");
         } else {
            NBTList<NBT> list = new NBTList(valueType);

            for(int i = 0; i < size; ++i) {
               list.addTag(this.readTag(stream, valueType));
            }

            return list;
         }
      }, (stream, tag) -> {
         this.writeTagType(stream, tag.getTagsType());
         stream.writeInt(tag.size());
         Iterator var3 = tag.getTags().iterator();

         while(var3.hasNext()) {
            NBT value = (NBT)var3.next();
            this.writeTag(stream, value);
         }

      });
   }
}
