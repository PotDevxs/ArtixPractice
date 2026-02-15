package dev.artixdev.libs.io.github.retrooper.packetevents.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.MapMaker;
import io.netty.buffer.PooledByteBufAllocator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.ItemStack;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.TextureProperty;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection.Reflection;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public final class SpigotReflectionUtil {
   private static final String MODIFIED_PACKAGE_NAME = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
   public static final String LEGACY_NMS_PACKAGE;
   public static final String OBC_PACKAGE;
   public static ServerVersion VERSION;
   public static boolean V_1_19_OR_HIGHER;
   public static boolean V_1_17_OR_HIGHER;
   public static boolean V_1_12_OR_HIGHER;
   public static Class<?> MINECRAFT_SERVER_CLASS;
   public static Class<?> NMS_PACKET_DATA_SERIALIZER_CLASS;
   public static Class<?> NMS_ITEM_STACK_CLASS;
   public static Class<?> NMS_IMATERIAL_CLASS;
   public static Class<?> NMS_ENTITY_CLASS;
   public static Class<?> ENTITY_PLAYER_CLASS;
   public static Class<?> BOUNDING_BOX_CLASS;
   public static Class<?> NMS_MINECRAFT_KEY_CLASS;
   public static Class<?> ENTITY_HUMAN_CLASS;
   public static Class<?> PLAYER_CONNECTION_CLASS;
   public static Class<?> SERVER_COMMON_PACKETLISTENER_IMPL_CLASS;
   public static Class<?> SERVER_CONNECTION_CLASS;
   public static Class<?> NETWORK_MANAGER_CLASS;
   public static Class<?> NMS_ENUM_PARTICLE_CLASS;
   public static Class<?> MOB_EFFECT_LIST_CLASS;
   public static Class<?> NMS_ITEM_CLASS;
   public static Class<?> DEDICATED_SERVER_CLASS;
   public static Class<?> NMS_WORLD_CLASS;
   public static Class<?> WORLD_SERVER_CLASS;
   public static Class<?> ENUM_PROTOCOL_DIRECTION_CLASS;
   public static Class<?> GAME_PROFILE_CLASS;
   public static Class<?> CRAFT_WORLD_CLASS;
   public static Class<?> CRAFT_SERVER_CLASS;
   public static Class<?> CRAFT_PLAYER_CLASS;
   public static Class<?> CRAFT_ENTITY_CLASS;
   public static Class<?> CRAFT_ITEM_STACK_CLASS;
   public static Class<?> CRAFT_PARTICLE_CLASS;
   public static Class<?> LEVEL_ENTITY_GETTER_CLASS;
   public static Class<?> PERSISTENT_ENTITY_SECTION_MANAGER_CLASS;
   public static Class<?> CRAFT_MAGIC_NUMBERS_CLASS;
   public static Class<?> IBLOCK_DATA_CLASS;
   public static Class<?> BLOCK_CLASS;
   public static Class<?> CRAFT_BLOCK_DATA_CLASS;
   public static Class<?> PROPERTY_MAP_CLASS;
   public static Class<?> DIMENSION_MANAGER_CLASS;
   public static Class<?> MOJANG_CODEC_CLASS;
   public static Class<?> MOJANG_ENCODER_CLASS;
   public static Class<?> DATA_RESULT_CLASS;
   public static Class<?> DYNAMIC_OPS_NBT_CLASS;
   public static Class<?> NMS_NBT_COMPOUND_CLASS;
   public static Class<?> NBT_COMPRESSION_STREAM_TOOLS_CLASS;
   public static Class<?> CHANNEL_CLASS;
   public static Class<?> BYTE_BUF_CLASS;
   public static Class<?> BYTE_TO_MESSAGE_DECODER;
   public static Class<?> MESSAGE_TO_BYTE_ENCODER;
   public static Field ENTITY_PLAYER_PING_FIELD;
   public static Field ENTITY_BOUNDING_BOX_FIELD;
   public static Field BYTE_BUF_IN_PACKET_DATA_SERIALIZER;
   public static Field DIMENSION_CODEC_FIELD;
   public static Field DYNAMIC_OPS_NBT_INSTANCE_FIELD;
   public static Field CRAFT_PARTICLE_PARTICLES_FIELD;
   public static Field NMS_MK_KEY_FIELD;
   public static Field LEGACY_NMS_PARTICLE_KEY_FIELD;
   public static Field LEGACY_NMS_KEY_TO_NMS_PARTICLE;
   public static Method IS_DEBUGGING;
   public static Method GET_CRAFT_PLAYER_HANDLE_METHOD;
   public static Method GET_CRAFT_ENTITY_HANDLE_METHOD;
   public static Method GET_CRAFT_WORLD_HANDLE_METHOD;
   public static Method GET_MOB_EFFECT_LIST_ID_METHOD;
   public static Method GET_MOB_EFFECT_LIST_BY_ID_METHOD;
   public static Method GET_ITEM_ID_METHOD;
   public static Method GET_ITEM_BY_ID_METHOD;
   public static Method GET_BUKKIT_ENTITY_METHOD;
   public static Method GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD;
   public static Method GET_ENTITY_BY_ID_METHOD;
   public static Method CRAFT_ITEM_STACK_AS_BUKKIT_COPY;
   public static Method CRAFT_ITEM_STACK_AS_NMS_COPY;
   public static Method BUKKIT_PARTICLE_TO_NMS_ENUM_PARTICLE;
   public static Method NMS_ENUM_PARTICLE_TO_BUKKIT_PARTICLE;
   public static Method READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD;
   public static Method WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD;
   public static Method GET_COMBINED_ID;
   public static Method GET_BY_COMBINED_ID;
   public static Method GET_CRAFT_BLOCK_DATA_FROM_IBLOCKDATA;
   public static Method PROPERTY_MAP_GET_METHOD;
   public static Method GET_DIMENSION_MANAGER;
   public static Method GET_DIMENSION_ID;
   public static Method GET_DIMENSION_KEY;
   public static Method CODEC_ENCODE_METHOD;
   public static Method DATA_RESULT_GET_METHOD;
   public static Method READ_NBT_FROM_STREAM_METHOD;
   public static Method WRITE_NBT_TO_STREAM_METHOD;
   private static Constructor<?> NMS_ITEM_STACK_CONSTRUCTOR;
   private static Constructor<?> NMS_PACKET_DATA_SERIALIZER_CONSTRUCTOR;
   private static Constructor<?> NMS_MINECRAFT_KEY_CONSTRUCTOR;
   private static Object MINECRAFT_SERVER_INSTANCE;
   private static Object MINECRAFT_SERVER_CONNECTION_INSTANCE;
   public static Map<Integer, Entity> ENTITY_ID_CACHE;

   private static void initConstructors() {
      Class itemClass = NMS_IMATERIAL_CLASS != null ? NMS_IMATERIAL_CLASS : NMS_ITEM_CLASS;

      try {
         NMS_ITEM_STACK_CONSTRUCTOR = NMS_ITEM_STACK_CLASS.getConstructor(itemClass, Integer.TYPE);
         NMS_PACKET_DATA_SERIALIZER_CONSTRUCTOR = NMS_PACKET_DATA_SERIALIZER_CLASS.getConstructor(BYTE_BUF_CLASS);
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            NMS_MINECRAFT_KEY_CONSTRUCTOR = NMS_MINECRAFT_KEY_CLASS.getConstructor(String.class, String.class);
         }
      } catch (NoSuchMethodException e) {
         e.printStackTrace();
      }

   }

   private static void initMethods() {
      IS_DEBUGGING = Reflection.getMethod(MINECRAFT_SERVER_CLASS, (String)"isDebugging", 0);
      GET_BUKKIT_ENTITY_METHOD = Reflection.getMethod(NMS_ENTITY_CLASS, (Class)CRAFT_ENTITY_CLASS, 0);
      GET_CRAFT_PLAYER_HANDLE_METHOD = Reflection.getMethod(CRAFT_PLAYER_CLASS, (String)"getHandle", 0);
      GET_CRAFT_ENTITY_HANDLE_METHOD = Reflection.getMethod(CRAFT_ENTITY_CLASS, (String)"getHandle", 0);
      GET_CRAFT_WORLD_HANDLE_METHOD = Reflection.getMethod(CRAFT_WORLD_CLASS, (String)"getHandle", 0);
      GET_MOB_EFFECT_LIST_ID_METHOD = Reflection.getMethod(MOB_EFFECT_LIST_CLASS, (String)(V_1_19_OR_HIGHER ? "g" : "getId"), 0);
      GET_MOB_EFFECT_LIST_BY_ID_METHOD = Reflection.getMethod(MOB_EFFECT_LIST_CLASS, (String)(V_1_19_OR_HIGHER ? "a" : "fromId"), 0);
      GET_ITEM_ID_METHOD = Reflection.getMethod(NMS_ITEM_CLASS, (String)(V_1_19_OR_HIGHER ? "g" : "getId"), 0);
      GET_ITEM_BY_ID_METHOD = Reflection.getMethod(NMS_ITEM_CLASS, (Class)NMS_ITEM_CLASS, 0);
      if (V_1_17_OR_HIGHER) {
         GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD = Reflection.getMethod(LEVEL_ENTITY_GETTER_CLASS, (Class)Iterable.class, 0);
      }

      if (DIMENSION_MANAGER_CLASS != null) {
         if (PacketEvents.getAPI().getServerManager().getVersion() == ServerVersion.V_1_16 || PacketEvents.getAPI().getServerManager().getVersion() == ServerVersion.V_1_16_1) {
            GET_DIMENSION_KEY = Reflection.getMethod(NMS_WORLD_CLASS, (String)"getTypeKey", 0);
         }

         GET_DIMENSION_MANAGER = Reflection.getMethod(NMS_WORLD_CLASS, (Class)DIMENSION_MANAGER_CLASS, 0);
         GET_DIMENSION_ID = Reflection.getMethod(DIMENSION_MANAGER_CLASS, (Class)Integer.TYPE, 0);
      }

      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
         CODEC_ENCODE_METHOD = Reflection.getMethod(MOJANG_ENCODER_CLASS, (String)"encodeStart", 0);
         DATA_RESULT_GET_METHOD = Reflection.getMethod(DATA_RESULT_CLASS, (String)"result", 0);
      }

      String getEntityByIdMethodName = VERSION.getProtocolVersion() != 47 && !V_1_19_OR_HIGHER ? "getEntity" : "a";
      GET_ENTITY_BY_ID_METHOD = Reflection.getMethodExact(WORLD_SERVER_CLASS, getEntityByIdMethodName, NMS_ENTITY_CLASS, Integer.TYPE);
      if (GET_ENTITY_BY_ID_METHOD == null) {
         GET_ENTITY_BY_ID_METHOD = Reflection.getMethodExact(WORLD_SERVER_CLASS, "getEntity", NMS_ENTITY_CLASS, Integer.TYPE);
      }

      if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
         BUKKIT_PARTICLE_TO_NMS_ENUM_PARTICLE = Reflection.getMethod(CRAFT_PARTICLE_CLASS, "toNMS", NMS_ENUM_PARTICLE_CLASS);
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            Class<?> particleClass = Reflection.getClassByNameWithoutException("org.bukkit.Particle");
            NMS_ENUM_PARTICLE_TO_BUKKIT_PARTICLE = Reflection.getMethod(CRAFT_PARTICLE_CLASS, "toBukkit", particleClass);
         }
      }

      CRAFT_ITEM_STACK_AS_BUKKIT_COPY = Reflection.getMethod(CRAFT_ITEM_STACK_CLASS, (String)"asBukkitCopy", 0);
      CRAFT_ITEM_STACK_AS_NMS_COPY = Reflection.getMethod(CRAFT_ITEM_STACK_CLASS, (String)"asNMSCopy", 0);
      READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD = Reflection.getMethodExact(NMS_PACKET_DATA_SERIALIZER_CLASS, "k", NMS_ITEM_STACK_CLASS);
      if (READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD == null) {
         READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD = Reflection.getMethod(NMS_PACKET_DATA_SERIALIZER_CLASS, (Class)NMS_ITEM_STACK_CLASS, 0);
      }

      WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD = Reflection.getMethodExact(NMS_PACKET_DATA_SERIALIZER_CLASS, "a", NMS_PACKET_DATA_SERIALIZER_CLASS, NMS_ITEM_STACK_CLASS);
      if (WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD == null) {
         WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD = Reflection.getMethod(NMS_PACKET_DATA_SERIALIZER_CLASS, 0, NMS_ITEM_STACK_CLASS);
      }

      GET_COMBINED_ID = Reflection.getMethod(BLOCK_CLASS, IBLOCK_DATA_CLASS, 0, Integer.TYPE);
      GET_BY_COMBINED_ID = Reflection.getMethod(BLOCK_CLASS, IBLOCK_DATA_CLASS, 0, Integer.TYPE);
      if (CRAFT_BLOCK_DATA_CLASS != null) {
         GET_CRAFT_BLOCK_DATA_FROM_IBLOCKDATA = Reflection.getMethodExact(CRAFT_BLOCK_DATA_CLASS, "fromData", CRAFT_BLOCK_DATA_CLASS, IBLOCK_DATA_CLASS);
      }

      READ_NBT_FROM_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, 0, DataInputStream.class);
      if (READ_NBT_FROM_STREAM_METHOD == null) {
         READ_NBT_FROM_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, 0, DataInput.class);
      }

      WRITE_NBT_TO_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, 0, NMS_NBT_COMPOUND_CLASS, DataOutput.class);
   }

   private static void initFields() {
      ENTITY_BOUNDING_BOX_FIELD = Reflection.getField(NMS_ENTITY_CLASS, BOUNDING_BOX_CLASS, 0, true);
      ENTITY_PLAYER_PING_FIELD = Reflection.getField(ENTITY_PLAYER_CLASS, "ping");
      BYTE_BUF_IN_PACKET_DATA_SERIALIZER = Reflection.getField(NMS_PACKET_DATA_SERIALIZER_CLASS, BYTE_BUF_CLASS, 0, true);
      CRAFT_PARTICLE_PARTICLES_FIELD = Reflection.getField(CRAFT_PARTICLE_CLASS, "particles");
      NMS_MK_KEY_FIELD = Reflection.getField(NMS_MINECRAFT_KEY_CLASS, "key");
      if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
         LEGACY_NMS_PARTICLE_KEY_FIELD = Reflection.getField(NMS_ENUM_PARTICLE_CLASS, "X");
         LEGACY_NMS_KEY_TO_NMS_PARTICLE = Reflection.getField(NMS_ENUM_PARTICLE_CLASS, "ac");
      }

      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
         DIMENSION_CODEC_FIELD = Reflection.getField(DIMENSION_MANAGER_CLASS, MOJANG_CODEC_CLASS, 0);
         DYNAMIC_OPS_NBT_INSTANCE_FIELD = Reflection.getField(DYNAMIC_OPS_NBT_CLASS, DYNAMIC_OPS_NBT_CLASS, 0);
      }

   }

   private static void initClasses() {
      MINECRAFT_SERVER_CLASS = getServerClass("server.MinecraftServer", "MinecraftServer");
      NMS_PACKET_DATA_SERIALIZER_CLASS = getServerClass("network.PacketDataSerializer", "PacketDataSerializer");
      NMS_ITEM_STACK_CLASS = getServerClass("world.item.ItemStack", "ItemStack");
      NMS_IMATERIAL_CLASS = getServerClass("world.level.IMaterial", "IMaterial");
      NMS_ENTITY_CLASS = getServerClass("world.entity.Entity", "Entity");
      ENTITY_PLAYER_CLASS = getServerClass("server.level.EntityPlayer", "EntityPlayer");
      BOUNDING_BOX_CLASS = getServerClass("world.phys.AxisAlignedBB", "AxisAlignedBB");
      NMS_MINECRAFT_KEY_CLASS = getServerClass("resources.MinecraftKey", "MinecraftKey");
      ENTITY_HUMAN_CLASS = getServerClass("world.entity.player.EntityHuman", "EntityHuman");
      PLAYER_CONNECTION_CLASS = getServerClass("server.network.PlayerConnection", "PlayerConnection");
      SERVER_COMMON_PACKETLISTENER_IMPL_CLASS = getServerClass("server.network.ServerCommonPacketListenerImpl", "ServerCommonPacketListenerImpl");
      SERVER_CONNECTION_CLASS = getServerClass("server.network.ServerConnection", "ServerConnection");
      NETWORK_MANAGER_CLASS = getServerClass("network.NetworkManager", "NetworkManager");
      MOB_EFFECT_LIST_CLASS = getServerClass("world.effect.MobEffectList", "MobEffectList");
      NMS_ITEM_CLASS = getServerClass("world.item.Item", "Item");
      DEDICATED_SERVER_CLASS = getServerClass("server.dedicated.DedicatedServer", "DedicatedServer");
      NMS_WORLD_CLASS = getServerClass("world.level.World", "World");
      WORLD_SERVER_CLASS = getServerClass("server.level.WorldServer", "WorldServer");
      ENUM_PROTOCOL_DIRECTION_CLASS = getServerClass("network.protocol.EnumProtocolDirection", "EnumProtocolDirection");
      if (V_1_17_OR_HIGHER) {
         LEVEL_ENTITY_GETTER_CLASS = getServerClass("world.level.entity.LevelEntityGetter", "");
         PERSISTENT_ENTITY_SECTION_MANAGER_CLASS = getServerClass("world.level.entity.PersistentEntitySectionManager", "");
      }

      DIMENSION_MANAGER_CLASS = getServerClass("world.level.dimension.DimensionManager", "DimensionManager");
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
         MOJANG_CODEC_CLASS = Reflection.getClassByNameWithoutException("com.mojang.serialization.Codec");
         MOJANG_ENCODER_CLASS = Reflection.getClassByNameWithoutException("com.mojang.serialization.Encoder");
         DATA_RESULT_CLASS = Reflection.getClassByNameWithoutException("com.mojang.serialization.DataResult");
         DYNAMIC_OPS_NBT_CLASS = getServerClass("nbt.DynamicOpsNBT", "DynamicOpsNBT");
      }

      if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
         NMS_ENUM_PARTICLE_CLASS = getServerClass((String)null, "EnumParticle");
      }

      CRAFT_MAGIC_NUMBERS_CLASS = getOBCClass("util.CraftMagicNumbers");
      IBLOCK_DATA_CLASS = getServerClass("world.level.block.state.IBlockData", "IBlockData");
      BLOCK_CLASS = getServerClass("world.level.block.Block", "Block");
      CRAFT_BLOCK_DATA_CLASS = getOBCClass("block.data.CraftBlockData");
      GAME_PROFILE_CLASS = Reflection.getClassByNameWithoutException("com.mojang.authlib.GameProfile");
      CRAFT_WORLD_CLASS = getOBCClass("CraftWorld");
      CRAFT_PLAYER_CLASS = getOBCClass("entity.CraftPlayer");
      CRAFT_SERVER_CLASS = getOBCClass("CraftServer");
      CRAFT_ENTITY_CLASS = getOBCClass("entity.CraftEntity");
      CRAFT_ITEM_STACK_CLASS = getOBCClass("inventory.CraftItemStack");
      CRAFT_PARTICLE_CLASS = getOBCClass("CraftParticle");
      CHANNEL_CLASS = getNettyClass("channel.Channel");
      BYTE_BUF_CLASS = getNettyClass("buffer.ByteBuf");
      BYTE_TO_MESSAGE_DECODER = getNettyClass("handler.codec.ByteToMessageDecoder");
      MESSAGE_TO_BYTE_ENCODER = getNettyClass("handler.codec.MessageToByteEncoder");
      NMS_NBT_COMPOUND_CLASS = getServerClass("nbt.NBTTagCompound", "NBTTagCompound");
      NBT_COMPRESSION_STREAM_TOOLS_CLASS = getServerClass("nbt.NBTCompressedStreamTools", "NBTCompressedStreamTools");
   }

   public static void init() {
      VERSION = PacketEvents.getAPI().getServerManager().getVersion();
      V_1_19_OR_HIGHER = VERSION.isNewerThanOrEquals(ServerVersion.V_1_19);
      V_1_17_OR_HIGHER = VERSION.isNewerThanOrEquals(ServerVersion.V_1_17);
      V_1_12_OR_HIGHER = VERSION.isNewerThanOrEquals(ServerVersion.V_1_12);
      initClasses();
      initFields();
      initMethods();
      initConstructors();
   }

   @Nullable
   public static Class<?> getServerClass(String modern, String legacy) {
      return V_1_17_OR_HIGHER ? Reflection.getClassByNameWithoutException("net.minecraft." + modern) : Reflection.getClassByNameWithoutException(LEGACY_NMS_PACKAGE + legacy);
   }

   public static boolean isMinecraftServerInstanceDebugging() {
      Object minecraftServerInstance = getMinecraftServerInstance(Bukkit.getServer());
      if (minecraftServerInstance != null && IS_DEBUGGING != null) {
         try {
            return (Boolean)IS_DEBUGGING.invoke(minecraftServerInstance);
         } catch (InvocationTargetException | IllegalAccessException e) {
            IS_DEBUGGING = null;
            return false;
         }
      } else {
         return false;
      }
   }

   public static Object getMinecraftServerInstance(Server server) {
      if (MINECRAFT_SERVER_INSTANCE == null) {
         try {
            MINECRAFT_SERVER_INSTANCE = Reflection.getField(CRAFT_SERVER_CLASS, MINECRAFT_SERVER_CLASS, 0).get(server);
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         }
      }

      return MINECRAFT_SERVER_INSTANCE;
   }

   public static Object getMinecraftServerConnectionInstance() {
      if (MINECRAFT_SERVER_CONNECTION_INSTANCE == null) {
         try {
            MINECRAFT_SERVER_CONNECTION_INSTANCE = Reflection.getField(MINECRAFT_SERVER_CLASS, SERVER_CONNECTION_CLASS, 0).get(getMinecraftServerInstance(Bukkit.getServer()));
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         }
      }

      return MINECRAFT_SERVER_CONNECTION_INSTANCE;
   }

   public static double getTPS() {
      return recentTPS()[0];
   }

   public static double[] recentTPS() {
      return (new ReflectionObject(getMinecraftServerInstance(Bukkit.getServer()), MINECRAFT_SERVER_CLASS)).readDoubleArray(0);
   }

   public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
      return Class.forName(LEGACY_NMS_PACKAGE + name);
   }

   public static Class<?> getOBCClass(String name) {
      return Reflection.getClassByNameWithoutException(OBC_PACKAGE + name);
   }

   public static Class<?> getNettyClass(String name) {
      return Reflection.getClassByNameWithoutException("io.netty." + name);
   }

   public static Entity getBukkitEntity(Object nmsEntity) {
      Object craftEntity = null;

      try {
         craftEntity = GET_BUKKIT_ENTITY_METHOD.invoke(nmsEntity);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
      }

      return (Entity)craftEntity;
   }

   public static Object getNMSEntity(Entity entity) {
      Object craftEntity = CRAFT_ENTITY_CLASS.cast(entity);

      try {
         return GET_CRAFT_ENTITY_HANDLE_METHOD.invoke(craftEntity);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Object getNMSAxisAlignedBoundingBox(Object nmsEntity) {
      try {
         return ENTITY_BOUNDING_BOX_FIELD.get(NMS_ENTITY_CLASS.cast(nmsEntity));
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         return null;
      }
   }

   public static Object getCraftPlayer(Player player) {
      return CRAFT_PLAYER_CLASS.cast(player);
   }

   public static Object getEntityPlayer(Player player) {
      Object craftPlayer = getCraftPlayer(player);

      try {
         return GET_CRAFT_PLAYER_HANDLE_METHOD.invoke(craftPlayer);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Object getPlayerConnection(Player player) {
      Object entityPlayer = getEntityPlayer(player);
      if (entityPlayer == null) {
         return null;
      } else {
         ReflectionObject wrappedEntityPlayer = new ReflectionObject(entityPlayer, ENTITY_PLAYER_CLASS);
         return wrappedEntityPlayer.readObject(0, PLAYER_CONNECTION_CLASS);
      }
   }

   public static Object getGameProfile(Player player) {
      Object entityPlayer = getEntityPlayer(player);
      ReflectionObject entityHumanWrapper = new ReflectionObject(entityPlayer, ENTITY_HUMAN_CLASS);
      return entityHumanWrapper.readObject(0, GAME_PROFILE_CLASS);
   }

   public static List<TextureProperty> getUserProfile(Player player) {
      if (PROPERTY_MAP_CLASS == null) {
         PROPERTY_MAP_CLASS = Reflection.getClassByNameWithoutException("com.mojang.authlib.properties.PropertyMap");
         PROPERTY_MAP_GET_METHOD = Reflection.getMethodExact(PROPERTY_MAP_CLASS, "get", Collection.class, Object.class);
      }

      Object nmsGameProfile = getGameProfile(player);
      ReflectionObject reflectGameProfile = new ReflectionObject(nmsGameProfile);
      Object nmsPropertyMap = reflectGameProfile.readObject(0, PROPERTY_MAP_CLASS);
      Collection nmsProperties = null;

      try {
         nmsProperties = (Collection)PROPERTY_MAP_GET_METHOD.invoke(nmsPropertyMap, "textures");
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
      }

      List<TextureProperty> properties = new ArrayList();
      Iterator var6 = nmsProperties.iterator();

      while(var6.hasNext()) {
         Object nmsProperty = var6.next();
         ReflectionObject reflectProperty = new ReflectionObject(nmsProperty);
         String name = "textures";
         String value = reflectProperty.readString(1);
         String signature = reflectProperty.readString(2);
         TextureProperty textureProperty = new TextureProperty(name, value, signature);
         properties.add(textureProperty);
      }

      return properties;
   }

   public static Object getNetworkManager(Player player) {
      Object playerConnection = getPlayerConnection(player);
      if (playerConnection == null) {
         return null;
      } else {
         Class<?> playerConnectionClass = SERVER_COMMON_PACKETLISTENER_IMPL_CLASS != null ? SERVER_COMMON_PACKETLISTENER_IMPL_CLASS : PLAYER_CONNECTION_CLASS;
         ReflectionObject wrapper = new ReflectionObject(playerConnection, playerConnectionClass);

         try {
            return wrapper.readObject(0, NETWORK_MANAGER_CLASS);
         } catch (Exception e) {
            try {
               playerConnection = wrapper.read(0, PLAYER_CONNECTION_CLASS);
               wrapper = new ReflectionObject(playerConnection, PLAYER_CONNECTION_CLASS);
               return wrapper.readObject(0, NETWORK_MANAGER_CLASS);
            } catch (Exception suppressed) {
               e.printStackTrace();
               return null;
            }
         }
      }
   }

   public static Object getChannel(Player player) {
      Object networkManager = getNetworkManager(player);
      if (networkManager == null) {
         return null;
      } else {
         ReflectionObject wrapper = new ReflectionObject(networkManager, NETWORK_MANAGER_CLASS);
         return wrapper.readObject(0, CHANNEL_CLASS);
      }
   }

   /** @deprecated */
   @Deprecated
   public static int getPlayerPingLegacy(Player player) {
      if (V_1_17_OR_HIGHER) {
         return -1;
      } else {
         if (ENTITY_PLAYER_PING_FIELD != null) {
            Object entityPlayer = getEntityPlayer(player);

            try {
               return ENTITY_PLAYER_PING_FIELD.getInt(entityPlayer);
            } catch (IllegalAccessException e) {
               e.printStackTrace();
            }
         }

         return -1;
      }
   }

   public static List<Object> getNetworkManagers() {
      ReflectionObject serverConnectionWrapper = new ReflectionObject(getMinecraftServerConnectionInstance());
      int i = 0;

      while(true) {
         try {
            List<?> list = (List)serverConnectionWrapper.readObject(i, List.class);
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               Object obj = var3.next();
               if (obj.getClass().isAssignableFrom(NETWORK_MANAGER_CLASS)) {
                  return (List<Object>) list;
               }
            }
         } catch (Exception e) {
            return (List)serverConnectionWrapper.readObject(1, List.class);
         }

         ++i;
      }
   }

   public static Object convertBukkitServerToNMSServer(Server server) {
      Object craftServer = CRAFT_SERVER_CLASS.cast(server);
      ReflectionObject wrapper = new ReflectionObject(craftServer);

      try {
         return wrapper.readObject(0, MINECRAFT_SERVER_CLASS);
      } catch (Exception e) {
         wrapper.readObject(0, DEDICATED_SERVER_CLASS);
         return null;
      }
   }

   public static Object convertBukkitWorldToWorldServer(World world) {
      Object craftWorld = CRAFT_WORLD_CLASS.cast(world);

      try {
         return GET_CRAFT_WORLD_HANDLE_METHOD.invoke(craftWorld);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Object convertWorldServerDimensionToNMSNbt(Object worldServer) {
      try {
         Object dimension = GET_DIMENSION_MANAGER.invoke(worldServer);
         Object dynamicNbtOps = DYNAMIC_OPS_NBT_INSTANCE_FIELD.get((Object)null);
         Object dataResult = CODEC_ENCODE_METHOD.invoke(DIMENSION_CODEC_FIELD.get((Object)null), dynamicNbtOps, dimension);
         Optional<?> optional = (Optional)DATA_RESULT_GET_METHOD.invoke(dataResult);
         return optional.orElse(null);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static int getDimensionId(Object worldServer) {
      try {
         Object dimension = GET_DIMENSION_MANAGER.invoke(worldServer);
         return (Integer)GET_DIMENSION_ID.invoke(dimension);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return 0;
      }
   }

   public static String getDimensionKey(Object worldServer) {
      try {
         return GET_DIMENSION_KEY.invoke(worldServer).toString();
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static String fromStringToJSON(String message) {
      return message == null ? null : "{\"text\": \"" + message + "\"}";
   }

   public static int generateEntityId() {
      Field field = Reflection.getField(NMS_ENTITY_CLASS, "entityCount");
      if (field == null) {
         field = Reflection.getField(NMS_ENTITY_CLASS, AtomicInteger.class, 0);
      }

      try {
         if (field.getType().equals(AtomicInteger.class)) {
            AtomicInteger atomicInteger = (AtomicInteger)field.get((Object)null);
            return atomicInteger.incrementAndGet();
         } else {
            int id = field.getInt((Object)null) + 1;
            field.set((Object)null, id);
            return id;
         }
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         throw new IllegalStateException("Failed to generate a new unique entity ID!");
      }
   }

   public static int getEffectId(Object nmsMobEffectList) {
      try {
         return (Integer)GET_MOB_EFFECT_LIST_ID_METHOD.invoke((Object)null, nmsMobEffectList);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return -1;
      }
   }

   public static Object getMobEffectListById(int effectID) {
      try {
         return GET_MOB_EFFECT_LIST_BY_ID_METHOD.invoke((Object)null, effectID);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static int getNMSItemId(Object nmsItem) {
      try {
         return (Integer)GET_ITEM_ID_METHOD.invoke((Object)null, nmsItem);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return -1;
      }
   }

   public static Object getNMSItemById(int id) {
      try {
         return GET_ITEM_BY_ID_METHOD.invoke((Object)null, id);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Object createNMSItemStack(Object nmsItem, int count) {
      try {
         return NMS_ITEM_STACK_CONSTRUCTOR.newInstance(nmsItem, count);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static ItemStack decodeBukkitItemStack(org.bukkit.inventory.ItemStack in) {
      Object buffer = PooledByteBufAllocator.DEFAULT.buffer();
      Object packetDataSerializer = createPacketDataSerializer(buffer);
      Object nmsItemStack = toNMSItemStack(in);
      writeNMSItemStackPacketDataSerializer(packetDataSerializer, nmsItemStack);
      PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
      ItemStack stack = wrapper.readItemStack();
      ByteBufHelper.release(buffer);
      return stack;
   }

   public static org.bukkit.inventory.ItemStack encodeBukkitItemStack(ItemStack in) {
      Object buffer = PooledByteBufAllocator.DEFAULT.buffer();
      PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
      wrapper.writeItemStack(in);
      Object packetDataSerializer = createPacketDataSerializer(wrapper.getBuffer());
      Object nmsItemStack = readNMSItemStackPacketDataSerializer(packetDataSerializer);
      org.bukkit.inventory.ItemStack stack = toBukkitItemStack(nmsItemStack);
      ByteBufHelper.release(buffer);
      return stack;
   }

   public static int getBlockDataCombinedId(MaterialData materialData) {
      if (PacketEvents.getAPI().getServerManager().getVersion() == ServerVersion.V_1_7_10) {
         throw new IllegalStateException("This operation is not supported yet on 1.7.10!");
      } else {
         int combinedID;
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            combinedID = -1;
         } else {
            combinedID = materialData.getItemType().getId() << 4 | materialData.getData();
         }

         return combinedID;
      }
   }

   public static MaterialData getBlockDataByCombinedId(int combinedID) {
      if (PacketEvents.getAPI().getServerManager().getVersion() == ServerVersion.V_1_7_10) {
         throw new IllegalStateException("This operation is not supported yet on 1.7.10!");
      } else {
         return null;
      }
   }

   public static Object createNMSItemStack(int itemID, int count) {
      try {
         Object nmsItem = getNMSItemById(itemID);
         return NMS_ITEM_STACK_CONSTRUCTOR.newInstance(nmsItem, count);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Object createPacketDataSerializer(Object byteBuf) {
      try {
         return NMS_PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(byteBuf);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static org.bukkit.inventory.ItemStack toBukkitItemStack(Object nmsItemStack) {
      try {
         return (org.bukkit.inventory.ItemStack)CRAFT_ITEM_STACK_AS_BUKKIT_COPY.invoke((Object)null, nmsItemStack);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Object toNMSItemStack(org.bukkit.inventory.ItemStack itemStack) {
      try {
         return CRAFT_ITEM_STACK_AS_NMS_COPY.invoke((Object)null, itemStack);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Object readNMSItemStackPacketDataSerializer(Object packetDataSerializer) {
      try {
         return READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD.invoke(packetDataSerializer);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Object writeNMSItemStackPacketDataSerializer(Object packetDataSerializer, Object nmsItemStack) {
      try {
         return WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD.invoke(packetDataSerializer, nmsItemStack);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static NBTCompound fromMinecraftNBT(Object nbtCompound) {
      byte[] bytes;
      try {
         ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

         try {
            DataOutputStream stream = new DataOutputStream(byteStream);

            try {
               writeNmsNbtToStream(nbtCompound, stream);
               bytes = byteStream.toByteArray();
            } catch (Throwable e) {
               try {
                  stream.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }

               throw e;
            }

            stream.close();
         } catch (Throwable e) {
            try {
               byteStream.close();
            } catch (Throwable suppressed) {
               e.addSuppressed(suppressed);
            }

            throw e;
         }

         byteStream.close();
      } catch (IOException e) {
         e.printStackTrace();
         return null;
      }

      Object buffer = UnpooledByteBufAllocationHelper.wrappedBuffer(bytes);
      PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
      NBTCompound nbt = wrapper.readNBT();
      ByteBufHelper.release(buffer);
      return nbt;
   }

   public static Object toMinecraftNBT(NBTCompound nbtCompound) {
      Object buffer = UnpooledByteBufAllocationHelper.buffer();
      PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
      wrapper.writeNBT(nbtCompound);
      byte[] bytes = ByteBufHelper.copyBytes(buffer);
      ByteBufHelper.release(buffer);

      try {
         ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);

         Object var6;
         try {
            DataInputStream stream = new DataInputStream(byteStream);

            try {
               var6 = readNmsNbtFromStream(stream);
            } catch (Throwable e) {
               try {
                  stream.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }

               throw e;
            }

            stream.close();
         } catch (Throwable e) {
            try {
               byteStream.close();
            } catch (Throwable suppressed) {
               e.addSuppressed(suppressed);
            }

            throw e;
         }

         byteStream.close();
         return var6;
      } catch (IOException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static void writeNmsNbtToStream(Object compound, DataOutput out) {
      try {
         WRITE_NBT_TO_STREAM_METHOD.invoke((Object)null, compound, out);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
      }

   }

   public static Object readNmsNbtFromStream(DataInputStream in) {
      try {
         return READ_NBT_FROM_STREAM_METHOD.invoke((Object)null, in);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   private static Entity getEntityByIdWithWorldUnsafe(World world, int id) {
      if (world == null) {
         return null;
      } else {
         Object craftWorld = CRAFT_WORLD_CLASS.cast(world);

         try {
            Object worldServer = GET_CRAFT_WORLD_HANDLE_METHOD.invoke(craftWorld);
            Object nmsEntity = GET_ENTITY_BY_ID_METHOD.invoke(worldServer, id);
            return nmsEntity == null ? null : getBukkitEntity(nmsEntity);
         } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
         }
      }
   }

   @Nullable
   private static Entity getEntityByIdUnsafe(World origin, int id) {
      Entity e = getEntityByIdWithWorldUnsafe(origin, id);
      if (e != null) {
         return e;
      } else {
         Iterator var3 = Bukkit.getWorlds().iterator();

         World world;
         while(var3.hasNext()) {
            world = (World)var3.next();
            Entity entity = getEntityByIdWithWorldUnsafe(world, id);
            if (entity != null) {
               return entity;
            }
         }

         var3 = Bukkit.getWorlds().iterator();

         while(var3.hasNext()) {
            world = (World)var3.next();

            try {
               Iterator var8 = world.getEntities().iterator();

               while(var8.hasNext()) {
                  Entity entity = (Entity)var8.next();
                  if (entity.getEntityId() == id) {
                     return entity;
                  }
               }
            } catch (ConcurrentModificationException ignored) {
               return null;
            }
         }

         return null;
      }
   }

   @Nullable
   public static Entity getEntityById(@Nullable World world, int entityID) {
      Entity e = (Entity)ENTITY_ID_CACHE.get(entityID);
      if (e != null) {
         return e;
      } else if (!V_1_17_OR_HIGHER) {
         return getEntityByIdUnsafe(world, entityID);
      } else {
         Iterator var3;
         try {
            if (world != null) {
               var3 = getEntityList(world).iterator();

               while(var3.hasNext()) {
                  Entity entity = (Entity)var3.next();
                  if (entity.getEntityId() == entityID) {
                     ENTITY_ID_CACHE.putIfAbsent(entity.getEntityId(), entity);
                     return entity;
                  }
               }
            }
         } catch (Exception ex) {
            System.out.println("Failed to find entity by id on 1.19.3!");
            throw ex;
         }

         try {
            var3 = Bukkit.getWorlds().iterator();

            while(var3.hasNext()) {
               World w = (World)var3.next();
               Iterator var5 = getEntityList(w).iterator();

               while(var5.hasNext()) {
                  Entity entity = (Entity)var5.next();
                  if (entity.getEntityId() == entityID) {
                     ENTITY_ID_CACHE.putIfAbsent(entity.getEntityId(), entity);
                     return entity;
                  }
               }
            }

            return null;
         } catch (Exception ex) {
            return null;
         }
      }
   }

   @Nullable
   public static Entity getEntityById(int entityID) {
      return getEntityById((World)null, entityID);
   }

   public static List<Entity> getEntityList(World world) {
      if (V_1_17_OR_HIGHER) {
         Object worldServer = convertBukkitWorldToWorldServer(world);
         ReflectionObject wrappedWorldServer = new ReflectionObject(worldServer);
         Object persistentEntitySectionManager = wrappedWorldServer.readObject(0, PERSISTENT_ENTITY_SECTION_MANAGER_CLASS);
         ReflectionObject wrappedPersistentEntitySectionManager = new ReflectionObject(persistentEntitySectionManager);
         Object levelEntityGetter = wrappedPersistentEntitySectionManager.readObject(0, LEVEL_ENTITY_GETTER_CLASS);
         Iterable nmsEntitiesIterable = null;

         try {
            nmsEntitiesIterable = (Iterable)GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD.invoke(levelEntityGetter);
         } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
         }

         List<Entity> entityList = new ArrayList();
         if (nmsEntitiesIterable != null) {
            Iterator var8 = nmsEntitiesIterable.iterator();

            while(var8.hasNext()) {
               Object nmsEntity = var8.next();
               Entity bukkitEntity = getBukkitEntity(nmsEntity);
               entityList.add(bukkitEntity);
            }
         }

         return entityList;
      } else {
         return world.getEntities();
      }
   }

   public static ParticleType toPacketEventsParticle(Enum<?> particle) {
      try {
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            BiMap<?, ?> map = (BiMap)CRAFT_PARTICLE_PARTICLES_FIELD.get((Object)null);
            if (particle.name().equals("BLOCK_DUST")) {
               particle = Enum.valueOf(particle.getClass(), "BLOCK_CRACK");
            }

            Object minecraftKey = map.get(particle);
            return ParticleTypes.getByName(minecraftKey.toString());
         } else {
            Object nmsParticle = BUKKIT_PARTICLE_TO_NMS_ENUM_PARTICLE.invoke((Object)null, particle);
            String key = (String)LEGACY_NMS_PARTICLE_KEY_FIELD.get(nmsParticle);
            Object minecraftKey = NMS_MINECRAFT_KEY_CONSTRUCTOR.newInstance("minecraft", key);
            return ParticleTypes.getByName(minecraftKey.toString());
         }
      } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Enum<?> fromPacketEventsParticle(ParticleType particle) {
      try {
         Object enumParticle;
         Object bukkitParticle;
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            BiMap<?, ?> map = (BiMap)CRAFT_PARTICLE_PARTICLES_FIELD.get((Object)null);
            enumParticle = NMS_MINECRAFT_KEY_CONSTRUCTOR.newInstance(particle.getName().getNamespace(), particle.getName().getKey());
            bukkitParticle = map.inverse().get(enumParticle);
            return (Enum)bukkitParticle;
         } else {
            Map<String, ?> keyToParticleMap = (Map)LEGACY_NMS_KEY_TO_NMS_PARTICLE.get((Object)null);
            enumParticle = keyToParticleMap.get(particle.getName().getKey());
            bukkitParticle = NMS_ENUM_PARTICLE_TO_BUKKIT_PARTICLE.invoke((Object)null, enumParticle);
            return (Enum)bukkitParticle;
         }
      } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   static {
      LEGACY_NMS_PACKAGE = "net.minecraft.server." + MODIFIED_PACKAGE_NAME + ".";
      OBC_PACKAGE = "org.bukkit.craftbukkit." + MODIFIED_PACKAGE_NAME + ".";
      ENTITY_ID_CACHE = (new MapMaker()).weakValues().makeMap();
   }
}
