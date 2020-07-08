package com.craftingdead.core.action;

import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class AbstractAction implements IAction {

  private final ActionType<?> actionType;
  protected final ILiving<?> performer;
  @Nullable
  protected ILiving<?> target;

  private final String performerBaseTranslationKey;
  private final String targetBaseTranslationKey;

  public AbstractAction(ActionType<?> actionType, ILiving<?> performer,
      @Nullable ILiving<?> target) {
    this.actionType = actionType;
    this.performer = performer;
    this.target = target;

    this.performerBaseTranslationKey =
        this.getTranslationKey() + ".performer" + (this.target == null ? ".self" : ".targeted");
    this.targetBaseTranslationKey = this.getTranslationKey() + ".target";
  }

  @Override
  public void writeData(PacketBuffer packetBuffer) {}

  @Override
  public void readData(PacketBuffer packetBuffer) {}

  @Override
  public ActionType<?> getActionType() {
    return this.actionType;
  }

  @Override
  public ILiving<?> getPerformer() {
    return this.performer;
  }

  @Override
  public ILiving.IActionProgress getPerformerProgress() {
    return new ActionProgress(this.performerBaseTranslationKey);
  }

  @Override
  public Optional<ILiving<?>> getTarget() {
    return Optional.ofNullable(this.target);
  }

  @Override
  public ILiving.IActionProgress getTargetProgress() {
    return new ActionProgress(this.targetBaseTranslationKey);
  }

  protected String getTranslationKey() {
    return Util.makeTranslationKey("action", AbstractAction.this.actionType.getRegistryName());
  }

  protected abstract float getProgress(float partialTicks);

  private class ActionProgress implements ILiving.IActionProgress {

    private final String baseTranslationKey;

    public ActionProgress(String baseTranslationKey) {
      this.baseTranslationKey = baseTranslationKey;
    }

    @Override
    public ITextComponent getMessage() {
      return this.makeTextComponent(this.baseTranslationKey + ".message");
    }

    @Override
    public ITextComponent getSubMessage() {
      return this.makeTextComponent(this.baseTranslationKey + ".sub_message");
    }

    @Override
    public float getProgress(float partialTicks) {
      return AbstractAction.this.getProgress(partialTicks);
    }

    @Override
    public void stop() {
      AbstractAction.this.getPerformer().cancelAction(true);
    }

    private ITextComponent makeTextComponent(String translationKey) {
      return new TranslationTextComponent(translationKey,
          AbstractAction.this.performer.getEntity().getDisplayName().getFormattedText(),
          AbstractAction.this.target == null ? null
              : AbstractAction.this.target.getEntity().getDisplayName().getFormattedText());
    }
  }
}
