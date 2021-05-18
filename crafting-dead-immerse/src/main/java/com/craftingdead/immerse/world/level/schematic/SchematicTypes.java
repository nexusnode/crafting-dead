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

package com.craftingdead.immerse.world.level.schematic;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.level.schematic.sponge.SpongeSchematicType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class SchematicTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<SchematicType<?>> SCHEMATIC_TYPES =
      DeferredRegister.create((Class<SchematicType<?>>) (Class<?>) SchematicType.class,
          CraftingDeadImmerse.ID);

  public static final RegistryObject<SchematicType<?>> SPONGE = SCHEMATIC_TYPES
      .register("sponge", SpongeSchematicType::new);
}
