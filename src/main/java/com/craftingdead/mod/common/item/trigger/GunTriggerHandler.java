package com.craftingdead.mod.common.item.trigger;

import java.util.ArrayDeque;
import java.util.Deque;

import com.craftingdead.mod.common.item.Bullet;
import com.craftingdead.mod.common.item.ItemGun;
import com.craftingdead.mod.common.multiplayer.PlayerMP;

import net.minecraft.item.ItemStack;

public class GunTriggerHandler implements TriggerHandler {

	private ItemStack itemStack;
	private Deque<Bullet> pendingBullets = new ArrayDeque<Bullet>();

	@Override
	public void update() {
		if (!pendingBullets.isEmpty()) {
			Bullet pendingBullet = pendingBullets.removeLast();
			System.out.println("Fire " + pendingBullet);
			this.addBullet();
		}
	}

	@Override
	public void triggerDown(PlayerMP player, ItemStack itemStack) {
		this.itemStack = itemStack;
		this.addBullet();
	}

	@Override
	public void triggerUp() {
		;
	}

	private void addBullet() {
		this.pendingBullets.addFirst(((ItemGun) this.itemStack.getItem()).createBullet(this.itemStack));
	}

}
