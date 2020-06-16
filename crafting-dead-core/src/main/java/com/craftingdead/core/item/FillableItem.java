package com.craftingdead.core.item;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.craftingdead.core.util.Text;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class FillableItem extends Item {

  /**
   * Used for i18n.
   */
  public static final ITextComponent WHEN_USED_ON_TOOLTIP =
      Text.translate("item_lore.fillable_item.when_used_on").applyTextStyle(TextFormatting.GRAY);

  private final ResourceLocation fullItem;
  /**
   * A short explanation about the {@link #blockPredicate}
   */
  @Nullable
  private final ITextComponent blockExplanation;
  @Nonnull
  private final BiPredicate<BlockPos, BlockState> blockPredicate;
  /**
   * A short explanation about the {@link #entityPredicate}
   */
  @Nullable
  private final ITextComponent entityExplanation;
  @Nonnull
  private final Predicate<Entity> entityPredicate;
  private final float probability;

  public FillableItem(Properties properties) {
    super(properties);
    this.fullItem = properties.fullItem;
    this.blockExplanation = properties.blockExplanation;
    this.blockPredicate = properties.blockPredicate;
    this.entityExplanation = properties.entityExplanation;
    this.entityPredicate = properties.entityPredicate;
    this.probability = properties.probability;
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn,
      LivingEntity target, Hand hand) {
    if (this.entityPredicate.test(target) && random.nextFloat() < this.probability) {
      playerIn.setHeldItem(hand, this.getReturnItem(stack, playerIn));
      return true;
    }
    return false;
  }

  @Override
  public void addInformation(ItemStack stack, World world,
      List<ITextComponent> lines, ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);

    if (this.fullItem != null) {
      ITextComponent canBeFilledText = Text.translate("item_lore.fillable_item.can_be_filled")
          .applyTextStyle(TextFormatting.GRAY);
      ITextComponent completeText = canBeFilledText.appendSibling(ForgeRegistries.ITEMS
          .getValue(this.fullItem).getName().applyTextStyle(TextFormatting.GREEN));

      if (this.blockExplanation != null) {
        completeText =
            completeText.appendSibling(this.blockExplanation);
      }

      if (this.entityExplanation != null) {
        completeText =
            completeText.appendSibling(this.entityExplanation);
      }

      lines.add(completeText);
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    BlockRayTraceResult rayTraceResult =
        (BlockRayTraceResult) rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.ANY);
    BlockState blockState = worldIn.getBlockState(rayTraceResult.getPos());
    ItemStack itemStack = playerIn.getHeldItem(handIn);
    if (this.blockPredicate.test(rayTraceResult.getPos(), blockState)
        && random.nextFloat() < this.probability) {
      if (blockState.getFluidState().getFluid() != Fluids.EMPTY) {
        playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
        if (!worldIn.isRemote) {
          CriteriaTriggers.FILLED_BUCKET
              .trigger((ServerPlayerEntity) playerIn,
                  new ItemStack(ForgeRegistries.ITEMS.getValue(this.fullItem)));
        }
      }
      return new ActionResult<>(ActionResultType.SUCCESS, this.getReturnItem(itemStack, playerIn));
    }
    return new ActionResult<>(ActionResultType.FAIL, itemStack);
  }

  private ItemStack getReturnItem(final ItemStack itemStack, PlayerEntity player) {
    player.addStat(Stats.ITEM_USED.get(this));
    final ItemStack fullStack = new ItemStack(ForgeRegistries.ITEMS.getValue(this.fullItem));
    itemStack.shrink(1);
    if (itemStack.isEmpty()) {
      return fullStack;
    } else {
      if (!player.inventory.addItemStackToInventory(fullStack)) {
        player.dropItem(fullStack, false);
      }
      return itemStack;
    }
  }

  public static class Properties extends Item.Properties {

    private ResourceLocation fullItem;
    @Nullable
    private ITextComponent blockExplanation;
    @Nonnull
    private BiPredicate<BlockPos, BlockState> blockPredicate = (blockPos, blockState) -> false;
    @Nullable
    private ITextComponent entityExplanation;
    @Nonnull
    private Predicate<Entity> entityPredicate = (entity) -> false;
    private float probability = 1.0F;

    public Properties setFullItem(ResourceLocation fullItem) {
      this.fullItem = fullItem;
      return this;
    }

    public Properties setBlockPredicate(@Nullable ITextComponent blockExplanation,
        @Nonnull BiPredicate<BlockPos, BlockState> blockPredicate) {
      this.blockExplanation = blockExplanation;
      this.blockPredicate = blockPredicate;
      return this;
    }

    public Properties setEntityPredicate(@Nullable ITextComponent entityExplanation,
        @Nonnull Predicate<Entity> entityPredicate) {
      this.entityExplanation = entityExplanation;
      this.entityPredicate = entityPredicate;
      return this;
    }

    public Properties setProbability(float probability) {
      this.probability = probability;
      return this;
    }
  }
}
