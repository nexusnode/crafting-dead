package com.craftingdead.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.player.ServerPlayer;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.masterserver.net.MasterServerConnector;
import com.craftingdead.mod.net.NetworkChannel;
import com.craftingdead.mod.server.ServerDist;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;

@Mod(CraftingDead.MOD_ID)
public class CraftingDead {

  /**
   * Mod ID.
   */
  public static final String MOD_ID = "craftingdead";

  /**
   * Mod version.
   */
  public static final String MOD_VERSION;

  /**
   * Mod display name.
   */
  public static final String MOD_DISPLAY_NAME;

  /**
   * Logger.
   */
  private static final Logger logger = LogManager.getLogger();

  /**
   * Singleton.
   */
  @Getter
  private static CraftingDead instance;

  /**
   * Mod distribution.
   */

  @Getter
  private final IModDist modDist;

  @Getter
  private final ThreadTaskExecutor<?> mainThreadExecutor;

  /**
   * Master server connector.
   */
  private MasterServerConnector<?, ?> masterServerConnector;

  static {
    MOD_VERSION =
        JarVersionLookupHandler.getImplementationVersion(CraftingDead.class).orElse("mod_version");
    assert MOD_VERSION != null;
    MOD_DISPLAY_NAME =
        JarVersionLookupHandler.getImplementationTitle(CraftingDead.class).orElse("mod_title");
    assert MOD_DISPLAY_NAME != null;
  }

  public CraftingDead() {
    instance = this;
    this.modDist = DistExecutor.runForDist(() -> ClientDist::new, () -> ServerDist::new);
    this.mainThreadExecutor =
        DistExecutor.runForDist(() -> () -> LogicalSidedProvider.WORKQUEUE.get(LogicalSide.CLIENT),
            () -> () -> LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER));
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::setup);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CommonConfig.clientConfigSpec);
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.commonConfigSpec);
  }

  private void setup(FMLCommonSetupEvent event) {
    this.masterServerConnector = this.modDist.getConnectorBuilder().build();
    this.masterServerConnector.start();
    NetworkChannel.loadChannels();
    logger.info("Registering capabilities");
    ModCapabilities.registerCapabilities();
  }

  // ================================================================================
  // Common Forge Events
  // ================================================================================

  @Mod.EventBusSubscriber(modid = CraftingDead.MOD_ID)
  public static class Events {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase == Phase.END) {
        event.player.getCapability(ModCapabilities.PLAYER, null)
            .ifPresent((player) -> player.tick());
        ItemStack itemStack = event.player.getHeldItemMainhand();
        itemStack.getCapability(ModCapabilities.TRIGGERABLE, null)
            .ifPresent((triggerable) -> triggerable.tick(itemStack, event.player));
      }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
      if (!event.isCanceled() && event.getEntity() instanceof PlayerEntity) {
        event.getEntity().getCapability(ModCapabilities.PLAYER, null)
            .ifPresent((player) -> event.setCanceled(player.onDeath(event.getSource())));
      }
      if (!event.isCanceled() && event.getSource().getTrueSource() instanceof PlayerEntity) {
        event.getSource().getTrueSource().getCapability(ModCapabilities.PLAYER, null)
            .ifPresent((player) -> event.setCanceled(player.onKill(event.getEntity())));
      }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
      event.getEntityPlayer().getCapability(ModCapabilities.PLAYER, null).<ServerPlayer>cast()
          .ifPresent((player) -> {
            event.getOriginal().getCapability(ModCapabilities.PLAYER, null).<ServerPlayer>cast()
                .ifPresent((that) -> {
                  player.copyFrom(that, event.isWasDeath());
                });
          });
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof ServerPlayerEntity) {
        ServerPlayer player = new ServerPlayer((ServerPlayerEntity) event.getObject());
        event.addCapability(new ResourceLocation(CraftingDead.MOD_ID, "player"),
            new SerializableProvider<>(player, ModCapabilities.PLAYER));
      }
    }
  }
}
