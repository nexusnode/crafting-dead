package com.craftingdead.immerse.client.gui.component;

import net.minecraft.client.gui.INestedGuiEventHandler;

public interface IParentView extends IView, INestedGuiEventHandler {

  @Override
  default void mouseMoved(double mouseX, double mouseY) {
    this.getEventListeners().forEach(listener -> listener.mouseMoved(mouseX, mouseY));
  }

  @Override
  default boolean isMouseOver(double mouseX, double mouseY) {
    return INestedGuiEventHandler.super.isMouseOver(mouseX, mouseY)
        || this.getEventListeners().stream()
            .anyMatch(listener -> listener.isMouseOver(mouseX, mouseY));
  }
}
