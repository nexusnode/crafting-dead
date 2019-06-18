package com.craftingdead.mod.capability;

import com.craftingdead.mod.capability.player.DefaultPlayer;
import com.craftingdead.mod.capability.player.Player;
import com.craftingdead.mod.capability.triggerable.DefaultTriggerable;
import com.craftingdead.mod.capability.triggerable.Triggerable;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities {

	@CapabilityInject(DefaultPlayer.class)
	public static final Capability<Player<?>> PLAYER = null;

	@CapabilityInject(Triggerable.class)
	public static final Capability<Triggerable> TRIGGERABLE = null;

	public static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(DefaultPlayer.class, new EmptyStorage<>(), DefaultPlayer::new);
		CapabilityManager.INSTANCE.register(Triggerable.class, new EmptyStorage<>(), DefaultTriggerable::new);
	}

	private static class EmptyStorage<C> implements Capability.IStorage<C> {

		@Override
		public INBT writeNBT(Capability<C> capability, C instance, Direction side) {
			return null;
		}

		@Override
		public void readNBT(Capability<C> capability, C instance, Direction side, INBT nbt) {
			;
		}
	}
}
