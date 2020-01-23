package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.animation.IAnimationController;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.gui.IAction;
import com.craftingdead.mod.client.gui.IngameGui;
import com.craftingdead.mod.network.NetworkChannel;
import com.craftingdead.mod.network.message.main.PlayerActionMessage;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ClientPlayer extends DefaultPlayer<ClientPlayerEntity> {

  public ClientPlayer(ClientPlayerEntity entity) {
    super(entity);
  }

  public void updateMetadata(int daysSurvived, int zombiesKilled, int playersKilled, int water,
      int maxWater, int stamina, int maxStamina) {
    this.daysSurvived = daysSurvived;
    this.zombiesKilled = zombiesKilled;
    this.playersKilled = playersKilled;
    this.water = water;
    this.maxWater = maxWater;
    this.stamina = stamina;
    this.maxStamina = maxStamina;
  }

  @Override
  public void tick() {
    super.tick();
    this.entity
        .getHeldItemMainhand()
        .getCapability(ModCapabilities.ANIMATION_CONTROLLER)
        .ifPresent(IAnimationController::tick);

    if (ClientDist.CROUCH.isKeyDown()) {
      this.entity.setPose(Pose.SWIMMING);
    }

    if (ClientDist.RELOAD.isKeyDown() && !this.isReloading()) {
      this.reload(true);
    }
  }

  @Override
  public void setTriggerPressed(boolean triggerPressed, boolean sendUpdate) {
    super.setTriggerPressed(triggerPressed, sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .sendToServer(
              new PlayerActionMessage(0, triggerPressed ? PlayerActionMessage.Action.TRIGGER_PRESSED
                  : PlayerActionMessage.Action.TRIGGER_RELEASED));
    }
  }

  @Override
  public void toggleAiming(boolean sendUpdate) {
    super.toggleAiming(sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .sendToServer(new PlayerActionMessage(0, PlayerActionMessage.Action.RELOAD));
    }
  }

  @Override
  public void reload() {
    super.reload();
    IngameGui ingameGui = ((ClientDist) CraftingDead.getInstance().getModDist()).getIngameGui();
    ingameGui.setAction(new ReloadAction());
  }

  @Override
  public void reload(boolean sendUpdate) {
    super.reload(sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .sendToServer(new PlayerActionMessage(0, PlayerActionMessage.Action.RELOAD));
    }
  }

  private class ReloadAction implements IAction {

    @Override
    public boolean isActive() {
      return ClientPlayer.this.isReloading();
    }

    @Override
    public ITextComponent getText() {
      return new TranslationTextComponent("action.reload");
    }

    @Override
    public float getProgress() {
      return (float) (ClientPlayer.this.totalReloadDurationTicks
          - ClientPlayer.this.reloadDurationTicks) / ClientPlayer.this.totalReloadDurationTicks;
    }
  }
}
