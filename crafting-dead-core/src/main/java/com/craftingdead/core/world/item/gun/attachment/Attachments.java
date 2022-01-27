/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.world.item.gun.attachment;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.inventory.GunCraftSlotType;
import com.craftingdead.core.world.item.ModItems;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class Attachments {

  public static final DeferredRegister<Attachment> ATTACHMENTS =
      DeferredRegister.create(Attachment.class, CraftingDead.ID);

  public static final Lazy<IForgeRegistry<Attachment>> REGISTRY =
      Lazy.of(ATTACHMENTS.makeRegistry("attachment", RegistryBuilder::new));

  public static final RegistryObject<Attachment> RED_DOT_SIGHT =
      ATTACHMENTS.register("red_dot_sight",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 2.5F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setItem(ModItems.RED_DOT_SIGHT)
              .build());

  public static final RegistryObject<Attachment> ACOG_SIGHT =
      ATTACHMENTS.register("acog_sight",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 3.25F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setItem(ModItems.ACOG_SIGHT)
              .build());

  public static final RegistryObject<Attachment> LP_SCOPE =
      ATTACHMENTS.register("lp_scope",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 5.0F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setScope(true)
              .setItem(ModItems.LP_SCOPE)
              .build());

  public static final RegistryObject<Attachment> HP_SCOPE =
      ATTACHMENTS.register("hp_scope",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 8.0F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setScope(true)
              .setItem(ModItems.HP_SCOPE)
              .build());

  public static final RegistryObject<Attachment> SUPPRESSOR =
      ATTACHMENTS.register("suppressor",
          () -> Attachment.builder()
              .setInventorySlot(GunCraftSlotType.MUZZLE_ATTACHMENT)
              .setSoundSuppressor(true)
              .setItem(ModItems.SUPPRESSOR)
              .build());

  public static final RegistryObject<Attachment> TACTICAL_GRIP =
      ATTACHMENTS.register("tactical_grip",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ACCURACY, 1.15F)
              .setInventorySlot(GunCraftSlotType.UNDERBARREL_ATTACHMENT)
              .setItem(ModItems.TACTICAL_GRIP)
              .build());

  public static final RegistryObject<Attachment> BIPOD =
      ATTACHMENTS.register("bipod",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ACCURACY, 1.05F)
              .setInventorySlot(GunCraftSlotType.UNDERBARREL_ATTACHMENT)
              .setItem(ModItems.BIPOD)
              .build());

  public static final RegistryObject<Attachment> EOTECH_SIGHT =
      ATTACHMENTS.register("eotech_sight",
          () -> Attachment.builder()
              .addMultiplier(Attachment.MultiplierType.ZOOM, 2.5F)
              .setInventorySlot(GunCraftSlotType.OVERBARREL_ATTACHMENT)
              .setItem(ModItems.EOTECH_SIGHT)
              .build());
}
