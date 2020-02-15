package com.craftingdead.mod.client.gui.screen.inventory;

import java.util.function.Supplier;
import java.util.stream.Stream;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.inventory.CraftingInventorySlotType;
import com.craftingdead.mod.inventory.container.ModPlayerContainer;
import com.craftingdead.mod.item.GunItem;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ModInventoryScreen extends DisplayEffectsScreen<ModPlayerContainer> {

  private static final ResourceLocation INVENTORY_BACKGROUND =
      new ResourceLocation(CraftingDead.ID, "textures/gui/container/inventory.png");

  private int oldMouseX;
  private int oldMouseY;

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

    if (this.playerInventory.player.isCreative()) {
      this.blit(this.guiLeft + 58, this.guiTop - 27, 0, this.ySize, 28, 30);
    }

    this.blit(this.guiLeft + 29, this.guiTop - 27, 0, this.ySize, 28, 32);

    this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    this.blit(this.guiLeft, this.guiTop - 28, 0, this.ySize + 30, 28, 32);

    IInventory inventory = this.container.getCraftingInventory();
    this.minecraft.getTextureManager().bindTexture(INVENTORY_BACKGROUND);

    ItemStack gunStack = inventory.getStackInSlot(CraftingInventorySlotType.GUN.getIndex());

    if (gunStack.getItem() instanceof GunItem) {
      GunItem gunItem = (GunItem) gunStack.getItem();
      this.blit(this.guiLeft + 122, this.guiTop + 45, 176, 0, 22, 22);

      Supplier<Stream<ItemStack>> addonStream = () -> CraftingInventorySlotType.ADDON_SLOTS
          .stream()
          .map(slot -> inventory.getStackInSlot(slot.getIndex()));

      boolean isCraftable = true;
      isCraftable = !addonStream.get().allMatch(ItemStack::isEmpty)
          && addonStream.get().anyMatch(gunItem::isAcceptedPaintOrAttachment);


      if (isCraftable) {
        this.blit(this.guiLeft + 122, this.guiTop + 45, 176, 22, 22, 22);
      }

      if (!isCraftable || !this.playerInventory.getItemStack().isEmpty()
          && !gunItem.isAcceptedPaintOrAttachment(this.playerInventory.getItemStack())) {
        this.blit(this.guiLeft + 122, this.guiTop + 45, 176, 44, 22, 22);
      }
    }

    InventoryScreen
        .drawEntity(this.guiLeft + 33, this.guiTop + 97, 30, (this.guiLeft + 51) - this.oldMouseX,
            (this.guiTop + 75 - 50) - this.oldMouseY, this.minecraft.player);
  }
}
