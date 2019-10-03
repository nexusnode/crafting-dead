package com.craftingdead.mod.test;


import com.craftingdead.mod.CraftingDead;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;



public class BackpackScreen extends ContainerScreen<BackpackContainer> {

    protected ResourceLocation background = new ResourceLocation(CraftingDead.ID, "textures/gui/backpack.png");

    protected int backgroundWidth;
    protected int backgroundHeight;

    public BackpackScreen(BackpackContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.backgroundHeight = 256;
        this.backgroundWidth = 256;
    }

    public BackpackScreen(BackpackContainer container, PlayerInventory playerInventory, ITextComponent title, ResourceLocation background) {
        super(container, playerInventory, title);
        this.background = background;
        this.backgroundHeight = 256;
        this.backgroundWidth = 256;
    }

    public void setBackground(ResourceLocation background) {
        this.background = background;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
       renderBackground();
    }
}
