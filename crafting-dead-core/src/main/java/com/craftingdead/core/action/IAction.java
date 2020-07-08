package com.craftingdead.core.action;

import java.util.Optional;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.network.PacketBuffer;

public interface IAction {

  /**
   * A prerequisite check to verify if this action is able to start.
   * 
   * @return if the action can start
   */
  boolean start();

  /**
   * Ticks the {@link IAction} and determines if it should continue running.
   * 
   * @return if the action is finished
   */
  boolean tick();

  void cancel();

  void writeData(PacketBuffer packetBuffer);

  void readData(PacketBuffer packetBuffer);

  ILiving<?> getPerformer();

  ILiving.IActionProgress getPerformerProgress();

  Optional<ILiving<?>> getTarget();

  ILiving.IActionProgress getTargetProgress();

  ActionType<?> getActionType();
}
