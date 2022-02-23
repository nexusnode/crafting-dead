/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.world.action;

import java.util.List;
import java.util.Set;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.immerse.util.BlockUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;

public class BuildDoorWallAction extends BuildAction {

  public static final Set<BlockPos> WALL_OFFSETS = Set.of(
      new BlockPos(1, 0, 0),
      new BlockPos(-1, 0, 0),
      new BlockPos(1, 1, 0),
      new BlockPos(-1, 1, 0),
      new BlockPos(0, 2, 0),
      new BlockPos(1, 2, 0),
      new BlockPos(-1, 2, 0));

  private final BuildDoorWallActionType type;

  protected BuildDoorWallAction(LivingExtension<?, ?> performer, BlockPlaceContext context,
      BuildDoorWallActionType type) {
    super(performer, context);
    this.type = type;
  }

  @Override
  protected List<Placement> createPlacements() {
    var rotation = BlockUtil.getRotation(this.getPerformer().getEntity().getDirection());
    var clickedPos = this.getContext().getClickedPos();

    var placements = ImmutableList.<Placement>builder();

    for (var offset : WALL_OFFSETS) {
      var pos = offset.rotate(rotation).offset(clickedPos);
      var placement = this.createPlacement(pos, this.type.getWallBlock());
      if (placement == null) {
        return null;
      }
      placements.add(placement);
    }

    var doorPlacement =
        this.createPlacement(this.getContext().getClickedPos(), this.type.getDoorBlock());
    if (doorPlacement == null) {
      return null;
    }
    placements.add(doorPlacement);

    return placements.build();
  }

  @Override
  public BuildActionType getType() {
    return this.type;
  }
}
