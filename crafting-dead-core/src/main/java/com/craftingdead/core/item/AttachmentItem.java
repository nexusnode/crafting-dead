package com.craftingdead.core.item;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.inventory.CraftingInventorySlotType;
import com.craftingdead.core.util.Text;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class AttachmentItem extends Item {

  private final Map<MultiplierType, Float> multipliers;
  private final CraftingInventorySlotType inventorySlot;
  private final boolean suppressesSounds;
  private final Pair<Model, ResourceLocation> model;

  public AttachmentItem(Properties properties) {
    super(properties);
    this.multipliers = properties.multipliers;
    this.inventorySlot = properties.inventorySlot;
    this.suppressesSounds = properties.suppressesSounds;
    this.model = properties.model == null ? null
        : Pair.of(DistExecutor.safeCallWhenOn(Dist.CLIENT, properties.model.getLeft()),
            properties.model.getRight());
  }

  public Float getMultiplier(MultiplierType multiplierType) {
    return this.multipliers.getOrDefault(multiplierType, 1.0F);
  }

  public CraftingInventorySlotType getInventorySlot() {
    return this.inventorySlot;
  }

  public boolean isSoundSuppressor() {
    return this.suppressesSounds;
  }

  public Pair<Model, ResourceLocation> getModel() {
    return this.model;
  }

  @Override
  public void addInformation(ItemStack stack, World world,
      List<ITextComponent> lines, ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);

    for (Entry<MultiplierType, Float> entry : this.multipliers.entrySet()) {
      lines.add(Text.translate(entry.getKey().getTranslationKey())
          .applyTextStyle(TextFormatting.GRAY)
          .appendSibling(Text.of(" " + entry.getValue() + "x").applyTextStyle(TextFormatting.RED)));
    }
  }

  public static enum MultiplierType {
    DAMAGE, ACCURACY, FOV;

    private final String translationKey = "attachment_multiplier_type." + this.name().toLowerCase();

    public String getTranslationKey() {
      return this.translationKey;
    }
  }

  public static class Properties extends Item.Properties {

    private final Map<MultiplierType, Float> multipliers = new EnumMap<>(MultiplierType.class);
    private CraftingInventorySlotType inventorySlot;
    private boolean suppressesSounds;
    private Pair<Supplier<DistExecutor.SafeCallable<Model>>, ResourceLocation> model;

    public Properties addMultiplier(MultiplierType modifierType, float multiplier) {
      this.multipliers.put(modifierType, multiplier);
      return this;
    }

    public Properties setSuppressesSounds(boolean suppressesSounds) {
      this.suppressesSounds = suppressesSounds;
      return this;
    }

    public Properties setInventorySlot(CraftingInventorySlotType inventorySlot) {
      this.inventorySlot = inventorySlot;
      return this;
    }

    public Properties setModel(
        Pair<Supplier<DistExecutor.SafeCallable<Model>>, ResourceLocation> model) {
      this.model = model;
      return this;
    }
  }
}
