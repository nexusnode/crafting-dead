package com.craftingdead.mod.client.gui.container;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.craftingdead.mod.client.gui.GuiCDScreen;
import com.craftingdead.mod.client.renderer.RenderHelper;
import com.craftingdead.mod.common.core.CraftingDead;

import net.minecraft.util.ResourceLocation;

public class GuiCDContainerList extends GuiCDContainer {

    private ArrayList<GuiCDContainerListSlot> slots = new ArrayList<GuiCDContainerListSlot>();

    private int mouseX;
    private int mouseY;

    private Rectangle scollerBox;
    private int scollerMinY = 0;
    private int scollerMaxY = 0;

    private boolean isScollerClicked = false;

    private int slotHeight = 11;
    private int selectedSlot = -1;

    public GuiCDContainerList(int par1, int par2, int par3, int par4, int par5, GuiCDScreen par6) {
        super(par1, par2, par3, par4, par5, par6);
    }

    public GuiCDContainerList setContent(ArrayList<GuiCDContainerListSlot> par1) {
        this.updateDisplayedSlots(par1);
        return this;
    }

    public GuiCDContainerList setContentText(ArrayList<String> par1) {
        this.updateDisplayedSlots(GuiCDContainerListSlotText.getListFromStrings(par1));
        return this;
    }

    public GuiCDContainerList setSlotHeight(int par1) {
        this.slotHeight = par1;
        return this;
    }

    public void initGui() {

        this.scollerBox = new Rectangle(this.posX + this.width - 21, this.posY + 1, 20, 40);
        this.scollerMinY = this.posY + 2;
        this.scollerMaxY = this.posY + this.height - 2 - this.scollerBox.height;
    }

    public void updateScreen() {

        this.updateScollerStatus();

        for (int i = 0; i < slots.size(); i++) {
            GuiCDContainerListSlot slot = slots.get(i);
            slot.onUpdate();
        }

        if (this.isScollerClicked) {

            int newY = this.mouseY - (this.scollerBox.height / 2);

            if (newY > scollerMaxY) {
                newY = this.scollerMaxY;
            }

            if (newY < scollerMinY) {
                newY = this.scollerMinY;
            }

            this.scollerBox = new Rectangle(this.scollerBox.x, newY, this.scollerBox.width, this.scollerBox.height);
        }
    }

    public void mouseClicked(int par1, int par2, int par3) {

        for (int i = 0; i < slots.size(); i++) {

            GuiCDContainerListSlot slot = slots.get(i);
            Rectangle rect = new Rectangle(slot.posX, slot.posY, this.width - 20, this.slotHeight);

            if (rect.contains(par1, par2) && slot.canSelect()) {

                if (this.selectedSlot == i) {

                    slot.onDoubleClick();
                }

                this.selectedSlot = i;
            }
        }
    }

    public void scrollUp() {

    }

    public void scrollDown() {

    }

    public void updateScollerStatus() {
        if (Mouse.isButtonDown(0)) {
            if (this.scollerBox.contains(this.mouseX, this.mouseY)) {
                this.isScollerClicked = true;
            }
        } else {
            this.isScollerClicked = false;
        }
    }

    public int getScollerPercentage() {
        int var1 = (this.scollerMaxY) - (this.scollerMinY);
        int var2 = this.scollerBox.y - (this.scollerBox.height / 2) - (this.scollerMinY - (this.scollerBox.height / 2));
        float var3 = (var2) * 100 / (var1);
        return (int) var3;
    }

    public void drawScreen(int par1, int par2, float par3) {
        this.drawBackground();
        this.mouseX = par1;
        this.mouseY = par2;

        this.updateScrollWheel();

        int textHeight = this.slots.size() * slotHeight;
        int textPercentage = (int) ((textHeight - (this.height)) * this.getScollerPercentage() / 100);
        textPercentage = textPercentage < 0 ? 0 : textPercentage;

        if (textHeight > this.height) {

            RenderHelper.drawRectangle(this.posX + this.width - 18, this.posY + 4, this.scollerBox.width - 6,
                    this.height - 7, 0x000000, 1.0F, false);
            RenderHelper.drawImage(this.scollerBox.x, this.scollerBox.y,
                    new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/scoller.png"), this.scollerBox.width,
                    this.scollerBox.height, 1.0F);
        }

        for (int i = 0; i < slots.size(); i++) {

            boolean flag = this.selectedSlot == i;
            int textPosY = (this.posY + 2) + (slotHeight * i) - (textHeight > this.height ? textPercentage : 0);

            int var1 = textPosY - (this.posY) - 2;

            if (var1 % slotHeight != 0) {

                textPosY = textPosY - (var1 % slotHeight);
            }

            if (var1 < 0) {
                continue;
            }

            slots.get(i).posX = this.posX;
            slots.get(i).posY = textPosY;

            if (textPosY < (this.posY - 2)) {
                continue;
            }

            if (textPosY > (this.posY + this.height - 2) - (this.slotHeight - 6)) {
                continue;
            }

            slots.get(i).doRender(this.posX + 4, textPosY, flag, this.width - 30, this.slotHeight);
        }
    }

    public void updateScrollWheel() {

        int l2 = 0;

        if (Mouse.isButtonDown(0)) {
            return;
        }

        Rectangle rect = new Rectangle(this.posX, this.posY, this.width, this.height);

        if (rect.contains(this.mouseX, this.mouseY) == false) {

            return;
        }

        while (Mouse.next() && (l2 = Mouse.getEventDWheel()) != 0) {

            if (l2 > 0) {

                l2 = -1;
            } else if (l2 < 0) {

                l2 = 1;
            }

            int newY = this.scollerBox.y + (l2 * this.slotHeight);

            if (newY > scollerMaxY) {
                newY = this.scollerMaxY;
            }

            if (newY < scollerMinY) {
                newY = this.scollerMinY;
            }

            this.scollerBox = new Rectangle(this.scollerBox.x, newY, this.scollerBox.width, this.scollerBox.height);
        }
    }

    public GuiCDContainerListSlot getSlectedSlot() {
        if (this.selectedSlot == -1) {
            return null;
        }
        return this.slots.get(this.selectedSlot);
    }

    public void updateDisplayedSlots(ArrayList<GuiCDContainerListSlot> par1) {
        this.slots.clear();
        this.slots.addAll(par1);
    }
}
