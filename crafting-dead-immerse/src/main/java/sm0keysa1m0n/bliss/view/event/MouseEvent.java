package sm0keysa1m0n.bliss.view.event;

import org.lwjgl.glfw.GLFW;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class MouseEvent extends Event {

  private final double mouseX;
  private final double mouseY;

  public MouseEvent(double mouseX, double mouseY) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
  }

  public double getMouseX() {
    return this.mouseX;
  }

  public double getMouseY() {
    return this.mouseY;
  }

  @Cancelable
  public static class MoveEvent extends MouseEvent {

    public MoveEvent(double mouseX, double mouseY) {
      super(mouseX, mouseY);
    }
  }

  @Cancelable
  public static class ButtonEvent extends MouseEvent {

    private final int button;
    private final int action;

    public ButtonEvent(double mouseX, double mouseY, int button, int action) {
      super(mouseX, mouseY);
      this.button = button;
      this.action = action;
    }


    /**
     * The mouse button that triggered this event.
     * https://www.glfw.org/docs/latest/group__buttons.html
     *
     * @see GLFW mouse constants starting with "GLFW_MOUSE_BUTTON_"
     */
    public int getButton() {
      return this.button;
    }

    /**
     * Integer representing the mouse button's action.
     *
     * @see GLFW#GLFW_PRESS
     * @see GLFW#GLFW_RELEASE
     */
    public int getAction() {
      return this.action;
    }
  }

  @Cancelable
  public static class DragEvent extends MouseEvent {

    private final int button;
    private final double deltaX;
    private final double deltaY;

    public DragEvent(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      super(mouseX, mouseY);
      this.button = button;
      this.deltaX = deltaX;
      this.deltaY = deltaY;
    }

    public int getButton() {
      return this.button;
    }

    public double getDeltaX() {
      return this.deltaX;
    }

    public double getDeltaY() {
      return this.deltaY;
    }
  }

  @Cancelable
  public static class ScrollEvent extends MouseEvent {

    private final double scrollDelta;

    public ScrollEvent(double mouseX, double mouseY, double scrollDelta) {
      super(mouseX, mouseY);
      this.scrollDelta = scrollDelta;
    }

    public double getScrollDelta() {
      return this.scrollDelta;
    }
  }
}
