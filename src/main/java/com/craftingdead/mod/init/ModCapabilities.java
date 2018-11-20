package com.craftingdead.mod.init;

import com.craftingdead.mod.capability.SimpleCapability;
import com.craftingdead.mod.capability.player.DefaultPlayer;
import com.craftingdead.mod.capability.player.Player;
import com.craftingdead.mod.capability.triggerable.DefaultTriggerable;
import com.craftingdead.mod.capability.triggerable.Triggerable;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities {

	@CapabilityInject(Player.class)
	public static final Capability<Player<?>> PLAYER = null;

	@CapabilityInject(Triggerable.class)
	public static final Capability<Triggerable> TRIGGERABLE = null;

	public static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(Player.class, new SimpleCapability.Storage<>(), DefaultPlayer::new);
		CapabilityManager.INSTANCE.register(Triggerable.class, new SimpleCapability.Storage<>(),
				DefaultTriggerable::new);
	}

}
