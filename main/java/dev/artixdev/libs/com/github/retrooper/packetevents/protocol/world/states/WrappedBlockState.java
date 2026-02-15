package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.BlockFace;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Attachment;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Bloom;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.East;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Face;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Hinge;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Instrument;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Leaves;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Mode;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.North;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Orientation;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Part;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.SculkSensorPhase;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Shape;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.South;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Thickness;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Tilt;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.TrialSpawnerState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Type;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.VerticalDirection;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.West;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public class WrappedBlockState {
   private static final WrappedBlockState AIR;
   private static final Map<Byte, Map<String, WrappedBlockState>> BY_STRING;
   private static final Map<Byte, Map<Integer, WrappedBlockState>> BY_ID;
   private static final Map<Byte, Map<WrappedBlockState, String>> INTO_STRING;
   private static final Map<Byte, Map<WrappedBlockState, Integer>> INTO_ID;
   private static final Map<Byte, Map<StateType, WrappedBlockState>> DEFAULT_STATES;
   private static final Map<String, String> STRING_UPDATER;
   private static Map<Map<StateValue, Object>, Map<StateValue, Object>> cache;
   int globalID;
   StateType type;
   Map<StateValue, Object> data = new HashMap(0);
   boolean hasClonedData = false;
   byte mappingsIndex;

   public WrappedBlockState(StateType type, String[] data, int globalID, byte mappingsIndex) {
      this.type = type;
      this.globalID = globalID;
      if (data != null) {
         for (String s : data) {
            try {
               String[] split = s.split("=");
               StateValue value = StateValue.byName(split[0]);
               this.data.put(value, value.getParser().apply(split[1].toUpperCase(Locale.ROOT)));
            } catch (Exception ex) {
               ex.printStackTrace();
               System.out.println("Failed to parse block state: " + s);
            }
         }
      }

      this.data = (Map)cache.computeIfAbsent(this.data, (key) -> {
         return key;
      });
      this.mappingsIndex = mappingsIndex;
   }

   public WrappedBlockState(StateType type, Map<StateValue, Object> data, int globalID, byte mappingsIndex) {
      this.globalID = globalID;
      this.type = type;
      this.data = data;
      this.mappingsIndex = mappingsIndex;
   }

   @NotNull
   public static WrappedBlockState getByGlobalId(int globalID) {
      return getByGlobalId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), globalID);
   }

   @NotNull
   public static WrappedBlockState getByGlobalId(ClientVersion version, int globalID) {
      if (globalID == 0) {
         return AIR;
      } else {
         byte mappingsIndex = getMappingsIndex(version);
         return ((WrappedBlockState)((Map)BY_ID.get(mappingsIndex)).getOrDefault(globalID, AIR)).clone();
      }
   }

   @NotNull
   public static WrappedBlockState getByString(String string) {
      return getByString(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), string);
   }

   @NotNull
   public static WrappedBlockState getByString(ClientVersion version, String string) {
      byte mappingsIndex = getMappingsIndex(version);
      return ((WrappedBlockState)((Map)BY_STRING.get(mappingsIndex)).getOrDefault(string.replace("minecraft:", ""), AIR)).clone();
   }

   @NotNull
   public static WrappedBlockState getDefaultState(StateType type) {
      return getDefaultState(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), type);
   }

   @NotNull
   public static WrappedBlockState getDefaultState(ClientVersion version, StateType type) {
      if (type == StateTypes.AIR) {
         return AIR;
      } else {
         byte mappingsIndex = getMappingsIndex(version);
         WrappedBlockState state = (WrappedBlockState)((Map)DEFAULT_STATES.get(mappingsIndex)).get(type);
         if (state == null) {
            PacketEvents.getAPI().getLogger().config("Default state for " + type.getName() + " is null. Returning AIR");
            return AIR;
         } else {
            return state.clone();
         }
      }
   }

   private static byte getMappingsIndex(ClientVersion version) {
      if (version.isOlderThan(ClientVersion.V_1_13)) {
         return 0;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_13_1)) {
         return 1;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
         return 2;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_14_4)) {
         return 3;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_15_2)) {
         return 4;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_16_1)) {
         return 5;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
         return 6;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_18_2)) {
         return 7;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_19_1)) {
         return 8;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_19_3)) {
         return 9;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_19_4)) {
         return 10;
      } else if (version.isOlderThanOrEquals(ClientVersion.V_1_20)) {
         return 11;
      } else {
         return (byte)(version.isOlderThanOrEquals(ClientVersion.V_1_20_2) ? 12 : 13);
      }
   }

   private static void loadLegacy() {
      Map<Integer, WrappedBlockState> stateByIdMap = new HashMap();
      Map<WrappedBlockState, Integer> stateToIdMap = new HashMap();
      Map<String, WrappedBlockState> stateByStringMap = new HashMap();
      Map<WrappedBlockState, String> stateToStringMap = new HashMap();
      HashMap stateTypeToBlockStateMap = new HashMap();

      try {
         InputStream mappings = (InputStream)PacketEvents.getAPI().getSettings().getResourceProvider().apply("assets/mappings/block/legacy_block_mappings.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(mappings));

         String line;
         while((line = reader.readLine()) != null) {
            String[] split = line.split(",");
            int id = Integer.parseInt(split[0]);
            int data = Integer.parseInt(split[1]);
            int combinedID = id << 4 | data;
            String fullString = line.substring(split[0].length() + split[1].length() + 2);
            int endIndex = split[2].indexOf("[");
            String blockString = split[2].substring(0, endIndex != -1 ? endIndex : split[2].length());
            StateType type = StateTypes.getByName(blockString);
            String[] dataStrings = null;
            if (endIndex != -1) {
               dataStrings = line.substring(split[0].length() + split[1].length() + 2 + blockString.length() + 1, line.length() - 1).split(",");
            }

            if (type == null) {
               PacketEvents.getAPI().getLogger().warning("Could not find type for " + blockString);
            }

            WrappedBlockState state = new WrappedBlockState(type, dataStrings, combinedID, (byte)0);
            stateByIdMap.put(combinedID, state);
            stateToStringMap.put(state, fullString);
            stateToIdMap.put(state, combinedID);
            stateByStringMap.putIfAbsent(fullString, state);
            stateTypeToBlockStateMap.putIfAbsent(type, state);
         }
      } catch (IOException ex) {
         PacketEvents.getAPI().getLogManager().debug("Palette reading failed! Unsupported version?");
         ex.printStackTrace();
      }

      BY_ID.put((byte)0, stateByIdMap);
      INTO_ID.put((byte)0, stateToIdMap);
      BY_STRING.put((byte)0, stateByStringMap);
      INTO_STRING.put((byte)0, stateToStringMap);
      DEFAULT_STATES.put((byte)0, stateTypeToBlockStateMap);
   }

   private static void loadModern(ClientVersion version) {
      byte mappingsIndex = getMappingsIndex(version);
      if (!BY_ID.containsKey(mappingsIndex)) {
         InputStream mappings = (InputStream)PacketEvents.getAPI().getSettings().getResourceProvider().apply("assets/mappings/block/modern_block_mappings.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(mappings));
         Map<Integer, WrappedBlockState> stateByIdMap = new HashMap();
         Map<WrappedBlockState, Integer> stateToIdMap = new HashMap();
         Map<String, WrappedBlockState> stateByStringMap = new HashMap();
         Map<WrappedBlockState, String> stateToStringMap = new HashMap();
         Map<StateType, WrappedBlockState> stateTypeToBlockStateMap = new HashMap();
         String versionString = version.getReleaseName();
         boolean found = false;
         int id = 0;

         String fullBlockString;
         try {
            while((fullBlockString = reader.readLine()) != null) {
               if (!found) {
                  if (fullBlockString.equals(versionString)) {
                     found = true;
                  }
               } else {
                  if (fullBlockString.charAt(1) == '.') {
                     break;
                  }

                  boolean isDefault = fullBlockString.startsWith("*");
                  fullBlockString = fullBlockString.replace("*", "");
                  int index = fullBlockString.indexOf("[");
                  String blockString = fullBlockString.substring(0, index == -1 ? fullBlockString.length() : index);
                  StateType type = StateTypes.getByName(blockString);
                  if (type == null) {
                     for (Entry<String, String> stringEntry : STRING_UPDATER.entrySet()) {
                        blockString = blockString.replace(stringEntry.getKey(), stringEntry.getValue());
                     }
                     type = StateTypes.getByName(blockString);
                     if (type == null) {
                        PacketEvents.getAPI().getLogger().warning("Unknown block type: " + fullBlockString);
                     }
                  }

                  String[] data = null;
                  if (index != -1) {
                     data = fullBlockString.substring(index + 1, fullBlockString.length() - 1).split(",");
                  }

                  WrappedBlockState state = new WrappedBlockState(type, data, id, mappingsIndex);
                  if (isDefault) {
                     stateTypeToBlockStateMap.put(state.getType(), state);
                  }

                  stateByStringMap.put(fullBlockString, state);
                  stateByIdMap.put(id, state);
                  stateToStringMap.put(state, fullBlockString);
                  stateToIdMap.put(state, id);
                  ++id;
               }
            }
         } catch (Exception ex) {
            ex.printStackTrace();
         }

         BY_ID.put(mappingsIndex, stateByIdMap);
         INTO_ID.put(mappingsIndex, stateToIdMap);
         BY_STRING.put(mappingsIndex, stateByStringMap);
         INTO_STRING.put(mappingsIndex, stateToStringMap);
         DEFAULT_STATES.put(mappingsIndex, stateTypeToBlockStateMap);
      }
   }

   public WrappedBlockState clone() {
      return new WrappedBlockState(this.type, this.data, this.globalID, this.mappingsIndex);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof WrappedBlockState)) {
         return false;
      } else {
         WrappedBlockState that = (WrappedBlockState)o;
         return this.type == that.type && this.data.equals(that.data);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.data});
   }

   public StateType getType() {
      return this.type;
   }

   public int getAge() {
      return (Integer)this.data.get(StateValue.AGE);
   }

   public void setAge(int age) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.AGE, age);
      this.checkIsStillValid();
   }

   public boolean isAttached() {
      return (Boolean)this.data.get(StateValue.ATTACHED);
   }

   public void setAttached(boolean attached) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ATTACHED, attached);
      this.checkIsStillValid();
   }

   public Attachment getAttachment() {
      return (Attachment)this.data.get(StateValue.ATTACHMENT);
   }

   public void setAttachment(Attachment attachment) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ATTACHMENT, attachment);
      this.checkIsStillValid();
   }

   public Axis getAxis() {
      return (Axis)this.data.get(StateValue.AXIS);
   }

   public void setAxis(Axis axis) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.AXIS, axis);
      this.checkIsStillValid();
   }

   public boolean isBerries() {
      return (Boolean)this.data.get(StateValue.BERRIES);
   }

   public void setBerries(boolean berries) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.BERRIES, berries);
      this.checkIsStillValid();
   }

   public int getBites() {
      return (Integer)this.data.get(StateValue.BITES);
   }

   public void setBites(int bites) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.BITES, bites);
      this.checkIsStillValid();
   }

   public boolean isBottom() {
      return (Boolean)this.data.get(StateValue.BOTTOM);
   }

   public void setBottom(boolean bottom) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.BOTTOM, bottom);
      this.checkIsStillValid();
   }

   public int getCandles() {
      return (Integer)this.data.get(StateValue.CANDLES);
   }

   public void setCandles(int candles) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CANDLES, candles);
      this.checkIsStillValid();
   }

   public int getCharges() {
      return (Integer)this.data.get(StateValue.CHARGES);
   }

   public void setCharges(int charges) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CHARGES, charges);
      this.checkIsStillValid();
   }

   public boolean isConditional() {
      return (Boolean)this.data.get(StateValue.CONDITIONAL);
   }

   public void setConditional(boolean conditional) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CONDITIONAL, conditional);
      this.checkIsStillValid();
   }

   public int getDelay() {
      return (Integer)this.data.get(StateValue.DELAY);
   }

   public void setDelay(int delay) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DELAY, delay);
      this.checkIsStillValid();
   }

   public boolean isDisarmed() {
      return (Boolean)this.data.get(StateValue.DISARMED);
   }

   public void setDisarmed(boolean disarmed) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DISARMED, disarmed);
      this.checkIsStillValid();
   }

   public int getDistance() {
      return (Integer)this.data.get(StateValue.DISTANCE);
   }

   public void setDistance(int distance) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DISTANCE, distance);
      this.checkIsStillValid();
   }

   public boolean isDown() {
      return (Boolean)this.data.get(StateValue.DOWN);
   }

   public void setDown(boolean down) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DOWN, down);
      this.checkIsStillValid();
   }

   public boolean isDrag() {
      return (Boolean)this.data.get(StateValue.DRAG);
   }

   public void setDrag(boolean drag) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DRAG, drag);
      this.checkIsStillValid();
   }

   public int getEggs() {
      return (Integer)this.data.get(StateValue.EGGS);
   }

   public void setEggs(int eggs) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.EGGS, eggs);
      this.checkIsStillValid();
   }

   public boolean isEnabled() {
      return (Boolean)this.data.get(StateValue.ENABLED);
   }

   public void setEnabled(boolean enabled) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ENABLED, enabled);
      this.checkIsStillValid();
   }

   public boolean isExtended() {
      return (Boolean)this.data.get(StateValue.EXTENDED);
   }

   public void setExtended(boolean extended) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.EXTENDED, extended);
      this.checkIsStillValid();
   }

   public boolean isEye() {
      return (Boolean)this.data.get(StateValue.EYE);
   }

   public void setEye(boolean eye) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.EYE, eye);
      this.checkIsStillValid();
   }

   public Face getFace() {
      return (Face)this.data.get(StateValue.FACE);
   }

   public void setFace(Face face) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.FACE, face);
      this.checkIsStillValid();
   }

   public BlockFace getFacing() {
      return (BlockFace)this.data.get(StateValue.FACING);
   }

   public void setFacing(BlockFace facing) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.FACING, facing);
      this.checkIsStillValid();
   }

   public Half getHalf() {
      return (Half)this.data.get(StateValue.HALF);
   }

   public void setHalf(Half half) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HALF, half);
      this.checkIsStillValid();
   }

   public boolean isHanging() {
      return (Boolean)this.data.get(StateValue.HANGING);
   }

   public void setHanging(boolean hanging) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HANGING, hanging);
      this.checkIsStillValid();
   }

   public boolean isHasBook() {
      return (Boolean)this.data.get(StateValue.HAS_BOOK);
   }

   public void setHasBook(boolean hasBook) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HAS_BOOK, hasBook);
      this.checkIsStillValid();
   }

   public boolean isHasBottle0() {
      return (Boolean)this.data.get(StateValue.HAS_BOTTLE_0);
   }

   public void setHasBottle0(boolean hasBottle0) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HAS_BOTTLE_0, hasBottle0);
      this.checkIsStillValid();
   }

   public boolean isHasBottle1() {
      return (Boolean)this.data.get(StateValue.HAS_BOTTLE_1);
   }

   public void setHasBottle1(boolean hasBottle1) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HAS_BOTTLE_1, hasBottle1);
      this.checkIsStillValid();
   }

   public boolean isHasBottle2() {
      return (Boolean)this.data.get(StateValue.HAS_BOTTLE_2);
   }

   public void setHasBottle2(boolean hasBottle2) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HAS_BOTTLE_2, hasBottle2);
      this.checkIsStillValid();
   }

   public boolean isHasRecord() {
      return (Boolean)this.data.get(StateValue.HAS_RECORD);
   }

   public void setHasRecord(boolean hasRecord) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HAS_RECORD, hasRecord);
      this.checkIsStillValid();
   }

   public int getHatch() {
      return (Integer)this.data.get(StateValue.HATCH);
   }

   public void setHatch(int hatch) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HATCH, hatch);
      this.checkIsStillValid();
   }

   public Hinge getHinge() {
      return (Hinge)this.data.get(StateValue.HINGE);
   }

   public void setHinge(Hinge hinge) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HINGE, hinge);
      this.checkIsStillValid();
   }

   public int getHoneyLevel() {
      return (Integer)this.data.get(StateValue.HONEY_LEVEL);
   }

   public void setHoneyLevel(int honeyLevel) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HONEY_LEVEL, honeyLevel);
      this.checkIsStillValid();
   }

   public boolean isInWall() {
      return (Boolean)this.data.get(StateValue.IN_WALL);
   }

   public void setInWall(boolean inWall) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.IN_WALL, inWall);
      this.checkIsStillValid();
   }

   public Instrument getInstrument() {
      return (Instrument)this.data.get(StateValue.INSTRUMENT);
   }

   public void setInstrument(Instrument instrument) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.INSTRUMENT, instrument);
      this.checkIsStillValid();
   }

   public boolean isInverted() {
      return (Boolean)this.data.get(StateValue.INVERTED);
   }

   public void setInverted(boolean inverted) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.INVERTED, inverted);
      this.checkIsStillValid();
   }

   public int getLayers() {
      return (Integer)this.data.get(StateValue.LAYERS);
   }

   public void setLayers(int layers) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.LAYERS, layers);
      this.checkIsStillValid();
   }

   public Leaves getLeaves() {
      return (Leaves)this.data.get(StateValue.LEAVES);
   }

   public void setLeaves(Leaves leaves) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.LEAVES, leaves);
      this.checkIsStillValid();
   }

   public int getLevel() {
      return (Integer)this.data.get(StateValue.LEVEL);
   }

   public void setLevel(int level) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.LEVEL, level);
      this.checkIsStillValid();
   }

   public boolean isLit() {
      return (Boolean)this.data.get(StateValue.LIT);
   }

   public void setLit(boolean lit) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.LIT, lit);
      this.checkIsStillValid();
   }

   public boolean isLocked() {
      return (Boolean)this.data.get(StateValue.LOCKED);
   }

   public void setLocked(boolean locked) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.LOCKED, locked);
      this.checkIsStillValid();
   }

   public Mode getMode() {
      return (Mode)this.data.get(StateValue.MODE);
   }

   public void setMode(Mode mode) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.MODE, mode);
      this.checkIsStillValid();
   }

   public int getMoisture() {
      return (Integer)this.data.get(StateValue.MOISTURE);
   }

   public void setMoisture(int moisture) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.MOISTURE, moisture);
      this.checkIsStillValid();
   }

   public North getNorth() {
      return (North)this.data.get(StateValue.NORTH);
   }

   public void setNorth(North north) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.NORTH, north);
      this.checkIsStillValid();
   }

   public int getNote() {
      return (Integer)this.data.get(StateValue.NOTE);
   }

   public void setNote(int note) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.NOTE, note);
      this.checkIsStillValid();
   }

   public boolean isOccupied() {
      return (Boolean)this.data.get(StateValue.OCCUPIED);
   }

   public void setOccupied(boolean occupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.OCCUPIED, occupied);
      this.checkIsStillValid();
   }

   public boolean isShrieking() {
      return (Boolean)this.data.get(StateValue.SHRIEKING);
   }

   public void setShrieking(boolean shrieking) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SHRIEKING, shrieking);
      this.checkIsStillValid();
   }

   public boolean isCanSummon() {
      return (Boolean)this.data.get(StateValue.CAN_SUMMON);
   }

   public void setCanSummon(boolean canSummon) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CAN_SUMMON, canSummon);
      this.checkIsStillValid();
   }

   public boolean isOpen() {
      return (Boolean)this.data.get(StateValue.OPEN);
   }

   public void setOpen(boolean open) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.OPEN, open);
      this.checkIsStillValid();
   }

   public Orientation getOrientation() {
      return (Orientation)this.data.get(StateValue.ORIENTATION);
   }

   public void setOrientation(Orientation orientation) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ORIENTATION, orientation);
      this.checkIsStillValid();
   }

   public Part getPart() {
      return (Part)this.data.get(StateValue.PART);
   }

   public void setPart(Part part) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.PART, part);
      this.checkIsStillValid();
   }

   public boolean isPersistent() {
      return (Boolean)this.data.get(StateValue.PERSISTENT);
   }

   public void setPersistent(boolean persistent) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.PERSISTENT, persistent);
      this.checkIsStillValid();
   }

   public int getPickles() {
      return (Integer)this.data.get(StateValue.PICKLES);
   }

   public void setPickles(int pickles) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.PICKLES, pickles);
      this.checkIsStillValid();
   }

   public int getPower() {
      return (Integer)this.data.get(StateValue.POWER);
   }

   public void setPower(int power) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.POWER, power);
      this.checkIsStillValid();
   }

   public boolean isPowered() {
      return (Boolean)this.data.get(StateValue.POWERED);
   }

   public void setPowered(boolean powered) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.POWERED, powered);
      this.checkIsStillValid();
   }

   public int getRotation() {
      return (Integer)this.data.get(StateValue.ROTATION);
   }

   public void setRotation(int rotation) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ROTATION, rotation);
      this.checkIsStillValid();
   }

   public SculkSensorPhase getSculkSensorPhase() {
      return (SculkSensorPhase)this.data.get(StateValue.SCULK_SENSOR_PHASE);
   }

   public void setSculkSensorPhase(SculkSensorPhase sculkSensorPhase) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SCULK_SENSOR_PHASE, sculkSensorPhase);
      this.checkIsStillValid();
   }

   public Shape getShape() {
      return (Shape)this.data.get(StateValue.SHAPE);
   }

   public void setShape(Shape shape) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SHAPE, shape);
      this.checkIsStillValid();
   }

   public boolean isShort() {
      return (Boolean)this.data.get(StateValue.SHORT);
   }

   public void setShort(boolean short_) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SHORT, short_);
      this.checkIsStillValid();
   }

   public boolean isSignalFire() {
      return (Boolean)this.data.get(StateValue.SIGNAL_FIRE);
   }

   public void setSignalFire(boolean signalFire) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SIGNAL_FIRE, signalFire);
      this.checkIsStillValid();
   }

   public boolean isSlotZeroOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_0_OCCUPIED);
   }

   public void setSlotZeroOccupied(boolean slotZeroOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_0_OCCUPIED, slotZeroOccupied);
      this.checkIsStillValid();
   }

   public boolean isSlotOneOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_1_OCCUPIED);
   }

   public void setSlotOneOccupied(boolean slotOneOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_1_OCCUPIED, slotOneOccupied);
      this.checkIsStillValid();
   }

   public boolean isSlotTwoOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_2_OCCUPIED);
   }

   public void setSlotTwoOccupied(boolean slotTwoOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_2_OCCUPIED, slotTwoOccupied);
      this.checkIsStillValid();
   }

   public boolean isSlotThreeOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_3_OCCUPIED);
   }

   public void setSlotThreeOccupied(boolean slotThreeOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_3_OCCUPIED, slotThreeOccupied);
      this.checkIsStillValid();
   }

   public boolean isSlotFourOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_4_OCCUPIED);
   }

   public void setSlotFourOccupied(boolean slotFourOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_4_OCCUPIED, slotFourOccupied);
      this.checkIsStillValid();
   }

   public boolean isSlotFiveOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_5_OCCUPIED);
   }

   public void setSlotFiveOccupied(boolean slotFiveOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_5_OCCUPIED, slotFiveOccupied);
      this.checkIsStillValid();
   }

   public boolean isSnowy() {
      return (Boolean)this.data.get(StateValue.SNOWY);
   }

   public void setSnowy(boolean snowy) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SNOWY, snowy);
      this.checkIsStillValid();
   }

   public int getStage() {
      return (Integer)this.data.get(StateValue.STAGE);
   }

   public void setStage(int stage) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.STAGE, stage);
      this.checkIsStillValid();
   }

   public South getSouth() {
      return (South)this.data.get(StateValue.SOUTH);
   }

   public void setSouth(South south) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SOUTH, south);
      this.checkIsStillValid();
   }

   public Thickness getThickness() {
      return (Thickness)this.data.get(StateValue.THICKNESS);
   }

   public void setThickness(Thickness thickness) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.THICKNESS, thickness);
      this.checkIsStillValid();
   }

   public Tilt getTilt() {
      return (Tilt)this.data.get(StateValue.TILT);
   }

   public void setTilt(Tilt tilt) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.TILT, tilt);
      this.checkIsStillValid();
   }

   public boolean isTriggered() {
      return (Boolean)this.data.get(StateValue.TRIGGERED);
   }

   public void setTriggered(boolean triggered) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.TRIGGERED, triggered);
      this.checkIsStillValid();
   }

   public Type getTypeData() {
      return (Type)this.data.get(StateValue.TYPE);
   }

   public void setTypeData(Type type) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.TYPE, type);
      this.checkIsStillValid();
   }

   public boolean isUnstable() {
      return (Boolean)this.data.get(StateValue.UNSTABLE);
   }

   public void setUnstable(boolean unstable) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.UNSTABLE, unstable);
      this.checkIsStillValid();
   }

   public boolean isUp() {
      return (Boolean)this.data.get(StateValue.UP);
   }

   public void setUp(boolean up) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.UP, up);
      this.checkIsStillValid();
   }

   public VerticalDirection getVerticalDirection() {
      return (VerticalDirection)this.data.get(StateValue.VERTICAL_DIRECTION);
   }

   public void setVerticalDirection(VerticalDirection verticalDirection) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.VERTICAL_DIRECTION, verticalDirection);
      this.checkIsStillValid();
   }

   public boolean isWaterlogged() {
      return (Boolean)this.data.get(StateValue.WATERLOGGED);
   }

   public void setWaterlogged(boolean waterlogged) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.WATERLOGGED, waterlogged);
      this.checkIsStillValid();
   }

   public East getEast() {
      return (East)this.data.get(StateValue.EAST);
   }

   public void setEast(East west) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.EAST, west);
      this.checkIsStillValid();
   }

   public West getWest() {
      return (West)this.data.get(StateValue.WEST);
   }

   public void setWest(West west) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.WEST, west);
      this.checkIsStillValid();
   }

   public Bloom getBloom() {
      return (Bloom)this.data.get(StateValue.BLOOM);
   }

   public void setBloom(Bloom bloom) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.BLOOM, bloom);
      this.checkIsStillValid();
   }

   public boolean isCracked() {
      return (Boolean)this.data.get(StateValue.CRACKED);
   }

   public void setCracked(boolean cracked) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CRACKED, cracked);
      this.checkIsStillValid();
   }

   public boolean isCrafting() {
      return (Boolean)this.data.get(StateValue.CRAFTING);
   }

   public void setCrafting(boolean crafting) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CRAFTING, crafting);
      this.checkIsStillValid();
   }

   public TrialSpawnerState getTrialSpawnerState() {
      return (TrialSpawnerState)this.data.get(StateValue.TRIAL_SPAWNER_STATE);
   }

   public void setTrialSpawnerState(TrialSpawnerState trialSpawnerState) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.TRIAL_SPAWNER_STATE, trialSpawnerState);
      this.checkIsStillValid();
   }

   private void checkIfCloneNeeded() {
      if (!this.hasClonedData) {
         this.data = new HashMap(this.data);
         this.hasClonedData = true;
      }

   }

   private void checkIsStillValid() {
      int oldGlobalID = this.globalID;
      this.globalID = this.getGlobalIdNoCache();
      if (this.globalID == -1) {
         WrappedBlockState blockState = ((WrappedBlockState)((Map)BY_ID.get(this.mappingsIndex)).getOrDefault(oldGlobalID, AIR)).clone();
         this.type = blockState.type;
         this.globalID = blockState.globalID;
         this.data = new HashMap(blockState.data);
         if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            PacketEvents.getAPI().getLogManager().warn("Attempt to modify an unknown property for this game version and block!");
            PacketEvents.getAPI().getLogManager().warn("Block: " + this.type.getName());
            for (Entry<StateValue, Object> entry : this.data.entrySet()) {
               PacketEvents.getAPI().getLogManager().warn(entry.getKey() + ": " + entry.getValue());
            }
            (new IllegalStateException("An invalid modification was made to a block!")).printStackTrace();
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public Map<StateValue, Object> getInternalData() {
      return this.data;
   }

   public int getGlobalId() {
      return this.globalID;
   }

   private int getGlobalIdNoCache() {
      return (Integer)((Map)INTO_ID.get(this.mappingsIndex)).getOrDefault(this, -1);
   }

   public String toString() {
      return (String)((Map)INTO_STRING.get(this.mappingsIndex)).get(this);
   }

   static {
      AIR = new WrappedBlockState(StateTypes.AIR, new EnumMap(StateValue.class), 0, (byte)0);
      BY_STRING = new HashMap();
      BY_ID = new HashMap();
      INTO_STRING = new HashMap();
      INTO_ID = new HashMap();
      DEFAULT_STATES = new HashMap();
      STRING_UPDATER = new HashMap();
      cache = new HashMap(4845, 70.0F);
      STRING_UPDATER.put("grass_path", "dirt_path");
      loadLegacy();
      for (ClientVersion version : ClientVersion.values()) {
         if (version.isNewerThanOrEquals(ClientVersion.V_1_13) && version.isRelease()) {
            loadModern(version);
         }
      }
      cache = null;
   }
}
