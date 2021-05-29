package com.craftingdead.survival.world.level.block;

import java.util.function.Supplier;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.particles.SurvivalParticleTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.particles.IParticleData;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SurvivalBlocks {

  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CraftingDeadSurvival.ID);

  public static final RegistryObject<Block> MILITARY_LOOT =
      BLOCKS.register("military_loot",
          () -> new LootBlock(AbstractBlock.Properties.of(Material.STONE)
              .strength(5.0F, 5.0F)
              .noOcclusion()));

  public static final RegistryObject<Block> MEDIC_LOOT =
      BLOCKS.register("medic_loot",
          () -> new LootBlock(AbstractBlock.Properties.of(Material.STONE)
              .strength(5.0F, 5.0F)
              .noOcclusion()));

  public static final RegistryObject<Block> CIVILIAN_LOOT =
      BLOCKS.register("civilian_loot",
          () -> new LootBlock(AbstractBlock.Properties.of(Material.STONE)
              .strength(5.0F, 5.0F)
              .noOcclusion()));

  public static final RegistryObject<Block> CIVILIAN_RARE_LOOT =
      BLOCKS.register("civilian_rare_loot",
          () -> new LootBlock(AbstractBlock.Properties.of(Material.STONE)
              .strength(5.0F, 5.0F)
              .noOcclusion()));

  public static final RegistryObject<Block> POLICE_LOOT =
      BLOCKS.register("police_loot",
          () -> new LootBlock(AbstractBlock.Properties.of(Material.STONE)
              .strength(5.0F, 5.0F)
              .noOcclusion()));

  public static final RegistryObject<Block> MILITARY_LOOT_GEN =
      BLOCKS.register("military_loot_gen",
          () -> lootGenerator(MILITARY_LOOT, SurvivalParticleTypes.MILITARY_LOOT_GEN::get));

  public static final RegistryObject<Block> MEDIC_LOOT_GEN =
      BLOCKS.register("medic_loot_gen",
          () -> lootGenerator(MEDIC_LOOT, SurvivalParticleTypes.MEDIC_LOOT_GEN::get));

  public static final RegistryObject<Block> CIVILIAN_LOOT_GEN =
      BLOCKS.register("civilian_loot_gen",
          () -> lootGenerator(CIVILIAN_LOOT, SurvivalParticleTypes.CIVILIAN_LOOT_GEN::get));

  public static final RegistryObject<Block> CIVILIAN_RARE_LOOT_GEN =
      BLOCKS.register("civilian_rare_loot_gen",
          () -> lootGenerator(CIVILIAN_RARE_LOOT,
              SurvivalParticleTypes.CIVILIAN_RARE_LOOT_GEN::get));

  public static final RegistryObject<Block> POLICE_LOOT_GEN =
      BLOCKS.register("police_loot_gen",
          () -> lootGenerator(POLICE_LOOT, SurvivalParticleTypes.POLICE_LOOT_GEN::get));

  private static LootGeneratorBlock lootGenerator(Supplier<Block> lootBlock,
      Supplier<IParticleData> particleOptions) {
    return new LootGeneratorBlock(
        AbstractBlock.Properties.of(Material.STRUCTURAL_AIR)
            .strength(5.0F, 5.0F)
            .randomTicks()
            .noOcclusion()
            .noCollission(),
        lootBlock, particleOptions);
  }
}
