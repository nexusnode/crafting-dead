package com.craftingdead.core.world.action;

import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class AbstractActionType<T extends Action> extends ForgeRegistryEntry<ActionType<?>>
    implements ActionType<T> {}
