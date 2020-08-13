package com.craftingdead.core.action;

import java.util.List;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReload;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.capability.scope.IScope;
import com.craftingdead.core.inventory.InventorySlotType;
import com.google.common.collect.ImmutableList;
import com.tiviacz.travelersbackpack.capability.CapabilityUtils;
import com.tiviacz.travelersbackpack.inventory.TravelersBackpackInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ReloadAction extends TimedAction {

  private final IGun gun;

  private ItemStack oldMagazineStack;

  public ReloadAction(ILiving<?> performer) {
    super(ActionTypes.RELOAD.get(), performer, null);
    this.gun = performer.getEntity().getHeldItemMainhand().getCapability(ModCapabilities.GUN)
        .orElseThrow(() -> new IllegalStateException("Performer not holding gun"));
  }

  @Override
  protected int getTotalDurationTicks() {
    return this.gun.getReloadDurationTicks();
  }

  @Override
  public boolean start() {
    ItemStack magazineStack = this.findAmmo(this.performer, true);
    if (!this.getPerformer().getEntity().isSprinting() && !magazineStack.isEmpty()) {
      if (this.gun instanceof IScope
          && ((IScope) this.gun).isAiming(this.getPerformer().getEntity(),
              this.performer.getEntity().getHeldItemMainhand())) {
        this.gun.toggleRightMouseAction(this.getPerformer(), false);
      }
      this.oldMagazineStack = this.gun.getMagazineStack();
      // Some guns may not have a reload sound
      this.gun.getReloadSound()
          .ifPresent(sound -> this.performer.getEntity().getEntityWorld().playMovingSound(null,
              this.performer.getEntity(), sound, SoundCategory.PLAYERS, 1.0F, 1.0F));
      if (this.performer.getEntity().getEntityWorld().isRemote()) {
        if (!this.oldMagazineStack.isEmpty()) {
          this.gun.getAnimation(AnimationType.RELOAD)
              .filter(animation -> animation instanceof GunAnimationReload)
              .map(animation -> (GunAnimationReload) animation)
              .ifPresent(animation -> {
                ((GunAnimationReload) animation).setEjectingClip(true);
                this.gun.getAnimationController().addAnimation(animation,
                    () -> this.playLoadAnimation(magazineStack));
              });
        } else {
          this.playLoadAnimation(magazineStack);
        }
      }
      return true;
    }
    return false;
  }

  private void playLoadAnimation(ItemStack magazineStack) {
    this.gun.setMagazineStack(magazineStack);
    this.gun.getAnimation(AnimationType.RELOAD)
        .filter(animation -> animation instanceof GunAnimationReload)
        .map(animation -> (GunAnimationReload) animation)
        .ifPresent(animation -> {
          animation.setEjectingClip(false);
          this.gun.getAnimationController().addAnimation(animation, null);
        });
  }

  @Override
  public boolean tick() {
    if (!this.getPerformer().getEntity().getEntityWorld().isRemote()
        && this.getPerformer().getEntity().isSprinting()) {
      this.getPerformer().cancelAction(true);
      return false;
    }
    return super.tick();
  }

  @Override
  protected void finish() {
    ItemStack magazineStack = this.findAmmo(this.performer, false);
    if (!magazineStack.isEmpty()) {
      if (!this.oldMagazineStack.isEmpty() && this.performer.getEntity() instanceof PlayerEntity) {
        ((PlayerEntity) this.performer.getEntity()).addItemStackToInventory(this.oldMagazineStack);
      }
      this.gun.setMagazineStack(magazineStack);
    }
  }

  @Override
  public void cancel() {
    super.cancel();
    if (this.getPerformer().getEntity().getEntityWorld().isRemote()) {
      if (this.gun.getReloadSound().isPresent()) {
        // Stop reload sound
        Minecraft.getInstance().getSoundHandler()
            .stop(this.gun.getReloadSound().get().getRegistryName(), SoundCategory.PLAYERS);
      }
      this.gun.getAnimationController().removeCurrentAnimation();
    }
    this.gun.setMagazineStack(this.oldMagazineStack);
  }

  private List<IItemHandler> collectAmmoProviders(ILiving<?> living) {
    ImmutableList.Builder<IItemHandler> builder = ImmutableList.builder();
    // Vest - first
    living.getItemHandler().getStackInSlot(InventorySlotType.VEST.getIndex())
        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(builder::add);
    // Backpack - second
    if (CraftingDead.getInstance().isTravelersBackpacksLoaded()
        && living.getEntity() instanceof PlayerEntity) {
      PlayerEntity playerEntity = (PlayerEntity) living.getEntity();
      TravelersBackpackInventory backpackInventory = CapabilityUtils.getBackpackInv(playerEntity);
      if (backpackInventory != null) {
        builder.add(new InvWrapper(backpackInventory));
      }
    }
    // Inventory - third
    living.getEntity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        .ifPresent(builder::add);
    return builder.build();
  }

  private ItemStack findAmmo(ILiving<?> living, boolean simulate) {
    for (IItemHandler ammoProvider : this.collectAmmoProviders(living)) {
      for (int i = 0; i < ammoProvider.getSlots(); ++i) {
        ItemStack itemStack = ammoProvider.getStackInSlot(i);
        if (this.gun.getAcceptedMagazines().contains(itemStack.getItem())
            && !itemStack
                .getCapability(ModCapabilities.MAGAZINE)
                .map(IMagazine::isEmpty)
                .orElse(true)) {
          return ammoProvider.extractItem(i, 1, simulate);
        }
      }
    }
    return ItemStack.EMPTY;
  }
}
