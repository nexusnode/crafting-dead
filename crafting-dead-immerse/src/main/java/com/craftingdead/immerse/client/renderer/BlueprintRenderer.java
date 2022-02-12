package com.craftingdead.immerse.client.renderer;

import java.util.Map;
import java.util.Set;
import com.craftingdead.immerse.world.item.BlueprintItem;
import com.craftingdead.immerse.world.item.ImmerseItems;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;

public class BlueprintRenderer {

  private static Map<BlueprintItem, Iterable<BlockPos>> positionOffsets;

  private static final Set<BlockPos> ZERO = Set.of(BlockPos.ZERO);

  public static void register() {
    var builder = ImmutableMap.<BlueprintItem, Iterable<BlockPos>>builder();

    builder.put(ImmerseItems.BASE_CENTER_BLUEPRINT.get(), ZERO);
    builder.put(ImmerseItems.CAMPFIRE_BLUEPRINT.get(), ZERO);
    builder.put(ImmerseItems.OAK_PLANKS_WALL_BLUEPRINT.get(),
        BlockPos.betweenClosed(-1, 0, 0, 1, 2, 0));

    positionOffsets = builder.build();
  }

  public static void render(BlueprintItem item, BlockPos blockPos) {
    var offsets = positionOffsets.get(item);
    if (offsets != null) {
      for (var pos : offsets) {
//        System.out.println("render");
        DebugRenderer.renderFilledBox(pos.offset(blockPos), 0, 1.0F, 1.0F, 1.0F, 1.0F);
      }
    }
  }
}
