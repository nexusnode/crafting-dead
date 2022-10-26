package com.craftingdead.decoration.world.level.block;

import java.util.EnumMap;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Shape definition for all blocks with custom model
 */
public class BlockShapes {

  public static final VoxelShape STREET_LIGHT =
      Shapes.join(Block.box(6.5, 0, 6.5, 9.5, 8, 9.5), Block.box(7, 0, 7, 9, 16, 9), BooleanOp.OR);
  public static final VoxelShape STREET_LIGHT_CROSS =
      Shapes.join(Block.box(0, 7, 7, 16, 9, 9), Block.box(7, 7, 0, 9, 9, 16), BooleanOp.OR);
  public static final VoxelShape STREET_LIGHT_POLE = Block.box(7, 0, 7, 9, 16, 9);
  public static final VoxelShape STOOL = Stream
      .of(Block.box(12, 0, 2, 14, 2, 4), Block.box(1.5, 1.5, 1.5, 14.5, 3.5, 14.5),
          Block.box(1, 3, 1, 15, 8, 15), Block.box(2, 0, 2, 4, 2, 4), Block.box(2, 0, 12, 4, 2, 14),
          Block.box(12, 0, 12, 14, 2, 14))
      .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
  public static final VoxelShape TABLE = Stream
      .of(Block.box(1, 0, 1, 3, 13, 3), Block.box(1, 0, 13, 3, 13, 15),
          Block.box(13, 0, 1, 15, 13, 3), Block.box(13, 0, 13, 15, 13, 15),
          Block.box(0, 13, 0, 16, 16, 16), Block.box(3, 9, 1.5, 13, 10, 2.5),
          Block.box(13.5, 8, 3, 14.5, 9, 13), Block.box(3, 9, 13.5, 13, 10, 14.5),
          Block.box(1.5, 8, 3, 2.5, 9, 13))
      .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
  public static final VoxelShape FLOWER_POT = Stream
      .of(Block.box(6.5, 9, 6.25, 8, 12, 8), Block.box(4, 0, 4, 12, 7, 12),
          Block.box(4.5, 7, 4.5, 11.5, 9, 11.5), Block.box(11, 7.5, 4, 12, 9.5, 12),
          Block.box(4, 7.5, 4, 5, 9.5, 12), Block.box(5, 7.5, 4, 11, 9.5, 5),
          Block.box(5, 7.5, 11, 11, 9.5, 12), Block.box(7, 9, 7, 9, 30, 9),
          Block.box(3, 16, 3, 13, 32, 13), Block.box(8, 9, 8, 10, 11, 10),
          Block.box(6.5, 9, 8, 8.5, 10, 9.75), Block.box(7.5, 9, 6.5, 9.5, 10, 7.5))
      .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
  public static final VoxelShape PLATE = Stream.of(Block.box(6.34315, 0.05, 4, 9.65685, 0.3, 12),
      Block.box(6.34315, 0, 4, 9.65685, 0.25, 12), Block.box(4, 0.025, 6.34315, 12, 0.275, 9.65685),
      Block.box(4, 0, 6.34315, 12, 0.25, 9.65685), Block.box(5.92893, 0.225, 3, 10.07107, 0.475, 4),
      Block.box(5.92893, 0.25, 3, 10.07107, 0.5, 4),
      Block.box(5.92893, 0.225, 12, 10.07107, 0.475, 13),
      Block.box(5.92893, 0.25, 12, 10.07107, 0.5, 13),
      Block.box(3, 0.225, 5.92893, 4, 0.475, 10.07107),
      Block.box(3, 0.25, 5.92893, 4, 0.5, 10.07107),
      Block.box(12, 0.225, 5.92893, 13, 0.475, 10.07107),
      Block.box(12, 0.25, 5.92893, 13, 0.5, 10.07107))
      .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

  public static final Function<Direction, VoxelShape> CONCRETE_BARRIER =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(0, 0, 7, 16, 14, 9), Block.box(0, 0, 9, 16, 2, 11),
              Block.box(0, 0, 5, 16, 2, 7))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> POLE_BARRIER =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(1, 0, 7, 3, 13, 9), Block.box(13, 0, 7, 15, 13, 9),
              Block.box(0, 9.3, 6, 16, 12.3, 7), Block.box(0, 5.3, 6, 16, 8.3, 7),
              Block.box(1, 13, 7, 3, 15.5, 9), Block.box(13, 13, 7, 15, 15.5, 9))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> ROAD_BARRIER =
      BlockShapes.computeOrientableShape(
          Stream.of(Block.box(12, -1, 3.75, 15, 12, 4.75), Block.box(1, -1, 3.75, 4, 12, 4.75),
              Block.box(12, -1, 11.1795, 15, 12, 12.1795),
              Block.box(1, -1, 11.1795, 4, 12, 12.1795),
              Block.box(0, 9, 9.275, 16, 13, 10.275), Block.box(0, 9, 5.67234, 16, 13, 6.67234),
              Block.box(0, 4, 11.275, 16, 8, 12.275), Block.box(0, 4, 3.67234, 16, 8, 4.67234))
              .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> SLAB_CONCRETE_BARRIER =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(0, 8, 7, 16, 22, 9), Block.box(0, 8, 9, 16, 10, 11),
              Block.box(0, 8, 5, 16, 10, 7), Block.box(0, 0, 0, 16, 8, 16))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> SLAB_STEEL_POLE_BARRIER =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(0, 0, 0, 16, 8, 16), Block.box(1, 8, 7, 3, 21, 9),
              Block.box(13, 8, 7, 15, 21, 9), Block.box(0, 17.3, 6, 16, 20.3, 7),
              Block.box(0, 13.3, 6, 16, 16.3, 7))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> SLAB_STRIPED_CONCRETE_BARRIER =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(0, 8, 7, 16, 22, 9), Block.box(0, 8, 9, 16, 10, 11),
              Block.box(0, 8, 5, 16, 10, 7), Block.box(0, 0, 0, 16, 8, 16))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> STEEL_POLE_BARRIER =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(13, 0, 7, 15, 13, 9), Block.box(1, 0, 7, 3, 13, 9),
              Block.box(0, 9.3, 6, 16, 12.3, 7), Block.box(0, 5.3, 6, 16, 8.3, 7))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> STOP_SIGN =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(7, -16, 7, 9, -10, 9), Block.box(7.5, -10, 7.5, 8.5, 31, 8.5),
              Block.box(0, 20.68629, 7.25, 16, 27.31371, 7.5),
              Block.box(0, 20.68629, 7.2, 16, 27.31371, 7.55),
              Block.box(4.68629, 16, 7.26, 11.31371, 32, 7.51),
              Block.box(4.68629, 16, 7.27, 11.31371, 32, 7.52))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> STREET_LIGHT_CURVE =
      BlockShapes.computeOrientableShape(
          Shapes.join(Block.box(7, 0, 7, 9, 16, 9), Block.box(7, 7, 0, 9, 9, 7), BooleanOp.OR));
  public static final Function<Direction, VoxelShape> STREET_LIGHT_DOUBLE_CURVE =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(7, 0, 7, 9, 16, 9), Block.box(7, 7, 0, 9, 9, 7),
              Block.box(7, 7, 9, 9, 9, 16))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> STREET_LIGHT_HORIZONTAL =
      BlockShapes.computeOrientableShape(Block.box(7, 7, 0, 9, 9, 16));
  public static final Function<Direction, VoxelShape> STREET_LIGHT_VERTICAL_CROSS =
      BlockShapes.computeOrientableShape(
          Shapes.join(Block.box(7, 0, 7, 9, 16, 9), Block.box(7, 7, 0, 9, 9, 16), BooleanOp.OR));
  public static final Function<Direction, VoxelShape> STRIPED_CONCRETE_BARRIER =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(0, 0, 7, 16, 14, 9), Block.box(0, 0, 9, 16, 2, 11),
              Block.box(0, 0, 5, 16, 2, 7))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> TRAFFIC_LIGHT =
      BlockShapes.computeOrientableShape(Block.box(5, 0, 21, 11, 16, 23));
  public static final Function<Direction, VoxelShape> UNLIT_POLE_BARRIER =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(13, 0, 7, 15, 13, 9), Block.box(1, 0, 7, 3, 13, 9),
              Block.box(0, 9.3, 6, 16, 12.3, 7), Block.box(0, 5.3, 6, 16, 8.3, 7))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> CHAIR =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(12, 0, 2, 14, 2, 4), Block.box(1.5, 1.5, 1.5, 14.5, 3.5, 14.5),
              Block.box(1, 3, 1, 15, 8, 15), Block.box(2, 0, 2, 4, 2, 4),
              Block.box(2, 0, 12, 4, 2, 14),
              Block.box(12, 0, 12, 14, 2, 14), Block.box(3.5, 11.5, 14, 12.5, 25.25, 14),
              Block.box(1.5, 8, 13.5, 3.5, 29, 14.5), Block.box(12.5, 8, 13.5, 14.5, 29, 14.5),
              Block.box(3.5, 25.25, 13.75, 12.5, 26.75, 14.25),
              Block.box(3.5, 10, 13.75, 12.5, 11.5, 14.25))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> TALL_CHAIR =
      BlockShapes.computeOrientableShape(Stream
          .of(Block.box(4, 10, 12.5, 12, 23, 13.5), Block.box(2, 9, 2, 14, 10, 14),
              Block.box(12, 0, 12, 14.025, 8, 14.025), Block.box(12, 8, 12, 14.025, 24, 14.025),
              Block.box(12, 0, 2, 14, 9, 4), Block.box(1.975, 8, 12, 4, 24, 14.025),
              Block.box(1.975, 0, 12, 4, 8, 14.025), Block.box(2, 0, 2, 4, 9, 4))
          .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> RADIO =
      computeOrientableShape(Shapes.join(Block.box(12.5, 9.25, 7.5, 13.5, 16.25, 8.5),
          Block.box(1, 0, 6, 15, 9, 10), BooleanOp.OR));
  public static final Function<Direction, VoxelShape> MILITARY_RADIO = computeOrientableShape(Stream
      .of(Block.box(9, 18, 11, 11, 19, 13), Block.box(12, 18, 11, 14, 19, 13),
          Block.box(2.25, 18, 11.25, 3.75, 21, 12.75), Block.box(1, 18, 16, 15, 24, 18),
          Block.box(1, 0, 10, 15, 18, 16), Block.box(2.5, 21, 11.5, 3.5, 32, 12.5))
      .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> SINK = computeOrientableShape(Stream
      .of(Block.box(5, 0, 9, 11, 11, 16), Block.box(2, 11, 13, 14, 16, 16),
          Block.box(2, 11, 6, 14, 16, 7), Block.box(13, 11, 7, 14, 16, 13),
          Block.box(2, 11, 7, 3, 16, 13), Block.box(3, 11, 7, 13, 12, 13),
          Block.box(7.25, 16, 13.75, 8.75, 21, 15.25),
          Block.box(7.25, 19.5, 11.25, 8.75, 21, 13.75),
          Block.box(4.25, 16, 13.75, 5.75, 17, 15.25),
          Block.box(10.25, 16, 13.75, 11.75, 17, 15.25))
      .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> COUNTER_SINK = computeOrientableShape(Stream
      .of(Block.box(2, 10.75, 12, 14, 15.75, 15), Block.box(2, 10.75, 5, 14, 15.75, 6),
          Block.box(13, 10.75, 6, 14, 15.75, 12), Block.box(2, 10.75, 6, 3, 15.75, 12),
          Block.box(7.25, 15.75, 12.75, 8.75, 20.75, 14.25),
          Block.box(7.25, 19.25, 10.25, 8.75, 20.75, 12.75),
          Block.box(4.25, 15.75, 12.75, 5.75, 16.75, 14.25),
          Block.box(10.25, 15.75, 12.75, 11.75, 16.75, 14.25), Block.box(0, 0, 2, 16, 12.5, 16),
          Block.box(0, 12.5, 0.5, 16, 16, 5), Block.box(0, 12.5, 5, 2, 16, 16),
          Block.box(2, 12.5, 15, 14, 16, 16), Block.box(14, 12.5, 5, 16, 16, 16))
      .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  public static final Function<Direction, VoxelShape> COUNTER_CORNER = computeOrientableShape(Shapes
      .join(Block.box(0.5, 12.5, 0.5, 16, 16, 16), Block.box(2, 0, 2, 16, 12.5, 16), BooleanOp.OR));
  public static final Function<Direction, VoxelShape> COUNTER = computeOrientableShape(Shapes
      .join(Block.box(0, 12.5, 0.5, 16, 16, 16), Block.box(0, 0, 2, 16, 12.5, 16), BooleanOp.OR));
  public static final Function<Direction, VoxelShape> FRIDGE = computeOrientableShape(Stream
      .of(Block.box(13, 1.75, 2, 14, 21.75, 3), Block.box(13, 23.75, 2, 14, 31.25, 3),
          Block.box(1.25, 23.25, 3.5, 14.75, 31.75, 4), Block.box(1.25, 1.25, 3.5, 14.75, 22.25, 4),
          Block.box(1, 1, 2.5, 15, 22.5, 3.5), Block.box(1, 23, 2.5, 15, 32, 3.5),
          Block.box(1, 0, 4, 15, 32, 16))
      .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());
  protected static final VoxelShape DOUBLE_TALL_BLOCK = Block.box(0, 0, 0, 16, 32, 16);

  /**
   * Rotates a shape and creates a map for each direction
   *
   * @param in - initial north facing shape
   * @return rotated shapes for each direction
   */
  public static Function<Direction, VoxelShape> computeOrientableShape(VoxelShape in) {
    var directions = Direction.values();
    var orientedShapes = new EnumMap<Direction, VoxelShape>(Direction.class);
    for (Direction direction : directions) {
      var buffer = new VoxelShape[] {in, Shapes.empty()};

      int times = (direction.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
      for (int j = 0; j < times; j++) {
        buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] =
            Shapes.or(buffer[1], Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
        buffer[0] = buffer[1];
        buffer[1] = Shapes.empty();
      }

      orientedShapes.put(direction, buffer[0]);
    }
    return orientedShapes::get;
  }
}
