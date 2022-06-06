package sm0keysa1m0n.bliss.view.event;

import net.minecraftforge.eventbus.api.Event;

public class KeyEvent extends Event {

  private final int key;
  private final int scancode;
  private final int action;
  private final int mods;

  public KeyEvent(int key, int scancode, int action, int mods) {
    this.key = key;
    this.scancode = scancode;
    this.action = action;
    this.mods = mods;
  }

  public int getKey() {
    return this.key;
  }

  public int getScancode() {
    return this.scancode;
  }

  public int getAction() {
    return this.action;
  }

  public int getMods() {
    return this.mods;
  }
}

