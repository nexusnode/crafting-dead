package com.craftingdead.mod.client;

import com.craftingdead.mod.CommonConfig;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.IModDist;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.player.ClientPlayer;
import com.craftingdead.mod.capability.player.DefaultPlayer;
import com.craftingdead.mod.capability.triggerable.GunController;
import com.craftingdead.mod.client.DiscordPresence.GameState;
import com.craftingdead.mod.client.animation.AnimationManager;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.craftingdead.mod.client.crosshair.CrosshairManager;
import com.craftingdead.mod.client.gui.IngameGui;
import com.craftingdead.mod.client.renderer.entity.AdvancedZombieRenderer;
import com.craftingdead.mod.client.renderer.entity.CorpseRenderer;
import com.craftingdead.mod.client.renderer.entity.MedicalCrateRenderer;
import com.craftingdead.mod.client.renderer.entity.MilitaryCrateRenderer;
import com.craftingdead.mod.client.renderer.entity.SupplyCrateRenderer;
import com.craftingdead.mod.client.renderer.player.LivingRendererMod;
import com.craftingdead.mod.entity.CorpseEntity;
import com.craftingdead.mod.entity.MedicalCrateEntity;
import com.craftingdead.mod.entity.MilitaryCrateEntity;
import com.craftingdead.mod.entity.SupplyCrateEntity;
import com.craftingdead.mod.entity.monster.AdvancedZombieEntity;
import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.masterserver.handshake.packet.HandshakePacket;
import com.craftingdead.mod.masterserver.modclientlogin.ModClientLoginSession;
import com.craftingdead.mod.masterserver.modclientlogin.packet.ModClientLoginPacket;
import com.craftingdead.network.pipeline.NetworkManager;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.BipedModel.ArmPose;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class ClientDist implements IModDist {

  /**
   * Using Reflection to set the swimming position, which is then redefined to crawl.
   */
  private static final Method setPose = ObfuscationReflectionHelper
      .findMethod(Entity.class, "func_213301_b", (Class[]) new Class[]{Pose.class});


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

  private IngameGui guiIngame;

  public ClientDist() {
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MinecraftForge.EVENT_BUS.register(this);

    ((IReloadableResourceManager) minecraft.getResourceManager())
        .addReloadListener(this.crosshairManager);

    this.guiIngame = new IngameGui(minecraft, this, CrosshairManager.DEFAULT_CROSSHAIR);
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

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientSetup(FMLClientSetupEvent event) {
    ClientRegistry.registerKeyBinding(TOGGLE_FIRE_MODE);
    ClientRegistry.registerKeyBinding(RELOAD);

    OBJLoader.INSTANCE.addDomain(CraftingDead.ID);
    OBJLoader.INSTANCE.onResourceManagerReload(Minecraft.getInstance().getResourceManager());

    RenderingRegistry.registerEntityRenderingHandler(CorpseEntity.class, CorpseRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(AdvancedZombieEntity.class, AdvancedZombieRenderer::new);

    /*
      Substitution of a class which is responsible for rendering
     */
    RenderingRegistry.registerEntityRenderingHandler(AbstractClientPlayerEntity.class,
        LivingRendererMod::new);

    RenderingRegistry.registerEntityRenderingHandler(MedicalCrateEntity.class,
        MedicalCrateRenderer::new);

    RenderingRegistry.registerEntityRenderingHandler(MilitaryCrateEntity.class,
        MilitaryCrateRenderer::new);

    RenderingRegistry.registerEntityRenderingHandler(SupplyCrateEntity.class,
        SupplyCrateRenderer::new);

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
  public void handleMouseInput(InputEvent.MouseInputEvent event) {
    if (minecraft.getConnection() != null && minecraft.currentScreen == null) {
      if (event.getButton() == minecraft.gameSettings.keyBindAttack.getKey().getKeyCode()) {
        boolean press = event.getAction() == GLFW.GLFW_PRESS;
        this.getPlayer().ifPresent((player) -> {
          player.setTriggerPressed(press);
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
  }

  @SubscribeEvent
  public void handleRenderLiving(RenderLivingEvent.Pre<?, BipedModel<?>> event) {

    if (event.getEntity() instanceof ClientPlayerEntity && !LivingRendererMod.class
        .isAssignableFrom(event.getRenderer().getClass())) {
      if (!event.getEntity().isSneaking()) {
        event.setCanceled(true);

        LivingRendererMod render = new LivingRendererMod(
            Minecraft.getInstance().getRenderManager());

        render.doRender((ClientPlayerEntity) event.getEntity(), event.getX(), event.getY(),
            event.getZ(), ((AbstractClientPlayerEntity) event.getEntity()).rotationYaw,
            event.getPartialRenderTick());
      }
    }

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
        this.guiIngame
            .renderGameOverlay(event.getPartialTicks(), event.getWindow().getScaledWidth(),
                event.getWindow().getScaledHeight());
        break;
      case CROSSHAIRS:
        this.getPlayer().ifPresent((player) -> {
          player
              .getEntity()
              .getHeldItemMainhand()
              .getCapability(ModCapabilities.AIMABLE)
              .ifPresent((aimable) -> {
                event.setCanceled(true);
                this.guiIngame
                    .renderCrosshairs(aimable.getAccuracy(), event.getPartialTicks(),
                        event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
              });
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
      Supplier<GunAnimation> animation =
          gunController.getItem().getAnimations().get(GunAnimation.Type.SHOOT);
      if (animation != null && animation.get() != null) {
        AnimationManager animationManager = this.animationManager;
        animationManager.clear(event.getItemStack());
        animationManager.setNextGunAnimation(event.getItemStack(), animation.get());
      }
    }
  }

  /*
   * Using Reflection to set the swimming position, which is then redefined to crawl.
   */
  @SubscribeEvent
  public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (ClientDist.CROUCH.isKeyDown()) {
        try {
          setPose.invoke(event.player, Pose.SWIMMING);
        } catch (IllegalAccessException | InvocationTargetException e) {
          System.out.println("Error using reflection to crawl.");
        }
      }
    }
  }

  /*
   * Responsible for climbing walls.
   */
  @SubscribeEvent(priority = EventPriority.LOW)
  public void tickClient(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.START) {
      PlayerEntity player = Minecraft.getInstance().player;
      Entity camera = Minecraft.getInstance().getRenderViewEntity();

      if (player == null || camera == null) {
        return;
      }

      Minecraft mc = Minecraft.getInstance();
      if (mc.currentScreen == null) {
        if (Minecraft.getInstance().gameSettings.keyBindSprint.isKeyDown()) {

          float grabDist = 0.75F;

          Vec3d lookVec = player.getLookVec().scale(grabDist);

          Vec3d lookVecBehind = player.getLookVec().scale(-0.25F);

          double height = 0.2D;
          /*
           * STANDART Height player = 1.8 , Snake = 1.5 , low 0.6
           *
           * lifting height
           */
          double yScanRangeAir = player.getHeight() + height;

          double yScanRangeSolid = 0.4D;

          double yScanRes = 0.2D;
          double yAirSize = 0.25D;
          double xzSize = 0.3D;
          double xzSizeBehind = 0.1D;

          /*
           * Player Box
           */
          AxisAlignedBB playerAABB = player.getBoundingBox();

          /*
           * Player Box
           */
          AxisAlignedBB spotForHandsAir = new AxisAlignedBB(player.posX + lookVec.x,
              playerAABB.minY, player.posZ + lookVec.z,
              player.posX + lookVec.x, playerAABB.minY, player.posZ + lookVec.z)
              .grow(xzSize, yAirSize, xzSize);

          /*
           * Boxing the size of the user who checks whether it is still necessary to go up
           */
          AxisAlignedBB behindUnderFeet = new AxisAlignedBB(player.posX + lookVecBehind.x,
              playerAABB.minY, player.posZ + lookVecBehind.z,
              player.posX + lookVecBehind.x, playerAABB.minY, player.posZ + lookVecBehind.z)
              .grow(xzSizeBehind, xzSizeBehind, xzSizeBehind);

          boolean foundGrabbableSpot = false;

          /*
           * Check for contact between the player and the wall in front of him.
           * Проверяет, что пользователь не наземле , и колизии
           */
          if (!player.onGround && player.world.isCollisionBoxesEmpty(player, behindUnderFeet)) {

            for (double y = yScanRangeAir; y > 0.25D && !foundGrabbableSpot; y -= yScanRes) {
              /*
               * Initial check to see if movement can begin at all.
               * Начальная проверка можно ли начать движение. И есть ли колизия с блоком который выше игрока.
               */
              if (player.world.isCollisionBoxesEmpty(player, spotForHandsAir.offset(0, y, 0))) {

                for (double y2 = 0; y2 < yScanRangeSolid; y2 += yScanRes) {
                  AxisAlignedBB axisAlignedBB = spotForHandsAir
                      .offset(0, y - (yAirSize * 1D) - y2, 0);
                  if (!player.world.isCollisionBoxesEmpty(player, axisAlignedBB)
                      && axisAlignedBB.minY + 0.15D > playerAABB.minY) {
                    foundGrabbableSpot = true;
                    break;
                  }
                }
              }
            }
          }
          if (foundGrabbableSpot) {
            float climbSpeed = 0.08F;
            if (player.getMotion().y < climbSpeed) {
              Vec3d speed = player.getMotion();
              player.setMotion(speed.x, climbSpeed, speed.z);
            }
          }
        }
      }
    }
  }


  @SubscribeEvent
  public static void onPreTextureStitch(TextureStitchEvent.Pre event) {
    event.addSprite(
        ResourceLocation.tryCreate("craftingdead:textures/block/yellow")
    );
  }
}
