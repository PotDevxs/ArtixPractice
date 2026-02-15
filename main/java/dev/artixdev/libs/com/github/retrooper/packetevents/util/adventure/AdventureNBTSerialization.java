package dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.nbt.api.BinaryTagHolder;
import dev.artixdev.libs.net.kyori.adventure.text.BlockNBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.ComponentBuilder;
import dev.artixdev.libs.net.kyori.adventure.text.EntityNBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.KeybindComponent;
import dev.artixdev.libs.net.kyori.adventure.text.NBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.ScoreComponent;
import dev.artixdev.libs.net.kyori.adventure.text.SelectorComponent;
import dev.artixdev.libs.net.kyori.adventure.text.StorageNBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.TextComponent;
import dev.artixdev.libs.net.kyori.adventure.text.TranslatableComponent;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;

public final class AdventureNBTSerialization {
   private static final AdventureNBTSerialization.TagType[] NBT_TYPES = AdventureNBTSerialization.TagType.values();
   private static final int DEPTH_LIMIT = 12;
   private static final String OBFUSCATED = "obfuscated";
   private static final String BOLD = "bold";
   private static final String STRIKETHROUGH = "strikethrough";
   private static final String UNDERLINED = "underlined";
   private static final String ITALIC = "italic";
   private static final String OPEN_URL = "open_url";
   private static final String RUN_COMMAND = "run_command";
   private static final String SUGGEST_COMMAND = "suggest_command";
   private static final String CHANGE_PAGE = "change_page";
   private static final String COPY_TO_CLIPBOARD = "copy_to_clipboard";
   private static final String SHOW_TEXT = "show_text";
   private static final String SHOW_ITEM = "show_item";
   private static final String SHOW_ENTITY = "show_entity";
   private static final String ITEM_ID = "id";
   private static final String ITEM_COUNT = "count";
   private static final String ITEM_TAG = "tag";
   private static final String ENTITY_TYPE = "type";
   private static final String ENTITY_ID = "id";
   private static final String ENTITY_NAME = "name";

   private AdventureNBTSerialization() {
   }

   private static AdventureNBTSerialization.TagType resolveNbtType(byte typeId) {
      if (typeId >= 0 && typeId < NBT_TYPES.length) {
         AdventureNBTSerialization.TagType nbtType = NBT_TYPES[typeId];
         if (nbtType == null) {
            throw new IllegalStateException("Invalid nbt type id read: " + typeId);
         } else {
            return nbtType;
         }
      } else {
         throw new IllegalStateException("Invalid nbt type id read: " + typeId);
      }
   }

   private static void requireType(AdventureNBTSerialization.TagType type, AdventureNBTSerialization.TagType wantedType) {
      if (type != wantedType) {
         throw new IllegalStateException("Expected nbt type " + wantedType + ", read " + type);
      }
   }

   private static void requireComponentType(AdventureNBTSerialization.TagType type) {
      if (type != AdventureNBTSerialization.TagType.STRING && type != AdventureNBTSerialization.TagType.COMPOUND) {
         throw new IllegalStateException("Expected nbt component type, read " + type);
      }
   }

   private static void requireState(boolean state) {
      if (!state) {
         throw new IllegalStateException();
      }
   }

   public static Component readComponent(Object byteBuf) throws IOException {
      return readComponent((DataInput)(new ByteBufInputStream(byteBuf)));
   }

   public static Component readComponent(DataInput input) throws IOException {
      AdventureNBTSerialization.TagType type = resolveNbtType(input.readByte());
      return readComponent(input, type);
   }

   private static Component readComponent(DataInput input, AdventureNBTSerialization.TagType rootType) throws IOException {
      return readComponent(input, rootType, 0);
   }

   private static Component readComponent(DataInput input, AdventureNBTSerialization.TagType rootType, int depth) throws IOException {
      if (depth > 12) {
         throw new RuntimeException("Depth limit reached while decoding component: " + depth + " > " + 12);
      } else if (rootType == AdventureNBTSerialization.TagType.STRING) {
         return Component.text(input.readUTF());
      } else if (rootType != AdventureNBTSerialization.TagType.COMPOUND) {
         throw new RuntimeException("Unsupported nbt tag type for component: " + rootType);
      } else {
         List<Component> extra = null;
         String text = null;
         String translate = null;
         String translateFallback = null;
         List<Component> translateWith = null;
         String scoreName = null;
         String scoreObjective = null;
         String selector = null;
         String keybind = null;
         String nbt = null;
         boolean nbtInterpret = false;
         String nbtBlock = null;
         String nbtEntity = null;
         String nbtStorage = null;
         Component separator = null;
         String font = null;
         String color = null;
         TextDecoration.State obfuscated = TextDecoration.State.NOT_SET;
         TextDecoration.State bold = TextDecoration.State.NOT_SET;
         TextDecoration.State strikethrough = TextDecoration.State.NOT_SET;
         TextDecoration.State underlined = TextDecoration.State.NOT_SET;
         TextDecoration.State italic = TextDecoration.State.NOT_SET;
         String insertion = null;
         ClickEvent.Action clickEventAction = null;
         String clickEventValue = null;
         HoverEvent.Action<?> hoverEventAction = null;
         Object hoverEventContents = null;

         AdventureNBTSerialization.TagType type;
         while((type = resolveNbtType(input.readByte())) != AdventureNBTSerialization.TagType.END) {
            String key = input.readUTF();
            byte var33 = -1;
            switch(key.hashCode()) {
            case -1884274053:
               if (key.equals("storage")) {
                  var33 = 12;
               }
               break;
            case -1771105512:
               if (key.equals("underlined")) {
                  var33 = 19;
               }
               break;
            case -1298275357:
               if (key.equals("entity")) {
                  var33 = 11;
               }
               break;
            case -1178781136:
               if (key.equals("italic")) {
                  var33 = 18;
               }
               break;
            case -972521773:
               if (key.equals("strikethrough")) {
                  var33 = 20;
               }
               break;
            case -815039716:
               if (key.equals("keybind")) {
                  var33 = 7;
               }
               break;
            case -800853518:
               if (key.equals("clickEvent")) {
                  var33 = 23;
               }
               break;
            case -384454993:
               if (key.equals("insertion")) {
                  var33 = 22;
               }
               break;
            case 0:
               if (key.equals("")) {
                  var33 = 0;
               }
               break;
            case 108864:
               if (key.equals("nbt")) {
                  var33 = 8;
               }
               break;
            case 3029637:
               if (key.equals("bold")) {
                  var33 = 17;
               }
               break;
            case 3148879:
               if (key.equals("font")) {
                  var33 = 15;
               }
               break;
            case 3556653:
               if (key.equals("text")) {
                  var33 = 1;
               }
               break;
            case 3649734:
               if (key.equals("with")) {
                  var33 = 4;
               }
               break;
            case 93832333:
               if (key.equals("block")) {
                  var33 = 10;
               }
               break;
            case 94842723:
               if (key.equals("color")) {
                  var33 = 16;
               }
               break;
            case 96965648:
               if (key.equals("extra")) {
                  var33 = 13;
               }
               break;
            case 109264530:
               if (key.equals("score")) {
                  var33 = 5;
               }
               break;
            case 148487876:
               if (key.equals("obfuscated")) {
                  var33 = 21;
               }
               break;
            case 246292158:
               if (key.equals("hoverEvent")) {
                  var33 = 24;
               }
               break;
            case 502937869:
               if (key.equals("interpret")) {
                  var33 = 9;
               }
               break;
            case 761243362:
               if (key.equals("fallback")) {
                  var33 = 3;
               }
               break;
            case 1052832078:
               if (key.equals("translate")) {
                  var33 = 2;
               }
               break;
            case 1191572447:
               if (key.equals("selector")) {
                  var33 = 6;
               }
               break;
            case 1732829925:
               if (key.equals("separator")) {
                  var33 = 14;
               }
            }

            switch(var33) {
            case 0:
            case 1:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(text == null);
               text = input.readUTF();
               break;
            case 2:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(translate == null);
               translate = input.readUTF();
               break;
            case 3:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(translateFallback == null);
               translateFallback = input.readUTF();
               break;
            case 4:
               requireType(type, AdventureNBTSerialization.TagType.LIST);
               requireState(translateWith == null);
               translateWith = readComponentList(input, depth + 1);
               break;
            case 5:
               requireType(type, AdventureNBTSerialization.TagType.COMPOUND);
               requireState(scoreName == null && scoreObjective == null);

               AdventureNBTSerialization.TagType scoreType;
               while((scoreType = resolveNbtType(input.readByte())) != AdventureNBTSerialization.TagType.END) {
                  String scoreKey = input.readUTF();
                  byte var53 = -1;
                  switch(scoreKey.hashCode()) {
                  case -1489585863:
                     if (scoreKey.equals("objective")) {
                        var53 = 1;
                     }
                     break;
                  case 3373707:
                     if (scoreKey.equals("name")) {
                        var53 = 0;
                     }
                  }

                  switch(var53) {
                  case 0:
                     requireType(scoreType, AdventureNBTSerialization.TagType.STRING);
                     requireState(scoreName == null);
                     scoreName = input.readUTF();
                     break;
                  case 1:
                     requireType(scoreType, AdventureNBTSerialization.TagType.STRING);
                     requireState(scoreObjective == null);
                     scoreObjective = input.readUTF();
                     break;
                  default:
                     throw new IllegalStateException("Invalid nbt key read for score key: '" + scoreKey + "'");
                  }
               }

               requireState(scoreName != null && scoreObjective != null);
               break;
            case 6:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(selector == null);
               selector = input.readUTF();
               break;
            case 7:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(keybind == null);
               keybind = input.readUTF();
               break;
            case 8:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(nbt == null);
               nbt = input.readUTF();
               break;
            case 9:
               requireType(type, AdventureNBTSerialization.TagType.BYTE);
               nbtInterpret = input.readBoolean();
               break;
            case 10:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(nbtBlock == null);
               nbtBlock = input.readUTF();
               break;
            case 11:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(nbtEntity == null);
               nbtEntity = input.readUTF();
               break;
            case 12:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(nbtStorage == null);
               nbtStorage = input.readUTF();
               break;
            case 13:
               requireType(type, AdventureNBTSerialization.TagType.LIST);
               requireState(extra == null);
               extra = readComponentList(input, depth + 1);
               break;
            case 14:
               requireComponentType(type);
               requireState(separator == null);
               separator = readComponent(input, type, depth + 1);
               break;
            case 15:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(font == null);
               font = input.readUTF();
               break;
            case 16:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(color == null);
               color = input.readUTF();
               break;
            case 17:
               requireType(type, AdventureNBTSerialization.TagType.BYTE);
               requireState(bold == TextDecoration.State.NOT_SET);
               bold = TextDecoration.State.byBoolean(input.readBoolean());
               break;
            case 18:
               requireType(type, AdventureNBTSerialization.TagType.BYTE);
               requireState(italic == TextDecoration.State.NOT_SET);
               italic = TextDecoration.State.byBoolean(input.readBoolean());
               break;
            case 19:
               requireType(type, AdventureNBTSerialization.TagType.BYTE);
               requireState(underlined == TextDecoration.State.NOT_SET);
               underlined = TextDecoration.State.byBoolean(input.readBoolean());
               break;
            case 20:
               requireType(type, AdventureNBTSerialization.TagType.BYTE);
               requireState(strikethrough == TextDecoration.State.NOT_SET);
               strikethrough = TextDecoration.State.byBoolean(input.readBoolean());
               break;
            case 21:
               requireType(type, AdventureNBTSerialization.TagType.BYTE);
               requireState(obfuscated == TextDecoration.State.NOT_SET);
               obfuscated = TextDecoration.State.byBoolean(input.readBoolean());
               break;
            case 22:
               requireType(type, AdventureNBTSerialization.TagType.STRING);
               requireState(insertion == null);
               insertion = input.readUTF();
               break;
            case 23:
               requireType(type, AdventureNBTSerialization.TagType.COMPOUND);
               requireState(clickEventAction == null && clickEventValue == null);

               AdventureNBTSerialization.TagType clickType;
               while((clickType = resolveNbtType(input.readByte())) != AdventureNBTSerialization.TagType.END) {
                  String clickKey = input.readUTF();
                  byte var38 = -1;
                  switch(clickKey.hashCode()) {
                  case -1422950858:
                     if (clickKey.equals("action")) {
                        var38 = 0;
                     }
                     break;
                  case 111972721:
                     if (clickKey.equals("value")) {
                        var38 = 1;
                     }
                  }

                  switch(var38) {
                  case 0:
                     requireType(clickType, AdventureNBTSerialization.TagType.STRING);
                     requireState(clickEventAction == null);
                     String actionId = input.readUTF();
                     byte var55 = -1;
                     switch(actionId.hashCode()) {
                     case -1654598210:
                        if (actionId.equals("change_page")) {
                           var55 = 3;
                        }
                        break;
                     case -504306182:
                        if (actionId.equals("open_url")) {
                           var55 = 0;
                        }
                        break;
                     case -404256420:
                        if (actionId.equals("copy_to_clipboard")) {
                           var55 = 4;
                        }
                        break;
                     case 378483088:
                        if (actionId.equals("suggest_command")) {
                           var55 = 2;
                        }
                        break;
                     case 1845855639:
                        if (actionId.equals("run_command")) {
                           var55 = 1;
                        }
                     }

                     switch(var55) {
                     case 0:
                        clickEventAction = ClickEvent.Action.OPEN_URL;
                        continue;
                     case 1:
                        clickEventAction = ClickEvent.Action.RUN_COMMAND;
                        continue;
                     case 2:
                        clickEventAction = ClickEvent.Action.SUGGEST_COMMAND;
                        continue;
                     case 3:
                        clickEventAction = ClickEvent.Action.CHANGE_PAGE;
                        continue;
                     case 4:
                        clickEventAction = ClickEvent.Action.COPY_TO_CLIPBOARD;
                        continue;
                     default:
                        throw new IllegalStateException("Illegal click event action read: '" + actionId + "'");
                     }
                  case 1:
                     requireType(clickType, AdventureNBTSerialization.TagType.STRING);
                     requireState(clickEventValue == null);
                     clickEventValue = input.readUTF();
                     break;
                  default:
                     throw new IllegalStateException("Illegal click event nbt key read: '" + clickKey + "'");
                  }
               }

               requireState(clickEventAction != null && clickEventValue != null);
               break;
            case 24:
               requireType(type, AdventureNBTSerialization.TagType.COMPOUND);
               requireState(hoverEventAction == null);

               AdventureNBTSerialization.TagType hoverType;
               while((hoverType = resolveNbtType(input.readByte())) != AdventureNBTSerialization.TagType.END) {
                  String hoverKey = input.readUTF();
                  byte var39 = -1;
                  switch(hoverKey.hashCode()) {
                  case -1422950858:
                     if (hoverKey.equals("action")) {
                        var39 = 0;
                     }
                     break;
                  case -567321830:
                     if (hoverKey.equals("contents")) {
                        var39 = 1;
                     }
                  }

                  byte var42;
                  switch(var39) {
                  case 0:
                     requireType(hoverType, AdventureNBTSerialization.TagType.STRING);
                     requireState(hoverEventAction == null);
                     String actionId = input.readUTF();
                     var42 = -1;
                     switch(actionId.hashCode()) {
                     case -1903644907:
                        if (actionId.equals("show_item")) {
                           var42 = 1;
                        }
                        break;
                     case -1903331025:
                        if (actionId.equals("show_text")) {
                           var42 = 0;
                        }
                        break;
                     case 133701477:
                        if (actionId.equals("show_entity")) {
                           var42 = 2;
                        }
                     }

                     switch(var42) {
                     case 0:
                        hoverEventAction = HoverEvent.Action.SHOW_TEXT;
                        continue;
                     case 1:
                        hoverEventAction = HoverEvent.Action.SHOW_ITEM;
                        continue;
                     case 2:
                        hoverEventAction = HoverEvent.Action.SHOW_ENTITY;
                        continue;
                     default:
                        throw new IllegalStateException("Illegal hover event action read: '" + actionId + "'");
                     }
                  case 1:
                     requireState(hoverEventContents == null);
                     requireState(hoverEventAction != null);
                     String var41 = hoverEventAction.toString();
                     var42 = -1;
                     switch(var41.hashCode()) {
                     case -1903644907:
                        if (var41.equals("show_item")) {
                           var42 = 1;
                        }
                        break;
                     case -1903331025:
                        if (var41.equals("show_text")) {
                           var42 = 0;
                        }
                        break;
                     case 133701477:
                        if (var41.equals("show_entity")) {
                           var42 = 2;
                        }
                     }

                     String itemId;
                     AdventureNBTSerialization.TagType itemType;
                     String itemKey;
                     byte var49;
                     switch(var42) {
                     case 0:
                        requireComponentType(hoverType);
                        hoverEventContents = readComponent(input, hoverType, depth + 1);
                        continue;
                     case 1:
                        if (hoverType == AdventureNBTSerialization.TagType.STRING) {
                           itemId = input.readUTF();
                           hoverEventContents = HoverEvent.ShowItem.showItem((Key)Key.key(itemId), 1);
                           continue;
                        }

                        requireType(hoverType, AdventureNBTSerialization.TagType.COMPOUND);
                        itemId = null;
                        int count = 1;
                        String tag = null;

                        while((itemType = resolveNbtType(input.readByte())) != AdventureNBTSerialization.TagType.END) {
                           itemKey = input.readUTF();
                           var49 = -1;
                           switch(itemKey.hashCode()) {
                           case 3355:
                              if (itemKey.equals("id")) {
                                 var49 = 0;
                              }
                              break;
                           case 114586:
                              if (itemKey.equals("tag")) {
                                 var49 = 2;
                              }
                              break;
                           case 94851343:
                              if (itemKey.equals("count")) {
                                 var49 = 1;
                              }
                           }

                           switch(var49) {
                           case 0:
                              requireType(itemType, AdventureNBTSerialization.TagType.STRING);
                              requireState(itemId == null);
                              itemId = input.readUTF();
                              break;
                           case 1:
                              requireType(itemType, AdventureNBTSerialization.TagType.INT);
                              count = input.readInt();
                              break;
                           case 2:
                              requireType(itemType, AdventureNBTSerialization.TagType.STRING);
                              tag = input.readUTF();
                           }
                        }

                        requireState(itemId != null);
                        hoverEventContents = HoverEvent.ShowItem.showItem(Key.key(itemId), count, tag == null ? null : BinaryTagHolder.binaryTagHolder(tag));
                        continue;
                     case 2:
                        requireType(hoverType, AdventureNBTSerialization.TagType.COMPOUND);
                        itemId = null;
                        UUID entityId = null;
                        Component entityName = null;

                        while((itemType = resolveNbtType(input.readByte())) != AdventureNBTSerialization.TagType.END) {
                           itemKey = input.readUTF();
                           var49 = -1;
                           switch(itemKey.hashCode()) {
                           case 3355:
                              if (itemKey.equals("id")) {
                                 var49 = 1;
                              }
                              break;
                           case 3373707:
                              if (itemKey.equals("name")) {
                                 var49 = 2;
                              }
                              break;
                           case 3575610:
                              if (itemKey.equals("type")) {
                                 var49 = 0;
                              }
                           }

                           switch(var49) {
                           case 0:
                              requireType(itemType, AdventureNBTSerialization.TagType.STRING);
                              requireState(itemId == null);
                              itemId = input.readUTF();
                              break;
                           case 1:
                              requireType(itemType, AdventureNBTSerialization.TagType.INT_ARRAY);
                              requireState(entityId == null);
                              entityId = readUniqueId(input);
                              break;
                           case 2:
                              requireComponentType(itemType);
                              requireState(entityName == null);
                              entityName = readComponent(input, itemType, depth + 1);
                           }
                        }

                        requireState(itemId != null && entityId != null);
                        hoverEventContents = HoverEvent.ShowEntity.showEntity(Key.key(itemId), entityId, entityName);
                     default:
                        continue;
                     }
                  default:
                     throw new IllegalStateException("Illegal hover event nbt key read: '" + hoverKey + "'");
                  }
               }

               requireState(hoverEventContents != null);
               break;
            default:
               throw new IllegalStateException("Illegal component nbt key read: '" + key + "'");
            }
         }

         Object builder;
         if (text != null) {
            builder = Component.text().content(text);
         } else if (translate != null) {
            if (translateWith != null) {
               builder = Component.translatable().key(translate).fallback(translateFallback).args(translateWith);
            } else {
               builder = Component.translatable().key(translate).fallback(translateFallback);
            }
         } else if (scoreName != null && scoreObjective != null) {
            builder = Component.score().name(scoreName).objective(scoreObjective);
         } else if (selector != null) {
            builder = Component.selector().pattern(selector).separator(separator);
         } else if (keybind != null) {
            builder = Component.keybind().keybind(keybind);
         } else {
            if (nbt == null) {
               throw new IllegalStateException("Illegal nbt component, component type could not be determined");
            }

            if (nbtBlock != null) {
               builder = ((BlockNBTComponent.Builder)((BlockNBTComponent.Builder)((BlockNBTComponent.Builder)Component.blockNBT().nbtPath(nbt)).interpret(nbtInterpret)).separator(separator)).pos(BlockNBTComponent.Pos.fromString(nbtBlock));
            } else if (nbtEntity != null) {
               builder = ((EntityNBTComponent.Builder)((EntityNBTComponent.Builder)((EntityNBTComponent.Builder)Component.entityNBT().nbtPath(nbt)).interpret(nbtInterpret)).separator(separator)).selector(nbtEntity);
            } else {
               if (nbtStorage == null) {
                  throw new IllegalStateException("Illegal nbt component, block/entity/storage is missing");
               }

               builder = ((StorageNBTComponent.Builder)((StorageNBTComponent.Builder)((StorageNBTComponent.Builder)Component.storageNBT().nbtPath(nbt)).interpret(nbtInterpret)).separator(separator)).storage(Key.key(nbtStorage));
            }
         }

         if (font != null) {
            ((ComponentBuilder)builder).font(Key.key(font));
         }

         if (color != null) {
            ((ComponentBuilder)builder).color(parseColor(color));
         }

         if (obfuscated != TextDecoration.State.NOT_SET) {
            ((ComponentBuilder)builder).decoration(TextDecoration.OBFUSCATED, obfuscated);
         }

         if (bold != TextDecoration.State.NOT_SET) {
            ((ComponentBuilder)builder).decoration(TextDecoration.BOLD, bold);
         }

         if (strikethrough != TextDecoration.State.NOT_SET) {
            ((ComponentBuilder)builder).decoration(TextDecoration.STRIKETHROUGH, strikethrough);
         }

         if (underlined != TextDecoration.State.NOT_SET) {
            ((ComponentBuilder)builder).decoration(TextDecoration.UNDERLINED, underlined);
         }

         if (italic != TextDecoration.State.NOT_SET) {
            ((ComponentBuilder)builder).decoration(TextDecoration.ITALIC, italic);
         }

         if (insertion != null) {
            ((ComponentBuilder)builder).insertion(insertion);
         }

         if (clickEventAction != null) {
            ((ComponentBuilder)builder).clickEvent(ClickEvent.clickEvent(clickEventAction, clickEventValue));
         }

         if (hoverEventAction != null) {
            @SuppressWarnings("unchecked")
            HoverEvent<Object> hoverEvent = HoverEvent.hoverEvent((HoverEvent.Action<Object>) hoverEventAction, hoverEventContents);
            ((ComponentBuilder)builder).hoverEvent(hoverEvent);
         }

         if (extra != null) {
            ((ComponentBuilder)builder).append((Iterable)extra);
         }

         return ((ComponentBuilder)builder).build();
      }
   }

   public static void writeComponent(DataOutput output, Component component) throws IOException {
      AdventureNBTSerialization.TagType tagType = getComponentTagType(component);
      output.writeByte(tagType.getId());
      writeComponent(output, component, tagType);
   }

   private static void writeComponent(DataOutput output, Component component, AdventureNBTSerialization.TagType rootType) throws IOException {
      if (rootType == AdventureNBTSerialization.TagType.STRING) {
         output.writeUTF(((TextComponent)component).content());
      } else if (rootType != AdventureNBTSerialization.TagType.COMPOUND) {
         throw new UnsupportedEncodingException();
      } else {
         if (component instanceof TextComponent) {
            output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
            output.writeUTF("text");
            output.writeUTF(((TextComponent)component).content());
         } else {
            String nbtPath;
            if (component instanceof TranslatableComponent) {
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("translate");
               output.writeUTF(((TranslatableComponent)component).key());
               nbtPath = ((TranslatableComponent)component).fallback();
               if (nbtPath != null) {
                  output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
                  output.writeUTF("fallback");
                  output.writeUTF(nbtPath);
               }

               List<Component> args = ((TranslatableComponent)component).args();
               if (!args.isEmpty()) {
                  output.writeByte(AdventureNBTSerialization.TagType.LIST.getId());
                  output.writeUTF("with");
                  writeComponentList(output, args);
               }
            } else if (component instanceof ScoreComponent) {
               output.writeByte(AdventureNBTSerialization.TagType.COMPOUND.getId());
               output.writeUTF("score");
               nbtPath = ((ScoreComponent)component).name();
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("name");
               output.writeUTF(nbtPath);
               String scoreObjective = ((ScoreComponent)component).objective();
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("objective");
               output.writeUTF(scoreObjective);
               output.writeByte(AdventureNBTSerialization.TagType.END.getId());
            } else if (component instanceof SelectorComponent) {
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("selector");
               output.writeUTF(((SelectorComponent)component).pattern());
               Component separator = ((SelectorComponent)component).separator();
               if (separator != null) {
                  AdventureNBTSerialization.TagType componentTagType = getComponentTagType(separator);
                  output.writeByte(componentTagType.getId());
                  output.writeUTF("separator");
                  writeComponent(output, separator, componentTagType);
               }
            } else if (component instanceof KeybindComponent) {
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("keybind");
               output.writeUTF(((KeybindComponent)component).keybind());
            } else {
               if (!(component instanceof NBTComponent)) {
                  throw new UnsupportedOperationException();
               }

               nbtPath = ((NBTComponent)component).nbtPath();
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("nbt");
               output.writeUTF(nbtPath);
               boolean interpret = ((NBTComponent)component).interpret();
               if (interpret) {
                  output.writeByte(AdventureNBTSerialization.TagType.BYTE.getId());
                  output.writeUTF("interpret");
                  output.writeBoolean(true);
               }

               Component separator = ((NBTComponent)component).separator();
               if (separator != null) {
                  AdventureNBTSerialization.TagType componentTagType = getComponentTagType(separator);
                  output.writeByte(componentTagType.getId());
                  output.writeUTF("separator");
                  writeComponent(output, separator, componentTagType);
               }

               if (component instanceof BlockNBTComponent) {
                  BlockNBTComponent.Pos pos = ((BlockNBTComponent)component).pos();
                  output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
                  output.writeUTF("block");
                  output.writeUTF(pos.asString());
               } else if (component instanceof EntityNBTComponent) {
                  String selector = ((EntityNBTComponent)component).selector();
                  output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
                  output.writeUTF("entity");
                  output.writeUTF(selector);
               } else {
                  if (!(component instanceof StorageNBTComponent)) {
                     throw new UnsupportedOperationException();
                  }

                  Key storage = ((StorageNBTComponent)component).storage();
                  output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
                  output.writeUTF("storage");
                  output.writeUTF(storage.asString());
               }
            }
         }

         if (component.hasStyling()) {
            Key font = component.font();
            if (font != null) {
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("font");
               output.writeUTF(font.asString());
            }

            TextColor color = component.color();
            if (color != null) {
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("color");
               output.writeUTF(stringifyColor(color));
            }

            TextDecoration.State bold = component.decoration(TextDecoration.BOLD);
            if (bold != TextDecoration.State.NOT_SET) {
               output.writeByte(AdventureNBTSerialization.TagType.BYTE.getId());
               output.writeUTF("bold");
               output.writeBoolean(bold == TextDecoration.State.TRUE);
            }

            TextDecoration.State italic = component.decoration(TextDecoration.ITALIC);
            if (italic != TextDecoration.State.NOT_SET) {
               output.writeByte(AdventureNBTSerialization.TagType.BYTE.getId());
               output.writeUTF("italic");
               output.writeBoolean(italic == TextDecoration.State.TRUE);
            }

            TextDecoration.State underlined = component.decoration(TextDecoration.UNDERLINED);
            if (underlined != TextDecoration.State.NOT_SET) {
               output.writeByte(AdventureNBTSerialization.TagType.BYTE.getId());
               output.writeUTF("underlined");
               output.writeBoolean(underlined == TextDecoration.State.TRUE);
            }

            TextDecoration.State strikethrough = component.decoration(TextDecoration.STRIKETHROUGH);
            if (strikethrough != TextDecoration.State.NOT_SET) {
               output.writeByte(AdventureNBTSerialization.TagType.BYTE.getId());
               output.writeUTF("strikethrough");
               output.writeBoolean(strikethrough == TextDecoration.State.TRUE);
            }

            TextDecoration.State obfuscated = component.decoration(TextDecoration.OBFUSCATED);
            if (obfuscated != TextDecoration.State.NOT_SET) {
               output.writeByte(AdventureNBTSerialization.TagType.BYTE.getId());
               output.writeUTF("obfuscated");
               output.writeBoolean(obfuscated == TextDecoration.State.TRUE);
            }

            String insertion = component.insertion();
            if (insertion != null) {
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("insertion");
               output.writeUTF(insertion);
            }

            ClickEvent clickEvent = component.clickEvent();
            if (clickEvent != null) {
               output.writeByte(AdventureNBTSerialization.TagType.COMPOUND.getId());
               output.writeUTF("clickEvent");
               ClickEvent.Action action = clickEvent.action();
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("action");
               output.writeUTF(action.toString());
               String value = clickEvent.value();
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("value");
               output.writeUTF(value);
               output.writeByte(AdventureNBTSerialization.TagType.END.getId());
            }

            HoverEvent<?> hoverEvent = component.hoverEvent();
            if (hoverEvent != null) {
               output.writeByte(AdventureNBTSerialization.TagType.COMPOUND.getId());
               output.writeUTF("hoverEvent");
               HoverEvent.Action<?> action = hoverEvent.action();
               output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
               output.writeUTF("action");
               output.writeUTF(action.toString());
               String var14 = action.toString();
               byte var15 = -1;
               switch(var14.hashCode()) {
               case -1903644907:
                  if (var14.equals("show_item")) {
                     var15 = 1;
                  }
                  break;
               case -1903331025:
                  if (var14.equals("show_text")) {
                     var15 = 0;
                  }
                  break;
               case 133701477:
                  if (var14.equals("show_entity")) {
                     var15 = 2;
                  }
               }

               switch(var15) {
               case 0:
                  Component text = (Component)hoverEvent.value();
                  AdventureNBTSerialization.TagType textTagType = getComponentTagType(text);
                  output.writeByte(textTagType.getId());
                  output.writeUTF("contents");
                  writeComponent(output, text, textTagType);
                  break;
               case 1:
                  HoverEvent.ShowItem item = (HoverEvent.ShowItem)hoverEvent.value();
                  Key itemId = item.item();
                  int count = item.count();
                  BinaryTagHolder nbt = item.nbt();
                  if (count == 1 && nbt == null) {
                     output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
                     output.writeUTF("contents");
                     output.writeUTF(itemId.asString());
                     break;
                  }

                  output.writeByte(AdventureNBTSerialization.TagType.COMPOUND.getId());
                  output.writeUTF("contents");
                  output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
                  output.writeUTF("id");
                  output.writeUTF(itemId.asString());
                  if (count != 1) {
                     output.writeByte(AdventureNBTSerialization.TagType.INT.getId());
                     output.writeUTF("count");
                     output.writeInt(count);
                  }

                  if (nbt != null) {
                     output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
                     output.writeUTF("tag");
                     output.writeUTF(nbt.string());
                  }

                  output.writeByte(AdventureNBTSerialization.TagType.END.getId());
                  break;
               case 2:
                  HoverEvent.ShowEntity entity = (HoverEvent.ShowEntity)hoverEvent.value();
                  Key entityType = entity.type();
                  UUID entityId = entity.id();
                  Component entityName = entity.name();
                  output.writeByte(AdventureNBTSerialization.TagType.COMPOUND.getId());
                  output.writeUTF("contents");
                  output.writeByte(AdventureNBTSerialization.TagType.STRING.getId());
                  output.writeUTF("type");
                  output.writeUTF(entityType.asString());
                  output.writeByte(AdventureNBTSerialization.TagType.INT_ARRAY.getId());
                  output.writeUTF("id");
                  writeUniqueId(output, entityId);
                  if (entityName != null) {
                     AdventureNBTSerialization.TagType nameTagType = getComponentTagType(entityName);
                     output.writeByte(nameTagType.getId());
                     output.writeUTF("name");
                     writeComponent(output, entityName, nameTagType);
                  }

                  output.writeByte(AdventureNBTSerialization.TagType.END.getId());
               }

               output.writeByte(AdventureNBTSerialization.TagType.END.getId());
            }
         }

         List<Component> children = component.children();
         if (!children.isEmpty()) {
            output.writeByte(AdventureNBTSerialization.TagType.LIST.getId());
            output.writeUTF("extra");
            writeComponentList(output, children);
         }

         output.writeByte(AdventureNBTSerialization.TagType.END.getId());
      }
   }

   private static List<Component> readComponentList(DataInput input, int depth) throws IOException {
      byte typeId = input.readByte();
      int length = input.readInt();
      if (typeId == AdventureNBTSerialization.TagType.END.getId() && length > 0) {
         throw new IllegalStateException("Non-empty list with no specified type read");
      } else {
         AdventureNBTSerialization.TagType type = resolveNbtType(typeId);
         requireComponentType(type);
         if (length == 0) {
            return Collections.emptyList();
         } else {
            List<Component> components = new ArrayList(length);

            for(int i = 0; i < length; ++i) {
               components.add(readComponent(input, type, depth));
            }

            return components;
         }
      }
   }

   private static void writeComponentList(DataOutput output, List<Component> components) throws IOException {
      if (components.isEmpty()) {
         output.writeByte(AdventureNBTSerialization.TagType.END.getId());
         output.writeInt(0);
      } else {
         boolean simple = true;
         Iterator var3 = components.iterator();

         while(var3.hasNext()) {
            Component component = (Component)var3.next();
            if (!isSimpleComponent(component)) {
               simple = false;
               break;
            }
         }

         AdventureNBTSerialization.TagType tagType = getComponentTagType(simple);
         output.writeByte(tagType.getId());
         output.writeInt(components.size());
         Iterator var7 = components.iterator();

         while(var7.hasNext()) {
            Component component = (Component)var7.next();
            writeComponent(output, component, tagType);
         }

      }
   }

   private static UUID readUniqueId(DataInput input) throws IOException {
      int arrayLength = input.readInt();
      if (arrayLength != 4) {
         throw new IllegalStateException("Invalid encoded uuid length: " + arrayLength + " != 4");
      } else {
         return new UUID((long)input.readInt() << 32 | (long)input.readInt() & 4294967295L, (long)input.readInt() << 32 | (long)input.readInt() & 4294967295L);
      }
   }

   private static void writeUniqueId(DataOutput output, UUID uniqueId) throws IOException {
      long mostBits = uniqueId.getMostSignificantBits();
      long leastBits = uniqueId.getLeastSignificantBits();
      output.writeInt(4);
      output.writeInt((int)(mostBits >> 32));
      output.writeInt((int)mostBits);
      output.writeInt((int)(leastBits >> 32));
      output.writeInt((int)leastBits);
   }

   private static AdventureNBTSerialization.TagType getComponentTagType(Component component) {
      return getComponentTagType(isSimpleComponent(component));
   }

   private static AdventureNBTSerialization.TagType getComponentTagType(boolean simple) {
      return simple ? AdventureNBTSerialization.TagType.STRING : AdventureNBTSerialization.TagType.COMPOUND;
   }

   private static boolean isSimpleComponent(Component component) {
      return component instanceof TextComponent && !component.hasStyling() && component.children().isEmpty();
   }

   private static TextColor parseColor(String colorStr) {
      if (colorStr.isEmpty()) {
         throw new IllegalStateException("Tried parsing empty color string");
      } else {
         TextColor color;
         if (colorStr.charAt(0) == '#') {
            color = TextColor.fromHexString(colorStr);
         } else {
            color = (TextColor)NamedTextColor.NAMES.value(colorStr);
         }

         if (color == null) {
            throw new IllegalStateException("Can't parse color from: " + colorStr);
         } else {
            return color;
         }
      }
   }

   private static String stringifyColor(TextColor color) {
      return color instanceof NamedTextColor ? color.toString() : color.asHexString();
   }

   private static enum TagType {
      END,
      BYTE,
      SHORT,
      INT,
      LONG,
      FLOAT,
      DOUBLE,
      BYTE_ARRAY,
      STRING,
      LIST,
      COMPOUND,
      INT_ARRAY,
      LONG_ARRAY;

      public int getId() {
         return this.ordinal();
      }

      // $FF: synthetic method
      private static AdventureNBTSerialization.TagType[] $values() {
         return new AdventureNBTSerialization.TagType[]{END, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BYTE_ARRAY, STRING, LIST, COMPOUND, INT_ARRAY, LONG_ARRAY};
      }
   }
}
