package com.craftingdead.discordrpc;

import java.lang.reflect.Method;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;

public class DiscordRPCMapper implements FunctionMapper {

	private static final String METHOD_PREFIX = "discord";

	@Override
	public String getFunctionName(NativeLibrary library, Method method) {
		String name = method.getName();
		name = new StringBuffer(name).insert(name.indexOf(METHOD_PREFIX) + METHOD_PREFIX.length(), "_")
				.replace(0, 1, Character.toString(Character.toUpperCase(name.charAt(0)))).toString();
		return name;
	}

}
