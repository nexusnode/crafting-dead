package com.craftingdead.mod.client;

import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.mod.CommonConfig;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.IModDist;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.player.ClientPlayer;
import com.craftingdead.mod.capability.player.DefaultPlayer;
import com.craftingdead.mod.capability.player.IPlayer;
import com.craftingdead.mod.client.DiscordPresence.GameState;
import com.craftingdead.mod.client.crosshair.CrosshairManager;
import com.craftingdead.mod.client.gui.IngameGui;
import com.craftingdead.mod.client.gui.screen.inventory.ModInventoryScreen;
import com.craftingdead.mod.client.gui.transition.TransitionManager;
import com.craftingdead.mod.client.gui.transition.Transitions;
import com.craftingdead.mod.client.model.ComplexModel;
import com.craftingdead.mod.client.model.GunModel;
import com.craftingdead.mod.client.renderer.entity.AdvancedZombieRenderer;
import com.craftingdead.mod.client.renderer.entity.CorpseRenderer;
import com.craftingdead.mod.client.renderer.entity.GrenadeRenderer;
import com.craftingdead.mod.client.renderer.entity.SupplyDropRenderer;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.inventory.container.ModContainerTypes;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.BipedModel.ArmPose;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientDist implements IModDist {

  public static final KeyBinding RELOAD =
      new KeyBinding("key.reload", GLFW.GLFW_KEY_R, "key.categories.gameplay");
  public static final KeyBinding TOGGLE_FIRE_MODE =
      new KeyBinding("key.toggle_fire_mode", GLFW.GLFW_KEY_F, "key.categories.gameplay");
  public static final KeyBinding CROUCH =
      new KeyBinding("key.crouch", GLFW.GLFW_KEY_C, "key.categories.gameplay");

  private static final Logger logger = LogManager.getLogger();

  private static final Minecraft minecraft = Minecraft.getInstance();

  private final CrosshairManager crosshairManager = new CrosshairManager();

  private final RecoilHelper recoilHelper = new RecoilHelper();

  private IngameGui ingameGui;

  private final TransitionManager transitionManager =
      new TransitionManager(minecraft, Transitions.GROW);

  public ClientDist() {
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MinecraftForge.EVENT_BUS.register(this);

    ((IReloadableResourceManager) minecraft.getResourceManager())
        .addReloadListener(this.crosshairManager);

    this.ingameGui = new IngameGui(minecraft, this, CrosshairManager.DEFAULT_CROSSHAIR);
  }

  public CrosshairManager getCrosshairManager() {
    return this.crosshairManager;
  }

  public LazyOptional<ClientPlayer> getPlayer() {
    return minecraft.player != null
        ? minecraft.player.getCapability(ModCapabilities.PLAYER, null).cast()
        : LazyOptional.empty();
  }

  public IngameGui getIngameGui() {
    return this.ingameGui;
  }

  public RecoilHelper getRecoilHelper() {
    return this.recoilHelper;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientSetup(FMLClientSetupEvent event) {
    ScreenManager.registerFactory(ModContainerTypes.PLAYER.get(), ModInventoryScreen::new);

    ModelLoaderRegistry
        .registerLoader(new ResourceLocation(CraftingDead.ID, "gun"), GunModel.Loader.INSTANCE);
    ModelLoaderRegistry
        .registerLoader(new ResourceLocation(CraftingDead.ID, "complex"),
            ComplexModel.Loader.INSTANCE);

    ClientRegistry.registerKeyBinding(TOGGLE_FIRE_MODE);
    ClientRegistry.registerKeyBinding(RELOAD);
    ClientRegistry.registerKeyBinding(CROUCH);

    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.corpse, CorpseRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.corpse, CorpseRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.advancedZombie, AdvancedZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.fastZombie, AdvancedZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.tankZombie, AdvancedZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.weakZombie, AdvancedZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.supplyDrop, SupplyDropRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.grenade, GrenadeRenderer::new);


    // GLFW code needs to run on main thread
    minecraft.enqueue(() -> {
      if (CommonConfig.clientConfig.applyBranding.get()) {
        StartupMessageManager.addModMessage("Applying branding");
        GLFW
            .glfwSetWindowTitle(minecraft.getWindow().getHandle(),
                String.format("%s %s", CraftingDead.DISPLAY_NAME, CraftingDead.VERSION));
        try {
          InputStream inputstream = minecraft
              .getResourceManager()
              .getResource(
                  new ResourceLocation(CraftingDead.ID, "textures/gui/icons/icon_16x16.png"))
              .getInputStream();
          InputStream inputstream1 = minecraft
              .getResourceManager()
              .getResource(
                  new ResourceLocation(CraftingDead.ID, "textures/gui/icons/icon_32x32.png"))
              .getInputStream();
          minecraft.getWindow().setWindowIcon(inputstream, inputstream1);
        } catch (IOException e) {
          logger.error("Couldn't set icon", e);
        }
      }
    });
  }

  @SubscribeEvent
  public void handleLoadComplete(FMLLoadCompleteEvent event) {
    if (CommonConfig.clientConfig.enableDiscordRpc.get()) {
      StartupMessageManager.addModMessage("Loading Discord integration");
      DiscordPresence.initialize("475405055302828034");
    }
    DiscordPresence.updateState(GameState.IDLE, this);
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleRawMouse(InputEvent.RawMouseEvent event) {
    if (minecraft.getConnection() != null && minecraft.currentScreen == null) {
      if (minecraft.gameSettings.keyBindAttack.matchesMouseKey(event.getButton())) {
        boolean triggerPressed = event.getAction() == GLFW.GLFW_PRESS;
        this.getPlayer().ifPresent(player -> {
          if (player.getEntity().getHeldItemMainhand().getItem() instanceof GunItem) {
            event.setCanceled(true);
            player.setTriggerPressed(triggerPressed, true);
          }
        });
      }
    }
  }

  @SubscribeEvent
  public void handleAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof ClientPlayerEntity) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "player"),
              new SerializableProvider<>(new ClientPlayer((ClientPlayerEntity) event.getObject()),
                  ModCapabilities.PLAYER));
    } else if (event.getObject() instanceof AbstractClientPlayerEntity) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "player"),
              new SerializableProvider<>(
                  new DefaultPlayer<>((AbstractClientPlayerEntity) event.getObject()),
                  ModCapabilities.PLAYER));
    }
  }

  @SubscribeEvent
  public void handleEntityJoinWorld(EntityJoinWorldEvent event) {
    ClientPlayNetHandler connection = minecraft.getConnection();
    if (connection != null && connection.getNetworkManager().isChannelOpen()) {
      if (minecraft.getIntegratedServer() != null && !minecraft.getIntegratedServer().getPublic()) {
        DiscordPresence.updateState(GameState.SINGLEPLAYER, this);
      } else if (minecraft.isConnectedToRealms()) {
        DiscordPresence.updateState(GameState.REALMS, this);
      } else if (minecraft.getIntegratedServer() == null
          && (minecraft.getCurrentServerData() == null
              || !minecraft.getCurrentServerData().isOnLAN())) {
        DiscordPresence.updateState(GameState.MULTIPLAYER, this);
      } else {
        DiscordPresence.updateState(GameState.LAN, this);
      }
    }
  }

  @SubscribeEvent
  public void handleGuiOpen(GuiOpenEvent event) {

    DiscordPresence.updateState(GameState.IDLE, this);
    if (event.getGui() instanceof MainMenuScreen) {
      // event.setGui(new StartScreen());
    }
  }

  @SubscribeEvent
  public void handleRenderLiving(RenderLivingEvent.Pre<?, BipedModel<?>> event) {
    ItemStack itemStack = event.getEntity().getHeldItemMainhand();
    if (event.getRenderer().getEntityModel() instanceof BipedModel
        && itemStack.getItem() instanceof GunItem) {
      BipedModel<?> model = event.getRenderer().getEntityModel();
      switch (event.getEntity().getPrimaryHand()) {
        case LEFT:
          model.leftArmPose = ArmPose.BOW_AND_ARROW;
          break;
        case RIGHT:
          model.rightArmPose = ArmPose.BOW_AND_ARROW;
          break;
        default:
          break;
      }
    }
  }

  @SubscribeEvent
  public void handleRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
    switch (event.getType()) {
      case ALL:
        this.ingameGui
            .renderGameOverlay(event.getPartialTicks(), event.getWindow().getScaledWidth(),
                event.getWindow().getScaledHeight());
        break;
      case CROSSHAIRS:
        this.getPlayer().ifPresent(player -> {
          ClientPlayerEntity playerEntity = player.getEntity();
          event.setCanceled(this.ingameGui.getAction().isActive(playerEntity));
          if (!event.isCanceled()) {
            ItemStack heldStack = playerEntity.getHeldItemMainhand();
            if (heldStack.getItem() instanceof GunItem) {
              GunItem gunItem = (GunItem) heldStack.getItem();
              event.setCanceled(true);
              this.ingameGui
                  .renderCrosshairs(gunItem.getAccuracy(heldStack, playerEntity),
                      event.getPartialTicks(), event.getWindow().getScaledWidth(),
                      event.getWindow().getScaledHeight());
            }
          }
        });
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleDrawScreenPre(DrawScreenEvent.Pre event) {
    event
        .setCanceled(this.transitionManager
            .checkDrawTransition(event.getMouseX(), event.getMouseY(),
                event.getRenderPartialTicks(), event.getGui()));
  }

  @SubscribeEvent
  public void handeFOVUpdate(FOVUpdateEvent event) {
    ItemStack heldStack = minecraft.player.getHeldItemMainhand();
    if (this.getPlayer().map(IPlayer::isAiming).orElse(false)
        && heldStack.getItem() instanceof GunItem) {
      event
          .setNewfov(event.getFov() * ((GunItem) heldStack.getItem())
              .getAttachmentMultiplier(heldStack, AttachmentItem.MultiplierType.FOV));
    }
  }

  @SubscribeEvent
  public void handleRenderTick(TickEvent.RenderTickEvent event) {
    switch (event.phase) {
      case START:
        if (minecraft.player != null) {
          Vec2f cameraVelocity = this.recoilHelper.update();
          minecraft.player.rotateTowards(cameraVelocity.x, cameraVelocity.y);
        }
        break;
      default:
        break;
    }
  }
}
