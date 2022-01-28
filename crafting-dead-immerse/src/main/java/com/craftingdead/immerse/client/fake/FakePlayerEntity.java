/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.client.fake;

import java.util.Map;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Team;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class FakePlayerEntity extends AbstractClientPlayer {

  private final Map<Type, ResourceLocation> textureLocations = Maps.newEnumMap(Type.class);
  private boolean pendingTextures;
  private String skinModel;

  public FakePlayerEntity(GameProfile gameProfile) {
    super(FakeLevel.getInstance(), gameProfile);
  }

  @Override
  public String getModelName() {
    return this.skinModel == null
        ? DefaultPlayerSkin.getSkinModelName(this.getGameProfile().getId())
        : this.skinModel;
  }

  @Override
  public boolean isSkinLoaded() {
    return this.textureLocations.containsKey(Type.SKIN);
  }

  @Override
  public ResourceLocation getSkinTextureLocation() {
    this.registerTextures();
    return MoreObjects.firstNonNull(this.textureLocations.get(Type.SKIN),
        DefaultPlayerSkin.getDefaultSkin(this.getGameProfile().getId()));
  }

  @Override
  public boolean isCapeLoaded() {
    return this.textureLocations.containsKey(Type.CAPE);
  }

  @Override
  public ResourceLocation getCloakTextureLocation() {
    this.registerTextures();
    return this.textureLocations.get(Type.CAPE);
  }

  @Override
  public boolean isElytraLoaded() {
    return this.textureLocations.containsKey(Type.ELYTRA);
  }

  @Override
  public ResourceLocation getElytraTextureLocation() {
    this.registerTextures();
    return this.textureLocations.get(Type.ELYTRA);
  }

  private void registerTextures() {
    synchronized (this) {
      if (!this.pendingTextures) {
        this.pendingTextures = true;
        Minecraft.getInstance().getSkinManager().registerSkins(this.getGameProfile(),
            (type, textureLocation, texture) -> {
              this.textureLocations.put(type, textureLocation);
              if (type == Type.SKIN) {
                this.skinModel = texture.getMetadata("model");
                if (this.skinModel == null) {
                  this.skinModel = "default";
                }
              }
            }, true);
      }
    }
  }

  @Override
  public Vec3 position() {
    return new Vec3(0.0D, 0.0D, 0.0D);
  }

  @Override
  protected PlayerInfo getPlayerInfo() {
    return null;
  }

  @Override
  public boolean isSpectator() {
    return false;
  }

  @Override
  public boolean isInvisibleTo(Player playerEntity) {
    return false;
  }

  @Override
  public Team getTeam() {
    return null;
  }
}
