package com.craftingdead.mod.item;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.potion.ModEffects;
import lombok.Getter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MedItem extends Item {

    @Getter
    public int bloodHeal;

    @Getter
    private boolean brokenlag;

    @Getter
    private boolean bleeding;

    @Getter
    private boolean adrenaline;

    private IItemProvider containerItem;

    public MedItem(MedItem.Properties properties) {
        super(properties);
        this.bloodHeal = properties.bloodHeal;
        this.brokenlag = properties.brokenlag;
        this.containerItem = properties.containerItem;
        this.bleeding = properties.bleeding;
        this.adrenaline = properties.adrenaline;
    }

    //TODO Fix Animation
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
                                                    Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.getHealth() > 20F) {
            return new ActionResult<>(ActionResultType.FAIL, itemstack);
        } else {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if(bloodHeal > 0) {
            entityLiving.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
                entityLiving.heal(bloodHeal);
            });
        }
        if(isBrokenlag()){
            entityLiving.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
                entityLiving.removePotionEffect(ModEffects.BROKEN_LEG);
            });
        }

        if(isBleeding()){
            entityLiving.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
                entityLiving.removePotionEffect(ModEffects.BROKEN_LEG);
            });
        }

        if(isAdrenaline()){
            entityLiving.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
                entityLiving.addPotionEffect(new EffectInstance(Effects.SPEED, 2000, 1));
            });
        }

        if (entityLiving instanceof PlayerEntity && this.hasContainerItem(stack)) {
            ((PlayerEntity) entityLiving).addItemStackToInventory(this.getContainerItem(stack));
        }

        stack.shrink(1);
        return stack;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this.containerItem);
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return this.containerItem != null;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        if (stack.getItem().isFood()) {
            return this.getFood().isFastEating() ? 16 : 32;
        } else {
            return 32;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (bloodHeal > 0) {
            tooltip.add(new TranslationTextComponent("Hearts " + TextFormatting.RED + bloodHeal));
        }

        if (isBrokenlag()) {
            tooltip.add(new TranslationTextComponent("Fixed Broken Legs"));
        }

        if (isBleeding()) {
            tooltip.add(new TranslationTextComponent("Stops Bleeding"));
        }

        if(isAdrenaline()){
            tooltip.add(new TranslationTextComponent("Induces Adrenaline"));
        }
    }

    /**
     * Changes one item to another
     */
    protected ItemStack turnSwapItem(ItemStack itemStack, PlayerEntity player, ItemStack stack) {
        itemStack.shrink(1);
        player.addStat(Stats.ITEM_USED.get(this));
        if (itemStack.isEmpty()) {
            return stack;
        } else {
            if (!player.inventory.addItemStackToInventory(stack)) {
                player.dropItem(stack, false);
            }
            return itemStack;
        }
    }

    public static class Properties extends Item.Properties {

        public int bloodHeal;

        public boolean adrenaline = false;

        public boolean brokenlag = false;

        public boolean bleeding = false; //stop loss of blood

        public boolean infection = false;

        public IItemProvider containerItem;

        public MedItem.Properties setMaxStackSize(int maxStackSize) {
            this.maxStackSize(maxStackSize);
            return this;
        }

        public MedItem.Properties setBloodHeal(int bloodHeal) {
            this.bloodHeal = bloodHeal;
            return this;
        }

        public MedItem.Properties setGroup(ItemGroup groupIn) {
            this.group(groupIn);
            return this;
        }

        public MedItem.Properties setAdrenaline() {
            this.adrenaline = true;
            return this;
        }

        public MedItem.Properties setBrokenLag() {
            this.brokenlag = true;
            return this;
        }

        public MedItem.Properties setBleeding() {
            this.bleeding = true;
            return this;
        }

        public MedItem.Properties setInfection() {
            this.infection = true;
            return this;
        }

        public MedItem.Properties setContainer(IItemProvider containerItem) {
            this.containerItem = containerItem;
            return this;
        }
    }

}


