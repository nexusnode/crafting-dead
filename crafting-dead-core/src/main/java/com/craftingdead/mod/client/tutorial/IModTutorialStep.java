package com.craftingdead.mod.client.tutorial;

import net.minecraft.client.tutorial.ITutorialStep;

public interface IModTutorialStep extends ITutorialStep {

  default void openModInventory() {}
}
