package dev.artixdev.practice.utils.serialize;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import dev.artixdev.libs.com.cryptomorin.xseries.XPotion;
import dev.artixdev.libs.com.google.gson.JsonArray;
import dev.artixdev.libs.com.google.gson.JsonDeserializationContext;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonNull;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonParser;
import dev.artixdev.libs.com.google.gson.JsonSerializationContext;
import dev.artixdev.libs.com.google.gson.JsonSerializer;
import dev.artixdev.practice.models.Kit;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemUtils;

public class KitSerializer implements JsonDeserializer<Kit>, JsonSerializer<Kit> {
    
    public Kit deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        return fromJson(jsonElement);
    }

    public JsonElement serialize(Kit kit, Type type, JsonSerializationContext context) {
        return toJson(kit);
    }

    private static JsonElement toJson(Kit kit) {
        if (kit == null) {
            return null;
        }
        
        JsonObject object = new JsonObject();
        object.addProperty("_id", kit.getName());
        object.addProperty("displayName", ChatUtils.translate(kit.getDisplayName()));
        object.addProperty("displayIcon", ItemUtils.serializeItemStack(kit.getIcon()));
        object.addProperty("knockbackProfile", kit.getKnockbackProfile());
        object.addProperty("botKnockbackProfile", kit.getBotKnockbackProfile());
        object.addProperty("bestOf", kit.getBestOf());
        object.addProperty("sortPriority", kit.getSortPriority());
        object.addProperty("noDamageTicks", kit.getNoDamageTicks());
        object.addProperty("respawnTicks", kit.getRespawnTicks());
        object.addProperty("countdownTicks", kit.getCountdownTicks());
        object.addProperty("disclaimerEnabled", kit.isDisclaimerEnabled());
        
        JsonObject loadOut = new JsonObject();
        loadOut.addProperty("items", ItemUtils.serializeItemStacks(kit.getContents()));
        loadOut.addProperty("armor", ItemUtils.serializeItemStacks(kit.getArmor()));
        loadOut.addProperty("editItems", ItemUtils.serializeItemStacks(kit.getEditItems()));
        object.add("inventory", loadOut);
        
        if (!kit.getPotionEffects().isEmpty()) {
            JsonArray effects = new JsonArray();
            kit.getPotionEffects().forEach((effect) -> {
                effects.add(effect.getType().getName() + ", " + effect.getDuration() + ", " + effect.getAmplifier());
            });
            object.add("effects", effects);
        }

        if (!kit.getDisclaimer().isEmpty()) {
            JsonArray disclaimerArray = new JsonArray();
            List<String> disclaimer = kit.getDisclaimer();
            Objects.requireNonNull(disclaimerArray);
            disclaimer.forEach(disclaimerArray::add);
            object.add("disclaimer", disclaimerArray);
        }

        if (!kit.getStartCommands().isEmpty()) {
            JsonArray commandsArray = new JsonArray();
            List<String> startCommands = kit.getStartCommands();
            Objects.requireNonNull(commandsArray);
            startCommands.forEach(commandsArray::add);
            object.add("startCommands", commandsArray);
        }

        if (!kit.getEndCommands().isEmpty()) {
            JsonArray commandsArray = new JsonArray();
            List<String> endCommands = kit.getEndCommands();
            Objects.requireNonNull(commandsArray);
            endCommands.forEach(commandsArray::add);
            object.add("endCommands", commandsArray);
        }

        JsonObject rules = new JsonObject();
        rules.addProperty("enabled", kit.isEnabled());
        rules.addProperty("editable", kit.isEditable());
        rules.addProperty("ranked", kit.isRanked());
        rules.addProperty("build", kit.isBuild());
        rules.addProperty("stickSpawn", kit.isStickSpawn());
        rules.addProperty("showHP", kit.isShowHP());
        rules.addProperty("noRegen", kit.isNoRegen());
        rules.addProperty("noFall", kit.isNoFall());
        rules.addProperty("noHunger", kit.isNoHunger());
        rules.addProperty("spleef", kit.isSpleef());
        rules.addProperty("battleRush", kit.isBattleRush());
        rules.addProperty("fireballFight", kit.isFireballFight());
        rules.addProperty("bedFight", kit.isBedFight());
        rules.addProperty("topFight", kit.isTopFight());
        rules.addProperty("stickFight", kit.isStickFight());
        rules.addProperty("pearlFight", kit.isPearlFight());
        rules.addProperty("bridges", kit.isBridges());
        rules.addProperty("boxing", kit.isBoxing());
        rules.addProperty("combo", kit.isCombo());
        rules.addProperty("sumo", kit.isSumo());
        rules.addProperty("liquidKill", kit.isLiquidKill());
        rules.addProperty("partyFFA", kit.isPartyFFA());
        rules.addProperty("partySplit", kit.isPartySplit());
        rules.addProperty("pearlDamage", kit.isPearlDamage());
        rules.addProperty("mlgRush", kit.isMlgRush());
        rules.addProperty("buildHeightDamage", kit.isBuildHeightDamage());
        object.add("rules", rules);
        
        return object;
    }

    private static Kit fromJson(JsonElement src) {
        if (src != null && src.isJsonObject()) {
            JsonObject object = src.getAsJsonObject();
            String name = object.get("_id").getAsString();
            String displayName = ChatUtils.translate(object.get("displayName").getAsString());
            ItemStack itemStack = null;

            String knockbackProfile;
            try {
                knockbackProfile = object.get("displayIcon").getAsString();
                JsonObject itemObject = JsonParser.parseString(knockbackProfile).getAsJsonObject();
                itemStack = ItemUtils.deserializeItemStack(itemObject);
            } catch (Exception e) {
                // Handle exception silently
            }

            if (object.has("knockbackProfile") && !object.get("knockbackProfile").equals(JsonNull.INSTANCE)) {
                knockbackProfile = object.get("knockbackProfile").getAsString();
            } else {
                knockbackProfile = "default";
            }

            String botKnockbackProfile;
            if (object.has("botKnockbackProfile") && !object.get("botKnockbackProfile").equals(JsonNull.INSTANCE)) {
                botKnockbackProfile = object.get("botKnockbackProfile").getAsString();
            } else {
                botKnockbackProfile = knockbackProfile;
            }

            int bestOf = object.get("bestOf").getAsInt();
            int sortPriority = object.get("sortPriority").getAsInt();
            int noDamageTicks = object.get("noDamageTicks").getAsInt();
            int countdownTicks = object.has("countdownTicks") ? object.get("countdownTicks").getAsInt() : 5;
            boolean disclaimerEnabled = object.has("disclaimerEnabled") ? object.get("disclaimerEnabled").getAsBoolean() : false;
            int respawnTicks = 5;
            
            if (object.has("respawnTicks")) {
                respawnTicks = object.get("respawnTicks").getAsInt();
            } else if (!name.contains("Bed") && !name.contains("BedFight")) {
                if (name.contains("Pearl") || name.contains("PearlFight") || name.contains("TopFight") || 
                    name.contains("StickFight") || name.contains("Rush") || name.contains("BattleRush")) {
                    respawnTicks = 3;
                }
            } else {
                respawnTicks = 4;
            }

            Kit kit = new Kit(name);
            kit.setDisplayName(displayName);
            if (itemStack != null) {
                kit.setDisplayIcon(itemStack);
            }

            kit.setKnockbackProfile(knockbackProfile);
            kit.setBotKnockbackProfile(botKnockbackProfile);
            kit.setBestOf(bestOf);
            kit.setSortPriority(sortPriority);
            kit.setNoDamageTicks(noDamageTicks);
            kit.setRespawnTicks(respawnTicks);
            kit.setCountdownTicks(countdownTicks);
            kit.setDisclaimerEnabled(disclaimerEnabled);
            
            JsonObject inventory = object.get("inventory").getAsJsonObject();
            ItemStack[] contents = ItemUtils.deserializeItemStacks(inventory.get("items").getAsString());
            ItemStack[] armor = ItemUtils.deserializeItemStacks(inventory.get("armor").getAsString());
            ItemStack[] editItems = ItemUtils.deserializeItemStacks(inventory.get("editItems").getAsString());
            kit.setContents(contents);
            kit.setArmor(armor);
            kit.setEditItems(editItems);
            
            JsonArray array;
            Iterator<JsonElement> iterator;
            JsonElement element;
            String command;
            
            if (object.has("effects")) {
                array = object.get("effects").getAsJsonArray();
                iterator = array.iterator();

                while(iterator.hasNext()) {
                    element = iterator.next();
                    command = element.getAsString();

                    try {
                        String[] splitString = command.split(", ");
                        Optional<XPotion> optional = XPotion.matchXPotion(splitString[0]);
                        if (optional.isPresent()) {
                            XPotion potion = optional.get();
                            PotionEffect potionEffect = new PotionEffect(potion.getPotionEffectType(), 
                                Integer.parseInt(splitString[1]), Integer.parseInt(splitString[2]));
                            kit.getPotionEffects().add(potionEffect);
                        }
                    } catch (Exception e) {
                        // Handle exception silently
                    }
                }
            }

            if (object.has("disclaimer")) {
                array = object.get("disclaimer").getAsJsonArray();
                iterator = array.iterator();

                while(iterator.hasNext()) {
                    element = iterator.next();
                    command = element.getAsString();
                    kit.getDisclaimer().add(command);
                }
            }

            if (object.has("startCommands")) {
                array = object.get("startCommands").getAsJsonArray();
                iterator = array.iterator();

                while(iterator.hasNext()) {
                    element = iterator.next();
                    command = element.getAsString();
                    kit.getStartCommands().add(command);
                }
            }

            if (object.has("endCommands")) {
                array = object.get("endCommands").getAsJsonArray();
                iterator = array.iterator();

                while(iterator.hasNext()) {
                    element = iterator.next();
                    command = element.getAsString();
                    kit.getEndCommands().add(command);
                }
            }

            JsonObject rules = object.get("rules").getAsJsonObject();
            kit.setEnabled(rules.get("enabled").getAsBoolean());
            kit.setEditable(rules.get("editable").getAsBoolean());
            kit.setRanked(rules.get("ranked").getAsBoolean());
            kit.setBuild(rules.get("build").getAsBoolean());
            kit.setStickSpawn(rules.get("stickSpawn").getAsBoolean());
            kit.setShowHP(rules.get("showHP").getAsBoolean());
            
            if (rules.has("regen")) {
                kit.setNoRegen(false);
            } else if (rules.has("noRegen")) {
                kit.setNoRegen(rules.get("noRegen").getAsBoolean());
            }

            if (rules.has("noFall")) {
                kit.setNoFall(rules.get("noFall").getAsBoolean());
            }

            if (rules.has("noHunger")) {
                kit.setNoHunger(rules.get("noHunger").getAsBoolean());
            }

            if (rules.has("liquidKill")) {
                kit.setLiquidKill(rules.get("liquidKill").getAsBoolean());
            }

            kit.setSpleef(rules.get("spleef").getAsBoolean());
            kit.setBattleRush(rules.get("battleRush").getAsBoolean());
            if (rules.has("fireballFight")) {
                kit.setFireballFight(rules.get("fireballFight").getAsBoolean());
            }

            if (rules.has("stickFight")) {
                kit.setStickFight(rules.get("stickFight").getAsBoolean());
            }

            kit.setPearlFight(rules.get("pearlFight").getAsBoolean());
            kit.setBridges(rules.get("bridges").getAsBoolean());
            kit.setBoxing(rules.get("boxing").getAsBoolean());
            kit.setCombo(rules.get("combo").getAsBoolean());
            kit.setSumo(rules.get("sumo").getAsBoolean());
            kit.setMlgRush(rules.get("mlgRush").getAsBoolean());
            
            if (rules.has("bedFight")) {
                kit.setBedFight(rules.get("bedFight").getAsBoolean());
            }

            if (rules.has("topFight")) {
                kit.setTopFight(rules.get("topFight").getAsBoolean());
            }

            kit.setPartyFFA(rules.get("partyFFA").getAsBoolean());
            kit.setPartySplit(rules.get("partySplit").getAsBoolean());
            kit.setPearlDamage(rules.get("pearlDamage").getAsBoolean());
            if (rules.has("buildHeightDamage")) {
                kit.setBuildHeightDamage(rules.get("buildHeightDamage").getAsBoolean());
            }

            return kit;
        } else {
            return null;
        }
    }
}
