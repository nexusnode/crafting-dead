package com.craftingdead.core.world.gun.attachment;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Nullable;
import com.craftingdead.core.world.inventory.GunCraftSlotType;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Attachment extends ForgeRegistryEntry<Attachment> implements IItemProvider {

  private final Map<MultiplierType, Float> multipliers;
  private final GunCraftSlotType inventorySlot;
  private final boolean soundSuppressor;
  private final boolean scope;
  private final Item item;

  @Nullable
  private String descriptionId;

  private Attachment(Builder builder) {
    this.multipliers = builder.multipliers;
    this.inventorySlot = builder.inventorySlot;
    this.soundSuppressor = builder.soundSuppressor;
    this.scope = builder.scope;
    this.item = builder.item;
  }

  public Map<MultiplierType, Float> getMultipliers() {
    return Collections.unmodifiableMap(this.multipliers);
  }

  public float getMultiplier(MultiplierType multiplierType) {
    return this.multipliers.getOrDefault(multiplierType, 1.0F);
  }

  public GunCraftSlotType getInventorySlot() {
    return this.inventorySlot;
  }

  public boolean isSoundSuppressor() {
    return this.soundSuppressor;
  }

  public boolean isScope() {
    return this.scope;
  }

  protected String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("attachment", this.getRegistryName());
    }
    return this.descriptionId;
  }

  public String getDescriptionId() {
    return this.getOrCreateDescriptionId();
  }

  public ITextComponent getDescription() {
    return new TranslationTextComponent(this.getDescriptionId());
  }

  @Override
  public Item asItem() {
    return this.item;
  }

  public static Builder builder() {
    return new Attachment.Builder();
  }

  public static enum MultiplierType {

    DAMAGE("damage"), ACCURACY("accuracy"), ZOOM("zoom");

    private final String name;

    private MultiplierType(String name) {
      this.name = name;
    }

    public String getTranslationKey() {
      return "attachment_multiplier_type." + this.name;
    }
  }

  public static class Builder {

    private final Map<MultiplierType, Float> multipliers = new EnumMap<>(MultiplierType.class);
    private GunCraftSlotType inventorySlot;
    private boolean soundSuppressor;
    private boolean scope;
    private Item item;

    public Builder addMultiplier(MultiplierType modifierType, float multiplier) {
      this.multipliers.put(modifierType, multiplier);
      return this;
    }

    public Builder setSoundSuppressor(boolean soundSuppressor) {
      this.soundSuppressor = soundSuppressor;
      return this;
    }

    public Builder setInventorySlot(GunCraftSlotType inventorySlot) {
      this.inventorySlot = inventorySlot;
      return this;
    }

    public Builder setScope(boolean scope) {
      this.scope = scope;
      return this;
    }

    public Builder setItem(Item item) {
      this.item = item;
      return this;
    }

    public Attachment build() {
      return new Attachment(this);
    }
  }
}
