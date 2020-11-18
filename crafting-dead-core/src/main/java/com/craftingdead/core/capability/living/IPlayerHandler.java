package com.craftingdead.core.capability.living;

public interface IPlayerHandler extends ILivingHandler {

  void playerTick();

  void copyFrom(IPlayer<?> that, boolean wasDeath);
}
