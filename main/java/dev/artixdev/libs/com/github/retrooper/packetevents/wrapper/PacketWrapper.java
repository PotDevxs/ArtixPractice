package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.VersionComparison;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.PacketSide;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.ChatType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.MessageSignature;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.Node;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.Parsers;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.RemoteChatSession;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.SignedCommandArgument;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.filter.FilterMask;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.filter.FilterMaskType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19_1;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.data.EntityMetadataProvider;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.ItemStack;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBT;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.GameMode;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.PublicProfileKey;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.recipe.data.MerchantOffer;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.Dimension;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.StringUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3i;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureNBTSerialization;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.crypto.MinecraftEncryptionUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.crypto.SaltSignature;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.crypto.SignatureData;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class PacketWrapper<T extends PacketWrapper> {
   @Nullable
   public Object buffer;
   protected ClientVersion clientVersion;
   protected ServerVersion serverVersion;
   private PacketTypeData packetTypeData;
   @Nullable
   protected User user;
   private static final int MODERN_MESSAGE_LENGTH = 262144;
   private static final int LEGACY_MESSAGE_LENGTH = 32767;

   public PacketWrapper(ClientVersion clientVersion, ServerVersion serverVersion, int packetID) {
      if (packetID == -1) {
         throw new IllegalArgumentException("Packet does not exist on this protocol version!");
      } else {
         this.clientVersion = clientVersion;
         this.serverVersion = serverVersion;
         this.buffer = null;
         this.packetTypeData = new PacketTypeData((PacketTypeCommon)null, packetID);
      }
   }

   public PacketWrapper(PacketReceiveEvent event) {
      this(event, true);
   }

   public PacketWrapper(PacketReceiveEvent event, boolean readData) {
      this.clientVersion = event.getUser().getClientVersion();
      this.serverVersion = event.getServerVersion();
      this.user = event.getUser();
      this.buffer = event.getByteBuf();
      this.packetTypeData = new PacketTypeData(event.getPacketType(), event.getPacketId());
      if (readData) {
         this.readEvent(event);
      }

   }

   public PacketWrapper(PacketSendEvent event) {
      this(event, true);
   }

   public PacketWrapper(PacketSendEvent event, boolean readData) {
      this.clientVersion = event.getUser().getClientVersion();
      this.serverVersion = event.getServerVersion();
      this.buffer = event.getByteBuf();
      this.packetTypeData = new PacketTypeData(event.getPacketType(), event.getPacketId());
      this.user = event.getUser();
      if (readData) {
         this.readEvent(event);
      }

   }

   public PacketWrapper(int packetID, ClientVersion clientVersion) {
      this(clientVersion, PacketEvents.getAPI().getServerManager().getVersion(), packetID);
   }

   public PacketWrapper(int packetID) {
      if (packetID == -1) {
         throw new IllegalArgumentException("Packet does not exist on this protocol version!");
      } else {
         this.clientVersion = ClientVersion.UNKNOWN;
         this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
         this.buffer = null;
         this.packetTypeData = new PacketTypeData((PacketTypeCommon)null, packetID);
      }
   }

   public PacketWrapper(PacketTypeCommon packetType) {
      this.clientVersion = ClientVersion.UNKNOWN;
      this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
      this.buffer = null;
      int id = packetType.getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
      this.packetTypeData = new PacketTypeData(packetType, id);
   }

   public static PacketWrapper<?> createUniversalPacketWrapper(Object byteBuf) {
      PacketWrapper<?> wrapper = new PacketWrapper(ClientVersion.UNKNOWN, PacketEvents.getAPI().getServerManager().getVersion(), -2);
      wrapper.buffer = byteBuf;
      return wrapper;
   }

   public static int getChunkX(long chunkKey) {
      return (int)(chunkKey & 4294967295L);
   }

   public static int getChunkZ(long chunkKey) {
      return (int)(chunkKey >>> 32 & 4294967295L);
   }

   public static long getChunkKey(int chunkX, int chunkZ) {
      return (long)chunkX & 4294967295L | ((long)chunkZ & 4294967295L) << 32;
   }

   @ApiStatus.Internal
   public final void prepareForSend(Object channel, boolean outgoing) {
      if (this.buffer == null || ByteBufHelper.refCnt(this.buffer) == 0) {
         this.buffer = ChannelHelper.pooledByteBuf(channel);
      }

      if (PacketEvents.getAPI().getInjector().isProxy()) {
         User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
         if (this.packetTypeData.getPacketType() == null) {
            this.packetTypeData.setPacketType(PacketType.getById(outgoing ? PacketSide.SERVER : PacketSide.CLIENT, user.getConnectionState(), this.serverVersion.toClientVersion(), this.packetTypeData.getNativePacketId()));
         }

         this.serverVersion = user.getClientVersion().toServerVersion();
         int id = this.packetTypeData.getPacketType().getId(user.getClientVersion());
         this.writeVarInt(id);
      } else {
         this.writeVarInt(this.packetTypeData.getNativePacketId());
      }

      this.write();
   }

   public void read() {
   }

   public void write() {
   }

   public void copy(T wrapper) {
   }

   public final void readEvent(ProtocolPacketEvent<?> event) {
      PacketWrapper<?> last = event.getLastUsedWrapper();
      if (last != null) {
         @SuppressWarnings("unchecked")
         T lastWrapper = (T) last;
         this.copy(lastWrapper);
      } else {
         this.read();
      }

      event.setLastUsedWrapper(this);
   }

   public ClientVersion getClientVersion() {
      return this.clientVersion;
   }

   public void setClientVersion(ClientVersion clientVersion) {
      this.clientVersion = clientVersion;
   }

   public ServerVersion getServerVersion() {
      return this.serverVersion;
   }

   public void setServerVersion(ServerVersion serverVersion) {
      this.serverVersion = serverVersion;
   }

   public Object getBuffer() {
      return this.buffer;
   }

   /** @deprecated */
   @Deprecated
   public int getPacketId() {
      return this.getNativePacketId();
   }

   /** @deprecated */
   @Deprecated
   public void setPacketId(int packetID) {
      this.setNativePacketId(packetID);
   }

   public int getNativePacketId() {
      return this.packetTypeData.getNativePacketId();
   }

   public void setNativePacketId(int nativePacketId) {
      this.packetTypeData.setNativePacketId(nativePacketId);
   }

   @ApiStatus.Internal
   public PacketTypeData getPacketTypeData() {
      return this.packetTypeData;
   }

   public int getMaxMessageLength() {
      return this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? 262144 : 32767;
   }

   /** @deprecated */
   @Deprecated
   public void resetByteBuf() {
      ByteBufHelper.clear(this.buffer);
   }

   public void resetBuffer() {
      ByteBufHelper.clear(this.buffer);
   }

   public byte readByte() {
      return ByteBufHelper.readByte(this.buffer);
   }

   public void writeByte(int value) {
      ByteBufHelper.writeByte(this.buffer, value);
   }

   public short readUnsignedByte() {
      return ByteBufHelper.readUnsignedByte(this.buffer);
   }

   public boolean readBoolean() {
      return this.readByte() != 0;
   }

   public void writeBoolean(boolean value) {
      this.writeByte(value ? 1 : 0);
   }

   public int readInt() {
      return ByteBufHelper.readInt(this.buffer);
   }

   public void writeInt(int value) {
      ByteBufHelper.writeInt(this.buffer, value);
   }

   public int readVarInt() {
      int value = 0;
      int length = 0;

      byte currentByte;
      do {
         currentByte = this.readByte();
         value |= (currentByte & 127) << length * 7;
         ++length;
         if (length > 5) {
            throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
         }
      } while((currentByte & 128) == 128);

      return value;
   }

   public void writeVarInt(int value) {
      while((value & -128) != 0) {
         this.writeByte(value & 127 | 128);
         value >>>= 7;
      }

      this.writeByte(value);
   }

   public <K, V> Map<K, V> readMap(PacketWrapper.Reader<K> keyFunction, PacketWrapper.Reader<V> valueFunction) {
      int size = this.readVarInt();
      Map<K, V> map = new HashMap(size);

      for(int i = 0; i < size; ++i) {
         K key = keyFunction.apply(this);
         V value = valueFunction.apply(this);
         map.put(key, value);
      }

      return map;
   }

   public <K, V> void writeMap(Map<K, V> map, PacketWrapper.Writer<K> keyConsumer, PacketWrapper.Writer<V> valueConsumer) {
      this.writeVarInt(map.size());
      for (Entry<K, V> entry : map.entrySet()) {
         K key = entry.getKey();
         V value = entry.getValue();
         keyConsumer.accept(this, key);
         valueConsumer.accept(this, value);
      }
   }

   public VillagerData readVillagerData() {
      int villagerTypeId = this.readVarInt();
      int villagerProfessionId = this.readVarInt();
      int level = this.readVarInt();
      return new VillagerData(villagerTypeId, villagerProfessionId, level);
   }

   public void writeVillagerData(VillagerData data) {
      this.writeVarInt(data.getType().getId());
      this.writeVarInt(data.getProfession().getId());
      this.writeVarInt(data.getLevel());
   }

   @NotNull
   public ItemStack readItemStack() {
      boolean v1_13_2 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_2);
      if (v1_13_2 && !this.readBoolean()) {
         return ItemStack.EMPTY;
      } else {
         int typeID = v1_13_2 ? this.readVarInt() : this.readShort();
         if (typeID < 0 && !v1_13_2) {
            return ItemStack.EMPTY;
         } else {
            ItemType type = ItemTypes.getById(this.serverVersion.toClientVersion(), typeID);
            int amount = this.readByte();
            int legacyData = v1_13_2 ? -1 : this.readShort();
            NBTCompound nbt = this.readNBT();
            return ItemStack.builder().type(type).amount(amount).nbt(nbt).legacyData(legacyData).build();
         }
      }
   }

   public void writeItemStack(ItemStack itemStack) {
      if (itemStack == null) {
         itemStack = ItemStack.EMPTY;
      }

      boolean v1_13_2 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_2);
      int typeID;
      if (v1_13_2) {
         if (itemStack.isEmpty()) {
            this.writeBoolean(false);
         } else {
            this.writeBoolean(true);
            typeID = itemStack.getType().getId(this.serverVersion.toClientVersion());
            this.writeVarInt(typeID);
            this.writeByte(itemStack.getAmount());
            this.writeNBT(itemStack.getNBT());
         }
      } else if (itemStack.isEmpty()) {
         this.writeShort(-1);
      } else {
         typeID = itemStack.getType().getId(this.serverVersion.toClientVersion());
         this.writeShort(typeID);
         this.writeByte(itemStack.getAmount());
         this.writeShort(itemStack.getLegacyData());
         this.writeNBT(itemStack.getNBT());
      }

   }

   public NBTCompound readNBT() {
      return (NBTCompound)this.readDirectNBT();
   }

   public NBT readDirectNBT() {
      return NBTCodec.readNBTFromBuffer(this.buffer, this.serverVersion);
   }

   public void writeNBT(NBTCompound nbt) {
      this.writeDirectNBT(nbt);
   }

   public void writeDirectNBT(NBT nbt) {
      NBTCodec.writeNBTToBuffer(this.buffer, this.serverVersion, nbt);
   }

   public String readString() {
      return this.readString(32767);
   }

   public String readString(int maxLen) {
      int j = this.readVarInt();
      if (j > maxLen * 4) {
         throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + maxLen * 4 + ")");
      } else if (j < 0) {
         throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
      } else {
         String s = ByteBufHelper.toString(this.buffer, ByteBufHelper.readerIndex(this.buffer), j, StandardCharsets.UTF_8);
         ByteBufHelper.readerIndex(this.buffer, ByteBufHelper.readerIndex(this.buffer) + j);
         if (s.length() > maxLen) {
            throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + maxLen + ")");
         } else {
            return s;
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public String readComponentJSON() {
      return AdventureSerializer.asVanilla(this.readComponent());
   }

   public void writeString(String s) {
      this.writeString(s, 32767);
   }

   public void writeString(String s, int maxLen) {
      this.writeString(s, maxLen, true);
   }

   public void writeString(String s, int maxLen, boolean substr) {
      if (substr) {
         s = StringUtil.maximizeLength(s, maxLen);
      }

      byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
      if (!substr && bytes.length > maxLen) {
         throw new IllegalStateException("String too big (was " + bytes.length + " bytes encoded, max " + maxLen + ")");
      } else {
         this.writeVarInt(bytes.length);
         ByteBufHelper.writeBytes(this.buffer, bytes);
      }
   }

   /** @deprecated */
   @Deprecated
   public void writeComponentJSON(String json) {
      this.writeComponent(AdventureSerializer.parseComponent(json));
   }

   public Component readComponent() {
      return this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3) ? this.readComponentAsNBT() : this.readComponentAsJSON();
   }

   public Component readComponentAsNBT() {
      try {
         return AdventureNBTSerialization.readComponent(this.buffer);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }
   }

   public Component readComponentAsJSON() {
      String jsonString = this.readString(this.getMaxMessageLength());
      return AdventureSerializer.parseComponent(jsonString);
   }

   public void writeComponent(Component component) {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.writeComponentAsNBT(component);
      } else {
         this.writeComponentAsJSON(component);
      }

   }

   public void writeComponentAsNBT(Component component) {
      try {
         AdventureNBTSerialization.writeComponent(new ByteBufOutputStream(this.buffer), component);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }
   }

   public void writeComponentAsJSON(Component component) {
      String jsonString = AdventureSerializer.toJson(component);
      this.writeString(jsonString, this.getMaxMessageLength());
   }

   public ResourceLocation readIdentifier(int maxLen) {
      return new ResourceLocation(this.readString(maxLen));
   }

   public ResourceLocation readIdentifier() {
      return this.readIdentifier(32767);
   }

   public void writeIdentifier(ResourceLocation identifier, int maxLen) {
      this.writeString(identifier.toString(), maxLen);
   }

   public void writeIdentifier(ResourceLocation identifier) {
      this.writeIdentifier(identifier, 32767);
   }

   public int readUnsignedShort() {
      return ByteBufHelper.readUnsignedShort(this.buffer);
   }

   public short readShort() {
      return ByteBufHelper.readShort(this.buffer);
   }

   public void writeShort(int value) {
      ByteBufHelper.writeShort(this.buffer, value);
   }

   public int readVarShort() {
      int low = this.readUnsignedShort();
      int high = 0;
      if ((low & '耀') != 0) {
         low &= 32767;
         high = this.readUnsignedByte();
      }

      return (high & 255) << 15 | low;
   }

   public void writeVarShort(int value) {
      int low = value & 32767;
      int high = (value & 8355840) >> 15;
      if (high != 0) {
         low |= 32768;
      }

      this.writeShort(low);
      if (high != 0) {
         this.writeByte(high);
      }

   }

   public long readLong() {
      return ByteBufHelper.readLong(this.buffer);
   }

   public void writeLong(long value) {
      ByteBufHelper.writeLong(this.buffer, value);
   }

   public long readVarLong() {
      long value = 0L;

      int size;
      byte b;
      for(size = 0; ((b = this.readByte()) & 128) == 128; value |= (long)(b & 127) << size++ * 7) {
      }

      return value | (long)(b & 127) << size * 7;
   }

   public void writeVarLong(long l) {
      while((l & -128L) != 0L) {
         this.writeByte((int)(l & 127L) | 128);
         l >>>= 7;
      }

      this.writeByte((int)l);
   }

   public float readFloat() {
      return ByteBufHelper.readFloat(this.buffer);
   }

   public void writeFloat(float value) {
      ByteBufHelper.writeFloat(this.buffer, value);
   }

   public double readDouble() {
      return ByteBufHelper.readDouble(this.buffer);
   }

   public void writeDouble(double value) {
      ByteBufHelper.writeDouble(this.buffer, value);
   }

   public byte[] readRemainingBytes() {
      return this.readBytes(ByteBufHelper.readableBytes(this.buffer));
   }

   public byte[] readBytes(int size) {
      byte[] bytes = new byte[size];
      ByteBufHelper.readBytes(this.buffer, bytes);
      return bytes;
   }

   public void writeBytes(byte[] array) {
      ByteBufHelper.writeBytes(this.buffer, array);
   }

   public byte[] readByteArray(int maxLength) {
      int len = this.readVarInt();
      if (len > maxLength) {
         throw new RuntimeException("The received byte array length is longer than maximum allowed (" + len + " > " + maxLength + ")");
      } else {
         return this.readBytes(len);
      }
   }

   public byte[] readByteArray() {
      return this.readByteArray(ByteBufHelper.readableBytes(this.buffer));
   }

   public void writeByteArray(byte[] array) {
      this.writeVarInt(array.length);
      this.writeBytes(array);
   }

   public int[] readVarIntArray() {
      int readableBytes = ByteBufHelper.readableBytes(this.buffer);
      int size = this.readVarInt();
      if (size > readableBytes) {
         throw new IllegalStateException("VarIntArray with size " + size + " is bigger than allowed " + readableBytes);
      } else {
         int[] array = new int[size];

         for(int i = 0; i < size; ++i) {
            array[i] = this.readVarInt();
         }

         return array;
      }
   }

   public void writeVarIntArray(int[] array) {
      this.writeVarInt(array.length);
      for (int i : array) {
         this.writeVarInt(i);
      }
   }

   public long[] readLongArray(int size) {
      long[] array = new long[size];

      for(int i = 0; i < array.length; ++i) {
         array[i] = this.readLong();
      }

      return array;
   }

   public byte[] readByteArrayOfSize(int size) {
      byte[] array = new byte[size];
      ByteBufHelper.readBytes(this.buffer, array);
      return array;
   }

   public void writeByteArrayOfSize(byte[] array) {
      ByteBufHelper.writeBytes(this.buffer, array);
   }

   public int[] readVarIntArrayOfSize(int size) {
      int[] array = new int[size];

      for(int i = 0; i < array.length; ++i) {
         array[i] = this.readVarInt();
      }

      return array;
   }

   public void writeVarIntArrayOfSize(int[] array) {
      for (int i : array) {
         this.writeVarInt(i);
      }
   }

   public long[] readLongArray() {
      int readableBytes = ByteBufHelper.readableBytes(this.buffer) / 8;
      int size = this.readVarInt();
      if (size > readableBytes) {
         throw new IllegalStateException("LongArray with size " + size + " is bigger than allowed " + readableBytes);
      } else {
         long[] array = new long[size];

         for(int i = 0; i < array.length; ++i) {
            array[i] = this.readLong();
         }

         return array;
      }
   }

   public void writeLongArray(long[] array) {
      this.writeVarInt(array.length);
      for (long l : array) {
         this.writeLong(l);
      }
   }

   public UUID readUUID() {
      long mostSigBits = this.readLong();
      long leastSigBits = this.readLong();
      return new UUID(mostSigBits, leastSigBits);
   }

   public void writeUUID(UUID uuid) {
      this.writeLong(uuid.getMostSignificantBits());
      this.writeLong(uuid.getLeastSignificantBits());
   }

   public Vector3i readBlockPosition() {
      long val = this.readLong();
      return new Vector3i(val, this.serverVersion);
   }

   public void writeBlockPosition(Vector3i pos) {
      long val = pos.getSerializedPosition(this.serverVersion);
      this.writeLong(val);
   }

   public GameMode readGameMode() {
      return GameMode.getById(this.readByte());
   }

   public void writeGameMode(@Nullable GameMode mode) {
      int id = mode == null ? -1 : mode.getId();
      this.writeByte(id);
   }

   public List<EntityData> readEntityMetadata() {
      List<EntityData> list = new ArrayList();
      int typeID;
      EntityDataType type;
      Object value;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         boolean v1_10 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10);

         short index;
         while((index = this.readUnsignedByte()) != 255) {
            typeID = v1_10 ? this.readVarInt() : this.readUnsignedByte();
            type = EntityDataTypes.getById(this.serverVersion.toClientVersion(), typeID);
            if (type == null) {
               throw new IllegalStateException("Unknown entity metadata type id: " + typeID + " version " + this.serverVersion.toClientVersion());
            }

            value = type.getDataDeserializer().apply(this);
            list.add(new EntityData(index, type, value));
         }
      } else {
         for(byte data = this.readByte(); data != 127; data = this.readByte()) {
            typeID = data & 31;
            type = EntityDataTypes.getById(this.serverVersion.toClientVersion(), typeID);
            value = type.getDataDeserializer().apply(this);
            EntityData entityData = new EntityData(typeID, type, value);
            list.add(entityData);
         }
      }

      return list;
   }

   public void writeEntityMetadata(List<EntityData> list) {
      if (list == null) {
         list = new ArrayList();
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         boolean v1_10 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10);

         for (EntityData entityData : list) {
            this.writeByte(entityData.getIndex());
            if (v1_10) {
               this.writeVarInt(entityData.getType().getId(this.serverVersion.toClientVersion()));
            } else {
               this.writeByte(entityData.getType().getId(this.serverVersion.toClientVersion()));
            }
            entityData.getType().getDataSerializer().accept(this, entityData.getValue());
         }

         this.writeByte(255);
      } else {
         for (EntityData entityData : list) {
            int typeID = entityData.getType().getId(this.serverVersion.toClientVersion());
            int index = entityData.getIndex();
            int data = (typeID << 5 | index & 31) & 255;
            this.writeByte(data);
            entityData.getType().getDataSerializer().accept(this, entityData.getValue());
         }

         this.writeByte(127);
      }

   }

   public void writeEntityMetadata(EntityMetadataProvider metadata) {
      this.writeEntityMetadata(metadata.entityData(this.serverVersion.toClientVersion()));
   }

   public Dimension readDimension() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         Dimension dimension = new Dimension(new NBTCompound());
         dimension.setDimensionName(this.readIdentifier().toString());
         return dimension;
      } else {
         return new Dimension(this.readNBT());
      }
   }

   public void writeDimension(Dimension dimension) {
      boolean v1_19 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
      boolean v1_16_2 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2);
      if (!v1_19 && v1_16_2) {
         this.writeNBT(dimension.getAttributes());
      } else {
         this.writeString(dimension.getDimensionName(), 32767);
      }

   }

   public SaltSignature readSaltSignature() {
      long salt = this.readLong();
      byte[] signature;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         if (this.readBoolean()) {
            signature = this.readBytes(256);
         } else {
            signature = new byte[0];
         }
      } else {
         signature = this.readByteArray(256);
      }

      return new SaltSignature(salt, signature);
   }

   public void writeSaltSignature(SaltSignature signature) {
      this.writeLong(signature.getSalt());
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         boolean present = signature.getSignature().length != 0;
         this.writeBoolean(present);
         if (present) {
            this.writeBytes(signature.getSignature());
         }
      } else {
         this.writeByteArray(signature.getSignature());
      }

   }

   public PublicKey readPublicKey() {
      return MinecraftEncryptionUtil.publicKey(this.readByteArray(512));
   }

   public void writePublicKey(PublicKey publicKey) {
      this.writeByteArray(publicKey.getEncoded());
   }

   public PublicProfileKey readPublicProfileKey() {
      Instant expiresAt = this.readTimestamp();
      PublicKey key = this.readPublicKey();
      byte[] keySignature = this.readByteArray(4096);
      return new PublicProfileKey(expiresAt, key, keySignature);
   }

   public void writePublicProfileKey(PublicProfileKey key) {
      this.writeTimestamp(key.getExpiresAt());
      this.writePublicKey(key.getKey());
      this.writeByteArray(key.getKeySignature());
   }

   public RemoteChatSession readRemoteChatSession() {
      return new RemoteChatSession(this.readUUID(), this.readPublicProfileKey());
   }

   public void writeRemoteChatSession(RemoteChatSession chatSession) {
      this.writeUUID(chatSession.getSessionId());
      this.writePublicProfileKey(chatSession.getPublicProfileKey());
   }

   public Instant readTimestamp() {
      return Instant.ofEpochMilli(this.readLong());
   }

   public void writeTimestamp(Instant timestamp) {
      this.writeLong(timestamp.toEpochMilli());
   }

   public SignatureData readSignatureData() {
      return new SignatureData(this.readTimestamp(), this.readPublicKey(), this.readByteArray(4096));
   }

   public void writeSignatureData(SignatureData signatureData) {
      this.writeTimestamp(signatureData.getTimestamp());
      this.writePublicKey(signatureData.getPublicKey());
      this.writeByteArray(signatureData.getSignature());
   }

   public static <K> IntFunction<K> limitValue(IntFunction<K> function, int limit) {
      return (i) -> {
         if (i > limit) {
            throw new RuntimeException("Value " + i + " is larger than limit " + limit);
         } else {
            return function.apply(i);
         }
      };
   }

   public WorldBlockPosition readWorldBlockPosition() {
      return new WorldBlockPosition(this.readIdentifier(), this.readBlockPosition());
   }

   public void writeWorldBlockPosition(WorldBlockPosition pos) {
      this.writeIdentifier(pos.getWorld());
      this.writeBlockPosition(pos.getBlockPosition());
   }

   public LastSeenMessages.Entry readLastSeenMessagesEntry() {
      return new LastSeenMessages.Entry(this.readUUID(), this.readByteArray());
   }

   public void writeLastMessagesEntry(LastSeenMessages.Entry entry) {
      this.writeUUID(entry.getUUID());
      this.writeByteArray(entry.getLastVerifier());
   }

   public LastSeenMessages.Update readLastSeenMessagesUpdate() {
      int signedMessages = this.readVarInt();
      BitSet seen = BitSet.valueOf(this.readBytes(3));
      return new LastSeenMessages.Update(signedMessages, seen);
   }

   public void writeLastSeenMessagesUpdate(LastSeenMessages.Update update) {
      this.writeVarInt(update.getOffset());
      byte[] lastSeen = Arrays.copyOf(update.getAcknowledged().toByteArray(), 3);
      this.writeBytes(lastSeen);
   }

   public LastSeenMessages.LegacyUpdate readLegacyLastSeenMessagesUpdate() {
      LastSeenMessages lastSeenMessages = this.readLastSeenMessages();
      LastSeenMessages.Entry lastReceived = (LastSeenMessages.Entry)this.readOptional(PacketWrapper::readLastSeenMessagesEntry);
      return new LastSeenMessages.LegacyUpdate(lastSeenMessages, lastReceived);
   }

   public void writeLegacyLastSeenMessagesUpdate(LastSeenMessages.LegacyUpdate legacyUpdate) {
      this.writeLastSeenMessages(legacyUpdate.getLastSeenMessages());
      this.writeOptional(legacyUpdate.getLastReceived(), PacketWrapper::writeLastMessagesEntry);
   }

   public MessageSignature.Packed readMessageSignaturePacked() {
      int id = this.readVarInt() - 1;
      return id == -1 ? new MessageSignature.Packed(new MessageSignature(this.readBytes(256))) : new MessageSignature.Packed(id);
   }

   public void writeMessageSignaturePacked(MessageSignature.Packed messageSignaturePacked) {
      this.writeVarInt(messageSignaturePacked.getId() + 1);
      if (messageSignaturePacked.getFullSignature().isPresent()) {
         this.writeBytes(((MessageSignature)messageSignaturePacked.getFullSignature().get()).getBytes());
      }

   }

   public LastSeenMessages.Packed readLastSeenMessagesPacked() {
      List<MessageSignature.Packed> packedMessageSignatures = (List)this.readCollection(limitValue(ArrayList::new, 20), PacketWrapper::readMessageSignaturePacked);
      return new LastSeenMessages.Packed(packedMessageSignatures);
   }

   public void writeLastSeenMessagesPacked(LastSeenMessages.Packed lastSeenMessagesPacked) {
      this.writeCollection(lastSeenMessagesPacked.getPackedMessageSignatures(), PacketWrapper::writeMessageSignaturePacked);
   }

   public LastSeenMessages readLastSeenMessages() {
      List<LastSeenMessages.Entry> entries = (List)this.readCollection(limitValue(ArrayList::new, 5), PacketWrapper::readLastSeenMessagesEntry);
      return new LastSeenMessages(entries);
   }

   public void writeLastSeenMessages(LastSeenMessages lastSeenMessages) {
      this.writeCollection(lastSeenMessages.getEntries(), PacketWrapper::writeLastMessagesEntry);
   }

   public List<SignedCommandArgument> readSignedCommandArguments() {
      return (List)this.readCollection(ArrayList::new, (_packet) -> {
         return new SignedCommandArgument(this.readString(), this.readByteArray());
      });
   }

   public void writeSignedCommandArguments(List<SignedCommandArgument> signedArguments) {
      this.writeCollection(signedArguments, (_packet, argument) -> {
         this.writeString(argument.getArgument());
         this.writeByteArray(argument.getSignature());
      });
   }

   public BitSet readBitSet() {
      return BitSet.valueOf(this.readLongArray());
   }

   public void writeBitSet(BitSet bitSet) {
      this.writeLongArray(bitSet.toLongArray());
   }

   public FilterMask readFilterMask() {
      FilterMaskType type = FilterMaskType.getById(this.readVarInt());
      switch(type) {
      case PARTIALLY_FILTERED:
         return new FilterMask(this.readBitSet());
      case PASS_THROUGH:
         return FilterMask.PASS_THROUGH;
      case FULLY_FILTERED:
         return FilterMask.FULLY_FILTERED;
      default:
         return null;
      }
   }

   public void writeFilterMask(FilterMask filterMask) {
      this.writeVarInt(filterMask.getType().getId());
      if (filterMask.getType() == FilterMaskType.PARTIALLY_FILTERED) {
         this.writeBitSet(filterMask.getMask());
      }

   }

   public MerchantOffer readMerchantOffer() {
      ItemStack buyItemPrimary = this.readItemStack();
      ItemStack sellItem = this.readItemStack();
      ItemStack buyItemSecondary = (ItemStack)this.readOptional(PacketWrapper::readItemStack);
      boolean tradeDisabled = this.readBoolean();
      int uses = this.readInt();
      int maxUses = this.readInt();
      int xp = this.readInt();
      int specialPrice = this.readInt();
      float priceMultiplier = this.readFloat();
      int demand = this.readInt();
      MerchantOffer data = MerchantOffer.of(buyItemPrimary, buyItemSecondary, sellItem, uses, maxUses, xp, specialPrice, priceMultiplier, demand);
      if (tradeDisabled) {
         data.setUses(data.getMaxUses());
      }

      return data;
   }

   public void writeMerchantOffer(MerchantOffer data) {
      this.writeItemStack(data.getFirstInputItem());
      this.writeItemStack(data.getOutputItem());
      ItemStack buyItemSecondary = data.getSecondInputItem();
      if (buyItemSecondary != null && buyItemSecondary.isEmpty()) {
         buyItemSecondary = null;
      }

      this.writeOptional(buyItemSecondary, PacketWrapper::writeItemStack);
      this.writeBoolean(data.getUses() >= data.getMaxUses());
      this.writeInt(data.getUses());
      this.writeInt(data.getMaxUses());
      this.writeInt(data.getXp());
      this.writeInt(data.getSpecialPrice());
      this.writeFloat(data.getPriceMultiplier());
      this.writeInt(data.getDemand());
   }

   public ChatMessage_v1_19_1.ChatTypeBoundNetwork readChatTypeBoundNetwork() {
      int id = this.readVarInt();
      ChatType type = ChatTypes.getById(this.getServerVersion().toClientVersion(), id);
      Component name = this.readComponent();
      Component targetName = (Component)this.readOptional(PacketWrapper::readComponent);
      return new ChatMessage_v1_19_1.ChatTypeBoundNetwork(type, name, targetName);
   }

   public void writeChatTypeBoundNetwork(ChatMessage_v1_19_1.ChatTypeBoundNetwork chatType) {
      this.writeVarInt(chatType.getType().getId(this.getServerVersion().toClientVersion()));
      this.writeComponent(chatType.getName());
      this.writeOptional(chatType.getTargetName(), PacketWrapper::writeComponent);
   }

   public Node readNode() {
      byte flags = this.readByte();
      int nodeType = flags & 3;
      boolean hasRedirect = (flags & 8) != 0;
      boolean hasSuggestionsType = nodeType == 2 && (flags & 16) != 0;
      List<Integer> children = this.readList(PacketWrapper::readVarInt);
      Integer redirectNodeIndex = hasRedirect ? this.readVarInt() : null;
      String name = nodeType != 1 && nodeType != 2 ? null : this.readString();
      Integer parserID = nodeType == 2 ? this.readVarInt() : null;
      List<Object> properties = null;
      if (nodeType == 2) {
         Optional<List<Object>> propertiesOpt = ((Parsers.Parser)Parsers.getParsers().get(parserID)).readProperties(this);
         properties = propertiesOpt.orElse(null);
      }
      ResourceLocation suggestionType = hasSuggestionsType ? this.readIdentifier() : null;
      return new Node(flags, children, redirectNodeIndex, name, parserID, properties, suggestionType);
   }

   public void writeNode(Node node) {
      this.writeByte(node.getFlags());
      this.writeList(node.getChildren(), PacketWrapper::writeVarInt);
      node.getRedirectNodeIndex().ifPresent(this::writeVarInt);
      node.getName().ifPresent(this::writeString);
      node.getParserID().ifPresent(this::writeVarInt);
      if (node.getProperties().isPresent()) {
         ((Parsers.Parser)Parsers.getParsers().get((Integer)node.getParserID().get())).writeProperties(this, (List)node.getProperties().get());
      }

      node.getSuggestionsType().ifPresent(this::writeIdentifier);
   }

   public <T extends Enum<T>> EnumSet<T> readEnumSet(Class<T> enumClazz) {
      @SuppressWarnings("unchecked")
      T[] values = (T[])enumClazz.getEnumConstants();
      byte[] bytes = new byte[-Math.floorDiv(-values.length, 8)];
      ByteBufHelper.readBytes(this.getBuffer(), bytes);
      BitSet bitSet = BitSet.valueOf(bytes);
      EnumSet<T> set = EnumSet.noneOf(enumClazz);

      for(int i = 0; i < values.length; ++i) {
         if (bitSet.get(i)) {
            set.add(values[i]);
         }
      }

      return set;
   }

   public <T extends Enum<T>> void writeEnumSet(EnumSet<T> set, Class<T> enumClazz) {
      @SuppressWarnings("unchecked")
      T[] values = (T[])enumClazz.getEnumConstants();
      BitSet bitSet = new BitSet(values.length);

      for(int i = 0; i < values.length; ++i) {
         if (set.contains(values[i])) {
            bitSet.set(i);
         }
      }

      this.writeBytes(Arrays.copyOf(bitSet.toByteArray(), -Math.floorDiv(-values.length, 8)));
   }

   @ApiStatus.Experimental
   public <U, V, R> U readMultiVersional(VersionComparison version, ServerVersion target, PacketWrapper.Reader<V> first, PacketWrapper.Reader<R> second) {
      @SuppressWarnings("unchecked")
      U result = (U)(this.serverVersion.is(version, target) ? first.apply(this) : second.apply(this));
      return result;
   }

   @ApiStatus.Experimental
   public <V> void writeMultiVersional(VersionComparison version, ServerVersion target, V value, PacketWrapper.Writer<V> first, PacketWrapper.Writer<V> second) {
      if (this.serverVersion.is(version, target)) {
         first.accept(this, value);
      } else {
         second.accept(this, value);
      }

   }

   public <R> R readOptional(PacketWrapper.Reader<R> reader) {
      return this.readBoolean() ? reader.apply(this) : null;
   }

   public <V> void writeOptional(V value, PacketWrapper.Writer<V> writer) {
      if (value != null) {
         this.writeBoolean(true);
         writer.accept(this, value);
      } else {
         this.writeBoolean(false);
      }

   }

   public <K, C extends Collection<K>> C readCollection(IntFunction<C> function, PacketWrapper.Reader<K> reader) {
      int size = this.readVarInt();
      C collection = function.apply(size);

      for(int i = 0; i < size; ++i) {
         collection.add(reader.apply(this));
      }

      return collection;
   }

   public <K> void writeCollection(Collection<K> collection, PacketWrapper.Writer<K> writer) {
      this.writeVarInt(collection.size());
      for (K key : collection) {
         writer.accept(this, key);
      }
   }

   public <K> List<K> readList(PacketWrapper.Reader<K> reader) {
      return (List)this.readCollection(ArrayList::new, reader);
   }

   public <K> void writeList(List<K> list, PacketWrapper.Writer<K> writer) {
      this.writeVarInt(list.size());
      for (K key : list) {
         writer.accept(this, key);
      }
   }

   @FunctionalInterface
   public interface Reader<T> extends Function<PacketWrapper<?>, T> {
   }

   @FunctionalInterface
   public interface Writer<T> extends BiConsumer<PacketWrapper<?>, T> {
   }
}
