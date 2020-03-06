package com.craftingdead.mod.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
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
import com.craftingdead.mod.client.model.PerspectiveAwareModel;
import com.craftingdead.mod.client.model.EquipableModel;
import com.craftingdead.mod.client.model.GunModel;
import com.craftingdead.mod.client.renderer.entity.AdvancedZombieRenderer;
import com.craftingdead.mod.client.renderer.entity.CorpseRenderer;
import com.craftingdead.mod.client.renderer.entity.GrenadeRenderer;
import com.craftingdead.mod.client.renderer.entity.SupplyDropRenderer;
import com.craftingdead.mod.client.renderer.entity.player.layer.ExtraSkinsLayer;
import com.craftingdead.mod.client.renderer.entity.player.FirstPersonRenderer;
import com.craftingdead.mod.client.renderer.entity.player.IHasExtraSkin;
import com.craftingdead.mod.client.renderer.entity.player.ExtraSkinProviders;
import com.craftingdead.mod.client.renderer.entity.player.layer.EquipableModelLayer;
import com.craftingdead.mod.client.renderer.entity.player.layer.EquippedMeleeLayer;
import com.craftingdead.mod.client.renderer.texture.PaintSpriteUploader;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.inventory.container.ModContainerTypes;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.BipedModel.ArmPose;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
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
      new KeyBinding("key.toggle_fire_mode", GLFW.GLFW_KEY_M, "key.categories.gameplay");
  public static final KeyBinding CROUCH =
      new KeyBinding("key.crouch", GLFW.GLFW_KEY_BACKSLASH, "key.categories.gameplay");
  public static final KeyBinding OPEN_PLAYER_CONTAINER =
      new KeyBinding("key.player", GLFW.GLFW_KEY_X, "key.categories.inventory");

  private static final Logger logger = LogManager.getLogger();

  private static final Minecraft minecraft = Minecraft.getInstance();

  /**
   * Random.
   */
  private static final Random random = new Random();

  private final CrosshairManager crosshairManager = new CrosshairManager();

  /**
   * Current camera velocity.
   */
  private Vec2f rotationVelocity = Vec2f.ZERO;

  /**
   * Camera velocity of last tick.
   */
  private Vec2f prevRotationVelocity = this.rotationVelocity;

  private long rollStartTime = 0;

  private float roll;

  private IngameGui ingameGui;

  private final TransitionManager transitionManager =
      new TransitionManager(minecraft, Transitions.GROW);

  private PaintSpriteUploader paintSpriteUploader;

  private ExtraSkinProviders extraSkinTextureProviders = new ExtraSkinProviders();

  public ClientDist() {
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MinecraftForge.EVENT_BUS.register(this);

    ((IReloadableResourceManager) minecraft.getResourceManager())
        .addReloadListener(this.crosshairManager);

    this.ingameGui = new IngameGui(minecraft, this, CrosshairManager.DEFAULT_CROSSHAIR);
  }

  public void joltCamera(float accuracy) {
    float amount = ((1.0F - accuracy) * 100) / 2.5F;
    float randomAmount = random.nextBoolean() ? amount : -amount;
    this.rotationVelocity =
        new Vec2f(this.rotationVelocity.x + randomAmount, this.rotationVelocity.y - amount);
    this.rollStartTime = Util.milliTime();
    this.roll = randomAmount;
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

  public PaintSpriteUploader getPaintSpriteUploader() {
    return this.paintSpriteUploader;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientSetup(FMLClientSetupEvent event) {
    this.paintSpriteUploader = new PaintSpriteUploader(minecraft.getTextureManager());

    ((IReloadableResourceManager) minecraft.getResourceManager())
        .addReloadListener(this.paintSpriteUploader);

    ScreenManager.registerFactory(ModContainerTypes.PLAYER.get(), ModInventoryScreen::new);

    ModelLoaderRegistry
        .registerLoader(new ResourceLocation(CraftingDead.ID, "gun"), GunModel.Loader.INSTANCE);
    ModelLoaderRegistry
        .registerLoader(new ResourceLocation(CraftingDead.ID, "perspective"),
            PerspectiveAwareModel.Loader.INSTANCE);
    ModelLoaderRegistry
        .registerLoader(new ResourceLocation(CraftingDead.ID, "equipable"),
            EquipableModel.Loader.INSTANCE);

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

    StartupMessageManager.addModMessage("Setup CustomFirstPersonRenderer before setting up layers");

    try {
      FirstPersonRenderer.inject(this.extraSkinTextureProviders);
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize CustomFirstPersonRenderer", e);
    }

    StartupMessageManager.addModMessage("Setup CD layers");

    this.extraSkinTextureProviders.registerExtraSkinProvider((player) -> {
      ItemStack clothingStack = player.getInventory().getStackInSlot(InventorySlotType.CLOTHING.getIndex());

      // Check if the ItemStack has a skin texture
      if (clothingStack.getItem() instanceof IHasExtraSkin) {
        return Optional.of((IHasExtraSkin) clothingStack.getItem());
      }

      // Item does not has a skin texture
      return Optional.empty();
    });

    this.registerPlayerLayer(renderer -> new ExtraSkinsLayer(renderer, this.extraSkinTextureProviders));
    this.registerPlayerLayer(EquippedMeleeLayer::new);
    this.registerPlayerLayer(renderer -> new EquipableModelLayer.Builder()
        .withRenderer(renderer)
        .withItemStackGetter(
            player -> player.getInventory().getStackInSlot(InventorySlotType.VEST.getIndex())
        )
        .withCrouchingOrientation(true)
        .build());
    this.registerPlayerLayer(renderer -> new EquipableModelLayer.Builder()
        .withRenderer(renderer)
        .withItemStackGetter(
            player -> player.getInventory().getStackInSlot(InventorySlotType.HAT.getIndex())
        )
        .withHeadOrientation(true)

        // Inverts X and Y rotation. This is from Mojang, based on HeadLayer.class.
        // TODO Find a reason to not remove this line. Also, if you remove it, you will
        // need to change the json file of every helmet since the scale affects positions.
        .withArbitraryTransformation(matrix -> matrix.scale(-1F, -1F, 1F))

        .build());
    this.registerPlayerLayer(renderer -> new EquipableModelLayer.Builder()
        .withRenderer(renderer)
        .withItemStackGetter(
            player -> player.getInventory().getStackInSlot(InventorySlotType.GUN.getIndex())
        )
        .withCrouchingOrientation(true)
        .build());
    this.registerPlayerLayer(renderer -> new EquipableModelLayer.Builder()
        .withRenderer(renderer)
        .withItemStackGetter(
            player -> player.getInventory().getStackInSlot(InventorySlotType.BACKPACK.getIndex())
        )
        .withCrouchingOrientation(true)
        .build());

    // GLFW code needs to run on main thread
    minecraft.enqueue(() -> {
      if (CommonConfig.clientConfig.applyBranding.get()) {
        StartupMessageManager.addModMessage("Applying branding");
        GLFW
            .glfwSetWindowTitle(minecraft.getWindow().getHandle(),
                String.format("%s %s", CraftingDead.DISPLAY_NAME, CraftingDead.VERSION));
        try {
          InputStream smallIcon = minecraft
              .getResourceManager()
              .getResource(
                  new ResourceLocation(CraftingDead.ID, "textures/gui/icons/icon_16x16.png"))
              .getInputStream();
          InputStream mediumIcon = minecraft
              .getResourceManager()
              .getResource(
                  new ResourceLocation(CraftingDead.ID, "textures/gui/icons/icon_32x32.png"))
              .getInputStream();
          minecraft.getWindow().setWindowIcon(smallIcon, mediumIcon);
        } catch (IOException e) {
          logger.error("Couldn't set icon", e);
        }
      }
    });
  }

  /**
   * Registers a layer into {@link PlayerRenderer}. Can be used normally during
   * {@link FMLClientSetupEvent}.
   *
   * @param function {@link Function} with a {@link PlayerRenderer} as input and a
   *        {@link LayerRenderer} as output.
   */
  public void registerPlayerLayer(
      Function<PlayerRenderer, LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> function) {
    // A little dirty way, blame Mojang
    minecraft.getRenderManager().getSkinMap().forEach((skin, renderer) -> {
      renderer.addLayer(function.apply(renderer));
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
  public void handleClientTick(TickEvent.ClientTickEvent event) {
    switch (event.phase) {
      case START:
        if (minecraft.loadingGui == null
            && (minecraft.currentScreen == null || minecraft.currentScreen.passEvents)) {
          while (TOGGLE_FIRE_MODE.isPressed()) {
            minecraft.player
                .getCapability(ModCapabilities.PLAYER)
                .ifPresent(player -> player.toggleFireMode(true));
          }
          while (OPEN_PLAYER_CONTAINER.isPressed()) {
            minecraft.player
                .getCapability(ModCapabilities.PLAYER)
                .ifPresent(IPlayer::openPlayerContainer);
          }
        }
        break;
      default:
        break;
    }
  }

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
  public void handleClientPlayerLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event) {
    if (event.getPlayer() == minecraft.player) {
      ClientPlayNetHandler connection = minecraft.getConnection();
      if (connection != null && connection.getNetworkManager().isChannelOpen()) {
        if (minecraft.getIntegratedServer() != null
            && !minecraft.getIntegratedServer().getPublic()) {
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
  }

  @SubscribeEvent
  public void handleClientPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
    DiscordPresence.updateState(GameState.IDLE, this);
  }

  @SubscribeEvent
  public void handleRenderLiving(RenderLivingEvent.Pre<?, BipedModel<?>> event) {

    /*
     *  Renders the ArmPose as BOW_AND_ARROW if the living entity is holding a gun.
     */

    ItemStack heldStack = event.getEntity().getHeldItemMainhand();
    if (event.getRenderer().getEntityModel() instanceof BipedModel
        && heldStack.getItem() instanceof GunItem) {
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
          ItemStack heldStack = playerEntity.getHeldItemMainhand();

          event
              .setCanceled(heldStack
                  .getCapability(ModCapabilities.ACTION)
                  .map(action -> action.isActive(playerEntity))
                  .orElse(false) || player.isAiming());

          if (!event.isCanceled()) {
            heldStack.getCapability(ModCapabilities.GUN_CONTROLLER).ifPresent(gunController -> {
              event.setCanceled(true);
              this.ingameGui
                  .renderCrosshairs(gunController.getAccuracy(playerEntity, heldStack),
                      event.getPartialTicks(), event.getWindow().getScaledWidth(),
                      event.getWindow().getScaledHeight());
            });
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
  public void handleCameraSetup(EntityViewRenderEvent.CameraSetup event) {
    float pct = MathHelper.clamp((Util.milliTime() - this.rollStartTime) / 1000.0F * 5, 0.0F, 1.0F);
    float roll = (float) Math.sin(Math.toRadians(180 * pct)) * this.roll / 20;
    if (pct == 1.0F) {
      this.roll = 0;
    }
    event.setRoll(roll);
  }

  @SubscribeEvent
  public void handeFOVUpdate(FOVUpdateEvent event) {
    ItemStack heldStack = minecraft.player.getHeldItemMainhand();
    if (this.getPlayer().map(IPlayer::isAiming).orElse(false)) {
      heldStack.getCapability(ModCapabilities.GUN_CONTROLLER).ifPresent(gunController -> {
        event
            .setNewfov(event.getFov()
                * gunController.getAttachmentMultiplier(AttachmentItem.MultiplierType.FOV));
      });
    }
  }

  @SubscribeEvent
  public void handleRenderTick(TickEvent.RenderTickEvent event) {
    switch (event.phase) {
      case START:
        if (minecraft.player != null) {
          float smoothYaw =
              MathHelper.lerp(0.25F, this.prevRotationVelocity.x, this.rotationVelocity.x);
          float smoothPitch =
              MathHelper.lerp(0.25F, this.prevRotationVelocity.y, this.rotationVelocity.y);
          this.rotationVelocity = Vec2f.ZERO;
          this.prevRotationVelocity = new Vec2f(smoothYaw, smoothPitch);
          minecraft.player.rotateTowards(smoothYaw, smoothPitch);
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleTextureStitch(TextureStitchEvent.Pre event) {
    event.addSprite(new ResourceLocation(CraftingDead.ID, "paint/vulcan_paint"));
  }
}
