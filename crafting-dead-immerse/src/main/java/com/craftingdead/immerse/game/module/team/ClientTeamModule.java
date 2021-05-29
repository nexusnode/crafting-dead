package com.craftingdead.immerse.game.module.team;

import java.util.function.Supplier;
import com.craftingdead.core.event.RenderArmClothingEvent;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.game.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientTeamModule<T extends Enum<T> & Team> extends TeamModule<T>
    implements Module.Tickable {

  private final Minecraft minecraft = Minecraft.getInstance();

  private final Supplier<? extends Screen> switchTeamsScreen;

  public ClientTeamModule(Class<T> teamType, Supplier<? extends Screen> switchTeamsScreen) {
    super(teamType);
    this.switchTeamsScreen = switchTeamsScreen;
  }

  @Override
  public void tick() {
    while (ClientDist.SWITCH_TEAMS.consumeClick()) {
      this.minecraft.setScreen(this.switchTeamsScreen.get());
    }
  }

  @Override
  public void load() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void unload() {
    MinecraftForge.EVENT_BUS.unregister(this);
  }

  @SubscribeEvent
  public void handleRenderArmClothing(RenderArmClothingEvent event) {
    // We need to have team as a variable because the compiler doesn't like it if we use flatMap
    // on Team.getSkin because of something to do with generics.
    Team team = this.getPlayerTeam(event.getPlayerEntity().getUUID()).orElse(null);
    if (team != null) {
      team.getSkin().ifPresent(event::setClothingTexture);
    }
  }
}
