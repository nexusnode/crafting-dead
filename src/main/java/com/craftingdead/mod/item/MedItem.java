package com.craftingdead.mod.item;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.potion.ModEffects;
import lombok.Getter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
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




    public MedItem(MedItem.Properties properties) {
        super(properties);
        this.bloodHeal = properties.bloodHeal;
        this.brokenlag = properties.brokenlag;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
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

        if (entityLiving instanceof PlayerEntity && this.hasContainerItem(stack)) {
            ((PlayerEntity) entityLiving).addItemStackToInventory(this.getContainerItem(stack));
        }
        stack.shrink(1);
        return stack;
    }

    // TODO
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

        if (isBrokenlag()) {
            tooltip.add(new TranslationTextComponent("Fixed Broken Legs"));
        }
    }

    public static class Properties extends Item.Properties {

        public int bloodHeal;

        public boolean adrenaline = false;

        public boolean brokenlag = false;

        public boolean bleeding = false; //stop loss of blood

        public boolean infection = false;


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



    }

}


