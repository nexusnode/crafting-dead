/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.gun.attachment;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.inventory.GunCraftSlotType;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class Attachments {

  public static final ResourceKey<Registry<Attachment>> REGISTRY_KEY =
      ResourceKey.createRegistryKey(new ResourceLocation(CraftingDead.ID, "attachment"));

  public static final DeferredRegister<Attachment> deferredRegister =
      DeferredRegister.create(REGISTRY_KEY, CraftingDead.ID);

  public static final Supplier<IForgeRegistry<Attachment>> registry =
      deferredRegister.makeRegistry(Attachment.class, RegistryBuilder::new);

  public static final RegistryObject<Attachment> RED_DOT_SIGHT =
      deferredRegister.register("red_dot_sight",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 2.5F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setItem(ModItems.RED_DOT_SIGHT)
              .build());

  public static final RegistryObject<Attachment> ACOG_SIGHT =
      deferredRegister.register("acog_sight",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 3.25F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setItem(ModItems.ACOG_SIGHT)
              .build());

  public static final RegistryObject<Attachment> LP_SCOPE =
      deferredRegister.register("lp_scope",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 5.0F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setScope(true)
              .setItem(ModItems.LP_SCOPE)
              .build());

  public static final RegistryObject<Attachment> HP_SCOPE =
      deferredRegister.register("hp_scope",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 8.0F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setScope(true)
              .setItem(ModItems.HP_SCOPE)
              .build());

  public static final RegistryObject<Attachment> SUPPRESSOR =
      deferredRegister.register("suppressor",
          () -> Attachment.builder()
              .setInventorySlot(GunCraftSlotType.MUZZLE_ATTACHMENT)
              .setSoundSuppressor(true)
              .setItem(ModItems.SUPPRESSOR)
              .build());

  public static final RegistryObject<Attachment> TACTICAL_GRIP =
      deferredRegister.register("tactical_grip",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ACCURACY, 1.15F)
              .setInventorySlot(GunCraftSlotType.UNDERBARREL_ATTACHMENT)
              .setItem(ModItems.TACTICAL_GRIP)
              .build());

  public static final RegistryObject<Attachment> BIPOD =
      deferredRegister.register("bipod",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ACCURACY, 1.05F)
              .setInventorySlot(GunCraftSlotType.UNDERBARREL_ATTACHMENT)
              .setItem(ModItems.BIPOD)
              .build());

  public static final RegistryObject<Attachment> EOTECH_SIGHT =
      deferredRegister.register("eotech_sight",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 2.5F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setItem(ModItems.EOTECH_SIGHT)
              .build());
}
