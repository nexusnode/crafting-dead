package com.craftingdead.mod.client.tutorial;

import com.craftingdead.mod.client.ClientDist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class OpenModInventoryStep implements IModTutorialStep {
  private static final ITextComponent TITLE =
      new TranslationTextComponent("tutorial.craftingdead.open_inventory.title");
  private static final ITextComponent DESCRIPTION =
      new TranslationTextComponent("tutorial.open_inventory.description",
          Tutorial.createKeybindComponent("craftingdead.inventory"));

  private final ClientDist client;
  private TutorialToast toast;
  private int timeWaiting;

  public OpenModInventoryStep(ClientDist client) {
    this.client = client;
  }

  @Override
  public void tick() {
    ++this.timeWaiting;
    if (this.timeWaiting >= 600 && this.toast == null) {
      this.toast = new TutorialToast(TutorialToast.Icons.RECIPE_BOOK, TITLE, DESCRIPTION, false);
      Minecraft.getInstance().getToastGui().add(this.toast);
    }
  }

  @Override
  public void onStop() {
    if (this.toast != null) {
      this.toast.hide();
      this.toast = null;
    }
  }

  @Override
  public void openModInventory() {
    this.client.setTutorialStep(ModTutorialSteps.NONE);
  }
}
