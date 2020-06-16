package com.craftingdead.core.client.tutorial;

import net.minecraft.client.tutorial.ITutorialStep;

public interface IModTutorialStep extends ITutorialStep {

  default void openModInventory() {}
}
