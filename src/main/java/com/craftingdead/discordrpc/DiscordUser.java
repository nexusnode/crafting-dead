package com.craftingdead.discordrpc;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.ImmutableList;
import com.sun.jna.Structure;

public class DiscordUser extends Structure {

	public String userId;
	public String username;
	public String discriminator;
	public String avatar;

	public DiscordUser(String encoding) {
		setStringEncoding(encoding);
	}

	public DiscordUser() {
		this("UTF-8");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DiscordUser))
			return false;
		DiscordUser that = (DiscordUser) o;
		return Objects.equals(userId, that.userId) && Objects.equals(username, that.username)
				&& Objects.equals(discriminator, that.discriminator) && Objects.equals(avatar, that.avatar);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, username, discriminator, avatar);
	}

	@Override
	protected List<String> getFieldOrder() {
		return ImmutableList.of("userId", "username", "discriminator", "avatar");
	}
}
