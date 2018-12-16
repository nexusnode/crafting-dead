package com.craftingdead.mod;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.config.Config;

@Config(modid = CraftingDead.MOD_ID, category = "")
public class ModConfig {

	@Config.LangKey("craftingdead.config.client")
	public static Client client = new Client();

	public static class Client {

		public static final KeyBinding KEY_BIND_TOGGLE_FIRE_MODE = new KeyBinding("key.toggle_fire_mode", Keyboard.KEY_F,
				"key.categories.gameplay");

		@Config.LangKey("craftingdead.config.client.apply_branding")
		@Config.RequiresMcRestart
		public boolean applyBranding = false;

		@Config.LangKey("craftingdead.config.client.enable_discord_rpc")
		@Config.RequiresMcRestart
		public boolean enableDiscordRpc = true;

	}

}
