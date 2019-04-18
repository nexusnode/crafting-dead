package com.craftingdead.mod;

import net.minecraftforge.common.config.Config;

@Config(modid = CraftingDead.MOD_ID, category = "")
public class ModConfig {

	@Config.LangKey("craftingdead.config.client")
	public static Client client = new Client();

	public static class Client {

		@Config.LangKey("craftingdead.config.client.apply_branding")
		@Config.RequiresMcRestart
		public boolean applyBranding = false;

		@Config.LangKey("craftingdead.config.client.enable_discord_rpc")
		@Config.RequiresMcRestart
		public boolean enableDiscordRpc = true;
		
		@Config.LangKey("craftingdead.config.client.display_blood")
		public boolean displayBlood = true;

	}

}
