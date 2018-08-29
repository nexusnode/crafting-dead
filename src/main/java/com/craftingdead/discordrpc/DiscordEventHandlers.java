package com.craftingdead.discordrpc;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.ImmutableList;
import com.sun.jna.Callback;
import com.sun.jna.Structure;

public class DiscordEventHandlers extends Structure {

	public OnReady ready;
	public OnStatus disconnected;
	public OnStatus errored;
	public OnGameUpdate joinGame;
	public OnGameUpdate spectateGame;
	public OnJoinRequest joinRequest;

	public interface OnReady extends Callback {
		void accept(DiscordUser user);
	}

	public interface OnStatus extends Callback {
		void accept(int errorCode, String message);
	}

	public interface OnGameUpdate extends Callback {
		void accept(String secret);
	}

	public interface OnJoinRequest extends Callback {
		void accept(DiscordUser request);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DiscordEventHandlers))
			return false;
		DiscordEventHandlers that = (DiscordEventHandlers) o;
		return Objects.equals(ready, that.ready) && Objects.equals(disconnected, that.disconnected)
				&& Objects.equals(errored, that.errored) && Objects.equals(joinGame, that.joinGame)
				&& Objects.equals(spectateGame, that.spectateGame) && Objects.equals(joinRequest, that.joinRequest);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ready, disconnected, errored, joinGame, spectateGame, joinRequest);
	}

	@Override
	protected List<String> getFieldOrder() {
		return ImmutableList.of("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
	}
}
