package com.craftingdead.virus.data;

import com.craftingdead.core.tag.ModItemTags;
import com.craftingdead.virus.CraftingDeadVirus;
import com.craftingdead.virus.item.VirusItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class VirusItemTagsProvider extends ItemTagsProvider {

  public VirusItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider,
      ExistingFileHelper existingFileHelper) {
    super(dataGenerator, blockTagProvider, CraftingDeadVirus.ID, existingFileHelper);
  }

  @Override
  public void addTags() {
    this.tag(ModItemTags.SYRINGES).add(VirusItems.RBI_SYRINGE.get(),
        VirusItems.CURE_SYRINGE.get());
    this.tag(ModItemTags.VIRUS_SYRINGE).add(VirusItems.RBI_SYRINGE.get());
  }

  @Override
  public String getName() {
    return "Crafting Dead Virus Item Tags";
  }
}
