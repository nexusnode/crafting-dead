package com.craftingdead.mod.client;

import com.craftingdead.mod.ServerProxy;
import com.craftingdead.mod.client.renderer.IronChestTileEntityRenderer;
import com.craftingdead.mod.type.ChestScreen;
import com.craftingdead.mod.type.ChestType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static com.craftingdead.mod.type.ModContainerType.ironchest;

public class ClientProxy
        extends ServerProxy {
    @Override
    public void preInit() {
        ScreenManager.registerFactory(ironchest, ChestScreen::new);
        super.preInit();
        ChestType[] arrchestType = ChestType.values();
        int n = arrchestType.length;
        int n2 = 0;
        while (n2 < n) {
            ChestType type = arrchestType[n2];
            if (type.clazz != null) {
                ClientRegistry.bindTileEntitySpecialRenderer(type.clazz, new IronChestTileEntityRenderer<>());
            }
            ++n2;
        }
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }
}