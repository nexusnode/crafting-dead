package com.craftingdead.core.world.gun.attachment;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.inventory.GunCraftSlotType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class Attachments {

  public static final DeferredRegister<Attachment> ATTACHMENTS =
      DeferredRegister.create(Attachment.class, CraftingDead.ID);

  public static final Lazy<IForgeRegistry<Attachment>> REGISTRY =
      Lazy.of(ATTACHMENTS.makeRegistry("attachments", RegistryBuilder::new));

  public static final RegistryObject<Attachment> RED_DOT_SIGHT =
      ATTACHMENTS.register("red_dot_sight",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 2.5F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .build());

  public static final RegistryObject<Attachment> ACOG_SIGHT =
      ATTACHMENTS.register("acog_sight",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 3.25F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .build());

  public static final RegistryObject<Attachment> LP_SCOPE =
      ATTACHMENTS.register("lp_scope",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 5.0F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setScope(true)
              .build());

  public static final RegistryObject<Attachment> HP_SCOPE =
      ATTACHMENTS.register("hp_scope",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 8.0F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setScope(true)
              .build());

  public static final RegistryObject<Attachment> SUPPRESSOR =
      ATTACHMENTS.register("suppressor",
          () -> Attachment.builder()
              .setInventorySlot(GunCraftSlotType.MUZZLE_ATTACHMENT)
              .setSoundSuppressor(true)
              .build());

  public static final RegistryObject<Attachment> TACTICAL_GRIP =
      ATTACHMENTS.register("tactical_grip",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ACCURACY, 1.15F)
              .setInventorySlot(GunCraftSlotType.UNDERBARREL_ATTACHMENT)
              .build());

  public static final RegistryObject<Attachment> BIPOD =
      ATTACHMENTS.register("bipod",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ACCURACY, 1.05F)
              .setInventorySlot(GunCraftSlotType.UNDERBARREL_ATTACHMENT)
              .build());

  public static final RegistryObject<Attachment> EOTECH_SIGHT =
      ATTACHMENTS.register("eotech_sight",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 2.5F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .build());
}
