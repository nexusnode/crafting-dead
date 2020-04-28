package com.craftingdead.mod.item;


import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.mod.entity.GrenadeEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class GrenadeItem extends Item {

  private final GrenadeType type;
  private final float timeUntilExplosion;
  private final double throwingForce;
  private final float explosionRadius;
  private final float grenadeRange;

  public GrenadeItem(Properties properties) {
    super(properties);
    this.type = properties.type;
    this.timeUntilExplosion = properties.timeUntilExplosion;
    this.throwingForce = properties.throwingForce;
    this.explosionRadius = properties.explosionRadius;
    this.grenadeRange = properties.grenadeRange;
  }

  @Override
  public void addInformation(ItemStack p_77624_1_, @Nullable World p_77624_2_,
      List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
    ITextComponent text = ITextComponent.copyWithoutSiblings(p_77624_3_.get(0));
    text.getStyle().setColor(TextFormatting.DARK_GREEN);
    text.appendText("" + "\n");
    text.appendText("Explosion Time: " + timeUntilExplosion / 20 + " seconds" + "\n");
    text.appendText("Explosion Radius: " + explosionRadius);
    p_77624_3_.add(text);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    worldIn
        .playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
            SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F,
            0.4F / (random.nextFloat() * 0.4F + 0.8F));
    if (!worldIn.isRemote) {
      GrenadeEntity grenadeEntity =
          new GrenadeEntity(worldIn, playerIn, type, this.explosionRadius, this.timeUntilExplosion);
      grenadeEntity.setItem(itemstack);
      Vec3d look = new Vec3d(playerIn.getLookVec().x * throwingForce,
          playerIn.getLookVec().y * throwingForce, playerIn.getLookVec().z * throwingForce);

      grenadeEntity.setVelocity(look.x, look.y, look.z);
      grenadeEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0, 1.5F, 1.0F);
      worldIn.addEntity(grenadeEntity);
    }

    playerIn.addStat(Stats.ITEM_USED.get(this));
    if (!playerIn.abilities.isCreativeMode) {
      itemstack.shrink(1);
    }

    return ActionResult.success(itemstack);
  }

  public static class Properties extends Item.Properties {

    private GrenadeType type = GrenadeType.FIRE;
    private float timeUntilExplosion = 0.1f;
    private double throwingForce = 1.1D;
    private float explosionRadius = 2f;
    private float grenadeRange = 1f;

    public Properties setExplosionRadius(float explosionRadius) {
      this.explosionRadius = explosionRadius;
      return this;
    }

    public Properties setGrenadeRange(float grenadeRange) {
      this.grenadeRange = grenadeRange;
      return this;
    }

    public Properties setTyoe(GrenadeType type) {
      this.type = type;
      return this;
    }

    public Properties setTimeUntilExplosion(float timeUntilExplosion) {
      this.timeUntilExplosion = timeUntilExplosion;
      return this;
    }

    public Properties setThrowingForce(double throwingForce) {
      this.throwingForce = throwingForce;
      return this;
    }
  }
}
