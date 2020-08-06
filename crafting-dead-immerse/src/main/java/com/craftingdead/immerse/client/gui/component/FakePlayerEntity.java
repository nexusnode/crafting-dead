package com.craftingdead.immerse.client.gui.component;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.server.SPlayerListItemPacket;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;

public class FakePlayerEntity extends AbstractClientPlayerEntity {

  public FakePlayerEntity(GameProfile gameProfile) {
    super(FakeWorld.getInstance(), gameProfile);
  }

  @Override
  public Vec3d getPositionVec() {
    return new Vec3d(99.0D, 99.0D, 99.0D);
  }

  @Override
  protected NetworkPlayerInfo getPlayerInfo() {
    return new NetworkPlayerInfo(new SPlayerListItemPacket().new AddPlayerData(
        this.getGameProfile(), 0, GameType.NOT_SET, null));
  }

  @Override
  public boolean isSpectator() {
    return false;
  }

  @Override
  public boolean hasPlayerInfo() {
    return false;
  }

  @Override
  public boolean isInvisibleToPlayer(PlayerEntity playerEntity) {
    return false;
  }

  @Override
  public Team getTeam() {
    return null;
  }
}
