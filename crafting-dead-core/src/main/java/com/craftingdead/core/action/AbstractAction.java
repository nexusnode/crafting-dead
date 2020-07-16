package com.craftingdead.core.action;

import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class AbstractAction implements IAction {

  private final ActionType<?> actionType;
  protected final ILiving<?> performer;
  @Nullable
  protected ILiving<?> target;

  public AbstractAction(ActionType<?> actionType, ILiving<?> performer,
      @Nullable ILiving<?> target) {
    this.actionType = actionType;
    this.performer = performer;
    this.target = target;
  }

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
    return new ActionProgress(true);
  }

  @Override
  public Optional<ILiving<?>> getTarget() {
    return Optional.ofNullable(this.target);
  }

  @Override
  public ILiving.IActionProgress getTargetProgress() {
    return new ActionProgress(false);
  }

  protected String getTranslationKey() {
    return Util.makeTranslationKey("action", AbstractAction.this.actionType.getRegistryName());
  }

  protected abstract float getProgress(float partialTicks);

  private class ActionProgress implements ILiving.IActionProgress {

    private final boolean performer;

    public ActionProgress(boolean performer) {
      this.performer = performer;
    }

    @Override
    public ITextComponent getMessage() {
      return new TranslationTextComponent(AbstractAction.this.getTranslationKey() + ".message");
    }

    @Override
    public ITextComponent getSubMessage() {
      return this.performer
          ? AbstractAction.this.target == null ? null
              : new TranslationTextComponent("action.target",
                  AbstractAction.this.target.getEntity().getDisplayName().getFormattedText())
          : new TranslationTextComponent("action.performer",
              AbstractAction.this.performer.getEntity().getDisplayName().getFormattedText());
    }

    @Override
    public float getProgress(float partialTicks) {
      return AbstractAction.this.getProgress(partialTicks);
    }

    @Override
    public void stop() {
      AbstractAction.this.getPerformer().cancelAction(true);
    }
  }
}
