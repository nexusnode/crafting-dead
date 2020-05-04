package com.craftingdead.mod.client.gui.screen.inventory;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.container.ModPlayerContainer;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ModInventoryScreen extends DisplayEffectsScreen<ModPlayerContainer> {

  private static final ResourceLocation INVENTORY_BACKGROUND =
      new ResourceLocation(CraftingDead.ID, "textures/gui/container/inventory.png");

  private int oldMouseX;
  private int oldMouseY;

  public ModInventoryScreen(PlayerInventory playerInventory) {
    this(new ModPlayerContainer(0, playerInventory), playerInventory,
        new TranslationTextComponent("container.crafting"));
    playerInventory.player.openContainer = this.container;
  }

  public ModInventoryScreen(ModPlayerContainer container, PlayerInventory playerInventory,
      ITextComponent title) {
    super(container, playerInventory, title);
    this.ySize = 186;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
    this.oldMouseX = mouseX;
    this.oldMouseY = mouseY;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
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
