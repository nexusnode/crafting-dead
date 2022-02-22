package com.craftingdead.immerse.world.action;

import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;

public abstract class AbstractBuildBlockActionType extends BuildActionType {

  private final Supplier<Block> block;

  protected AbstractBuildBlockActionType(Builder<?> builder) {
    super(builder);
    this.block = builder.block;
  }

  public Block getBlock() {
    return this.block.get();
  }

  public static abstract class Builder<SELF extends Builder<SELF>>
      extends BuildActionType.Builder<SELF> {

    private Supplier<Block> block;

    public SELF block(Block block) {
      return this.block(() -> block);
    }

    public SELF block(Supplier<Block> block) {
      this.block = block;
      return this.self();
    }
  }
}
