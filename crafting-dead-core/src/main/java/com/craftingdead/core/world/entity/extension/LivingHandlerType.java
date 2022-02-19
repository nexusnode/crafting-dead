package com.craftingdead.core.world.entity.extension;

import net.minecraft.resources.ResourceLocation;

public record LivingHandlerType<T extends LivingHandler>(ResourceLocation id) {}
