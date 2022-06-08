package sm0keysa1m0n.bliss.style;

import java.util.OptionalInt;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;

/**
 * Holds all state representing pseudo-classes and maps them to a bit value.
 * 
 * @author Sm0keySa1m0n
 */
public class States {

  private static final int NILL = -1;

  private static final Object2IntMap<String> states =
      Util.make(new Object2IntOpenHashMap<>(), map -> map.defaultReturnValue(NILL));

  public static final int ENABLED = register("enabled");
  public static final int DISABLED = register("disabled");
  public static final int HOVER = register("hover");
  public static final int FOCUS = register("focus");
  public static final int FOCUS_VISIBLE = register("focus-visible");
  public static final int CHECKED = register("checked");

  private static int n = 0;

  private static int register(String name) {
    var value = 1 << n++;
    if (states.put(name, value) != NILL) {
      throw new IllegalStateException("Duplicate state: " + name);
    }
    return value;
  }

  public static OptionalInt get(String name) {
    var value = states.getInt(name);
    return value == NILL ? OptionalInt.empty() : OptionalInt.of(value);
  }

  public static int combine(int... states) {
    var combined = 0;
    for (int i = 0; i < states.length; i++) {
      combined |= states[i];
    }
    return combined;
  }

  public static IntSet split(int state) {
    var splitStates = new IntOpenHashSet();
    states.values().forEach(value -> {
      if ((state & value) == value) {
        splitStates.add(value);
      }
    });
    return splitStates;
  }
}
