package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat;

import java.util.HashMap;
import java.util.Map;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TypesBuilder;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TypesBuilderData;

public class ChatTypes {
   private static final Map<String, ChatType> CHAT_TYPE_MAP = new HashMap();
   private static final Map<Byte, Map<Integer, ChatType>> CHAT_TYPE_ID_MAP = new HashMap();
   private static final TypesBuilder TYPES_BUILDER;
   public static final ChatType CHAT;
   public static final ChatType SAY_COMMAND;
   public static final ChatType MSG_COMMAND_INCOMING;
   public static final ChatType MSG_COMMAND_OUTGOING;
   public static final ChatType TEAM_MSG_COMMAND_INCOMING;
   public static final ChatType TEAM_MSG_COMMAND_OUTGOING;
   public static final ChatType EMOTE_COMMAND;
   public static final ChatType RAW;
   /** @deprecated */
   @Deprecated
   public static final ChatType SYSTEM;
   /** @deprecated */
   @Deprecated
   public static final ChatType GAME_INFO;
   /** @deprecated */
   @Deprecated
   public static final ChatType MSG_COMMAND;
   /** @deprecated */
   @Deprecated
   public static final ChatType TEAM_MSG_COMMAND;

   public static ChatType define(String key) {
      final TypesBuilderData data = TYPES_BUILDER.define(key);
      ChatType chatType = new ChatType() {
         private final int[] ids = data.getData();

         public ResourceLocation getName() {
            return data.getName();
         }

         public int getId(ClientVersion version) {
            int index = ChatTypes.TYPES_BUILDER.getDataIndex(version);
            return this.ids[index];
         }
      };
      CHAT_TYPE_MAP.put(chatType.getName().toString(), chatType);
      ClientVersion[] var3 = TYPES_BUILDER.getVersions();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ClientVersion version = var3[var5];
         int index = TYPES_BUILDER.getDataIndex(version);
         Map<Integer, ChatType> typeIdMap = (Map)CHAT_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
            return new HashMap();
         });
         typeIdMap.put(chatType.getId(version), chatType);
      }

      return chatType;
   }

   public static ChatType getByName(String name) {
      return (ChatType)CHAT_TYPE_MAP.get(name);
   }

   public static ChatType getById(ClientVersion version, int id) {
      int index = TYPES_BUILDER.getDataIndex(version);
      return (ChatType)((Map)CHAT_TYPE_ID_MAP.get((byte)index)).get(id);
   }

   static {
      TYPES_BUILDER = new TypesBuilder("chat/chat_type_mappings", new ClientVersion[]{ClientVersion.V_1_18_2, ClientVersion.V_1_19, ClientVersion.V_1_19_1});
      TYPES_BUILDER.unloadFileMappings();
      CHAT = define("chat");
      SAY_COMMAND = define("say_command");
      MSG_COMMAND_INCOMING = define("msg_command_incoming");
      MSG_COMMAND_OUTGOING = define("msg_command_outgoing");
      TEAM_MSG_COMMAND_INCOMING = define("team_msg_command_incoming");
      TEAM_MSG_COMMAND_OUTGOING = define("team_msg_command_outgoing");
      EMOTE_COMMAND = define("emote_command");
      RAW = define("raw");
      SYSTEM = define("system");
      GAME_INFO = define("game_info");
      MSG_COMMAND = define("msg_command");
      TEAM_MSG_COMMAND = define("team_msg_command");
   }
}
