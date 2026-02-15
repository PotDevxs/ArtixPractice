package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.PacketSide;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_12;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_12_1;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_13;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_14;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_14_4;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_15;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_15_2;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_16;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_16_2;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_17;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_18;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_1;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_3;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_4;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_2;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_3;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_7_10;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_8;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_9;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_9_3;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_2;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_3;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_12;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_12_1;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_13;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_14;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_15_2;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_16;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_16_2;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_17;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_1;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_3;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_4;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_2;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_3;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_7_10;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_8;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_9;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.VersionMapper;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public final class PacketType {
   private static boolean PREPARED = false;
   private static final VersionMapper CLIENTBOUND_PLAY_VERSION_MAPPER;
   private static final VersionMapper SERVERBOUND_PLAY_VERSION_MAPPER;
   private static final VersionMapper CLIENTBOUND_CONFIG_VERSION_MAPPER;

   public static void prepare() {
      PacketType.Play.Client.load();
      PacketType.Play.Server.load();
      PacketType.Configuration.Server.load();
      PREPARED = true;
   }

   public static boolean isPrepared() {
      return PREPARED;
   }

   public static PacketTypeCommon getById(PacketSide side, ConnectionState state, ClientVersion version, int packetID) {
      switch(state) {
      case HANDSHAKING:
         if (side == PacketSide.CLIENT) {
            return PacketType.Handshaking.Client.getById(packetID);
         }

         return PacketType.Handshaking.Server.getById(packetID);
      case STATUS:
         if (side == PacketSide.CLIENT) {
            return PacketType.Status.Client.getById(packetID);
         }

         return PacketType.Status.Server.getById(packetID);
      case LOGIN:
         if (side == PacketSide.CLIENT) {
            return PacketType.Login.Client.getById(packetID);
         }

         return PacketType.Login.Server.getById(packetID);
      case PLAY:
         if (side == PacketSide.CLIENT) {
            return PacketType.Play.Client.getById(version, packetID);
         }

         return PacketType.Play.Server.getById(version, packetID);
      case CONFIGURATION:
         if (side == PacketSide.CLIENT) {
            return PacketType.Configuration.Client.getById(packetID);
         }

         return PacketType.Configuration.Server.getById(version, packetID);
      default:
         return null;
      }
   }

   static {
      CLIENTBOUND_PLAY_VERSION_MAPPER = new VersionMapper(new ClientVersion[]{ClientVersion.V_1_7_10, ClientVersion.V_1_8, ClientVersion.V_1_9, ClientVersion.V_1_10, ClientVersion.V_1_12, ClientVersion.V_1_12_1, ClientVersion.V_1_13, ClientVersion.V_1_14, ClientVersion.V_1_14_4, ClientVersion.V_1_15, ClientVersion.V_1_15_2, ClientVersion.V_1_16, ClientVersion.V_1_16_2, ClientVersion.V_1_17, ClientVersion.V_1_18, ClientVersion.V_1_19, ClientVersion.V_1_19_1, ClientVersion.V_1_19_3, ClientVersion.V_1_19_4, ClientVersion.V_1_20_2, ClientVersion.V_1_20_3});
      SERVERBOUND_PLAY_VERSION_MAPPER = new VersionMapper(new ClientVersion[]{ClientVersion.V_1_7_10, ClientVersion.V_1_8, ClientVersion.V_1_9, ClientVersion.V_1_12, ClientVersion.V_1_12_1, ClientVersion.V_1_13, ClientVersion.V_1_14, ClientVersion.V_1_15_2, ClientVersion.V_1_16, ClientVersion.V_1_16_2, ClientVersion.V_1_17, ClientVersion.V_1_19, ClientVersion.V_1_19_1, ClientVersion.V_1_19_3, ClientVersion.V_1_19_4, ClientVersion.V_1_20_2, ClientVersion.V_1_20_3});
      CLIENTBOUND_CONFIG_VERSION_MAPPER = new VersionMapper(new ClientVersion[]{ClientVersion.V_1_20_2, ClientVersion.V_1_20_3});
   }

   public static class Play {
      public static enum Server implements ClientBoundPacket, PacketTypeCommon {
         SET_COMPRESSION,
         MAP_CHUNK_BULK,
         UPDATE_ENTITY_NBT,
         UPDATE_SIGN,
         USE_BED,
         SPAWN_WEATHER_ENTITY,
         TITLE,
         WORLD_BORDER,
         COMBAT_EVENT,
         ENTITY_MOVEMENT,
         SPAWN_LIVING_ENTITY,
         SPAWN_PAINTING,
         SCULK_VIBRATION_SIGNAL,
         ACKNOWLEDGE_PLAYER_DIGGING,
         CHAT_PREVIEW_PACKET,
         NAMED_SOUND_EFFECT,
         PLAYER_CHAT_HEADER,
         PLAYER_INFO,
         DISPLAY_CHAT_PREVIEW,
         UPDATE_ENABLED_FEATURES,
         SPAWN_PLAYER,
         WINDOW_CONFIRMATION,
         SPAWN_ENTITY,
         SPAWN_EXPERIENCE_ORB,
         ENTITY_ANIMATION,
         STATISTICS,
         BLOCK_BREAK_ANIMATION,
         BLOCK_ENTITY_DATA,
         BLOCK_ACTION,
         BLOCK_CHANGE,
         BOSS_BAR,
         SERVER_DIFFICULTY,
         CLEAR_TITLES,
         TAB_COMPLETE,
         MULTI_BLOCK_CHANGE,
         DECLARE_COMMANDS,
         CLOSE_WINDOW,
         WINDOW_ITEMS,
         WINDOW_PROPERTY,
         SET_SLOT,
         SET_COOLDOWN,
         PLUGIN_MESSAGE,
         DISCONNECT,
         ENTITY_STATUS,
         EXPLOSION,
         UNLOAD_CHUNK,
         CHANGE_GAME_STATE,
         OPEN_HORSE_WINDOW,
         INITIALIZE_WORLD_BORDER,
         KEEP_ALIVE,
         CHUNK_DATA,
         EFFECT,
         PARTICLE,
         UPDATE_LIGHT,
         JOIN_GAME,
         MAP_DATA,
         MERCHANT_OFFERS,
         ENTITY_RELATIVE_MOVE,
         ENTITY_RELATIVE_MOVE_AND_ROTATION,
         ENTITY_ROTATION,
         VEHICLE_MOVE,
         OPEN_BOOK,
         OPEN_WINDOW,
         OPEN_SIGN_EDITOR,
         PING,
         CRAFT_RECIPE_RESPONSE,
         PLAYER_ABILITIES,
         END_COMBAT_EVENT,
         ENTER_COMBAT_EVENT,
         DEATH_COMBAT_EVENT,
         FACE_PLAYER,
         PLAYER_POSITION_AND_LOOK,
         UNLOCK_RECIPES,
         DESTROY_ENTITIES,
         REMOVE_ENTITY_EFFECT,
         RESOURCE_PACK_SEND,
         RESPAWN,
         ENTITY_HEAD_LOOK,
         SELECT_ADVANCEMENTS_TAB,
         ACTION_BAR,
         WORLD_BORDER_CENTER,
         WORLD_BORDER_LERP_SIZE,
         WORLD_BORDER_SIZE,
         WORLD_BORDER_WARNING_DELAY,
         WORLD_BORDER_WARNING_REACH,
         CAMERA,
         HELD_ITEM_CHANGE,
         UPDATE_VIEW_POSITION,
         UPDATE_VIEW_DISTANCE,
         SPAWN_POSITION,
         DISPLAY_SCOREBOARD,
         ENTITY_METADATA,
         ATTACH_ENTITY,
         ENTITY_VELOCITY,
         ENTITY_EQUIPMENT,
         SET_EXPERIENCE,
         UPDATE_HEALTH,
         SCOREBOARD_OBJECTIVE,
         SET_PASSENGERS,
         TEAMS,
         UPDATE_SCORE,
         UPDATE_SIMULATION_DISTANCE,
         SET_TITLE_SUBTITLE,
         TIME_UPDATE,
         SET_TITLE_TEXT,
         SET_TITLE_TIMES,
         ENTITY_SOUND_EFFECT,
         SOUND_EFFECT,
         STOP_SOUND,
         PLAYER_LIST_HEADER_AND_FOOTER,
         NBT_QUERY_RESPONSE,
         COLLECT_ITEM,
         ENTITY_TELEPORT,
         UPDATE_ADVANCEMENTS,
         UPDATE_ATTRIBUTES,
         ENTITY_EFFECT,
         DECLARE_RECIPES,
         TAGS,
         CHAT_MESSAGE,
         ACKNOWLEDGE_BLOCK_CHANGES,
         SERVER_DATA,
         SYSTEM_CHAT_MESSAGE,
         DELETE_CHAT,
         CUSTOM_CHAT_COMPLETIONS,
         DISGUISED_CHAT,
         PLAYER_INFO_REMOVE,
         PLAYER_INFO_UPDATE,
         DAMAGE_EVENT,
         HURT_ANIMATION,
         BUNDLE,
         CHUNK_BIOMES,
         CHUNK_BATCH_END,
         CHUNK_BATCH_BEGIN,
         DEBUG_PONG,
         CONFIGURATION_START,
         RESET_SCORE,
         RESOURCE_PACK_REMOVE,
         TICKING_STATE,
         TICKING_STEP;

         private static int INDEX = 0;
         private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap();
         private final int[] ids;

         private Server() {
            this.ids = new int[PacketType.CLIENTBOUND_PLAY_VERSION_MAPPER.getVersions().length];
            Arrays.fill(this.ids, -1);
         }

         public int getId(ClientVersion version) {
            if (!PacketType.PREPARED) {
               PacketType.prepare();
            }

            int index = PacketType.CLIENTBOUND_PLAY_VERSION_MAPPER.getIndex(version);
            return this.ids[index];
         }

         @Nullable
         public static PacketTypeCommon getById(ClientVersion version, int packetId) {
            if (!PacketType.PREPARED) {
               PacketType.prepare();
            }

            int index = PacketType.CLIENTBOUND_PLAY_VERSION_MAPPER.getIndex(version);
            Map<Integer, PacketTypeCommon> map = (Map)PACKET_TYPE_ID_MAP.get((byte)index);
            return (PacketTypeCommon)map.get(packetId);
         }

         public PacketSide getSide() {
            return PacketSide.SERVER;
         }

         private static void loadPacketIds(Enum<?>[] enumConstants) {
            int index = INDEX;
            Enum[] var2 = enumConstants;
            int var3 = enumConstants.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Enum<?> constant = var2[var4];
               int id = constant.ordinal();
               PacketType.Play.Server value = valueOf(constant.name());
               value.ids[index] = id;
               Map<Integer, PacketTypeCommon> packetIdMap = (Map)PACKET_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
                  return new HashMap();
               });
               packetIdMap.put(id, value);
            }

            ++INDEX;
         }

         public static void load() {
            INDEX = 0;
            loadPacketIds(ClientboundPacketType_1_7_10.values());
            loadPacketIds(ClientboundPacketType_1_8.values());
            loadPacketIds(ClientboundPacketType_1_9.values());
            loadPacketIds(ClientboundPacketType_1_9_3.values());
            loadPacketIds(ClientboundPacketType_1_12.values());
            loadPacketIds(ClientboundPacketType_1_12_1.values());
            loadPacketIds(ClientboundPacketType_1_13.values());
            loadPacketIds(ClientboundPacketType_1_14.values());
            loadPacketIds(ClientboundPacketType_1_14_4.values());
            loadPacketIds(ClientboundPacketType_1_15.values());
            loadPacketIds(ClientboundPacketType_1_15_2.values());
            loadPacketIds(ClientboundPacketType_1_16.values());
            loadPacketIds(ClientboundPacketType_1_16_2.values());
            loadPacketIds(ClientboundPacketType_1_17.values());
            loadPacketIds(ClientboundPacketType_1_18.values());
            loadPacketIds(ClientboundPacketType_1_19.values());
            loadPacketIds(ClientboundPacketType_1_19_1.values());
            loadPacketIds(ClientboundPacketType_1_19_3.values());
            loadPacketIds(ClientboundPacketType_1_19_4.values());
            loadPacketIds(ClientboundPacketType_1_20_2.values());
            loadPacketIds(ClientboundPacketType_1_20_3.values());
         }

         // $FF: synthetic method
         private static PacketType.Play.Server[] $values() {
            return new PacketType.Play.Server[]{SET_COMPRESSION, MAP_CHUNK_BULK, UPDATE_ENTITY_NBT, UPDATE_SIGN, USE_BED, SPAWN_WEATHER_ENTITY, TITLE, WORLD_BORDER, COMBAT_EVENT, ENTITY_MOVEMENT, SPAWN_LIVING_ENTITY, SPAWN_PAINTING, SCULK_VIBRATION_SIGNAL, ACKNOWLEDGE_PLAYER_DIGGING, CHAT_PREVIEW_PACKET, NAMED_SOUND_EFFECT, PLAYER_CHAT_HEADER, PLAYER_INFO, DISPLAY_CHAT_PREVIEW, UPDATE_ENABLED_FEATURES, SPAWN_PLAYER, WINDOW_CONFIRMATION, SPAWN_ENTITY, SPAWN_EXPERIENCE_ORB, ENTITY_ANIMATION, STATISTICS, BLOCK_BREAK_ANIMATION, BLOCK_ENTITY_DATA, BLOCK_ACTION, BLOCK_CHANGE, BOSS_BAR, SERVER_DIFFICULTY, CLEAR_TITLES, TAB_COMPLETE, MULTI_BLOCK_CHANGE, DECLARE_COMMANDS, CLOSE_WINDOW, WINDOW_ITEMS, WINDOW_PROPERTY, SET_SLOT, SET_COOLDOWN, PLUGIN_MESSAGE, DISCONNECT, ENTITY_STATUS, EXPLOSION, UNLOAD_CHUNK, CHANGE_GAME_STATE, OPEN_HORSE_WINDOW, INITIALIZE_WORLD_BORDER, KEEP_ALIVE, CHUNK_DATA, EFFECT, PARTICLE, UPDATE_LIGHT, JOIN_GAME, MAP_DATA, MERCHANT_OFFERS, ENTITY_RELATIVE_MOVE, ENTITY_RELATIVE_MOVE_AND_ROTATION, ENTITY_ROTATION, VEHICLE_MOVE, OPEN_BOOK, OPEN_WINDOW, OPEN_SIGN_EDITOR, PING, CRAFT_RECIPE_RESPONSE, PLAYER_ABILITIES, END_COMBAT_EVENT, ENTER_COMBAT_EVENT, DEATH_COMBAT_EVENT, FACE_PLAYER, PLAYER_POSITION_AND_LOOK, UNLOCK_RECIPES, DESTROY_ENTITIES, REMOVE_ENTITY_EFFECT, RESOURCE_PACK_SEND, RESPAWN, ENTITY_HEAD_LOOK, SELECT_ADVANCEMENTS_TAB, ACTION_BAR, WORLD_BORDER_CENTER, WORLD_BORDER_LERP_SIZE, WORLD_BORDER_SIZE, WORLD_BORDER_WARNING_DELAY, WORLD_BORDER_WARNING_REACH, CAMERA, HELD_ITEM_CHANGE, UPDATE_VIEW_POSITION, UPDATE_VIEW_DISTANCE, SPAWN_POSITION, DISPLAY_SCOREBOARD, ENTITY_METADATA, ATTACH_ENTITY, ENTITY_VELOCITY, ENTITY_EQUIPMENT, SET_EXPERIENCE, UPDATE_HEALTH, SCOREBOARD_OBJECTIVE, SET_PASSENGERS, TEAMS, UPDATE_SCORE, UPDATE_SIMULATION_DISTANCE, SET_TITLE_SUBTITLE, TIME_UPDATE, SET_TITLE_TEXT, SET_TITLE_TIMES, ENTITY_SOUND_EFFECT, SOUND_EFFECT, STOP_SOUND, PLAYER_LIST_HEADER_AND_FOOTER, NBT_QUERY_RESPONSE, COLLECT_ITEM, ENTITY_TELEPORT, UPDATE_ADVANCEMENTS, UPDATE_ATTRIBUTES, ENTITY_EFFECT, DECLARE_RECIPES, TAGS, CHAT_MESSAGE, ACKNOWLEDGE_BLOCK_CHANGES, SERVER_DATA, SYSTEM_CHAT_MESSAGE, DELETE_CHAT, CUSTOM_CHAT_COMPLETIONS, DISGUISED_CHAT, PLAYER_INFO_REMOVE, PLAYER_INFO_UPDATE, DAMAGE_EVENT, HURT_ANIMATION, BUNDLE, CHUNK_BIOMES, CHUNK_BATCH_END, CHUNK_BATCH_BEGIN, DEBUG_PONG, CONFIGURATION_START, RESET_SCORE, RESOURCE_PACK_REMOVE, TICKING_STATE, TICKING_STEP};
         }
      }

      public static enum Client implements PacketTypeCommon, ServerBoundPacket {
         CHAT_PREVIEW,
         TELEPORT_CONFIRM,
         QUERY_BLOCK_NBT,
         SET_DIFFICULTY,
         CHAT_MESSAGE,
         CLIENT_STATUS,
         CLIENT_SETTINGS,
         TAB_COMPLETE,
         WINDOW_CONFIRMATION,
         CLICK_WINDOW_BUTTON,
         CLICK_WINDOW,
         CLOSE_WINDOW,
         PLUGIN_MESSAGE,
         EDIT_BOOK,
         QUERY_ENTITY_NBT,
         INTERACT_ENTITY,
         GENERATE_STRUCTURE,
         KEEP_ALIVE,
         LOCK_DIFFICULTY,
         PLAYER_POSITION,
         PLAYER_POSITION_AND_ROTATION,
         PLAYER_ROTATION,
         PLAYER_FLYING,
         VEHICLE_MOVE,
         STEER_BOAT,
         PICK_ITEM,
         CRAFT_RECIPE_REQUEST,
         PLAYER_ABILITIES,
         PLAYER_DIGGING,
         ENTITY_ACTION,
         STEER_VEHICLE,
         PONG,
         RECIPE_BOOK_DATA,
         SET_DISPLAYED_RECIPE,
         SET_RECIPE_BOOK_STATE,
         NAME_ITEM,
         RESOURCE_PACK_STATUS,
         ADVANCEMENT_TAB,
         SELECT_TRADE,
         SET_BEACON_EFFECT,
         HELD_ITEM_CHANGE,
         UPDATE_COMMAND_BLOCK,
         UPDATE_COMMAND_BLOCK_MINECART,
         CREATIVE_INVENTORY_ACTION,
         UPDATE_JIGSAW_BLOCK,
         UPDATE_STRUCTURE_BLOCK,
         UPDATE_SIGN,
         ANIMATION,
         SPECTATE,
         PLAYER_BLOCK_PLACEMENT,
         USE_ITEM,
         CHAT_COMMAND,
         CHAT_ACK,
         CHAT_SESSION_UPDATE,
         CHUNK_BATCH_ACK,
         CONFIGURATION_ACK,
         DEBUG_PING,
         SLOT_STATE_CHANGE;

         private static int INDEX = 0;
         private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap();
         private final int[] ids;

         private Client() {
            this.ids = new int[PacketType.SERVERBOUND_PLAY_VERSION_MAPPER.getVersions().length];
            Arrays.fill(this.ids, -1);
         }

         @Nullable
         public static PacketTypeCommon getById(ClientVersion version, int packetId) {
            if (!PacketType.PREPARED) {
               PacketType.prepare();
            }

            int index = PacketType.SERVERBOUND_PLAY_VERSION_MAPPER.getIndex(version);
            Map<Integer, PacketTypeCommon> packetIdMap = (Map)PACKET_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
               return new HashMap();
            });
            return (PacketTypeCommon)packetIdMap.get(packetId);
         }

         private static void loadPacketIds(Enum<?>[] enumConstants) {
            int index = INDEX;
            Enum[] var2 = enumConstants;
            int var3 = enumConstants.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Enum<?> constant = var2[var4];
               int id = constant.ordinal();
               PacketType.Play.Client value = valueOf(constant.name());
               value.ids[index] = id;
               Map<Integer, PacketTypeCommon> packetIdMap = (Map)PACKET_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
                  return new HashMap();
               });
               packetIdMap.put(id, value);
            }

            ++INDEX;
         }

         public static void load() {
            INDEX = 0;
            loadPacketIds(ServerboundPacketType_1_7_10.values());
            loadPacketIds(ServerboundPacketType_1_8.values());
            loadPacketIds(ServerboundPacketType_1_9.values());
            loadPacketIds(ServerboundPacketType_1_12.values());
            loadPacketIds(ServerboundPacketType_1_12_1.values());
            loadPacketIds(ServerboundPacketType_1_13.values());
            loadPacketIds(ServerboundPacketType_1_14.values());
            loadPacketIds(ServerboundPacketType_1_15_2.values());
            loadPacketIds(ServerboundPacketType_1_16.values());
            loadPacketIds(ServerboundPacketType_1_16_2.values());
            loadPacketIds(ServerboundPacketType_1_17.values());
            loadPacketIds(ServerboundPacketType_1_19.values());
            loadPacketIds(ServerboundPacketType_1_19_1.values());
            loadPacketIds(ServerboundPacketType_1_19_3.values());
            loadPacketIds(ServerboundPacketType_1_19_4.values());
            loadPacketIds(ServerboundPacketType_1_20_2.values());
            loadPacketIds(ServerboundPacketType_1_20_3.values());
         }

         public int getId(ClientVersion version) {
            if (!PacketType.PREPARED) {
               PacketType.prepare();
            }

            int index = PacketType.SERVERBOUND_PLAY_VERSION_MAPPER.getIndex(version);
            return this.ids[index];
         }

         public PacketSide getSide() {
            return PacketSide.CLIENT;
         }

         // $FF: synthetic method
         private static PacketType.Play.Client[] $values() {
            return new PacketType.Play.Client[]{CHAT_PREVIEW, TELEPORT_CONFIRM, QUERY_BLOCK_NBT, SET_DIFFICULTY, CHAT_MESSAGE, CLIENT_STATUS, CLIENT_SETTINGS, TAB_COMPLETE, WINDOW_CONFIRMATION, CLICK_WINDOW_BUTTON, CLICK_WINDOW, CLOSE_WINDOW, PLUGIN_MESSAGE, EDIT_BOOK, QUERY_ENTITY_NBT, INTERACT_ENTITY, GENERATE_STRUCTURE, KEEP_ALIVE, LOCK_DIFFICULTY, PLAYER_POSITION, PLAYER_POSITION_AND_ROTATION, PLAYER_ROTATION, PLAYER_FLYING, VEHICLE_MOVE, STEER_BOAT, PICK_ITEM, CRAFT_RECIPE_REQUEST, PLAYER_ABILITIES, PLAYER_DIGGING, ENTITY_ACTION, STEER_VEHICLE, PONG, RECIPE_BOOK_DATA, SET_DISPLAYED_RECIPE, SET_RECIPE_BOOK_STATE, NAME_ITEM, RESOURCE_PACK_STATUS, ADVANCEMENT_TAB, SELECT_TRADE, SET_BEACON_EFFECT, HELD_ITEM_CHANGE, UPDATE_COMMAND_BLOCK, UPDATE_COMMAND_BLOCK_MINECART, CREATIVE_INVENTORY_ACTION, UPDATE_JIGSAW_BLOCK, UPDATE_STRUCTURE_BLOCK, UPDATE_SIGN, ANIMATION, SPECTATE, PLAYER_BLOCK_PLACEMENT, USE_ITEM, CHAT_COMMAND, CHAT_ACK, CHAT_SESSION_UPDATE, CHUNK_BATCH_ACK, CONFIGURATION_ACK, DEBUG_PING, SLOT_STATE_CHANGE};
         }
      }
   }

   public static class Configuration {
      public static enum Server implements ClientBoundPacket, PacketTypeCommon {
         PLUGIN_MESSAGE,
         DISCONNECT,
         CONFIGURATION_END,
         KEEP_ALIVE,
         PING,
         REGISTRY_DATA,
         RESOURCE_PACK_SEND,
         UPDATE_ENABLED_FEATURES,
         UPDATE_TAGS,
         RESOURCE_PACK_REMOVE;

         private static int INDEX = 0;
         private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap();
         private final int[] ids;

         private Server() {
            this.ids = new int[PacketType.CLIENTBOUND_CONFIG_VERSION_MAPPER.getVersions().length];
            Arrays.fill(this.ids, -1);
         }

         public static void load() {
            INDEX = 0;
            loadPacketIds(ClientboundConfigPacketType_1_20_2.values());
            loadPacketIds(ClientboundConfigPacketType_1_20_3.values());
         }

         private static void loadPacketIds(Enum<?>[] enumConstants) {
            int index = INDEX;
            Enum[] var2 = enumConstants;
            int var3 = enumConstants.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Enum<?> constant = var2[var4];
               int id = constant.ordinal();
               PacketType.Configuration.Server value = valueOf(constant.name());
               value.ids[index] = id;
               Map<Integer, PacketTypeCommon> packetIdMap = (Map)PACKET_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
                  return new HashMap();
               });
               packetIdMap.put(id, value);
            }

            ++INDEX;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetId) {
            return getById(ClientVersion.getLatest(), packetId);
         }

         @Nullable
         public static PacketTypeCommon getById(ClientVersion version, int packetId) {
            if (!PacketType.PREPARED) {
               PacketType.prepare();
            }

            int index = PacketType.CLIENTBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
            Map<Integer, PacketTypeCommon> map = (Map)PACKET_TYPE_ID_MAP.get((byte)index);
            return (PacketTypeCommon)map.get(packetId);
         }

         /** @deprecated */
         @Deprecated
         public int getId() {
            return this.getId(ClientVersion.getLatest());
         }

         public int getId(ClientVersion version) {
            if (!PacketType.PREPARED) {
               PacketType.prepare();
            }

            int index = PacketType.CLIENTBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
            return this.ids[index];
         }

         public PacketSide getSide() {
            return PacketSide.SERVER;
         }

         // $FF: synthetic method
         private static PacketType.Configuration.Server[] $values() {
            return new PacketType.Configuration.Server[]{PLUGIN_MESSAGE, DISCONNECT, CONFIGURATION_END, KEEP_ALIVE, PING, REGISTRY_DATA, RESOURCE_PACK_SEND, UPDATE_ENABLED_FEATURES, UPDATE_TAGS, RESOURCE_PACK_REMOVE};
         }
      }

      public static enum Client implements PacketTypeConstant, ServerBoundPacket {
         CLIENT_SETTINGS(0),
         PLUGIN_MESSAGE(1),
         CONFIGURATION_END_ACK(2),
         KEEP_ALIVE(3),
         PONG(4),
         RESOURCE_PACK_STATUS(5);

         private final int id;

         private Client(int id) {
            this.id = id;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetId) {
            switch(packetId) {
            case 0:
               return CLIENT_SETTINGS;
            case 1:
               return PLUGIN_MESSAGE;
            case 2:
               return CONFIGURATION_END_ACK;
            case 3:
               return KEEP_ALIVE;
            case 4:
               return PONG;
            case 5:
               return RESOURCE_PACK_STATUS;
            default:
               return null;
            }
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.CLIENT;
         }

         // $FF: synthetic method
         private static PacketType.Configuration.Client[] $values() {
            return new PacketType.Configuration.Client[]{CLIENT_SETTINGS, PLUGIN_MESSAGE, CONFIGURATION_END_ACK, KEEP_ALIVE, PONG, RESOURCE_PACK_STATUS};
         }
      }
   }

   public static class Handshaking {
      public static enum Server implements ClientBoundPacket, PacketTypeConstant {
         LEGACY_SERVER_LIST_RESPONSE(254);

         private final int id;

         private Server(int id) {
            this.id = id;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetID) {
            return packetID == 254 ? LEGACY_SERVER_LIST_RESPONSE : null;
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.SERVER;
         }

         // $FF: synthetic method
         private static PacketType.Handshaking.Server[] $values() {
            return new PacketType.Handshaking.Server[]{LEGACY_SERVER_LIST_RESPONSE};
         }
      }

      public static enum Client implements PacketTypeConstant, ServerBoundPacket {
         HANDSHAKE(0),
         LEGACY_SERVER_LIST_PING(254);

         private final int id;

         private Client(int id) {
            this.id = id;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetID) {
            if (packetID == 0) {
               return HANDSHAKE;
            } else {
               return packetID == 254 ? LEGACY_SERVER_LIST_PING : null;
            }
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.CLIENT;
         }

         // $FF: synthetic method
         private static PacketType.Handshaking.Client[] $values() {
            return new PacketType.Handshaking.Client[]{HANDSHAKE, LEGACY_SERVER_LIST_PING};
         }
      }
   }

   public static class Status {
      public static enum Server implements ClientBoundPacket, PacketTypeConstant {
         RESPONSE(0),
         PONG(1);

         private final int id;

         private Server(int id) {
            this.id = id;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetID) {
            if (packetID == 0) {
               return RESPONSE;
            } else {
               return packetID == 1 ? PONG : null;
            }
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.SERVER;
         }

         // $FF: synthetic method
         private static PacketType.Status.Server[] $values() {
            return new PacketType.Status.Server[]{RESPONSE, PONG};
         }
      }

      public static enum Client implements PacketTypeConstant, ServerBoundPacket {
         REQUEST(0),
         PING(1);

         private final int id;

         private Client(int id) {
            this.id = id;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetId) {
            if (packetId == 0) {
               return REQUEST;
            } else {
               return packetId == 1 ? PING : null;
            }
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.CLIENT;
         }

         // $FF: synthetic method
         private static PacketType.Status.Client[] $values() {
            return new PacketType.Status.Client[]{REQUEST, PING};
         }
      }
   }

   public static class Login {
      public static enum Server implements ClientBoundPacket, PacketTypeConstant {
         DISCONNECT(0),
         ENCRYPTION_REQUEST(1),
         LOGIN_SUCCESS(2),
         SET_COMPRESSION(3),
         LOGIN_PLUGIN_REQUEST(4);

         private final int id;

         private Server(int id) {
            this.id = id;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetID) {
            switch(packetID) {
            case 0:
               return DISCONNECT;
            case 1:
               return ENCRYPTION_REQUEST;
            case 2:
               return LOGIN_SUCCESS;
            case 3:
               return SET_COMPRESSION;
            case 4:
               return LOGIN_PLUGIN_REQUEST;
            default:
               return null;
            }
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.SERVER;
         }

         // $FF: synthetic method
         private static PacketType.Login.Server[] $values() {
            return new PacketType.Login.Server[]{DISCONNECT, ENCRYPTION_REQUEST, LOGIN_SUCCESS, SET_COMPRESSION, LOGIN_PLUGIN_REQUEST};
         }
      }

      public static enum Client implements PacketTypeConstant, ServerBoundPacket {
         LOGIN_START(0),
         ENCRYPTION_RESPONSE(1),
         LOGIN_PLUGIN_RESPONSE(2),
         LOGIN_SUCCESS_ACK(3);

         private final int id;

         private Client(int id) {
            this.id = id;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetID) {
            if (packetID == 0) {
               return LOGIN_START;
            } else if (packetID == 1) {
               return ENCRYPTION_RESPONSE;
            } else if (packetID == 2) {
               return LOGIN_PLUGIN_RESPONSE;
            } else {
               return packetID == 3 ? LOGIN_SUCCESS_ACK : null;
            }
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.CLIENT;
         }

         // $FF: synthetic method
         private static PacketType.Login.Client[] $values() {
            return new PacketType.Login.Client[]{LOGIN_START, ENCRYPTION_RESPONSE, LOGIN_PLUGIN_RESPONSE, LOGIN_SUCCESS_ACK};
         }
      }
   }
}
