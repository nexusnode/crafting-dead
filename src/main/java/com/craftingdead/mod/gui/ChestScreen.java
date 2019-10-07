package com.craftingdead.mod.gui;

import com.craftingdead.mod.container.ChestContainer;
import com.craftingdead.mod.type.ChestType;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value= Dist.CLIENT)
public class ChestScreen extends ContainerScreen<ChestContainer>
        implements IHasContainer<ChestContainer> {

    private ResourceLocation GUI = new ResourceLocation("craftingdead", "textures/gui/new_block_container.png");

    private ChestType chestType;
    private int textureXSize;
    private int textureYSize;

    public ChestScreen(ChestContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.chestType = container.getChestType();
        this.textureXSize = container.getChestType().xSize;
        this.textureYSize = container.getChestType().ySize;
        this.textureXSize = container.getChestType().textureXSize;
        this.textureYSize = container.getChestType().textureYSize;
        this.passEvents = false;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(x, y, 0, 0, this.xSize, this.ySize);
    }

}
