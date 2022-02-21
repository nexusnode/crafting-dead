package com.craftingdead.immerse.world.action;

import java.util.function.Supplier;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class BuildDoorWallActionType extends BlueprintActionType {

  private final Supplier<Block> wallBlock;
  private final Supplier<Block> doorBlock;

  protected BuildDoorWallActionType(Builder builder) {
    super(builder);
    this.wallBlock = builder.wallBlock;
    this.doorBlock = builder.doorBlock;
  }

  public Block getWallBlock() {
    return this.wallBlock.get();
  }

  public Block getDoorBlock() {
    return this.doorBlock.get();
  }

  @Override
  protected BlueprintAction create(LivingExtension<?, ?> performer, BlockPlaceContext context) {
    return new BuildDoorWallAction(performer, context, this);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends BlueprintActionType.Builder<Builder> {

    private Supplier<Block> wallBlock;
    private Supplier<Block> doorBlock;

    public Builder wallBlock(Block wallBlock) {
      return this.wallBlock(() -> wallBlock);
    }

    public Builder wallBlock(Supplier<Block> wallBlock) {
      this.wallBlock = wallBlock;
      return this;
    }

    public Builder doorBlock(Block doorBlock) {
      return this.doorBlock(() -> doorBlock);
    }

    public Builder doorBlock(Supplier<Block> doorBlock) {
      this.doorBlock = doorBlock;
      return this;
    }

    @Override
    public ItemActionType<?> build() {
      return new BuildDoorWallActionType(this);
    }
  }
}
