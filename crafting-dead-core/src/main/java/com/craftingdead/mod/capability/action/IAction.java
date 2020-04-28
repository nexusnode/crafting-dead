package com.craftingdead.mod.capability.action;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.ITextComponent;

public interface IAction {

  boolean isActive(ClientPlayerEntity playerEntity);

  ITextComponent getText(ClientPlayerEntity playerEntity);

  float getProgress(ClientPlayerEntity playerEntity);
}
