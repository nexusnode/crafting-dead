package com.craftingdead.immerse.world.level.block.entity;

import java.util.UUID;
import javax.annotation.Nullable;
import com.craftingdead.immerse.world.level.extension.LandClaim;
import com.craftingdead.immerse.world.level.extension.LegacyBase;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class BaseCenterBlockEntity extends BlockEntity {

  private static final int RADIUS = 35;
  private static final int BUILD_RADIUS = 7;

  @Nullable
  private UUID baseId;

  public BaseCenterBlockEntity(BlockPos pos, BlockState state) {
    super(ImmerseBlockEntityTypes.BASE_CENTER.get(), pos, state);
  }

  public void placed(LivingEntity livingEntity) {
    if (livingEntity instanceof Player player) {
      player.displayClientMessage(new TranslatableComponent("message.creating_base"), false);
    }

    var landManager = LevelExtension.getOrThrow(this.level).getLandManager();

    var base = new LegacyBase(livingEntity.getUUID(), this.getBlockPos(), BUILD_RADIUS);
    this.baseId = base.getId();
    landManager.registerLandOwner(base);

    landManager
        .registerLandClaim(
            new LandClaim(new BoundingBox(this.getBlockPos()).inflatedBy(RADIUS), this.baseId))
        .whenCompleteAsync((result, exception) -> {
          if (livingEntity instanceof Player player) {
            if (result) {
              player.displayClientMessage(
                  new TranslatableComponent("message.base_creation_succeeded")
                      .withStyle(ChatFormatting.GREEN),
                  true);
            } else {
              player.displayClientMessage(
                  new TranslatableComponent("message.base_creation_failed")
                      .withStyle(ChatFormatting.RED),
                  true);
            }
          }
        }, this.level.getServer());
  }

  public void removed() {
    if (this.baseId != null) {
      var landManager = LevelExtension.getOrThrow(this.level).getLandManager();
      landManager.removeLandOwner(this.baseId);
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    if (this.baseId != null) {
      tag.putUUID("baseId", this.baseId);
    }
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    if (tag.hasUUID("baseId")) {
      this.baseId = tag.getUUID("baseId");
    }
  }
}
