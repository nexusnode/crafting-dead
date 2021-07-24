package com.craftingdead.core.world.item;

import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.world.gun.skin.SimplePaint;
import com.craftingdead.core.world.gun.skin.Skin;
import com.craftingdead.core.world.gun.skin.Skins;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class PaintItem extends Item {

  private final RegistryKey<Skin> skin;

  public PaintItem(RegistryKey<Skin> skin, Properties properties) {
    super(properties);
    this.skin = skin;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SimpleCapabilityProvider<>(
        LazyOptional.of(() -> new SimplePaint(this.skin)), () -> Capabilities.PAINT);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable World level,
      List<ITextComponent> lines, ITooltipFlag flag) {
    lines.add(new TranslationTextComponent("item_lore.paint.accepted_guns")
        .withStyle(TextFormatting.GRAY));
    Skins.REGISTRY.get(this.skin).getAcceptedGuns().stream()
        .map(ForgeRegistries.ITEMS::getValue)
        .map(Item::getDescription)
        .map(text -> text.copy().withStyle(TextFormatting.RED))
        .forEach(lines::add);
  }
}
