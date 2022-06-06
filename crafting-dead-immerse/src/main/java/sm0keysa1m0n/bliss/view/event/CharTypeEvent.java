package sm0keysa1m0n.bliss.view.event;

import net.minecraftforge.eventbus.api.Event;

public class CharTypeEvent extends Event {

  private final char character;
  private final int mods;

  public CharTypeEvent(char character, int mods) {
    this.character = character;
    this.mods = mods;
  }

  public char getCharacter() {
    return this.character;
  }

  public int getMods() {
    return this.mods;
  }
}
