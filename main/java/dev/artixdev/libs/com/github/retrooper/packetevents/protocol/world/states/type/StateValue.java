package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.type;

import java.util.HashMap;
import java.util.function.Function;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.BlockFace;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Attachment;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
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

public enum StateValue {
   AGE("age", Integer::parseInt),
   ATTACHED("attached", Boolean::parseBoolean),
   ATTACHMENT("attachment", Attachment::valueOf),
   AXIS("axis", Axis::valueOf),
   BERRIES("berries", Boolean::parseBoolean),
   BITES("bites", Integer::parseInt),
   BLOOM("bloom", Boolean::parseBoolean),
   BOTTOM("bottom", Boolean::parseBoolean),
   CANDLES("candles", Integer::parseInt),
   CAN_SUMMON("can_summon", Boolean::parseBoolean),
   CHARGES("charges", Integer::parseInt),
   CONDITIONAL("conditional", Boolean::parseBoolean),
   DELAY("delay", Integer::parseInt),
   DISARMED("disarmed", Boolean::parseBoolean),
   DISTANCE("distance", Integer::parseInt),
   DOWN("down", Boolean::parseBoolean),
   DRAG("drag", Boolean::parseBoolean),
   DUSTED("dusted", Integer::parseInt),
   EAST("east", East::valueOf),
   EGGS("eggs", Integer::parseInt),
   ENABLED("enabled", Boolean::parseBoolean),
   EXTENDED("extended", Boolean::parseBoolean),
   EYE("eye", Boolean::parseBoolean),
   FACE("face", Face::valueOf),
   FACING("facing", BlockFace::valueOf),
   FLOWER_AMOUNT("flower_amount", Integer::parseInt),
   HALF("half", Half::valueOf),
   HANGING("hanging", Boolean::parseBoolean),
   HAS_BOOK("has_book", Boolean::parseBoolean),
   HAS_BOTTLE_0("has_bottle_0", Boolean::parseBoolean),
   HAS_BOTTLE_1("has_bottle_1", Boolean::parseBoolean),
   HAS_BOTTLE_2("has_bottle_2", Boolean::parseBoolean),
   HAS_RECORD("has_record", Boolean::parseBoolean),
   HATCH("hatch", Integer::parseInt),
   HINGE("hinge", Hinge::valueOf),
   HONEY_LEVEL("honey_level", Integer::parseInt),
   IN_WALL("in_wall", Boolean::parseBoolean),
   INSTRUMENT("instrument", Instrument::valueOf),
   INVERTED("inverted", Boolean::parseBoolean),
   LAYERS("layers", Integer::parseInt),
   LEAVES("leaves", Leaves::valueOf),
   LEVEL("level", Integer::parseInt),
   LIT("lit", Boolean::parseBoolean),
   LOCKED("locked", Boolean::parseBoolean),
   MODE("mode", Mode::valueOf),
   MOISTURE("moisture", Integer::parseInt),
   NORTH("north", North::valueOf),
   NOTE("note", Integer::parseInt),
   OCCUPIED("occupied", Boolean::parseBoolean),
   OPEN("open", Boolean::parseBoolean),
   ORIENTATION("orientation", Orientation::valueOf),
   PART("part", Part::valueOf),
   PERSISTENT("persistent", Boolean::parseBoolean),
   PICKLES("pickles", Integer::parseInt),
   POWER("power", Integer::parseInt),
   POWERED("powered", Boolean::parseBoolean),
   ROTATION("rotation", Integer::parseInt),
   SCULK_SENSOR_PHASE("sculk_sensor_phase", SculkSensorPhase::valueOf),
   SHAPE("shape", Shape::valueOf),
   SHORT("short", Boolean::parseBoolean),
   SHRIEKING("shrieking", Boolean::parseBoolean),
   SIGNAL_FIRE("signal_fire", Boolean::parseBoolean),
   SLOT_0_OCCUPIED("slot_0_occupied", Boolean::parseBoolean),
   SLOT_1_OCCUPIED("slot_1_occupied", Boolean::parseBoolean),
   SLOT_2_OCCUPIED("slot_2_occupied", Boolean::parseBoolean),
   SLOT_3_OCCUPIED("slot_3_occupied", Boolean::parseBoolean),
   SLOT_4_OCCUPIED("slot_4_occupied", Boolean::parseBoolean),
   SLOT_5_OCCUPIED("slot_5_occupied", Boolean::parseBoolean),
   SNOWY("snowy", Boolean::parseBoolean),
   STAGE("stage", Integer::parseInt),
   SOUTH("south", South::valueOf),
   THICKNESS("thickness", Thickness::valueOf),
   TILT("tilt", Tilt::valueOf),
   TRIGGERED("triggered", Boolean::parseBoolean),
   TYPE("type", Type::valueOf),
   UNSTABLE("unstable", Boolean::parseBoolean),
   UP("up", Boolean::parseBoolean),
   VERTICAL_DIRECTION("vertical_direction", VerticalDirection::valueOf),
   WATERLOGGED("waterlogged", Boolean::parseBoolean),
   WEST("west", West::valueOf),
   CRACKED("cracked", Boolean::parseBoolean),
   CRAFTING("crafting", Boolean::parseBoolean),
   TRIAL_SPAWNER_STATE("trial_spawner_state", TrialSpawnerState::valueOf);

   private final String name;
   private final Function<String, Object> parser;
   private static final HashMap<String, StateValue> values = new HashMap();

   private StateValue(String name, Function<String, Object> parser) {
      this.name = name;
      this.parser = parser;
   }

   public static StateValue byName(String name) {
      return (StateValue)values.get(name);
   }

   public String getName() {
      return this.name;
   }

   public Function<String, Object> getParser() {
      return this.parser;
   }

   // $FF: synthetic method
   private static StateValue[] $values() {
      return new StateValue[]{AGE, ATTACHED, ATTACHMENT, AXIS, BERRIES, BITES, BLOOM, BOTTOM, CANDLES, CAN_SUMMON, CHARGES, CONDITIONAL, DELAY, DISARMED, DISTANCE, DOWN, DRAG, DUSTED, EAST, EGGS, ENABLED, EXTENDED, EYE, FACE, FACING, FLOWER_AMOUNT, HALF, HANGING, HAS_BOOK, HAS_BOTTLE_0, HAS_BOTTLE_1, HAS_BOTTLE_2, HAS_RECORD, HATCH, HINGE, HONEY_LEVEL, IN_WALL, INSTRUMENT, INVERTED, LAYERS, LEAVES, LEVEL, LIT, LOCKED, MODE, MOISTURE, NORTH, NOTE, OCCUPIED, OPEN, ORIENTATION, PART, PERSISTENT, PICKLES, POWER, POWERED, ROTATION, SCULK_SENSOR_PHASE, SHAPE, SHORT, SHRIEKING, SIGNAL_FIRE, SLOT_0_OCCUPIED, SLOT_1_OCCUPIED, SLOT_2_OCCUPIED, SLOT_3_OCCUPIED, SLOT_4_OCCUPIED, SLOT_5_OCCUPIED, SNOWY, STAGE, SOUTH, THICKNESS, TILT, TRIGGERED, TYPE, UNSTABLE, UP, VERTICAL_DIRECTION, WATERLOGGED, WEST, CRACKED, CRAFTING, TRIAL_SPAWNER_STATE};
   }

   static {
      StateValue[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         StateValue value = var0[var2];
         values.put(value.name, value);
      }

   }
}
