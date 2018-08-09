package com.craftingdead.asm;

public class Callback {
	
	private final CallbackType callbackType;
	private final String callbackClass;
	private final String callbackMethod;

	public static enum CallbackType {
		REDIRECT, EVENT;
	}

	public Callback(CallbackType callbackType, String callbackMethod, String callbackClass) {
		this.callbackType = callbackType;
		this.callbackClass = callbackClass.replace('.', '/');
		this.callbackMethod = callbackMethod;
	}

	public CallbackType getType() {
		return this.callbackType;
	}

	public String getCallbackClass() {
		return this.callbackClass;
	}

	public String getCallbackMethod() {
		return this.callbackMethod;
	}
	
	@Override
	public String toString() {
		return this.callbackMethod;
	}

	@Override
	public boolean equals(Object other) {
		if ((other == null) || (!(other instanceof Callback))) {
			return false;
		}
		Callback callback = (Callback) other;
		return (callback.callbackClass.equals(this.callbackClass))
				&& (callback.callbackMethod.equals(this.callbackMethod))
				&& (callback.callbackType == this.callbackType);
	}
	
}
