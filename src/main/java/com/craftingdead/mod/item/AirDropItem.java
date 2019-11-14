package com.craftingdead.mod.item;

import com.craftingdead.mod.entity.MedicalCrateEntity;
import com.craftingdead.mod.entity.MilitaryCrateEntity;
import com.craftingdead.mod.entity.SupplyCrateEntity;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//TODO The model is taken from another mod, you must not forget to do your own !!!
public class AirDropItem extends Item {

  boolean isMedical = false;

  boolean isMilitary = false;

  boolean isSupply = false;

  public AirDropItem(AirDropItem.Properties properties) {
    super(properties);
    isMedical = properties.medical;
    isMilitary = properties.military;
    isSupply = properties.supply;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
      ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    World world = context.getWorld();
    BlockPos blockpos = context.getPos();
    ItemStack itemstack = context.getItem();
    if (!world.isRemote) {

      if (isMedical) {
        MedicalCrateEntity medicalCrateEntity = new MedicalCrateEntity(world,
            (double) blockpos.getX() + 0.5D,
            (double) blockpos.getY() + 25D, (double) blockpos.getZ() + 0.5D);
        medicalCrateEntity.randomLoot();
        medicalCrateEntity.setCustomName(itemstack.getDisplayName());
        world.addEntity(medicalCrateEntity);
      }

      if (isMilitary) {
        MilitaryCrateEntity militaryCrateEntity = new MilitaryCrateEntity(world,
            (double) blockpos.getX() + 0.5D,
            (double) blockpos.getY() + 25D, (double) blockpos.getZ() + 0.5D);
        militaryCrateEntity.randomLoot();
        militaryCrateEntity.setCustomName(itemstack.getDisplayName());
        world.addEntity(militaryCrateEntity);
      }

      if (isSupply) {
        SupplyCrateEntity supplyCrateEntity = new SupplyCrateEntity(world,
            (double) blockpos.getX() + 0.5D,
            (double) blockpos.getY() + 25D, (double) blockpos.getZ() + 0.5D);
        supplyCrateEntity.randomLoot();
        supplyCrateEntity.setCustomName(itemstack.getDisplayName());
        world.addEntity(supplyCrateEntity);
      }

    }

    itemstack.shrink(1);
    return ActionResultType.SUCCESS;
  }

  public static class Properties extends Item.Properties {

    public boolean medical;
    public boolean military;
    public boolean supply;

    public AirDropItem.Properties setMaxStackSize(int maxStackSize) {
      this.maxStackSize(maxStackSize);
      return this;
    }

    public AirDropItem.Properties setGroup(ItemGroup groupIn) {
      this.group(groupIn);
      return this;
    }

    public AirDropItem.Properties setMedical() {
      medical = true;
      return this;
    }

    public AirDropItem.Properties setMilitary() {
      military = true;
      return this;
    }

    public AirDropItem.Properties setSupply() {
      supply = true;
      return this;
    }


  }
}