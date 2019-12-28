package com.craftingdead.mod.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.mod.CommonConfig;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.IModDist;
import com.craftingdead.mod.capability.GunController;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.player.ClientPlayer;
import com.craftingdead.mod.capability.player.DefaultPlayer;
import com.craftingdead.mod.client.DiscordPresence.GameState;
import com.craftingdead.mod.client.animation.AnimationManager;
import com.craftingdead.mod.client.animation.IGunAnimation;
import com.craftingdead.mod.client.crosshair.CrosshairManager;
import com.craftingdead.mod.client.gui.IngameGui;
import com.craftingdead.mod.client.gui.transition.TransitionManager;
import com.craftingdead.mod.client.gui.transition.Transitions;
import com.craftingdead.mod.client.model.GunRenderer;
import com.craftingdead.mod.client.model.builtin.BuiltinModel;
import com.craftingdead.mod.client.model.builtin.BuiltinModelLoader;
import com.craftingdead.mod.client.renderer.entity.AdvancedZombieRenderer;
import com.craftingdead.mod.client.renderer.entity.CorpseRenderer;
import com.craftingdead.mod.client.renderer.entity.SupplyDropRenderer;
import com.craftingdead.mod.entity.CorpseEntity;
import com.craftingdead.mod.entity.SupplyDropEntity;
import com.craftingdead.mod.entity.monster.AdvancedZombieEntity;
import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.masterserver.handshake.packet.HandshakePacket;
import com.craftingdead.mod.masterserver.modclientlogin.ModClientLoginSession;
import com.craftingdead.mod.masterserver.modclientlogin.packet.ModClientLoginPacket;
import com.craftingdead.network.pipeline.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.BipedModel.ArmPose;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
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

  private CrosshairManager crosshairManager = new CrosshairManager();

  private AnimationManager animationManager = new AnimationManager();

  private RecoilHelper recoilHelper = new RecoilHelper();

  private IngameGui ingameGui;

  private final TransitionManager transitionManager =
      new TransitionManager(minecraft, Transitions.GROW);

  public ClientDist() {
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MinecraftForge.EVENT_BUS.register(this);

    ((IReloadableResourceManager) minecraft.getResourceManager())
        .addReloadListener(this.crosshairManager);

    this.ingameGui = new IngameGui(minecraft, this, CrosshairManager.DEFAULT_CROSSHAIR);

    ModelLoaderRegistry.registerLoader(BuiltinModelLoader.INSTANCE);
  }

  public CrosshairManager getCrosshairManager() {
    return crosshairManager;
  }

  public AnimationManager getAnimationManager() {
    return animationManager;
  }

  @Override
  public boolean isUsingNativeTransport() {
    return minecraft.gameSettings.isUsingNativeTransport();
  }

  @Override
  public void handleConnect(NetworkManager networkManager) {
    networkManager
        .sendMessage(new HandshakePacket(CraftingDead.MASTER_SERVER_VERSION,
            HandshakePacket.MOD_CLIENT_LOGIN));
    networkManager.setSession(new ModClientLoginSession(networkManager));

    Session session = minecraft.getSession();
    UUID id = session.getProfile().getId();
    String username = session.getUsername();
    networkManager.sendMessage(new ModClientLoginPacket(id, username, CraftingDead.VERSION));
  }

  public LazyOptional<ClientPlayer> getPlayer() {
    return minecraft.player != null
        ? minecraft.player.getCapability(ModCapabilities.PLAYER, null).cast()
        : LazyOptional.empty();
  }

  public IngameGui getIngameGui() {
    return this.ingameGui;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientSetup(FMLClientSetupEvent event) {
    ClientRegistry.registerKeyBinding(TOGGLE_FIRE_MODE);
    ClientRegistry.registerKeyBinding(RELOAD);
    ClientRegistry.registerKeyBinding(CROUCH);

    OBJLoader.INSTANCE.addDomain(CraftingDead.ID);
    OBJLoader.INSTANCE.onResourceManagerReload(Minecraft.getInstance().getResourceManager());

    RenderingRegistry.registerEntityRenderingHandler(CorpseEntity.class, CorpseRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(AdvancedZombieEntity.class, AdvancedZombieRenderer::new);

    RenderingRegistry
        .registerEntityRenderingHandler(SupplyDropEntity.class, SupplyDropRenderer::new);

    // GLFW code needs to run on main thread
    minecraft.enqueue(() -> {
      if (CommonConfig.clientConfig.applyBranding.get()) {
        StartupMessageManager.addModMessage("Applying branding");
        GLFW
            .glfwSetWindowTitle(minecraft.mainWindow.getHandle(),
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
          minecraft.mainWindow.setWindowIcon(inputstream, inputstream1);
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
  public void handleClientTick(TickEvent.ClientTickEvent event) {
    switch (event.phase) {
      case END:
        CraftingDead.getInstance().tickConnection();
        if (minecraft.world != null && !minecraft.isGamePaused()) {
          this.animationManager.tick();

        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleRenderTick(TickEvent.RenderTickEvent event) {
    switch (event.phase) {
      case START:
        if (minecraft.player != null) {
          Vec2f position = this.recoilHelper.update();
          minecraft.player.rotateTowards(position.x, position.y);
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleRawMouse(InputEvent.RawMouseEvent event) {
    if (minecraft.getConnection() != null && minecraft.currentScreen == null) {
      if (event.getButton() == minecraft.gameSettings.keyBindAttack.getKey().getKeyCode()) {
        boolean press = event.getAction() == GLFW.GLFW_PRESS;
        this.getPlayer().ifPresent((player) -> {
          ItemStack heldStack = player.getEntity().getHeldItemMainhand();
          heldStack.getCapability(ModCapabilities.SHOOTABLE, null).ifPresent(shootable -> {
            shootable.setTriggerPressed(heldStack, player.getEntity(), press);
            event.setCanceled(true);
          });
        });
      }
    }
  }

  @SubscribeEvent
  public void handleKeyInput(InputEvent.KeyInputEvent event) {
    if (CROUCH.isPressed()) {
      this.getPlayer().ifPresent(player -> player.getEntity().setPose(Pose.SWIMMING));
    }

    if (RELOAD.isPressed()) {
      this.getPlayer().ifPresent(player -> {
        PlayerEntity playerEntity = player.getEntity();
        ItemStack heldStack = playerEntity.getHeldItemMainhand();
        heldStack
            .getCapability(ModCapabilities.SHOOTABLE)
            .ifPresent(shootable -> shootable.reload(heldStack, playerEntity));
      });
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
    if (minecraft.isIntegratedServerRunning()) {
      DiscordPresence.updateState(GameState.SINGLEPLAYER, this);
    } else {
      ServerData serverData = minecraft.getCurrentServerData();
      DiscordPresence
          .updateState(serverData.isOnLAN() ? GameState.LAN : GameState.MULTIPLAYER, this);
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
    // We don't use RenderPlayerEvent.Pre as it gets called too early resulting in
    // changes we make to the arm pose being overwritten
    ItemStack stack = event.getEntity().getHeldItemMainhand();
    // isAssignableFrom is faster than instanceof
    if (GunItem.class.isAssignableFrom(stack.getItem().getClass())) {
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
        this.getPlayer().ifPresent((player) -> {
          PlayerEntity playerEntity = player.getEntity();
          ItemStack heldStack = playerEntity.getHeldItemMainhand();
          heldStack
              .getCapability(ModCapabilities.ACTION)
              .ifPresent(action -> event.setCanceled(action.isActive(playerEntity, heldStack)));
          if (!event.isCanceled()) {
            heldStack.getCapability(ModCapabilities.AIMABLE).ifPresent(aimable -> {
              event.setCanceled(true);
              this.ingameGui
                  .renderCrosshairs(aimable.getAccuracy(), event.getPartialTicks(),
                      event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
            });
          }
        });
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleGunShootEventPre(GunEvent.ShootEvent.Pre event) {
    // This is event can be called on both the client thread and server thread so
    // check we are on the client first
    if (event.getEntity().world.isRemote()) {
      GunController gunController = event.getController();
      // Only jolt camera if the event is for our own player
      if (event.getEntity() == minecraft.player) {
        this.recoilHelper.jolt(gunController.getAccuracy());
      }
      Supplier<IGunAnimation> animation =
          gunController.getItem().getAnimations().get(IGunAnimation.Type.SHOOT);
      if (animation != null && animation.get() != null) {
        this.animationManager.clear(event.getItemStack());
        this.animationManager.setNextGunAnimation(event.getItemStack(), animation.get());
      }
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
  public void handlePlayerTick(TickEvent.PlayerTickEvent event) {
    switch (event.phase) {
      case END:
        if (ClientDist.CROUCH.isKeyDown()) {

        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleModelRegistry(ModelRegistryEvent event) {
    BuiltinModelLoader.INSTANCE
        .registerModel(new ModelResourceLocation(ModItems.ACR.getId(), "inventory"),
            new BuiltinModel(new GunRenderer(new ResourceLocation(CraftingDead.ID, "item/acr"))));
  }

  @SubscribeEvent
  public void handleModelBakeEvent(ModelBakeEvent event) {
    for (ResourceLocation modelLocation : event.getModelRegistry().keySet()) {
      if (BuiltinModelLoader.INSTANCE.accepts(modelLocation)) {
        IUnbakedModel model;
        try {
          model = BuiltinModelLoader.INSTANCE.loadModel(modelLocation);
        } catch (Exception e) {
          logger.error("Couldn't load model", e);
          continue;
        }

        IBakedModel bakedModel = model
            .bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(),
                new BasicState(model.getDefaultState(), false), DefaultVertexFormats.ITEM);
        event.getModelRegistry().put(modelLocation, bakedModel);
      }
    }
  }
}
