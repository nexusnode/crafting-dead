package com.craftingdead.mod;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class CommonConfig {

	public static final Client CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;
	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static class Client {

		public final BooleanValue applyBranding;

		public final BooleanValue enableDiscordRpc;

		public final BooleanValue displayBlood;

		Client(ForgeConfigSpec.Builder builder) {
			builder.push("client");
			{
				this.applyBranding = builder //
						.translation("options.craftingdead.client.apply_branding") //
						.define("applyBranding", false);
				this.enableDiscordRpc = builder //
						.translation("options.craftingdead.client.enable_discord_rpc") //
						.define("enableDiscordRpc", true);
				this.displayBlood = builder //
						.translation("options.craftingdead.client.display_blood") //
						.define("displayBlood", true);
			}
			builder.pop();
		}
	}
}
