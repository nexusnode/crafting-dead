package com.craftingdead.mod.gui;

import com.craftingdead.mod.container.BackpackContainer;
import com.craftingdead.mod.container.ChestContainer;
import com.craftingdead.mod.type.Backpack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BackpackScreen extends ContainerScreen<BackpackContainer>
        implements IHasContainer<BackpackContainer> {

    protected int backgroundWidth;
    protected int backgroundHeight;

    private ResourceLocation GUI = new ResourceLocation("craftingdead", "textures/gui/backpack_container.png");

    public BackpackScreen(BackpackContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.backgroundHeight = 512;
        this.backgroundWidth = 512;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(x, y, 0, 0, this.xSize, this.ySize,this.backgroundWidth, this.backgroundHeight);
    }

}
