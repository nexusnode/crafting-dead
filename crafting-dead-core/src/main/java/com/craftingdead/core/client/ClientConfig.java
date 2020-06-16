package com.craftingdead.core.client;

import com.craftingdead.core.client.tutorial.ModTutorialSteps;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

  public final ForgeConfigSpec.BooleanValue applyBranding;

  public final ForgeConfigSpec.BooleanValue enableDiscordRpc;

  public final ForgeConfigSpec.BooleanValue displayBlood;

  public final ForgeConfigSpec.EnumValue<ModTutorialSteps> tutorialStep;

  public ClientConfig(ForgeConfigSpec.Builder builder) {
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
      this.tutorialStep = builder.defineEnum("tutorialStep", ModTutorialSteps.OPEN_INVENTORY);
    }
    builder.pop();
  }
}
