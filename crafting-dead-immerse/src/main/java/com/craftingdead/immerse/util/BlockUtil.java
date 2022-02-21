package com.craftingdead.immerse.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

public class BlockUtil {

  public static Rotation getRotation(Direction direction) {
    return switch (direction) {
      case NORTH -> Rotation.NONE;
      case EAST -> Rotation.CLOCKWISE_90;
      case SOUTH -> Rotation.CLOCKWISE_180;
      case WEST -> Rotation.COUNTERCLOCKWISE_90;
      default -> throw new IllegalArgumentException("Unexpected value: " + direction);
    };
  }
}
