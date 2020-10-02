/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.gui.component;

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
