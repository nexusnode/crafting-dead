package com.craftingdead.mod.item;

import java.util.function.Supplier;

public interface FireMode {

	boolean canShoot(boolean triggerPressed);

	public static enum Modes implements Supplier<FireMode> {

		AUTO {
			@Override
			public FireMode get() {
				return new FireMode() {
					@Override
					public boolean canShoot(boolean triggerPressed) {
						return triggerPressed;
					}
				};
			}
		},
		SEMI {
			@Override
			public FireMode get() {
				return new FireMode() {

					private boolean lastTriggerPressed;

					@Override
					public boolean canShoot(boolean triggerPressed) {
						boolean result = triggerPressed && !this.lastTriggerPressed;
						this.lastTriggerPressed = triggerPressed;
						return result;
					}
				};
			}
		};

	}

}
