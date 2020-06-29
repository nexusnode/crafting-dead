package com.craftingdead.core.client.gui.screen.inventory;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.client.gui.SimpleButton;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.inventory.container.ModInventoryContainer;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.main.OpenStorageMessage;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ModInventoryScreen extends DisplayEffectsScreen<ModInventoryContainer> {

  private static final ResourceLocation INVENTORY_BACKGROUND =
      new ResourceLocation(CraftingDead.ID, "textures/gui/container/inventory.png");

  private int oldMouseX;
  private int oldMouseY;

  private Button vestButton;

  private boolean transitioning = false;

  public ModInventoryScreen(ModInventoryContainer container, PlayerInventory playerInventory,
      ITextComponent title) {
    super(container, playerInventory, title);
    this.ySize = 186;
  }

  @Override
  public void init() {
    super.init();
    this.vestButton =
        new SimpleButton(this.guiLeft + 83, this.guiTop + 66, 10, 16, ">", (button) -> {
          NetworkChannel.MAIN
              .getSimpleChannel()
              .sendToServer(new OpenStorageMessage(InventorySlotType.VEST));
          this.transitioning = true;
        });
    this.addButton(this.vestButton);
    this.refreshButtonStatus();
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
    this.oldMouseX = mouseX;
    this.oldMouseY = mouseY;
  }

  @Override
  public void tick() {
    super.tick();
    this.refreshButtonStatus();
  }

  private void refreshButtonStatus() {
    this.vestButton.active = this.container
        .getItemHandler()
        .getStackInSlot(InventorySlotType.VEST.getIndex())
        .getCapability(ModCapabilities.STORAGE)
        .isPresent();
  }

  public boolean isTransitioning() {
    return this.transitioning;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.renderBackground();
    this.minecraft.getTextureManager().bindTexture(INVENTORY_BACKGROUND);

    this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

    ItemStack gunStack = this.container.getGunStack();
    gunStack.getCapability(ModCapabilities.GUN).ifPresent(gunController -> {

      this.blit(this.guiLeft + 122, this.guiTop + 45, 176, 0, 22, 22);

      if ((!this.container.isCraftingInventoryEmpty() && this.container.isCraftable())
          || gunController.isAcceptedPaintOrAttachment(this.playerInventory.getItemStack())) {
        this.blit(this.guiLeft + 122, this.guiTop + 45, 176, 22, 22, 22);
      } else if (!this.container.isCraftingInventoryEmpty()) {
        this.blit(this.guiLeft + 122, this.guiTop + 45, 176, 44, 22, 22);
      }
    });

    InventoryScreen
        .drawEntity(this.guiLeft + 33, this.guiTop + 97, 30, (this.guiLeft + 51) - this.oldMouseX,
            (this.guiTop + 75 - 50) - this.oldMouseY, this.minecraft.player);
  }
}
