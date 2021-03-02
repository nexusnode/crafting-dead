package com.craftingdead.core.ammoprovider;

import com.craftingdead.core.action.RemoveMagazineAction;
import com.craftingdead.core.action.reload.MagazineReloadAction;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.magazine.IMagazine;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;

public class MagazineAmmoProvider implements IAmmoProvider {

  private ItemStack magazineStack;
  private boolean stackChanged;

  public MagazineAmmoProvider() {
    this(ItemStack.EMPTY);
  }

  public MagazineAmmoProvider(ItemStack magazineStack) {
    this.magazineStack = magazineStack;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.put("magazineStack", this.magazineStack.serializeNBT());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    if (nbt.contains("magazineStack", Constants.NBT.TAG_COMPOUND)) {
      this.magazineStack = ItemStack.read(nbt.getCompound("magazineStack"));
      this.stackChanged = true;
    }
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    if (this.stackChanged || writeAll) {
      out.writeBoolean(true);
      out.writeItemStack(this.magazineStack);
      this.stackChanged = false;
    } else {
      out.writeBoolean(false);
    }
    this.getMagazine().ifPresent(magazine -> magazine.encode(out, writeAll));
  }

  @Override
  public void decode(PacketBuffer in) {
    if (in.readBoolean()) {
      this.magazineStack = in.readItemStack();
    }
    this.getMagazine().ifPresent(magazine -> magazine.decode(in));
  }

  @Override
  public boolean requiresSync() {
    return this.getMagazine().map(IMagazine::requiresSync).orElse(false) || this.stackChanged;
  }

  @Override
  public void reload(ILiving<?, ?> living) {
    living.performAction(new MagazineReloadAction(living), true);
  }

  @Override
  public void unload(ILiving<?, ?> living) {
    living.performAction(new RemoveMagazineAction(living), true);
  }

  @Override
  public int getReserveSize() {
    return 0;
  }

  @Override
  public ItemStack getMagazineStack() {
    return this.magazineStack;
  }

  public void setMagazineStack(ItemStack magazineStack) {
    this.magazineStack = magazineStack;
    this.stackChanged = true;
  }

  @Override
  public AmmoProviderType getType() {
    return AmmoProviderTypes.MAGAZINE.get();
  }
}
