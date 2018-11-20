package com.craftingdead.mod.capability.player;

import net.minecraft.client.entity.AbstractClientPlayer;

public abstract class ClientPlayer<E extends AbstractClientPlayer> extends Player<E> {

	public ClientPlayer(E entity) {
		super(entity);
	}

}
